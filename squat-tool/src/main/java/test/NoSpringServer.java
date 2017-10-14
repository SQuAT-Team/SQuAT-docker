package test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.usagemodel.UsageModel;

import com.fasterxml.jackson.databind.deser.SettableAnyProperty;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import io.github.squat_team.AbstractPCMBot;
import io.github.squat_team.json.JSONConverter;
import io.github.squat_team.json.JSONUtils;
import io.github.squat_team.json.JSONification;
import io.github.squat_team.json.UnJSONification;
import io.github.squat_team.model.OptimizationType;
import io.github.squat_team.model.PCMArchitectureInstance;
import io.github.squat_team.model.PCMResult;
import io.github.squat_team.model.PCMScenarioResult;
import io.github.squat_team.model.ResponseMeasureType;
import io.github.squat_team.model.RestArchitecture;
import io.github.squat_team.performance.AbstractPerformancePCMScenario;
import io.github.squat_team.performance.PerformanceMetric;
import io.github.squat_team.performance.PerformancePCMCPUScenario;
import io.github.squat_team.performance.PerformancePCMWorkloadScenario;
import io.github.squat_team.performance.peropteryx.PerOpteryxPCMBot;
import io.github.squat_team.performance.peropteryx.ThreadPoolProvider;
import io.github.squat_team.performance.peropteryx.configuration.Configuration;
import io.github.squat_team.util.SQuATHelper;

public class NoSpringServer {
	/** The port to use */
	private final int port;

	/** The HttpServer */
	protected final transient HttpServer httpServer;

	/** Map to save the current execution status of the various bots */
	private final Map<String, ExecutionStatus> executions;

	/** Thread pool for executions */
	private final ExecutorService threadPool = Executors.newFixedThreadPool(32);

	/**
	 * Synchronize on this object before performing any analyze or search for
	 * alternatives
	 */
	private static final Object LQNS_LOCK = new Object();

	/**
	 * Create the default {@link Configuration}
	 *
	 * @return the default configuration
	 */
	private static Configuration createDefaultConfiguration() {
		TestConstants testConstants = new TestConstants();
		Configuration configuration = new Configuration();
		configuration.getPerOpteryxConfig().setGenerationSize(100);
		configuration.getPerOpteryxConfig().setMaxIterations(10);
		configuration.getLqnsConfig().setLqnsOutputDir(testConstants.LQN_OUTPUT);
		configuration.getExporterConfig().setPcmOutputFolder(testConstants.PCM_STORAGE_PATH);
		configuration.getPcmModelsConfig().setPathmapFolder(testConstants.PCM_MODEL_FILES);
		return configuration;
	}

	/**
	 * @param result
	 * @param restArch
	 */
	private static JSONObject buildResult(PCMScenarioResult result, RestArchitecture restArch) {
		return buildResult(new JSONObject(), result, restArch);
	}

	/**
	 * 
	 * @param target
	 * @param result
	 * @param restArch
	 * @return
	 */
	private static JSONObject buildResult(JSONObject target, PCMScenarioResult result, RestArchitecture restArch) {
		// Put result
		PCMResult pcmResult = result.getResult();
		JSONObject jsonPCMResult = new JSONObject();
		target.put("pcm-result", jsonPCMResult);
		jsonPCMResult.put("response", String.valueOf(pcmResult.getResponse()));
		jsonPCMResult.put("measure-type", pcmResult.getResponseMeasureType());

		// Put name and architecture
		target.put("name", result.getResultingArchitecture().getName());
		target.put("architecture-instance", JSONConverter.build(result.getResultingArchitecture()));

		// Put additional arch
		if (restArch.getCost() != null)
			target.put("cost", restArch.getCost());
		if (restArch.getInsinter() != null)
			target.put("insinter-modular", restArch.getInsinter());
		if (restArch.getSplitrespn() != null)
			target.put("splitrespn-modular", restArch.getSplitrespn());
		if (restArch.getWrapper() != null)
			target.put("wrapper-modular", restArch.getWrapper());

		return target;
	}

	/**
	 * @param object
	 * @return the scenario
	 */
	public static AbstractPerformancePCMScenario getScenarioFromObject(JSONObject object) {
		OptimizationType optimizationType = null;
		PCMResult expectedResult = null;
		AbstractPerformancePCMScenario scenario = null;

		// Type
		if (object.has("type")) {
			optimizationType = OptimizationType.valueOf(object.getString("type"));
		}

		// Expected Result
		if (object.has("expectedResult")) {
			JSONObject jsonExpectedResult = object.getJSONObject("expectedResult");
			ResponseMeasureType responseMeasureType = null;

			if (jsonExpectedResult.has("responseMeasureType")) {
				responseMeasureType = ResponseMeasureType.valueOf(jsonExpectedResult.getString("responseMeasureType"));
			}

			expectedResult = new PCMResult(responseMeasureType);

			if (jsonExpectedResult.has("response")) {
				Comparable<?> response = jsonExpectedResult.getString("response");
				expectedResult.setResponse(response);
			}
		}

		if (object.has("ids") && object.has("rate")) {
			final List<String> ids = new ArrayList<>();
			object.getJSONArray("ids").forEach(o -> {
				ids.add((String) o);
			});

			double rate = object.getDouble("rate");

			String scenarioType = object.getString("scenario-type");
			switch (scenarioType) {
			case "CPU":
				scenario = new PerformancePCMCPUScenario(optimizationType, ids, rate);
				break;
			case "WORKLOAD":
				scenario = new PerformancePCMWorkloadScenario(optimizationType, ids, rate);
				break;
			}

			// metric
			if (object.has("metric")) {
				scenario.setMetric(PerformanceMetric.valueOf(object.getString("metric")));
			}
		}

		if (scenario != null)
			scenario.setExpectedResponse(expectedResult);
		else
			System.err.println("WARNING - scenario could not be retrieved");

		return scenario;
	}

	/**
	 * Bot executor function, this functions generates the UUID and prepares
	 * asynchronous execution
	 *
	 * @param requestBody
	 *            the HTTP POST request body
	 * @param fn
	 *            the function to execute the corresponding function to the rest
	 *            endpoint
	 */
	private String botFn(String requestBody, BiFunction<ExecutionContext, JSONObject, String> fn) {
		String executionUUID = UUID.randomUUID().toString();
		String response = executionUUID;
		try {
			JSONStringer jsonStringer = new JSONStringer();
			jsonStringer.object();
			jsonStringer.key("executionID").value(executionUUID);
			jsonStringer.endObject();
			response = jsonStringer.toString();
			this.executions.put(executionUUID, ExecutionStatus.WAITING);

			ExecutionStatus status = this.executions.get(executionUUID);
			if (status == null)
				return null;
			this.executions.put(executionUUID, ExecutionStatus.EXECUTING);

			// Retrieve parameters
			JSONObject jsonBody = new JSONObject(requestBody);

			// Architecture instance
			UnJSONification unJSONification = new UnJSONification(executionUUID);
			RestArchitecture restArchitecture = JSONConverter.buildFromBody(jsonBody);

			if (restArchitecture.getCost() != null) {
				unJSONification.getFile(restArchitecture.getCost());
			}
			if (restArchitecture.getInsinter() != null) {
				unJSONification.getFile(restArchitecture.getInsinter());
			}
			if (restArchitecture.getSplitrespn() != null) {
				unJSONification.getFile(restArchitecture.getSplitrespn());
			}
			if (restArchitecture.getWrapper() != null) {
				unJSONification.getFile(restArchitecture.getWrapper());
			}

			// Architecture instance
			PCMArchitectureInstance architectureInstance = unJSONification
					.getArchitectureInstance(restArchitecture.getRestArchitecture());

			// Scenario
			AbstractPerformancePCMScenario scenario = NoSpringServer
					.getScenarioFromObject(jsonBody.getJSONObject("scenario"));

			// Configuration
			Configuration configuration = createDefaultConfiguration();

			// Create the bot and context
			PerOpteryxPCMBot bot = new PerOpteryxPCMBot(scenario, configuration);
			bot.setDebugMode(false);
			bot.setDetailedAnalysis(true);
			ExecutionContext context = new ExecutionContext(bot, architectureInstance, restArchitecture);

			// Prepare the result object
			JSONObject rootJSON = new JSONObject();
			rootJSON.put("executionUUID", executionUUID);

			// Execute and generate result
			String result = fn.apply(context, rootJSON);
			return result;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			this.executions.remove(executionUUID);
		}
		return response;
	}

	/**
	 * 
	 */
	private static String readBody(HttpExchange exchg) {
		String body = null;

		try (InputStream is = exchg.getRequestBody()) {
			List<Byte> byteList = new ArrayList<>();
			int ch;
			while ((ch = is.read()) != -1) {
				byteList.add((byte) ch);
			}

			byte b[] = new byte[byteList.size()];
			int index = -1;
			for (byte byt : byteList) {
				b[++index] = byt;
			}
			byteList.clear();
			body = new String(b);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return body;
	}

	@SuppressWarnings("restriction")
	public NoSpringServer(int port, String... args) throws IOException {
		this.port = port;
		this.executions = Collections.synchronizedMap(new HashMap<>());
		this.httpServer = HttpServer.create(new InetSocketAddress(this.port), 0);

		this.httpServer.createContext("/status", exchg -> {
			String rsp;
			if ("GET".equalsIgnoreCase(exchg.getRequestMethod())) {
				rsp = exchg.getRequestURI().getPath();
				if (rsp.contains("/status/")) {
					rsp = rsp.substring("/status/".length());
				}
			} else {
				rsp = "INVALID METHOD";
			}
			exchg.sendResponseHeaders(200, rsp.length());
			OutputStream os = exchg.getResponseBody();
			os.write(rsp.getBytes());
			os.close();
		});

		this.httpServer.createContext("/test", exchg -> {
			String body = readBody(exchg);
			System.out.println(body);
			exchg.getResponseHeaders().add("Status", "OK");
			exchg.sendResponseHeaders(200, body.length());
			try (OutputStream os = exchg.getResponseBody()) {
				os.write(body.getBytes());
				os.flush();
			}
		});

		this.httpServer.createContext("/run", exchg -> {
			String input = readBody(exchg);

			ThreadPoolProvider.BOT_POOL.execute(() -> {
				try {
					new SQuATMain(new TestConstants());
					SQuATMain.mainFn(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});

			System.out.println(input);
			exchg.getResponseHeaders().add("Status", "OK");
			exchg.sendResponseHeaders(200, input.length());
			try (OutputStream os = exchg.getResponseBody()) {
				os.write(input.getBytes());
				os.flush();
			}
		});

		this.httpServer.createContext("/analyze", exchg -> {
			this.threadPool.execute(() -> {
				String body = readBody(exchg);
				String rsp = null;
				if ("POST".equalsIgnoreCase(exchg.getRequestMethod())) {
					rsp = this.botFn(body, (ctx, obj) -> {
						PerOpteryxPCMBot bot = ctx.getBot();
						PCMArchitectureInstance architectureInstance = ctx.getArchitectureInstance();
						PCMScenarioResult result = null;
						synchronized (LQNS_LOCK) {
							System.out.println("SFA");
							result = bot.analyze(architectureInstance);
						}
						String resultString;
						try {
							buildResult(obj, result, ctx.getRestArchitecture());
							resultString = obj.toString();
						} catch (JSONException e) {
							resultString = e.getMessage();
						}
						return resultString;
					});
				} else {
					rsp = "INVALID METHOD";
				}

				try {
					exchg.getResponseHeaders().add("Status", "OK");
					exchg.sendResponseHeaders(200, rsp.length());
					try (OutputStream os = exchg.getResponseBody()) {
						os.write(rsp.getBytes());
						os.flush();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		});

		this.httpServer.createContext("/searchForAlternatives", exchg -> {
			this.threadPool.execute(() -> {
				String body = readBody(exchg);
				String rsp = null;
				if ("POST".equalsIgnoreCase(exchg.getRequestMethod())) {
					rsp = this.botFn(body, (ctx, obj) -> {
						PerOpteryxPCMBot bot = ctx.getBot();
						PCMArchitectureInstance architectureInstance = ctx.getArchitectureInstance();
						List<PCMScenarioResult> results = null;
						synchronized (LQNS_LOCK) {
							System.out.println("SFA");
							results = bot.searchForAlternatives(architectureInstance);
						}
						String resultString;
						try {
							JSONArray jsonResults = new JSONArray();
							obj.put("values", jsonResults);
							for (PCMScenarioResult result : results) {
								jsonResults.put(buildResult(result, ctx.getRestArchitecture()));
							}
							resultString = obj.toString();
						} catch (JSONException e) {
							resultString = e.getMessage();
						}
						return resultString;
					});

				} else {
					rsp = "INVALID METHOD";
				}

				try {
					exchg.getResponseHeaders().add("Status", "OK");
					exchg.sendResponseHeaders(200, rsp.length());
					try (OutputStream os = exchg.getResponseBody()) {
						os.write(rsp.getBytes());
						os.flush();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		});

		this.httpServer.start();

		if (args.length > 0 && "print".equalsIgnoreCase(args[0])) {
			String basicPath = TestConstants.BASIC_FILE_PATH;
			Allocation allocation = SQuATHelper.loadAllocationModel("file:/" + basicPath + ".allocation");
			org.palladiosimulator.pcm.system.System system = SQuATHelper
					.loadSystemModel("file:/" + basicPath + ".system");
			ResourceEnvironment resourceenvironment = SQuATHelper
					.loadResourceEnvironmentModel("file:/" + basicPath + ".resourceenvironment");
			Repository repository = SQuATHelper.loadRepositoryModel("file:/" + basicPath + ".repository");
			UsageModel usageModel = SQuATHelper.loadUsageModel("file:/" + basicPath + ".usagemodel");
			PCMArchitectureInstance architectureInstance = new PCMArchitectureInstance("", repository, system,
					allocation, resourceenvironment, usageModel);
			JSONification jsoNification2 = new JSONification();
			jsoNification2.add(architectureInstance);
			String jsonArch = jsoNification2.toJSON();
			System.out.println(jsonArch);
		}
	}
}

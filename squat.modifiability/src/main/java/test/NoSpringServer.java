package test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiFunction;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import io.github.squat_team.json.JSONConverter;
import io.github.squat_team.json.JSONification;
import io.github.squat_team.json.UnJSONification;
import io.github.squat_team.model.OptimizationType;
import io.github.squat_team.model.PCMArchitectureInstance;
import io.github.squat_team.model.PCMResult;
import io.github.squat_team.model.PCMScenarioResult;
import io.github.squat_team.model.ResponseMeasureType;
import io.github.squat_team.model.RestArchitecture;
import io.github.squat_team.modifiability.ModifiabilityElement;
import io.github.squat_team.modifiability.ModifiabilityInstruction;
import io.github.squat_team.modifiability.ModifiabilityOperation;
import io.github.squat_team.modifiability.ModifiabilityPCMScenario;
import io.github.squat_team.modifiability.kamp.KAMPPCMBot;
import io.github.squat_team.modifiability.kamp.EvaluationType;
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
	 * Build the {@link ModifiabilityInstruction} from the given {@link JSONObject}
	 * 
	 * @param o
	 *            the object to build the instruction from
	 * @return the built {@link ModifiabilityInstruction}
	 */
	public static ModifiabilityInstruction buildModInstructionFromJSON(JSONObject o) {
		ModifiabilityInstruction change = new ModifiabilityInstruction();

		if (o.has("operation")) {
			change.operation = ModifiabilityOperation.valueOf(o.getString("operation"));
		}

		if (o.has("element")) {
			change.element = ModifiabilityElement.valueOf(o.getString("element"));
		}

		if (o.has("parameters")) {
			change.parameters = new HashMap<>();
			JSONObject map = o.getJSONObject("parameters");
			for (String key : JSONObject.getNames(map)) {
				change.parameters.put(key, map.getString(key));
			}
		}

		return change;
	}

	/**
	 * Build the scenario from the given {@link JSONObject}
	 * 
	 * @param object
	 *            the object to build
	 * @return the scenario
	 */
	public static ModifiabilityPCMScenario getScenarioFromObject(JSONObject object) {
		OptimizationType optimizationType = null;
		PCMResult expectedResult = null;

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

		final ModifiabilityPCMScenario scenario = new ModifiabilityPCMScenario(optimizationType);
		scenario.setExpectedResponse(expectedResult);

		// Changes
		if (object.has("changes")) {
			JSONArray changesArray = object.getJSONArray("changes");
			changesArray.forEach(o -> scenario.addChange(buildModInstructionFromJSON((JSONObject) o)));
		}

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

			//
			// Execute on the Bot thread pool
			//
			ExecutionStatus status = this.executions.get(executionUUID);
			if (status == null)
				return "";
			this.executions.put(executionUUID, ExecutionStatus.EXECUTING);

			// Retrieve parameters
			JSONObject jsonBody = new JSONObject(requestBody);

			// Write from JSON
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
			ModifiabilityPCMScenario scenario = NoSpringServer
					.getScenarioFromObject(jsonBody.getJSONObject("scenario"));

			// Create the bot and context
			KAMPPCMBot bot = new KAMPPCMBot(scenario);
			bot.setEvaluationType(EvaluationType.COMPLEXITY);
			ExecutionContext context = new ExecutionContext(bot, architectureInstance, restArchitecture);

			// Prepare the result object
			JSONObject rootJSON = new JSONObject();
			rootJSON.put("executionUUID", executionUUID);

			// Execute and generate result
			String result = fn.apply(context, rootJSON);
			return result;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			this.executions.remove(executionUUID);
		}
		return response;
	}

	/**
	 * Build the {@link JSONObject} representing the result from the given
	 * {@link PCMScenarioResult} and {@link RestArchitecture}
	 * 
	 * @param result
	 *            the result to insert into the {@link JSONObject}
	 * @param restArch
	 *            the rest architecture to get cost, and henshin files from
	 * @return the created {@link JSONObject}
	 */
	private static JSONObject buildResult(PCMScenarioResult result, RestArchitecture restArch) {
		return buildResult(new JSONObject(), result, restArch);
	}

	/**
	 * Build the {@link JSONObject} representing the result from the given
	 * {@link PCMScenarioResult} and {@link RestArchitecture} inserting it into the
	 * given target object.
	 * 
	 * @param target
	 *            the target to insert the result into
	 * @param result
	 *            the result to insert into the {@link JSONObject}
	 * @param restArch
	 *            the rest architecture to get cost, and henshin files from
	 * @return the created {@link JSONObject}
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
	 * Read the whole body from the {@link InputStream} of the given
	 * {@link HttpExchange} handle
	 *
	 * @return the body as {@link String}
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

	/**
	 * Create the {@link NoSpringServer} and create the contexts
	 * 
	 * @param port
	 *            the port to use
	 * @param args
	 *            the arguments to start the server with
	 * @throws IOException
	 */
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
						KAMPPCMBot bot = ctx.getBot();
						PCMArchitectureInstance architectureInstance = ctx.getArchitectureInstance();
						long init = System.currentTimeMillis();
						PCMScenarioResult result = bot.analyze(architectureInstance);
						long duration = System.currentTimeMillis() - init;
						obj.put("duration", duration);
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
						KAMPPCMBot bot = ctx.getBot();
						PCMArchitectureInstance architectureInstance = ctx.getArchitectureInstance();
						long init = System.currentTimeMillis();
						List<PCMScenarioResult> results = bot.searchForAlternatives(architectureInstance);
						long duration = System.currentTimeMillis() - init;
						obj.put("duration", duration);
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
			String BASE = TestConstants.MODEL_PATH;
			String basicPath = TestConstants.MODEL_PATH + "/default";
			Allocation allocation = SQuATHelper.loadAllocationModel("file:/" + basicPath + ".allocation");
			org.palladiosimulator.pcm.system.System system = SQuATHelper
					.loadSystemModel("file:/" + basicPath + ".system");
			ResourceEnvironment resourceenvironment = SQuATHelper
					.loadResourceEnvironmentModel("file:/" + basicPath + ".resourceenvironment");
			Repository repository = SQuATHelper.loadRepositoryModel("file:/" + basicPath + ".repository");
			UsageModel usageModel = SQuATHelper.loadUsageModel("file:/" + basicPath + ".usagemodel");
			PCMArchitectureInstance architectureInstance = new PCMArchitectureInstance("", repository, system,
					allocation, resourceenvironment, usageModel);

			Repository repositoryAlternatives = SQuATHelper
					.loadRepositoryModel("file:/" + TestConstants.MODEL_PATH + "/alternativeRepository.repository");
			architectureInstance.setRepositoryWithAlternatives(repositoryAlternatives);

			JSONification jsoNification2 = new JSONification();
			jsoNification2.add(architectureInstance);
			jsoNification2.add("cost", new File("" + basicPath + ".cost"));
			jsoNification2.add("insinter-modular", new File("" + BASE + "/insinter-modular.henshin"));
			jsoNification2.add("splitrespn-modular", new File("" + BASE + "/splitrespn-modular.henshin"));
			jsoNification2.add("wrapper-modular", new File("" + BASE + "/wrapper-modular.henshin"));

			String jsonArch = jsoNification2.toJSON();
			System.out.println(jsonArch);
		}

	}
}

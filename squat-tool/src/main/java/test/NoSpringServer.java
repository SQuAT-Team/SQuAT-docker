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
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.usagemodel.UsageModel;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import io.github.squat_team.AbstractPCMBot;
import io.github.squat_team.json.JSONUtils;
import io.github.squat_team.json.JSONification;
import io.github.squat_team.json.UnJSONification;
import io.github.squat_team.model.OptimizationType;
import io.github.squat_team.model.PCMArchitectureInstance;
import io.github.squat_team.model.PCMResult;
import io.github.squat_team.model.PCMScenarioResult;
import io.github.squat_team.model.ResponseMeasureType;
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

        if (object.has("ids") && object.has("factor")) {
            final List<String> ids = new ArrayList<>();
            object.getJSONArray("ids").forEach(o -> {
                ids.add((String)o);
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

        return scenario;
    }

    /**
	 * Bot executor function, this functions generates the UUID and prepares asynchronous execution
	 *
	 * @param requestBody the HTTP POST request body
	 * @param fn the function to execute the corresponding function to the rest endpoint
	 */
	private String botFn(String requestBody, BiFunction<ExecutionContext, JSONStringer, String> fn) {
		String executionUUID = UUID.randomUUID().toString();
		String response = executionUUID;
		try {
			JSONStringer jsonStringer = new JSONStringer();
			jsonStringer.object();
			jsonStringer.key("executionID").value(executionUUID);
			jsonStringer.endObject();
			response  = jsonStringer.toString();
			this.executions.put(executionUUID, ExecutionStatus.WAITING);

			//
			// Execute on the Bot thread pool
			//
			ThreadPoolProvider.BOT_POOL.execute(() -> {
				ExecutionStatus status = this.executions.get(executionUUID);
				if (status == null)
					return;
				this.executions.put(executionUUID, ExecutionStatus.EXECUTING);

				try {
					// Retrieve parameters
					JSONObject jsonBody = new JSONObject(requestBody);
					String callbackURL = jsonBody.getString("cbURL");

					// Architecture instance
                    JSONObject jsonArchInstance = jsonBody.getJSONObject("architecture-instance");
                    UnJSONification unJSONification = new UnJSONification(executionUUID);
					PCMArchitectureInstance architectureInstance = unJSONification.getArchitectureInstance(jsonArchInstance);

					// Scenario
                    AbstractPerformancePCMScenario scenario = NoSpringServer.getScenarioFromObject(jsonBody.getJSONObject("scenario"));

					// Configuration
					Configuration configuration = createDefaultConfiguration();

					// Create the bot and context
					PerOpteryxPCMBot bot = new PerOpteryxPCMBot(scenario, configuration);
					bot.setDebugMode(false);
					bot.setDetailedAnalysis(true);
					ExecutionContext context = new ExecutionContext(bot, architectureInstance);

					// Prepare the result stringer
					JSONStringer resultStringer = new JSONStringer();
					resultStringer.object().key("executionUUID").value(executionUUID);

					// Execute and generate result
                    String result = fn.apply(context, resultStringer);

                    System.out.println(" --- RESULTS AVAILABLE --- ");

					// call back the result callback url
					URL url = new URL(callbackURL);
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("POST");
                    connection.setDoOutput(true);
                    connection.setRequestProperty( "Content-Length", String.valueOf(result.length()));
					try (OutputStream outputStream = connection.getOutputStream()) {
						outputStream.write(result.getBytes());
						outputStream.flush();
					}

                    try (InputStream in = connection.getInputStream()) {
                    }
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					this.executions.remove(executionUUID);
				}
			});
		} catch (JSONException e) {
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

    public NoSpringServer(int port, String...args) throws IOException {
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
            String body = readBody(exchg);
            String rsp = null;
            if ("POST".equalsIgnoreCase(exchg.getRequestMethod())) {
                rsp = this.botFn(body, (ctx, stringer) -> {
                    PerOpteryxPCMBot bot = ctx.getBot();
                    PCMArchitectureInstance architectureInstance = ctx.getArchitectureInstance();
                    PCMScenarioResult result = bot.analyze(architectureInstance);
                    String resultString;
                    try {
                        JSONification jsoNification = new JSONification(stringer);
                        jsoNification.add(result);
                        resultString = jsoNification.toJSON();
                    } catch (JSONException e) {
                        resultString = e.getMessage();
                    }
                    return resultString;
                });
            } else {
                rsp = "INVALID METHOD";
            }

            exchg.getResponseHeaders().add("Status", "OK");
			exchg.sendResponseHeaders(200, rsp.length());
			try (OutputStream os = exchg.getResponseBody()) {
                os.write(rsp.getBytes());
                os.flush();
			}
        });

        this.httpServer.createContext("/searchForAlternatives", exchg -> {
            String body = readBody(exchg);
            String rsp = null;
            if ("POST".equalsIgnoreCase(exchg.getRequestMethod())) {
                rsp = this.botFn(body, (ctx, stringer) -> {
                    PerOpteryxPCMBot bot = ctx.getBot();
                    PCMArchitectureInstance architectureInstance = ctx.getArchitectureInstance();

                    // TODO 
                    // deserializing should create a new directory with the name of the execution-UUID
                    // create all files there and upon finishing dlete this folder
/*
                    String basicPath = TestConstants.BASIC_FILE_PATH;
                    Allocation allocation = SQuATHelper.loadAllocationModel("file:/" + basicPath + ".allocation");
                    org.palladiosimulator.pcm.system.System system = SQuATHelper
                            .loadSystemModel("file:/" + basicPath + ".system");
                    ResourceEnvironment resourceenvironment = SQuATHelper
                            .loadResourceEnvironmentModel("file:/" + basicPath + ".resourceenvironment");
                    Repository repository = SQuATHelper.loadRepositoryModel("file:/" + basicPath + ".repository");
                    UsageModel usageModel = SQuATHelper.loadUsageModel("file:/" + basicPath + ".usagemodel");
                    PCMArchitectureInstance architectureInstance = new PCMArchitectureInstance("", repository, system, allocation, resourceenvironment, usageModel);
*/
                    //final String FILE_PREFIX = ""; // "file:/";
                    //String basicPath = "pcm/default";
                    //Allocation allocation = JSONUtils.loadResource(FILE_PREFIX + basicPath + ".allocation");
                    //org.palladiosimulator.pcm.system.System system = JSONUtils.loadResource(FILE_PREFIX + basicPath + ".system");
                    //ResourceEnvironment resourceenvironment = JSONUtils.loadResource(FILE_PREFIX + basicPath + ".resourceenvironment");
                    //Repository repository = JSONUtils.loadResource(FILE_PREFIX + basicPath + ".repository");
                    //UsageModel usageModel = JSONUtils.loadResource(FILE_PREFIX + basicPath + ".usagemodel");
                    //architectureInstance = new PCMArchitectureInstance("", repository, system, allocation, resourceenvironment, usageModel);

                    List<PCMScenarioResult> results = bot.searchForAlternatives(architectureInstance);
                    for (PCMScenarioResult result : results) {
                        System.out.println("----");
                        String uri = result.getResultingArchitecture().getAllocation().eResource().getURI()
                                .segment(result.getResultingArchitecture().getAllocation().eResource().getURI().segmentCount() - 2)
                                .toString();
                        System.out.println("Name: " + uri);
                        System.out.println("Response Time: " + result.getResult().getResponse());
                    }

                    String resultString;
                    try {
                        JSONification jsoNification = new JSONification(stringer);
                        jsoNification.add(results);
                        resultString = jsoNification.toJSON();
                    } catch (JSONException e) {
                        resultString = e.getMessage();
                    }
                    return resultString;
                });
            } else {
                rsp = "INVALID METHOD";
            }

            exchg.getResponseHeaders().add("Status", "OK");
			exchg.sendResponseHeaders(200, rsp.length());
			try (OutputStream os = exchg.getResponseBody()) {
                os.write(rsp.getBytes());
                os.flush();
			}
        });

        this.httpServer.start();

        if(args.length > 0 && "print".equalsIgnoreCase(args[0])) {
            String basicPath = TestConstants.BASIC_FILE_PATH;
            Allocation allocation = SQuATHelper.loadAllocationModel("file:/" + basicPath + ".allocation");
            org.palladiosimulator.pcm.system.System system = SQuATHelper
                    .loadSystemModel("file:/" + basicPath + ".system");
            ResourceEnvironment resourceenvironment = SQuATHelper
                    .loadResourceEnvironmentModel("file:/" + basicPath + ".resourceenvironment");
            Repository repository = SQuATHelper.loadRepositoryModel("file:/" + basicPath + ".repository");
            UsageModel usageModel = SQuATHelper.loadUsageModel("file:/" + basicPath + ".usagemodel");
            PCMArchitectureInstance architectureInstance = new PCMArchitectureInstance("", repository, system, allocation, resourceenvironment, usageModel);
            JSONification jsoNification2 = new JSONification();
            jsoNification2.add(architectureInstance);
            String jsonArch = jsoNification2.toJSON();
            System.out.println(jsonArch);
        }
    }
}

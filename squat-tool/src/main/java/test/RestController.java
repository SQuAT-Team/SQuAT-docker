package test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;

import org.json.JSONException;
import org.json.JSONStringer;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import io.github.squat_team.AbstractPCMBot;
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
import io.github.squat_team.performance.peropteryx.PerOpteryxPCMBot;
import io.github.squat_team.performance.peropteryx.ThreadPoolProvider;
import io.github.squat_team.performance.peropteryx.configuration.Configuration;
import io.github.squat_team.util.SQuATHelper;

@org.springframework.web.bind.annotation.RestController
public class RestController {

	/** Map to save the current execution status of the various bots */
	private final Map<String, ExecutionStatus> executions;

	/**
	 * 
	 */
	public RestController() {
		this.executions = Collections.synchronizedMap(new HashMap<>());
	}

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
					PCMArchitectureInstance architectureInstance = UnJSONification.getArchitectureInstance(jsonArchInstance);

					// Scenario - TODO
					JSONObject jsonScenario = null;
					List<String> cpuIDs = new ArrayList<String>();
					cpuIDs.add(TestConstants.CPU_ID);
					AbstractPerformancePCMScenario scenario = new PerformancePCMCPUScenario(OptimizationType.MINIMIZATION, cpuIDs, 1.0);
					PCMResult expectedResponse = new PCMResult(ResponseMeasureType.DECIMAL);
					expectedResponse.setResponse(6.0);
					scenario.setExpectedResponse(expectedResponse);
					scenario.setMetric(PerformanceMetric.RESPONSE_TIME);

					// Configuration - TODO
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

					// call back the result callback url
					URL url = new URL(callbackURL);
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("POST");
					connection.setDoOutput(true);
					try (OutputStream outputStream = connection.getOutputStream()) {
						outputStream.write(result.getBytes());
						outputStream.flush();
					}
					connection.disconnect();
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

	@RequestMapping(path = "/greeting", method = RequestMethod.POST)
	public String greeting(
			@RequestParam(value = "name", defaultValue = "World") String name,
			@RequestBody String content) {
		return content;
	}

	/**
	 * Endpoint to get the current status of execution for the given executionUUID
	 *
	 * @param executionUUID the UUID to get the status for
	 * @return JSON response as String
	 */
	@RequestMapping(path = "/status/{executionUUID}", method = RequestMethod.GET)
	public String getStatus(@PathVariable String executionUUID) {
		String result = "Error";
		try {
			JSONStringer jsonStringer = new JSONStringer();
			jsonStringer.object();
			jsonStringer.key("executionID").value(executionUUID);
			jsonStringer.key("status");
			ExecutionStatus status = this.executions.get(executionUUID);
			if (status != null)
				jsonStringer.value(status);
			else
				jsonStringer.value("Not available");
			jsonStringer.endObject();
			result = jsonStringer.toString();
		} catch (JSONException e) {

		}
		return result;
	}

	/**
	 *
	 * @param body the request body
	 * @return
	 */
	@RequestMapping(path = "/analyze", method = RequestMethod.POST)
	public String analyze(@RequestBody String body) {
		return this.botFn(body, (ctx, stringer) -> {
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
	}

	private static void mainFn() {
		new Thread(() -> {
			try {
				new SQuATMain(new TestConstants());
				SQuATMain.mainFn(null);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();
	}

	/**
	 *
	 * @param body the request body
	 * @return
	 */
	@RequestMapping(path = "/test", method = RequestMethod.POST)
	public String tyest(@RequestBody String body) {
		return this.botFn(body, (ctx, stringer) -> {
			try {
				//SQuATMain.mainFn(null);
		
				PerOpteryxPCMBot bot = ctx.getBot();
				PCMArchitectureInstance architectureInstance = ctx.getArchitectureInstance();
				
				//architectureInstance = new PCMArchitectureInstance("TEST", repository, system, allocation, resourceenvironment, usageModel);
				
				SQuATMain.optimize(bot, architectureInstance, "./pcm/", createDefaultConfiguration());
				//SQuATMain.analyze(bot, architectureInstance, "./pcm/");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return "";
		});
	}

	/**
	 *
	 * @param body the request body
	 * @return
	 */
	@RequestMapping(path = "/searchForAlternatives", method = RequestMethod.POST)
	public String searchForAlternatives(@RequestBody String body) {
		return this.botFn(body, (ctx, stringer) -> {
			PerOpteryxPCMBot bot = ctx.getBot();
			PCMArchitectureInstance architectureInstance = ctx.getArchitectureInstance();
			List<PCMScenarioResult> results = bot.searchForAlternatives(architectureInstance);
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
	}

	/**
	 * Kills the execution and all bots
	 *
	 * @return "down" although this will never appear
	 */
	@RequestMapping("/kill")
	public String kill() {
		System.exit(0);
		return "down";
	}
}

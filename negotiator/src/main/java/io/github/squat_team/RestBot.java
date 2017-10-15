package io.github.squat_team;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.json.JSONArray;
import org.json.JSONObject;

import io.github.squat_team.agentsUtils.BotManager.BotType;
import io.github.squat_team.model.PCMResult;
import io.github.squat_team.model.ResponseMeasureType;
import io.github.squat_team.model.RestArchitecture;
import io.github.squat_team.model.RestScenarioResult;

public class RestBot {

	private static final Object LOCK = new Object();

	/** The remote URI the bot corresponds to */
	private final String remoteURI;

	/** The scenario we use the bot for */
	private final JSONObject scenario;

	/** The UUID of the bot (not used now) */
	private final String botUUID;

	/** The type of the bot, whether Performance or Modifiability */
	private final BotType botType;

	/** The name of the bot. This is not an identifier! */
	private final String name;

	/**
	 * Create a new {@link RestBot} with the given Parameters
	 * 
	 * @param botType
	 *            the {@link BotType} of the bot
	 * @param remoteURI
	 *            the URI to perform remote call on
	 * @param scenario
	 *            the scenario to use and optimize by this bot
	 */
	public RestBot(String name, BotType botType, String remoteURI, JSONObject scenario) {
		this.botType = Objects.requireNonNull(botType);
		this.remoteURI = Objects.requireNonNull(remoteURI);
		this.botUUID = UUID.randomUUID().toString();
		this.scenario = scenario;
		this.name = name;
	}

	/**
	 * Read the whole stream from the given {@link InputStream} and return a
	 * {@link JSONObject} representing the body
	 * 
	 * @param is
	 *            the {@link InputStream} to read the body from
	 * @return the {@link JSONObject} representing the body of the request
	 */
	private static JSONObject readBody(InputStream is) throws IOException {
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
		return new JSONObject(new String(b));
	}

	/**
	 * Perform the call to the remote end point
	 *
	 * @param body
	 *            the body of the call, representing the parameters of the function
	 *            call
	 * @param uriPath
	 *            the path to the end point to call
	 * @return the response from the end point as {@link JSONObject}
	 */
	private JSONObject call(String body, String uriPath) {
		JSONObject result = null;
		try {
			URL url = new URL(this.remoteURI + "/" + uriPath);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setConnectTimeout(3600000);
			connection.setRequestProperty("Content-Length", String.valueOf(body.length()));
			try (OutputStream outputStream = connection.getOutputStream()) {
				outputStream.write(body.getBytes());
				outputStream.flush();
			}

			try (InputStream is = connection.getInputStream()) {
				result = readBody(is);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Build the {@link RestScenarioResult} from the given {@link JSONObject}. If
	 * the given object does not contain an architecture {@code null} is returned.
	 * 
	 * @param obj
	 *            the object to build the result from
	 * @return the created {@link RestScenarioResult}
	 */
	private RestScenarioResult buildRestScenarioResult(JSONObject obj) {
		if (!obj.has("architecture-instance"))
			return null;

		// Architecture
		JSONObject jsonArchitecture = obj.getJSONObject("architecture-instance");
		JSONObject cost = null;
		JSONObject insinter = null;
		JSONObject splitrespn = null;
		JSONObject wrapper = null;
		PCMResult pcmResult = null;

		// Set additional architecture fields if available
		if (obj.has("cost"))
			cost = obj.getJSONObject("cost");
		if (obj.has("insinter-modular"))
			insinter = obj.getJSONObject("insinter-modular");
		if (obj.has("wrapper-modular"))
			wrapper = obj.getJSONObject("wrapper-modular");

		// PCM Result
		if (obj.has("pcm-result")) {
			JSONObject jsonResult = obj.getJSONObject("pcm-result");
			double response = Double.valueOf(jsonResult.getString("response"));
			String typeString = jsonResult.getString("measure-type");
			ResponseMeasureType responseMeasureType = ResponseMeasureType.valueOf(typeString);
			pcmResult = new PCMResult(responseMeasureType);
			pcmResult.setResponse(response);
		}

		return new RestScenarioResult(this.botType, jsonArchitecture.getString("name"), jsonArchitecture, pcmResult,
				cost, insinter, splitrespn, wrapper);
	}

	/**
	 * Build the body of the call from the given {@link RestArchitecture}
	 * 
	 * @param architecture
	 *            the architecture to use as parameter
	 * @return the created body as {@link String}
	 */
	private String buildBodyFromArchitecture(RestArchitecture architecture) {
		JSONObject root = new JSONObject();
		root.put("scenario", this.scenario);
		root.put("architecture-instance", architecture.getRestArchitecture());
		if (architecture.getCost() != null)
			root.put("cost", architecture.getCost());
		if (architecture.getInsinter() != null)
			root.put("insinter-modular", architecture.getInsinter());
		if (architecture.getSplitrespn() != null)
			root.put("splitrespn-modular", architecture.getSplitrespn());
		if (architecture.getWrapper() != null)
			root.put("wrapper-modular", architecture.getWrapper());
		return root.toString();
	}

	/**
	 * Analyze the given architecture
	 * 
	 * @param architecture
	 *            the architecture to analyze
	 * @return {@link CompletableFuture} to hold the {@link RestScenarioResult}
	 */
	public CompletableFuture<RestScenarioResult> analyze(RestArchitecture architecture) {
		return CompletableFuture.supplyAsync(() -> {
			if (NegotiatorConfiguration.sequential()) {
				synchronized (LOCK) {
					return buildRestScenarioResult(this.call(this.buildBodyFromArchitecture(architecture), "analyze"));
				}
			} else {
				return buildRestScenarioResult(this.call(this.buildBodyFromArchitecture(architecture), "analyze"));
			}
		});
	}

	/**
	 * Search for alternative results in the given architecture
	 * 
	 * @param architecture
	 *            the architecture to search for alternatives in
	 * @return {@link CompletableFuture} to hold the a List of
	 *         {@link RestScenarioResult}
	 */
	public CompletableFuture<List<RestScenarioResult>> searchForAlternatives(RestArchitecture architecture) {
		return CompletableFuture.supplyAsync(() -> {
			if (NegotiatorConfiguration.sequential()) {
				synchronized (LOCK) {
					final List<RestScenarioResult> results = new ArrayList<>();
					JSONObject result = this.call(this.buildBodyFromArchitecture(architecture),
							"searchForAlternatives");
					JSONArray jsonResults = result.getJSONArray("values");
					jsonResults.forEach(o -> {
						results.add(buildRestScenarioResult((JSONObject) o));
					});
					return results;
				}
			} else {
				final List<RestScenarioResult> results = new ArrayList<>();
				JSONObject result = this.call(this.buildBodyFromArchitecture(architecture), "searchForAlternatives");
				JSONArray jsonResults = result.getJSONArray("values");
				jsonResults.forEach(o -> {
					results.add(buildRestScenarioResult((JSONObject) o));
				});
				return results;
			}
		});
	}

	/**
	 * @return the UUID of this bot
	 */
	public String getBotUUID() {
		return this.botUUID;
	}

	/**
	 * @return the {@link BotType}
	 */
	public BotType getBotType() {
		return this.botType;
	}

	/**
	 * @return the name of the bot. This is not an identifier!
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return the expected result of the scenario in the bot.
	 */
	public Float getExpectedResult() {
		JSONObject expectedResult = (JSONObject) this.scenario.get("expectedResult");
		Double response = Double.valueOf((String) expectedResult.get("response"));
		return response.floatValue();
	}
}

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

import org.json.JSONArray;
import org.json.JSONObject;

import io.github.squat_team.agentsUtils.BotManager.BotType;
import io.github.squat_team.model.PCMResult;
import io.github.squat_team.model.ResponseMeasureType;
import io.github.squat_team.model.RestArchitecture;
import io.github.squat_team.model.RestScenarioResult;

public class RestBot {

    /** The remote URI the bot corresponds to */
    private final String remoteURI;

    /** The scenario we use the bot for */
    private final JSONObject scenario;

    /** The UUID of the bot (not used now) */
    private final String botUUID;

    /** The type of the bot, whether Performance or Modifiability */
    private final BotType botType;

    /**
     * @param botType
     * @param remoteURI
     * @param scenario
     */
    public RestBot(BotType botType, String remoteURI, JSONObject scenario) {
        this.botType = Objects.requireNonNull(botType);
        this.remoteURI = Objects.requireNonNull(remoteURI);
        this.botUUID = UUID.randomUUID().toString();
        this.scenario = scenario;
    }

    /**
     * @param is
     * @return
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
     * @param body
     * @param uriPath
     * @return
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
     * @param obj
     * @return
     */
    private RestScenarioResult buildFromRoot(JSONObject obj) {
        // Architecture
        JSONObject jsonArchitecture = obj.getJSONObject("architecture-instance");
        JSONObject cost = null;
        JSONObject insinter = null;
        JSONObject splitrespn = null;
        JSONObject wrapper = null;

        // Set additional architecture fields if available
        if (obj.has("cost"))
            cost = obj.getJSONObject("cost");
        if (obj.has("insinter-modular"))
            insinter = obj.getJSONObject("insinter-modular");
        if (obj.has("wrapper-modular"))
            wrapper = obj.getJSONObject("wrapper-modular");

        // PCM Result
        JSONObject jsonResult = obj.getJSONObject("pcm-result");
        double response = Double.valueOf(jsonResult.getString("response"));
        String typeString = jsonResult.getString("measure-type");
        ResponseMeasureType responseMeasureType = ResponseMeasureType.valueOf(typeString);
        PCMResult pcmResult = new PCMResult(responseMeasureType);
        pcmResult.setResponse(response);

        return new RestScenarioResult(this.botType, jsonArchitecture.getString("name"), 
                jsonArchitecture, pcmResult, cost, insinter, splitrespn, wrapper);
    }

    /**
     * @param architecture
     * @return
     */
    private String buildBody(RestArchitecture architecture) {
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
     * @param body
     * @return
     */
    public RestScenarioResult analyze(RestArchitecture architecture) {
        return buildFromRoot(this.call(this.buildBody(architecture), "analyze"));
    }

    /**
     * @param body
     * @return
     */
    public List<RestScenarioResult> searchForAlternatives(RestArchitecture architecture) {
        final List<RestScenarioResult> results = new ArrayList<>();
        String body = null;
        JSONObject result = this.call(this.buildBody(architecture), "searchForAlternatives");
        JSONArray jsonResults = result.getJSONArray("values");
        jsonResults.forEach(o -> {
            results.add(buildFromRoot((JSONObject) o));
        });
        return results;
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
}

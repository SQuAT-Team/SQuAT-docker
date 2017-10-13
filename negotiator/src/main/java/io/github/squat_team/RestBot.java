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
import java.util.function.Consumer;

import org.json.JSONArray;
import org.json.JSONObject;

import io.github.squat_team.json.JSONification;
import io.github.squat_team.json.UnJSONification;
import io.github.squat_team.model.PCMArchitectureInstance;
import io.github.squat_team.model.PCMResult;
import io.github.squat_team.model.PCMScenario;
import io.github.squat_team.model.PCMScenarioResult;
import io.github.squat_team.model.ResponseMeasureType;
import io.github.squat_team.model.RestScenarioResult;

public class RestBot {

    private final String remoteURI;

    private PCMScenario scenario;

    private final String botUUID;

    /**
     * @param remoteURI
     */
    public RestBot(String remoteURI) {
        this.remoteURI = Objects.requireNonNull(remoteURI);
        this.botUUID = UUID.randomUUID().toString();
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
    private static RestScenarioResult buildFromRoot(JSONObject obj) {
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

        return new RestScenarioResult(jsonArchitecture.getString("name"), 
            jsonArchitecture, pcmResult, cost, insinter, splitrespn, wrapper);
    }

    /**
     * @param body
     * @return
     */
    public RestScenarioResult analyze(String body) {
        return buildFromRoot(this.call(body, "analyze"));
    }

    /**
     * @param body
     * @return
     */
    public List<RestScenarioResult> searchForAlternatives(String body) {
        final List<RestScenarioResult> results = new ArrayList<>();
        JSONObject result = this.call(body, "searchForAlternatives");
        JSONArray jsonResults = result.getJSONArray("values");
        jsonResults.forEach(o -> {
            results.add(buildFromRoot((JSONObject)o));
        });
        return results;
    }

    public PCMScenario getScenario() {
        return scenario;
    }

    public void setScenario(PCMScenario scenario) {
        this.scenario = scenario;
    }

    public String getBotUUID() {
        return this.botUUID;
    }
}

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

import org.json.JSONObject;

import io.github.squat_team.json.JSONification;
import io.github.squat_team.json.UnJSONification;
import io.github.squat_team.model.PCMArchitectureInstance;
import io.github.squat_team.model.PCMResult;
import io.github.squat_team.model.PCMScenario;
import io.github.squat_team.model.PCMScenarioResult;

public class RestBot {

    private final String remoteURI;

    private PCMScenario scenario;

    private final String botUUID;

    public RestBot(String remoteURI) {
        this.remoteURI = Objects.requireNonNull(remoteURI);
        this.botUUID = UUID.randomUUID().toString();
    }

    /**
     * @param is
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
     */
    private JSONObject call(String body) {
        JSONObject result = null;
        try {
            URL url = new URL(this.remoteURI);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setConnectTimeout(3600000);
            connection.setRequestProperty("Content-Length", String.valueOf(body.length()));
            System.out.println(String.valueOf(body.length()));
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

    public PCMScenarioResult analyze(PCMArchitectureInstance currentArchitecture) {
        JSONification jsoNification = new JSONification();
        jsoNification.add(currentArchitecture);
        JSONObject result = this.call(jsoNification.toString());
        UnJSONification unJSONification = new UnJSONification(result.getString("executionUUID"));
        return unJSONification.getPCMScenarioResult(result);
    }

	public List<PCMScenarioResult> searchForAlternatives(PCMArchitectureInstance currentArchitecture) {
        return null;
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

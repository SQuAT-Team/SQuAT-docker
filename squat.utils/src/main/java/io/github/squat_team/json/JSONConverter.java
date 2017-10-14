package io.github.squat_team.json;

import java.io.File;
import java.util.Base64;

import org.eclipse.emf.ecore.resource.Resource;
import org.json.JSONObject;

import io.github.squat_team.model.PCMArchitectureInstance;
import io.github.squat_team.model.PCMResult;
import io.github.squat_team.model.PCMScenarioResult;
import io.github.squat_team.model.RestArchitecture;

public class JSONConverter {

    /**
     * @param jsonBody
     */
    public static RestArchitecture buildFromBody(JSONObject jsonBody) {
        String name = null;
        JSONObject arch = null;
        JSONObject cost = null;
        JSONObject insinter = null;
        JSONObject splitrespn = null;
        JSONObject wrapper = null;

        if (jsonBody.has("architecture-instance")) {
            arch = jsonBody.getJSONObject("architecture-instance");
            name = arch.getString("name");
        }
        if (jsonBody.has("cost"))
            cost = jsonBody.getJSONObject("cost");
        if (jsonBody.has("insinter-modular"))
            insinter = jsonBody.getJSONObject("insinter-modular");
        if (jsonBody.has("splitrespn-modular"))
            splitrespn = jsonBody.getJSONObject("splitrespn-modular");
        if (jsonBody.has("wrapper-modular"))
            wrapper = jsonBody.getJSONObject("wrapper-modular");

        return new RestArchitecture(name, arch, cost, insinter, splitrespn, wrapper);
    }

    public static JSONObject build(PCMResult result) {
        JSONObject jsonResult = new JSONObject();
        jsonResult.put("response", String.valueOf(result.getResponse()));
        jsonResult.put("measure-type", result.getResponseMeasureType());
        return jsonResult;
    }

    public static JSONObject build(PCMArchitectureInstance architectureInstance) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("name", architectureInstance);
        jsonObject.put("repository", build(architectureInstance.getRepository().eResource()));
        if (architectureInstance.getRepositoryWithAlternatives() != null)
            jsonObject.put("repository-with-alternatives",
                    build(architectureInstance.getRepositoryWithAlternatives().eResource()));
        jsonObject.put("system", build(architectureInstance.getSystem().eResource()));
        jsonObject.put("allocation", build(architectureInstance.getAllocation().eResource()));
        jsonObject.put("resource-environment", build(architectureInstance.getResourceEnvironment().eResource()));
        jsonObject.put("usage-model", build(architectureInstance.getUsageModel().eResource()));

        return jsonObject;
    }

    public static JSONObject build(Resource resource) {
        byte[] fileContent = JSONUtils.fromEResource(resource).getBytes();
		String encoded = Base64.getEncoder().encodeToString(fileContent);
		String filename = new File(resource.getURI().toString()).getName();
        
        JSONObject jsonResource = new JSONObject();
        jsonResource.put("filename", filename);
        jsonResource.put("filecontent", encoded);
        return jsonResource;
    }
}

package io.github.squat_team.json;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.usagemodel.UsageModel;

import io.github.squat_team.model.PCMArchitectureInstance;
import io.github.squat_team.model.PCMResult;
import io.github.squat_team.model.PCMScenarioResult;
import io.github.squat_team.model.ResponseMeasureType;

public class UnJSONification {

	/**
	 * Create the object based on the {@link JSONObject} and the provided key. The
	 * {@link JSONObject} needs to provide the serialized file to the given type T
	 * with the given key.
	 * 
	 * @param jsonObject
	 *            the object that contains the key value pair
	 * @param key
	 *            the key for the serialized file value
	 * @return the created object
	 * @throws JSONException
	 * @throws IOException
	 */
	private static <T> T load(JSONObject jsonObject, String key) throws JSONException, IOException {
		return JSONUtils.writeToFileAndLoad(jsonObject.getString(key), JSONUtils::loadResource);
	}

	/**
	 * Return the {@link PCMResult} contained in the given {@link JSONObject}
	 * 
	 * @param jsonObject
	 *            the object that contains the {@link PCMResult}
	 * @return the restored object
	 * @throws JSONException
	 */
	public static PCMResult getPCMResult(JSONObject jsonObject) throws JSONException {
		double response = jsonObject.getDouble("response");
		String typeString = jsonObject.getString("measure-type");
		ResponseMeasureType responseMeasureType = ResponseMeasureType.valueOf(typeString);
		PCMResult pcmResult = new PCMResult(responseMeasureType);
		pcmResult.setResponse(response);
		return pcmResult;
	}

	/**
	 * Return the {@link PCMScenarioResult} contained in the given
	 * {@link JSONObject}
	 * 
	 * @param jsonObject
	 *            the object that contains the {@link PCMScenarioResult}
	 * @return the restored object
	 * @throws JSONException
	 */
	public static PCMScenarioResult getPCMScenarioResult(JSONObject jsonObject) throws JSONException {
		PCMScenarioResult result = new PCMScenarioResult(null);
		JSONObject achitectureJson = jsonObject.getJSONObject("architecture-instance");
		PCMArchitectureInstance achitecture = UnJSONification.getArchitectureInstance(achitectureJson);
		result.setResultingArchitecture(achitecture);
		JSONObject pcmResultJson = jsonObject.getJSONObject("pcm-result");
		PCMResult pcmResult = UnJSONification.getPCMResult(pcmResultJson);
		result.setResult(pcmResult);
		return result;
	}

	/**
	 * Return the {@link PCMArchitectureInstance} contained in the given
	 * {@link JSONObject}
	 * 
	 * @param jsonObject
	 *            the object that contains the {@link PCMArchitectureInstance}
	 * @return the restored object
	 * @throws JSONException
	 */
	public static PCMArchitectureInstance getArchitectureInstance(JSONObject jsonObject) throws JSONException {
		String name = jsonObject.getString("name");
		try {
			Repository repository = load(jsonObject, "repository");
			Repository repositoryAlternatives = null;
			if (jsonObject.has("repository-with-alternatives")) {
				repositoryAlternatives = load(jsonObject, "repository-with-alternatives");
			}
			org.palladiosimulator.pcm.system.System system = load(jsonObject, "system");
			Allocation allocation = load(jsonObject, "allocation");
			ResourceEnvironment resource = load(jsonObject, "resource-environment");
			UsageModel usage = load(jsonObject, "usage-model");
			PCMArchitectureInstance instance = new PCMArchitectureInstance(name, repository, system, allocation,
					resource, usage);
			if (repositoryAlternatives != null) {
				instance.setRepositoryWithAlternatives(repositoryAlternatives);
			}

			return instance;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}

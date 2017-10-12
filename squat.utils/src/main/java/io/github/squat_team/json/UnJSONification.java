package io.github.squat_team.json;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Base64;
import java.util.function.Function;

import org.json.JSONException;
import org.json.JSONObject;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.usagemodel.UsageModel;

import io.github.squat_team.model.PCMArchitectureInstance;
import io.github.squat_team.model.PCMResult;
import io.github.squat_team.model.PCMScenario;
import io.github.squat_team.model.PCMScenarioResult;
import io.github.squat_team.model.ResponseMeasureType;

public class UnJSONification {

	/** The execution UUID */
	private final String executionUUID;

	/** Function to create a file object from a path */
	private Function<String, File> fileFromPath = f -> new File(f);

	/**
	 *
	 * @param executionUUID
	 */
	public UnJSONification(String executionUUID) {
		this.executionUUID = executionUUID;
	}

	/**
	 * Write the given content to a file on the file system and parse it using the
	 * given function
	 * 
	 * @param content
	 *            the content to be written
	 * @param fn
	 *            the parsing function to call
	 * @return the created object
	 * @throws IOException
	 */
	public <T> T writeToFileAndLoad(JSONObject content, Function<String, T> fn) throws IOException {
		new File(this.executionUUID).mkdir();
		final String filename = this.executionUUID + "/" + content.getString("filename");
		String fileContent = new String(Base64.getDecoder().decode(content.getString("filecontent")));
		try (FileWriter fw = new FileWriter(filename)) {
			fw.write(fileContent);
		}
		return fn.apply(filename);
	}

	/**
	 *
	 * @param content
	 * @return
	 */
	public File getFile(JSONObject content) throws IOException {
		return this.writeToFileAndLoad(content, this.fileFromPath);
	}

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
	private <T> T load(JSONObject jsonObject, String key) throws JSONException, IOException {
		return this.writeToFileAndLoad(jsonObject.getJSONObject(key), JSONUtils::loadResource);
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
		double response = Double.valueOf(jsonObject.getString("response"));
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
	public PCMScenarioResult getPCMScenarioResult(JSONObject jsonObject) throws JSONException {
		PCMScenarioResult result = new PCMScenarioResult(null);
		JSONObject achitectureJson = jsonObject.getJSONObject("architecture-instance");
		PCMArchitectureInstance achitecture = this.getArchitectureInstance(achitectureJson);
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
	public PCMArchitectureInstance getArchitectureInstance(JSONObject jsonObject) throws JSONException {
		String name = jsonObject.getString("name");
		try {
			Repository repository = this.load(jsonObject, "repository");
			Repository repositoryAlternatives = null;
			if (jsonObject.has("repository-with-alternatives")) {
				repositoryAlternatives = this.load(jsonObject, "repository-with-alternatives");
			}
			org.palladiosimulator.pcm.system.System system = this.load(jsonObject, "system");
			Allocation allocation = this.load(jsonObject, "allocation");
			ResourceEnvironment resource = this.load(jsonObject, "resource-environment");
			UsageModel usage = this.load(jsonObject, "usage-model");
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

	public void clearDirectory() {
		final File directory = new File(this.executionUUID + "/");
		for(String filename : directory.list()) {
			new File(directory.getPath(), filename).delete();
		}
		directory.delete();
	}
}

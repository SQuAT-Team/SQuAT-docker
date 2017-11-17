package io.github.squat_team.json;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

import org.eclipse.emf.ecore.resource.Resource;
import org.json.JSONException;
import org.json.JSONStringer;

import io.github.squat_team.model.PCMArchitectureInstance;
import io.github.squat_team.model.PCMResult;
import io.github.squat_team.model.PCMScenario;
import io.github.squat_team.model.PCMScenarioResult;

/**
 * 
 * @author roehrdor
 *
 */
public class JSONification {

	/** The JSON stringer used to compose the JSON */
	private final JSONStringer jsonStringer;

	/** flag that indicates whether this JSONification is done */
	private boolean done;

	/**
	 * Create a new {@link JSONification}
	 * 
	 * @throws JSONException
	 */
	public JSONification() throws JSONException {
		this(new JSONStringer());
		this.jsonStringer.object();
		this.done = false;
	}

	/**
	 * Create a new {@link JSONification} with the given {@link JSONStringer}
	 *
	 * @param jsonStringer the stringer to use
	 *
	 * @throws JSONException
	 */
	public JSONification(JSONStringer jsonStringer) throws JSONException {
		this.jsonStringer = jsonStringer;
		this.done = false;
	}

	/**
	 * Throws an exception if {@link JSONification#toJSON()} was already called
	 * 
	 * @throws IllegalStateException
	 *             if {@link JSONification#toJSON()} was already called
	 */
	private void throwIfInvalidState() throws IllegalStateException {
		if (this.done) {
			throw new IllegalStateException("toJSON already called");
		}
	}


	/**
	 * 
	 * @param scenario
	 * @throws JSONException
	 */
	public synchronized void add(PCMScenario scenario) throws JSONException {
		this.throwIfInvalidState();
		Objects.requireNonNull(scenario);
		this.jsonStringer.key("scenario");
		this.jsonStringer.object();
		this.jsonStringer.key("opt-type").value(scenario.getType());
		this.add(scenario.getExpectedResult());
		this.jsonStringer.endObject();
	}

	/**
	 * Add the {@link PCMScenarioResult} to the JSON
	 * 
	 * @param scenarioResult
	 *            the result to add
	 * @throws JSONException
	 */
	public synchronized void add(PCMScenarioResult scenarioResult) throws JSONException {
		this.throwIfInvalidState();
		Objects.requireNonNull(scenarioResult);
		this.jsonStringer.key("pcm-scenario-result").object();
		this.add(scenarioResult.getResultingArchitecture());
		this.add(scenarioResult.getResult());
		this.jsonStringer.endObject();
	}

	/**
	 * Add the list of {@link PCMScenarioResult} to the JSON
	 * 
	 * @param results
	 *            the results to add
	 * @throws JSONException
	 */
	public synchronized void add(List<PCMScenarioResult> results) throws JSONException {
		this.throwIfInvalidState();
		Objects.requireNonNull(results);
		this.jsonStringer.key("values");
		this.jsonStringer.array();
		for(PCMScenarioResult scenarioResult : results) {
			this.jsonStringer.object();
			this.add(scenarioResult.getResultingArchitecture());
			this.add(scenarioResult.getResult());
			this.jsonStringer.endObject();
		}
		this.jsonStringer.endArray();
	}

	/**
	 * Add the given file to the JSON
	 *
	 * @param key the key to use for this file
	 * @param file the file whose content to add
	 */
	public synchronized void add(String key, File file) throws JSONException {
		this.throwIfInvalidState();
		Objects.requireNonNull(key);
		Objects.requireNonNull(file);
		try {
			byte[] fileContent = Files.readAllBytes(file.toPath());
			this.jsonStringer.key(key);
			this.jsonStringer.object();
			this.jsonStringer.key("filename");
			this.jsonStringer.value(file.getName());
			String encoded = Base64.getEncoder().encodeToString(fileContent);
			this.jsonStringer.key("filecontent");
			this.jsonStringer.value(encoded);
			this.jsonStringer.endObject();
		} catch (IOException e) {
			e.printStackTrace(System.err);
		}
	}

	/**
	 * Add the {@link PCMResult} to the JSON
	 * 
	 * @param pcmResult
	 *            the result to add
	 * @throws JSONException
	 */
	public synchronized void add(PCMResult pcmResult) throws JSONException {
		this.throwIfInvalidState();
		Objects.requireNonNull(pcmResult);
		this.jsonStringer.key("pcm-result").object();
		this.jsonStringer.key("response").value(String.valueOf(pcmResult.getResponse()));
		this.jsonStringer.key("measure-type").value(pcmResult.getResponseMeasureType());
		this.jsonStringer.endObject();
	}

	/**
	 * Add the {@link PCMArchitectureInstance} to the JSON
	 * 
	 * @param architectureInstance
	 *            the instance to add
	 * @throws JSONException
	 */
	public synchronized void add(PCMArchitectureInstance architectureInstance) throws JSONException {
		this.throwIfInvalidState();
		Objects.requireNonNull(architectureInstance);
		this.jsonStringer.key("architecture-instance").object();
		this.jsonStringer.key("name").value(architectureInstance.getName());
		this.add(architectureInstance.getRepository().eResource(), "repository");
		if (architectureInstance.getRepositoryWithAlternatives() != null)
			this.add(architectureInstance.getRepositoryWithAlternatives().eResource(), "repository-with-alternatives");
		this.add(architectureInstance.getSystem().eResource(), "system");
		this.add(architectureInstance.getAllocation().eResource(), "allocation");
		this.add(architectureInstance.getResourceEnvironment().eResource(), "resource-environment");
		this.add(architectureInstance.getUsageModel().eResource(), "usage-model");
		this.jsonStringer.endObject();
	}

	/**
	 * Add the {@link Resource} to the JSON
	 * 
	 * @param resource
	 *            the resource to add
	 * @param the
	 *            key to use
	 * @throws JSONException
	 */
	public synchronized void add(Resource resource, String key) throws JSONException {
		this.throwIfInvalidState();
		this.jsonStringer.key(key);
		this.jsonStringer.object();
		byte[] fileContent = JSONUtils.fromEResource(resource).getBytes();
		String encoded = Base64.getEncoder().encodeToString(fileContent);
		this.jsonStringer.key("filename");
		String filename = new File(resource.getURI().toString()).getName();
		this.jsonStringer.value(filename);
		this.jsonStringer.key("filecontent");
		this.jsonStringer.value(encoded);
		this.jsonStringer.endObject();
	}

	/**
	 * Get the JSON
	 * 
	 * @return the JSON String
	 * @throws JSONException
	 */
	public synchronized String toJSON() throws JSONException {
		this.throwIfInvalidState();
		this.done = true;
		this.jsonStringer.endObject();
		return this.jsonStringer.toString();
	}
}

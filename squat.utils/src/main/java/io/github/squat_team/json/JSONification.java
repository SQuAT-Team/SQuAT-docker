package io.github.squat_team.json;

import java.util.List;
import java.util.Objects;

import org.eclipse.emf.ecore.resource.Resource;
import org.json.JSONException;
import org.json.JSONStringer;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.usagemodel.UsageModel;

import io.github.squat_team.model.PCMArchitectureInstance;
import io.github.squat_team.model.PCMResult;
import io.github.squat_team.model.PCMScenario;
import io.github.squat_team.model.PCMScenarioResult;
import io.github.squat_team.util.SQuATHelper;

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
		this.jsonStringer.array();
		results.forEach(this::add);
		this.jsonStringer.endArray();
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
		this.jsonStringer.key("response").value((double) pcmResult.getResponse());
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
		this.jsonStringer.value(JSONUtils.fromEResource(resource));
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

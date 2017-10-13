package io.github.squat_team.agentsUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

import io.github.squat_team.RestBot;
import io.github.squat_team.model.OptimizationType;
import io.github.squat_team.model.ResponseMeasureType;
import io.github.squat_team.model.RestArchitecture;
import io.github.squat_team.modifiability.ModifiabilityElement;
import io.github.squat_team.modifiability.ModifiabilityOperation;
import io.github.squat_team.performance.PerformanceMetric;

/**
 * Sets up the specific {@link SillyBot} and scenarios used in this specific
 * case study. TODO: PA! SEQUENTIAL/PARALLEL Analysis calls
 */
public class LoadHelper implements ILoadHelper {

	@Override
	public List<SillyBot> generateSillyBotsAndAnalyze(List<RestArchitecture> architecturalAlternatives,
			RestArchitecture initialArchitecture) throws InterruptedException, ExecutionException {
		List<SillyBot> sillyBots = new ArrayList<>();
		for (RestBot currentBot : BotManager.getInstance().getAllBots()) {
			SillyBot newSillyBot = generateFrom(currentBot, initialArchitecture);
			analyzeAlternatives(currentBot, newSillyBot, architecturalAlternatives);
			sillyBots.add(newSillyBot);

			newSillyBot.printUtilies();
		}
		return sillyBots;
	}

	/**
	 * Generates a {@link SillyBot} from a {@link RestBot}.
	 * 
	 * @param bot
	 *            the bot to generate from.
	 * @param initialArchitecture
	 *            the initial architecture of the whole run.
	 * @return the generated architecture.
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	private SillyBot generateFrom(RestBot bot, RestArchitecture initialArchitecture) throws InterruptedException, ExecutionException {
		SillyBot newSillyBot;
		Double initialArchitectureResponse = (Double) bot.analyze(initialArchitecture).get().getResult().getResponse();

		switch (bot.getBotType()) {
		case PERFORMANCE:
			newSillyBot = new PerformanceSillyBot(initialArchitectureResponse.floatValue(), bot.getName(),
					bot.getExpectedResult());
			break;
		case MODIFIABILITY:
			newSillyBot = new ModifiabilitySillyBot(initialArchitectureResponse.floatValue(), bot.getName(),
					bot.getExpectedResult());
			break;
		default:
			throw new IllegalArgumentException("The bot type is not implemented yet: " + bot.getBotType());
		}
		return newSillyBot;
	}

	/**
	 * Analyzes the alternatives and adds the results to the bot.
	 * 
	 * @param bot
	 *            the real bot related to the silly bot.
	 * @param sillyBot
	 *            the bot that will contian the analysis results.
	 * @param architecturalAlternatives
	 *            ALL alternatives to analyze.
	 * @return the bot that contains the analysis results.
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	private SillyBot analyzeAlternatives(RestBot bot, SillyBot sillyBot,
			List<RestArchitecture> architecturalAlternatives) throws InterruptedException, ExecutionException {
		for (RestArchitecture currentArchitecturalAlternative : architecturalAlternatives) {
			Proposal propsal;
			Double currentArchitectureResponse = (Double) bot.analyze(currentArchitecturalAlternative).get().getResult()
					.getResponse();

			switch (bot.getBotType()) {
			case PERFORMANCE:
				propsal = new PerformanceProposal(currentArchitectureResponse.floatValue(),
						currentArchitecturalAlternative.getName());
				break;
			case MODIFIABILITY:
				propsal = new ModifiabilityProposal(currentArchitectureResponse.floatValue(),
						currentArchitecturalAlternative.getName());
				break;
			default:
				throw new IllegalArgumentException("The bot type is not implemented yet: " + bot.getBotType());
			}
			sillyBot.insertInOrder(propsal);
		}
		return sillyBot;
	}

	/**
	 * A scenario which increases the workload +10%.
	 * 
	 * @return the created {@link JSONObject}
	 */
	public static JSONObject createPerformanceScenarioS1(ResponseMeasureType type, double response) {
		ArrayList<String> workloadIDs = new ArrayList<String>();
		workloadIDs.add("_Uc-igC6OEd-Jla2o7wkBzQ");
		JSONObject scenario = new JSONObject();

		scenario.put("type", OptimizationType.MINIMIZATION);
		JSONArray ids = new JSONArray();
		scenario.put("ids", ids);
		workloadIDs.forEach(ids::put);
		scenario.put("rate", 1.1);
		scenario.put("scenario-type", "WORKLOAD");

		// PCMRESULT
		JSONObject expectedResult = new JSONObject();
		expectedResult.put("responseMeasureType", type);
		expectedResult.put("response", String.valueOf(response));
		scenario.put("expectedResult", expectedResult);

		scenario.put("metric", PerformanceMetric.RESPONSE_TIME);

		return scenario;
	}

	/**
	 * A scenario which increases the workload +30%.
	 * 
	 * @return the created {@link JSONObject}
	 */
	public static JSONObject createPerformanceScenarioS4(ResponseMeasureType type, double response) {
		ArrayList<String> workloadIDs = new ArrayList<String>();
		workloadIDs.add("_Uc-igC6OEd-Jla2o7wkBzQ");
		JSONObject scenario = new JSONObject();

		scenario.put("type", OptimizationType.MINIMIZATION);
		JSONArray ids = new JSONArray();
		scenario.put("ids", ids);
		workloadIDs.forEach(ids::put);
		scenario.put("rate", 1.3);
		scenario.put("scenario-type", "WORKLOAD");

		// PCMRESULT
		JSONObject expectedResult = new JSONObject();
		expectedResult.put("responseMeasureType", type);
		expectedResult.put("response", String.valueOf(response));
		scenario.put("expectedResult", expectedResult);

		scenario.put("metric", PerformanceMetric.RESPONSE_TIME);

		return scenario;
	}

	/**
	 * A scenario which simulates a system failure in the DB-cluster. (-50% CPU).
	 * 
	 * @return the created {@link JSONObject}
	 */
	public static JSONObject createPerformanceScenarioS2(ResponseMeasureType type, double response) {
		ArrayList<String> workloadIDs = new ArrayList<String>();
		workloadIDs.add("_GecPsF7fEeavvL8WcdoZSg");
		JSONObject scenario = new JSONObject();

		scenario.put("type", OptimizationType.MINIMIZATION);
		JSONArray ids = new JSONArray();
		scenario.put("ids", ids);
		workloadIDs.forEach(ids::put);
		scenario.put("rate", 0.5);
		scenario.put("scenario-type", "CPU");

		// PCMRESULT
		JSONObject expectedResult = new JSONObject();
		expectedResult.put("responseMeasureType", type);
		expectedResult.put("response", String.valueOf(response));
		scenario.put("expectedResult", expectedResult);

		scenario.put("metric", PerformanceMetric.RESPONSE_TIME);
		return scenario;
	}

	/**
	 * A scenario which simulates a system failure in the Server1-cluster. (-50%
	 * CPU).
	 * 
	 * @return the created {@link JSONObject}
	 */
	public static JSONObject createPerformanceScenarioS3(ResponseMeasureType type, double response) {
		ArrayList<String> workloadIDs = new ArrayList<String>();
		workloadIDs.add("_psq4IiINEeSC6_3TPN1J7A");
		JSONObject scenario = new JSONObject();

		scenario.put("type", OptimizationType.MINIMIZATION);
		JSONArray ids = new JSONArray();
		scenario.put("ids", ids);
		workloadIDs.forEach(ids::put);
		scenario.put("rate", 0.5);
		scenario.put("scenario-type", "CPU");

		// PCMRESULT
		JSONObject expectedResult = new JSONObject();
		expectedResult.put("responseMeasureType", type);
		expectedResult.put("response", String.valueOf(response));
		scenario.put("expectedResult", expectedResult);

		scenario.put("metric", PerformanceMetric.RESPONSE_TIME);

		return scenario;
	}

	/**
	 * Create a ModifiabilityInstruction by the given parameters and add them to the
	 * {@link JSONStringer}
	 *
	 * This method returns without modifying the {@link JSONStringer} if the arrays
	 * keys and values have not the same length.
	 *
	 * This method will throw {@link NullPointerException} if either the keys or
	 * values are null.
	 *
	 * @param op
	 *            the {@link ModifiabilityOperation} of the instruction
	 * @param el
	 *            the {@link ModifiabilityElement} of the instruction
	 * @param keys
	 *            the parameter keys
	 * @param values
	 *            the parameter values
	 */
	private static JSONObject createModifiabilityInstruction(ModifiabilityOperation op, ModifiabilityElement el,
			String keys[], String values[]) throws NullPointerException {
		Objects.requireNonNull(keys);
		Objects.requireNonNull(values);
		if (keys.length != values.length)
			return null;
		final int LEN = keys.length;
		JSONObject ret = new JSONObject();
		ret.put("operation", String.valueOf(op));
		ret.put("element", String.valueOf(el));

		JSONArray parameters = new JSONArray();
		ret.put("parameters", parameters);
		for (int i = 0; i < LEN; ++i) {
			parameters.put(new JSONObject().put(keys[i], values[i]));
		}
		return ret;
	}

	/**
	 * Create the first Modifiability scenario
	 *
	 * @param type
	 *            the {@link ResponseMeasureType} to use for this scenario
	 * @param response
	 *            the expected response value
	 */
	public static JSONObject createModifiabilityScenarioS1(ResponseMeasureType type, Comparable<Double> response) {

		JSONObject scenario = new JSONObject();

		// expected Result
		JSONObject expectedResult = new JSONObject();
		expectedResult.put("responseMeasureType", type);
		expectedResult.put("response", String.valueOf(response));
		scenario.put("expectedResult", expectedResult);

		scenario.put("type", OptimizationType.MINIMIZATION);
		JSONArray changes = new JSONArray();
		scenario.put("changes", changes);
		changes.put(createModifiabilityInstruction(ModifiabilityOperation.MODIFY, ModifiabilityElement.INTERFACE,
				new String[] { "name" }, new String[] { "IExternalPayment" }));

		changes.put(createModifiabilityInstruction(ModifiabilityOperation.MODIFY, ModifiabilityElement.COMPONENT,
				new String[] { "name" }, new String[] { "BusinessTripMgmt" }));

		return scenario;
	}

	/**
	 * Create the second Modifiability scenario
	 *
	 * @param type
	 *            the {@link ResponseMeasureType} to use for this scenario
	 * @param response
	 *            the expected response value
	 * @param jsonStringer
	 *            this json stringer is used to insert the scenario into the JSON
	 *            object. This {@link JSONStringer} is required to be in a state
	 *            where a key can be created
	 */
	public static JSONObject createModifiabilityScenarioS2(ResponseMeasureType type, Comparable<Double> response) {

		JSONObject scenario = new JSONObject();

		// expected Result
		JSONObject expectedResult = new JSONObject();
		expectedResult.put("responseMeasureType", type);
		expectedResult.put("response", String.valueOf(response));
		scenario.put("expectedResult", expectedResult);

		scenario.put("type", OptimizationType.MINIMIZATION);
		JSONArray changes = new JSONArray();
		scenario.put("changes", changes);

		changes.put(createModifiabilityInstruction(ModifiabilityOperation.MODIFY, ModifiabilityElement.INTERFACE,
				new String[] { "name" }, new String[] { "ITripDB" }));

		changes.put(createModifiabilityInstruction(ModifiabilityOperation.CREATE, ModifiabilityElement.INTERFACE,
				new String[] { "name" }, new String[] { "Analytics" }));

		changes.put(createModifiabilityInstruction(ModifiabilityOperation.CREATE, ModifiabilityElement.OPERATION,
				new String[] { "iname", "oname" }, new String[] { "Analytics", "getLastTrips" }));

		changes.put(createModifiabilityInstruction(ModifiabilityOperation.CREATE, ModifiabilityElement.COMPONENT,
				new String[] { "name" }, new String[] { "Insights" }));

		changes.put(createModifiabilityInstruction(ModifiabilityOperation.CREATE, ModifiabilityElement.PROVIDEDROLE,
				new String[] { "cname", "iname" }, new String[] { "Insights", "Analytics" }));

		changes.put(createModifiabilityInstruction(ModifiabilityOperation.MODIFY, ModifiabilityElement.REQUIREDROLE,
				new String[] { "cname", "iname" }, new String[] { "Insights", "ITripDB" }));

		return scenario;
	}

	/**
	 * Create the third Modifiability scenario. Adds a User Managament component.
	 *
	 * @param type
	 *            the {@link ResponseMeasureType} to use for this scenario
	 * @param response
	 *            the expected response value
	 */
	public static JSONObject createModifiabilityScenarioS3(ResponseMeasureType type, Comparable<Double> response) {
		JSONObject scenario = new JSONObject();

		// expected Result
		JSONObject expectedResult = new JSONObject();
		expectedResult.put("responseMeasureType", type);
		expectedResult.put("response", String.valueOf(response));
		scenario.put("expectedResult", expectedResult);

		scenario.put("type", OptimizationType.MINIMIZATION);
		JSONArray changes = new JSONArray();
		scenario.put("changes", changes);

		changes.put(createModifiabilityInstruction(ModifiabilityOperation.MODIFY, ModifiabilityElement.INTERFACE,
				new String[] { "name" }, new String[] { "IBusiness Trip" }));

		changes.put(createModifiabilityInstruction(ModifiabilityOperation.CREATE, ModifiabilityElement.INTERFACE,
				new String[] { "name" }, new String[] { "IUserManagement" }));

		changes.put(createModifiabilityInstruction(ModifiabilityOperation.CREATE, ModifiabilityElement.OPERATION,
				new String[] { "iname", "oname" }, new String[] { "IUserManagement", "verifyLoginData" }));

		changes.put(createModifiabilityInstruction(ModifiabilityOperation.CREATE, ModifiabilityElement.OPERATION,
				new String[] { "iname", "oname" }, new String[] { "IUserManagement", "updateUser" }));

		changes.put(createModifiabilityInstruction(ModifiabilityOperation.CREATE, ModifiabilityElement.COMPONENT,
				new String[] { "name" }, new String[] { "UserManagement" }));

		changes.put(createModifiabilityInstruction(ModifiabilityOperation.CREATE, ModifiabilityElement.PROVIDEDROLE,
				new String[] { "cname", "iname" }, new String[] { "UserManagement", "IUserManagement" }));

		changes.put(createModifiabilityInstruction(ModifiabilityOperation.MODIFY, ModifiabilityElement.REQUIREDROLE,
				new String[] { "cname", "iname" }, new String[] { "BusinessTripMgmt", "IUserManagement" }));

		return scenario;
	}

	/**
	 * Create the first Modifiability scenario. Makes changes to the Business Trip
	 * Mgmt Component and the interfaces of ITripDB and IBooking, to show additional
	 * data.
	 *
	 * @param type
	 *            the {@link ResponseMeasureType} to use for this scenario
	 * @param response
	 *            the expected response value
	 */
	public static JSONObject createModifiabilityScenarioS4(ResponseMeasureType type, Comparable<Double> response) {

		JSONObject scenario = new JSONObject();

		// expected Result
		JSONObject expectedResult = new JSONObject();
		expectedResult.put("responseMeasureType", type);
		expectedResult.put("response", String.valueOf(response));
		scenario.put("expectedResult", expectedResult);

		scenario.put("type", OptimizationType.MINIMIZATION);
		JSONArray changes = new JSONArray();
		scenario.put("changes", changes);
		changes.put(createModifiabilityInstruction(ModifiabilityOperation.MODIFY, ModifiabilityElement.INTERFACE,
				new String[] { "name" }, new String[] { "ITripDB" }));

		changes.put(createModifiabilityInstruction(ModifiabilityOperation.MODIFY, ModifiabilityElement.INTERFACE,
				new String[] { "name" }, new String[] { "IBooking" }));

		changes.put(createModifiabilityInstruction(ModifiabilityOperation.MODIFY, ModifiabilityElement.COMPONENT,
				new String[] { "name" }, new String[] { "BusinessTripMgmt" }));

		return scenario;
	}
}

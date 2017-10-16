package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONObject;
import org.json.JSONStringer;

import io.github.squat_team.NegotiatorConfiguration;
import io.github.squat_team.RestBot;
import io.github.squat_team.SQuATSillyBotsNegotiator;
import io.github.squat_team.agentsUtils.ArchitectureInitializer;
import io.github.squat_team.agentsUtils.BotManager;
import io.github.squat_team.agentsUtils.LoadHelper;
import io.github.squat_team.agentsUtils.BotManager.BotType;
import io.github.squat_team.agentsUtils.transformations.ArchitecturalTransformationsFactory;
import io.github.squat_team.model.ResponseMeasureType;
import io.github.squat_team.model.RestArchitecture;
import io.github.squat_team.model.RestScenarioResult;

public class DockerMain {

	public static String buildBodyFromArchitecture(RestArchitecture architecture, JSONObject scenario) {
		JSONObject root = new JSONObject();
		root.put("scenario", scenario);
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

	public static void main(String[] args) throws InterruptedException, IOException {

		// RestArchitecture initialArch =
		// ArchitectureInitializer.loadSpecificModel("test");
		// // ArchitecturalTransformationsFactory fact = new
		// // ArchitecturalTransformationsFactory(initialArch);
		// RestBot b1 = new RestBot("b1", BotType.MODIFIABILITY,
		// "http://127.0.0.1:8081",
		// LoadHelper.createModifiabilityScenarioS1(ResponseMeasureType.DECIMAL,
		// 120.0));
		// try {
		// System.out.println(b1.searchForAlternatives(initialArch).get());
		// } catch (ExecutionException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// b1.searchForAlternatives(initialArch).thenAccept(System.out::println);
		// BotManager.getInstance().addBot(b1);
		// List<RestScenarioResult> results = new ArrayList<>();

		System.out.println("negotiator starts in 5 seconds...");
		Thread.sleep(5000);
		System.out.println("negotiator starting...");
		SQuATSillyBotsNegotiator n = new SQuATSillyBotsNegotiator();
		new NoSpringServer(n, 8082);
		n.negotiatiateUntilAnAgreementIsReached();
		// RestArchitecture initialArchitecture =
		// ArchitectureInitializer.loadSpecificModel(NegotiatorConfiguration.INITIAL_ARCHITECTURE_NAME);
		// JSONObject scJsonObject =
		// LoadHelper.createPerformanceScenarioS2(ResponseMeasureType.DECIMAL, 40.0);
		// System.out.println(buildBodyFromArchitecture(initialArchitecture,
		// scJsonObject));

		/*
		 * JSONStringer jsonStringer = new JSONStringer(); jsonStringer.object();
		 * //LoadHelper.createModifiabilityScenarioS2(ResponseMeasureType.DECIMAL,
		 * 120.0, jsonStringer);
		 * //LoadHelper.createModifiabilityScenarioS1(ResponseMeasureType.DECIMAL,
		 * 120.0, jsonStringer);
		 * //PerformanceScenarioHelper.createScenarioOfWorkload(jsonStringer);
		 * LoadHelper.loadSpecificModel(jsonStringer, ""); jsonStringer.endObject();
		 * String body = jsonStringer.toString(); //System.out.println(body); RestBot
		 * restbot = new RestBot("http://localhost:8080/searchForAlternatives");
		 * restbot.searchForAlternatives(body).forEach(r -> {
		 * System.out.println(r.getResult().getResponse()); });
		 */
	}
}

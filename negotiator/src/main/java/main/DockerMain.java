package main;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONStringer;

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

    public static void main(String[] args) throws InterruptedException {
        
//        RestArchitecture initialArch = ArchitectureInitializer.loadSpecificModel("test");
//        ArchitecturalTransformationsFactory fact = new ArchitecturalTransformationsFactory(initialArch);
//        RestBot b1 = new RestBot("b1", BotType.MODIFIABILITY, "http://localhost:8081", LoadHelper.createModifiabilityScenarioS1(ResponseMeasureType.DECIMAL, 120.0));
//        //b1.searchForAlternatives(initialArch).thenAccept(System.out::println);
//        BotManager.getInstance().addBot(b1);
//        List<RestScenarioResult> results = new ArrayList<>();
//        fact.foo(initialArch, results).thenAccept(System.out::println);

        new SQuATSillyBotsNegotiator().negotiatiateUntilAnAgreementIsReached();
        Thread.sleep(100000);
        /*
        JSONStringer jsonStringer = new JSONStringer();
        jsonStringer.object();
        //LoadHelper.createModifiabilityScenarioS2(ResponseMeasureType.DECIMAL, 120.0, jsonStringer);
        //LoadHelper.createModifiabilityScenarioS1(ResponseMeasureType.DECIMAL, 120.0, jsonStringer);
        //PerformanceScenarioHelper.createScenarioOfWorkload(jsonStringer);
        LoadHelper.loadSpecificModel(jsonStringer, "");
        jsonStringer.endObject();
        String body = jsonStringer.toString();
        //System.out.println(body);
        RestBot restbot = new RestBot("http://localhost:8080/searchForAlternatives");
        restbot.searchForAlternatives(body).forEach(r -> {
            System.out.println(r.getResult().getResponse());
        });
        */
    }
}

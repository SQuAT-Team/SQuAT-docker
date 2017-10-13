package io.github.squat_team.agentsUtils.transformations;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONStringer;

import edu.squat.transformations.ArchitecturalVersion;
import io.github.squat_team.RestBot;
import io.github.squat_team.agentsUtils.LoadHelper;
import io.github.squat_team.agentsUtils.PerformanceScenarioHelper;
import io.github.squat_team.model.RestArchitecture;
import io.github.squat_team.model.RestScenarioResult;

public class PerformanceTransformationFactory {

    public List<RestArchitecture> generateArchitecturalVersionsUsingPerformanceTransformations(
            ArchitecturalVersion architecturalVersion) {
        List<RestArchitecture> ret = new ArrayList<RestArchitecture>();
        RestBot performanceBot = new RestBot("http://127.0.0.1:8080");
        JSONStringer jsonStringer = new JSONStringer();
        jsonStringer.object();
        PerformanceScenarioHelper.createScenarioOfWorkload(jsonStringer);
        LoadHelper.loadSpecificModel(jsonStringer, "");
        jsonStringer.endObject();
        String body = jsonStringer.toString();
        List<RestScenarioResult> results = performanceBot.searchForAlternatives(body);
        results.forEach(r -> ret.add(r.getArchitecture()));
        return ret;
    }
}

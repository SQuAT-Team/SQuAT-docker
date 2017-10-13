package io.github.squat_team.agentsUtils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

import edu.squat.transformations.ArchitecturalVersion;
import io.github.squat_team.model.OptimizationType;
import io.github.squat_team.model.ResponseMeasureType;
import io.github.squat_team.model.RestArchitecture;
import io.github.squat_team.modifiability.ModifiabilityElement;
import io.github.squat_team.modifiability.ModifiabilityOperation;
import io.github.squat_team.performance.PerformanceMetric;

/**
 * Sets up the specific {@link SillyBot} and scenarios used in this specific
 * case study. TODO: PA! Adjust for use with REST Interface and set the correct
 * scenarios for stplus. (see commented out)
 */
public class LoadHelper implements ILoadHelper {

    public List<SillyBot> loadBotsForArchitecturalAlternatives(List<ArchitecturalVersion> architecturalAlternatives,
            ArchitecturalVersion initialArchitecture) {
        return null;
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
     * A scenario which simulates a system failure in the Server1-cluster. (-50% CPU).
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
     * Create a ModifiabilityInstruction by the given parameters and add them
     * to the {@link JSONStringer}
     *
     * This method returns without modifying the {@link JSONStringer} if the 
     * arrays keys and values have not the same length.
     *
     * This method will throw {@link NullPointerException} if either the 
     * keys or values are null.
     *
     * @param op the {@link ModifiabilityOperation} of the instruction
     * @param el the {@link ModifiabilityElement} of the instruction
     * @param keys the parameter keys
     * @param values the parameter values
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
     * @param type the {@link ResponseMeasureType} to use for this scenario
     * @param response the expected response value
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
     * @param type the {@link ResponseMeasureType} to use for this scenario
     * @param response the expected response value
     * @param jsonStringer this json stringer is used to insert the scenario
     *  into the JSON object. This {@link JSONStringer} is required to be in 
     *  a state where a key can be created
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
     * @param file the file to read
     * @return the base64 encoded file
     */
    private static String buildStringFromFile(String file) {
        String ret = "";
        try (RandomAccessFile raf = new RandomAccessFile(new File(file), "r")) {
            byte[] fileContent = new byte[(int) raf.length()];
            raf.read(fileContent);
            ret = Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    private static void addType(JSONStringer jsonStringer, String basicPath, String filename, String filetype) {
        jsonStringer.key(filetype);
        jsonStringer.object();
        jsonStringer.key("filename").value(basicPath + "/" + filename + "." + filetype);
        jsonStringer.key("filecontent").value(buildStringFromFile(basicPath + "/" + filename + "." + filetype));
        jsonStringer.endObject();
    }

    /**
     * Create a {@link JSONObject} for the given key and file
     *
     * @param key the key to use for this file
     * @param file the file whose content to add
     * @return the object or null if file could not be read
     */
    private static JSONObject create(String key, File file) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(file);
        try {
            JSONObject obj = new JSONObject();
            byte[] fileContent = Files.readAllBytes(file.toPath());
            String encoded = Base64.getEncoder().encodeToString(fileContent);
            obj.put("filename", file.getName());
            obj.put("filecontent", encoded);
            return obj;
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        return null;
    }

    public static void putIfNotNull(JSONObject parent, String key, File file) {
        JSONObject child = create(key, file);
        if (child != null)
            parent.put(key, child);
    }

    /**
     * @param name
     * @return {@link RestArchitecture} instance
     */
    public static RestArchitecture loadSpecificModel(String name) {
        final String MODEL_NAME = "default";
        final String MODEL_PATH = "/home/roehrdor/Workspace-oxygen/SQuAT-docker/squat.modifiability/model";
        final String ALTERNATIVE_REPOSITORY_PATH = "/home/roehrdor/Workspace-oxygen/SQuAT-docker/squat.modifiability/model/alternativeRepository.repository";
        String BASE = MODEL_PATH;
        String basicPath = MODEL_PATH + "/default";

        // Architecture
        JSONObject architecture = new JSONObject();
        architecture.put("name", name);
        putIfNotNull(architecture, "repository", new File(basicPath + ".repository"));
        putIfNotNull(architecture, "system", new File(basicPath + ".system"));
        putIfNotNull(architecture, "allocation", new File(basicPath + ".allocation"));
        putIfNotNull(architecture, "resource-environment", new File(basicPath + ".resourceenvironment"));
        putIfNotNull(architecture, "usage-model", new File(basicPath + ".usagemodel"));
        putIfNotNull(architecture, "repository-with-alternatives",
                new File(BASE + "/" + "alternativeRepository" + ".repository"));

        // Optional architectur part
        JSONObject cost = create("cost", new File("" + basicPath + ".cost"));
        JSONObject insinter = create("insinter-modular", new File("" + BASE + "/insinter-modular.henshin"));
        JSONObject splitrespn = create("splitrespn-modular", new File("" + BASE + "/splitrespn-modular.henshin"));
        JSONObject wrapper = create("wrapper-modular", new File("" + BASE + "/wrapper-modular.henshin"));

        return new RestArchitecture(name, architecture, cost, insinter, splitrespn, wrapper);
    }
}

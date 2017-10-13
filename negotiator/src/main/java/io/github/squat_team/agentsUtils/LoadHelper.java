package io.github.squat_team.agentsUtils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

import edu.squat.transformations.ArchitecturalVersion;
import io.github.squat_team.model.OptimizationType;
import io.github.squat_team.model.ResponseMeasureType;
import io.github.squat_team.modifiability.ModifiabilityElement;
import io.github.squat_team.modifiability.ModifiabilityOperation;

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
        ;
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
    public static JSONObject createModifiabilityScenarioS2(ResponseMeasureType type, Comparable<Double> response,
            JSONStringer jsonStringer) {

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
     * Add the given file to the JSON
     *
     * @param jsonStringer
     * @param key the key to use for this file
     * @param file the file whose content to add
     */
    private static void add(JSONStringer jsonStringer, String key, File file) {
        Objects.requireNonNull(jsonStringer);
        Objects.requireNonNull(key);
        Objects.requireNonNull(file);
        try {
            byte[] fileContent = Files.readAllBytes(file.toPath());
            jsonStringer.key(key);
            jsonStringer.object();
            jsonStringer.key("filename");
            jsonStringer.value(file.getName());
            String encoded = Base64.getEncoder().encodeToString(fileContent);
            jsonStringer.key("filecontent");
            jsonStringer.value(encoded);
            jsonStringer.endObject();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    /**
     * @param jsonStringer
     */
    public static void loadSpecificModel(JSONStringer jsonStringer, String name) {
        final String MODEL_NAME = "default";
        final String MODEL_PATH = "/home/roehrdor/Workspace-oxygen/SQuAT-docker/squat.modifiability/model";
        final String ALTERNATIVE_REPOSITORY_PATH = "/home/roehrdor/Workspace-oxygen/SQuAT-docker/squat.modifiability/model/alternativeRepository.repository";
        String BASE = MODEL_PATH;
        String basicPath = MODEL_PATH + "/default";

        jsonStringer.key("architecture-instance").object();
        jsonStringer.key("name").value("");

        add(jsonStringer, "repository", new File(basicPath + ".repository"));
        add(jsonStringer, "system", new File(basicPath + ".system"));
        add(jsonStringer, "allocation", new File(basicPath + ".allocation"));
        add(jsonStringer, "resource-environment", new File(basicPath + ".resourceenvironment"));
        add(jsonStringer, "usage-model", new File(basicPath + ".usagemodel"));
        add(jsonStringer, "repository-with-alternatives",
                new File(BASE + "/" + "alternativeRepository" + ".repository"));

        jsonStringer.endObject();

        add(jsonStringer, "cost", new File("" + basicPath + ".cost"));
        add(jsonStringer, "insinter-modular", new File("" + BASE + "/insinter-modular.henshin"));
        add(jsonStringer, "splitrespn-modular", new File("" + BASE + "/splitrespn-modular.henshin"));
        add(jsonStringer, "wrapper-modular", new File("" + BASE + "/wrapper-modular.henshin"));
    }
}

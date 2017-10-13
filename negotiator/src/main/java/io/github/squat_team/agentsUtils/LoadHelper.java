package io.github.squat_team.agentsUtils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

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
     * jsonStringer, keys or values are null.
     *
     * @param jsonStringer the json stringer to serialize the instruction
     * @param op the {@link ModifiabilityOperation} of the instruction
     * @param el the {@link ModifiabilityElement} of the instruction
     * @param keys the parameter keys
     * @param values the parameter values
     */
    private static void createModifiabilityInstruction(JSONStringer jsonStringer, 
            ModifiabilityOperation op, ModifiabilityElement el, 
            String keys[], String values[]) throws NullPointerException {
        Objects.requireNonNull(jsonStringer);
        Objects.requireNonNull(keys);
        Objects.requireNonNull(values);
        if (keys.length != values.length)
            return;
        final int LEN = keys.length;
        jsonStringer.object();
        jsonStringer.key("operation").value(String.valueOf(op));
        jsonStringer.key("element").value(String.valueOf(el));
        jsonStringer.key("parameters").object();
        for (int i = 0; i < LEN; ++i) {
            jsonStringer.key(keys[i]).value(values[i]);
        }
        jsonStringer.endObject();
        jsonStringer.endObject();
    }

    /**
     * Create the first Modifiability scenario
     *
     * @param type the {@link ResponseMeasureType} to use for this scenario
     * @param response the expected response value
     * @param jsonStringer this json stringer is used to insert the scenario
     *  into the JSON object. This {@link JSONStringer} is required to be in 
     *  a state where a key can be created
     */
    public static void createModifiabilityScenarioS1(ResponseMeasureType type, 
            Comparable<Double> response, JSONStringer jsonStringer) {
        jsonStringer.key("scenario");
        jsonStringer.object();

        jsonStringer.key("expectedResult").object();
        jsonStringer.key("responseMeasureType").value(type);
        jsonStringer.key("response").value(String.valueOf(response));
        jsonStringer.endObject();

        jsonStringer.key("type").value(OptimizationType.MINIMIZATION);
        jsonStringer.key("changes").array();

        createModifiabilityInstruction(jsonStringer, ModifiabilityOperation.MODIFY, ModifiabilityElement.INTERFACE, 
            new String[]{"name"}, new String[]{"IExternalPayment"});

        createModifiabilityInstruction(jsonStringer, ModifiabilityOperation.MODIFY, ModifiabilityElement.COMPONENT, 
            new String[]{"name"}, new String[]{"BusinessTripMgmt"});

        jsonStringer.endArray();
        jsonStringer.endObject();
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
    public static void createModifiabilityScenarioS2(ResponseMeasureType type, 
            Comparable<Double> response, JSONStringer jsonStringer) {
        jsonStringer.key("scenario");
        jsonStringer.object();

        jsonStringer.key("expectedResult").object();
        jsonStringer.key("responseMeasureType").value(type);
        jsonStringer.key("response").value(String.valueOf(response));
        jsonStringer.endObject();

        jsonStringer.key("type").value(OptimizationType.MINIMIZATION);
        jsonStringer.key("changes").array();

        createModifiabilityInstruction(jsonStringer, 
            ModifiabilityOperation.MODIFY, ModifiabilityElement.INTERFACE, 
            new String[]{"name"}, new String[]{"ITripDB"});

        createModifiabilityInstruction(jsonStringer, 
            ModifiabilityOperation.CREATE, ModifiabilityElement.INTERFACE, 
            new String[]{"name"}, new String[]{"Analytics"});

        createModifiabilityInstruction(jsonStringer, 
            ModifiabilityOperation.CREATE, ModifiabilityElement.OPERATION, 
            new String[]{"iname", "oname"}, new String[]{"Analytics", "getLastTrips"});

        createModifiabilityInstruction(jsonStringer, 
            ModifiabilityOperation.CREATE, ModifiabilityElement.COMPONENT, 
            new String[]{"name"}, new String[]{"Insights"});

        createModifiabilityInstruction(jsonStringer, 
            ModifiabilityOperation.CREATE, ModifiabilityElement.PROVIDEDROLE, 
            new String[]{"cname", "iname"}, new String[]{"Insights", "Analytics"});

        createModifiabilityInstruction(jsonStringer, 
            ModifiabilityOperation.MODIFY, ModifiabilityElement.REQUIREDROLE, 
            new String[]{"cname", "iname"}, new String[]{"Insights", "ITripDB"});

        jsonStringer.endArray();
        jsonStringer.endObject();
    }

    /**
     * @param file the file to read
     * @return the base64 encoded file
     */
    private static String buildStringFromFile(String file) {
        String ret = "";
        try (RandomAccessFile raf = new RandomAccessFile(new File(file), "r")) {
            byte[] fileContent = new byte[(int)raf.length()];
            raf.read(fileContent);
            ret = Base64.getEncoder().encodeToString(fileContent);
        } catch(IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    private static void addType(JSONStringer jsonStringer, String basicPath, 
            String filename, String filetype) {
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
        add(jsonStringer, "repository-with-alternatives", new File(BASE + "/" + "alternativeRepository" + ".repository"));

        jsonStringer.endObject();

        add(jsonStringer, "cost", new File("" + basicPath + ".cost"));
        add(jsonStringer, "insinter-modular", new File("" + BASE + "/insinter-modular.henshin"));
        add(jsonStringer, "splitrespn-modular", new File("" + BASE + "/splitrespn-modular.henshin"));
        add(jsonStringer, "wrapper-modular", new File("" + BASE + "/wrapper-modular.henshin"));
    }
}

package io.github.squat_team.agentsUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.json.JSONStringer;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;
import org.palladiosimulator.pcm.usagemodel.UsageModel;

import edu.squat.transformations.ArchitecturalVersion;
import io.github.squat_team.json.JSONification;
import io.github.squat_team.model.OptimizationType;
import io.github.squat_team.model.PCMArchitectureInstance;
import io.github.squat_team.model.PCMResult;
import io.github.squat_team.model.PCMScenario;
import io.github.squat_team.model.PCMScenarioResult;
import io.github.squat_team.model.ResponseMeasureType;
import io.github.squat_team.modifiability.ModifiabilityElement;
import io.github.squat_team.modifiability.ModifiabilityOperation;
import io.github.squat_team.util.SQuATHelper;

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
                /*
        Float responseTimeScenario1 = 120f;// 120f;
        Float responseTimeScenario2 = 300f;// 300f;
        Float responseTimePScenario1 = 30f;// 30f;
        Float responseTimePScenario2 = 40f;// 40f;
        PCMScenario m1Scenario = createModifiabilityScenarioS1(ResponseMeasureType.DECIMAL, responseTimeScenario1);
        PCMScenario m2Scenario = createModifiabilityScenarioS2(ResponseMeasureType.DECIMAL, responseTimeScenario2);
        List<SillyBot> bots = new ArrayList<>();
        try {
            ModifiabilitySillyBot m1Bot = new ModifiabilitySillyBot(calculateModifiabilityComplexity(
                    m1Scenario, KAMPPCMBot.TYPE_COMPLEXITY, initialArchitecture), "m1", responseTimeScenario1);
            ModifiabilitySillyBot m2Bot = new ModifiabilitySillyBot(calculateModifiabilityComplexity(
                    m2Scenario, KAMPPCMBot.TYPE_COMPLEXITY, initialArchitecture), "m2", responseTimeScenario2);
            PerformanceSillyBot p1Bot = new PerformanceSillyBot(
                    calculatePerformanceComplexityForScenario(
                            PerformanceScenarioHelper.createScenarioOfWorkload(), initialArchitecture),
                    "p1", responseTimePScenario1);// Workload
            PerformanceSillyBot p2Bot = new PerformanceSillyBot(
                    alculatePerformanceComplexityForScenario(
                            PerformanceScenarioHelper.createScenarioOfCPU(), initialArchitecture),
                    "p2", responseTimePScenario2);// CPU
    
            for (Iterator<ArchitecturalVersion> iterator = architecturalAlternatives.iterator(); iterator.hasNext();) {
                ArchitecturalVersion architecturalVersion = iterator.next();
    
                m1Bot.insertInOrder(new ModifiabilityProposal(
                        calculateModifiabilityComplexity(m1Scenario, KAMPPCMBot.TYPE_COMPLEXITY, architecturalVersion),
                        architecturalVersion.getName()));
                m2Bot.insertInOrder(new ModifiabilityProposal(
                        calculateModifiabilityComplexity(m2Scenario, KAMPPCMBot.TYPE_COMPLEXITY, architecturalVersion),
                        architecturalVersion.getName()));
    
                p1Bot.insertInOrder(
                        new PerformanceProposal(
                                calculatePerformanceComplexityForScenario(
                                        PerformanceScenarioHelper.createScenarioOfWorkload(), architecturalVersion),
                                architecturalVersion.getName()));
                p2Bot.insertInOrder(
                        new PerformanceProposal(
                                calculatePerformanceComplexityForScenario(
                                        PerformanceScenarioHelper.createScenarioOfCPU(), architecturalVersion),
                                architecturalVersion.getName()));
            }
    
            bots.add(m1Bot);
            bots.add(m2Bot);
            bots.add(p1Bot);
            bots.add(p2Bot);
    
            m1Bot.printUtilies();
            m2Bot.printUtilies();
            p1Bot.printUtilies();
            p2Bot.printUtilies();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bots;
    }
    
    // TODO: PA! Adjust this to call the REST Interace instead.
    private float calculateModifiabilityComplexity(PCMScenario scenario, String evaluationType,
            ArchitecturalVersion architecturalVersion) throws Exception {
        boolean debug = true;
        @SuppressWarnings("unchecked")
        Comparable<Float> expectedResponse = scenario.getExpectedResult().getResponse();
        if (debug)
            java.lang.System.out.println("The goal of scenario: " + expectedResponse.toString());
        KAMPPCMBot bot = new KAMPPCMBot(scenario);
        bot.setEvaluationType(evaluationType);
        if (debug)
            java.lang.System.out.println("The evaluation type is: " + evaluationType);
        //
    
        PCMArchitectureInstance model = this.loadSpecificModel(architecturalVersion);
        PCMScenarioResult scenarioResult = bot.analyze(model);
        String satisfaction_alt1 = scenarioResult.isSatisfied() >= 0 ? "SATISFIED" : "NOT SATISFIED";
        if (debug)
            java.lang.System.out
                    .println("The scenario satisfaction with " + model.getName() + " is: " + satisfaction_alt1);
        @SuppressWarnings("unchecked")
        Comparable<Float> response_alt1 = scenarioResult.getResult().getResponse();
        return ((Float) response_alt1).floatValue();
    }
    
    // TODO: PA! Adjust this to call the REST Interace instead.
    private float calculatePerformanceComplexityForScenario(AbstractPerformancePCMScenario scenario,
            ArchitecturalVersion architecturalVersion) {
        PerOpteryxPCMBot bot = PerformanceScenarioHelper.createPCMBot(scenario);
        PCMArchitectureInstance architecture = PerformanceScenarioHelper.createArchitecture(architecturalVersion);
        PCMScenarioResult result = bot.analyze(architecture);
        if (result == null)// is unsolvable
            return 9999f;
        else
            return new Float(result.getResult().getResponse().toString()).floatValue();
    }
    
    // TODO: PA! In the end this could (maybe) be replaced with PCMHelper method
    // from squat utils instead.
    private PCMArchitectureInstance loadSpecificModel(ArchitecturalVersion architecturalVersion) {
        Repository repository = SQuATHelper.loadRepositoryModel(
                architecturalVersion.getPath() + "/" + architecturalVersion.getRepositoryFilename());
        ResourceEnvironment resourceEnvironment = SQuATHelper.loadResourceEnvironmentModel(
                architecturalVersion.getPath() + "/" + architecturalVersion.getResourceEnvironmentFilename());
        System system = SQuATHelper
                .loadSystemModel(architecturalVersion.getPath() + "/" + architecturalVersion.getSystemFilename());
        Allocation allocation = SQuATHelper.loadAllocationModel(
                architecturalVersion.getPath() + "/" + architecturalVersion.getAllocationFilename());
        UsageModel usageModel = SQuATHelper
                .loadUsageModel(architecturalVersion.getPath() + "/" + architecturalVersion.getUsageFilename());
        PCMArchitectureInstance instance = new PCMArchitectureInstance(architecturalVersion.getFileName(), repository,
                system, allocation, resourceEnvironment, usageModel);
        return instance;
    }
    
    private PCMScenario createModifiabilityScenarioS1(ResponseMeasureType type, Comparable<Float> response) {
        // Adding a pickupshop
        ModifiabilityPCMScenario scenario = new ModifiabilityPCMScenario(OptimizationType.MINIMIZATION);
        PCMResult expectedResult = new PCMResult(type);
        expectedResult.setResponse(response);
        scenario.setExpectedResponse(expectedResult);
        //
        ModifiabilityInstruction i1 = new ModifiabilityInstruction();
        i1.operation = ModifiabilityOperation.CREATE;
        i1.element = ModifiabilityElement.COMPONENT;
        i1.parameters.put("name", "org.cocome.pickupshop.UserManager");
        scenario.addChange(i1);
        ModifiabilityInstruction i2 = new ModifiabilityInstruction();
        i2.operation = ModifiabilityOperation.CREATE;
        i2.element = ModifiabilityElement.REQUIREDROLE;
        i2.parameters.put("cname", "org.cocome.pickupshop.UserManager");
        i2.parameters.put("iname", "ILoginManager");
        scenario.addChange(i2);
        ModifiabilityInstruction i3 = new ModifiabilityInstruction();
        i3.operation = ModifiabilityOperation.CREATE;
        i3.element = ModifiabilityElement.INTERFACE;
        i3.parameters.put("name", "IUserManager");
        scenario.addChange(i3);
        ModifiabilityInstruction i4 = new ModifiabilityInstruction();
        i4.operation = ModifiabilityOperation.CREATE;
        i4.element = ModifiabilityElement.PROVIDEDROLE;
        i4.parameters.put("cname", "org.cocome.pickupshop.UserManager");
        i4.parameters.put("iname", "IUserManager");
        scenario.addChange(i4);
        ModifiabilityInstruction i5 = new ModifiabilityInstruction();
        i5.operation = ModifiabilityOperation.CREATE;
        i5.element = ModifiabilityElement.COMPONENT;
        i5.parameters.put("name", "org.cocome.pickupshop.Inventory");
        scenario.addChange(i5);
        ModifiabilityInstruction i6 = new ModifiabilityInstruction();
        i6.operation = ModifiabilityOperation.CREATE;
        i6.element = ModifiabilityElement.REQUIREDROLE;
        i6.parameters.put("cname", "org.cocome.pickupshop.Inventory");
        i6.parameters.put("iname", "IEnterpriseManager");
        scenario.addChange(i6);
        ModifiabilityInstruction i7 = new ModifiabilityInstruction();
        i7.operation = ModifiabilityOperation.CREATE;
        i7.element = ModifiabilityElement.REQUIREDROLE;
        i7.parameters.put("cname", "org.cocome.pickupshop.Inventory");
        i7.parameters.put("iname", "IStoreManager");
        scenario.addChange(i7);
        ModifiabilityInstruction i8 = new ModifiabilityInstruction();
        i8.operation = ModifiabilityOperation.CREATE;
        i8.element = ModifiabilityElement.INTERFACE;
        i8.parameters.put("name", "IInventory");
        scenario.addChange(i8);
        ModifiabilityInstruction i9 = new ModifiabilityInstruction();
        i9.operation = ModifiabilityOperation.CREATE;
        i9.element = ModifiabilityElement.PROVIDEDROLE;
        i9.parameters.put("cname", "org.cocome.pickupshop.Inventory");
        i9.parameters.put("iname", "IInventory");
        scenario.addChange(i9);
        ModifiabilityInstruction i10 = new ModifiabilityInstruction();
        i10.operation = ModifiabilityOperation.CREATE;
        i10.element = ModifiabilityElement.COMPONENT;
        i10.parameters.put("name", "org.cocome.pickupshop.CheckOut");
        scenario.addChange(i10);
        ModifiabilityInstruction i11 = new ModifiabilityInstruction();
        i11.operation = ModifiabilityOperation.CREATE;
        i11.element = ModifiabilityElement.REQUIREDROLE;
        i11.parameters.put("cname", "org.cocome.pickupshop.CheckOut");
        i11.parameters.put("iname", "IBankLocal");
        scenario.addChange(i11);
        ModifiabilityInstruction i12 = new ModifiabilityInstruction();
        i12.operation = ModifiabilityOperation.CREATE;
        i12.element = ModifiabilityElement.REQUIREDROLE;
        i12.parameters.put("cname", "org.cocome.pickupshop.CheckOut");
        i12.parameters.put("iname", "IInventory");
        scenario.addChange(i12);
        ModifiabilityInstruction i13 = new ModifiabilityInstruction();
        i13.operation = ModifiabilityOperation.CREATE;
        i13.element = ModifiabilityElement.INTERFACE;
        i13.parameters.put("name", "ICheckOut");
        scenario.addChange(i13);
        ModifiabilityInstruction i14 = new ModifiabilityInstruction();
        i14.operation = ModifiabilityOperation.CREATE;
        i14.element = ModifiabilityElement.PROVIDEDROLE;
        i14.parameters.put("cname", "org.cocome.pickupshop.CheckOut");
        i14.parameters.put("iname", "ICheckOut");
        scenario.addChange(i14);
        ModifiabilityInstruction i15 = new ModifiabilityInstruction();
        i15.operation = ModifiabilityOperation.CREATE;
        i15.element = ModifiabilityElement.COMPONENT;
        i15.parameters.put("name", "org.cocome.pickupshop.ShoppingCart");
        scenario.addChange(i15);
        ModifiabilityInstruction i16 = new ModifiabilityInstruction();
        i16.operation = ModifiabilityOperation.CREATE;
        i16.element = ModifiabilityElement.REQUIREDROLE;
        i16.parameters.put("cname", "org.cocome.pickupshop.ShoppingCart");
        i16.parameters.put("iname", "IInventory");
        scenario.addChange(i16);
        ModifiabilityInstruction i17 = new ModifiabilityInstruction();
        i17.operation = ModifiabilityOperation.CREATE;
        i17.element = ModifiabilityElement.REQUIREDROLE;
        i17.parameters.put("cname", "org.cocome.pickupshop.ShoppingCart");
        i17.parameters.put("iname", "ICheckOut");
        scenario.addChange(i17);
        ModifiabilityInstruction i18 = new ModifiabilityInstruction();
        i18.operation = ModifiabilityOperation.CREATE;
        i18.element = ModifiabilityElement.REQUIREDROLE;
        i18.parameters.put("cname", "org.cocome.pickupshop.ShoppingCart");
        i18.parameters.put("iname", "IUserManager");
        scenario.addChange(i18);
    
        return scenario;
    }
    
    private PCMScenario createModifiabilityScenarioS2(ResponseMeasureType type, Comparable<Float> response) {
        ModifiabilityPCMScenario scenario = new ModifiabilityPCMScenario(OptimizationType.MINIMIZATION);
        PCMResult expectedResult = new PCMResult(type);
        expectedResult.setResponse(response);
        scenario.setExpectedResponse(expectedResult);
        //
        ModifiabilityInstruction i1 = new ModifiabilityInstruction();
        i1.operation = ModifiabilityOperation.CREATE;
        i1.element = ModifiabilityElement.COMPONENT;
        i1.parameters.put("name", "org.cocome.tradingsystem.inventory.data.persistence.ServiceAdapter");
        scenario.addChange(i1);
        ModifiabilityInstruction i2 = new ModifiabilityInstruction();
        i2.operation = ModifiabilityOperation.CREATE;
        i2.element = ModifiabilityElement.INTERFACE;
        i2.parameters.put("name", "ServiceAdapter");
        scenario.addChange(i2);
        ModifiabilityInstruction i3 = new ModifiabilityInstruction();
        i3.operation = ModifiabilityOperation.CREATE;
        i3.element = ModifiabilityElement.PROVIDEDROLE;
        i3.parameters.put("cname", "org.cocome.tradingsystem.inventory.data.persistence.ServiceAdapter");
        i3.parameters.put("iname", "ServiceAdapter");
        scenario.addChange(i3);
        ModifiabilityInstruction i4 = new ModifiabilityInstruction();
        i4.operation = ModifiabilityOperation.CREATE;
        i4.element = ModifiabilityElement.REQUIREDROLE;
        i4.parameters.put("cname", "org.cocome.tradingsystem.inventory.data.Store");
        i4.parameters.put("iname", "ServiceAdapter");
        scenario.addChange(i4);
        ModifiabilityInstruction i5 = new ModifiabilityInstruction();
        i5.operation = ModifiabilityOperation.CREATE;
        i5.element = ModifiabilityElement.REQUIREDROLE;
        i5.parameters.put("cname", "org.cocome.tradingsystem.inventory.data.Enterprise");
        i5.parameters.put("iname", "ServiceAdapter");
        scenario.addChange(i5);
        ModifiabilityInstruction i6 = new ModifiabilityInstruction();
        i6.operation = ModifiabilityOperation.CREATE;
        i6.element = ModifiabilityElement.REQUIREDROLE;
        i6.parameters.put("cname", "org.cocome.tradingsystem.inventory.data.Persistence");
        i6.parameters.put("iname", "ServiceAdapter");
        scenario.addChange(i6);
        ModifiabilityInstruction i7 = new ModifiabilityInstruction();
        i7.operation = ModifiabilityOperation.CREATE;
        i7.element = ModifiabilityElement.REQUIREDROLE;
        i7.parameters.put("cname", "org.cocome.tradingsystem.inventory.data.UserManager");
        i7.parameters.put("iname", "ServiceAdapter");
        scenario.addChange(i7);
        return scenario;
    }
    */

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
            Comparable<Float> response, JSONStringer jsonStringer) {
        jsonStringer.key("scenario");
        jsonStringer.object();

        jsonStringer.key("expectedResult").object();
        jsonStringer.key("responseMeasureType").value(type);
        jsonStringer.key("response").value(response);
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
        jsonStringer.key("response").value(response);
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
            ModifiabilityOperation.CREATE, ModifiabilityElement.PROVIDEDROLE, 
            new String[]{"cname", "iname"}, new String[]{"Insights", "Analytics"});

        createModifiabilityInstruction(jsonStringer, 
            ModifiabilityOperation.CREATE, ModifiabilityElement.REQUIREDROLE, 
            new String[]{"cname", "iname"}, new String[]{"Insights", "ITripDB"});

        createModifiabilityInstruction(jsonStringer, 
            ModifiabilityOperation.MODIFY, ModifiabilityElement.COMPONENT, 
            new String[]{"name"}, new String[]{"BusinessTripMgmt"});

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

        JSONification jsoNification = new JSONification(jsonStringer);
        jsoNification.add("repository", new File(basicPath + ".repository"));
        jsoNification.add("system", new File(basicPath + ".system"));
        jsoNification.add("allocation", new File(basicPath + ".allocation"));
        jsoNification.add("resource-environment", new File(basicPath + ".resourceenvironment"));
        jsoNification.add("usage-model", new File(basicPath + ".usagemodel"));
        jsoNification.add("repository-with-alternatives", new File(BASE + "/" + "alternativeRepository" + ".repository"));

        jsoNification.add("cost", new File("" + basicPath + ".cost"));
        jsoNification.add("insinter-modular", new File("" + BASE + "/insinter-modular.henshin"));
        jsoNification.add("splitrespn-modular", new File("" + BASE + "/splitrespn-modular.henshin"));
        jsoNification.add("wrapper-modular", new File("" + BASE + "/wrapper-modular.henshin"));

        jsonStringer.endObject();
    }
}

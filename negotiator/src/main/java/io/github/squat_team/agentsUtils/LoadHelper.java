package io.github.squat_team.agentsUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;
import org.palladiosimulator.pcm.usagemodel.UsageModel;

import edu.squat.transformations.ArchitecturalVersion;
import io.github.squat_team.model.OptimizationType;
import io.github.squat_team.model.PCMArchitectureInstance;
import io.github.squat_team.model.PCMResult;
import io.github.squat_team.model.PCMScenario;
import io.github.squat_team.model.PCMScenarioResult;
import io.github.squat_team.model.ResponseMeasureType;
import io.github.squat_team.modifiability.ModifiabilityElement;
import io.github.squat_team.modifiability.ModifiabilityInstruction;
import io.github.squat_team.modifiability.ModifiabilityOperation;
import io.github.squat_team.modifiability.ModifiabilityPCMScenario;
import io.github.squat_team.modifiability.kamp.KAMPPCMBot;
import io.github.squat_team.performance.AbstractPerformancePCMScenario;
import io.github.squat_team.performance.peropteryx.PerOpteryxPCMBot;
import io.github.squat_team.util.SQuATHelper;

/**
 * Sets up the specific {@link SillyBot} and scenarios used in this specific
 * case study. TODO: PA! Adjust for use with REST Interface and set the correct
 * scenarios for stplus. (see commented out)
 */
public class LoadHelper implements ILoadHelper {

	public List<SillyBot> loadBotsForArchitecturalAlternatives(List<ArchitecturalVersion> architecturalAlternatives,
			ArchitecturalVersion initialArchitecture) {
		Float responseTimeScenario1 = 120f;// 120f;
		Float responseTimeScenario2 = 300f;// 300f;
		Float responseTimePScenario1 = 30f;// 30f;
		Float responseTimePScenario2 = 40f;// 40f;
		PCMScenario m1Scenario = createModifiabilityScenarioS1(ResponseMeasureType.DECIMAL, responseTimeScenario1);
		PCMScenario m2Scenario = createModifiabilityScenarioS2(ResponseMeasureType.DECIMAL, responseTimeScenario2);
		List<SillyBot> bots = new ArrayList<>();
		try {
			ModifiabilitySillyBot m1Bot = new ModifiabilitySillyBot(/* 115f */calculateModifiabilityComplexity(
					m1Scenario, KAMPPCMBot.TYPE_COMPLEXITY, initialArchitecture), "m1", responseTimeScenario1);
			ModifiabilitySillyBot m2Bot = new ModifiabilitySillyBot(/* 190.5f */calculateModifiabilityComplexity(
					m2Scenario, KAMPPCMBot.TYPE_COMPLEXITY, initialArchitecture), "m2", responseTimeScenario2);
			PerformanceSillyBot p1Bot = new PerformanceSillyBot(
					/* 111.7639f */calculatePerformanceComplexityForScenario(
							PerformanceScenarioHelper.createScenarioOfWorkload(), initialArchitecture),
					"p1", responseTimePScenario1);// Workload
			PerformanceSillyBot p2Bot = new PerformanceSillyBot(
					/* 74.0173f */calculatePerformanceComplexityForScenario(
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
	/**
	 * SCENARIOS OF STPLUS+ private PCMScenario
	 * createModifiabilityScenarioS1(ResponseMeasureType type, Comparable<Float>
	 * response) { ModifiabilityPCMScenario scenario = new
	 * ModifiabilityPCMScenario(OptimizationType.MINIMIZATION); PCMResult
	 * expectedResult = new PCMResult(type); expectedResult.setResponse(response);
	 * scenario.setExpectedResponse(expectedResult); // ModifiabilityInstruction i1
	 * = new ModifiabilityInstruction(); i1.operation =
	 * ModifiabilityOperation.MODIFY; i1.element = ModifiabilityElement.INTERFACE;
	 * i1.parameters.put("name", "IExternalPayment"); scenario.addChange(i1);
	 * ModifiabilityInstruction i2 = new ModifiabilityInstruction(); i2.operation =
	 * ModifiabilityOperation.MODIFY; i2.element = ModifiabilityElement.COMPONENT;
	 * i2.parameters.put("name", "BusinessTripMgmt"); scenario.addChange(i2); //
	 * return scenario; } private PCMScenario
	 * createModifiabilityScenarioS2(ResponseMeasureType type, Comparable<Float>
	 * response) { ModifiabilityPCMScenario scenario = new
	 * ModifiabilityPCMScenario(OptimizationType.MINIMIZATION); PCMResult
	 * expectedResult = new PCMResult(type); expectedResult.setResponse(response);
	 * scenario.setExpectedResponse(expectedResult); // ModifiabilityInstruction i1
	 * = new ModifiabilityInstruction(); i1.operation =
	 * ModifiabilityOperation.MODIFY; i1.element = ModifiabilityElement.INTERFACE;
	 * i1.parameters.put("name", "ITripDB"); scenario.addChange(i1);
	 * ModifiabilityInstruction i2 = new ModifiabilityInstruction(); i2.operation =
	 * ModifiabilityOperation.CREATE; i2.element = ModifiabilityElement.INTERFACE;
	 * i2.parameters.put("name", "Analytics"); scenario.addChange(i2);
	 * ModifiabilityInstruction i3 = new ModifiabilityInstruction(); i3.operation =
	 * ModifiabilityOperation.CREATE; i3.element = ModifiabilityElement.OPERATION;
	 * i3.parameters.put("iname", "Analytics"); i3.parameters.put("oname",
	 * "getLastTrips"); scenario.addChange(i3); ModifiabilityInstruction i4 = new
	 * ModifiabilityInstruction(); i4.operation = ModifiabilityOperation.CREATE;
	 * i4.element = ModifiabilityElement.COMPONENT; i4.parameters.put("name",
	 * "Insights"); scenario.addChange(i4); ModifiabilityInstruction i5 = new
	 * ModifiabilityInstruction(); i5.operation = ModifiabilityOperation.CREATE;
	 * i5.element = ModifiabilityElement.PROVIDEDROLE; i5.parameters.put("cname",
	 * "Insights"); i5.parameters.put("iname", "Analytics"); scenario.addChange(i5);
	 * ModifiabilityInstruction i6 = new ModifiabilityInstruction(); i6.operation =
	 * ModifiabilityOperation.CREATE; i6.element =
	 * ModifiabilityElement.REQUIREDROLE; i6.parameters.put("cname", "Insights");
	 * i6.parameters.put("iname", "ITripDB"); scenario.addChange(i6); // return
	 * scenario; }
	 **/

}

package io.github.squat_team.modifiability.kamp;

import java.util.Vector;

import org.junit.Assert;
import org.junit.Test;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryComponent;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;
import org.palladiosimulator.pcm.usagemodel.UsageModel;

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
import io.github.squat_team.util.SQuATHelper;

public class SimpleTacticsPlusTest {
	private String machinePath;
	private String dirPath;
	private String[] modelNames;
	//private String stplus_base = "stplus";
	//private String stplus_alt0 = "stplus-0-Payment System";
	//private String stplus_alt1 = "stplus-1-Payment System";
	private String[] repositoryFile;
	private String[] resourceEnvironmentFile;
	private String[] baseSystemFile;
	private String[] baseAllocationFile;
	private String[] baseUsageFile;

	
	private void loadPCMModel() {
		 //machinePath = "/Users/santiagovidal/Documents/Programacion/kamp-test/squat-tool/src/test/resources/";
		 //dirPath = machinePath + "io/github/squat_team/casestudies/SimpleTactics+/";
		//modelNames = new String[]{"stplus","stplus-0-Payment System","stplus-1-Payment System"};
		 machinePath ="file:\\E:\\Downloads\\eclipse-modifiability-bot-workspace\\model\\"/*"/Users/santiagovidal/Downloads/"*/;
		 dirPath = machinePath /* + "stplus-final/"*/;
		 modelNames = new String[]{"stplus","stplus-0-Payment System","stplus-0-IExporter","stplus-1-ITripDB","stplus-2-IExternalPayment","stplus-3-IEmployeePayment","stplus-4-IBooking","stplus-5-IBusiness Trip","stplus-split-0-IExporter","stplus-split-1-ITripDB","stplus-split-2-IExternalPayment", "stplus-split-3-IEmployeePayment","stplus-split-4-IBooking","stplus-split-5-IBusiness Trip"};
		 repositoryFile=new String[modelNames.length];
		 resourceEnvironmentFile=new String[modelNames.length];
		 baseSystemFile=new String[modelNames.length];
		 baseAllocationFile=new String[modelNames.length];
		 baseUsageFile=new String[modelNames.length];
		 for (int i = 0; i < modelNames.length; i++) {
			 repositoryFile[i]=dirPath + modelNames[i] + ".repository";
			 resourceEnvironmentFile[i]= dirPath + modelNames[i] + ".resourceenvironment";
			 baseSystemFile[i]=dirPath + modelNames[i] + ".system";
			 baseAllocationFile[i]= dirPath + modelNames[i] + ".allocation";
			 baseUsageFile[i]=dirPath + modelNames[i] + ".usagemodel";
		 }
		 
	}
	
	
	@SuppressWarnings("rawtypes")
	// Deactivated for Docker: @Test
	public void testAnalysis() throws Exception {
		PCMScenario scenario = this.createModifiabilityScenario();
		Comparable response_expected = scenario.getExpectedResult().getResponse();
		java.lang.System.out.println("The goal of the scenario is: " + ((Float)response_expected).floatValue());
		KAMPPCMBotDeprecated bot = new KAMPPCMBotDeprecated(scenario);
		//
		loadPCMModel();
		Vector<PCMArchitectureInstance> pcmInstances=new Vector<PCMArchitectureInstance>();
		for (int i = 0; i < modelNames.length; i++) {
			PCMArchitectureInstance model=loadSpecificModel(repositoryFile[i], repositoryFile[i], resourceEnvironmentFile[i], baseSystemFile[i], baseAllocationFile[i], baseUsageFile[i]);
			PCMScenarioResult scenarioResult = bot.analyze(model);
			String satisfaction_alt1 = scenarioResult.isSatisfied() >= 0 ? "SATISFIED" : "NOT SATISFIED";
			java.lang.System.out.println("The scenario satisfaction with " + model.getName() + " is: " + satisfaction_alt1);
			Comparable response_alt1 = scenarioResult.getResult().getResponse();
			java.lang.System.out.println("The number of affected components is: " + ((Float)response_alt1).floatValue());
		}
		/*PCMArchitectureInstance stplus_base = this.loadSimpleTacticsPlus("SimpleTactics+BASE");
		PCMArchitectureInstance stplus_alt0 = this.loadSimpleTacticsPlusAlt0("SimpleTactics+ALT0");
		PCMArchitectureInstance stplus_alt1 = this.loadSimpleTacticsPlusAlt1("SimpleTactics+ALT1");*/
		//
		/*PCMScenarioResult scenarioResult_base = bot.analyze(stplus_base);
		String satisfaction_base = scenarioResult_base.isSatisfied() >= 0 ? "SATISFIED" : "NOT SATISFIED";
		java.lang.System.out.println("The scenario satisfaction with " + stplus_base.getName() + " is: " + satisfaction_base);*/
		//Assert.assertTrue(scenarioResult_base.isSatisfied() < 0);
		/*int AFFECTED_COMPONENTS = 6;
		Comparable response_base = scenarioResult_base.getResult().getResponse();
		java.lang.System.out.println("The number of affected components is: " + ((Float)response_base).floatValue());*/
	//	Assert.assertEquals(((Float)response_base).floatValue(), AFFECTED_COMPONENTS);
		//
		/*PCMScenarioResult scenarioResult_alt0 = bot.analyze(stplus_alt0);
		String satisfaction_alt0 = scenarioResult_alt0.isSatisfied() >= 0 ? "SATISFIED" : "NOT SATISFIED";
		java.lang.System.out.println("The scenario satisfaction with " + stplus_alt0.getName() + " is: " + satisfaction_alt0);*/
	//	Assert.assertTrue(scenarioResult_alt0.isSatisfied() < 0);
		//int AFFECTED_COMPONENTS = 6;
		/*Comparable response_alt0 = scenarioResult_alt0.getResult().getResponse();
		java.lang.System.out.println("The number of affected components is: " + ((Float)response_alt0).floatValue());*/
		//Assert.assertEquals(((Integer)response_alt0).intValue(), AFFECTED_COMPONENTS);
		//
	/*	PCMScenarioResult scenarioResult_alt1 = bot.analyze(stplus_alt1);
		String satisfaction_alt1 = scenarioResult_alt0.isSatisfied() >= 0 ? "SATISFIED" : "NOT SATISFIED";
		java.lang.System.out.println("The scenario satisfaction with " + stplus_alt1.getName() + " is: " + satisfaction_alt1);*/
	//	Assert.assertTrue(scenarioResult_alt1.isSatisfied() < 0);
		//int AFFECTED_COMPONENTS = 6;
	/*	Comparable response_alt1 = scenarioResult_alt1.getResult().getResponse();
		java.lang.System.out.println("The number of affected components is: " + ((Float)response_alt1).floatValue());*/
		//Assert.assertEquals(((Integer)response_alt1).intValue(), AFFECTED_COMPONENTS);
	}
	
	

	//@Test
	public void testAlternatives() {
		PCMScenario scenario = this.createModifiabilityScenario();
		KAMPPCMBotDeprecated bot = new KAMPPCMBotDeprecated(scenario);
	//	PCMArchitectureInstance stplus = loadSimpleTacticsPlus("SimpleTactics+");
	}
	

	private PCMScenario createModifiabilityScenario() {
		/*ModifiabilityPCMScenario scenario = new ModifiabilityPCMScenario(OptimizationType.MINIMIZATION);
		PCMResult expectedResult = new PCMResult(ResponseMeasureType.DECIMAL);
		expectedResult.setResponse(new Float(5));
		scenario.setExpectedResponse(expectedResult);
		ModifiabilityInstruction i1 = new ModifiabilityInstruction();
		i1.operation = ModifiabilityOperation.MODIFY;
		i1.element = ModifiabilityElement.INTERFACE;
		i1.parameters.put("name", "IExternalPayment");
		scenario.addChange(i1);
		ModifiabilityInstruction i2 = new ModifiabilityInstruction();
		i2.operation = ModifiabilityOperation.MODIFY;
		i2.element = ModifiabilityElement.COMPONENT;
		i2.parameters.put("name", "BusinessTripMgmt");
		scenario.addChange(i2);*/
		
		/*ModifiabilityPCMScenario scenario = new ModifiabilityPCMScenario(OptimizationType.MINIMIZATION);
		PCMResult expectedResult = new PCMResult(ResponseMeasureType.DECIMAL);
		expectedResult.setResponse(new Float(5));
		scenario.setExpectedResponse(expectedResult);
		ModifiabilityInstruction i1 = new ModifiabilityInstruction();
		i1.operation = ModifiabilityOperation.MODIFY;
		i1.element = ModifiabilityElement.INTERFACE;
		i1.parameters.put("name", "IExternalPayment");
		scenario.addChange(i1);
		ModifiabilityInstruction i2 = new ModifiabilityInstruction();
		i2.operation = ModifiabilityOperation.CREATE;
		i2.element = ModifiabilityElement.OPERATION;
		i2.parameters.put("iname", "IExternalPayment");
		i2.parameters.put("oname", "specialPay");
		scenario.addChange(i2);
		ModifiabilityInstruction i3 = new ModifiabilityInstruction();
		i3.operation = ModifiabilityOperation.MODIFY;
		i3.element = ModifiabilityElement.COMPONENT;
		i3.parameters.put("name", "BusinessTripMgmt");
		scenario.addChange(i3);*/
		
		
		ModifiabilityPCMScenario scenario = new ModifiabilityPCMScenario(OptimizationType.MINIMIZATION);
		PCMResult expectedResult = new PCMResult(ResponseMeasureType.DECIMAL);
		expectedResult.setResponse(new Float(5));
		scenario.setExpectedResponse(expectedResult);
		ModifiabilityInstruction i1 = new ModifiabilityInstruction();
		i1.operation = ModifiabilityOperation.MODIFY;
		i1.element = ModifiabilityElement.INTERFACE;
		i1.parameters.put("name", "ITripDB");
		scenario.addChange(i1);
		
		ModifiabilityInstruction i2 = new ModifiabilityInstruction();
		i2.operation = ModifiabilityOperation.CREATE;
		i2.element = ModifiabilityElement.INTERFACE;
		i2.parameters.put("name", "Analytics");
		scenario.addChange(i2);
		
		ModifiabilityInstruction i3 = new ModifiabilityInstruction();
		i3.operation = ModifiabilityOperation.CREATE;
		i3.element = ModifiabilityElement.OPERATION;
		i3.parameters.put("iname", "Analytics");
		i3.parameters.put("oname", "getLastTrips");
		scenario.addChange(i3);
		
		ModifiabilityInstruction i4 = new ModifiabilityInstruction();
		i4.operation = ModifiabilityOperation.CREATE;
		i4.element = ModifiabilityElement.COMPONENT;
		i4.parameters.put("name", "Insights");
		scenario.addChange(i4);
		
		ModifiabilityInstruction i5 = new ModifiabilityInstruction();
		i5.operation = ModifiabilityOperation.CREATE;
		i5.element = ModifiabilityElement.PROVIDEDROLE;
		i5.parameters.put("cname", "Insights");
		i5.parameters.put("iname", "Analytics");
		scenario.addChange(i5);
		
		ModifiabilityInstruction i6 = new ModifiabilityInstruction();
		i6.operation = ModifiabilityOperation.CREATE;
		i6.element = ModifiabilityElement.REQUIREDROLE;
		i6.parameters.put("cname", "Insights");
		i6.parameters.put("iname", "ITripDB");
		scenario.addChange(i6);
		
		
		
		/*SCENARIO 2: this scenarios doesn't work because Kamp doesn't propagate changes to components when an operation is added to an interface  
		 * ModifiabilityPCMScenario scenario = new ModifiabilityPCMScenario(OptimizationType.MINIMIZATION);
		PCMResult expectedResult = new PCMResult(ResponseMeasureType.DECIMAL);
		expectedResult.setResponse(new Float(5));
		scenario.setExpectedResponse(expectedResult);
		ModifiabilityInstruction i1 = new ModifiabilityInstruction();
		i1.operation = ModifiabilityOperation.CREATE;
		i1.element = ModifiabilityElement.OPERATION;
		i1.parameters.put("iname", "ITripDB");
		i1.parameters.put("oname", "executeReport");
		scenario.addChange(i1);
		
		ModifiabilityInstruction i2 = new ModifiabilityInstruction();
		i2.operation = ModifiabilityOperation.CREATE;
		i2.element = ModifiabilityElement.INTERFACE;
		i2.parameters.put("name", "Analytics");
		scenario.addChange(i2);
		
		ModifiabilityInstruction i3 = new ModifiabilityInstruction();
		i3.operation = ModifiabilityOperation.CREATE;
		i3.element = ModifiabilityElement.OPERATION;
		i3.parameters.put("iname", "Analytics");
		i3.parameters.put("oname", "getLastTrips");
		scenario.addChange(i3);
		
		
		ModifiabilityInstruction i4 = new ModifiabilityInstruction();
		i4.operation = ModifiabilityOperation.CREATE;
		i4.element = ModifiabilityElement.COMPONENT;
		i4.parameters.put("name", "Insights");
		scenario.addChange(i4);
		
		ModifiabilityInstruction i5 = new ModifiabilityInstruction();
		i5.operation = ModifiabilityOperation.CREATE;
		i5.element = ModifiabilityElement.PROVIDEDROLE;
		i5.parameters.put("cname", "Insights");
		i5.parameters.put("iname", "Analytics");
		scenario.addChange(i5);
		
		ModifiabilityInstruction i6 = new ModifiabilityInstruction();
		i6.operation = ModifiabilityOperation.CREATE;
		i6.element = ModifiabilityElement.REQUIREDROLE;
		i6.parameters.put("cname", "Insights");
		i6.parameters.put("iname", "ITripDB");
		scenario.addChange(i6);*/
		
		
		
		
		return scenario;
	}
	
	private PCMArchitectureInstance loadSpecificModel(String name, String repositoryFile,String enviromentFile,String systemFile, String sllocationFile, String usageFile){
		Repository repository = SQuATHelper.loadRepositoryModel(repositoryFile);
		ResourceEnvironment resourceEnvironment = SQuATHelper.loadResourceEnvironmentModel(enviromentFile);
		System system = SQuATHelper.loadSystemModel(systemFile);
		Allocation allocation = SQuATHelper.loadAllocationModel(sllocationFile);
		UsageModel usageModel = SQuATHelper.loadUsageModel(usageFile);
		PCMArchitectureInstance instance = new PCMArchitectureInstance(name, repository, system, allocation, resourceEnvironment, usageModel);
		return instance;
	}
	
	/*private PCMArchitectureInstance loadSimpleTacticsPlus(String name) {
		Repository repository = SQuATHelper.loadRepositoryModel(repositoryFile[0]);
		ResourceEnvironment resourceEnvironment = SQuATHelper.loadResourceEnvironmentModel(resourceEnvironmentFile[0]);
		System system = SQuATHelper.loadSystemModel(baseSystemFile[0]);
		Allocation allocation = SQuATHelper.loadAllocationModel(baseAllocationFile[0]);
		UsageModel usageModel = SQuATHelper.loadUsageModel(baseUsageFile[0]);
		PCMArchitectureInstance instance = new PCMArchitectureInstance(name, repository, system, allocation, resourceEnvironment, usageModel);
		*/
		/*java.lang.System.out.println("SimpleTacticsPlus");
		for(RepositoryComponent component:repository.getComponents__Repository()){
			KAMPPCMBot bot = new KAMPPCMBot(null);
			java.lang.System.out.println(component.getEntityName()+" "+bot.getComplexityForComponent((BasicComponent) component));
		}*/
		
	/*	return instance;
	}*/
	
	/*private PCMArchitectureInstance loadSimpleTacticsPlusAlt0(String name) {
		Repository repository = SQuATHelper.loadRepositoryModel(repositoryFile[1]);
		ResourceEnvironment resourceEnvironment = SQuATHelper.loadResourceEnvironmentModel(resourceEnvironmentFile[1]);
		System system = SQuATHelper.loadSystemModel(baseSystemFile[1]);
		Allocation allocation = SQuATHelper.loadAllocationModel(baseAllocationFile[1]);
		UsageModel usageModel = SQuATHelper.loadUsageModel(baseUsageFile[1]);
		PCMArchitectureInstance instance = new PCMArchitectureInstance(name, repository, system, allocation, resourceEnvironment, usageModel);*/
		
		/*java.lang.System.out.println("SimpleTacticsPlusAlt0");
		for(RepositoryComponent component:repository.getComponents__Repository()){
			KAMPPCMBot bot = new KAMPPCMBot(null);
			java.lang.System.out.println(component.getEntityName()+" "+bot.getComplexityForComponent((BasicComponent) component));
		}*/
		
	/*	return instance;
	}*/
	
	/*private PCMArchitectureInstance loadSimpleTacticsPlusAlt1(String name) {
		Repository repository = SQuATHelper.loadRepositoryModel(repositoryFile[2]);
		ResourceEnvironment resourceEnvironment = SQuATHelper.loadResourceEnvironmentModel(resourceEnvironmentFile[2]);
		System system = SQuATHelper.loadSystemModel(baseSystemFile[2]);
		Allocation allocation = SQuATHelper.loadAllocationModel(baseAllocationFile[2]);
		UsageModel usageModel = SQuATHelper.loadUsageModel(baseUsageFile[2]);
		PCMArchitectureInstance instance = new PCMArchitectureInstance(name, repository, system, allocation, resourceEnvironment, usageModel);*/
		
		/*java.lang.System.out.println("SimpleTacticsPlusAlt1");
		for(RepositoryComponent component:repository.getComponents__Repository()){
			KAMPPCMBot bot = new KAMPPCMBot(null);
			java.lang.System.out.println(component.getEntityName()+" "+bot.getComplexityForComponent((BasicComponent) component));
		}*/
		
		/*return instance;
	}*/
}

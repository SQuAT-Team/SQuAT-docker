package io.github.squat_team.modifiability.kamp.ecsa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.repository.Repository;
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
import io.github.squat_team.modifiability.kamp.KAMPPCMBotDeprecated;
import io.github.squat_team.util.SQuATHelper;

@SuppressWarnings("rawtypes")
public class SimpleTacticsECSATest {
	private String machinePath;
	private String dirPath;
	private String[] modelNames;
	private String[] repositoryFile;
	private String[] resourceEnvironmentFile;
	private String[] baseSystemFile;
	private String[] baseAllocationFile;
	private String[] baseUsageFile;
	//
	private List<AnalysisResult> results;
	private Map<AnalysisResult, AnalysisResult> resultsMap;
	//
	public SimpleTacticsECSATest() {
		this.results = new ArrayList<AnalysisResult>();
		this.resultsMap = new HashMap<AnalysisResult, AnalysisResult>();
	}
	
	private void setModifiabilityPCMModel() {
		machinePath = "/Users/alejandrorago/Documents/Implementacion/Repositorios/kamp-test/";
		dirPath = machinePath + "squat-tool/src/test/resources/io/github/squat_team/ecsa/modifiability/";
		String[] localPath = new String[] {
				// initial architecture
				"stplus",
				// split resp tactic
				"stplus-0-Payment System",
				// wrapper tactic
				"stplus-0-IExporter", "stplus-1-ITripDB", 
				"stplus-2-IExternalPayment", "stplus-3-IEmployeePayment",
				"stplus-4-IBooking", "stplus-5-IBusiness Trip",
				// split resp + wrapper tactic
				"stplus-0-Payment System-0-IExporter", 
				"stplus-0-Payment System-1-ITripDB", 
				"stplus-0-Payment System-2-IExternalPayment",
				"stplus-0-Payment System-3-IEmployeePayment", 
				"stplus-0-Payment System-4-IBooking", 
				"stplus-0-Payment System-5-IBusiness Trip" };
		modelNames = new String[] {
				// initial architecture
				"stplus-initial",
				// split resp tactic
				"stplus-mod-split(PaymentSystem)",
				// wrapper tactic
				"stplus-mod-wrapper(IExporter)", 
				"stplus-mod-wrapper(ITripDB)", 
				"stplus-mod-wrapper(IExternalPayment)", 
				"stplus-mod-wrapper(IEmployeePayment)",
				"stplus-mod-wrapper(IBooking)", 
				"stplus-mod-wrapper(IBusinessTrip)",
				// split resp + wrapper tactic
				"stplus-mod-split(PaymentSystem)-wrapper(IExporter)", 
				"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)", 
				"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)",
				"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)", 
				"stplus-mod-split(PaymentSystem)-wrapper(IBooking)", 
				"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)" };
		repositoryFile = new String[modelNames.length];
		resourceEnvironmentFile = new String[modelNames.length];
		baseSystemFile = new String[modelNames.length];
		baseAllocationFile = new String[modelNames.length];
		baseUsageFile = new String[modelNames.length];
		for (int i = 0; i < modelNames.length; i++) {
			repositoryFile[i] = dirPath + localPath[i] + ".repository";
			resourceEnvironmentFile[i] = dirPath + localPath[i] + ".resourceenvironment";
			baseSystemFile[i] = dirPath + localPath[i] + ".system";
			baseAllocationFile[i] = dirPath + localPath[i] + ".allocation";
			baseUsageFile[i] = dirPath + localPath[i] + ".usagemodel";
		}
	}
	
	private void setPerformancePCMModel() {
		machinePath = "/Users/alejandrorago/Documents/Implementacion/Repositorios/kamp-test/";
		dirPath = machinePath + "squat-tool/src/test/resources/io/github/squat_team/ecsa/performance/";
		String[] localPath = new String[] {
				// initial architecture
				//"opt-models/stplus_min/stplus",
				// performance scenario 1
				//"opt-models-scenario1/stplus_min/stplus",
				"opt-models-scenario1/candidate258_minPlus/default",
				"opt-models-scenario1/candidate281_minPlus/default",
				"opt-models-scenario1/candidate338_minPlus/default",
				"opt-models-scenario1/candidate340_minPlus/default",
				"opt-models-scenario1/candidate397_minPlus/default",
				"opt-models-scenario1/candidate404_minPlus/default",
				"opt-models-scenario1/candidate436_minPlus/default",
				"opt-models-scenario1/candidate444_minPlus/default",
				"opt-models-scenario1/candidate494_minPlus/default",
				"opt-models-scenario1/candidate64_minPlus/default",
				// performance scenario 2
				//"opt-models-scenario2/stplus_min/stplus",
				"opt-models-scenario2/candidate209_minPlus/default",
				"opt-models-scenario2/candidate22_minPlus/default",
				"opt-models-scenario2/candidate325_minPlus/default",
				"opt-models-scenario2/candidate330_minPlus/default",
				"opt-models-scenario2/candidate358_minPlus/default",
				"opt-models-scenario2/candidate366_minPlus/default",
				"opt-models-scenario2/candidate416_minPlus/default",
				"opt-models-scenario2/candidate476_minPlus/default",
				"opt-models-scenario2/candidate479_minPlus/default",
				"opt-models-scenario2/candidate480_minPlus/default"
		};
		modelNames = new String[] {
				// initial architecture
				//"stplus",
				// performance scenario 1
				//"stplus-ps1",
				"stplus-ps1-258",
				"stplus-ps1-281",
				"stplus-ps1-338",
				"stplus-ps1-340",
				"stplus-ps1-397",
				"stplus-ps1-404",
				"stplus-ps1-436",
				"stplus-ps1-444",
				"stplus-ps1-494",
				"stplus-ps1-64",
				// performance scenario 2
				//"stplus-ps2",
				"stplus-ps2-209",
				"stplus-ps2-22",
				"stplus-ps2-325",
				"stplus-ps2-330",
				"stplus-ps2-358",
				"stplus-ps2-366",
				"stplus-ps2-416",
				"stplus-ps2-476",
				"stplus-ps2-479",
				"stplus-ps2-480"
		};
		repositoryFile = new String[modelNames.length];
		resourceEnvironmentFile = new String[modelNames.length];
		baseSystemFile = new String[modelNames.length];
		baseAllocationFile = new String[modelNames.length];
		baseUsageFile = new String[modelNames.length];
		for (int i = 0; i < modelNames.length; i++) {
			repositoryFile[i] = dirPath + localPath[i] + ".repository";
			resourceEnvironmentFile[i] = dirPath + localPath[i] + ".resourceenvironment";
			baseSystemFile[i] = dirPath + localPath[i] + ".system";
			baseAllocationFile[i] = dirPath + localPath[i] + ".allocation";
			baseUsageFile[i] = dirPath + localPath[i] + ".usagemodel";
		}
	}
	
	private void setPerformanceModifiabilityPCMModel() {
		machinePath = "/Users/alejandrorago/Documents/Implementacion/Repositorios/kamp-test/";
		dirPath = machinePath + "squat-tool/src/test/resources/io/github/squat_team/ecsa/performance-modifiability/";
		String[] localPath = new String[] {
				// performance scenario 1
				// candidate 258
				"ps1/candidate258_minPlus/default-0-IExporter",
				"ps1/candidate258_minPlus/default-1-ITripDB",
				"ps1/candidate258_minPlus/default-2-IExternalPayment",
				"ps1/candidate258_minPlus/default-3-IEmployeePayment",
				"ps1/candidate258_minPlus/default-4-IBooking",
				"ps1/candidate258_minPlus/default-5-IBusiness Trip",
				"ps1/candidate258_minPlus/default-0-Payment System",
				"ps1/candidate258_minPlus/default-0-Payment System-0-IExporter",
				"ps1/candidate258_minPlus/default-0-Payment System-1-ITripDB",
				"ps1/candidate258_minPlus/default-0-Payment System-2-IExternalPayment",
				"ps1/candidate258_minPlus/default-0-Payment System-3-IEmployeePayment",
				"ps1/candidate258_minPlus/default-0-Payment System-4-IBooking",
				"ps1/candidate258_minPlus/default-0-Payment System-5-IBusiness Trip",
				// candidate 281
				"ps1/candidate281_minPlus/default-0-IExporter",
				"ps1/candidate281_minPlus/default-1-ITripDB",
				"ps1/candidate281_minPlus/default-2-IExternalPayment",
				"ps1/candidate281_minPlus/default-3-IEmployeePayment",
				"ps1/candidate281_minPlus/default-4-IBooking",
				"ps1/candidate281_minPlus/default-5-IBusiness Trip",
				"ps1/candidate281_minPlus/default-0-Payment System",
				"ps1/candidate281_minPlus/default-0-Payment System-0-IExporter",
				"ps1/candidate281_minPlus/default-0-Payment System-1-ITripDB",
				"ps1/candidate281_minPlus/default-0-Payment System-2-IExternalPayment",
				"ps1/candidate281_minPlus/default-0-Payment System-3-IEmployeePayment",
				"ps1/candidate281_minPlus/default-0-Payment System-4-IBooking",
				"ps1/candidate281_minPlus/default-0-Payment System-5-IBusiness Trip",
				// candidate 338
				"ps1/candidate338_minPlus/default-0-IExporter",
				"ps1/candidate338_minPlus/default-1-ITripDB",
				"ps1/candidate338_minPlus/default-2-IExternalPayment",
				"ps1/candidate338_minPlus/default-3-IEmployeePayment",
				"ps1/candidate338_minPlus/default-4-IBooking",
				"ps1/candidate338_minPlus/default-5-IBusiness Trip",
				"ps1/candidate338_minPlus/default-0-Payment System",
				"ps1/candidate338_minPlus/default-0-Payment System-0-IExporter",
				"ps1/candidate338_minPlus/default-0-Payment System-1-ITripDB",
				"ps1/candidate338_minPlus/default-0-Payment System-2-IExternalPayment",
				"ps1/candidate338_minPlus/default-0-Payment System-3-IEmployeePayment",
				"ps1/candidate338_minPlus/default-0-Payment System-4-IBooking",
				"ps1/candidate338_minPlus/default-0-Payment System-5-IBusiness Trip",
				// candidate 340
				"ps1/candidate340_minPlus/default-0-IExporter",
				"ps1/candidate340_minPlus/default-1-ITripDB",
				"ps1/candidate340_minPlus/default-2-IExternalPayment",
				"ps1/candidate340_minPlus/default-3-IEmployeePayment",
				"ps1/candidate340_minPlus/default-4-IBooking",
				"ps1/candidate340_minPlus/default-5-IBusiness Trip",
				"ps1/candidate340_minPlus/default-0-Payment System",
				"ps1/candidate340_minPlus/default-0-Payment System-0-IExporter",
				"ps1/candidate340_minPlus/default-0-Payment System-1-ITripDB",
				"ps1/candidate340_minPlus/default-0-Payment System-2-IExternalPayment",
				"ps1/candidate340_minPlus/default-0-Payment System-3-IEmployeePayment",
				"ps1/candidate340_minPlus/default-0-Payment System-4-IBooking",
				"ps1/candidate340_minPlus/default-0-Payment System-5-IBusiness Trip",
				// candidate 397
				"ps1/candidate397_minPlus/default-0-IExporter",
				"ps1/candidate397_minPlus/default-1-ITripDB",
				"ps1/candidate397_minPlus/default-2-IExternalPayment",
				"ps1/candidate397_minPlus/default-3-IEmployeePayment",
				"ps1/candidate397_minPlus/default-4-IBooking",
				"ps1/candidate397_minPlus/default-5-IBusiness Trip",
				"ps1/candidate397_minPlus/default-0-Payment System",
				"ps1/candidate397_minPlus/default-0-Payment System-0-IExporter",
				"ps1/candidate397_minPlus/default-0-Payment System-1-ITripDB",
				"ps1/candidate397_minPlus/default-0-Payment System-2-IExternalPayment",
				"ps1/candidate397_minPlus/default-0-Payment System-3-IEmployeePayment",
				"ps1/candidate397_minPlus/default-0-Payment System-4-IBooking",
				"ps1/candidate397_minPlus/default-0-Payment System-5-IBusiness Trip",
				// candidate 404
				"ps1/candidate404_minPlus/default-0-IExporter",
				"ps1/candidate404_minPlus/default-1-ITripDB",
				"ps1/candidate404_minPlus/default-2-IExternalPayment",
				"ps1/candidate404_minPlus/default-3-IEmployeePayment",
				"ps1/candidate404_minPlus/default-4-IBooking",
				"ps1/candidate404_minPlus/default-5-IBusiness Trip",
				"ps1/candidate404_minPlus/default-0-Payment System",
				"ps1/candidate404_minPlus/default-0-Payment System-0-IExporter",
				"ps1/candidate404_minPlus/default-0-Payment System-1-ITripDB",
				"ps1/candidate404_minPlus/default-0-Payment System-2-IExternalPayment",
				"ps1/candidate404_minPlus/default-0-Payment System-3-IEmployeePayment",
				"ps1/candidate404_minPlus/default-0-Payment System-4-IBooking",
				"ps1/candidate404_minPlus/default-0-Payment System-5-IBusiness Trip",
				// candidate 436
				"ps1/candidate436_minPlus/default-0-IExporter",
				"ps1/candidate436_minPlus/default-1-ITripDB",
				"ps1/candidate436_minPlus/default-2-IExternalPayment",
				"ps1/candidate436_minPlus/default-3-IEmployeePayment",
				"ps1/candidate436_minPlus/default-4-IBooking",
				"ps1/candidate436_minPlus/default-5-IBusiness Trip",
				"ps1/candidate436_minPlus/default-0-Payment System",
				"ps1/candidate436_minPlus/default-0-Payment System-0-IExporter",
				"ps1/candidate436_minPlus/default-0-Payment System-1-ITripDB",
				"ps1/candidate436_minPlus/default-0-Payment System-2-IExternalPayment",
				"ps1/candidate436_minPlus/default-0-Payment System-3-IEmployeePayment",
				"ps1/candidate436_minPlus/default-0-Payment System-4-IBooking",
				"ps1/candidate436_minPlus/default-0-Payment System-5-IBusiness Trip",
				// candidate 444
				"ps1/candidate444_minPlus/default-0-IExporter",
				"ps1/candidate444_minPlus/default-1-ITripDB",
				"ps1/candidate444_minPlus/default-2-IExternalPayment",
				"ps1/candidate444_minPlus/default-3-IEmployeePayment",
				"ps1/candidate444_minPlus/default-4-IBooking",
				"ps1/candidate444_minPlus/default-5-IBusiness Trip",
				"ps1/candidate444_minPlus/default-0-Payment System",
				"ps1/candidate444_minPlus/default-0-Payment System-0-IExporter",
				"ps1/candidate444_minPlus/default-0-Payment System-1-ITripDB",
				"ps1/candidate444_minPlus/default-0-Payment System-2-IExternalPayment",
				"ps1/candidate444_minPlus/default-0-Payment System-3-IEmployeePayment",
				"ps1/candidate444_minPlus/default-0-Payment System-4-IBooking",
				"ps1/candidate444_minPlus/default-0-Payment System-5-IBusiness Trip",
				// candidate 494
				"ps1/candidate494_minPlus/default-0-IExporter",
				"ps1/candidate494_minPlus/default-1-ITripDB",
				"ps1/candidate494_minPlus/default-2-IExternalPayment",
				"ps1/candidate494_minPlus/default-3-IEmployeePayment",
				"ps1/candidate494_minPlus/default-4-IBooking",
				"ps1/candidate494_minPlus/default-5-IBusiness Trip",
				"ps1/candidate494_minPlus/default-0-Payment System",
				"ps1/candidate494_minPlus/default-0-Payment System-0-IExporter",
				"ps1/candidate494_minPlus/default-0-Payment System-1-ITripDB",
				"ps1/candidate494_minPlus/default-0-Payment System-2-IExternalPayment",
				"ps1/candidate494_minPlus/default-0-Payment System-3-IEmployeePayment",
				"ps1/candidate494_minPlus/default-0-Payment System-4-IBooking",
				"ps1/candidate494_minPlus/default-0-Payment System-5-IBusiness Trip",
				// candidate 64
				"ps1/candidate64_minPlus/default-0-IExporter",
				"ps1/candidate64_minPlus/default-1-ITripDB",
				"ps1/candidate64_minPlus/default-2-IExternalPayment",
				"ps1/candidate64_minPlus/default-3-IEmployeePayment",
				"ps1/candidate64_minPlus/default-4-IBooking",
				"ps1/candidate64_minPlus/default-5-IBusiness Trip",
				"ps1/candidate64_minPlus/default-0-Payment System",
				"ps1/candidate64_minPlus/default-0-Payment System-0-IExporter",
				"ps1/candidate64_minPlus/default-0-Payment System-1-ITripDB",
				"ps1/candidate64_minPlus/default-0-Payment System-2-IExternalPayment",
				"ps1/candidate64_minPlus/default-0-Payment System-3-IEmployeePayment",
				"ps1/candidate64_minPlus/default-0-Payment System-4-IBooking",
				"ps1/candidate64_minPlus/default-0-Payment System-5-IBusiness Trip",
				// performance scenario 2
				// candidate 209
				"ps2/candidate209_minPlus/default-0-IExporter",
				"ps2/candidate209_minPlus/default-1-ITripDB",
				"ps2/candidate209_minPlus/default-2-IExternalPayment",
				"ps2/candidate209_minPlus/default-3-IEmployeePayment",
				"ps2/candidate209_minPlus/default-4-IBooking",
				"ps2/candidate209_minPlus/default-5-IBusiness Trip",
				"ps2/candidate209_minPlus/default-0-Payment System",
				"ps2/candidate209_minPlus/default-0-Payment System-0-IExporter",
				"ps2/candidate209_minPlus/default-0-Payment System-1-ITripDB",
				"ps2/candidate209_minPlus/default-0-Payment System-2-IExternalPayment",
				"ps2/candidate209_minPlus/default-0-Payment System-3-IEmployeePayment",
				"ps2/candidate209_minPlus/default-0-Payment System-4-IBooking",
				"ps2/candidate209_minPlus/default-0-Payment System-5-IBusiness Trip",
				// candidate 22
				"ps2/candidate22_minPlus/default-0-IExporter",
				"ps2/candidate22_minPlus/default-1-ITripDB",
				"ps2/candidate22_minPlus/default-2-IExternalPayment",
				"ps2/candidate22_minPlus/default-3-IEmployeePayment",
				"ps2/candidate22_minPlus/default-4-IBooking",
				"ps2/candidate22_minPlus/default-5-IBusiness Trip",
				"ps2/candidate22_minPlus/default-0-Payment System",
				"ps2/candidate22_minPlus/default-0-Payment System-0-IExporter",
				"ps2/candidate22_minPlus/default-0-Payment System-1-ITripDB",
				"ps2/candidate22_minPlus/default-0-Payment System-2-IExternalPayment",
				"ps2/candidate22_minPlus/default-0-Payment System-3-IEmployeePayment",
				"ps2/candidate22_minPlus/default-0-Payment System-4-IBooking",
				"ps2/candidate22_minPlus/default-0-Payment System-5-IBusiness Trip",
				// candidate 325
				"ps2/candidate325_minPlus/default-0-IExporter",
				"ps2/candidate325_minPlus/default-1-ITripDB",
				"ps2/candidate325_minPlus/default-2-IExternalPayment",
				"ps2/candidate325_minPlus/default-3-IEmployeePayment",
				"ps2/candidate325_minPlus/default-4-IBooking",
				"ps2/candidate325_minPlus/default-5-IBusiness Trip",
				"ps2/candidate325_minPlus/default-0-Payment System",
				"ps2/candidate325_minPlus/default-0-Payment System-0-IExporter",
				"ps2/candidate325_minPlus/default-0-Payment System-1-ITripDB",
				"ps2/candidate325_minPlus/default-0-Payment System-2-IExternalPayment",
				"ps2/candidate325_minPlus/default-0-Payment System-3-IEmployeePayment",
				"ps2/candidate325_minPlus/default-0-Payment System-4-IBooking",
				"ps2/candidate325_minPlus/default-0-Payment System-5-IBusiness Trip",
				// candidate 330
				"ps2/candidate330_minPlus/default-0-IExporter",
				"ps2/candidate330_minPlus/default-1-ITripDB",
				"ps2/candidate330_minPlus/default-2-IExternalPayment",
				"ps2/candidate330_minPlus/default-3-IEmployeePayment",
				"ps2/candidate330_minPlus/default-4-IBooking",
				"ps2/candidate330_minPlus/default-5-IBusiness Trip",
				"ps2/candidate330_minPlus/default-0-Payment System",
				"ps2/candidate330_minPlus/default-0-Payment System-0-IExporter",
				"ps2/candidate330_minPlus/default-0-Payment System-1-ITripDB",
				"ps2/candidate330_minPlus/default-0-Payment System-2-IExternalPayment",
				"ps2/candidate330_minPlus/default-0-Payment System-3-IEmployeePayment",
				"ps2/candidate330_minPlus/default-0-Payment System-4-IBooking",
				"ps2/candidate330_minPlus/default-0-Payment System-5-IBusiness Trip",
				// candidate 358
				"ps2/candidate358_minPlus/default-0-IExporter",
				"ps2/candidate358_minPlus/default-1-ITripDB",
				"ps2/candidate358_minPlus/default-2-IExternalPayment",
				"ps2/candidate358_minPlus/default-3-IEmployeePayment",
				"ps2/candidate358_minPlus/default-4-IBooking",
				"ps2/candidate358_minPlus/default-5-IBusiness Trip",
				"ps2/candidate358_minPlus/default-0-Payment System",
				"ps2/candidate358_minPlus/default-0-Payment System-0-IExporter",
				"ps2/candidate358_minPlus/default-0-Payment System-1-ITripDB",
				"ps2/candidate358_minPlus/default-0-Payment System-2-IExternalPayment",
				"ps2/candidate358_minPlus/default-0-Payment System-3-IEmployeePayment",
				"ps2/candidate358_minPlus/default-0-Payment System-4-IBooking",
				"ps2/candidate358_minPlus/default-0-Payment System-5-IBusiness Trip",
				// candidate 366
				"ps2/candidate366_minPlus/default-0-IExporter",
				"ps2/candidate366_minPlus/default-1-ITripDB",
				"ps2/candidate366_minPlus/default-2-IExternalPayment",
				"ps2/candidate366_minPlus/default-3-IEmployeePayment",
				"ps2/candidate366_minPlus/default-4-IBooking",
				"ps2/candidate366_minPlus/default-5-IBusiness Trip",
				"ps2/candidate366_minPlus/default-0-Payment System",
				"ps2/candidate366_minPlus/default-0-Payment System-0-IExporter",
				"ps2/candidate366_minPlus/default-0-Payment System-1-ITripDB",
				"ps2/candidate366_minPlus/default-0-Payment System-2-IExternalPayment",
				"ps2/candidate366_minPlus/default-0-Payment System-3-IEmployeePayment",
				"ps2/candidate366_minPlus/default-0-Payment System-4-IBooking",
				"ps2/candidate366_minPlus/default-0-Payment System-5-IBusiness Trip",
				// candidate 416
				"ps2/candidate416_minPlus/default-0-IExporter",
				"ps2/candidate416_minPlus/default-1-ITripDB",
				"ps2/candidate416_minPlus/default-2-IExternalPayment",
				"ps2/candidate416_minPlus/default-3-IEmployeePayment",
				"ps2/candidate416_minPlus/default-4-IBooking",
				"ps2/candidate416_minPlus/default-5-IBusiness Trip",
				"ps2/candidate416_minPlus/default-0-Payment System",
				"ps2/candidate416_minPlus/default-0-Payment System-0-IExporter",
				"ps2/candidate416_minPlus/default-0-Payment System-1-ITripDB",
				"ps2/candidate416_minPlus/default-0-Payment System-2-IExternalPayment",
				"ps2/candidate416_minPlus/default-0-Payment System-3-IEmployeePayment",
				"ps2/candidate416_minPlus/default-0-Payment System-4-IBooking",
				"ps2/candidate416_minPlus/default-0-Payment System-5-IBusiness Trip",
				// candidate 476
				"ps2/candidate476_minPlus/default-0-IExporter",
				"ps2/candidate476_minPlus/default-1-ITripDB",
				"ps2/candidate476_minPlus/default-2-IExternalPayment",
				"ps2/candidate476_minPlus/default-3-IEmployeePayment",
				"ps2/candidate476_minPlus/default-4-IBooking",
				"ps2/candidate476_minPlus/default-5-IBusiness Trip",
				"ps2/candidate476_minPlus/default-0-Payment System",
				"ps2/candidate476_minPlus/default-0-Payment System-0-IExporter",
				"ps2/candidate476_minPlus/default-0-Payment System-1-ITripDB",
				"ps2/candidate476_minPlus/default-0-Payment System-2-IExternalPayment",
				"ps2/candidate476_minPlus/default-0-Payment System-3-IEmployeePayment",
				"ps2/candidate476_minPlus/default-0-Payment System-4-IBooking",
				"ps2/candidate476_minPlus/default-0-Payment System-5-IBusiness Trip",
				// candidate 479
				"ps2/candidate479_minPlus/default-0-IExporter",
				"ps2/candidate479_minPlus/default-1-ITripDB",
				"ps2/candidate479_minPlus/default-2-IExternalPayment",
				"ps2/candidate479_minPlus/default-3-IEmployeePayment",
				"ps2/candidate479_minPlus/default-4-IBooking",
				"ps2/candidate479_minPlus/default-5-IBusiness Trip",
				"ps2/candidate479_minPlus/default-0-Payment System",
				"ps2/candidate479_minPlus/default-0-Payment System-0-IExporter",
				"ps2/candidate479_minPlus/default-0-Payment System-1-ITripDB",
				"ps2/candidate479_minPlus/default-0-Payment System-2-IExternalPayment",
				"ps2/candidate479_minPlus/default-0-Payment System-3-IEmployeePayment",
				"ps2/candidate479_minPlus/default-0-Payment System-4-IBooking",
				"ps2/candidate479_minPlus/default-0-Payment System-5-IBusiness Trip",
				// candidate 480
				"ps2/candidate480_minPlus/default-0-IExporter",
				"ps2/candidate480_minPlus/default-1-ITripDB",
				"ps2/candidate480_minPlus/default-2-IExternalPayment",
				"ps2/candidate480_minPlus/default-3-IEmployeePayment",
				"ps2/candidate480_minPlus/default-4-IBooking",
				"ps2/candidate480_minPlus/default-5-IBusiness Trip",
				"ps2/candidate480_minPlus/default-0-Payment System",
				"ps2/candidate480_minPlus/default-0-Payment System-0-IExporter",
				"ps2/candidate480_minPlus/default-0-Payment System-1-ITripDB",
				"ps2/candidate480_minPlus/default-0-Payment System-2-IExternalPayment",
				"ps2/candidate480_minPlus/default-0-Payment System-3-IEmployeePayment",
				"ps2/candidate480_minPlus/default-0-Payment System-4-IBooking",
				"ps2/candidate480_minPlus/default-0-Payment System-5-IBusiness Trip",
		};
		

		modelNames = new String[] {
				// initial architecture
				"stplus-initial",
				// split resp tactic
				"stplus-mod-split(PaymentSystem)",
				// wrapper tactic
				"stplus-mod-wrapper(IExporter)", 
				"stplus-mod-wrapper(ITripDB)", 
				"stplus-mod-wrapper(IExternalPayment)", 
				"stplus-mod-wrapper(IEmployeePayment)",
				"stplus-mod-wrapper(IBooking)", 
				"stplus-mod-wrapper(IBusinessTrip)",
				// split resp + wrapper tactic
				"stplus-mod-split(PaymentSystem)-wrapper(IExporter)", 
				"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)", 
				"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)",
				"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)", 
				"stplus-mod-split(PaymentSystem)-wrapper(IBooking)", 
				"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)" };
		
		modelNames = new String[] {
				// initial architecture
				//"stplus",
				// performance scenario
				// candidate 258
				"stplus-ps1-258-mod-wrapper(IExporter)",
				"stplus-ps1-258-mod-wrapper(ITripDB)",
				"stplus-ps1-258-mod-wrapper(IExternalPayment)",
				"stplus-ps1-258-mod-wrapper(IEmployeePayment)",
				"stplus-ps1-258-mod-wrapper(IBooking)",
				"stplus-ps1-258-mod-wrapper(IBusinessTrip)",
				"stplus-ps1-258-mod-split(PaymentSystem)",
				"stplus-ps1-258-mod-split(PaymentSystem)-wrapper(IExporter)",
				"stplus-ps1-258-mod-split(PaymentSystem)-wrapper(ITripDB)",
				"stplus-ps1-258-mod-split(PaymentSystem)-wrapper(IExternalPayment)",
				"stplus-ps1-258-mod-split(PaymentSystem)-wrapper(IEmployeePayment)",
				"stplus-ps1-258-mod-split(PaymentSystem)-wrapper(IBooking)",
				"stplus-ps1-258-mod-split(PaymentSystem)-wrapper(IBusinessTrip)",
				// candidate 281
				"stplus-ps1-281-mod-wrapper(IExporter)",
				"stplus-ps1-281-mod-wrapper(ITripDB)",
				"stplus-ps1-281-mod-wrapper(IExternalPayment)",
				"stplus-ps1-281-mod-wrapper(IEmployeePayment)",
				"stplus-ps1-281-mod-wrapper(IBooking)",
				"stplus-ps1-281-mod-wrapper(IBusinessTrip)",
				"stplus-ps1-281-mod-split(PaymentSystem)",
				"stplus-ps1-281-mod-split(PaymentSystem)-wrapper(IExporter)",
				"stplus-ps1-281-mod-split(PaymentSystem)-wrapper(ITripDB)",
				"stplus-ps1-281-mod-split(PaymentSystem)-wrapper(IExternalPayment)",
				"stplus-ps1-281-mod-split(PaymentSystem)-wrapper(IEmployeePayment)",
				"stplus-ps1-281-mod-split(PaymentSystem)-wrapper(IBooking)",
				"stplus-ps1-281-mod-split(PaymentSystem)-wrapper(IBusinessTrip)",
				// candidate 338
				"stplus-ps1-338-mod-wrapper(IExporter)",
				"stplus-ps1-338-mod-wrapper(ITripDB)",
				"stplus-ps1-338-mod-wrapper(IExternalPayment)",
				"stplus-ps1-338-mod-wrapper(IEmployeePayment)",
				"stplus-ps1-338-mod-wrapper(IBooking)",
				"stplus-ps1-338-mod-wrapper(IBusinessTrip)",
				"stplus-ps1-338-mod-split(PaymentSystem)",
				"stplus-ps1-338-mod-split(PaymentSystem)-wrapper(IExporter)",
				"stplus-ps1-338-mod-split(PaymentSystem)-wrapper(ITripDB)",
				"stplus-ps1-338-mod-split(PaymentSystem)-wrapper(IExternalPayment)",
				"stplus-ps1-338-mod-split(PaymentSystem)-wrapper(IEmployeePayment)",
				"stplus-ps1-338-mod-split(PaymentSystem)-wrapper(IBooking)",
				"stplus-ps1-338-mod-split(PaymentSystem)-wrapper(IBusinessTrip)",
				// candidate 340
				"stplus-ps1-340-mod-wrapper(IExporter)",
				"stplus-ps1-340-mod-wrapper(ITripDB)",
				"stplus-ps1-340-mod-wrapper(IExternalPayment)",
				"stplus-ps1-340-mod-wrapper(IEmployeePayment)",
				"stplus-ps1-340-mod-wrapper(IBooking)",
				"stplus-ps1-340-mod-wrapper(IBusinessTrip)",
				"stplus-ps1-340-mod-split(PaymentSystem)",
				"stplus-ps1-340-mod-split(PaymentSystem)-wrapper(IExporter)",
				"stplus-ps1-340-mod-split(PaymentSystem)-wrapper(ITripDB)",
				"stplus-ps1-340-mod-split(PaymentSystem)-wrapper(IExternalPayment)",
				"stplus-ps1-340-mod-split(PaymentSystem)-wrapper(IEmployeePayment)",
				"stplus-ps1-340-mod-split(PaymentSystem)-wrapper(IBooking)",
				"stplus-ps1-340-mod-split(PaymentSystem)-wrapper(IBusinessTrip)",
				// candidate 397
				"stplus-ps1-397-mod-wrapper(IExporter)",
				"stplus-ps1-397-mod-wrapper(ITripDB)",
				"stplus-ps1-397-mod-wrapper(IExternalPayment)",
				"stplus-ps1-397-mod-wrapper(IEmployeePayment)",
				"stplus-ps1-397-mod-wrapper(IBooking)",
				"stplus-ps1-397-mod-wrapper(IBusinessTrip)",
				"stplus-ps1-397-mod-split(PaymentSystem)",
				"stplus-ps1-397-mod-split(PaymentSystem)-wrapper(IExporter)",
				"stplus-ps1-397-mod-split(PaymentSystem)-wrapper(ITripDB)",
				"stplus-ps1-397-mod-split(PaymentSystem)-wrapper(IExternalPayment)",
				"stplus-ps1-397-mod-split(PaymentSystem)-wrapper(IEmployeePayment)",
				"stplus-ps1-397-mod-split(PaymentSystem)-wrapper(IBooking)",
				"stplus-ps1-397-mod-split(PaymentSystem)-wrapper(IBusinessTrip)",
				// candidate 404
				"stplus-ps1-404-mod-wrapper(IExporter)",
				"stplus-ps1-404-mod-wrapper(ITripDB)",
				"stplus-ps1-404-mod-wrapper(IExternalPayment)",
				"stplus-ps1-404-mod-wrapper(IEmployeePayment)",
				"stplus-ps1-404-mod-wrapper(IBooking)",
				"stplus-ps1-404-mod-wrapper(IBusinessTrip)",
				"stplus-ps1-404-mod-split(PaymentSystem)",
				"stplus-ps1-404-mod-split(PaymentSystem)-wrapper(IExporter)",
				"stplus-ps1-404-mod-split(PaymentSystem)-wrapper(ITripDB)",
				"stplus-ps1-404-mod-split(PaymentSystem)-wrapper(IExternalPayment)",
				"stplus-ps1-404-mod-split(PaymentSystem)-wrapper(IEmployeePayment)",
				"stplus-ps1-404-mod-split(PaymentSystem)-wrapper(IBooking)",
				"stplus-ps1-404-mod-split(PaymentSystem)-wrapper(IBusinessTrip)",
				// candidate 436
				"stplus-ps1-436-mod-wrapper(IExporter)",
				"stplus-ps1-436-mod-wrapper(ITripDB)",
				"stplus-ps1-436-mod-wrapper(IExternalPayment)",
				"stplus-ps1-436-mod-wrapper(IEmployeePayment)",
				"stplus-ps1-436-mod-wrapper(IBooking)",
				"stplus-ps1-436-mod-wrapper(IBusinessTrip)",
				"stplus-ps1-436-mod-split(PaymentSystem)",
				"stplus-ps1-436-mod-split(PaymentSystem)-wrapper(IExporter)",
				"stplus-ps1-436-mod-split(PaymentSystem)-wrapper(ITripDB)",
				"stplus-ps1-436-mod-split(PaymentSystem)-wrapper(IExternalPayment)",
				"stplus-ps1-436-mod-split(PaymentSystem)-wrapper(IEmployeePayment)",
				"stplus-ps1-436-mod-split(PaymentSystem)-wrapper(IBooking)",
				"stplus-ps1-436-mod-split(PaymentSystem)-wrapper(IBusinessTrip)",
				// candidate 444
				"stplus-ps1-444-mod-wrapper(IExporter)",
				"stplus-ps1-444-mod-wrapper(ITripDB)",
				"stplus-ps1-444-mod-wrapper(IExternalPayment)",
				"stplus-ps1-444-mod-wrapper(IEmployeePayment)",
				"stplus-ps1-444-mod-wrapper(IBooking)",
				"stplus-ps1-444-mod-wrapper(IBusinessTrip)",
				"stplus-ps1-444-mod-split(PaymentSystem)",
				"stplus-ps1-444-mod-split(PaymentSystem)-wrapper(IExporter)",
				"stplus-ps1-444-mod-split(PaymentSystem)-wrapper(ITripDB)",
				"stplus-ps1-444-mod-split(PaymentSystem)-wrapper(IExternalPayment)",
				"stplus-ps1-444-mod-split(PaymentSystem)-wrapper(IEmployeePayment)",
				"stplus-ps1-444-mod-split(PaymentSystem)-wrapper(IBooking)",
				"stplus-ps1-444-mod-split(PaymentSystem)-wrapper(IBusinessTrip)",
				// candidate 494
				"stplus-ps1-494-mod-wrapper(IExporter)",
				"stplus-ps1-494-mod-wrapper(ITripDB)",
				"stplus-ps1-494-mod-wrapper(IExternalPayment)",
				"stplus-ps1-494-mod-wrapper(IEmployeePayment)",
				"stplus-ps1-494-mod-wrapper(IBooking)",
				"stplus-ps1-494-mod-wrapper(IBusinessTrip)",
				"stplus-ps1-494-mod-split(PaymentSystem)",
				"stplus-ps1-494-mod-split(PaymentSystem)-wrapper(IExporter)",
				"stplus-ps1-494-mod-split(PaymentSystem)-wrapper(ITripDB)",
				"stplus-ps1-494-mod-split(PaymentSystem)-wrapper(IExternalPayment)",
				"stplus-ps1-494-mod-split(PaymentSystem)-wrapper(IEmployeePayment)",
				"stplus-ps1-494-mod-split(PaymentSystem)-wrapper(IBooking)",
				"stplus-ps1-494-mod-split(PaymentSystem)-wrapper(IBusinessTrip)",
				// candidate 64
				"stplus-ps1-64-mod-wrapper(IExporter)",
				"stplus-ps1-64-mod-wrapper(ITripDB)",
				"stplus-ps1-64-mod-wrapper(IExternalPayment)",
				"stplus-ps1-64-mod-wrapper(IEmployeePayment)",
				"stplus-ps1-64-mod-wrapper(IBooking)",
				"stplus-ps1-64-mod-wrapper(IBusinessTrip)",
				"stplus-ps1-64-mod-split(PaymentSystem)",
				"stplus-ps1-64-mod-split(PaymentSystem)-wrapper(IExporter)",
				"stplus-ps1-64-mod-split(PaymentSystem)-wrapper(ITripDB)",
				"stplus-ps1-64-mod-split(PaymentSystem)-wrapper(IExternalPayment)",
				"stplus-ps1-64-mod-split(PaymentSystem)-wrapper(IEmployeePayment)",
				"stplus-ps1-64-mod-split(PaymentSystem)-wrapper(IBooking)",
				"stplus-ps1-64-mod-split(PaymentSystem)-wrapper(IBusinessTrip)",
				// performance scenario 2
				// candidate 209
				"stplus-ps2-209-mod-wrapper(IExporter)",
				"stplus-ps2-209-mod-wrapper(ITripDB)",
				"stplus-ps2-209-mod-wrapper(IExternalPayment)",
				"stplus-ps2-209-mod-wrapper(IEmployeePayment)",
				"stplus-ps2-209-mod-wrapper(IBooking)",
				"stplus-ps2-209-mod-wrapper(IBusinessTrip)",
				"stplus-ps2-209-mod-split(PaymentSystem)",
				"stplus-ps2-209-mod-split(PaymentSystem)-wrapper(IExporter)",
				"stplus-ps2-209-mod-split(PaymentSystem)-wrapper(ITripDB)",
				"stplus-ps2-209-mod-split(PaymentSystem)-wrapper(IExternalPayment)",
				"stplus-ps2-209-mod-split(PaymentSystem)-wrapper(IEmployeePayment)",
				"stplus-ps2-209-mod-split(PaymentSystem)-wrapper(IBooking)",
				"stplus-ps2-209-mod-split(PaymentSystem)-wrapper(IBusinessTrip)",
				// candidate 22
				"stplus-ps2-22-mod-wrapper(IExporter)",
				"stplus-ps2-22-mod-wrapper(ITripDB)",
				"stplus-ps2-22-mod-wrapper(IExternalPayment)",
				"stplus-ps2-22-mod-wrapper(IEmployeePayment)",
				"stplus-ps2-22-mod-wrapper(IBooking)",
				"stplus-ps2-22-mod-wrapper(IBusinessTrip)",
				"stplus-ps2-22-mod-split(PaymentSystem)",
				"stplus-ps2-22-mod-split(PaymentSystem)-wrapper(IExporter)",
				"stplus-ps2-22-mod-split(PaymentSystem)-wrapper(ITripDB)",
				"stplus-ps2-22-mod-split(PaymentSystem)-wrapper(IExternalPayment)",
				"stplus-ps2-22-mod-split(PaymentSystem)-wrapper(IEmployeePayment)",
				"stplus-ps2-22-mod-split(PaymentSystem)-wrapper(IBooking)",
				"stplus-ps2-22-mod-split(PaymentSystem)-wrapper(IBusinessTrip)",
				// candidate 325
				"stplus-ps2-325-mod-wrapper(IExporter)",
				"stplus-ps2-325-mod-wrapper(ITripDB)",
				"stplus-ps2-325-mod-wrapper(IExternalPayment)",
				"stplus-ps2-325-mod-wrapper(IEmployeePayment)",
				"stplus-ps2-325-mod-wrapper(IBooking)",
				"stplus-ps2-325-mod-wrapper(IBusinessTrip)",
				"stplus-ps2-325-mod-split(PaymentSystem)",
				"stplus-ps2-325-mod-split(PaymentSystem)-wrapper(IExporter)",
				"stplus-ps2-325-mod-split(PaymentSystem)-wrapper(ITripDB)",
				"stplus-ps2-325-mod-split(PaymentSystem)-wrapper(IExternalPayment)",
				"stplus-ps2-325-mod-split(PaymentSystem)-wrapper(IEmployeePayment)",
				"stplus-ps2-325-mod-split(PaymentSystem)-wrapper(IBooking)",
				"stplus-ps2-325-mod-split(PaymentSystem)-wrapper(IBusinessTrip)",
				// candidate 330
				"stplus-ps2-330-mod-wrapper(IExporter)",
				"stplus-ps2-330-mod-wrapper(ITripDB)",
				"stplus-ps2-330-mod-wrapper(IExternalPayment)",
				"stplus-ps2-330-mod-wrapper(IEmployeePayment)",
				"stplus-ps2-330-mod-wrapper(IBooking)",
				"stplus-ps2-330-mod-wrapper(IBusinessTrip)",
				"stplus-ps2-330-mod-split(PaymentSystem)",
				"stplus-ps2-330-mod-split(PaymentSystem)-wrapper(IExporter)",
				"stplus-ps2-330-mod-split(PaymentSystem)-wrapper(ITripDB)",
				"stplus-ps2-330-mod-split(PaymentSystem)-wrapper(IExternalPayment)",
				"stplus-ps2-330-mod-split(PaymentSystem)-wrapper(IEmployeePayment)",
				"stplus-ps2-330-mod-split(PaymentSystem)-wrapper(IBooking)",
				"stplus-ps2-330-mod-split(PaymentSystem)-wrapper(IBusinessTrip)",
				// candidate 358
				"stplus-ps2-358-mod-wrapper(IExporter)",
				"stplus-ps2-358-mod-wrapper(ITripDB)",
				"stplus-ps2-358-mod-wrapper(IExternalPayment)",
				"stplus-ps2-358-mod-wrapper(IEmployeePayment)",
				"stplus-ps2-358-mod-wrapper(IBooking)",
				"stplus-ps2-358-mod-wrapper(IBusinessTrip)",
				"stplus-ps2-358-mod-split(PaymentSystem)",
				"stplus-ps2-358-mod-split(PaymentSystem)-wrapper(IExporter)",
				"stplus-ps2-358-mod-split(PaymentSystem)-wrapper(ITripDB)",
				"stplus-ps2-358-mod-split(PaymentSystem)-wrapper(IExternalPayment)",
				"stplus-ps2-358-mod-split(PaymentSystem)-wrapper(IEmployeePayment)",
				"stplus-ps2-358-mod-split(PaymentSystem)-wrapper(IBooking)",
				"stplus-ps2-358-mod-split(PaymentSystem)-wrapper(IBusinessTrip)",
				// candidate 366
				"stplus-ps2-366-mod-wrapper(IExporter)",
				"stplus-ps2-366-mod-wrapper(ITripDB)",
				"stplus-ps2-366-mod-wrapper(IExternalPayment)",
				"stplus-ps2-366-mod-wrapper(IEmployeePayment)",
				"stplus-ps2-366-mod-wrapper(IBooking)",
				"stplus-ps2-366-mod-wrapper(IBusinessTrip)",
				"stplus-ps2-366-mod-split(PaymentSystem)",
				"stplus-ps2-366-mod-split(PaymentSystem)-wrapper(IExporter)",
				"stplus-ps2-366-mod-split(PaymentSystem)-wrapper(ITripDB)",
				"stplus-ps2-366-mod-split(PaymentSystem)-wrapper(IExternalPayment)",
				"stplus-ps2-366-mod-split(PaymentSystem)-wrapper(IEmployeePayment)",
				"stplus-ps2-366-mod-split(PaymentSystem)-wrapper(IBooking)",
				"stplus-ps2-366-mod-split(PaymentSystem)-wrapper(IBusinessTrip)",
				// candidate 416
				"stplus-ps2-416-mod-wrapper(IExporter)",
				"stplus-ps2-416-mod-wrapper(ITripDB)",
				"stplus-ps2-416-mod-wrapper(IExternalPayment)",
				"stplus-ps2-416-mod-wrapper(IEmployeePayment)",
				"stplus-ps2-416-mod-wrapper(IBooking)",
				"stplus-ps2-416-mod-wrapper(IBusinessTrip)",
				"stplus-ps2-416-mod-split(PaymentSystem)",
				"stplus-ps2-416-mod-split(PaymentSystem)-wrapper(IExporter)",
				"stplus-ps2-416-mod-split(PaymentSystem)-wrapper(ITripDB)",
				"stplus-ps2-416-mod-split(PaymentSystem)-wrapper(IExternalPayment)",
				"stplus-ps2-416-mod-split(PaymentSystem)-wrapper(IEmployeePayment)",
				"stplus-ps2-416-mod-split(PaymentSystem)-wrapper(IBooking)",
				"stplus-ps2-416-mod-split(PaymentSystem)-wrapper(IBusinessTrip)",
				// candidate 476
				"stplus-ps2-476-mod-wrapper(IExporter)",
				"stplus-ps2-476-mod-wrapper(ITripDB)",
				"stplus-ps2-476-mod-wrapper(IExternalPayment)",
				"stplus-ps2-476-mod-wrapper(IEmployeePayment)",
				"stplus-ps2-476-mod-wrapper(IBooking)",
				"stplus-ps2-476-mod-wrapper(IBusinessTrip)",
				"stplus-ps2-476-mod-split(PaymentSystem)",
				"stplus-ps2-476-mod-split(PaymentSystem)-wrapper(IExporter)",
				"stplus-ps2-476-mod-split(PaymentSystem)-wrapper(ITripDB)",
				"stplus-ps2-476-mod-split(PaymentSystem)-wrapper(IExternalPayment)",
				"stplus-ps2-476-mod-split(PaymentSystem)-wrapper(IEmployeePayment)",
				"stplus-ps2-476-mod-split(PaymentSystem)-wrapper(IBooking)",
				"stplus-ps2-476-mod-split(PaymentSystem)-wrapper(IBusinessTrip)",
				// candidate 479
				"stplus-ps2-479-mod-wrapper(IExporter)",
				"stplus-ps2-479-mod-wrapper(ITripDB)",
				"stplus-ps2-479-mod-wrapper(IExternalPayment)",
				"stplus-ps2-479-mod-wrapper(IEmployeePayment)",
				"stplus-ps2-479-mod-wrapper(IBooking)",
				"stplus-ps2-479-mod-wrapper(IBusinessTrip)",
				"stplus-ps2-479-mod-split(PaymentSystem)",
				"stplus-ps2-479-mod-split(PaymentSystem)-wrapper(IExporter)",
				"stplus-ps2-479-mod-split(PaymentSystem)-wrapper(ITripDB)",
				"stplus-ps2-479-mod-split(PaymentSystem)-wrapper(IExternalPayment)",
				"stplus-ps2-479-mod-split(PaymentSystem)-wrapper(IEmployeePayment)",
				"stplus-ps2-479-mod-split(PaymentSystem)-wrapper(IBooking)",
				"stplus-ps2-479-mod-split(PaymentSystem)-wrapper(IBusinessTrip)",
				// candidate 480
				"stplus-ps2-480-mod-wrapper(IExporter)",
				"stplus-ps2-480-mod-wrapper(ITripDB)",
				"stplus-ps2-480-mod-wrapper(IExternalPayment)",
				"stplus-ps2-480-mod-wrapper(IEmployeePayment)",
				"stplus-ps2-480-mod-wrapper(IBooking)",
				"stplus-ps2-480-mod-wrapper(IBusinessTrip)",
				"stplus-ps2-480-mod-split(PaymentSystem)",
				"stplus-ps2-480-mod-split(PaymentSystem)-wrapper(IExporter)",
				"stplus-ps2-480-mod-split(PaymentSystem)-wrapper(ITripDB)",
				"stplus-ps2-480-mod-split(PaymentSystem)-wrapper(IExternalPayment)",
				"stplus-ps2-480-mod-split(PaymentSystem)-wrapper(IEmployeePayment)",
				"stplus-ps2-480-mod-split(PaymentSystem)-wrapper(IBooking)",
				"stplus-ps2-480-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"
		};
		repositoryFile = new String[modelNames.length];
		resourceEnvironmentFile = new String[modelNames.length];
		baseSystemFile = new String[modelNames.length];
		baseAllocationFile = new String[modelNames.length];
		baseUsageFile = new String[modelNames.length];
		for (int i = 0; i < modelNames.length; i++) {
			repositoryFile[i] = dirPath + localPath[i] + ".repository";
			resourceEnvironmentFile[i] = dirPath + localPath[i] + ".resourceenvironment";
			baseSystemFile[i] = dirPath + localPath[i] + ".system";
			baseAllocationFile[i] = dirPath + localPath[i] + ".allocation";
			baseUsageFile[i] = dirPath + localPath[i] + ".usagemodel";
		}
	}
	
	private void setModifiabilityPerformancePCMModel() {
		machinePath = "/Users/alejandrorago/Documents/Implementacion/Repositorios/kamp-test/";
		dirPath = machinePath + "squat-tool/src/test/resources/io/github/squat_team/ecsa/modifiability-performance/";
		String[] localPath = new String[] {
				// performance scenario 1
				"final-scenario1-scenario2/stplus-0-Payment System/stplus-0-Payment System/20170415-224038/candidate136/default",
				"final-scenario1-scenario2/stplus-0-Payment System/stplus-0-Payment System/20170415-224038/candidate162/default",
				"final-scenario1-scenario2/stplus-0-Payment System/stplus-0-Payment System/20170415-224038/candidate208/default",
				"final-scenario1-scenario2/stplus-0-Payment System/stplus-0-Payment System/20170415-224038/candidate232/default",
				"final-scenario1-scenario2/stplus-0-Payment System/stplus-0-Payment System/20170415-224038/candidate239/default",
				"final-scenario1-scenario2/stplus-0-Payment System/stplus-0-Payment System/20170415-224038/candidate316/default",
				"final-scenario1-scenario2/stplus-0-Payment System/stplus-0-Payment System/20170415-224038/candidate321/default",
				"final-scenario1-scenario2/stplus-0-Payment System/stplus-0-Payment System/20170415-224038/candidate355/default",
				"final-scenario1-scenario2/stplus-0-Payment System/stplus-0-Payment System/20170415-224038/candidate367/default",
				"final-scenario1-scenario2/stplus-0-Payment System/stplus-0-Payment System/20170415-224038/candidate419/default",
				"final-scenario1-scenario2/stplus-0-IExporter/stplus-0-IExporter/20170415-223924/candidate66/default",
				"final-scenario1-scenario2/stplus-0-IExporter/stplus-0-IExporter/20170415-223924/candidate141/default",
				"final-scenario1-scenario2/stplus-0-IExporter/stplus-0-IExporter/20170415-223924/candidate227/default",
				"final-scenario1-scenario2/stplus-0-IExporter/stplus-0-IExporter/20170415-223924/candidate238/default",
				"final-scenario1-scenario2/stplus-0-IExporter/stplus-0-IExporter/20170415-223924/candidate275/default",
				"final-scenario1-scenario2/stplus-0-IExporter/stplus-0-IExporter/20170415-223924/candidate329/default",
				"final-scenario1-scenario2/stplus-0-IExporter/stplus-0-IExporter/20170415-223924/candidate450/default",
				"final-scenario1-scenario2/stplus-0-IExporter/stplus-0-IExporter/20170415-223924/candidate459/default",
				"final-scenario1-scenario2/stplus-0-IExporter/stplus-0-IExporter/20170415-223924/candidate494/default",
				"final-scenario1-scenario2/stplus-0-IExporter/stplus-0-IExporter/20170415-223924/candidate500/default",
				"final-scenario1-scenario2/stplus-1-ITripDB/stplus-1-ITripDB/20170415-224136/candidate161/default",
				"final-scenario1-scenario2/stplus-1-ITripDB/stplus-1-ITripDB/20170415-224136/candidate167/default",
				"final-scenario1-scenario2/stplus-1-ITripDB/stplus-1-ITripDB/20170415-224136/candidate228/default",
				"final-scenario1-scenario2/stplus-1-ITripDB/stplus-1-ITripDB/20170415-224136/candidate233/default",
				"final-scenario1-scenario2/stplus-1-ITripDB/stplus-1-ITripDB/20170415-224136/candidate353/default",
				"final-scenario1-scenario2/stplus-1-ITripDB/stplus-1-ITripDB/20170415-224136/candidate354/default",
				"final-scenario1-scenario2/stplus-1-ITripDB/stplus-1-ITripDB/20170415-224136/candidate357/default",
				"final-scenario1-scenario2/stplus-1-ITripDB/stplus-1-ITripDB/20170415-224136/candidate358/default",
				"final-scenario1-scenario2/stplus-1-ITripDB/stplus-1-ITripDB/20170415-224136/candidate432/default",
				"final-scenario1-scenario2/stplus-1-ITripDB/stplus-1-ITripDB/20170415-224136/candidate476/default",
				"final-scenario1-scenario2/stplus-2-IExternalPayment/stplus-2-IExternalPayment/20170415-224255/candidate195/default",
				"final-scenario1-scenario2/stplus-2-IExternalPayment/stplus-2-IExternalPayment/20170415-224255/candidate247/default",
				"final-scenario1-scenario2/stplus-2-IExternalPayment/stplus-2-IExternalPayment/20170415-224255/candidate263/default",
				"final-scenario1-scenario2/stplus-2-IExternalPayment/stplus-2-IExternalPayment/20170415-224255/candidate333/default",
				"final-scenario1-scenario2/stplus-2-IExternalPayment/stplus-2-IExternalPayment/20170415-224255/candidate350/default",
				"final-scenario1-scenario2/stplus-2-IExternalPayment/stplus-2-IExternalPayment/20170415-224255/candidate396/default",
				"final-scenario1-scenario2/stplus-2-IExternalPayment/stplus-2-IExternalPayment/20170415-224255/candidate405/default",
				"final-scenario1-scenario2/stplus-2-IExternalPayment/stplus-2-IExternalPayment/20170415-224255/candidate425/default",
				"final-scenario1-scenario2/stplus-2-IExternalPayment/stplus-2-IExternalPayment/20170415-224255/candidate426/default",
				"final-scenario1-scenario2/stplus-2-IExternalPayment/stplus-2-IExternalPayment/20170415-224255/candidate464/default",
				"final-scenario1-scenario2/stplus-3-IEmployeePayment/stplus-3-IEmployeePayment/20170415-224400/candidate109/default",
				"final-scenario1-scenario2/stplus-3-IEmployeePayment/stplus-3-IEmployeePayment/20170415-224400/candidate133/default",
				"final-scenario1-scenario2/stplus-3-IEmployeePayment/stplus-3-IEmployeePayment/20170415-224400/candidate138/default",
				"final-scenario1-scenario2/stplus-3-IEmployeePayment/stplus-3-IEmployeePayment/20170415-224400/candidate311/default",
				"final-scenario1-scenario2/stplus-3-IEmployeePayment/stplus-3-IEmployeePayment/20170415-224400/candidate330/default",
				"final-scenario1-scenario2/stplus-3-IEmployeePayment/stplus-3-IEmployeePayment/20170415-224400/candidate41/default",
				"final-scenario1-scenario2/stplus-3-IEmployeePayment/stplus-3-IEmployeePayment/20170415-224400/candidate416/default",
				"final-scenario1-scenario2/stplus-3-IEmployeePayment/stplus-3-IEmployeePayment/20170415-224400/candidate417/default",
				"final-scenario1-scenario2/stplus-3-IEmployeePayment/stplus-3-IEmployeePayment/20170415-224400/candidate447/default",
				"final-scenario1-scenario2/stplus-3-IEmployeePayment/stplus-3-IEmployeePayment/20170415-224400/candidate497/default",
				"final-scenario1-scenario2/stplus-4-IBooking/stplus-4-IBooking/20170415-224506/candidate154/default",
				"final-scenario1-scenario2/stplus-4-IBooking/stplus-4-IBooking/20170415-224506/candidate229/default",
				"final-scenario1-scenario2/stplus-4-IBooking/stplus-4-IBooking/20170415-224506/candidate237/default",
				"final-scenario1-scenario2/stplus-4-IBooking/stplus-4-IBooking/20170415-224506/candidate269/default",
				"final-scenario1-scenario2/stplus-4-IBooking/stplus-4-IBooking/20170415-224506/candidate277/default",
				"final-scenario1-scenario2/stplus-4-IBooking/stplus-4-IBooking/20170415-224506/candidate282/default",
				"final-scenario1-scenario2/stplus-4-IBooking/stplus-4-IBooking/20170415-224506/candidate316/default",
				"final-scenario1-scenario2/stplus-4-IBooking/stplus-4-IBooking/20170415-224506/candidate364/default",
				"final-scenario1-scenario2/stplus-4-IBooking/stplus-4-IBooking/20170415-224506/candidate404/default",
				"final-scenario1-scenario2/stplus-4-IBooking/stplus-4-IBooking/20170415-224506/candidate450/default",
				"final-scenario1-scenario2/stplus-5-IBusiness Trip/stplus-5-IBusiness Trip/20170415-224613/candidate110/default",
				"final-scenario1-scenario2/stplus-5-IBusiness Trip/stplus-5-IBusiness Trip/20170415-224613/candidate142/default",
				"final-scenario1-scenario2/stplus-5-IBusiness Trip/stplus-5-IBusiness Trip/20170415-224613/candidate216/default",
				"final-scenario1-scenario2/stplus-5-IBusiness Trip/stplus-5-IBusiness Trip/20170415-224613/candidate232/default",
				"final-scenario1-scenario2/stplus-5-IBusiness Trip/stplus-5-IBusiness Trip/20170415-224613/candidate295/default",
				"final-scenario1-scenario2/stplus-5-IBusiness Trip/stplus-5-IBusiness Trip/20170415-224613/candidate296/default",
				"final-scenario1-scenario2/stplus-5-IBusiness Trip/stplus-5-IBusiness Trip/20170415-224613/candidate442/default",
				"final-scenario1-scenario2/stplus-5-IBusiness Trip/stplus-5-IBusiness Trip/20170415-224613/candidate472/default",
				"final-scenario1-scenario2/stplus-5-IBusiness Trip/stplus-5-IBusiness Trip/20170415-224613/candidate474/default",
				"final-scenario1-scenario2/stplus-5-IBusiness Trip/stplus-5-IBusiness Trip/20170415-224613/candidate478/default",
				"final-scenario1-scenario2/stplus-split-0-IExporter/stplus-split-0-IExporter/20170415-224724/candidate118/default",
				"final-scenario1-scenario2/stplus-split-0-IExporter/stplus-split-0-IExporter/20170415-224724/candidate275/default",
				"final-scenario1-scenario2/stplus-split-0-IExporter/stplus-split-0-IExporter/20170415-224724/candidate351/default",
				"final-scenario1-scenario2/stplus-split-0-IExporter/stplus-split-0-IExporter/20170415-224724/candidate355/default",
				"final-scenario1-scenario2/stplus-split-0-IExporter/stplus-split-0-IExporter/20170415-224724/candidate382/default",
				"final-scenario1-scenario2/stplus-split-0-IExporter/stplus-split-0-IExporter/20170415-224724/candidate389/default",
				"final-scenario1-scenario2/stplus-split-0-IExporter/stplus-split-0-IExporter/20170415-224724/candidate390/default",
				"final-scenario1-scenario2/stplus-split-0-IExporter/stplus-split-0-IExporter/20170415-224724/candidate406/default",
				"final-scenario1-scenario2/stplus-split-0-IExporter/stplus-split-0-IExporter/20170415-224724/candidate425/default",
				"final-scenario1-scenario2/stplus-split-0-IExporter/stplus-split-0-IExporter/20170415-224724/candidate428/default",
				"final-scenario1-scenario2/stplus-split-1-ITripDB/stplus-split-1-ITripDB/20170415-224823/candidate220/default",
				"final-scenario1-scenario2/stplus-split-1-ITripDB/stplus-split-1-ITripDB/20170415-224823/candidate290/default",
				"final-scenario1-scenario2/stplus-split-1-ITripDB/stplus-split-1-ITripDB/20170415-224823/candidate327/default",
				"final-scenario1-scenario2/stplus-split-1-ITripDB/stplus-split-1-ITripDB/20170415-224823/candidate377/default",
				"final-scenario1-scenario2/stplus-split-1-ITripDB/stplus-split-1-ITripDB/20170415-224823/candidate406/default",
				"final-scenario1-scenario2/stplus-split-1-ITripDB/stplus-split-1-ITripDB/20170415-224823/candidate423/default",
				"final-scenario1-scenario2/stplus-split-1-ITripDB/stplus-split-1-ITripDB/20170415-224823/candidate439/default",
				"final-scenario1-scenario2/stplus-split-1-ITripDB/stplus-split-1-ITripDB/20170415-224823/candidate446/default",
				"final-scenario1-scenario2/stplus-split-1-ITripDB/stplus-split-1-ITripDB/20170415-224823/candidate447/default",
				"final-scenario1-scenario2/stplus-split-1-ITripDB/stplus-split-1-ITripDB/20170415-224823/candidate454/default",
				"final-scenario1-scenario2/stplus-split-2-IExternalPayment/stplus-split-2-IExternalPayment/20170415-224952/candidate197/default",
				"final-scenario1-scenario2/stplus-split-2-IExternalPayment/stplus-split-2-IExternalPayment/20170415-224952/candidate232/default",
				"final-scenario1-scenario2/stplus-split-2-IExternalPayment/stplus-split-2-IExternalPayment/20170415-224952/candidate233/default",
				"final-scenario1-scenario2/stplus-split-2-IExternalPayment/stplus-split-2-IExternalPayment/20170415-224952/candidate235/default",
				"final-scenario1-scenario2/stplus-split-2-IExternalPayment/stplus-split-2-IExternalPayment/20170415-224952/candidate276/default",
				"final-scenario1-scenario2/stplus-split-2-IExternalPayment/stplus-split-2-IExternalPayment/20170415-224952/candidate277/default",
				"final-scenario1-scenario2/stplus-split-2-IExternalPayment/stplus-split-2-IExternalPayment/20170415-224952/candidate33/default",
				"final-scenario1-scenario2/stplus-split-2-IExternalPayment/stplus-split-2-IExternalPayment/20170415-224952/candidate355/default",
				"final-scenario1-scenario2/stplus-split-2-IExternalPayment/stplus-split-2-IExternalPayment/20170415-224952/candidate362/default",
				"final-scenario1-scenario2/stplus-split-2-IExternalPayment/stplus-split-2-IExternalPayment/20170415-224952/candidate368/default",
				"final-scenario1-scenario2/stplus-split-3-IEmployeePayment/stplus-split-3-IEmployeePayment/20170415-225044/candidate12/default",
				"final-scenario1-scenario2/stplus-split-3-IEmployeePayment/stplus-split-3-IEmployeePayment/20170415-225044/candidate13/default",
				"final-scenario1-scenario2/stplus-split-3-IEmployeePayment/stplus-split-3-IEmployeePayment/20170415-225044/candidate176/default",
				"final-scenario1-scenario2/stplus-split-3-IEmployeePayment/stplus-split-3-IEmployeePayment/20170415-225044/candidate224/default",
				"final-scenario1-scenario2/stplus-split-3-IEmployeePayment/stplus-split-3-IEmployeePayment/20170415-225044/candidate281/default",
				"final-scenario1-scenario2/stplus-split-3-IEmployeePayment/stplus-split-3-IEmployeePayment/20170415-225044/candidate318/default",
				"final-scenario1-scenario2/stplus-split-3-IEmployeePayment/stplus-split-3-IEmployeePayment/20170415-225044/candidate356/default",
				"final-scenario1-scenario2/stplus-split-3-IEmployeePayment/stplus-split-3-IEmployeePayment/20170415-225044/candidate402/default",
				"final-scenario1-scenario2/stplus-split-3-IEmployeePayment/stplus-split-3-IEmployeePayment/20170415-225044/candidate438/default",
				"final-scenario1-scenario2/stplus-split-3-IEmployeePayment/stplus-split-3-IEmployeePayment/20170415-225044/candidate483/default",
				"final-scenario1-scenario2/stplus-split-4-IBooking/stplus-split-4-IBooking/20170415-225147/candidate100/default",
				"final-scenario1-scenario2/stplus-split-4-IBooking/stplus-split-4-IBooking/20170415-225147/candidate145/default",
				"final-scenario1-scenario2/stplus-split-4-IBooking/stplus-split-4-IBooking/20170415-225147/candidate232/default",
				"final-scenario1-scenario2/stplus-split-4-IBooking/stplus-split-4-IBooking/20170415-225147/candidate300/default",
				"final-scenario1-scenario2/stplus-split-4-IBooking/stplus-split-4-IBooking/20170415-225147/candidate326/default",
				"final-scenario1-scenario2/stplus-split-4-IBooking/stplus-split-4-IBooking/20170415-225147/candidate353/default",
				"final-scenario1-scenario2/stplus-split-4-IBooking/stplus-split-4-IBooking/20170415-225147/candidate400/default",
				"final-scenario1-scenario2/stplus-split-4-IBooking/stplus-split-4-IBooking/20170415-225147/candidate474/default",
				"final-scenario1-scenario2/stplus-split-4-IBooking/stplus-split-4-IBooking/20170415-225147/candidate488/default",
				"final-scenario1-scenario2/stplus-split-4-IBooking/stplus-split-4-IBooking/20170415-225147/candidate86/default",
				"final-scenario1-scenario2/stplus-split-5-IBusiness Trip/stplus-split-5-IBusiness Trip/20170415-225301/candidate319/default",
				"final-scenario1-scenario2/stplus-split-5-IBusiness Trip/stplus-split-5-IBusiness Trip/20170415-225301/candidate321/default",
				"final-scenario1-scenario2/stplus-split-5-IBusiness Trip/stplus-split-5-IBusiness Trip/20170415-225301/candidate323/default",
				"final-scenario1-scenario2/stplus-split-5-IBusiness Trip/stplus-split-5-IBusiness Trip/20170415-225301/candidate356/default",
				"final-scenario1-scenario2/stplus-split-5-IBusiness Trip/stplus-split-5-IBusiness Trip/20170415-225301/candidate392/default",
				"final-scenario1-scenario2/stplus-split-5-IBusiness Trip/stplus-split-5-IBusiness Trip/20170415-225301/candidate397/default",
				"final-scenario1-scenario2/stplus-split-5-IBusiness Trip/stplus-split-5-IBusiness Trip/20170415-225301/candidate426/default",
				"final-scenario1-scenario2/stplus-split-5-IBusiness Trip/stplus-split-5-IBusiness Trip/20170415-225301/candidate427/default",
				"final-scenario1-scenario2/stplus-split-5-IBusiness Trip/stplus-split-5-IBusiness Trip/20170415-225301/candidate433/default",
				"final-scenario1-scenario2/stplus-split-5-IBusiness Trip/stplus-split-5-IBusiness Trip/20170415-225301/candidate62/default",
				// performance scenario 2
				"final-scenario2-scenario1/stplus-0-Payment System/stplus-0-Payment System/20170415-230000/candidate119/default",
				"final-scenario2-scenario1/stplus-0-Payment System/stplus-0-Payment System/20170415-230000/candidate179/default",
				"final-scenario2-scenario1/stplus-0-Payment System/stplus-0-Payment System/20170415-230000/candidate191/default",
				"final-scenario2-scenario1/stplus-0-Payment System/stplus-0-Payment System/20170415-230000/candidate222/default",
				"final-scenario2-scenario1/stplus-0-Payment System/stplus-0-Payment System/20170415-230000/candidate239/default",
				"final-scenario2-scenario1/stplus-0-Payment System/stplus-0-Payment System/20170415-230000/candidate402/default",
				"final-scenario2-scenario1/stplus-0-Payment System/stplus-0-Payment System/20170415-230000/candidate43/default",
				"final-scenario2-scenario1/stplus-0-Payment System/stplus-0-Payment System/20170415-230000/candidate432/default",
				"final-scenario2-scenario1/stplus-0-Payment System/stplus-0-Payment System/20170415-230000/candidate470/default",
				"final-scenario2-scenario1/stplus-0-Payment System/stplus-0-Payment System/20170415-230000/candidate69/default",
				"final-scenario2-scenario1/stplus-0-IExporter/stplus-0-IExporter/20170415-225907/candidate146/default",
				"final-scenario2-scenario1/stplus-0-IExporter/stplus-0-IExporter/20170415-225907/candidate148/default",
				"final-scenario2-scenario1/stplus-0-IExporter/stplus-0-IExporter/20170415-225907/candidate162/default",
				"final-scenario2-scenario1/stplus-0-IExporter/stplus-0-IExporter/20170415-225907/candidate243/default",
				"final-scenario2-scenario1/stplus-0-IExporter/stplus-0-IExporter/20170415-225907/candidate270/default",
				"final-scenario2-scenario1/stplus-0-IExporter/stplus-0-IExporter/20170415-225907/candidate281/default",
				"final-scenario2-scenario1/stplus-0-IExporter/stplus-0-IExporter/20170415-225907/candidate318/default",
				"final-scenario2-scenario1/stplus-0-IExporter/stplus-0-IExporter/20170415-225907/candidate324/default",
				"final-scenario2-scenario1/stplus-0-IExporter/stplus-0-IExporter/20170415-225907/candidate52/default",
				"final-scenario2-scenario1/stplus-0-IExporter/stplus-0-IExporter/20170415-225907/candidate90/default",
				"final-scenario2-scenario1/stplus-1-ITripDB/stplus-1-ITripDB/20170415-230058/candidate143/default",
				"final-scenario2-scenario1/stplus-1-ITripDB/stplus-1-ITripDB/20170415-230058/candidate189/default",
				"final-scenario2-scenario1/stplus-1-ITripDB/stplus-1-ITripDB/20170415-230058/candidate192/default",
				"final-scenario2-scenario1/stplus-1-ITripDB/stplus-1-ITripDB/20170415-230058/candidate231/default",
				"final-scenario2-scenario1/stplus-1-ITripDB/stplus-1-ITripDB/20170415-230058/candidate309/default",
				"final-scenario2-scenario1/stplus-1-ITripDB/stplus-1-ITripDB/20170415-230058/candidate314/default",
				"final-scenario2-scenario1/stplus-1-ITripDB/stplus-1-ITripDB/20170415-230058/candidate33/default",
				"final-scenario2-scenario1/stplus-1-ITripDB/stplus-1-ITripDB/20170415-230058/candidate347/default",
				"final-scenario2-scenario1/stplus-1-ITripDB/stplus-1-ITripDB/20170415-230058/candidate354/default",
				"final-scenario2-scenario1/stplus-1-ITripDB/stplus-1-ITripDB/20170415-230058/candidate86/default",
				"final-scenario2-scenario1/stplus-2-IExternalPayment/stplus-2-IExternalPayment/20170415-230200/candidate145/default",
				"final-scenario2-scenario1/stplus-2-IExternalPayment/stplus-2-IExternalPayment/20170415-230200/candidate147/default",
				"final-scenario2-scenario1/stplus-2-IExternalPayment/stplus-2-IExternalPayment/20170415-230200/candidate270/default",
				"final-scenario2-scenario1/stplus-2-IExternalPayment/stplus-2-IExternalPayment/20170415-230200/candidate275/default",
				"final-scenario2-scenario1/stplus-2-IExternalPayment/stplus-2-IExternalPayment/20170415-230200/candidate324/default",
				"final-scenario2-scenario1/stplus-2-IExternalPayment/stplus-2-IExternalPayment/20170415-230200/candidate354/default",
				"final-scenario2-scenario1/stplus-2-IExternalPayment/stplus-2-IExternalPayment/20170415-230200/candidate369/default",
				"final-scenario2-scenario1/stplus-2-IExternalPayment/stplus-2-IExternalPayment/20170415-230200/candidate445/default",
				"final-scenario2-scenario1/stplus-2-IExternalPayment/stplus-2-IExternalPayment/20170415-230200/candidate449/default",
				"final-scenario2-scenario1/stplus-2-IExternalPayment/stplus-2-IExternalPayment/20170415-230200/candidate453/default",
				"final-scenario2-scenario1/stplus-3-IEmployeePayment/stplus-3-IEmployeePayment/20170415-230310/candidate130/default",
				"final-scenario2-scenario1/stplus-3-IEmployeePayment/stplus-3-IEmployeePayment/20170415-230310/candidate237/default",
				"final-scenario2-scenario1/stplus-3-IEmployeePayment/stplus-3-IEmployeePayment/20170415-230310/candidate257/default",
				"final-scenario2-scenario1/stplus-3-IEmployeePayment/stplus-3-IEmployeePayment/20170415-230310/candidate310/default",
				"final-scenario2-scenario1/stplus-3-IEmployeePayment/stplus-3-IEmployeePayment/20170415-230310/candidate358/default",
				"final-scenario2-scenario1/stplus-3-IEmployeePayment/stplus-3-IEmployeePayment/20170415-230310/candidate361/default",
				"final-scenario2-scenario1/stplus-3-IEmployeePayment/stplus-3-IEmployeePayment/20170415-230310/candidate437/default",
				"final-scenario2-scenario1/stplus-3-IEmployeePayment/stplus-3-IEmployeePayment/20170415-230310/candidate441/default",
				"final-scenario2-scenario1/stplus-3-IEmployeePayment/stplus-3-IEmployeePayment/20170415-230310/candidate489/default",
				"final-scenario2-scenario1/stplus-3-IEmployeePayment/stplus-3-IEmployeePayment/20170415-230310/candidate496/default",
				"final-scenario2-scenario1/stplus-4-IBooking/stplus-4-IBooking/20170415-230416/candidate117/default",
				"final-scenario2-scenario1/stplus-4-IBooking/stplus-4-IBooking/20170415-230416/candidate143/default",
				"final-scenario2-scenario1/stplus-4-IBooking/stplus-4-IBooking/20170415-230416/candidate237/default",
				"final-scenario2-scenario1/stplus-4-IBooking/stplus-4-IBooking/20170415-230416/candidate286/default",
				"final-scenario2-scenario1/stplus-4-IBooking/stplus-4-IBooking/20170415-230416/candidate299/default",
				"final-scenario2-scenario1/stplus-4-IBooking/stplus-4-IBooking/20170415-230416/candidate330/default",
				"final-scenario2-scenario1/stplus-4-IBooking/stplus-4-IBooking/20170415-230416/candidate375/default",
				"final-scenario2-scenario1/stplus-4-IBooking/stplus-4-IBooking/20170415-230416/candidate376/default",
				"final-scenario2-scenario1/stplus-4-IBooking/stplus-4-IBooking/20170415-230416/candidate384/default",
				"final-scenario2-scenario1/stplus-4-IBooking/stplus-4-IBooking/20170415-230416/candidate401/default",
				"final-scenario2-scenario1/stplus-5-IBusiness Trip/stplus-5-IBusiness Trip/20170415-230513/candidate107/default",
				"final-scenario2-scenario1/stplus-5-IBusiness Trip/stplus-5-IBusiness Trip/20170415-230513/candidate130/default",
				"final-scenario2-scenario1/stplus-5-IBusiness Trip/stplus-5-IBusiness Trip/20170415-230513/candidate156/default",
				"final-scenario2-scenario1/stplus-5-IBusiness Trip/stplus-5-IBusiness Trip/20170415-230513/candidate238/default",
				"final-scenario2-scenario1/stplus-5-IBusiness Trip/stplus-5-IBusiness Trip/20170415-230513/candidate276/default",
				"final-scenario2-scenario1/stplus-5-IBusiness Trip/stplus-5-IBusiness Trip/20170415-230513/candidate282/default",
				"final-scenario2-scenario1/stplus-5-IBusiness Trip/stplus-5-IBusiness Trip/20170415-230513/candidate440/default",
				"final-scenario2-scenario1/stplus-5-IBusiness Trip/stplus-5-IBusiness Trip/20170415-230513/candidate442/default",
				"final-scenario2-scenario1/stplus-5-IBusiness Trip/stplus-5-IBusiness Trip/20170415-230513/candidate90/default",
				"final-scenario2-scenario1/stplus-5-IBusiness Trip/stplus-5-IBusiness Trip/20170415-230513/candidate98/default",
				"final-scenario2-scenario1/stplus-split-0-IExporter/stplus-split-0-IExporter/20170415-230616/candidate143/default",
				"final-scenario2-scenario1/stplus-split-0-IExporter/stplus-split-0-IExporter/20170415-230616/candidate191/default",
				"final-scenario2-scenario1/stplus-split-0-IExporter/stplus-split-0-IExporter/20170415-230616/candidate268/default",
				"final-scenario2-scenario1/stplus-split-0-IExporter/stplus-split-0-IExporter/20170415-230616/candidate307/default",
				"final-scenario2-scenario1/stplus-split-0-IExporter/stplus-split-0-IExporter/20170415-230616/candidate314/default",
				"final-scenario2-scenario1/stplus-split-0-IExporter/stplus-split-0-IExporter/20170415-230616/candidate352/default",
				"final-scenario2-scenario1/stplus-split-0-IExporter/stplus-split-0-IExporter/20170415-230616/candidate354/default",
				"final-scenario2-scenario1/stplus-split-0-IExporter/stplus-split-0-IExporter/20170415-230616/candidate355/default",
				"final-scenario2-scenario1/stplus-split-0-IExporter/stplus-split-0-IExporter/20170415-230616/candidate55/default",
				"final-scenario2-scenario1/stplus-split-0-IExporter/stplus-split-0-IExporter/20170415-230616/candidate7/default",
				"final-scenario2-scenario1/stplus-split-1-ITripDB/stplus-split-1-ITripDB/20170415-230700/candidate137/default",
				"final-scenario2-scenario1/stplus-split-1-ITripDB/stplus-split-1-ITripDB/20170415-230700/candidate142/default",
				"final-scenario2-scenario1/stplus-split-1-ITripDB/stplus-split-1-ITripDB/20170415-230700/candidate171/default",
				"final-scenario2-scenario1/stplus-split-1-ITripDB/stplus-split-1-ITripDB/20170415-230700/candidate189/default",
				"final-scenario2-scenario1/stplus-split-1-ITripDB/stplus-split-1-ITripDB/20170415-230700/candidate209/default",
				"final-scenario2-scenario1/stplus-split-1-ITripDB/stplus-split-1-ITripDB/20170415-230700/candidate238/default",
				"final-scenario2-scenario1/stplus-split-1-ITripDB/stplus-split-1-ITripDB/20170415-230700/candidate275/default",
				"final-scenario2-scenario1/stplus-split-1-ITripDB/stplus-split-1-ITripDB/20170415-230700/candidate281/default",
				"final-scenario2-scenario1/stplus-split-1-ITripDB/stplus-split-1-ITripDB/20170415-230700/candidate285/default",
				"final-scenario2-scenario1/stplus-split-1-ITripDB/stplus-split-1-ITripDB/20170415-230700/candidate414/default",
				"final-scenario2-scenario1/stplus-split-2-IExternalPayment/stplus-split-2-IExternalPayment/20170415-230802/candidate192/default",
				"final-scenario2-scenario1/stplus-split-2-IExternalPayment/stplus-split-2-IExternalPayment/20170415-230802/candidate203/default",
				"final-scenario2-scenario1/stplus-split-2-IExternalPayment/stplus-split-2-IExternalPayment/20170415-230802/candidate275/default",
				"final-scenario2-scenario1/stplus-split-2-IExternalPayment/stplus-split-2-IExternalPayment/20170415-230802/candidate279/default",
				"final-scenario2-scenario1/stplus-split-2-IExternalPayment/stplus-split-2-IExternalPayment/20170415-230802/candidate360/default",
				"final-scenario2-scenario1/stplus-split-2-IExternalPayment/stplus-split-2-IExternalPayment/20170415-230802/candidate364/default",
				"final-scenario2-scenario1/stplus-split-2-IExternalPayment/stplus-split-2-IExternalPayment/20170415-230802/candidate403/default",
				"final-scenario2-scenario1/stplus-split-2-IExternalPayment/stplus-split-2-IExternalPayment/20170415-230802/candidate439/default",
				"final-scenario2-scenario1/stplus-split-2-IExternalPayment/stplus-split-2-IExternalPayment/20170415-230802/candidate450/default",
				"final-scenario2-scenario1/stplus-split-2-IExternalPayment/stplus-split-2-IExternalPayment/20170415-230802/candidate492/default",
				"final-scenario2-scenario1/stplus-split-3-IEmployeePayment/stplus-split-3-IEmployeePayment/20170415-230905/candidate250/default",
				"final-scenario2-scenario1/stplus-split-3-IEmployeePayment/stplus-split-3-IEmployeePayment/20170415-230905/candidate321/default",
				"final-scenario2-scenario1/stplus-split-3-IEmployeePayment/stplus-split-3-IEmployeePayment/20170415-230905/candidate33/default",
				"final-scenario2-scenario1/stplus-split-3-IEmployeePayment/stplus-split-3-IEmployeePayment/20170415-230905/candidate359/default",
				"final-scenario2-scenario1/stplus-split-3-IEmployeePayment/stplus-split-3-IEmployeePayment/20170415-230905/candidate360/default",
				"final-scenario2-scenario1/stplus-split-3-IEmployeePayment/stplus-split-3-IEmployeePayment/20170415-230905/candidate369/default",
				"final-scenario2-scenario1/stplus-split-3-IEmployeePayment/stplus-split-3-IEmployeePayment/20170415-230905/candidate370/default",
				"final-scenario2-scenario1/stplus-split-3-IEmployeePayment/stplus-split-3-IEmployeePayment/20170415-230905/candidate456/default",
				"final-scenario2-scenario1/stplus-split-3-IEmployeePayment/stplus-split-3-IEmployeePayment/20170415-230905/candidate499/default",
				"final-scenario2-scenario1/stplus-split-3-IEmployeePayment/stplus-split-3-IEmployeePayment/20170415-230905/candidate501/default",
				"final-scenario2-scenario1/stplus-split-4-IBooking/stplus-split-4-IBooking/20170415-231013/candidate226/default",
				"final-scenario2-scenario1/stplus-split-4-IBooking/stplus-split-4-IBooking/20170415-231013/candidate325/default",
				"final-scenario2-scenario1/stplus-split-4-IBooking/stplus-split-4-IBooking/20170415-231013/candidate401/default",
				"final-scenario2-scenario1/stplus-split-4-IBooking/stplus-split-4-IBooking/20170415-231013/candidate415/default",
				"final-scenario2-scenario1/stplus-split-4-IBooking/stplus-split-4-IBooking/20170415-231013/candidate452/default",
				"final-scenario2-scenario1/stplus-split-4-IBooking/stplus-split-4-IBooking/20170415-231013/candidate456/default",
				"final-scenario2-scenario1/stplus-split-4-IBooking/stplus-split-4-IBooking/20170415-231013/candidate462/default",
				"final-scenario2-scenario1/stplus-split-4-IBooking/stplus-split-4-IBooking/20170415-231013/candidate505/default",
				"final-scenario2-scenario1/stplus-split-4-IBooking/stplus-split-4-IBooking/20170415-231013/candidate506/default",
				"final-scenario2-scenario1/stplus-split-4-IBooking/stplus-split-4-IBooking/20170415-231013/candidate508/default",
				"final-scenario2-scenario1/stplus-split-5-IBusiness Trip/stplus-split-5-IBusiness Trip/20170415-231122/candidate119/default",
				"final-scenario2-scenario1/stplus-split-5-IBusiness Trip/stplus-split-5-IBusiness Trip/20170415-231122/candidate242/default",
				"final-scenario2-scenario1/stplus-split-5-IBusiness Trip/stplus-split-5-IBusiness Trip/20170415-231122/candidate278/default",
				"final-scenario2-scenario1/stplus-split-5-IBusiness Trip/stplus-split-5-IBusiness Trip/20170415-231122/candidate344/default",
				"final-scenario2-scenario1/stplus-split-5-IBusiness Trip/stplus-split-5-IBusiness Trip/20170415-231122/candidate373/default",
				"final-scenario2-scenario1/stplus-split-5-IBusiness Trip/stplus-split-5-IBusiness Trip/20170415-231122/candidate407/default",
				"final-scenario2-scenario1/stplus-split-5-IBusiness Trip/stplus-split-5-IBusiness Trip/20170415-231122/candidate408/default",
				"final-scenario2-scenario1/stplus-split-5-IBusiness Trip/stplus-split-5-IBusiness Trip/20170415-231122/candidate439/default",
				"final-scenario2-scenario1/stplus-split-5-IBusiness Trip/stplus-split-5-IBusiness Trip/20170415-231122/candidate447/default",
				"final-scenario2-scenario1/stplus-split-5-IBusiness Trip/stplus-split-5-IBusiness Trip/20170415-231122/candidate482/default"
		};

		modelNames = new String[] {
				// performance scenario 1
				"stplus-mod-split(PaymentSystem)-ps1-136",
				"stplus-mod-split(PaymentSystem)-ps1-162",
				"stplus-mod-split(PaymentSystem)-ps1-208",
				"stplus-mod-split(PaymentSystem)-ps1-232",
				"stplus-mod-split(PaymentSystem)-ps1-239",
				"stplus-mod-split(PaymentSystem)-ps1-316",
				"stplus-mod-split(PaymentSystem)-ps1-321",
				"stplus-mod-split(PaymentSystem)-ps1-355",
				"stplus-mod-split(PaymentSystem)-ps1-367",
				"stplus-mod-split(PaymentSystem)-ps1-419",				
				"stplus-mod-wrapper(IExporter)-ps1-66",
				"stplus-mod-wrapper(IExporter)-ps1-141",
				"stplus-mod-wrapper(IExporter)-ps1-227",
				"stplus-mod-wrapper(IExporter)-ps1-238",
				"stplus-mod-wrapper(IExporter)-ps1-275",
				"stplus-mod-wrapper(IExporter)-ps1-329",
				"stplus-mod-wrapper(IExporter)-ps1-450",
				"stplus-mod-wrapper(IExporter)-ps1-459",
				"stplus-mod-wrapper(IExporter)-ps1-494",
				"stplus-mod-wrapper(IExporter)-ps1-500",				
				"stplus-mod-wrapper(ITripDB)-ps1-161",
				"stplus-mod-wrapper(ITripDB)-ps1-167",
				"stplus-mod-wrapper(ITripDB)-ps1-228",
				"stplus-mod-wrapper(ITripDB)-ps1-233",
				"stplus-mod-wrapper(ITripDB)-ps1-353",
				"stplus-mod-wrapper(ITripDB)-ps1-354",
				"stplus-mod-wrapper(ITripDB)-ps1-357",
				"stplus-mod-wrapper(ITripDB)-ps1-358",
				"stplus-mod-wrapper(ITripDB)-ps1-432",
				"stplus-mod-wrapper(ITripDB)-ps1-476",				
				"stplus-mod-wrapper(IExternalPayment)-ps1-195",
				"stplus-mod-wrapper(IExternalPayment)-ps1-247",
				"stplus-mod-wrapper(IExternalPayment)-ps1-263",
				"stplus-mod-wrapper(IExternalPayment)-ps1-333",
				"stplus-mod-wrapper(IExternalPayment)-ps1-350",
				"stplus-mod-wrapper(IExternalPayment)-ps1-396",
				"stplus-mod-wrapper(IExternalPayment)-ps1-405",
				"stplus-mod-wrapper(IExternalPayment)-ps1-425",
				"stplus-mod-wrapper(IExternalPayment)-ps1-426",
				"stplus-mod-wrapper(IExternalPayment)-ps1-464",				
				"stplus-mod-wrapper(IEmployeePayment)-ps1-109",
				"stplus-mod-wrapper(IEmployeePayment)-ps1-133",
				"stplus-mod-wrapper(IEmployeePayment)-ps1-138",
				"stplus-mod-wrapper(IEmployeePayment)-ps1-311",
				"stplus-mod-wrapper(IEmployeePayment)-ps1-330",
				"stplus-mod-wrapper(IEmployeePayment)-ps1-41",
				"stplus-mod-wrapper(IEmployeePayment)-ps1-416",
				"stplus-mod-wrapper(IEmployeePayment)-ps1-417",
				"stplus-mod-wrapper(IEmployeePayment)-ps1-447",
				"stplus-mod-wrapper(IEmployeePayment)-ps1-497",				
				"stplus-mod-wrapper(IBooking)-ps1-154",
				"stplus-mod-wrapper(IBooking)-ps1-229",
				"stplus-mod-wrapper(IBooking)-ps1-237",
				"stplus-mod-wrapper(IBooking)-ps1-269",
				"stplus-mod-wrapper(IBooking)-ps1-277",
				"stplus-mod-wrapper(IBooking)-ps1-282",
				"stplus-mod-wrapper(IBooking)-ps1-316",
				"stplus-mod-wrapper(IBooking)-ps1-364",
				"stplus-mod-wrapper(IBooking)-ps1-404",
				"stplus-mod-wrapper(IBooking)-ps1-450",				
				"stplus-mod-wrapper(IBusinessTrip)-ps1-110",
				"stplus-mod-wrapper(IBusinessTrip)-ps1-142",
				"stplus-mod-wrapper(IBusinessTrip)-ps1-216",
				"stplus-mod-wrapper(IBusinessTrip)-ps1-232",
				"stplus-mod-wrapper(IBusinessTrip)-ps1-295",
				"stplus-mod-wrapper(IBusinessTrip)-ps1-296",
				"stplus-mod-wrapper(IBusinessTrip)-ps1-442",
				"stplus-mod-wrapper(IBusinessTrip)-ps1-472",
				"stplus-mod-wrapper(IBusinessTrip)-ps1-474",
				"stplus-mod-wrapper(IBusinessTrip)-ps1-478",				
				"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps1-118",
				"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps1-275",
				"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps1-351",
				"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps1-355",
				"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps1-382",
				"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps1-389",
				"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps1-390",
				"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps1-406",
				"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps1-425",
				"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps1-428",				
				"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps1-220",
				"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps1-290",
				"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps1-327",
				"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps1-377",
				"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps1-406",
				"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps1-423",
				"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps1-439",
				"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps1-446",
				"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps1-447",
				"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps1-454",				
				"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps1-197",
				"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps1-232",
				"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps1-233",
				"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps1-235",
				"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps1-276",
				"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps1-277",
				"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps1-33",
				"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps1-355",
				"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps1-362",
				"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps1-368",				
				"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps1-12",
				"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps1-13",
				"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps1-176",
				"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps1-224",
				"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps1-281",
				"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps1-318",
				"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps1-356",
				"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps1-402",
				"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps1-438",
				"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps1-483",				
				"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps1-100",
				"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps1-145",
				"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps1-232",
				"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps1-300",
				"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps1-326",
				"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps1-353",
				"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps1-400",
				"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps1-474",
				"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps1-488",
				"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps1-86",				
				"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps1-319",
				"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps1-321",
				"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps1-323",
				"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps1-356",
				"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps1-392",
				"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps1-397",
				"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps1-426",
				"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps1-427",
				"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps1-433",
				"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps1-62",
				// performance scenario 2
				"stplus-mod-split(PaymentSystem)-ps2-119",
				"stplus-mod-split(PaymentSystem)-ps2-179",
				"stplus-mod-split(PaymentSystem)-ps2-191",
				"stplus-mod-split(PaymentSystem)-ps2-222",
				"stplus-mod-split(PaymentSystem)-ps2-239",
				"stplus-mod-split(PaymentSystem)-ps2-402",
				"stplus-mod-split(PaymentSystem)-ps2-43",
				"stplus-mod-split(PaymentSystem)-ps2-432",
				"stplus-mod-split(PaymentSystem)-ps2-470",
				"stplus-mod-split(PaymentSystem)-ps2-69",
				"stplus-mod-wrapper(IExporter)-ps2-146",
				"stplus-mod-wrapper(IExporter)-ps2-148",
				"stplus-mod-wrapper(IExporter)-ps2-162",
				"stplus-mod-wrapper(IExporter)-ps2-243",
				"stplus-mod-wrapper(IExporter)-ps2-270",
				"stplus-mod-wrapper(IExporter)-ps2-281",
				"stplus-mod-wrapper(IExporter)-ps2-318",
				"stplus-mod-wrapper(IExporter)-ps2-324",
				"stplus-mod-wrapper(IExporter)-ps2-52",
				"stplus-mod-wrapper(IExporter)-ps2-90",
				"stplus-mod-wrapper(ITripDB)-ps2-143",
				"stplus-mod-wrapper(ITripDB)-ps2-189",
				"stplus-mod-wrapper(ITripDB)-ps2-192",
				"stplus-mod-wrapper(ITripDB)-ps2-231",
				"stplus-mod-wrapper(ITripDB)-ps2-309",
				"stplus-mod-wrapper(ITripDB)-ps2-314",
				"stplus-mod-wrapper(ITripDB)-ps2-33",
				"stplus-mod-wrapper(ITripDB)-ps2-347",
				"stplus-mod-wrapper(ITripDB)-ps2-354",
				"stplus-mod-wrapper(ITripDB)-ps2-86",
				"stplus-mod-wrapper(IExternalPayment)-ps2-145",
				"stplus-mod-wrapper(IExternalPayment)-ps2-147",
				"stplus-mod-wrapper(IExternalPayment)-ps2-270",
				"stplus-mod-wrapper(IExternalPayment)-ps2-275",
				"stplus-mod-wrapper(IExternalPayment)-ps2-324",
				"stplus-mod-wrapper(IExternalPayment)-ps2-354",
				"stplus-mod-wrapper(IExternalPayment)-ps2-369",
				"stplus-mod-wrapper(IExternalPayment)-ps2-445",
				"stplus-mod-wrapper(IExternalPayment)-ps2-449",
				"stplus-mod-wrapper(IExternalPayment)-ps2-453",
				"stplus-mod-wrapper(IEmployeePayment)-ps2-130",
				"stplus-mod-wrapper(IEmployeePayment)-ps2-237",
				"stplus-mod-wrapper(IEmployeePayment)-ps2-257",
				"stplus-mod-wrapper(IEmployeePayment)-ps2-310",
				"stplus-mod-wrapper(IEmployeePayment)-ps2-358",
				"stplus-mod-wrapper(IEmployeePayment)-ps2-361",
				"stplus-mod-wrapper(IEmployeePayment)-ps2-437",
				"stplus-mod-wrapper(IEmployeePayment)-ps2-441",
				"stplus-mod-wrapper(IEmployeePayment)-ps2-489",
				"stplus-mod-wrapper(IEmployeePayment)-ps2-496",
				"stplus-mod-wrapper(IBooking)-ps2-117",
				"stplus-mod-wrapper(IBooking)-ps2-143",
				"stplus-mod-wrapper(IBooking)-ps2-237",
				"stplus-mod-wrapper(IBooking)-ps2-286",
				"stplus-mod-wrapper(IBooking)-ps2-299",
				"stplus-mod-wrapper(IBooking)-ps2-330",
				"stplus-mod-wrapper(IBooking)-ps2-375",
				"stplus-mod-wrapper(IBooking)-ps2-376",
				"stplus-mod-wrapper(IBooking)-ps2-384",
				"stplus-mod-wrapper(IBooking)-ps2-401",
				"stplus-mod-wrapper(IBusinessTrip)-ps2-107",
				"stplus-mod-wrapper(IBusinessTrip)-ps2-130",
				"stplus-mod-wrapper(IBusinessTrip)-ps2-156",
				"stplus-mod-wrapper(IBusinessTrip)-ps2-238",
				"stplus-mod-wrapper(IBusinessTrip)-ps2-276",
				"stplus-mod-wrapper(IBusinessTrip)-ps2-282",
				"stplus-mod-wrapper(IBusinessTrip)-ps2-440",
				"stplus-mod-wrapper(IBusinessTrip)-ps2-442",
				"stplus-mod-wrapper(IBusinessTrip)-ps2-90",
				"stplus-mod-wrapper(IBusinessTrip)-ps2-98",
				"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps2-143",
				"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps2-191",
				"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps2-268",
				"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps2-307",
				"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps2-314",
				"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps2-352",
				"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps2-354",
				"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps2-355",
				"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps2-55",
				"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps2-7",
				"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps2-137",
				"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps2-142",
				"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps2-171",
				"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps2-189",
				"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps2-209",
				"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps2-238",
				"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps2-275",
				"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps2-281",
				"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps2-285",
				"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps2-414",
				"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps2-192",
				"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps2-203",
				"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps2-275",
				"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps2-279",
				"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps2-360",
				"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps2-364",
				"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps2-403",
				"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps2-439",
				"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps2-450",
				"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps2-492",
				"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps2-250",
				"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps2-321",
				"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps2-33",
				"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps2-359",
				"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps2-360",
				"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps2-369",
				"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps2-370",
				"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps2-456",
				"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps2-499",
				"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps2-501",
				"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps2-226",
				"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps2-325",
				"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps2-401",
				"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps2-415",
				"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps2-452",
				"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps2-456",
				"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps2-462",
				"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps2-505",
				"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps2-506",
				"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps2-508",
				"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps2-119",
				"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps2-242",
				"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps2-278",
				"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps2-344",
				"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps2-373",
				"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps2-407",
				"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps2-408",
				"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps2-439",
				"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps2-447",
				"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps2-482"
		};
		
		repositoryFile = new String[modelNames.length];
		resourceEnvironmentFile = new String[modelNames.length];
		baseSystemFile = new String[modelNames.length];
		baseAllocationFile = new String[modelNames.length];
		baseUsageFile = new String[modelNames.length];
		for (int i = 0; i < modelNames.length; i++) {
			repositoryFile[i] = dirPath + localPath[i] + ".repository";
			resourceEnvironmentFile[i] = dirPath + localPath[i] + ".resourceenvironment";
			baseSystemFile[i] = dirPath + localPath[i] + ".system";
			baseAllocationFile[i] = dirPath + localPath[i] + ".allocation";
			baseUsageFile[i] = dirPath + localPath[i] + ".usagemodel";
		}
	}
	
	private PCMArchitectureInstance loadSpecificModel(String name, String repositoryFile, String enviromentFile,
			String systemFile, String sllocationFile, String usageFile) {
		Repository repository = SQuATHelper.loadRepositoryModel(repositoryFile);
		ResourceEnvironment resourceEnvironment = SQuATHelper.loadResourceEnvironmentModel(enviromentFile);
		System system = SQuATHelper.loadSystemModel(systemFile);
		Allocation allocation = SQuATHelper.loadAllocationModel(sllocationFile);
		UsageModel usageModel = SQuATHelper.loadUsageModel(usageFile);
		PCMArchitectureInstance instance = new PCMArchitectureInstance(name, repository, system, allocation,
				resourceEnvironment, usageModel);
		return instance;
	}

	public void runAllTests() throws Exception {
		//Modifiability
		this.setModifiabilityPCMModel();
		this.runTests();
		//Performance
		this.setPerformancePCMModel();
		this.runTests();
		//Performance-Modifiability
		this.setPerformanceModifiabilityPCMModel();
		this.runTests();
		//Modifiability-Performance
		this.setModifiabilityPerformancePCMModel();
		this.runTests();
	}
	
	private void runTests() throws Exception {
		this.results.clear(); 
		this.resultsMap.clear();
		String evaluationType; ResponseMeasureType type; Comparable response;
		//Affected components
		evaluationType = KAMPPCMBotDeprecated.TYPE_ELEMENTS;
		type = ResponseMeasureType.NUMERIC;
		response = new Integer(5);
		this.testModifiabilityScenario(this.createModifiabilityScenarioS1(type, response), "M1", evaluationType);
		response = new Integer(7);
		this.testModifiabilityScenario(this.createModifiabilityScenarioS2(type, response), "M2", evaluationType);
		//Complexity 
		evaluationType = KAMPPCMBotDeprecated.TYPE_COMPLEXITY;
		type = ResponseMeasureType.DECIMAL;
		response = new Float(120);
		this.testModifiabilityScenario(this.createModifiabilityScenarioS1(type, response), "M1", evaluationType);
		response = new Float(300);
		this.testModifiabilityScenario(this.createModifiabilityScenarioS2(type, response), "M2", evaluationType);
		//Print code
		this.printCode();
	}
	
	@SuppressWarnings("rawtypes")
	public void testModifiabilityScenario(PCMScenario scenario, String scenarioName, String evaluationType) throws Exception {
		boolean debug = false;
		Comparable expectedResponse = scenario.getExpectedResult().getResponse();
		if(debug) java.lang.System.out.println("The goal of scenario " + scenarioName + ": " + expectedResponse.toString());
		KAMPPCMBotDeprecated bot = new KAMPPCMBotDeprecated(scenario);
		bot.setEvaluationType(evaluationType);
		if(debug) java.lang.System.out.println("The evaluation type is: " + evaluationType);
		//
		for (int i = 0; i < modelNames.length; i++) {
			PCMArchitectureInstance model = this.loadSpecificModel(modelNames[i], repositoryFile[i], resourceEnvironmentFile[i], baseSystemFile[i], baseAllocationFile[i], baseUsageFile[i]);
			PCMScenarioResult scenarioResult = bot.analyze(model);
			String satisfaction_alt1 = scenarioResult.isSatisfied() >= 0 ? "SATISFIED" : "NOT SATISFIED";
			if(debug) java.lang.System.out.println("The scenario satisfaction with " + model.getName() + " is: " + satisfaction_alt1);
			Comparable response_alt1 = scenarioResult.getResult().getResponse();
			if(response_alt1 instanceof Integer)
				if(debug) java.lang.System.out.println("The response measure of the scenario is: " + ((Integer) response_alt1).intValue());
			if(response_alt1 instanceof Float)
				if(debug) java.lang.System.out.println("The response measure of the scenario is: " + ((Float) response_alt1).floatValue());
			//
			AnalysisResult analysisResult = new AnalysisResult();
			analysisResult.qa = "Modifiability";
			analysisResult.scenario = scenarioName.toLowerCase();
			analysisResult.model = model.getName();
			if(resultsMap.keySet().contains(analysisResult)) {
				analysisResult = resultsMap.get(analysisResult);
			}
			else {
				results.add(analysisResult);
				resultsMap.put(analysisResult, analysisResult);
			}
			analysisResult.measureValues.put(evaluationType, response_alt1);
		}
	}
	
	private PCMScenario createModifiabilityScenarioS1(ResponseMeasureType type, Comparable response) {
		ModifiabilityPCMScenario scenario = new ModifiabilityPCMScenario(OptimizationType.MINIMIZATION);
		PCMResult expectedResult = new PCMResult(type);
		expectedResult.setResponse(response);
		scenario.setExpectedResponse(expectedResult);
		//
		ModifiabilityInstruction i1 = new ModifiabilityInstruction();
		i1.operation = ModifiabilityOperation.MODIFY;
		i1.element = ModifiabilityElement.INTERFACE;
		i1.parameters.put("name", "IExternalPayment");
		scenario.addChange(i1);
		ModifiabilityInstruction i2 = new ModifiabilityInstruction();
		i2.operation = ModifiabilityOperation.MODIFY;
		i2.element = ModifiabilityElement.COMPONENT;
		i2.parameters.put("name", "BusinessTripMgmt");
		scenario.addChange(i2);
		//
		return scenario;
	}

	/** This is the right way of implementing scenario S1. However, it does not work because KAMP
	 * doesn't propagate changes to components when an operation is added to an interface.
	 **/
	/*private PCMScenario createModifiabilityScenarioS1(ResponseMeasureType type, Comparable response) {
		ModifiabilityPCMScenario scenario = new ModifiabilityPCMScenario(OptimizationType.MINIMIZATION);
		PCMResult expectedResult = new PCMResult(type);
		expectedResult.setResponse(response);
		scenario.setExpectedResponse(expectedResult);
		//
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
		scenario.addChange(i3);
		//
		return scenario;
	}*/
	
	private PCMScenario createModifiabilityScenarioS2(ResponseMeasureType type, Comparable response) {	
		ModifiabilityPCMScenario scenario = new ModifiabilityPCMScenario(OptimizationType.MINIMIZATION);
		PCMResult expectedResult = new PCMResult(type);
		expectedResult.setResponse(response);
		scenario.setExpectedResponse(expectedResult);
		//
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
		//
		return scenario;
	}

	/** This is the right way of implementing scenario S2. However, it does not work because KAMP
	 * doesn't propagate changes to components when an operation is added to an interface.
	 * @throws Exception 
	 **/
	/*private PCMScenario createModifiabilityScenarioS2(ResponseMeasureType type) {
		ModifiabilityPCMScenario scenario = new ModifiabilityPCMScenario(OptimizationType.MINIMIZATION);
		PCMResult expectedResult = new PCMResult(type);
		expectedResult.setResponse(response);
		scenario.setExpectedResponse(expectedResult);
		//
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
		scenario.addChange(i6);
		//
		return scenario;
	}*/
	
	public void printCode() {
		for(AnalysisResult r : results) {
			if(r.qa.equals("Modifiability")) {
				String variableName = r.scenario + "Bot";
				String methodName = ".insertInOrder";
				String classType = r.qa + "Proposal";
				String components = r.measureValues.get(KAMPPCMBotDeprecated.TYPE_ELEMENTS).toString();
				String complexity = r.measureValues.get(KAMPPCMBotDeprecated.TYPE_COMPLEXITY).toString() + "f";
				String parameters = "(" + components + ", " + complexity + ", \"" + r.model + "\")";
				String codeLine = variableName + methodName + "(" + "new " + classType + parameters + ")" + ";";
				java.lang.System.out.println(codeLine);
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		SimpleTacticsECSATest test = new SimpleTacticsECSATest();
		test.runAllTests();
	}
	
	class AnalysisResult {
		public String qa;
		public String scenario;
		public String model;
		public Map<String, Comparable> measureValues;
		
		public AnalysisResult() {
			measureValues = new HashMap<String, Comparable>();
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((model == null) ? 0 : model.hashCode());
			result = prime * result + ((qa == null) ? 0 : qa.hashCode());
			result = prime * result + ((scenario == null) ? 0 : scenario.hashCode());
			return result;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			AnalysisResult other = (AnalysisResult) obj;
			if (model == null) {
				if (other.model != null)
					return false;
			} else if (!model.equals(other.model))
				return false;
			if (qa == null) {
				if (other.qa != null)
					return false;
			} else if (!qa.equals(other.qa))
				return false;
			if (scenario == null) {
				if (other.scenario != null)
					return false;
			} else if (!scenario.equals(other.scenario))
				return false;
			return true;
		}
	}
}
package io.github.squat_team.agentsUtils.transformations;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.URI;

import edu.squat.transformations.ArchitecturalVersion;
import io.github.squat_team.agentsUtils.PerformanceScenarioHelper;
import io.github.squat_team.model.PCMArchitectureInstance;
import io.github.squat_team.model.PCMScenarioResult;
import io.github.squat_team.performance.AbstractPerformancePCMScenario;
import io.github.squat_team.performance.peropteryx.PerOpteryxPCMBot;
import io.github.squat_team.util.PCMWorkingCopyCreator;
import test.TestConstants;

public class PerformanceTransformationFactory {

	
	public List<ArchitecturalVersion> generateArchitecturalVersionsUsingPerformanceTransformations(
			ArchitecturalVersion architecturalVersion) {
		List<ArchitecturalVersion> ret=new ArrayList<ArchitecturalVersion>();
		AbstractPerformancePCMScenario workloadScenario = PerformanceScenarioHelper.createScenarioOfWorkload();
		ret.addAll(createAlternativesForScenario(architecturalVersion, workloadScenario));
		AbstractPerformancePCMScenario cpuScenario = PerformanceScenarioHelper.createScenarioOfCPU();
		ret.addAll(createAlternativesForScenario(architecturalVersion, cpuScenario));
		return ret;
	}

	

	private List<ArchitecturalVersion> createAlternativesForScenario(ArchitecturalVersion architecturalVersion,
			AbstractPerformancePCMScenario scenario) {
		
		
		List<ArchitecturalVersion> ret=new ArrayList<ArchitecturalVersion>();
		

		PerOpteryxPCMBot bot=PerformanceScenarioHelper.createPCMBot(scenario);
		PCMArchitectureInstance architecture=PerformanceScenarioHelper.createArchitecture(architecturalVersion);
		
		
		List<PCMScenarioResult> results = bot.searchForAlternatives(architecture);
		
		for (Iterator<PCMScenarioResult> iterator = results.iterator(); iterator.hasNext();) {
			PCMScenarioResult pcmScenarioResult = (PCMScenarioResult) iterator.next();
			
			PCMArchitectureInstance archInstance=pcmScenarioResult.getResultingArchitecture();
			URI uri=archInstance.getUsageModel().eResource().getURI();
			File modelFile=new File(uri.toFileString());
			String newModelName=modelFile.getParentFile().getParentFile().getName()+"-"+modelFile.getParentFile().getName();
			PCMWorkingCopyCreator copyCreator=new PCMWorkingCopyCreator(newModelName, new File(TestConstants.MAIN_STORAGE_PATH));
			PCMArchitectureInstance archInstanceInRightLocation=copyCreator.createWorkingCopy(archInstance);
			File modelFileRightLocation=new File(archInstanceInRightLocation.getUsageModel().eResource().getURI().toFileString());
			ArchitecturalVersion newAlternative=new ArchitecturalVersion(modelFileRightLocation.getName().substring(0, modelFileRightLocation.getName().lastIndexOf('.')), modelFileRightLocation.getParentFile().getName(), ArchitecturalVersion.PERFORMANCE);
		
			newAlternative.setFullPathToAlternativeRepository(archInstanceInRightLocation.getRepositoryWithAlternatives().eResource().getURI().toFileString());
			ret.add(newAlternative);
		}
		
		return ret;
	}

	
	
}

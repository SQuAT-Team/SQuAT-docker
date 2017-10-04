package io.github.squat_team.modifiability.kamp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.henshin.model.resource.HenshinResourceSet;

import edu.squat.transformations.ArchitecturalVersion;
import edu.squat.transformations.modifiability.PCMTransformerRunner;
import edu.squat.transformations.modifiability.insinter.InsInterRunner;
import edu.squat.transformations.modifiability.splitrespn.SplitRespNRunner;
import edu.squat.transformations.modifiability.wrapper.WrapperRunner;
import io.github.squat_team.HenshinResourceSetManager;
import io.github.squat_team.model.PCMArchitectureInstance;
import io.github.squat_team.util.PCMRepositoryModifier;

public class ModifiabilityTransformationsFactory {
	private final static String wrapperHenshinFilename = "wrapper-modular.henshin";
	private final static String splitrespHenshinFilename = "splitrespn-modular.henshin";
	private final static String insinterHenshinFilename = "insinter-modular.henshin";
	private ArchitecturalVersion currentInitialArchitecture;
	private HenshinResourceSet resourceSet;
	private PCMRepositoryModifier repoModifier;
	
	public List<ArchitecturalVersion> runModifiabilityTransformationsInAModel(ArchitecturalVersion initialArchitecture){
		List<ArchitecturalVersion> ret=new ArrayList<>();
		this.currentInitialArchitecture=initialArchitecture;
		resourceSet = HenshinResourceSetManager.getInstance().getHenshinResourceSet(currentInitialArchitecture.getAbsolutePath());
		mergeRepository();
		
		ret.addAll(runWrapper());
		List<ArchitecturalVersion> splitAlternatives=runSplitResp();
		
		ret.addAll(splitAlternatives);
		
		splitRepository(ret);
		
		for (Iterator<ArchitecturalVersion> iterator = splitAlternatives.iterator(); iterator.hasNext();) {
			ArchitecturalVersion architecturalVersion = (ArchitecturalVersion) iterator.next();
			resourceSet = HenshinResourceSetManager.getInstance().getHenshinResourceSet(currentInitialArchitecture.getAbsolutePath());
			currentInitialArchitecture=architecturalVersion;
			mergeRepository();
			List<ArchitecturalVersion> ret2=runWrapper();
			ret.addAll(ret2);
			splitRepository(ret2);
		}
		return ret;
	}

	private void splitRepository(List<ArchitecturalVersion> ret) {
		// remove alternative components
		for (Iterator<ArchitecturalVersion> iterator = ret.iterator(); iterator.hasNext();) {
			ArchitecturalVersion architecturalVersion = (ArchitecturalVersion) iterator.next();
			PCMArchitectureInstance loadedArchitecture = TestHelper.createArchitecture(architecturalVersion);
			
			repoModifier.separateRepository(loadedArchitecture);
			architecturalVersion.setFullPathToAlternativeRepository(loadedArchitecture.getRepositoryWithAlternatives().eResource().getURI().toFileString());
		}
		PCMArchitectureInstance loadedInitialArchitecture = TestHelper.createArchitecture(currentInitialArchitecture);
		repoModifier.separateRepository(TestHelper.createArchitecture(currentInitialArchitecture));
		currentInitialArchitecture.setFullPathToAlternativeRepository(loadedInitialArchitecture.getRepositoryWithAlternatives().eResource().getURI().toFileString());
	}

	private void mergeRepository() {
		repoModifier=new PCMRepositoryModifier(TestHelper.createArchitecture(currentInitialArchitecture));
		repoModifier.mergeRepositories();
	}

	public List<ArchitecturalVersion> runWrapper() {
		WrapperRunner runner = new WrapperRunner();
		return this.runTransformation(runner, wrapperHenshinFilename);
	}
	
	public List<ArchitecturalVersion> runSplitResp() {
		SplitRespNRunner runner = new SplitRespNRunner();
		return this.runTransformation(runner, splitrespHenshinFilename);
	}
	
	public List<ArchitecturalVersion> runInsInter() {
		InsInterRunner runner = new InsInterRunner();
		return this.runTransformation(runner, insinterHenshinFilename);
	}
	
	private List<ArchitecturalVersion> runTransformation( PCMTransformerRunner runner, String henshinFilename) {
		List<ArchitecturalVersion> ret;
		ArchitecturalVersion resultantArchitecture=new ArchitecturalVersion(currentInitialArchitecture.getFileName() + "-" + "#REPLACEMENT#",currentInitialArchitecture.getPath(),"");
		
		runner.setResourceSet(resourceSet);
		ret= runner.run(currentInitialArchitecture.getAbsolutePath(),
				currentInitialArchitecture.getRepositoryFilename(), currentInitialArchitecture.getSystemFilename(), currentInitialArchitecture.getResourceEnvironmentFilename(), currentInitialArchitecture.getAllocationFilename(), currentInitialArchitecture.getUsageFilename(),
			henshinFilename, 
			resultantArchitecture.getRepositoryFilename(), resultantArchitecture.getSystemFilename(), resultantArchitecture.getResourceEnvironmentFilename(), resultantArchitecture.getAllocationFilename(), resultantArchitecture.getUsageFilename(),
			true);
		
		return ret;
	}
}

package edu.squat.transformations.modifiability.tests;

import edu.squat.transformations.modifiability.PCMTransformerRunner;
import edu.squat.transformations.modifiability.insinter.InsInterRunner;
import edu.squat.transformations.modifiability.splitrespn.SplitRespNRunner;
import edu.squat.transformations.modifiability.wrapper.WrapperRunner;

public class TesterECSA {
	private final static String wrapperHenshinFilename = "wrapper-modular.henshin";
	private final static String splitrespHenshinFilename = "splitrespn-modular.henshin";
	private final static String insinterHenshinFilename = "insinter-modular.henshin";
	//
	private String repositoryFilename, systemFilename, resourceEnvironmentFilename, allocationFilename, usageFilename;
	private String resultRepositoryFilename, resultSystemFilename, resultResourceEnvironmentFilename, resultAllocationFilename, resultUsageFilename;

	public void runWrapper(String dirPath, String filename) {
		WrapperRunner runner = new WrapperRunner();
		this.runTransformation(dirPath, filename, runner, wrapperHenshinFilename);
	}
	
	public void runSplitResp(String dirPath, String filename) {
		SplitRespNRunner runner = new SplitRespNRunner();
		this.runTransformation(dirPath, filename, runner, splitrespHenshinFilename);
	}
	
	public void runInsInter(String dirPath, String filename) {
		InsInterRunner runner = new InsInterRunner();
		this.runTransformation(dirPath, filename, runner, insinterHenshinFilename);
	}
	
	private void runTransformation(String dirPath, String filename, PCMTransformerRunner runner, String henshinFilename) {
		this.setFilenames(filename);
		runner.run(dirPath,
			repositoryFilename, systemFilename, resourceEnvironmentFilename, allocationFilename, usageFilename,
			henshinFilename, 
			resultRepositoryFilename, resultSystemFilename, resultResourceEnvironmentFilename, resultAllocationFilename, resultUsageFilename,
			true);
	}
	
	private void setFilenames(String filename) {
		repositoryFilename = filename + ".repository";
		systemFilename = filename + ".system";
		resourceEnvironmentFilename = filename + ".resourceenvironment";
		allocationFilename = filename + ".allocation";
		usageFilename = filename + ".usagemodel";
		//
		resultRepositoryFilename = filename + "-" + "#REPLACEMENT#" + ".repository";
		resultSystemFilename = filename + "-" + "#REPLACEMENT#" + ".system";
		resultResourceEnvironmentFilename = filename + "-" + "#REPLACEMENT#" + ".resourceenvironment";
		resultAllocationFilename = filename + "-" + "#REPLACEMENT#" + ".allocation";
		resultUsageFilename = filename + "-" + "#REPLACEMENT#" + ".usagemodel";
	}
	
	public static void main(String[] args) {
		String filename;
		// Modifiability
		String modPath = "test/ecsa/modifiability";
		TesterECSA tester = new TesterECSA();
		
		// Run first level transformations for the initial model
		filename = "stplus"; 
		//tester.runWrapper(modPath, filename); 
		//tester.runSplitResp(modPath, filename);
		// Run second level transformations
		filename = "stplus-0-Payment System"; 
		tester.runWrapper(modPath, filename);
		
		// Run first level transformations for the candidates of the two performance scenarios
		filename = "default";
		String perfPath = "test/ecsa/performance-modifiability/";
		String perfScenario1 = perfPath + "ps1/";
		String[] perfScenario1Candidates = new String[] { 
			perfScenario1 + "candidate258_minPlus",
			perfScenario1 + "candidate281_minPlus",
			perfScenario1 + "candidate338_minPlus",
			perfScenario1 + "candidate340_minPlus",
			perfScenario1 + "candidate397_minPlus",
			perfScenario1 + "candidate404_minPlus",
			perfScenario1 + "candidate436_minPlus",
			perfScenario1 + "candidate444_minPlus",
			perfScenario1 + "candidate494_minPlus",
			perfScenario1 + "candidate64_minPlus"
		};
		String perfScenario2 = perfPath + "ps2/";
		String[] perfScenario2Candidates = new String[] { 
			perfScenario2 + "candidate209_minPlus",
			perfScenario2 + "candidate22_minPlus", 
			perfScenario2 + "candidate325_minPlus",
			perfScenario2 + "candidate330_minPlus", 
			perfScenario2 + "candidate358_minPlus",
			perfScenario2 + "candidate366_minPlus", 
			perfScenario2 + "candidate416_minPlus",
			perfScenario2 + "candidate476_minPlus", 
			perfScenario2 + "candidate479_minPlus",
			perfScenario2 + "candidate480_minPlus"
		};
		int index;
		
		index = 0;
		//tester.runSplitResp(perfScenario1Candidates[index], filename);
		//tester.runWrapper(perfScenario1Candidates[index], filename);

		index = 0;
		//tester.runSplitResp(perfScenario2Candidates[index], filename);
		//tester.runWrapper(perfScenario2Candidates[index], filename);
		
//		for(String candidatePath : perfScenario1Candidates) {
//			tester.runSplitResp(candidatePath, filename);
//			tester.runWrapper(candidatePath, filename);
//		}
//		for(String candidatePath : perfScenario2Candidates) {
//			tester.runSplitResp(candidatePath, filename);
//			tester.runWrapper(candidatePath, filename); 
//		}

		// Run second level transformations for the candidates of the two performance scenarios
		index = 9;
		filename = "default-0-Payment System";
		//tester.runWrapper(perfScenario1Candidates[index], filename);
		//tester.runWrapper(perfScenario2Candidates[index], filename);
	}
}

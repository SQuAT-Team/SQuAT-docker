package io.github.squat_team.modifiability.kamp;

import edu.squat.transformations.modifiability.wrapper.WrapperRunner;

/**
 * Tests runs of transformations in different directories.
 */
public class DoubleRunTest {
	// @Test
	public void test() {
		String dirPath = "/home/sebastian/git/SQuAT-docker/squat.modifiability/model";
		String dirPath2 = "/home/sebastian/git/SQuAT-docker/squat.modifiability/model2";
		String henshinFilename = "wrapper-modular.henshin";
		String henshinFilename2 = "wrapper-modular.henshin";
		String repositoryFilename, systemFilename, resourceEnvironmentFilename, allocationFilename, usageFilename;
		String resultRepositoryFilename, resultSystemFilename, resultResourceEnvironmentFilename,
				resultAllocationFilename, resultUsageFilename;

		WrapperRunner runner = new WrapperRunner();

		// Complete Individual testing
		repositoryFilename = "default.repository";
		systemFilename = "default.system";
		resourceEnvironmentFilename = "default.resourceenvironment";
		allocationFilename = "default.allocation";
		usageFilename = "default.usagemodel";
		resultRepositoryFilename = "default-" + "#REPLACEMENT#" + ".repository";
		resultSystemFilename = "default-" + "#REPLACEMENT#" + ".system";
		resultResourceEnvironmentFilename = "default-" + "#REPLACEMENT#" + ".resourceenvironment";
		resultAllocationFilename = "default-" + "#REPLACEMENT#" + ".allocation";
		resultUsageFilename = "default-" + "#REPLACEMENT#" + ".usagemodel";
		runner.run(dirPath, repositoryFilename, systemFilename, resourceEnvironmentFilename, allocationFilename,
				usageFilename, henshinFilename, resultRepositoryFilename, resultSystemFilename,
				resultResourceEnvironmentFilename, resultAllocationFilename, resultUsageFilename, true);

		System.out.println("NEXT MODEL");
		WrapperRunner runner2 = new WrapperRunner();

		// Complete SimpleTactics+ testing
		repositoryFilename = "default.repository";
		systemFilename = "default.system";
		resourceEnvironmentFilename = "default.resourceenvironment";
		allocationFilename = "default.allocation";
		usageFilename = "default.usagemodel";
		resultRepositoryFilename = "default-" + "#REPLACEMENT#" + ".repository";
		resultSystemFilename = "default-" + "#REPLACEMENT#" + ".system";
		resultResourceEnvironmentFilename = "default-" + "#REPLACEMENT#" + ".resourceenvironment";
		resultAllocationFilename = "default-" + "#REPLACEMENT#" + ".allocation";
		resultUsageFilename = "default-" + "#REPLACEMENT#" + ".usagemodel";
		runner2.run(dirPath2, repositoryFilename, systemFilename, resourceEnvironmentFilename, allocationFilename,
				usageFilename, henshinFilename2, resultRepositoryFilename, resultSystemFilename,
				resultResourceEnvironmentFilename, resultAllocationFilename, resultUsageFilename, true);
	}
}

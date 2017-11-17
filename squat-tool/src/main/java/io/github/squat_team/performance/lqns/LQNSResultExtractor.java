package io.github.squat_team.performance.lqns;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.CoreException;
import org.opt4j.core.Criterion;
import org.palladiosimulator.solver.lqn.LqnModelType;
import org.palladiosimulator.solver.models.PCMInstance;
import org.palladiosimulator.solver.transformations.pcm2lqn.LqnXmlHandler;

import de.uka.ipd.sdq.dsexplore.analysis.AnalysisFailedException;
import de.uka.ipd.sdq.dsexplore.launch.DSELaunch;
import de.uka.ipd.sdq.dsexplore.launch.DSEWorkflowConfiguration;
import io.github.squat_team.performance.peropteryx.configuration.Configuration;
import io.github.squat_team.performance.peropteryx.configuration.DSEWorkflowConfigurationBuilder;
import io.github.squat_team.performance.peropteryx.overwrite.MyDSELaunch;
import io.github.squat_team.performance.peropteryx.overwrite.analysis.MyLQNQualityAttributeDeclaration;

/**
 * Extracts a LQNSResult from the the LQN solver output file.
 */
public class LQNSResultExtractor {

	public static LQNSResult extract(PCMInstance pcmInstance, Configuration configuration, String outputPath)
			throws CoreException, AnalysisFailedException {
		DSEWorkflowConfiguration dseConfiguration = buildDSEWorkflowConfiguration(configuration);
		Criterion criterion = buildCriterion(pcmInstance, dseConfiguration);
		String solverFilePath = getOutputFile(outputPath);
		return buildLQNSResult(pcmInstance, criterion, solverFilePath);
	}

	private static DSEWorkflowConfiguration buildDSEWorkflowConfiguration(Configuration configuration)
			throws CoreException {
		DSELaunch launch = new MyDSELaunch(); // just uses reset debugger
		DSEWorkflowConfigurationBuilder builder = new DSEWorkflowConfigurationBuilder();
		builder.init(configuration);
		return builder.build(launch);
	}

	private static Criterion buildCriterion(PCMInstance pcmInstance, DSEWorkflowConfiguration dseConfiguration)
			throws CoreException {
		MyLQNQualityAttributeDeclaration declaration = new MyLQNQualityAttributeDeclaration();
		CriteriaInitializer criteriaInitializer = new CriteriaInitializer(pcmInstance, declaration);
		criteriaInitializer.initializeCriteria(dseConfiguration);
		List<Criterion> criterions = criteriaInitializer.getCriterions();
		if (!criterions.isEmpty()) {
			return criterions.get(0);
		} else {
			return null;
		}
	}

	private static String getOutputFile(String outputPath) {
		final List<Path> lqnsResultsFile;
		try {
			lqnsResultsFile = Files.list(new File(outputPath).toPath())
					.filter(path -> path.toString().toLowerCase().endsWith(".out.lqxo")).collect(Collectors.toList());
		} catch (final IOException e) {
			throw new RuntimeException("Could not read results directory: " + outputPath, e);
		}
		if (lqnsResultsFile.isEmpty()) {
			throw new RuntimeException("Analysis failed. No LQNS result files in directory: " + outputPath);
		}
		// assume there is only one! Uses different paths every time!
		String solverFilePath = lqnsResultsFile.get(lqnsResultsFile.size() - 1).toUri().toString();
		return solverFilePath.replaceAll("file:.", "");
	}

	private static LQNSResult buildLQNSResult(PCMInstance pcmInstance, Criterion criterion, String solverFilePath)
			throws AnalysisFailedException {
		LqnModelType model = LqnXmlHandler.loadModelFromXMI(solverFilePath);
		return new LQNSResult(pcmInstance, model, criterion);
	}

}

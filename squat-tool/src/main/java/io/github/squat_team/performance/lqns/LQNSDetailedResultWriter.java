package io.github.squat_team.performance.lqns;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.palladiosimulator.pcm.core.entity.NamedElement;

import de.fakeller.performance.analysis.result.PerformanceResult;
import de.fakeller.performance.analysis.result.Result;
import io.github.squat_team.model.PCMArchitectureInstance;
import io.github.squat_team.util.PCMFileFinder;

/**
 * Writes the detailed results of the LQN Solver analysis to a file in the
 * folder of the PCM instance.
 */
public class LQNSDetailedResultWriter {
	private static final String FILE_EXTENSION = "detailed";
	private PerformanceResult<NamedElement> result;

	public static File determineFileDestination(PCMArchitectureInstance pcmInstance) throws IOException {
		PCMFileFinder pcmPathFinder = new PCMFileFinder(pcmInstance);
		File file = new File(pcmPathFinder.getPath() + File.separator + pcmPathFinder.getName() + "." + FILE_EXTENSION);
		file.createNewFile();
		return file;
	}

	public LQNSDetailedResultWriter(PerformanceResult<NamedElement> result) {
		this.result = result;
	}

	public void writeTo(File file) throws IOException {
		BufferedWriter output = null;
		try {
			output = new BufferedWriter(new FileWriter(file));
			writeContent(output);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (output != null) {
				output.close();
			}
		}
	}

	private void writeContent(BufferedWriter writer) throws IOException {
		for (final Result<NamedElement> r : result.getResults()) {
			writer.write(r.attachedTo().getEntityName() + ": " + r.value().toHumanReadable());
			writer.newLine();
		}
	}

}

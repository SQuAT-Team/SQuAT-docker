package io.github.squat_team.performance.peropteryx.export;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.solver.models.PCMInstance;

import de.uka.ipd.sdq.dsexplore.analysis.PCMPhenotype;
import de.uka.ipd.sdq.dsexplore.opt4j.start.Opt4JStarter;

/**
 * Persists the given PCM instance, when
 * {@link #savePCM(PCMPhenotype, PCMInstance)} is called. Make sure to use
 * {@link #init(String)} before.
 */
public class PCMFileExporter {
	public static final String FILE_PREFIX = "default";
	private static Map<Long, PCMFileExporter> threadInstances = new HashMap<Long, PCMFileExporter>();

	private String date = getDate();
	private String storagePath;
	private int candidateCounter = 0;

	// Genotype IDs - Folder Names
	private Map<String, String> candidates = new HashMap<String, String>();
	// Genotype IDs - Counter
	private Map<String, Integer> calledCounter = new HashMap<String, Integer>();

	private PCMFileExporter() {
		// THREAD SINGLETON PATTERN
	}

	public static PCMFileExporter getInstance() {
		PCMFileExporter exporter;
		if (threadInstances.containsKey(Thread.currentThread().getId())) {
			exporter = threadInstances.get(Thread.currentThread().getId());
		} else {
			exporter = new PCMFileExporter();
			threadInstances.put(Thread.currentThread().getId(), exporter);
		}
		return exporter;
	}

	/**
	 * Initializes the path for the export.
	 * 
	 * @param storagePath
	 *            - path to the PCM directory.
	 */
	public void init(String storagePath) {
		this.storagePath = storagePath;
		this.date = getDate();
	}

	/**
	 * Saves the PCM, if it is called the last time. It is assumed, that each
	 * pcm is called two times for each objective.
	 * 
	 * @param pcmp
	 *            - delivers the metadata
	 * @param pcm
	 *            - instance to save
	 * @return if new PCM was saved
	 */
	public boolean savePCMChecked(PCMPhenotype pcmp, PCMInstance pcm) {
		String candidateID = pcmp.getGenotypeID();
		if (calledCounter.containsKey(candidateID)) {
			calledCounter.put(candidateID, calledCounter.get(candidateID) + 1);
		} else {
			calledCounter.put(candidateID, 1);
		}
		try {
			if (calledCounter.get(candidateID) == 2 * Opt4JStarter.getDSEEvaluator().getObjectives().size()) {
				savePCM(pcmp, pcm);
			}
		} catch (CoreException e) {
			// should not happen
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Saves the given PCM instance.
	 * 
	 * @param pcmp
	 *            - delivers the metadata
	 * @param pcm
	 *            - instance to save
	 * @return if new PCM was saved
	 */
	public boolean savePCM(PCMPhenotype pcmp, PCMInstance pcm) {
		if (!candidates.containsKey(pcmp.getGenotypeID())) {
			String folderNamePrefix = "candidate" + candidateCounter;
			String folderPath = storagePath + "/" + date + "/" + folderNamePrefix + "/";

			saveAllFiles(pcm, folderPath);
			saveInfoFile(pcmp, folderPath + FILE_PREFIX + ".txt");

			candidates.put(pcmp.getGenotypeID(), folderPath);
			this.candidateCounter++;
			return true;
		}
		return false;
	}

	/**
	 * Resets the saved candidates and closes the exporter. Call if you do not
	 * want to use this class in this run anymore.
	 */
	protected void close() {
		this.candidateCounter = 0;
		candidates.clear();
		threadInstances.remove(Thread.currentThread().getId());
	}

	protected Map<String, String> getPCMStoragePaths() {
		return candidates;
	}

	/**
	 * Saves all PCM files.
	 * 
	 * @param pcm
	 * @param directoryPath
	 */
	private void saveAllFiles(PCMInstance pcm, String directoryPath) {
		String allocationFilePath = directoryPath + FILE_PREFIX + ".allocation";
		String usagemodelFilePath = directoryPath + FILE_PREFIX + ".usagemodel";
		pcm.saveToXMIFile(pcm.getAllocation(), allocationFilePath);
		fixAllocationFile(new File(allocationFilePath), directoryPath);
		
		List<Repository> repositories = pcm.getRepositories();
		int i = 0;
		for (Repository repository : repositories) {
			if(i == 1){
				// this should be the right one
				pcm.saveToXMIFile(repository, directoryPath + FILE_PREFIX + ".repository");
			}else{
				pcm.saveToXMIFile(repository, directoryPath + FILE_PREFIX + i + ".repository");
			}
			i++;
		}
		pcm.saveToXMIFile(pcm.getResourceEnvironment(), directoryPath + FILE_PREFIX + ".resourceenvironment");
		pcm.saveToXMIFile(pcm.getResourceRepository(), directoryPath + FILE_PREFIX + ".resourcetype");
		pcm.saveToXMIFile(pcm.getSystem(), directoryPath + FILE_PREFIX + ".system");
		pcm.saveToXMIFile(pcm.getUsageModel(), directoryPath + FILE_PREFIX + ".usagemodel");
		fixUsagemodelFile(new File(usagemodelFilePath), directoryPath);
	}

	/**
	 * Saves a file which contains additional information for this pcm.
	 * 
	 * @param pcmp
	 * @param filePath
	 */
	private void saveInfoFile(PCMPhenotype pcmp, String filePath) {
		PrintWriter writer;
		try {
			writer = new PrintWriter(filePath, "UTF-8");
			writer.println("Created by Headless PerOpteryx");
			writer.println("Numeric ID:");
			writer.println(pcmp.getNumericID());
			writer.println("Genotype ID:");
			writer.println(pcmp.getGenotypeID());
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private String getDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
		Date date = new Date();
		return dateFormat.format(date);
	}

	/**
	 * Replaces the links to the other PCM files with the correct ones.
	 * 
	 * @param file
	 *            - the allocation file
	 * @param directoryPath
	 *            - the path of the pcm directory in which the allocation file
	 *            is allocated
	 */
	private void fixAllocationFile(File file, String directoryPath) {
		String str = "";
		BufferedReader br;

		// Read allocation file
		try {
			br = new BufferedReader(new FileReader(file));
			try {
				StringBuilder sb = new StringBuilder();
				String line = br.readLine();

				while (line != null) {
					sb.append(line);
					sb.append(System.lineSeparator());
					line = br.readLine();
				}

				// Adjust paths in allocation file
				str = sb.toString();
				str = str.replaceAll("href=\"" + ".*" + "cand\\.", /*"file:/" + directoryPath + */"href=\"" + FILE_PREFIX + "."); // no absolute path
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		// if successful, write back to allocation file
		if (!str.equals("")) {
			FileWriter fw;
			try {
				fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(str);
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Replaces the links to the other PCM files with the correct ones.
	 * 
	 * @param file
	 *            - the usagemodel file
	 * @param directoryPath
	 *            - the path of the pcm directory in which the usagemodel file
	 *            is allocated
	 */
	private void fixUsagemodelFile(File file, String directoryPath) {
		String str = "";
		BufferedReader br;

		// Read allocation file
		try {
			br = new BufferedReader(new FileReader(file));
			try {
				StringBuilder sb = new StringBuilder();
				String line = br.readLine();

				while (line != null) {
					sb.append(line);
					sb.append(System.lineSeparator());
					line = br.readLine();
				}

				// Adjust paths in usagemodel file
				str = sb.toString();
				str = str.replaceAll("file:/" + ".*" + "cand", /*"file:/" + directoryPath + */FILE_PREFIX); // no absolute path
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		// if successful, write back to usagemodel file
		if (!str.equals("")) {
			FileWriter fw;
			try {
				fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(str);
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

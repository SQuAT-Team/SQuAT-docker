package io.github.squat_team.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.resource.Resource;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.usagemodel.UsageModel;

import io.github.squat_team.model.PCMArchitectureInstance;

/**
 * Creates a working copy of an architecture instance.
 */
public class PCMWorkingCopyCreator {

	private static final String WORKING_COPY_NAME = "bot_workingcopy";

	/**
	 * Saves a working copy of the model to the same directory as the original.
	 * 
	 * @param resource
	 *            the model
	 * @param extension
	 *            file name extension
	 * @return URI to the file
	 */
	private String createWorkingCopy(Resource resource, String extension) {
		File file = null;
		FileOutputStream fos = null;

		try {
			// create
			file = new File(new File(resource.getURI().toFileString()).getParentFile(),
					WORKING_COPY_NAME + "." + extension);
			file.createNewFile();
			fos = new FileOutputStream(file);

			resource.save(fos, Collections.EMPTY_MAP);

			return file.toURI().toString();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
				}
			}
		}

		return "";
	}

	/**
	 * Saves the current state of the architecture and creates a working copy.
	 * 
	 * @param architecture
	 *            the architecture to save.
	 * @return the working copy.
	 */
	public PCMArchitectureInstance createWorkingCopy(PCMArchitectureInstance architecture) {
		PCMArchitectureInstance workingCopy = new PCMArchitectureInstance(architecture.getName(), null, null, null,
				null, null);

		Allocation allocation = architecture.getAllocation();
		Repository repository = architecture.getRepository();
		ResourceEnvironment resourceEnvironment = architecture.getResourceEnvironment();
		org.palladiosimulator.pcm.system.System system = architecture.getSystem();
		UsageModel usageModel = architecture.getUsageModel();
		
		// copy 
		if (repository != null && repository.eResource() != null) {
			String fileLocation = createWorkingCopy(repository.eResource(), "repository");
			workingCopy.setRepository(SQuATHelper.loadRepositoryModel(fileLocation));
		}
		if (resourceEnvironment != null && resourceEnvironment.eResource() != null) {
			String fileLocation = createWorkingCopy(resourceEnvironment.eResource(), "resourceenvironment");
			workingCopy.setResourceEnvironment(SQuATHelper.loadResourceEnvironmentModel(fileLocation));
		}
		if (system != null && system.eResource() != null) {
			String fileLocation = createWorkingCopy(system.eResource(), "system");
			org.palladiosimulator.pcm.system.System systemCopy = SQuATHelper.loadSystemModel(fileLocation);

			// fix references in system model and reload
			PCMFileFinder finder = new PCMFileFinder(architecture);
			PCMFileFinder finderCopy = new PCMFileFinder(workingCopy);
			adjustReferences(systemCopy.eResource(), finder.getName(), finderCopy.getName());
			systemCopy = SQuATHelper.loadSystemModel(fileLocation);

			workingCopy.setSystem(systemCopy);
		}
		if (usageModel != null && usageModel.eResource() != null) {
			String fileLocation = createWorkingCopy(usageModel.eResource(), "usagemodel");
			UsageModel usageModelCopy = SQuATHelper.loadUsageModel(fileLocation);

			// fix references in usage model and reload
			PCMFileFinder finder = new PCMFileFinder(architecture);
			PCMFileFinder finderCopy = new PCMFileFinder(workingCopy);
			adjustReferences(usageModelCopy.eResource(), finder.getName(), finderCopy.getName());
			usageModelCopy = SQuATHelper.loadUsageModel(fileLocation);

			workingCopy.setUsageModel(usageModelCopy);
		}
		if (allocation != null && allocation.eResource() != null) {
			String fileLocation = createWorkingCopy(allocation.eResource(), "allocation");

			// set references to other model files and save
			Allocation allocationCopy = SQuATHelper.loadAllocationModel(fileLocation);
			allocationCopy.setSystem_Allocation(workingCopy.getSystem());
			allocationCopy.setTargetResourceEnvironment_Allocation(workingCopy.getResourceEnvironment());
			try {
				allocationCopy.eResource().save(Collections.EMPTY_MAP);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// fix references in allocation model and reload
			PCMFileFinder finder = new PCMFileFinder(architecture);
			PCMFileFinder finderCopy = new PCMFileFinder(workingCopy);
			adjustReferences(allocationCopy.eResource(), finder.getName(), finderCopy.getName());
			allocationCopy = SQuATHelper.loadAllocationModel(fileLocation);

			workingCopy.setAllocation(allocationCopy);
		}
		
		workingCopy.setRepositoryWithAlternatives(architecture.getRepositoryWithAlternatives());

		return workingCopy;
	}

	private void adjustReferences(Resource resource, String oldName, String newName) {
		try {
			File file = new File(resource.getURI().toFileString());

			// read and replace
			List<String> lines = new ArrayList<String>();
			String line = null;
			FileReader fileReader;
			fileReader = new FileReader(file);

			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while ((line = bufferedReader.readLine()) != null) {
				if (line.contains("href=\"" + oldName)) {
					line = line.replaceAll("href=\"" + oldName, "href=\"" + newName);
				}
				lines.add(line);
			}
			fileReader.close();
			bufferedReader.close();

			// write
			FileWriter fileWriter = new FileWriter(file);
			BufferedWriter out = new BufferedWriter(fileWriter);
			for (String outputLine : lines) {
				out.write(outputLine);
				out.newLine();
			}

			out.flush();
			out.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
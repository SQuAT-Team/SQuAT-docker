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
	private static final String ALTERANTIVE_EXTENSION_NAME = "alternative";
	private String modelName;
	private File folder;
	
	public PCMWorkingCopyCreator(String modelName, File folder) {
		this.modelName = modelName;
		this.folder = folder;
	}
	
	public PCMWorkingCopyCreator() {
	}
	
	
	/**
	 * Saves a working copy of the model to the same directory as the original.
	 * 
	 * @param resource
	 *            the model
	 * @param extension
	 *            file name extension
	 * @return URI to the file
	 */
	private String createWorkingCopy(Resource resource, String extension, boolean altRepository) {
		File file = null;
		FileOutputStream fos = null;

		
		try {
			 //create
			if(folder == null || modelName == null){
				file = new File(new File(resource.getURI().toFileString()).getParentFile(),
						WORKING_COPY_NAME + "." + extension);			
			}else{
				if(altRepository){
					file = new File(folder,
							modelName + ALTERANTIVE_EXTENSION_NAME + "." + extension);	
				}else{
					file = new File(folder,
							modelName + "." + extension);	
				}
	
			}

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
		Repository altRepository = architecture.getRepositoryWithAlternatives();
		
		// copy 
		if (repository != null && repository.eResource() != null) {
			String fileLocation = createWorkingCopy(repository.eResource(), "repository", false);
			workingCopy.setRepository(SQuATHelper.loadRepositoryModel(fileLocation));
		}
		if (folder != null && this.modelName != null && altRepository != null && altRepository.eResource() != null) {
			String fileLocation = createWorkingCopy(altRepository.eResource(), "repository", true);
			workingCopy.setRepositoryWithAlternatives(SQuATHelper.loadRepositoryModel(fileLocation));
		}
		if (resourceEnvironment != null && resourceEnvironment.eResource() != null) {
			String fileLocation = createWorkingCopy(resourceEnvironment.eResource(), "resourceenvironment", false);
			workingCopy.setResourceEnvironment(SQuATHelper.loadResourceEnvironmentModel(fileLocation));
		}
		if (system != null && system.eResource() != null) {
			String fileLocation = createWorkingCopy(system.eResource(), "system", false);
			org.palladiosimulator.pcm.system.System systemCopy = SQuATHelper.loadSystemModel(fileLocation);

			// fix references in system model and reload
			PCMFileFinder finder = new PCMFileFinder(architecture);
			PCMFileFinder finderCopy = new PCMFileFinder(workingCopy);
			adjustReferences(systemCopy.eResource(), finder.getName(), finderCopy.getName());
			systemCopy = SQuATHelper.loadSystemModel(fileLocation);

			workingCopy.setSystem(systemCopy);
		}
		if (usageModel != null && usageModel.eResource() != null) {
			String fileLocation = createWorkingCopy(usageModel.eResource(), "usagemodel", false);
			UsageModel usageModelCopy = SQuATHelper.loadUsageModel(fileLocation);

			// fix references in usage model and reload
			PCMFileFinder finder = new PCMFileFinder(architecture);
			PCMFileFinder finderCopy = new PCMFileFinder(workingCopy);
			adjustReferences(usageModelCopy.eResource(), finder.getName(), finderCopy.getName());
			usageModelCopy = SQuATHelper.loadUsageModel(fileLocation);

			workingCopy.setUsageModel(usageModelCopy);
		}
		if (allocation != null && allocation.eResource() != null) {
			String fileLocation = createWorkingCopy(allocation.eResource(), "allocation", false);

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
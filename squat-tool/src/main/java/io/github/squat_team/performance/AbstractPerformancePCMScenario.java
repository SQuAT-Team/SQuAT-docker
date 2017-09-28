package io.github.squat_team.performance;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;

import org.eclipse.emf.ecore.resource.Resource;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.usagemodel.UsageModel;

import io.github.squat_team.model.OptimizationType;
import io.github.squat_team.model.PCMArchitectureInstance;
import io.github.squat_team.model.PCMScenario;
import io.github.squat_team.util.SQuATHelper;

public abstract class AbstractPerformancePCMScenario extends PCMScenario
		implements PerformancePCMTransformationScenario {
	private PerformanceMetric metric;

	public AbstractPerformancePCMScenario(OptimizationType type) {
		super(type);
	}

	public PerformanceMetric getMetric() {
		return metric;
	}

	public void setMetric(PerformanceMetric metric) {
		this.metric = metric;
	}

	/**
	 * Saves a model to its current location.
	 * 
	 * @param resource
	 *            the model
	 * @return true if successful
	 */
	private boolean saveModel(Resource resource) {
		try {
			resource.save(Collections.EMPTY_MAP);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Saves a model to the temp directory.
	 * 
	 * @param resource
	 *            the model
	 * @param extension
	 *            file name extension
	 * @return URI to the file
	 */
	private String saveModel(Resource resource, String extension) {
		File file = null;
		FileOutputStream fos = null;

		try {
			// create
			file = File.createTempFile("default_perfbot_scenariocopy", "." + extension,
					new File(resource.getURI().toFileString()).getParentFile());
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
	 * Saves the model.
	 * 
	 * @param architecture
	 *            the model.
	 */
	protected void saveModel(PCMArchitectureInstance architecture) {
		Allocation allocation = architecture.getAllocation();
		Repository repository = architecture.getRepository();
		ResourceEnvironment resourceEnvironment = architecture.getResourceEnvironment();
		org.palladiosimulator.pcm.system.System system = architecture.getSystem();
		UsageModel usageModel = architecture.getUsageModel();

		if (allocation != null && allocation.eResource() != null) {
			saveModel(allocation.eResource());
		}
		if (repository != null && repository.eResource() != null) {
			saveModel(repository.eResource());
		}
		if (resourceEnvironment != null && resourceEnvironment.eResource() != null) {
			saveModel(resourceEnvironment.eResource());
		}
		if (system != null && system.eResource() != null) {
			saveModel(system.eResource());
		}
		if (usageModel != null && usageModel.eResource() != null) {
			saveModel(usageModel.eResource());
		}
	}

	/**
	 * Saves the current state of the architecture and creates a working copy.
	 * 
	 * @param architecture
	 *            the architecture to save.
	 * @return the working copy.
	 */
	protected PCMArchitectureInstance createWorkingCopy(PCMArchitectureInstance architecture) {
		// TODO: works for usagemodel and allocation, but PerOpteryx searchs in
		// allocation model for the rest of the models.

		PCMArchitectureInstance workingCopy = new PCMArchitectureInstance(architecture.getName(), null, null, null,
				null, null);

		Allocation allocation = architecture.getAllocation();
		Repository repository = architecture.getRepository();
		ResourceEnvironment resourceEnvironment = architecture.getResourceEnvironment();
		org.palladiosimulator.pcm.system.System system = architecture.getSystem();
		UsageModel usageModel = architecture.getUsageModel();

		if (allocation != null && allocation.eResource() != null) {
			String fileLocation = saveModel(allocation.eResource(), "allocation");
			workingCopy.setAllocation(SQuATHelper.loadAllocationModel(fileLocation));
		}
		if (repository != null && repository.eResource() != null) {
			String fileLocation = saveModel(repository.eResource(), "repository");
			workingCopy.setRepository(SQuATHelper.loadRepositoryModel(fileLocation));
		}
		if (resourceEnvironment != null && resourceEnvironment.eResource() != null) {
			String fileLocation = saveModel(resourceEnvironment.eResource(), "resourceenvironment");
			workingCopy.setResourceEnvironment(SQuATHelper.loadResourceEnvironmentModel(fileLocation));
		}
		if (system != null && system.eResource() != null) {
			String fileLocation = saveModel(system.eResource(), "system");
			workingCopy.setSystem(SQuATHelper.loadSystemModel(fileLocation));
		}
		if (usageModel != null && usageModel.eResource() != null) {
			String fileLocation = saveModel(usageModel.eResource(), "usagemodel");
			workingCopy.setUsageModel(SQuATHelper.loadUsageModel(fileLocation));
		}

		return workingCopy;
	}

}
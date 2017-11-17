package io.github.squat_team.performance;

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.palladiosimulator.pcm.core.PCMRandomVariable;
import org.palladiosimulator.pcm.resourceenvironment.ProcessingResourceSpecification;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

import io.github.squat_team.model.OptimizationType;
import io.github.squat_team.model.PCMArchitectureInstance;

/**
 * A scenario based on a change of a CPU clock rate.
 */
public class PerformancePCMCPUScenario extends AbstractPerformancePCMScenario {
	private List<String> workloadIDs;
	private double processingRateFactor;

	/**
	 * Constructs a new scenario based on a change of CPU processing rates.
	 * 
	 * @param type
	 *            describes if the optimization is minimization or maximization.
	 * @param cpuIDs
	 *            the IDs of the CPUs which should be considered. Processing
	 *            rate will only be changed for these CPUs.
	 * @param processingRateFactor
	 *            factor to multiply the processing rate value with.
	 */
	public PerformancePCMCPUScenario(OptimizationType type, List<String> cpuIDs, double processingRateFactor) {
		super(type);
		this.workloadIDs = cpuIDs;
		this.processingRateFactor = processingRateFactor;
	}

	private void setProcessingRateInCPU(ResourceEnvironment resourceEnvironment, double factor) {
		EList<ResourceContainer> resourceContainers = resourceEnvironment.getResourceContainer_ResourceEnvironment();
		for (ResourceContainer container : resourceContainers) {
			for (ProcessingResourceSpecification resourceSpecification : container
					.getActiveResourceSpecifications_ResourceContainer()) {
				if (workloadIDs.contains(resourceSpecification.getId())) {
					setProcessingRateAttribute(resourceSpecification, factor);
				}
			}
		}
	}

	private void setProcessingRateAttribute(ProcessingResourceSpecification resourceSpecification, double factor) {
		PCMRandomVariable variable = resourceSpecification.getProcessingRate_ProcessingResourceSpecification();
		System.out.println(variable.eClass().getEAllAttributes());

		// search for processing rate attributes
		EAttribute specification = null;
		for (EAttribute attribute : variable.eClass().getEAllAttributes()) {
			if (attribute.getName().equals("specification")) {
				specification = attribute;
				break;
			}
		}

		// set new value
		String attributeText = variable.eGet(specification).toString();
		String newAttributeText;
		newAttributeText = computeBasicAttributeText(attributeText, factor);
		variable.eSet(specification, newAttributeText);
	}

	private String computeBasicAttributeText(String attributeText, double factor) {
		Double newValue = factor * Double.valueOf(attributeText);
		return newValue.toString();
	}

	@Override
	public void transform(PCMArchitectureInstance architecture) {
		ResourceEnvironment resourceEnvironment = architecture.getResourceEnvironment();
		setProcessingRateInCPU(resourceEnvironment, this.processingRateFactor);
		architecture.saveModel();
	}

	@Override
	public void inverseTransform(PCMArchitectureInstance architecture) {
		ResourceEnvironment resourceEnvironment = architecture.getResourceEnvironment();
		setProcessingRateInCPU(resourceEnvironment, 1.0 / this.processingRateFactor);
		architecture.saveModel();
	}

}
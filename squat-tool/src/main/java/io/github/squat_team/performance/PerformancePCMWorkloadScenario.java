package io.github.squat_team.performance;

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.palladiosimulator.pcm.usagemodel.UsageScenario;
import org.palladiosimulator.pcm.usagemodel.Workload;

import io.github.squat_team.model.OptimizationType;
import io.github.squat_team.model.PCMArchitectureInstance;

/**
 * A scenario based on a change of the workload.
 */
public class PerformancePCMWorkloadScenario extends AbstractPerformancePCMScenario {
	private List<String> workloadIDs;
	private double workloadFactor;

	/**
	 * Constructs a new workload scenario.
	 * 
	 * @param type
	 *            describes if the optimization is minimization or maximization.
	 * @param workloadIDs
	 *            the IDs of the Usage Scenarios which should be considered.
	 *            Workload will only be changed for these Usage Scenarios.
	 * @param workloadFactor
	 *            factor to multiply the workload value with. For a function
	 *            like Exp(x) the factor will be multiplied with x.
	 */
	public PerformancePCMWorkloadScenario(OptimizationType type, List<String> workloadIDs, double workloadFactor) {
		super(type);
		this.workloadIDs = workloadIDs;
		this.workloadFactor = workloadFactor;
	}

	private void setWorkloadInUsageScenarios(UsageModel usageModel, double factor) {
		EList<UsageScenario> usageScenarios = usageModel.getUsageScenario_UsageModel();
		for (UsageScenario usageScenario : usageScenarios) {
			// only specified ones
			if (workloadIDs.contains(usageScenario.getId())) {
				setWorkloadAttribute(usageScenario, factor);
			}
		}
	}

	private void setWorkloadAttribute(UsageScenario usageScenario, double factor) {
		Workload workload = usageScenario.getWorkload_UsageScenario();

		// search for workload attributes
		for (EObject workloadValue : workload.eContents()) {
			EAttribute specification = null;
			for (EAttribute attribute : workloadValue.eClass().getEAllAttributes()) {
				if (attribute.getName().equals("specification")) {
					specification = attribute;
					break;
				}
			}

			// set new value
			String attributeText = workloadValue.eGet(specification).toString();
			String newAttributeText;
			if (attributeText.startsWith("Exp(")) {
				newAttributeText = computeExpAttributeText(attributeText, factor);
			} else {
				newAttributeText = computeBasicAttributeText(attributeText, factor);
			}
			workloadValue.eSet(specification, newAttributeText);
		}

	}

	private String computeBasicAttributeText(String attributeText, double factor) {
		Double newValue = factor * Double.valueOf(attributeText);
		return newValue.toString();
	}

	private String computeExpAttributeText(String attributeText, double factor) {
		String attributeNumber = attributeText.substring(4, attributeText.length() - 1);

		Double newValue = factor * Double.valueOf(attributeNumber);
		return "Exp(" + newValue.toString() + ")";
	}

	@Override
	public void transform(PCMArchitectureInstance architecture) {
		UsageModel usageModel = architecture.getUsageModel();
		setWorkloadInUsageScenarios(usageModel, this.workloadFactor);
		architecture.saveModel();
	}

	@Override
	public void inverseTransform(PCMArchitectureInstance architecture) {
		setWorkloadInUsageScenarios(architecture.getUsageModel(), 1.0 / this.workloadFactor);
		architecture.saveModel();
	}

}
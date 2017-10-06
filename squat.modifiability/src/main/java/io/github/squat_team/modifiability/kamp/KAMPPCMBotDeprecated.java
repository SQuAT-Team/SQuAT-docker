package io.github.squat_team.modifiability.kamp;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.palladiosimulator.pcm.core.entity.InterfaceProvidingEntity;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.Interface;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.OperationProvidedRole;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.repository.ProvidedRole;
import org.palladiosimulator.pcm.repository.RepositoryComponent;
import org.palladiosimulator.pcm.repository.impl.BasicComponentImpl;
import org.palladiosimulator.pcm.repository.impl.OperationProvidedRoleImpl;
import org.palladiosimulator.pcm.seff.AbstractAction;
import org.palladiosimulator.pcm.seff.AbstractBranchTransition;
import org.palladiosimulator.pcm.seff.BranchAction;
import org.palladiosimulator.pcm.seff.GuardedBranchTransition;
import org.palladiosimulator.pcm.seff.LoopAction;
import org.palladiosimulator.pcm.seff.ResourceDemandingBehaviour;
import org.palladiosimulator.pcm.seff.SeffPackage;
import org.palladiosimulator.pcm.seff.ServiceEffectSpecification;
import org.palladiosimulator.pcm.seff.impl.InternalActionImpl;
import org.palladiosimulator.pcm.seff.impl.ResourceDemandingSEFFImpl;
import org.palladiosimulator.pcm.seff.impl.StartActionImpl;
import org.palladiosimulator.pcm.seff.impl.StopActionImpl;

import edu.kit.ipd.sdq.kamp.core.Activity;
import edu.kit.ipd.sdq.kamp.core.ActivityElementType;
import edu.kit.ipd.sdq.kamp.core.ArchitectureModelFactoryFacade;
import edu.kit.ipd.sdq.kamp.core.ArchitectureModelLookup;
import edu.kit.ipd.sdq.kamp.core.ArchitectureVersion;

import io.github.squat_team.AbstractPCMBot;
import io.github.squat_team.model.PCMArchitectureInstance;
import io.github.squat_team.model.PCMResult;
import io.github.squat_team.model.PCMScenario;
import io.github.squat_team.model.PCMScenarioResult;
import io.github.squat_team.model.ResponseMeasureType;
import io.github.squat_team.modifiability.ModifiabilityInstruction;
import io.github.squat_team.modifiability.ModifiabilityPCMScenario;

/**
 * This class was is used in tests, but it was only used for
 * {@link #analyze(PCMArchitectureInstance)} in the final approach. The new
 * {@link KAMPPCMBot} should be used instead, because it fully implements the
 * interface. This class only exists for the old tests.
 * 
 * @deprecated
 */
@SuppressWarnings("unused")
public class KAMPPCMBotDeprecated extends AbstractPCMBot {
	public static String TYPE_ELEMENTS = "Elements";
	public static String TYPE_COMPLEXITY = "Complexity";
	public static String TYPE_EFFORT = "Effort";
	//
	protected ArchitectureVersion baseAV;
	protected ArchitectureVersion changedAV;
	//
	private String evaluationType = TYPE_COMPLEXITY;

	public KAMPPCMBotDeprecated(PCMScenario scenario) {
		super(scenario);
	}

	public String getEvaluationType() {
		return evaluationType;
	}

	public void setEvaluationType(String evaluationType) {
		this.evaluationType = evaluationType;
	}

	@Override
	public PCMScenarioResult analyze(PCMArchitectureInstance currentArchitecture) {
		// Run the propagation and create the result
		PCMScenarioResult scenarioResult = new PCMScenarioResult(this);
		// Register the initial architecture
		// There is no tactic, because this is just an evaluation
		scenarioResult.setAppliedTactic(null);
		// The resulting architecture is just the original one, because we did not make
		// changes
		scenarioResult.setResultingArchitecture(currentArchitecture);
		try {
			// First convert the base model
			ArchitectureVersionWrapper baseWrapper = new ArchitectureVersionWrapper(currentArchitecture);
			baseAV = baseWrapper.transformToKAMP();
			// Pessimistic approach for CIA - This could be configured from outside...
			ArchitectureModelFactoryFacade.setupComponentInternalDependenciesPessimistic(baseAV);
			// Second create the changed model
			ArchitectureVersionWrapper changedWrapper = new ArchitectureVersionWrapper(currentArchitecture);
			changedAV = changedWrapper.transformToKAMP();
			this.setupChangedModel();
			// Pessimistic approach for CIA - This could be configured from outside...
			ArchitectureModelFactoryFacade.setupComponentInternalDependenciesPessimistic(changedAV);
			// Run CIA
			List<Activity> activities = KAMPHeadlessRunner.runAnalysis(baseAV, changedAV);
			// Create the result
			PCMResult result = null;
			if (evaluationType.equals(TYPE_ELEMENTS)) {
				result = new PCMResult(ResponseMeasureType.NUMERIC);
				int changes = this.computeElementsReponse(activities);
				result.setResponse(new Integer(changes));
			} else if (evaluationType.equals(TYPE_COMPLEXITY)) {
				result = new PCMResult(ResponseMeasureType.DECIMAL);
				float complexity = this.computeComplexityReponse(activities);
				result.setResponse(new Float(complexity));
			}
			scenarioResult.setResult(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return scenarioResult;
	}

	private int computeElementsReponse(List<Activity> activities) {
		// We should define how we measure changes -> work hours, complexity, number of
		// changes, etc.
		int numberOfChanges = 0;
		for (Activity activity : activities) {
			EObject element = activity.getElement();
			ActivityElementType type = activity.getElementType();
			// Count only basic components and interfaces, the other elements are not
			// relevant
			if (type.equals(ActivityElementType.BASICCOMPONENT) || type.equals(ActivityElementType.BASICCOMPONENT)) {
				numberOfChanges++;
			}
			if (!activity.getSubactivities().isEmpty())
				numberOfChanges += computeElementsReponse(activity.getSubactivities());
			if (!activity.getFollowupActivities().isEmpty())
				numberOfChanges += computeElementsReponse(activity.getFollowupActivities());
		}
		return numberOfChanges;
	}

	private float computeComplexityReponse(List<Activity> activities) {
		// We compute the complexity of a component based on the number of operations
		// io.github.squat_team.util.KAMPHelper.printActivities(activities, null);
		float complexityResponse = 0;
		Set<BasicComponent> affectedComponents = new HashSet<BasicComponent>();
		for (Activity activity : activities) {
			if (activity.getElementType() == ActivityElementType.BASICCOMPONENT) {
				BasicComponent component = (BasicComponent) activity.getElement();
				affectedComponents.add(component);
				// System.out.println(component.getEntityName());
				float componentComplexity = getComplexityForComponent(component);
				if (componentIsMappedInScenario(component))
					complexityResponse = (float) (complexityResponse + Math.pow(componentComplexity, 2));
				else
					complexityResponse = (float) (complexityResponse + Math.pow(componentComplexity, 2) / 2);
			} else {
				// System.out.println(activity.getElementType().name());
				// System.out.println("Implement this brach please");
			}

		}
		// System.out.println("Affected components: " + affectedComponents.size());
		// BasicComponent basicComponent = (BasicComponent)
		// ArchitectureModelLookup.lookUpComponentByName(changedAV,
		// componentName);
		return complexityResponse;
	}

	private boolean componentIsMappedInScenario(BasicComponent component) {
		ModifiabilityPCMScenario modifiabilityScenario = (ModifiabilityPCMScenario) scenario;
		for (ModifiabilityInstruction change : modifiabilityScenario.getChanges()) {
			switch (change.element) {
			case COMPONENT:
				String componentName = change.parameters.get("name");
				BasicComponent basicComponent = ArchitectureModelFactoryFacade.createBasicComponent(changedAV,
						componentName);
				if (basicComponent == component)
					return true;
				break;
			case INTERFACE:
				String interfaceName = change.parameters.get("name");
				OperationInterface operationInterface = ArchitectureModelFactoryFacade.createInterface(changedAV,
						interfaceName);
				// I'm not sure what we'll do when we map an interface instead of a component
				break;
			}
		}
		return false;
	}

	public int getComplexityForComponent(BasicComponent component) {
		// The complexity of the component is based on the kind of SEFFs that it
		// containts
		// System.out.println(component.getEntityName());
		// int operations = 0;
		int activitiesValue = 0;
		if (component != null) {
			EList<ServiceEffectSpecification> seffs = component.getServiceEffectSpecifications__BasicComponent();
			for (Iterator<ServiceEffectSpecification> iterator = seffs.iterator(); iterator.hasNext();) {
				ServiceEffectSpecification serviceEffectSpecification = (ServiceEffectSpecification) iterator.next();
				if (serviceEffectSpecification instanceof ResourceDemandingSEFFImpl) {
					ResourceDemandingSEFFImpl resourceSEFF = (ResourceDemandingSEFFImpl) serviceEffectSpecification;
					EList<AbstractAction> steps = resourceSEFF.getSteps_Behaviour();
					activitiesValue = activitiesValue + calculateComplexityForSteps(steps);
					// System.out.println(steps.size());
				} else {
					System.out.println(
							"ERROR: Without implementation. Please Implement for ServiceåEffectSpecificationImpl");
				}
			}
		}
		/*
		 * EList roles=component.getProvidedRoles_InterfaceProvidingEntity(); for
		 * (Iterator iterator = roles.iterator(); iterator.hasNext();) {
		 * OperationProvidedRole role = (OperationProvidedRole) iterator.next();
		 * for(OperationSignature signature :
		 * role.getProvidedInterface__OperationProvidedRole().
		 * getSignatures__OperationInterface()){
		 * System.out.println(signature.getEntityName()); } operations=operations+
		 * role.getProvidedInterface__OperationProvidedRole().
		 * getSignatures__OperationInterface().size(); } return operations;
		 */
		return activitiesValue;
	}

	private int calculateComplexityForSteps(EList<AbstractAction> steps) {
		int activitiesValue = 0;
		for (Iterator<AbstractAction> it = steps.iterator(); it.hasNext();) {
			AbstractAction abstractAction = (AbstractAction) it.next();
			switch (abstractAction.eClass().getClassifierID()) {
			case SeffPackage.STOP_ACTION:
				activitiesValue = activitiesValue + 0; // The value of this activity is 0 because it is not complex
				break;
			case SeffPackage.RESOURCE_DEMANDING_BEHAVIOUR:
				break;
			case SeffPackage.BRANCH_ACTION:
				activitiesValue = activitiesValue + 2;
				BranchAction action = (BranchAction) abstractAction;
				for (AbstractBranchTransition branch : action.getBranches_Branch()) {
					ResourceDemandingBehaviour rdb = branch.getBranchBehaviour_BranchTransition();
					activitiesValue = activitiesValue + calculateComplexityForSteps(rdb.getSteps_Behaviour());
				}
				break;
			case SeffPackage.START_ACTION:
				activitiesValue = activitiesValue + 0; // The value of this activity is 0 because it is not complex
				break;
			case SeffPackage.RESOURCE_DEMANDING_SEFF:
				break;
			case SeffPackage.RESOURCE_DEMANDING_INTERNAL_BEHAVIOUR:
				break;
			case SeffPackage.RELEASE_ACTION:
				break;
			case SeffPackage.LOOP_ACTION:
				activitiesValue = activitiesValue + 2;
				LoopAction loopAction = (LoopAction) abstractAction;
				activitiesValue = activitiesValue
						+ calculateComplexityForSteps(loopAction.getBodyBehaviour_Loop().getSteps_Behaviour());
				break;
			case SeffPackage.FORK_ACTION:
				activitiesValue = activitiesValue + 2;
				break;
			case SeffPackage.FORKED_BEHAVIOUR:
				break;
			case SeffPackage.SYNCHRONISATION_POINT:
				break;
			case SeffPackage.EXTERNAL_CALL_ACTION:
				activitiesValue = activitiesValue + 2;
				break;
			case SeffPackage.CALL_RETURN_ACTION:
				activitiesValue = activitiesValue + 1;
				break;
			case SeffPackage.PROBABILISTIC_BRANCH_TRANSITION:
				break;
			case SeffPackage.ACQUIRE_ACTION:
				break;
			case SeffPackage.COLLECTION_ITERATOR_ACTION:
				break;
			case SeffPackage.GUARDED_BRANCH_TRANSITION:
				break;
			case SeffPackage.SET_VARIABLE_ACTION:
				activitiesValue = activitiesValue + 1;
				break;
			case SeffPackage.INTERNAL_CALL_ACTION:
				activitiesValue = activitiesValue + 1;
				break;
			case SeffPackage.EMIT_EVENT_ACTION:
				break;
			case SeffPackage.INTERNAL_ACTION:
				activitiesValue = activitiesValue + 1;
				break;
			default:
				throw new IllegalArgumentException(
						"The class '" + abstractAction.eClass().getName() + "' is not a valid classifier");
			}

		}
		return activitiesValue;
	}

	private void setupChangedModel() throws Exception {
		if (scenario == null)
			throw new Exception("Forgot to set scenario");
		if (!(scenario instanceof ModifiabilityPCMScenario))
			throw new Exception("Wrong scenario for a modifiability bot");
		ModifiabilityPCMScenario modifiabilityScenario = (ModifiabilityPCMScenario) scenario;
		for (ModifiabilityInstruction instruction : modifiabilityScenario.getChanges())
			this.handleOperation(instruction);
	}

	// It would be nice to use fabian's fluent interface here...anyways, there are
	// things that need KAMP special models...internal modification marks and others
	private void handleOperation(ModifiabilityInstruction instruction) {
		switch (instruction.operation) {
		case CREATE:
			this.handleCreateElement(instruction);
			break;
		case DELETE:
			this.handleDeleteElement(instruction);
			break;
		case MODIFY:
			this.handleModifyElement(instruction);
			break;
		default:
			break;
		}
	}

	private void handleCreateElement(ModifiabilityInstruction instruction) {
		String interfaceName;
		OperationInterface operationInterface;
		String componentName;
		BasicComponent basicComponent;
		switch (instruction.element) {
		case ASSEMBLYCONNECTOR:
			break;
		case ASSEMBLYCONTEXT:
			break;
		case COMPONENT:
			componentName = instruction.parameters.get("name");
			basicComponent = ArchitectureModelFactoryFacade.createBasicComponent(changedAV, componentName);
			break;
		case DATATYPE:
			break;
		case INTERFACE:
			interfaceName = instruction.parameters.get("name");
			operationInterface = ArchitectureModelFactoryFacade.createInterface(changedAV, interfaceName);
			break;
		case OPERATION:
			interfaceName = instruction.parameters.get("iname");
			String operationName = instruction.parameters.get("oname");
			operationInterface = (OperationInterface) ArchitectureModelLookup.lookUpInterfaceByName(changedAV,
					interfaceName);
			OperationSignature operationSignature = ArchitectureModelFactoryFacade
					.createSignatureForInterface(operationInterface, operationName);
			break;
		case PARAMETER:
			break;
		case PROVIDEDROLE:
			componentName = instruction.parameters.get("cname");
			basicComponent = (BasicComponent) ArchitectureModelLookup.lookUpComponentByName(changedAV, componentName);
			interfaceName = instruction.parameters.get("iname");
			operationInterface = (OperationInterface) ArchitectureModelLookup.lookUpInterfaceByName(changedAV,
					interfaceName);
			ArchitectureModelFactoryFacade.createProvidedRole(basicComponent, operationInterface);
			break;
		case REQUIREDROLE:
			componentName = instruction.parameters.get("cname");
			basicComponent = (BasicComponent) ArchitectureModelLookup.lookUpComponentByName(changedAV, componentName);
			interfaceName = instruction.parameters.get("iname");
			operationInterface = (OperationInterface) ArchitectureModelLookup.lookUpInterfaceByName(changedAV,
					interfaceName);
			ArchitectureModelFactoryFacade.createRequiredRole(basicComponent, operationInterface);
			break;
		case SIGNATURE:
			break;
		default:
			break;
		}
	}

	private void handleDeleteElement(ModifiabilityInstruction instruction) {
		switch (instruction.element) {
		case ASSEMBLYCONNECTOR:
			break;
		case ASSEMBLYCONTEXT:
			break;
		case COMPONENT:
			String componentName = instruction.parameters.get("name");
			RepositoryComponent basicComponent = (RepositoryComponent) ArchitectureModelLookup
					.lookUpComponentByName(changedAV, componentName);
			ArchitectureModelFactoryFacade.deleteComponentConnector(basicComponent);
			break;
		case DATATYPE:
			break;
		case INTERFACE:
			String interfaceName = instruction.parameters.get("name");
			OperationInterface operationInterface = (OperationInterface) ArchitectureModelLookup
					.lookUpInterfaceByName(changedAV, interfaceName);
			// Not implemented in ArchitectureModelFactoryFacade
			EcoreUtil.delete(operationInterface);
			break;
		case OPERATION:
			break;
		case PARAMETER:
			break;
		case PROVIDEDROLE:
			break;
		case REQUIREDROLE:
			break;
		case SIGNATURE:
			break;
		default:
			break;
		}
	}

	private void handleModifyElement(ModifiabilityInstruction instruction) {
		switch (instruction.element) {
		case ASSEMBLYCONNECTOR:
			break;
		case ASSEMBLYCONTEXT:
			break;
		case COMPONENT:
			String componentName = instruction.parameters.get("name");
			RepositoryComponent basicComponent = (RepositoryComponent) ArchitectureModelLookup
					.lookUpComponentByName(changedAV, componentName);
			ArchitectureModelFactoryFacade.assignInternalModificationMarkToComponent(changedAV, basicComponent);
			break;
		case DATATYPE:
			break;
		case INTERFACE:
			String interfaceName = instruction.parameters.get("name");
			OperationInterface operationInterface = (OperationInterface) ArchitectureModelLookup
					.lookUpInterfaceByName(changedAV, interfaceName);
			ArchitectureModelFactoryFacade.assignInternalModificationMarkToInterface(changedAV, operationInterface);
			break;
		case OPERATION:
			break;
		case PARAMETER:
			break;
		case PROVIDEDROLE:
			break;
		case REQUIREDROLE:
			break;
		case SIGNATURE:
			break;
		default:
			break;
		}
	}

	@Override
	public List<PCMScenarioResult> searchForAlternatives(PCMArchitectureInstance currentArchitecture) {
		return null;
	}
}

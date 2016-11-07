package io.github.squat_team.modifiability.kamp;

import java.util.List;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.OperationInterface;

import edu.kit.ipd.sdq.kamp.core.Activity;
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

@SuppressWarnings("unused")
public class KAMPPCMBot extends AbstractPCMBot {
	protected ArchitectureVersion baseAV;
	protected ArchitectureVersion changedAV;
	
	public KAMPPCMBot(PCMScenario scenario) {
		super(scenario);
	}

	@Override
	public PCMScenarioResult analyze(PCMArchitectureInstance currentArchitecture) {
		//Run the propagation and create the result
		PCMScenarioResult scenarioResult = new PCMScenarioResult(this);
		//Register the initial architecture
		//There is no tactic, because this is just an evaluation
		scenarioResult.setAppliedTactic(null);
		//The resulting architecture is just the original one, because we did not make changes
		scenarioResult.setResultingArchitecture(currentArchitecture);
		try {
			//First convert the base model
			ArchitectureVersionWrapper baseWrapper = new ArchitectureVersionWrapper(currentArchitecture);
			baseAV = baseWrapper.transformToKAMP();
			//Pessimistic approach for CIA - This could be configured from outside...
			ArchitectureModelFactoryFacade.setupComponentInternalDependenciesPessimistic(baseAV);
			
			//Second create the changed model
			ArchitectureVersionWrapper changedWrapper = new ArchitectureVersionWrapper(currentArchitecture);
			changedAV = changedWrapper.transformToKAMP();
			this.setupChangedModel();
			//Pessimistic approach for CIA - This could be configured from outside...
			ArchitectureModelFactoryFacade.setupComponentInternalDependenciesPessimistic(changedAV);
			
			//Run Change Impact Analyses
			List<Activity> activities = KAMPHeadlessRunner.runAnalysis(baseAV, changedAV);
			//Create the result
			int changes = this.computeReponse(activities);
			PCMResult result = new PCMResult(ResponseMeasureType.NUMERIC);
			result.setResponse(new Integer(changes));
			scenarioResult.setResult(result);	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return scenarioResult;
	}
	
	private int computeReponse(List<Activity> activities) {
		//We should define how we measure changes -> work hours, complexity, number of changes, etc.
		int numberOfChanges = 0;
		for (Activity activity : activities) {
			activity.getElement();
			activity.getElementType();
			numberOfChanges++;
			if (!activity.getSubactivities().isEmpty())
				numberOfChanges += computeReponse(activity.getSubactivities());
			if (!activity.getFollowupActivities().isEmpty())
				numberOfChanges += computeReponse(activity.getFollowupActivities());
		}
		return numberOfChanges;
	}
	
	private void setupChangedModel() throws Exception {
		if(scenario == null)
			throw new Exception("Forgot to set scenario");
		if(!(scenario instanceof ModifiabilityPCMScenario))
			throw new Exception("Wrong scenario for a modifiability bot");
		ModifiabilityPCMScenario modifiabilityScenario = (ModifiabilityPCMScenario) scenario;
		for(ModifiabilityInstruction instruction : modifiabilityScenario.getChanges())
			this.handleOperation(instruction);
	}
	
	//It would be nice to use fabian's fluent interface here...anyways, there are things that need KAMP special models...internal modification marks and others
	private void handleOperation(ModifiabilityInstruction instruction) {
		switch(instruction.operation) {
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
		switch(instruction.element) {
		case ASSEMBLYCONNECTOR:
			break;
		case ASSEMBLYCONTEXT:
			break;
		case COMPONENT:
			String componentName = instruction.parameters.get("name");
			BasicComponent basicComponent = ArchitectureModelFactoryFacade.createBasicComponent(changedAV, componentName);
			break;
		case DATATYPE:
			break;
		case INTERFACE:
			String interfaceName = instruction.parameters.get("name");
			OperationInterface operationInterface = ArchitectureModelFactoryFacade.createInterface(changedAV, interfaceName);
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
	
	private void handleDeleteElement(ModifiabilityInstruction instruction) {
		switch(instruction.element) {
		case ASSEMBLYCONNECTOR:
			break;
		case ASSEMBLYCONTEXT:
			break;
		case COMPONENT:
			String componentName = instruction.parameters.get("name");
			BasicComponent basicComponent = 
					(BasicComponent) ArchitectureModelLookup.lookUpComponentByName(changedAV, componentName);
			ArchitectureModelFactoryFacade.deleteComponentConnector(basicComponent);
			break;
		case DATATYPE:
			break;
		case INTERFACE:
			String interfaceName = instruction.parameters.get("name");
			OperationInterface operationInterface = 
					(OperationInterface) ArchitectureModelLookup.lookUpInterfaceByName(changedAV, interfaceName);
			//Not implemented in ArchitectureModelFactoryFacade
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
		switch(instruction.element) {
		case ASSEMBLYCONNECTOR:
			break;
		case ASSEMBLYCONTEXT:
			break;
		case COMPONENT:
			String componentName = instruction.parameters.get("name");
			BasicComponent basicComponent = 
					(BasicComponent) ArchitectureModelLookup.lookUpComponentByName(changedAV, componentName);
			ArchitectureModelFactoryFacade.assignInternalModificationMarkToComponent(changedAV, basicComponent);
			break;
		case DATATYPE:
			break;
		case INTERFACE:
			String interfaceName = instruction.parameters.get("name");
			OperationInterface operationInterface = 
					(OperationInterface) ArchitectureModelLookup.lookUpInterfaceByName(changedAV, interfaceName);
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

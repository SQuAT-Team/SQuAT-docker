package edu.squat.transformations.modifiability.wrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.henshin.interpreter.EGraph;
import org.eclipse.emf.henshin.interpreter.Engine;
import org.eclipse.emf.henshin.interpreter.RuleApplication;
import org.eclipse.emf.henshin.interpreter.impl.EGraphImpl;
import org.eclipse.emf.henshin.interpreter.impl.EngineImpl;
import org.eclipse.emf.henshin.interpreter.impl.RuleApplicationImpl;
import org.eclipse.emf.henshin.model.Rule;
import org.eclipse.emf.henshin.model.resource.HenshinResourceSet;
import org.eclipse.emf.henshin.trace.Trace;
import org.eclipse.emf.henshin.trace.TraceFactory;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.allocation.AllocationFactory;
import org.palladiosimulator.pcm.core.CoreFactory;
import org.palladiosimulator.pcm.core.PCMRandomVariable;
import org.palladiosimulator.pcm.core.composition.AssemblyConnector;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.CompositionFactory;
import org.palladiosimulator.pcm.core.composition.Connector;
import org.palladiosimulator.pcm.core.composition.ProvidedDelegationConnector;
import org.palladiosimulator.pcm.core.entity.EntityFactory;
import org.palladiosimulator.pcm.core.entity.ResourceProvidedRole;
import org.palladiosimulator.pcm.parameter.ParameterFactory;
import org.palladiosimulator.pcm.parameter.VariableCharacterisation;
import org.palladiosimulator.pcm.parameter.VariableCharacterisationType;
import org.palladiosimulator.pcm.parameter.VariableUsage;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.OperationProvidedRole;
import org.palladiosimulator.pcm.repository.OperationRequiredRole;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.repository.Parameter;
import org.palladiosimulator.pcm.repository.PrimitiveDataType;
import org.palladiosimulator.pcm.repository.ProvidedRole;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryComponent;
import org.palladiosimulator.pcm.repository.RepositoryFactory;
import org.palladiosimulator.pcm.repository.RequiredRole;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourcetype.ProcessingResourceType;
import org.palladiosimulator.pcm.resourcetype.ResourceType;
import org.palladiosimulator.pcm.resourcetype.ResourcetypeFactory;
import org.palladiosimulator.pcm.seff.AbstractAction;
import org.palladiosimulator.pcm.seff.AbstractBranchTransition;
import org.palladiosimulator.pcm.seff.BranchAction;
import org.palladiosimulator.pcm.seff.ExternalCallAction;
import org.palladiosimulator.pcm.seff.InternalAction;
import org.palladiosimulator.pcm.seff.InternalCallAction;
import org.palladiosimulator.pcm.seff.LoopAction;
import org.palladiosimulator.pcm.seff.ResourceDemandingInternalBehaviour;
import org.palladiosimulator.pcm.seff.ResourceDemandingSEFF;
import org.palladiosimulator.pcm.seff.SeffFactory;
import org.palladiosimulator.pcm.seff.ServiceEffectSpecification;
import org.palladiosimulator.pcm.seff.SetVariableAction;
import org.palladiosimulator.pcm.seff.StartAction;
import org.palladiosimulator.pcm.seff.StopAction;
import org.palladiosimulator.pcm.seff.seff_performance.ParametricResourceDemand;
import org.palladiosimulator.pcm.seff.seff_performance.SeffPerformanceFactory;
import org.palladiosimulator.pcm.usagemodel.AbstractUserAction;
import org.palladiosimulator.pcm.usagemodel.Branch;
import org.palladiosimulator.pcm.usagemodel.BranchTransition;
import org.palladiosimulator.pcm.usagemodel.EntryLevelSystemCall;
import org.palladiosimulator.pcm.usagemodel.Loop;
import org.palladiosimulator.pcm.usagemodel.ScenarioBehaviour;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.palladiosimulator.pcm.usagemodel.UsageScenario;

import de.uka.ipd.sdq.stoex.AbstractNamedReference;
import de.uka.ipd.sdq.stoex.NamespaceReference;
import de.uka.ipd.sdq.stoex.StoexFactory;
import de.uka.ipd.sdq.stoex.VariableReference;
import edu.squat.pcm.PCMDefault;
import edu.squat.transformations.ArchitecturalVersion;
import edu.squat.transformations.modifiability.PCMTransformerRunner;
import edu.squat.transformations.modifiability.RunnerHelper;
import edu.squat.transformations.modifiability.Tactic;

public class WrapperRunner extends PCMTransformerRunner {
	private final static String TRACE_Wrapped = "wrap";
	private final static String TRACE_Affected = "affected";
	@SuppressWarnings("unused")
	private final static String TRACE_Wrapper = "wrapper";
	private final static String TRACE_Connection = "connection";
	private final static String TRACE_AssemblyContext = "assembly-cont";
	private final static String TRACE_AssemblyConnector = "assembly-conn";
	private final static String TRACE_AllocationContext = "allocation-cont";
	//
	private Engine engine;
	//
	private Rule createInitialTrace;
	private Rule markComponents2Wrap;
	private Rule createWrapper;
	private Rule reconnectComponents2Wrapper;
	
	public WrapperRunner() {
		super();
	}

	
	@Override
	public void loadRules() {
		createInitialTrace = (Rule) module.getUnit("createInitialTrace");
		//createInitialTrace.setCheckDangling(false);
		markComponents2Wrap = (Rule) module.getUnit("markComponents2Wrap");
		createWrapper = (Rule) module.getUnit("createWrapper");
		//createWrapper.setCheckDangling(false);
		reconnectComponents2Wrapper = (Rule) module.getUnit("reconnectComponents2Wrapper");
	}
	
	@Override
	public List<ArchitecturalVersion> run(boolean saveResult) {
		List<ArchitecturalVersion> ret= new ArrayList<>();
		
		candidateTactics = new ArrayList<Tactic>();
		//Create and configure an engine
		engine = new EngineImpl();
		//engine.getOptions().put(engine.OPTION_CHECK_DANGLING, false);
		EGraph tempGraph1 = graph.copy(null);
		//Run the first and second rule to create the initial traces
		RuleApplication firstRule = this.runFirstRule(tempGraph1);
		boolean successFirstRule = firstRule.execute(monitor);
		RuleApplication secondRule = this.runSecondRule(tempGraph1);
		boolean successSecondRule = secondRule.execute(monitor);
		
		//If the transformation was successful continue with the other rules
		if(successFirstRule && successSecondRule) {
			System.out.println("Successfully marked components and interfaces that could be wrapped");
			
			Trace root = RunnerHelper.getTraceRoot(tempGraph1);
			
			List<Trace> candidateTraces = this.computeCandidates(root);
			//Avoiding references to the graph because we are going to clone it...we need emf-less references
			List<Integer> candidatePointers = this.transformToPointers(root, candidateTraces);
			
			int counter = 0;
			while(counter < candidatePointers.size()) {
				//Cloning the graph
				
				EGraph tempGraph = tempGraph1.copy(null);
				Trace tempRoot = RunnerHelper.getTraceRoot(tempGraph);
				//Repository tempRepository = RunnerHelper.getRepositoryRoot(tempGraph);
				//Getting the interfaces
				Trace tempCandidateTrace = this.convertToCandidate(tempRoot, candidatePointers.get(counter));
				//Removing all but the chain
				tempRoot.getSubTraces().clear();
				tempRoot.getSubTraces().add(tempCandidateTrace);
			
				//Configuring and executing the third rule
				RuleApplication thirdRule = this.runThirdRule(tempGraph);
			
				//Duplicating the interface for the intermediate
				OperationInterface oldInterface = (OperationInterface) thirdRule.getResultParameterValue("iWrap");	
				BasicComponent newComponent = (BasicComponent) thirdRule.getResultParameterValue("cWrapper");
				OperationInterface newInterface = (OperationInterface) thirdRule.getResultParameterValue("iWrapper");		
				this.duplicateInterface(oldInterface, newInterface);
				
				//Save the name for the files
				String interfaceName = oldInterface.getEntityName();
				String fileName = interfaceName;
				
				//Creating a service effect specification for the intermediate
				this.createSEFF(tempGraph, oldInterface, newComponent, newInterface);
			
				//Configuring and executing the fourth rule
				this.runFourthRule(tempGraph);
				
				//Fix affected components SEFFs
				this.fixAffectedSEFFs(tempGraph, oldInterface, newComponent, newInterface);
				
				if(this.arePerformanceModelsLoaded()) {
					//Adjust the system and allocation models by executing a monolithic method (ugly but fast)
					this.fixSystemAndAllocation(tempGraph, oldInterface, newComponent, newInterface);
					//Adjust the usage model
					this.fixUsageModel(tempGraph, oldInterface, newComponent, newInterface);
				}

				//Clean up and delete old assembly connectors
				this.runLastRule(tempGraph);
				
				//Store the results
				this.addTactic(null, tempGraph, null);
				if (saveResult) {
					String nameOfTheModel=resultRepositoryFilename.replaceAll(".repository", "").replace("#REPLACEMENT#", String.valueOf(counter) + "-" + fileName);
					
					ret.add(new ArchitecturalVersion(nameOfTheModel, dirPath,ArchitecturalVersion.MODIFIABILITY));
					RunnerHelper.saveRepositoryResult(
							resourceSet, 
							tempGraph, 
							resultRepositoryFilename.replace("#REPLACEMENT#", String.valueOf(counter) + "-" + fileName));
					if(this.arePerformanceModelsLoaded()) {
						RunnerHelper.saveSystemResult(
								resourceSet, 
								tempGraph, 
								resultSystemFilename.replace("#REPLACEMENT#", String.valueOf(counter) + "-" + fileName));
						RunnerHelper.saveResourceEnvironmentResult(
								resourceSet, 
								tempGraph, 
								resultResourceEnvironmentFilename.replace("#REPLACEMENT#", String.valueOf(counter) + "-" + fileName));
						RunnerHelper.saveAllocationResult(
								resourceSet, 
								tempGraph, 
								resultAllocationFilename.replace("#REPLACEMENT#", String.valueOf(counter) + "-" + fileName));
					}
					if(this.isUsageModelLoaded()) {
						RunnerHelper.saveUsageResult(
								resourceSet, 
								tempGraph, 
								resultUsageFilename.replace("#REPLACEMENT#", String.valueOf(counter) + "-" + fileName));
					}
				}
				
				counter++;
			}
		}
		else {
			System.out.println("Could not mark components and interfaces that could use a wrapper");
		}
		
		return ret;
	}
	

	private void fixAffectedSEFFs(EGraph tempGraph, OperationInterface oldInterface, BasicComponent newComponent, OperationInterface newInterface) {
		Trace root = RunnerHelper.getTraceRoot(tempGraph);
		Trace wrapTrace = RunnerHelper.getTraces(root, TRACE_Wrapped, false).get(0);
		List<Trace> affectedTraces = RunnerHelper.getTraces(wrapTrace, TRACE_Affected, false);
		for(Trace affectedTrace : affectedTraces) {
			BasicComponent affectedComponent = (BasicComponent) affectedTrace.getSource().get(0);
			List<Trace> connectionTraces = RunnerHelper.getTraces(affectedTrace, TRACE_Connection, false);
			for(Trace connectionTrace : connectionTraces) {
				OperationRequiredRole oldRequiredRole = (OperationRequiredRole) connectionTrace.getSource().get(0);
				OperationRequiredRole newRequiredRole = (OperationRequiredRole) connectionTrace.getTarget().get(0);
				this.fixAffectedSEFF(affectedComponent, oldInterface, newInterface, oldRequiredRole, newRequiredRole);
			}
		}
	}
	
	private void fixAffectedSEFF(BasicComponent affectedComponent, OperationInterface oldInterface, OperationInterface newInterface, OperationRequiredRole oldRequiredRole, OperationRequiredRole newRequiredRole) {
		Iterator<ServiceEffectSpecification> seffIterator = affectedComponent.getServiceEffectSpecifications__BasicComponent().iterator();
    	while(seffIterator.hasNext()) {
    		ResourceDemandingSEFF seff = (ResourceDemandingSEFF) seffIterator.next();
    		this.fixAffectedSEFF(seff.getSteps_Behaviour(), oldInterface, newInterface, oldRequiredRole, newRequiredRole);
		}
	}
	
	private void fixAffectedSEFF(List<AbstractAction> actions, OperationInterface oldInterface, OperationInterface newInterface, OperationRequiredRole oldRequiredRole, OperationRequiredRole newRequiredRole) {
    	Iterator<AbstractAction> actionIterator = actions.iterator();
    	while(actionIterator.hasNext()) {
    		AbstractAction action = actionIterator.next();
			if(action instanceof BranchAction) {
		    	BranchAction branchAction = (BranchAction) action;
		    	Iterator<AbstractBranchTransition> branchTransitions = branchAction.getBranches_Branch().iterator();
		    	while(branchTransitions.hasNext()) {
		    		AbstractBranchTransition branchTransition = branchTransitions.next();
		    		this.fixAffectedSEFF(branchTransition.getBranchBehaviour_BranchTransition().getSteps_Behaviour(), oldInterface, newInterface, oldRequiredRole, newRequiredRole);
		    	}
			}
			if(action instanceof LoopAction) {
				LoopAction loopAction = (LoopAction) action;
				this.fixAffectedSEFF(loopAction.getBodyBehaviour_Loop().getSteps_Behaviour(), oldInterface, newInterface, oldRequiredRole, newRequiredRole);
			}
			if(action instanceof ExternalCallAction) {
				ExternalCallAction externalCallAction = (ExternalCallAction) action;
				OperationRequiredRole requiredRole = externalCallAction.getRole_ExternalService();
				OperationSignature requiredSignature = externalCallAction.getCalledService_ExternalService();
				OperationInterface requiredInterface = requiredRole.getRequiredInterface__OperationRequiredRole();
				if(requiredRole.equals(oldRequiredRole) && requiredInterface.equals(oldInterface)) {
					externalCallAction.setRole_ExternalService(newRequiredRole);
					externalCallAction.setCalledService_ExternalService(RunnerHelper.findSignature(requiredSignature, newInterface));
				}
			}
		}
	}

	private List<Trace> computeCandidates(Trace root) {
		List<Trace> candidates = new ArrayList<Trace>();
		List<Trace> wraps = RunnerHelper.getTraces(root, TRACE_Wrapped, false);
		for(Trace wrap : wraps)
			//if(this.isCandidate(wrap))
				candidates.add(wrap);
		return candidates;
	}
	
	private boolean isCandidate(Trace wrap) {
		List<Trace> affectedComponents = RunnerHelper.getTraces(wrap, TRACE_Affected, false);
		if(affectedComponents.size() > 1)
			return true;
		else
			return false;
	}
	
	private List<Integer> transformToPointers(Trace root, List<Trace> candidateTraces) {
		List<Integer> pointers = new ArrayList<Integer>();
		List<Trace> wraps = RunnerHelper.getTraces(root, TRACE_Wrapped, false);
		for(Trace candidate : candidateTraces) {
			int pointer = wraps.indexOf(candidate);
			pointers.add(pointer);
		}
		return pointers;
	}

	private Trace convertToCandidate(Trace tempRoot, Integer index) {
		List<Trace> wraps = RunnerHelper.getTraces(tempRoot, TRACE_Wrapped, false);
		Trace candidate = wraps.get(index);
		return candidate;
	}

	private void fixSystemAndAllocation(EGraph graph, OperationInterface oldInterface, BasicComponent newComponent, OperationInterface newInterface) {
		Trace traceRoot = RunnerHelper.getTraceRoot(graph);
		Repository repositoryRoot = RunnerHelper.getRepositoryRoot(graph);
		org.palladiosimulator.pcm.system.System systemRoot = RunnerHelper.getSystemRoot(graph);
		
		//Create the assembly context
		AssemblyContext wrapperAssemblyContext = CompositionFactory.eINSTANCE.createAssemblyContext();
		wrapperAssemblyContext.setEncapsulatedComponent__AssemblyContext(newComponent);
		wrapperAssemblyContext.setEntityName(RunnerHelper.getAssemblyContextName(newComponent));
		systemRoot.getAssemblyContexts__ComposedStructure().add(wrapperAssemblyContext);
		Trace assemblyContextTrace = TraceFactory.eINSTANCE.createTrace();
		assemblyContextTrace.setName(TRACE_AssemblyContext);
		//assemblyContextTrace.getSource().add(null);
		assemblyContextTrace.getTarget().add((EObject) wrapperAssemblyContext);
		traceRoot.getSubTraces().add(assemblyContextTrace);
		
		//Find providing components
		List<BasicComponent> providingComponents = new ArrayList<BasicComponent>();
		for(RepositoryComponent component : repositoryRoot.getComponents__Repository()) {
			if(component instanceof BasicComponent) {
				BasicComponent basicComponent = (BasicComponent) component;
				for(ProvidedRole providedRole : basicComponent.getProvidedRoles_InterfaceProvidingEntity()) {
					if(providedRole instanceof OperationProvidedRole) {
						OperationProvidedRole operationProvidedRole = (OperationProvidedRole) providedRole;
						if(operationProvidedRole.getProvidedInterface__OperationProvidedRole().equals(oldInterface))
							providingComponents.add(basicComponent);
					}
				}
			}
		}
		
		//Find affected components
		List<BasicComponent> affectedComponents = new ArrayList<BasicComponent>();
		Trace wrapTrace = RunnerHelper.getTraces(traceRoot, TRACE_Wrapped, false).get(0);
		List<Trace> affectedTraces = RunnerHelper.getTraces(wrapTrace, TRACE_Affected, false);
		for(Trace affectedTrace : affectedTraces) {
			BasicComponent affectedComponent = (BasicComponent) affectedTrace.getSource().get(0);
			affectedComponents.add(affectedComponent);
		}
		
		//Reconnect the assembly connectors
		List<Connector> connectorsToAdd = new ArrayList<Connector>();
		Iterator<Connector> connectors = systemRoot.getConnectors__ComposedStructure().iterator();
		while(connectors.hasNext()) {
			Connector connector = connectors.next();
			if(connector instanceof ProvidedDelegationConnector) {
				//DO NOTHING
			}
			if(connector instanceof AssemblyConnector) {
				AssemblyConnector oldConnector = (AssemblyConnector) connector;
				AssemblyContext providingAssemblyContext = oldConnector.getProvidingAssemblyContext_AssemblyConnector();
				AssemblyContext requiringAssemblyContext = oldConnector.getRequiringAssemblyContext_AssemblyConnector();
				BasicComponent providingComponent = (BasicComponent) providingAssemblyContext.getEncapsulatedComponent__AssemblyContext();
				BasicComponent requiringComponent = (BasicComponent) requiringAssemblyContext.getEncapsulatedComponent__AssemblyContext();
				
				if(providingComponents.contains(providingComponent) && affectedComponents.contains(requiringComponent)) {
					AssemblyConnector wrapperConnector = CompositionFactory.eINSTANCE.createAssemblyConnector();
					wrapperConnector.setEntityName(RunnerHelper.getAssemblyConnectorName(requiringAssemblyContext, wrapperAssemblyContext));
					wrapperConnector.setProvidingAssemblyContext_AssemblyConnector(wrapperAssemblyContext);
					wrapperConnector.setRequiringAssemblyContext_AssemblyConnector(requiringAssemblyContext);
					OperationProvidedRole wrapperProvidedRole = RunnerHelper.findProvidedRole(newComponent, newInterface);
					OperationRequiredRole wrapperRequiredRole = RunnerHelper.findRequiredRole(requiringComponent, newInterface);
					wrapperConnector.setProvidedRole_AssemblyConnector(wrapperProvidedRole);
					wrapperConnector.setRequiredRole_AssemblyConnector(wrapperRequiredRole);
					connectorsToAdd.add(wrapperConnector);
					//
					Trace connectorTrace = TraceFactory.eINSTANCE.createTrace();
					connectorTrace.setName(TRACE_AssemblyConnector);
					connectorTrace.getSource().add((EObject) oldConnector);
					connectorTrace.getTarget().add((EObject) wrapperConnector);
					traceRoot.getSubTraces().add(connectorTrace);
				}
			}
		}
		//Create a new assembly connector between the wrapper and the wrapped component
		for(BasicComponent oldComponent : providingComponents) {
			List<AssemblyContext> wrapAssemblyContexts = RunnerHelper.findAssemblyContexts(oldComponent, systemRoot);
			for(AssemblyContext wrapAssemblyContext : wrapAssemblyContexts) {
				AssemblyConnector wrapConnector = CompositionFactory.eINSTANCE.createAssemblyConnector();
				wrapConnector.setEntityName(RunnerHelper.getAssemblyConnectorName(wrapperAssemblyContext, wrapAssemblyContext));
				wrapConnector.setProvidingAssemblyContext_AssemblyConnector(wrapAssemblyContext);
				wrapConnector.setRequiringAssemblyContext_AssemblyConnector(wrapperAssemblyContext);
				OperationProvidedRole wrapProvidedRole = RunnerHelper.findProvidedRole(oldComponent, oldInterface);
				OperationRequiredRole wrapRequiredRole = RunnerHelper.findRequiredRole(newComponent, oldInterface);
				wrapConnector.setProvidedRole_AssemblyConnector(wrapProvidedRole);
				wrapConnector.setRequiredRole_AssemblyConnector(wrapRequiredRole);
				connectorsToAdd.add(wrapConnector);
				//
				Trace connectorTrace = TraceFactory.eINSTANCE.createTrace();
				connectorTrace.setName(TRACE_AssemblyConnector);
				//connectorTrace.getSource().add(null);
				connectorTrace.getTarget().add((EObject) wrapConnector);
				traceRoot.getSubTraces().add(connectorTrace);
			}
		}
		
		//Update delegation connectors and provided role
		Iterator<Connector> delegationConnectors = systemRoot.getConnectors__ComposedStructure().iterator();
		while(delegationConnectors.hasNext()) {
			Connector connector = delegationConnectors.next();
			if(connector instanceof ProvidedDelegationConnector) {
				ProvidedDelegationConnector delegationConnector = (ProvidedDelegationConnector) connector;
				AssemblyContext oldProvidingAssemblyContext = delegationConnector.getAssemblyContext_ProvidedDelegationConnector();
				OperationProvidedRole oldInnerProvidedRole = delegationConnector.getInnerProvidedRole_ProvidedDelegationConnector();
				OperationProvidedRole outterProvidedRole = delegationConnector.getOuterProvidedRole_ProvidedDelegationConnector();
				for(BasicComponent oldComponent : providingComponents) {
					if(RunnerHelper.findProvidedRole(oldComponent, oldInterface).equals(oldInnerProvidedRole)) {
						delegationConnector.setAssemblyContext_ProvidedDelegationConnector(wrapperAssemblyContext);
						OperationProvidedRole newInnerProvidedRole = RunnerHelper.findProvidedRole(newComponent, newInterface);
						delegationConnector.setInnerProvidedRole_ProvidedDelegationConnector(newInnerProvidedRole);
						outterProvidedRole.setProvidedInterface__OperationProvidedRole(newInterface);
					}
				}
			}
		}
		
		//Add all the connectors to the system model
		systemRoot.getConnectors__ComposedStructure().addAll(connectorsToAdd);
		
		//Find the resources allocated to assembly contexts encapsulating the old component
		Allocation allocationRoot = RunnerHelper.getAllocationRoot(graph);
		Map<ResourceContainer, Integer> counter = new HashMap<ResourceContainer, Integer>();
		for(BasicComponent oldComponent : providingComponents) {
			List<AssemblyContext> assemblyContexts = RunnerHelper.findAssemblyContexts(oldComponent, systemRoot);
			for(AssemblyContext assemblyContext : assemblyContexts) {
				ResourceContainer resourceContainer = RunnerHelper.findAllocationResourceContainer(assemblyContext, allocationRoot);
				if(counter.containsKey(resourceContainer))
					counter.put(resourceContainer, counter.get(resourceContainer) + 1);
				else
					counter.put(resourceContainer, 1);
			}
		}
		//Allocate the assembly context to the resource with higher allocated resources
		ResourceContainer bestContainer = null;
		int maxAllocation = Integer.MIN_VALUE;
		for(ResourceContainer resourceContainer : counter.keySet()) {
			if(bestContainer == null || counter.get(resourceContainer) > maxAllocation) {
				bestContainer = resourceContainer;
				maxAllocation = counter.get(resourceContainer);
			}
		}
		AllocationContext wrapperAllocationContext = AllocationFactory.eINSTANCE.createAllocationContext();
		wrapperAllocationContext.setEntityName(RunnerHelper.getAllocationContextName(wrapperAssemblyContext));
		wrapperAllocationContext.setAssemblyContext_AllocationContext(wrapperAssemblyContext);
		wrapperAllocationContext.setResourceContainer_AllocationContext(bestContainer);
		allocationRoot.getAllocationContexts_Allocation().add(wrapperAllocationContext);
		//
		Trace allocationTrace = TraceFactory.eINSTANCE.createTrace();
		allocationTrace.setName(TRACE_AllocationContext);
		//allocationTrace.getSource().add(null);
		allocationTrace.getTarget().add((EObject) wrapperAllocationContext);
		traceRoot.getSubTraces().add(allocationTrace);
	}
	
	private void fixUsageModel(EGraph graph, OperationInterface oldInterface, BasicComponent newComponent, OperationInterface newInterface) {
		Trace traceRoot = RunnerHelper.getTraceRoot(graph);
		Repository repositoryRoot = RunnerHelper.getRepositoryRoot(graph);
		org.palladiosimulator.pcm.system.System systemRoot = RunnerHelper.getSystemRoot(graph);
		UsageModel usageRoot = RunnerHelper.getUsageRoot(graph);
		
		//Find entry points recursively
		for(UsageScenario usageScenario : usageRoot.getUsageScenario_UsageModel()) {
			ScenarioBehaviour scenarioBehavior = usageScenario.getScenarioBehaviour_UsageScenario();
			EList<AbstractUserAction> actions = scenarioBehavior.getActions_ScenarioBehaviour();
			this.fixUsageModel(graph, oldInterface, newComponent, newInterface, actions);
		}
	}
	
	private void fixUsageModel(EGraph graph, OperationInterface oldInterface, BasicComponent newComponent, OperationInterface newInterface, EList<AbstractUserAction> actions) {
		for(AbstractUserAction action : actions)
			this.fixUsageModel(graph, oldInterface, newComponent, newInterface, action);
	}
	
	private void fixUsageModel(EGraph graph, OperationInterface oldInterface, BasicComponent newComponent, OperationInterface newInterface, AbstractUserAction action) {
		if(action instanceof Branch) {
			Branch branch = (Branch) action;
			for(BranchTransition branchTransition : branch.getBranchTransitions_Branch()) {
				ScenarioBehaviour branchedBehavior = branchTransition.getBranchedBehaviour_BranchTransition();
				this.fixUsageModel(graph, oldInterface, newComponent, newInterface, branchedBehavior.getActions_ScenarioBehaviour());
			}
		}
		if(action instanceof Loop) {
			Loop loop = (Loop) action;
			ScenarioBehaviour bodyBehavior = loop.getBodyBehaviour_Loop();
			this.fixUsageModel(graph, oldInterface, newComponent, newInterface, bodyBehavior.getActions_ScenarioBehaviour());
		}
		if(action instanceof EntryLevelSystemCall) {
			EntryLevelSystemCall call = (EntryLevelSystemCall) action;
			//The provided role is updated, no need to change it
			OperationProvidedRole providedRole = call.getProvidedRole_EntryLevelSystemCall();
			//Modify the signature of the call
			if(providedRole.getProvidedInterface__OperationProvidedRole().equals(newInterface)) {
				OperationSignature oldSignature = call.getOperationSignature__EntryLevelSystemCall();
				OperationSignature newSignature = RunnerHelper.findSignature(oldSignature, newInterface);
				call.setOperationSignature__EntryLevelSystemCall(newSignature);
			}
		}
	}

	private RuleApplication runFirstRule(EGraph graph) {
		RuleApplication app = new RuleApplicationImpl(engine);
		app.setEGraph(graph);
		app.setRule(createInitialTrace);
		return app;
	}

	private RuleApplication runSecondRule(EGraph graph) {
		RuleApplication app = new RuleApplicationImpl(engine);
		app.setEGraph(graph);
		app.setRule(markComponents2Wrap);
		return app;
	}

	private RuleApplication runThirdRule(EGraph graph) {
		RuleApplication app = new RuleApplicationImpl(engine);
		app.setEGraph(graph);
		app.setRule(createWrapper);
		boolean success = app.execute(monitor);
		if(success)
			System.out.println("Successfully created the wrapper component and interface");
		else
			System.out.println("Could not create the wrapper component and interface");
		return app;
	}

	private void duplicateInterface(OperationInterface oldInterface, OperationInterface newInterface) {
		RepositoryFactory factory = RepositoryFactory.eINSTANCE;
		for(OperationSignature signature : oldInterface.getSignatures__OperationInterface()) {
			OperationSignature clonedSignature = factory.createOperationSignature();
			clonedSignature.setEntityName(signature.getEntityName());
			clonedSignature.setReturnType__OperationSignature(signature.getReturnType__OperationSignature());
			for(Parameter parameter : signature.getParameters__OperationSignature()) {
				Parameter clonedParameter = factory.createParameter();
				clonedParameter.setParameterName(parameter.getParameterName());
				clonedParameter.setDataType__Parameter(parameter.getDataType__Parameter());
				clonedParameter.setEventType__Parameter(parameter.getEventType__Parameter());
				clonedSignature.getParameters__OperationSignature().add(clonedParameter);
			}
			newInterface.getSignatures__OperationInterface().add(clonedSignature);
		}
		System.out.println("Successfully duplicated the interface of the wrapped component into the wrapper");
	}

	private void createSEFF(EGraph graph, OperationInterface oldInterface, BasicComponent newComponent, OperationInterface newInterface) {
		SeffFactory sFactory = SeffFactory.eINSTANCE;
		UsageModel usageModel = RunnerHelper.getUsageRoot(graph);
		for(OperationSignature signature : newInterface.getSignatures__OperationInterface()) {
			ResourceDemandingSEFF seff = sFactory.createResourceDemandingSEFF();
			seff.setDescribedService__SEFF(signature);
			//
			StartAction start = sFactory.createStartAction();
			start.setEntityName("start");
			//Create an internal call simulating the overhead of the wrapper
			InternalAction internalAction = sFactory.createInternalAction();
			internalAction.setEntityName("Overhead" + " [" + newComponent.getEntityName() + "]");
			ParametricResourceDemand cpuDemand = SeffPerformanceFactory.eINSTANCE.createParametricResourceDemand();
			
			cpuDemand.setRequiredResource_ParametricResourceDemand(PCMDefault.getCPU());
			
			PCMRandomVariable consumptionVariable = CoreFactory.eINSTANCE.createPCMRandomVariable();
			
			consumptionVariable.setSpecification("2");
			cpuDemand.setSpecification_ParametericResourceDemand(consumptionVariable);
			internalAction.getResourceDemand_Action().add(cpuDemand);
			//
			
			ExternalCallAction externalCallAction = sFactory.createExternalCallAction();
			externalCallAction.setEntityName(RunnerHelper.getExternalCallActionName(newInterface, signature));
			OperationSignature externalSignature = RunnerHelper.findSignature(signature, oldInterface);
			OperationRequiredRole externalRole = RunnerHelper.findRequiredRole(newComponent, oldInterface);
			externalCallAction.setCalledService_ExternalService(externalSignature);
			externalCallAction.setRole_ExternalService(externalRole);
			
			//Find a similar external action to replicate the variable caracterisation in the wrapper
			List<ExternalCallAction> similarExternalActions = this.collectSimilarExternalCalls(newComponent, oldInterface, externalSignature);
			//Check if there is a reference in the usage model, in case it is a entry interface...
			List<EntryLevelSystemCall> similarEntryCalls = this.collectSimilarEntryCalls(usageModel, newComponent, oldInterface, externalSignature);
			
			if(similarExternalActions.size() > 0) {
				ExternalCallAction similarAction = similarExternalActions.get(0);
				EList<VariableUsage> similarInput = similarAction.getInputVariableUsages__CallAction();
				//EList<VariableUsage> similarReturn = similarAction.getReturnVariableUsage__CallReturnAction();
				//externalCallAction.getInputVariableUsages__CallAction().addAll(EcoreUtil.copyAll(similarInput));
				//externalCallAction.getReturnVariableUsage__CallReturnAction().addAll(EcoreUtil.copyAll(similarReturn));
				for(Parameter parameter : externalSignature.getParameters__OperationSignature()) {
					VariableUsage variableUsage = ParameterFactory.eINSTANCE.createVariableUsage();
					externalCallAction.getInputVariableUsages__CallAction().add(variableUsage);
					//Reference
					variableUsage.setNamedReference__VariableUsage(this.createReference(parameter.getParameterName()));
					//Find the characterisation type in a similar action
					List<VariableCharacterisation> similarCharacterisation = this.findCharacterisation(parameter, similarInput);
					VariableCharacterisationType type;
					if(similarCharacterisation.size() > 0)
						type = similarCharacterisation.get(0).getType();
					else 
						type = VariableCharacterisationType.VALUE;
					//Characterisation
					variableUsage.getVariableCharacterisation_VariableUsage().add(this.createCharacterisation(type, parameter.getParameterName() + "." + type.name()));
				}
			}
			else if(similarEntryCalls.size() > 0) {
				EntryLevelSystemCall similarCall = similarEntryCalls.get(0);
				EList<VariableUsage> similarInput = similarCall.getInputParameterUsages_EntryLevelSystemCall();
				for(Parameter parameter : externalSignature.getParameters__OperationSignature()) {
					VariableUsage variableUsage = ParameterFactory.eINSTANCE.createVariableUsage();
					externalCallAction.getInputVariableUsages__CallAction().add(variableUsage);
					//Reference
					variableUsage.setNamedReference__VariableUsage(this.createReference(parameter.getParameterName()));
					//Find the characterisation type in a similar action
					List<VariableCharacterisation> similarCharacterisation = this.findCharacterisation(parameter, similarInput);
					VariableCharacterisationType type;
					if(similarCharacterisation.size() > 0)
						type = similarCharacterisation.get(0).getType();
					else 
						type = VariableCharacterisationType.VALUE;
					//Characterisation
					variableUsage.getVariableCharacterisation_VariableUsage().add(this.createCharacterisation(type, parameter.getParameterName() + "." + type.name()));
				}
			}
			
			SetVariableAction returnSetAction = null;
			if(externalSignature.getReturnType__OperationSignature() != null) {
				List<SetVariableAction> similarReturnActions = this.collectSimilarReturnSetVariables(newComponent, externalSignature);
				if(similarReturnActions.size() > 0) {
 					SetVariableAction similarReturnAction = similarReturnActions.get(0);
 					returnSetAction = sFactory.createSetVariableAction();
 					returnSetAction.setEntityName("SetReturn" + " [" + oldInterface.getEntityName() + "." + externalSignature.getEntityName() + "]");
 					int variableNumber = 1;
 					for(VariableUsage similarReturnVariableUsage : similarReturnAction.getLocalVariableUsages_SetVariableAction()) {
 						VariableUsage externalReturnVariableUsage = ParameterFactory.eINSTANCE.createVariableUsage();
 						VariableUsage setReturnVariableUsage = ParameterFactory.eINSTANCE.createVariableUsage();
 						//
 						AbstractNamedReference similarNameReference = similarReturnVariableUsage.getNamedReference__VariableUsage();
 						VariableCharacterisation similarVariableCharacterisation = similarReturnVariableUsage.getVariableCharacterisation_VariableUsage().get(0);
 						VariableCharacterisationType similarType = similarVariableCharacterisation.getType();
 						String localVariable = "returnVar" + variableNumber++;
 						//
 						String externalName = similarNameReference.getReferenceName();
 						externalReturnVariableUsage.getVariableCharacterisation_VariableUsage().add(this.createCharacterisation(similarType, this.computeVariableReferenceName(similarNameReference) + "." + similarType.getName()));
 						externalReturnVariableUsage.setNamedReference__VariableUsage(this.createReference(localVariable));
 						setReturnVariableUsage.getVariableCharacterisation_VariableUsage().add(this.createCharacterisation(similarType, localVariable + "." + similarType.getName()));
 						setReturnVariableUsage.setNamedReference__VariableUsage(EcoreUtil.copy(similarNameReference));
 						//
 						externalCallAction.getReturnVariableUsage__CallReturnAction().add(externalReturnVariableUsage);
 						returnSetAction.getLocalVariableUsages_SetVariableAction().add(setReturnVariableUsage);
 					}
				}
				else if(similarEntryCalls.size() > 0) {
					EntryLevelSystemCall similarCall = similarEntryCalls.get(0);
					EList<VariableUsage> similarReturn = similarCall.getOutputParameterUsages_EntryLevelSystemCall();
					for(VariableUsage returnVariableUsage : similarReturn)
						externalCallAction.getReturnVariableUsage__CallReturnAction().add(EcoreUtil.copy(returnVariableUsage));
				}
				else {
					VariableUsage returnUsage = ParameterFactory.eINSTANCE.createVariableUsage();
					externalCallAction.getReturnVariableUsage__CallReturnAction().add(returnUsage);
					//Reference
					returnUsage.setNamedReference__VariableUsage(this.createReference("return"));
					//Characterisation
					returnUsage.getVariableCharacterisation_VariableUsage().add(this.createCharacterisation(VariableCharacterisationType.VALUE, "return" + "." + VariableCharacterisationType.VALUE.name()));
				}
			}
			//
			StopAction stop = sFactory.createStopAction();
			stop.setEntityName("stop");
			//
			start.setSuccessor_AbstractAction(internalAction);
			internalAction.setPredecessor_AbstractAction(start);
			internalAction.setSuccessor_AbstractAction(externalCallAction);
			externalCallAction.setPredecessor_AbstractAction(internalAction);
			if(returnSetAction == null) {
				externalCallAction.setSuccessor_AbstractAction(stop);
				stop.setPredecessor_AbstractAction(externalCallAction);
			}
			else {
				externalCallAction.setSuccessor_AbstractAction(returnSetAction);
				returnSetAction.setPredecessor_AbstractAction(externalCallAction);
				returnSetAction.setSuccessor_AbstractAction(stop);
				stop.setPredecessor_AbstractAction(returnSetAction);
			}
			seff.getSteps_Behaviour().add(start);
			seff.getSteps_Behaviour().add(internalAction);
			seff.getSteps_Behaviour().add(externalCallAction);
			if(returnSetAction != null)
				seff.getSteps_Behaviour().add(returnSetAction);
			seff.getSteps_Behaviour().add(stop);
			newComponent.getServiceEffectSpecifications__BasicComponent().add(seff);
		}
		System.out.println("Successfully created a simple SEFF for the wrapper component and interface");
	}
	
	private String computeVariableReferenceName(AbstractNamedReference reference) {
		String name = null;
		if(reference instanceof VariableReference)
			name = ((VariableReference)reference).getReferenceName();
		else if(reference instanceof NamespaceReference)
			name = ((NamespaceReference)reference).getReferenceName() + "." + this.computeVariableReferenceName(((NamespaceReference)reference).getInnerReference_NamespaceReference());
		return name;
	}
	
	private VariableReference createReference(String name) {
		VariableReference variableReference = StoexFactory.eINSTANCE.createVariableReference();
		variableReference.setReferenceName(name);
		return variableReference;
	}

	private VariableCharacterisation createCharacterisation(VariableCharacterisationType type, String specification) {
		VariableCharacterisation variableCharacterisation = ParameterFactory.eINSTANCE.createVariableCharacterisation();
		variableCharacterisation.setType(type);
		PCMRandomVariable valueVariable = CoreFactory.eINSTANCE.createPCMRandomVariable();
		valueVariable.setSpecification(specification);
		variableCharacterisation.setSpecification_VariableCharacterisation(valueVariable);
		return variableCharacterisation;
	}
	
	private List<SetVariableAction> collectSimilarReturnSetVariables(BasicComponent newComponent, OperationSignature signature) {
		List<SetVariableAction> variableCallActions = new ArrayList<SetVariableAction>();
		Repository repository = newComponent.getRepository__RepositoryComponent();
		Iterator<RepositoryComponent> components = repository.getComponents__Repository().iterator();
		while(components.hasNext()) {
			BasicComponent component = (BasicComponent) components.next();
			if(!component.equals(newComponent)) {
				variableCallActions.addAll(this.collectReturnSetVariables(component, signature));
			}
		}
		return variableCallActions;
	}

	private List<SetVariableAction> collectReturnSetVariables(BasicComponent component, OperationSignature signature) {
		List<SetVariableAction> variableCallActions = new ArrayList<SetVariableAction>();
		Iterator<ServiceEffectSpecification> seffIterator = component.getServiceEffectSpecifications__BasicComponent().iterator();
    	while(seffIterator.hasNext()) {
    		ResourceDemandingSEFF seff = (ResourceDemandingSEFF) seffIterator.next();
    		if(seff.getDescribedService__SEFF().equals(signature))
    			variableCallActions.addAll(this.collectReturnSetVariables(seff.getSteps_Behaviour()));
		}
		return variableCallActions;
	}
	
	private List<SetVariableAction> collectReturnSetVariables(List<AbstractAction> actions) {
		List<SetVariableAction> setVariableActions = new ArrayList<SetVariableAction>();
		Iterator<AbstractAction> actionIterator = actions.iterator();
    	while(actionIterator.hasNext() && setVariableActions.size() == 0) {
    		AbstractAction action = actionIterator.next();
			if(action instanceof SetVariableAction) {
				SetVariableAction setVariableAction = (SetVariableAction) action;
				AbstractAction successor = setVariableAction.getSuccessor_AbstractAction();
				if(successor != null && successor instanceof StopAction) {
					setVariableActions.add(setVariableAction);
				}
			}
		}
		return setVariableActions;
	}

	private List<VariableCharacterisation> findCharacterisation(Parameter parameter, EList<VariableUsage> similarInput) {
		List<VariableCharacterisation> similarVariableCharacterisation = new ArrayList<VariableCharacterisation>();
		for(VariableUsage similarUsageVariable : similarInput) {
			AbstractNamedReference namedReference = similarUsageVariable.getNamedReference__VariableUsage();
			if(namedReference != null && namedReference.getReferenceName().equals(parameter.getParameterName()))
				similarVariableCharacterisation.addAll(similarUsageVariable.getVariableCharacterisation_VariableUsage());
		}
		return similarVariableCharacterisation;
	}


	private VariableUsage findVariableUsage(EList<VariableUsage> variableUsages, String name) {
		for(VariableUsage usage : variableUsages) {
			if(usage.getNamedReference__VariableUsage() != null) 
				if(usage.getNamedReference__VariableUsage().getReferenceName().equals(name))
					return usage;
		}
		return null;
	}
	
	private List<EntryLevelSystemCall> collectSimilarEntryCalls(UsageModel usageModel, BasicComponent newComponent, OperationInterface oldInterface, OperationSignature signature) {
		List<EntryLevelSystemCall> entryCalls = new ArrayList<EntryLevelSystemCall>();
		for(UsageScenario usageScenario : usageModel.getUsageScenario_UsageModel()) {
			ScenarioBehaviour scenarioBehaviour = usageScenario.getScenarioBehaviour_UsageScenario();
			entryCalls.addAll(this.collectEntryCalls(scenarioBehaviour.getActions_ScenarioBehaviour(), newComponent, oldInterface, signature));
		}
		return entryCalls;
	}
	
	private List<EntryLevelSystemCall> collectEntryCalls(EList<AbstractUserAction> actions, 
			BasicComponent newComponent, OperationInterface oldInterface, OperationSignature signature) {
		List<EntryLevelSystemCall> entryCalls = new ArrayList<EntryLevelSystemCall>();
		Iterator<AbstractUserAction> actionIterator = actions.iterator();
    	while(actionIterator.hasNext()) {
    		AbstractUserAction action = (AbstractUserAction) actionIterator.next();
    		entryCalls.addAll(this.collectEntryCalls(action, newComponent, oldInterface, signature));
		}
		return entryCalls;
	}
		
	private List<EntryLevelSystemCall> collectEntryCalls(AbstractUserAction action, 
			BasicComponent newComponent, OperationInterface oldInterface, OperationSignature signature) {
		List<EntryLevelSystemCall> entryCalls = new ArrayList<EntryLevelSystemCall>();
		if(action instanceof Branch) {
			Branch branchAction = (Branch) action;
	    	Iterator<BranchTransition> branchTransitions = branchAction.getBranchTransitions_Branch().iterator();
	    	while(branchTransitions.hasNext()) {
	    		BranchTransition branchTransition = branchTransitions.next();
	    		entryCalls.addAll(
	    			this.collectEntryCalls(
	    				branchTransition.getBranchedBehaviour_BranchTransition().getActions_ScenarioBehaviour(), 
	    				newComponent, oldInterface, signature)
	    		);
	    	}
		}
		if(action instanceof Loop) {
			Loop loopAction = (Loop) action;
			entryCalls.addAll(
				this.collectEntryCalls(
					loopAction.getBodyBehaviour_Loop().getActions_ScenarioBehaviour(), 
					newComponent, oldInterface, signature)
			);
		}
		if(action instanceof EntryLevelSystemCall) {
			EntryLevelSystemCall entryCall = (EntryLevelSystemCall) action;
			//The provided role is found in the system model, no need to check it here...perhaps in the calling method is useful...
			//OperationProvidedRole entryProvidedRole = entryCall.getProvidedRole_EntryLevelSystemCall();
			OperationSignature entrySignature = entryCall.getOperationSignature__EntryLevelSystemCall();
			if(entrySignature.equals(signature)) {
				entryCalls.add(entryCall);
			}
		}
		return entryCalls;
	}

	private List<ExternalCallAction> collectSimilarExternalCalls(BasicComponent newComponent, OperationInterface oldInterface, OperationSignature signature) {
		List<ExternalCallAction> externalCallActions = new ArrayList<ExternalCallAction>();
		Repository repository = newComponent.getRepository__RepositoryComponent();
		Iterator<RepositoryComponent> components = repository.getComponents__Repository().iterator();
		while(components.hasNext()) {
			BasicComponent component = (BasicComponent) components.next();
			if(!component.equals(newComponent)) {
				externalCallActions.addAll(this.collectExternalCalls(component, oldInterface, signature));
			}
		}
		return externalCallActions;
	}
	
	private List<ExternalCallAction> collectExternalCalls(BasicComponent component, OperationInterface oldInterface, OperationSignature signature) {
		List<ExternalCallAction> externalCallActions = new ArrayList<ExternalCallAction>();
		Iterator<ServiceEffectSpecification> seffIterator = component.getServiceEffectSpecifications__BasicComponent().iterator();
		OperationRequiredRole oldRequiredRole = RunnerHelper.findRequiredRole(component, oldInterface);
    	while(seffIterator.hasNext()) {
    		ResourceDemandingSEFF seff = (ResourceDemandingSEFF) seffIterator.next();
    		externalCallActions.addAll(this.collectExternalCalls(seff.getSteps_Behaviour(), oldInterface, oldRequiredRole, signature));
		}
		return externalCallActions;
	}
	
	private List<ExternalCallAction> collectExternalCalls(List<AbstractAction> actions, OperationInterface oldInterface, OperationRequiredRole oldRequiredRole, OperationSignature signature) {
		List<ExternalCallAction> externalCallActions = new ArrayList<ExternalCallAction>();
		Iterator<AbstractAction> actionIterator = actions.iterator();
    	while(actionIterator.hasNext()) {
    		AbstractAction action = actionIterator.next();
			if(action instanceof BranchAction) {
		    	BranchAction branchAction = (BranchAction) action;
		    	Iterator<AbstractBranchTransition> branchTransitions = branchAction.getBranches_Branch().iterator();
		    	while(branchTransitions.hasNext()) {
		    		AbstractBranchTransition branchTransition = branchTransitions.next();
		    		externalCallActions.addAll(this.collectExternalCalls(branchTransition.getBranchBehaviour_BranchTransition().getSteps_Behaviour(), oldInterface, oldRequiredRole, signature));
		    	}
			}
			if(action instanceof LoopAction) {
				LoopAction loopAction = (LoopAction) action;
				externalCallActions.addAll(this.collectExternalCalls(loopAction.getBodyBehaviour_Loop().getSteps_Behaviour(), oldInterface, oldRequiredRole, signature));
			}
			if(action instanceof ExternalCallAction) {
				ExternalCallAction externalCallAction = (ExternalCallAction) action;
				OperationRequiredRole requiredRole = externalCallAction.getRole_ExternalService();
				OperationSignature requiredSignature = externalCallAction.getCalledService_ExternalService();
				OperationInterface requiredInterface = requiredRole.getRequiredInterface__OperationRequiredRole();
				if(requiredRole.equals(oldRequiredRole) && requiredInterface.equals(oldInterface) && requiredSignature.equals(signature)) {
					externalCallActions.add(externalCallAction);
				}
			}
		}
		return externalCallActions;
	}

	private RuleApplication runFourthRule(EGraph graph) {
		RuleApplication app = new RuleApplicationImpl(engine);
		app.setEGraph(graph);
		app.setRule(reconnectComponents2Wrapper);
		boolean success = app.execute(monitor);
		if(success)
			System.out.println("Successfully reconnected existing components to wrapper");
		else
			System.out.println("Could not reconnect existing components to wrapper");
		return app;
	}
	
	private void runLastRule(EGraph graph) {
		Trace root = RunnerHelper.getTraceRoot(graph);
		//Removing operation required roles
		Trace wrapTrace = RunnerHelper.getTraces(root, TRACE_Wrapped, false).get(0);
		List<Trace> affectedTraces = RunnerHelper.getTraces(wrapTrace, TRACE_Affected, false);
		for(Trace affectedTrace : affectedTraces) {
			BasicComponent affectedComponent = (BasicComponent) affectedTrace.getSource().get(0);
			List<Trace> connectionTraces = RunnerHelper.getTraces(affectedTrace, TRACE_Connection, false);
			for(Trace connectionTrace : connectionTraces) {
				if(connectionTrace.getSource().size() > 0) {
					OperationRequiredRole operationRequiredRole = (OperationRequiredRole) connectionTrace.getSource().get(0);
					affectedComponent.getRequiredRoles_InterfaceRequiringEntity().remove(operationRequiredRole);
					EcoreUtil.delete((EObject) operationRequiredRole, false);
				}
			}
		}
		//Removing assembly connectors
		List<Trace> assemblyConnectors = RunnerHelper.getTraces(root, TRACE_AssemblyConnector, true);
		for(Trace trace : assemblyConnectors) {
			if(trace.getSource().size() > 0) {
				EObject oldAssemblyConnector = (EObject) trace.getSource().get(0);
				EcoreUtil.delete((EObject) oldAssemblyConnector, false);
			}
		}
		//EcoreUtil.delete((EObject) root, true);
	}
	
	public static void main(String[] args) {
		String dirPath = "src/edu/squat/transformations/modifiability/wrapper";
		String henshinFilename = "wrapper-modular.henshin";
		String repositoryFilename, systemFilename, resourceEnvironmentFilename, allocationFilename, usageFilename;
		String resultRepositoryFilename, resultSystemFilename, resultResourceEnvironmentFilename, resultAllocationFilename, resultUsageFilename;

		WrapperRunner runner = new WrapperRunner();
		
		//Individual testing
		repositoryFilename = "wrap-test.repository";
		resultRepositoryFilename = "wrap-test-" + "#REPLACEMENT#" + ".repository";
		//runner.run(dirPath, repositoryFilename, henshinFilename, resultRepositoryFilename, true);
		
		//Complete Individual testing
		repositoryFilename = "wrap-test.repository";
		systemFilename = "wrap-test.system";
		resourceEnvironmentFilename = "wrap-test.resourceenvironment";
		allocationFilename = "wrap-test.allocation";
		resultRepositoryFilename = "wrap-test-" + "#REPLACEMENT#" + ".repository";
		resultSystemFilename = "wrap-test-" + "#REPLACEMENT#" + ".system";
		resultResourceEnvironmentFilename = "wrap-test-" + "#REPLACEMENT#" + ".resourceenvironment";
		resultAllocationFilename = "wrap-test-" + "#REPLACEMENT#" + ".allocation";
		/*runner.run(dirPath, 
				repositoryFilename, systemFilename, resourceEnvironmentFilename, allocationFilename,
				henshinFilename, 
				resultRepositoryFilename, resultSystemFilename, resultResourceEnvironmentFilename, resultAllocationFilename,
				true);*/
		
		//Complete SimpleTactics+ testing
		repositoryFilename = "stplus.repository";
		systemFilename = "stplus.system";
		resourceEnvironmentFilename = "stplus.resourceenvironment";
		allocationFilename = "stplus.allocation";
		usageFilename = "stplus.usagemodel";
		resultRepositoryFilename = "stplus-" + "#REPLACEMENT#" + ".repository";
		resultSystemFilename = "stplus-" + "#REPLACEMENT#" + ".system";
		resultResourceEnvironmentFilename = "stplus-" + "#REPLACEMENT#" + ".resourceenvironment";
		resultAllocationFilename = "stplus-" + "#REPLACEMENT#" + ".allocation";
		resultUsageFilename = "stplus-" + "#REPLACEMENT#" + ".usagemodel";
		/*runner.run(dirPath, 
				repositoryFilename, systemFilename, resourceEnvironmentFilename, allocationFilename, usageFilename,
				henshinFilename, 
				resultRepositoryFilename, resultSystemFilename, resultResourceEnvironmentFilename, resultAllocationFilename, resultUsageFilename,
				true);*/
		
		//Complete SimpleTactics+ testing after split responsability
		repositoryFilename = "stplus-split.repository";
		systemFilename = "stplus-split.system";
		resourceEnvironmentFilename = "stplus-split.resourceenvironment";
		allocationFilename = "stplus-split.allocation";
		usageFilename = "stplus-split.usagemodel";
		resultRepositoryFilename = "stplus-split-" + "#REPLACEMENT#" + ".repository";
		resultSystemFilename = "stplus-split-" + "#REPLACEMENT#" + ".system";
		resultResourceEnvironmentFilename = "stplus-split-" + "#REPLACEMENT#" + ".resourceenvironment";
		resultAllocationFilename = "stplus-split-" + "#REPLACEMENT#" + ".allocation";
		resultUsageFilename = "stplus-split-" + "#REPLACEMENT#" + ".usagemodel";
		/*runner.run(dirPath, 
				repositoryFilename, systemFilename, resourceEnvironmentFilename, allocationFilename, usageFilename,
				henshinFilename, 
				resultRepositoryFilename, resultSystemFilename, resultResourceEnvironmentFilename, resultAllocationFilename, resultUsageFilename,
				true);*/
		
		//CoCoME testing
		repositoryFilename = "cocome.repository";
		systemFilename = "cocome.system";
		resourceEnvironmentFilename = "cocome.resourceenvironment";
		allocationFilename = "cocome.allocation";
		usageFilename = "cocome.usagemodel";
		resultRepositoryFilename = "cocome-" + "#REPLACEMENT#" + ".repository";
		resultSystemFilename = "cocome-" + "#REPLACEMENT#" + ".system";
		resultResourceEnvironmentFilename = "cocome-" + "#REPLACEMENT#" + ".resourceenvironment";
		resultAllocationFilename = "cocome-" + "#REPLACEMENT#" + ".allocation";
		resultUsageFilename = "cocome-" + "#REPLACEMENT#" + ".usagemodel";
		runner.run(dirPath, 
				repositoryFilename, systemFilename, resourceEnvironmentFilename, allocationFilename, usageFilename,
				henshinFilename, 
				resultRepositoryFilename, resultSystemFilename, resultResourceEnvironmentFilename, resultAllocationFilename, resultUsageFilename,
				true);
	}
}

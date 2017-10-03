package edu.squat.transformations.modifiability.splitrespn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.henshin.interpreter.EGraph;
import org.eclipse.emf.henshin.interpreter.Engine;
import org.eclipse.emf.henshin.interpreter.RuleApplication;
import org.eclipse.emf.henshin.interpreter.impl.EngineImpl;
import org.eclipse.emf.henshin.interpreter.impl.RuleApplicationImpl;
import org.eclipse.emf.henshin.model.Rule;
import org.eclipse.emf.henshin.trace.Trace;
import org.eclipse.emf.henshin.trace.TraceFactory;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.allocation.AllocationFactory;
import org.palladiosimulator.pcm.core.composition.AssemblyConnector;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.CompositionFactory;
import org.palladiosimulator.pcm.core.composition.Connector;
import org.palladiosimulator.pcm.core.composition.ProvidedDelegationConnector;
import org.palladiosimulator.pcm.parameter.VariableUsage;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.OperationProvidedRole;
import org.palladiosimulator.pcm.repository.OperationRequiredRole;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.repository.PassiveResource;
import org.palladiosimulator.pcm.repository.ProvidedRole;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryComponent;
import org.palladiosimulator.pcm.repository.RepositoryFactory;
import org.palladiosimulator.pcm.repository.RequiredRole;
import org.palladiosimulator.pcm.repository.Signature;
import org.palladiosimulator.pcm.seff.AbstractAction;
import org.palladiosimulator.pcm.seff.AbstractBranchTransition;
import org.palladiosimulator.pcm.seff.BranchAction;
import org.palladiosimulator.pcm.seff.ExternalCallAction;
import org.palladiosimulator.pcm.seff.LoopAction;
import org.palladiosimulator.pcm.seff.ResourceDemandingSEFF;
import org.palladiosimulator.pcm.seff.ServiceEffectSpecification;

import edu.squat.transformations.ArchitecturalVersion;
import edu.squat.transformations.modifiability.PCMTransformerRunner;
import edu.squat.transformations.modifiability.RunnerHelper;
import edu.squat.transformations.modifiability.Tactic;

public class SplitRespNRunner extends PCMTransformerRunner {
	private final static String TRACE_Splittable = "splittable";
	private final static String TRACE_SplittedInterface = "splitint";
	private final static String TRACE_RewireInternal = "rewire-internal";
	private final static String TRACE_RewireExternal = "rewire-external";
	private final static String TRACE_AssemblyContext = "assembly-cont";
	private final static String TRACE_AssemblyConnector = "assembly-conn";
	private final static String TRACE_AllocationContext = "allocation-cont";
	//
	private Engine engine;
	//
	private Rule createInitialTrace;
	private Rule markComponents2Split;
	private Rule splitComp4Interface;
	//
	private boolean smartReconnection = false;
	//
	public SplitRespNRunner(boolean smartReconnection) {
		super();
		this.smartReconnection = smartReconnection;
	}

	public SplitRespNRunner() {
		super();
	}
	
	@Override
	public void loadRules() {
		createInitialTrace = (Rule) module.getUnit("createInitialTrace");
		//createInitialTrace.setCheckDangling(false);
		markComponents2Split = (Rule) module.getUnit("markComponents2Split");
		splitComp4Interface = (Rule) module.getUnit("splitComp4Interface");
	}
	
	@Override
	public List<ArchitecturalVersion> run(boolean saveResult) {
		List<ArchitecturalVersion> ret= new ArrayList<>();
		
		candidateTactics = new ArrayList<Tactic>();
		//Create and configure an engine
		engine = new EngineImpl();
		//engine.getOptions().put(engine.OPTION_CHECK_DANGLING, false);

		//Run the first and second rule to create the initial traces
		RuleApplication firstRule = this.runFirstRule(graph);
		boolean successFirstRule = firstRule.execute(monitor);
		RuleApplication secondRule = this.runSecondRule(graph);
		boolean successSecondRule = secondRule.execute(monitor);
		
		//If the transformation was successful continue with the other rules
		if(successFirstRule && successSecondRule) {
			System.out.println("Successfully marked components and interfaces that could be splitted");
			Trace root = RunnerHelper.getTraceRoot(graph);
			
			List<Trace> candidateTraces = this.computeCandidates(root);
			//Avoiding references to the graph because we are going to clone it...we need emf-less references
			List<Integer> candidatePointers = this.transformToPointers(root, candidateTraces);
			
			int counter = 0;
			while(counter < candidatePointers.size()) {
				//Cloning the graph
				EGraph tempGraph = graph.copy(null);
				Trace tempRoot = RunnerHelper.getTraceRoot(tempGraph);
				//Repository tempRepository = RunnerHelper.getRepositoryRoot(tempGraph);
				//Getting the interfaces
				Trace tempCandidateTrace = this.convertToCandidate(tempRoot, candidatePointers.get(counter));
				//Removing all but the chain
				tempRoot.getSubTraces().clear();
				tempRoot.getSubTraces().add(tempCandidateTrace);
				
				//Gathering the seed component
				BasicComponent seedC = (BasicComponent) tempCandidateTrace.getSource().get(0);
				String fileName = seedC.getEntityName(); 
			
				//Configuring and executing the third rule
				this.runThirdRule(tempGraph);
				
				this.replicateComponent(tempGraph);
				this.migrateSEFF(tempGraph);
				this.rewireComponents(tempGraph);
				
				if(this.arePerformanceModelsLoaded()) {
					//Adjust the system and allocation models by executing a monolithic method (ugly but fast)
					this.fixSystemAndAllocation(tempGraph);
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
			System.out.println("Could not mark components and interfaces that could be splitted");
		}
		return ret;
	}

	private List<Trace> computeCandidates(Trace root) {
		List<Trace> candidates = new ArrayList<Trace>();
		List<Trace> splittables = RunnerHelper.getTraces(root, TRACE_Splittable, false);
		for(Trace splittable : splittables)
			if(this.isCandidate(splittable))
				candidates.add(splittable);
		return candidates;
	}
	
	private boolean isCandidate(Trace splittable) {
		List<Trace> splits = RunnerHelper.getTraces(splittable, TRACE_SplittedInterface, false);
		if(splits.size() > 1)
			return true;
		else
			return false;
	}
	
	private List<Integer> transformToPointers(Trace root, List<Trace> candidateTraces) {
		List<Integer> pointers = new ArrayList<Integer>();
		List<Trace> splittables = RunnerHelper.getTraces(root, TRACE_Splittable, false);
		for(Trace candidate : candidateTraces) {
			int pointer = splittables.indexOf(candidate);
			pointers.add(pointer);
		}
		return pointers;
	}

	private Trace convertToCandidate(Trace tempRoot, Integer index) {
		List<Trace> splittables = RunnerHelper.getTraces(tempRoot, TRACE_Splittable, false);
		Trace candidate = splittables.get(index);
		return candidate;
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
		app.setRule(markComponents2Split);
		return app;
	}

	private RuleApplication runThirdRule(EGraph graph) {
		RuleApplication app = new RuleApplicationImpl(engine);
		app.setEGraph(graph);
		app.setRule(splitComp4Interface);
		boolean success = app.execute(monitor);
		if(success)
			System.out.println("Successfully created the splitted components and linked them to their corresponding interface");
		else
			System.out.println("Could not create the splitted components and link them to their corresponding interface");
		return app;
	}

	private void replicateComponent(EGraph tempGraph) {
		Trace splittableTrace = RunnerHelper.getTraces(tempGraph, TRACE_Splittable, false).get(0);
		BasicComponent oldComponent = (BasicComponent) splittableTrace.getSource().get(0);
		List<Trace> splitintTraces = RunnerHelper.getTraces(splittableTrace, TRACE_SplittedInterface, false);
		for(Trace splitintTrace : splitintTraces) {
			//OperationInterface oldInterface = (OperationInterface) splitintTrace.getSource().get(0);
			BasicComponent newComponent = (BasicComponent) splitintTrace.getTarget().get(0);
			//Duplicating passive resources
			EList<PassiveResource> passiveResources = oldComponent.getPassiveResource_BasicComponent();
			if(passiveResources != null && !passiveResources.isEmpty()) {
				Collection<PassiveResource> passiveResourcesClone = EcoreUtil.copyAll(passiveResources);
				for(PassiveResource pr : passiveResourcesClone)
					pr.setId(EcoreUtil.generateUUID());
				newComponent.getPassiveResource_BasicComponent().addAll(passiveResourcesClone);
			}
			//Duplicating component resources
			EList<VariableUsage> variableUsages = oldComponent.getComponentParameterUsage_ImplementationComponentType();
			if(variableUsages != null && !variableUsages.isEmpty()) {
				Collection<VariableUsage> variableUsagesClone = EcoreUtil.copyAll(variableUsages);
				newComponent.getComponentParameterUsage_ImplementationComponentType().addAll(variableUsagesClone);
			}
		}
		System.out.println("Successfully cloned the internal elements of the component and the associated interface");
	}

	private void migrateSEFF(EGraph tempGraph) {
		Trace splittableTrace = RunnerHelper.getTraces(tempGraph, TRACE_Splittable, false).get(0);
		BasicComponent oldComponent = (BasicComponent) splittableTrace.getSource().get(0);
		List<Trace> splitintTraces = RunnerHelper.getTraces(splittableTrace, TRACE_SplittedInterface, false);
		for(Trace splitintTrace : splitintTraces) {
			//OperationInterface oldInterface = (OperationInterface) splitintTrace.getSource().get(0);
			BasicComponent newComponent = (BasicComponent) splitintTrace.getTarget().get(0);
			List<ServiceEffectSpecification> seff2migrate = new ArrayList<ServiceEffectSpecification>();
			for(ServiceEffectSpecification seff : oldComponent.getServiceEffectSpecifications__BasicComponent()) {
				if(this.shouldMigrate(seff, newComponent))
					seff2migrate.add(seff);
			}
			oldComponent.getServiceEffectSpecifications__BasicComponent().removeAll(seff2migrate);
			newComponent.getServiceEffectSpecifications__BasicComponent().addAll(seff2migrate);
		}
	}
	
	private boolean shouldMigrate(ServiceEffectSpecification seff, BasicComponent component) {
		boolean migrate = false;
		Iterator<ProvidedRole> providedRoles = component.getProvidedRoles_InterfaceProvidingEntity().iterator();
		while(providedRoles.hasNext() && migrate == false) {
			OperationProvidedRole operationProvidedRole = (OperationProvidedRole) providedRoles.next();
			OperationInterface operationInterface = operationProvidedRole.getProvidedInterface__OperationProvidedRole();
			Iterator<OperationSignature> signatures = operationInterface.getSignatures__OperationInterface().iterator();
			while(signatures.hasNext() && migrate == false) {
				Signature signature = signatures.next();
				if(signature.equals(seff.getDescribedService__SEFF()))
					migrate = true;
			}
		}
		return migrate;
	}
	
	private void rewireComponents(EGraph tempGraph) {
		Trace splittableTrace = RunnerHelper.getTraces(tempGraph, TRACE_Splittable, false).get(0);
		BasicComponent oldComponent = (BasicComponent) splittableTrace.getSource().get(0);
		List<Trace> splitintTraces = RunnerHelper.getTraces(splittableTrace, TRACE_SplittedInterface, false);
		//Reconnect external components which are required by the original component...
		for(Trace splitintTrace : splitintTraces) {
			//OperationInterface splitInterface = (OperationInterface) splitintTrace.getSource().get(0);
			BasicComponent splitComponent = (BasicComponent) splitintTrace.getTarget().get(0);
			for(RequiredRole requiredRole : oldComponent.getRequiredRoles_InterfaceRequiringEntity()) {
				OperationRequiredRole operationRequiredRole = (OperationRequiredRole) requiredRole;
				OperationInterface operationInterface = operationRequiredRole.getRequiredInterface__OperationRequiredRole();
				if(!smartReconnection || this.shouldRewire(operationInterface, splitComponent)) {
					OperationRequiredRole newRequiredRole = RepositoryFactory.eINSTANCE.createOperationRequiredRole();
					newRequiredRole.setRequiredInterface__OperationRequiredRole(operationInterface);
					splitComponent.getRequiredRoles_InterfaceRequiringEntity().add(newRequiredRole);
					Trace rewireTrace = TraceFactory.eINSTANCE.createTrace();
					rewireTrace.setName(TRACE_RewireExternal);
					rewireTrace.getSource().add((EObject) splitComponent);
					rewireTrace.getTarget().add((EObject) newRequiredRole);
					splittableTrace.getSubTraces().add(rewireTrace);
				}
			}
		}
		//Reconnect between splits...
		for(int i = 0; i < splitintTraces.size() - 1; i++) {
			Trace splitintTraceI = splitintTraces.get(i);
			OperationInterface interfaceI = (OperationInterface) splitintTraceI.getSource().get(0);
			BasicComponent componentI = (BasicComponent) splitintTraceI.getTarget().get(0);
			for(int j = i + 1; j < splitintTraces.size(); j++) {
				Trace splitintTraceJ = splitintTraces.get(j);
				OperationInterface interfaceJ = (OperationInterface) splitintTraceJ.getSource().get(0);
				BasicComponent componentJ = (BasicComponent) splitintTraceJ.getTarget().get(0);
				if(!smartReconnection || this.shouldRewire(interfaceI, componentJ)) {
					OperationRequiredRole newRequiredRole = RepositoryFactory.eINSTANCE.createOperationRequiredRole();
					newRequiredRole.setRequiredInterface__OperationRequiredRole(interfaceI);
					componentJ.getRequiredRoles_InterfaceRequiringEntity().add(newRequiredRole);
					Trace rewireTrace = TraceFactory.eINSTANCE.createTrace();
					rewireTrace.setName(TRACE_RewireInternal);
					rewireTrace.getSource().add((EObject) componentJ);
					rewireTrace.getTarget().add((EObject) newRequiredRole);
					splittableTrace.getSubTraces().add(rewireTrace);
				}
				if(!smartReconnection || this.shouldRewire(interfaceJ, componentI)) {
					OperationRequiredRole newRequiredRole = RepositoryFactory.eINSTANCE.createOperationRequiredRole();
					newRequiredRole.setRequiredInterface__OperationRequiredRole(interfaceJ);
					componentI.getRequiredRoles_InterfaceRequiringEntity().add(newRequiredRole);
					Trace rewireTrace = TraceFactory.eINSTANCE.createTrace();
					rewireTrace.setName(TRACE_RewireInternal);
					rewireTrace.getSource().add((EObject) componentI);
					rewireTrace.getTarget().add((EObject) newRequiredRole);
					splittableTrace.getSubTraces().add(rewireTrace);
				}
			}
		}
	}
	
	private boolean shouldRewire(OperationInterface operationInterface, BasicComponent requiringComponent) {
		boolean shouldRewire = false;
		Iterator<ServiceEffectSpecification> seffIterator = requiringComponent.getServiceEffectSpecifications__BasicComponent().iterator();
    	while(seffIterator.hasNext() && !shouldRewire) {
    		ResourceDemandingSEFF seff = (ResourceDemandingSEFF) seffIterator.next();
    		if(this.isLinked(seff.getSteps_Behaviour(), operationInterface))
    			shouldRewire = true;
		}
		return shouldRewire;
	}
	
	private boolean isLinked(List<AbstractAction> actions, OperationInterface operationInterface) {
		boolean isLinked = false;
    	Iterator<AbstractAction> actionIterator = actions.iterator();
    	while(actionIterator.hasNext() && !isLinked) {
    		AbstractAction action = actionIterator.next();
			if(action instanceof BranchAction) {
		    	BranchAction branchAction = (BranchAction) action;
		    	Iterator<AbstractBranchTransition> branchTransitions = branchAction.getBranches_Branch().iterator();
		    	while(branchTransitions.hasNext() && !isLinked) {
		    		AbstractBranchTransition branchTransition = branchTransitions.next();
		    		if(this.isLinked(branchTransition.getBranchBehaviour_BranchTransition().getSteps_Behaviour(), operationInterface))
						isLinked = true;
		    	}
			}
			if(action instanceof LoopAction) {
				LoopAction loopAction = (LoopAction) action;
				if(this.isLinked(loopAction.getBodyBehaviour_Loop().getSteps_Behaviour(), operationInterface))
					isLinked = true;
			}
			if(action instanceof ExternalCallAction) {
				ExternalCallAction externalCallAction = (ExternalCallAction) action;
				OperationInterface externalInterface = externalCallAction.getRole_ExternalService().getRequiredInterface__OperationRequiredRole();
				if(externalInterface.equals(operationInterface))
					isLinked = true;
			}
		}
		return isLinked;
	}

	private void fixSystemAndAllocation(EGraph graph) {
		Trace traceRoot = RunnerHelper.getTraceRoot(graph);
		Repository repositoryRoot = RunnerHelper.getRepositoryRoot(graph);
		org.palladiosimulator.pcm.system.System systemRoot = RunnerHelper.getSystemRoot(graph);
		
		//Create the assembly context
		Trace splittableTrace = RunnerHelper.getTraces(traceRoot, TRACE_Splittable, false).get(0);
		BasicComponent originalComponent = (BasicComponent) splittableTrace.getSource().get(0);
		List<AssemblyContext> oldAssemblyContexts = RunnerHelper.findAssemblyContexts(originalComponent, systemRoot);
		for(AssemblyContext oldAssemblyContext : oldAssemblyContexts) {
			List<Trace> splitintTraces = RunnerHelper.getTraces(splittableTrace, TRACE_SplittedInterface, false);
			for(Trace splitintTrace : splitintTraces) {
				BasicComponent splitComponent = (BasicComponent) splitintTrace.getTarget().get(0);
				AssemblyContext splitAssemblyContext = CompositionFactory.eINSTANCE.createAssemblyContext();
				splitAssemblyContext.setEncapsulatedComponent__AssemblyContext(splitComponent);
				splitAssemblyContext.setEntityName(RunnerHelper.getAssemblyContextName(splitComponent));
				systemRoot.getAssemblyContexts__ComposedStructure().add(splitAssemblyContext);
				Trace assemblyContextTrace = TraceFactory.eINSTANCE.createTrace();
				assemblyContextTrace.setName(TRACE_AssemblyContext);
				assemblyContextTrace.getSource().add((EObject) oldAssemblyContext);
				assemblyContextTrace.getTarget().add((EObject) splitAssemblyContext);
				traceRoot.getSubTraces().add(assemblyContextTrace);
			}
		}
		//Fix and create new assembly connectors
		List<Connector> connectorsToAdd = new ArrayList<Connector>();
		List<Trace> assemblyContextTraces = RunnerHelper.getTraces(traceRoot, TRACE_AssemblyContext, false);
		for(Trace assemblyContextTrace : assemblyContextTraces) {
			AssemblyContext oldAssemblyContext = (AssemblyContext) assemblyContextTrace.getSource().get(0);
			AssemblyContext splitAssemblyContext = (AssemblyContext) assemblyContextTrace.getTarget().get(0);
			BasicComponent oldComponent = (BasicComponent) oldAssemblyContext.getEncapsulatedComponent__AssemblyContext();
			BasicComponent splitComponent = (BasicComponent) splitAssemblyContext.getEncapsulatedComponent__AssemblyContext();
			
			Iterator<Connector> connectors = systemRoot.getConnectors__ComposedStructure().iterator();
			while(connectors.hasNext()) {
				Connector connector = connectors.next();
				if(connector instanceof ProvidedDelegationConnector) {
					//Fix for delegation connectors to the splitted component
					ProvidedDelegationConnector oldDelegationConnector = (ProvidedDelegationConnector) connector;
					if(oldDelegationConnector.getAssemblyContext_ProvidedDelegationConnector().equals(oldAssemblyContext) && this.providesInterface(splitComponent, oldDelegationConnector.getInnerProvidedRole_ProvidedDelegationConnector().getProvidedInterface__OperationProvidedRole())) {
						ProvidedDelegationConnector newDelegationConnector = CompositionFactory.eINSTANCE.createProvidedDelegationConnector();
						newDelegationConnector.setEntityName(oldDelegationConnector.getEntityName());
						newDelegationConnector.setAssemblyContext_ProvidedDelegationConnector(splitAssemblyContext);
						newDelegationConnector.setInnerProvidedRole_ProvidedDelegationConnector(
								this.searchNewProvidedRole(oldComponent, splitComponent, oldDelegationConnector.getInnerProvidedRole_ProvidedDelegationConnector())
						);
						newDelegationConnector.setOuterProvidedRole_ProvidedDelegationConnector(oldDelegationConnector.getOuterProvidedRole_ProvidedDelegationConnector());
						connectorsToAdd.add(newDelegationConnector);
						Trace connectorTrace = TraceFactory.eINSTANCE.createTrace();
						connectorTrace.setName(TRACE_AssemblyConnector);
						connectorTrace.getSource().add((EObject) oldDelegationConnector);
						connectorTrace.getTarget().add((EObject) newDelegationConnector);
						traceRoot.getSubTraces().add(connectorTrace);
					}
				}
				if(connector instanceof AssemblyConnector) {
					//Fix and create new assembly connectors for external components which required the original component
					AssemblyConnector oldConnector = (AssemblyConnector) connector;
					if(oldConnector.getProvidingAssemblyContext_AssemblyConnector().equals(oldAssemblyContext)) {
						AssemblyConnector newConnector = CompositionFactory.eINSTANCE.createAssemblyConnector();
						OperationProvidedRole newProvidedRole = this.searchNewProvidedRole(oldComponent, splitComponent, oldConnector.getProvidedRole_AssemblyConnector());
						if(newProvidedRole != null) {
							newConnector.setEntityName(RunnerHelper.getAssemblyConnectorName(oldConnector.getRequiringAssemblyContext_AssemblyConnector(), splitAssemblyContext));
							newConnector.setProvidingAssemblyContext_AssemblyConnector(splitAssemblyContext);
							newConnector.setRequiringAssemblyContext_AssemblyConnector(oldConnector.getRequiringAssemblyContext_AssemblyConnector());
							newConnector.setProvidedRole_AssemblyConnector(newProvidedRole);
							newConnector.setRequiredRole_AssemblyConnector(oldConnector.getRequiredRole_AssemblyConnector());
							connectorsToAdd.add(newConnector);
							Trace connectorTrace = TraceFactory.eINSTANCE.createTrace();
							connectorTrace.setName(TRACE_AssemblyConnector);
							connectorTrace.getSource().add((EObject) oldConnector);
							connectorTrace.getTarget().add((EObject) newConnector);
							traceRoot.getSubTraces().add(connectorTrace);
						}
					}
					//Fix and create new assembly connectors for external components required by the original component
					//List<Trace> rewireExternals = RunnerHelper.getTraces(splittableTrace, TRACE_RewireExternal, false);
					if(oldConnector.getRequiringAssemblyContext_AssemblyConnector().equals(oldAssemblyContext)) {
						AssemblyConnector newConnector = CompositionFactory.eINSTANCE.createAssemblyConnector();
						OperationRequiredRole newRequiredRole = this.searchNewRequiredRole(oldComponent, splitComponent, oldConnector.getRequiredRole_AssemblyConnector());
						if(newRequiredRole != null) {
							newConnector.setEntityName(RunnerHelper.getAssemblyConnectorName(splitAssemblyContext, oldConnector.getProvidingAssemblyContext_AssemblyConnector()));
							newConnector.setProvidingAssemblyContext_AssemblyConnector(oldConnector.getProvidingAssemblyContext_AssemblyConnector());
							newConnector.setRequiringAssemblyContext_AssemblyConnector(splitAssemblyContext);
							newConnector.setProvidedRole_AssemblyConnector(oldConnector.getProvidedRole_AssemblyConnector());
							newConnector.setRequiredRole_AssemblyConnector(newRequiredRole);
							connectorsToAdd.add(newConnector);
							Trace connectorTrace = TraceFactory.eINSTANCE.createTrace();
							connectorTrace.setName(TRACE_AssemblyConnector);
							connectorTrace.getSource().add((EObject) oldConnector);
							connectorTrace.getTarget().add((EObject) newConnector);
							traceRoot.getSubTraces().add(connectorTrace);
						}
					}
				}
			}
		}
		//Create the new assembly connectors for internal splits
		List<Trace> rewireInternalTraces = RunnerHelper.getTraces(splittableTrace, TRACE_RewireInternal, false);
		for(Trace rewireInternalTrace : rewireInternalTraces) {
			BasicComponent requiringSplitComponent = (BasicComponent) rewireInternalTrace.getSource().get(0);
			OperationRequiredRole requiredRole = (OperationRequiredRole) rewireInternalTrace.getTarget().get(0);
			OperationInterface operationInterface = requiredRole.getRequiredInterface__OperationRequiredRole();
			//There must be one and only one providing split component
			BasicComponent providingSplitComponent = this.searchProvidingComponents(repositoryRoot, originalComponent, operationInterface).get(0);
			OperationProvidedRole providedRole = this.searchProvidedRole(providingSplitComponent, operationInterface);
			List<AssemblyContext> requiringSplitAssemblyContexts = RunnerHelper.findAssemblyContexts(requiringSplitComponent, systemRoot);
			List<AssemblyContext> providingSplitAssemblyContexts = RunnerHelper.findAssemblyContexts(providingSplitComponent, systemRoot);
			for(AssemblyContext requiringSplitAssemblyContext : requiringSplitAssemblyContexts) {
				for(AssemblyContext providingSplitAssemblyContext : providingSplitAssemblyContexts) {
					AssemblyConnector newConnector = CompositionFactory.eINSTANCE.createAssemblyConnector();
					newConnector.setEntityName(RunnerHelper.getAssemblyConnectorName(requiringSplitAssemblyContext, providingSplitAssemblyContext));
					newConnector.setProvidingAssemblyContext_AssemblyConnector(providingSplitAssemblyContext);
					newConnector.setRequiringAssemblyContext_AssemblyConnector(requiringSplitAssemblyContext);
					newConnector.setProvidedRole_AssemblyConnector(providedRole);
					newConnector.setRequiredRole_AssemblyConnector(requiredRole);
					connectorsToAdd.add(newConnector);
					Trace connectorTrace = TraceFactory.eINSTANCE.createTrace();
					connectorTrace.setName(TRACE_AssemblyConnector);
					//connectorTrace.getSource().add(null);
					connectorTrace.getTarget().add((EObject) newConnector);
					traceRoot.getSubTraces().add(connectorTrace);
				}
			}
		}
		//Add the new connectors
		systemRoot.getConnectors__ComposedStructure().addAll(connectorsToAdd);
		//Fix the allocations contexts
		Allocation allocationRoot = RunnerHelper.getAllocationRoot(graph);
		List<AllocationContext> allocationsToAdd = new ArrayList<AllocationContext>();
		for(Trace assemblyContextTrace : assemblyContextTraces) {
			AssemblyContext oldAssemblyContext = (AssemblyContext) assemblyContextTrace.getSource().get(0);
			AssemblyContext newAssemblyContext = (AssemblyContext) assemblyContextTrace.getTarget().get(0);
			Iterator<AllocationContext> allocationContexts = allocationRoot.getAllocationContexts_Allocation().iterator();
			while(allocationContexts.hasNext()) {
				AllocationContext oldAllocationContext = allocationContexts.next();
				if(oldAllocationContext.getAssemblyContext_AllocationContext().equals(oldAssemblyContext)) {
					AllocationContext newAllocationContext = AllocationFactory.eINSTANCE.createAllocationContext();
					newAllocationContext.setEntityName(RunnerHelper.getAllocationContextName(newAssemblyContext));
					newAllocationContext.setAssemblyContext_AllocationContext(newAssemblyContext);
					newAllocationContext.setResourceContainer_AllocationContext(oldAllocationContext.getResourceContainer_AllocationContext());
					allocationsToAdd.add(newAllocationContext);
					Trace allocationTrace = TraceFactory.eINSTANCE.createTrace();
					allocationTrace.setName(TRACE_AllocationContext);
					allocationTrace.getSource().add((EObject) oldAllocationContext);
					allocationTrace.getTarget().add((EObject) newAllocationContext);
					traceRoot.getSubTraces().add(allocationTrace);
				}
			}
		}
		//Add the new allocations
		allocationRoot.getAllocationContexts_Allocation().addAll(allocationsToAdd);
	}
	
	private OperationProvidedRole searchProvidedRole(BasicComponent component, OperationInterface operationInterface) {
		for(ProvidedRole providedRole : component.getProvidedRoles_InterfaceProvidingEntity()) {
			OperationProvidedRole operationProvidedRole = (OperationProvidedRole) providedRole;
			if(operationProvidedRole.getProvidedInterface__OperationProvidedRole().equals(operationInterface))
				return operationProvidedRole;
		}
		return null;
	}
	
	private List<BasicComponent> searchProvidingComponents(Repository repository, BasicComponent originalComponent, OperationInterface operationInterface) {
		List<BasicComponent> providingComponents = new ArrayList<BasicComponent>();
		Iterator<RepositoryComponent> components = repository.getComponents__Repository().iterator();
		while(components.hasNext()) {
			BasicComponent component = (BasicComponent) components.next();
			if(this.providesInterface(component, operationInterface))
				providingComponents.add(component);
		}
		providingComponents.remove(originalComponent);
		return providingComponents;
	}
	
	private OperationProvidedRole searchNewProvidedRole(BasicComponent oldComponent, BasicComponent newComponent, OperationProvidedRole oldOperationProvidedRole) {
		for(ProvidedRole newProvidedRole : newComponent.getProvidedRoles_InterfaceProvidingEntity()) {
			if(newProvidedRole instanceof OperationProvidedRole) {
				OperationProvidedRole newOperationProvidedRole = (OperationProvidedRole) newProvidedRole;
				if(newOperationProvidedRole.getProvidedInterface__OperationProvidedRole().equals(oldOperationProvidedRole.getProvidedInterface__OperationProvidedRole()))
					return newOperationProvidedRole;
			}
		}
		return null;
	}
	
	private OperationRequiredRole searchNewRequiredRole(BasicComponent oldComponent, BasicComponent newComponent, OperationRequiredRole oldOperationRequiredRole) {
		for(RequiredRole newRequiredRole : newComponent.getRequiredRoles_InterfaceRequiringEntity()) {
			if(newRequiredRole instanceof OperationRequiredRole) {
				OperationRequiredRole newOperationRequiredRole = (OperationRequiredRole) newRequiredRole;
				if(newOperationRequiredRole.getRequiredInterface__OperationRequiredRole().equals(oldOperationRequiredRole.getRequiredInterface__OperationRequiredRole()))
					return newOperationRequiredRole;
			}
		}
		return null;
	}
	
	private boolean providesInterface(BasicComponent component, OperationInterface oInterface) {
		for(ProvidedRole providedRole : component.getProvidedRoles_InterfaceProvidingEntity())
			if(providedRole instanceof OperationProvidedRole) {
				OperationProvidedRole operationProvidedRole = (OperationProvidedRole) providedRole;
				if(operationProvidedRole.getProvidedInterface__OperationProvidedRole().equals(oInterface))
					return true;
			}
		return false;
	}
	
	private void runLastRule(EGraph graph) {
		Trace root = RunnerHelper.getTraceRoot(graph);
		Trace splittable = RunnerHelper.getTraces(root, TRACE_Splittable, false).get(0);
		BasicComponent oldComponent = (BasicComponent) splittable.getSource().get(0);
		EcoreUtil.delete((EObject) oldComponent, true);
		//Removing all the traces
		List<Trace> assemblyContexts = RunnerHelper.getTraces(root, TRACE_AssemblyContext, true);
		List<Trace> assemblyConnectors = RunnerHelper.getTraces(root, TRACE_AssemblyConnector, true);
		List<Trace> allocationContexts = RunnerHelper.getTraces(root, TRACE_AllocationContext, true);
		for(Trace trace : allocationContexts) {
			AllocationContext oldAllocationContext = (AllocationContext) trace.getSource().get(0);
			EcoreUtil.delete((EObject) oldAllocationContext, false);
		}
		for(Trace trace : assemblyConnectors) {
			if(trace.getSource().size() > 0) {
				EObject oldAssemblyConnector = (EObject) trace.getSource().get(0);
				EcoreUtil.delete((EObject) oldAssemblyConnector, false);
			}
		}
		for(Trace trace : assemblyContexts) {
			AssemblyContext oldAssemblyContext = (AssemblyContext) trace.getSource().get(0);
			EcoreUtil.delete((EObject) oldAssemblyContext, false);
		}
		//EcoreUtil.delete((EObject) root, true);
	}
	
	public static void main(String[] args) {
		String dirPath = "src/edu/squat/transformations/modifiability/splitrespn";
		String henshinFilename = "splitrespn-modular.henshin";
		String repositoryFilename, systemFilename, resourceEnvironmentFilename, allocationFilename, usageFilename;
		String resultRepositoryFilename, resultSystemFilename, resultResourceEnvironmentFilename, resultAllocationFilename, resultUsageFilename;

		SplitRespNRunner runner = new SplitRespNRunner(true);
		
		//Dual testing
		repositoryFilename = "split-dual.repository";
		resultRepositoryFilename = "split-dual-" + "#REPLACEMENT#" + ".repository";
		//runner.run(dirPath, repositoryFilename, henshinFilename, resultRepositoryFilename, true);
		
		//Complete dual testing
		repositoryFilename = "split-dual.repository";
		systemFilename = "split-dual.system";
		resourceEnvironmentFilename = "split-dual.resourceenvironment";
		allocationFilename = "split-dual.allocation";
		resultRepositoryFilename = "split-dual-" + "#REPLACEMENT#" + ".repository";
		resultSystemFilename = "split-dual-" + "#REPLACEMENT#" + ".system";
		resultResourceEnvironmentFilename = "split-dual-" + "#REPLACEMENT#" + ".resourceenvironment";
		resultAllocationFilename = "split-dual-" + "#REPLACEMENT#" + ".allocation";
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

package edu.squat.transformations.modifiability;

import java.util.List;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.henshin.interpreter.ApplicationMonitor;
import org.eclipse.emf.henshin.interpreter.EGraph;
import org.eclipse.emf.henshin.interpreter.Match;
import org.eclipse.emf.henshin.interpreter.impl.EGraphImpl;
import org.eclipse.emf.henshin.interpreter.impl.LoggingApplicationMonitor;
import org.eclipse.emf.henshin.model.HenshinPackage;
import org.eclipse.emf.henshin.model.Module;
import org.eclipse.emf.henshin.model.resource.HenshinResourceSet;
import org.eclipse.emf.henshin.trace.TracePackage;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationPackage;
import org.palladiosimulator.pcm.core.CorePackage;
import org.palladiosimulator.pcm.core.entity.EntityPackage;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryPackage;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentPackage;
import org.palladiosimulator.pcm.seff.SeffPackage;
import org.palladiosimulator.pcm.system.SystemPackage;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.palladiosimulator.pcm.usagemodel.UsagemodelPackage;

import de.uka.ipd.sdq.identifier.IdentifierPackage;
import de.uka.ipd.sdq.stoex.StoexPackage;
import edu.squat.pcm.PCMHelper;
import edu.squat.transformations.ArchitecturalVersion;

public abstract class PCMTransformerRunner {
	protected String dirPath;
	//
	protected String repositoryFilename;
	protected String systemFilename;
	protected String resourceEnvironmentFilename;
	protected String allocationFilename;
	protected String usageFilename;
	//
	protected String henshinFilename;
	//
	protected String resultRepositoryFilename;
	protected String resultSystemFilename;
	protected String resultResourceEnvironmentFilename;
	protected String resultAllocationFilename;
	protected String resultUsageFilename;
	//
	protected HenshinResourceSet resourceSet;
	protected Module module;
	protected EGraph graph;
	//
	protected Repository repository;
	protected org.palladiosimulator.pcm.system.System system;
	protected ResourceEnvironment resourceEnvironment;
	protected Allocation allocation;
	protected UsageModel usage;
	//
	protected boolean performanceModelsLoaded = false;
	protected boolean usageModelLoaded = false;
	protected List<Tactic> candidateTactics;
	//
	protected ApplicationMonitor monitor;
	//
	static {
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
		//PCM Packages
		EPackage.Registry.INSTANCE.put(IdentifierPackage.eNS_URI, IdentifierPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(StoexPackage.eNS_URI, StoexPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(CorePackage.eNS_URI, CorePackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(EntityPackage.eNS_URI, EntityPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(RepositoryPackage.eNS_URI, RepositoryPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(SystemPackage.eNS_URI, SystemPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(ResourceenvironmentPackage.eNS_URI, ResourceenvironmentPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(AllocationPackage.eNS_URI, AllocationPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(UsagemodelPackage.eNS_URI, UsagemodelPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(SeffPackage.eNS_URI, SeffPackage.eINSTANCE);
		//Henshin Packages
		EPackage.Registry.INSTANCE.put(HenshinPackage.eNS_URI, HenshinPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(TracePackage.eNS_URI, TracePackage.eINSTANCE);
		//EPackage.Registry.INSTANCE.put(StateSpacePackage.eNS_URI, StateSpacePackage.eINSTANCE);
		//EPackage.Registry.INSTANCE.put(WrapPackage.eNS_URI, WrapPackage.eINSTANCE);
	}
	//
	public PCMTransformerRunner() {
		monitor = new LoggingApplicationMonitor();
		((LoggingApplicationMonitor) monitor).setLogStream(System.out);
		resourceSet=null;
	}
	
	public boolean arePerformanceModelsLoaded() {
		return performanceModelsLoaded;
	}
	
	public boolean isUsageModelLoaded() {
		return usageModelLoaded;
	}
	
	private void setParameters(String dirPath, 
			String repositoryFilename, 
			String henshinFilename, 
			String resultRepositoryFilename) {
		this.setParameters(dirPath, 
				repositoryFilename, null, null, null, null,
				henshinFilename, 
				resultRepositoryFilename, null, null, null, null);
	}
	
	private void setParameters(String dirPath, 
			String repositoryFilename, String systemFilename, String resourceEnvironmentFilename, String allocationFilename, 
			String henshinFilename, 
			String resultRepositoryFilename, String resultSystemFilename, String resultResourceEnvironmentFilename, String resultAllocationFilename) {
		this.setParameters(dirPath, 
				repositoryFilename, systemFilename, resourceEnvironmentFilename, allocationFilename, null,
				henshinFilename, 
				resultRepositoryFilename, resultSystemFilename, resultResourceEnvironmentFilename, resultAllocationFilename, null);
	}
	
	private void setParameters(String dirPath, 
			String repositoryFilename, String systemFilename, String resourceEnvironmentFilename, String allocationFilename, String usageFilename, 
			String henshinFilename, 
			String resultRepositoryFilename, String resultSystemFilename, String resultResourceEnvironmentFilename, String resultAllocationFilename, String resultUsageFilename) {
		this.dirPath = dirPath;
		this.repositoryFilename = repositoryFilename;
		this.systemFilename = systemFilename;
		this.resourceEnvironmentFilename = resourceEnvironmentFilename;
		this.allocationFilename = allocationFilename;
		this.usageFilename = usageFilename;
		this.henshinFilename = henshinFilename;
		this.resultRepositoryFilename = resultRepositoryFilename;
		this.resultSystemFilename = resultSystemFilename;
		this.resultResourceEnvironmentFilename = resultResourceEnvironmentFilename;
		this.resultAllocationFilename = resultAllocationFilename;
		this.resultUsageFilename = resultUsageFilename;
	}
	public void setResourceSet(HenshinResourceSet resourceSet) {
		this.resourceSet = resourceSet;
	}
	
	private void loadModels() {
		// Create a resource set with a base directory
		if(resourceSet==null)
			resourceSet = new HenshinResourceSet(dirPath);
		// Load the module
		module = resourceSet.getModule(henshinFilename, false);
		// Load the example model into an EGraph
		//graph = new EGraphImpl(resourceSet.getResource(repositoryFilename));
		graph = new EGraphImpl();
		graph.addGraph(resourceSet.getResource(repositoryFilename).getContents().get(0));
		repository = PCMHelper.loadRepositoryModel(dirPath + "/" + repositoryFilename);
		if(systemFilename != null && !systemFilename.isEmpty()) {
			graph.addGraph(resourceSet.getResource(systemFilename).getContents().get(0));
			system = PCMHelper.loadSystemModel(dirPath + "/" + systemFilename);
		}
		if(resourceEnvironmentFilename != null && !resourceEnvironmentFilename.isEmpty()) {
			graph.addGraph(resourceSet.getResource(resourceEnvironmentFilename).getContents().get(0));
			resourceEnvironment = PCMHelper.loadResourceEnvironmentModel(dirPath + "/" + resourceEnvironmentFilename);
		}
		if(allocationFilename != null && !allocationFilename.isEmpty()) {
			graph.addGraph(resourceSet.getResource(allocationFilename).getContents().get(0));
			allocation = PCMHelper.loadAllocationModel(dirPath + "/" + allocationFilename);
		}
		if(usageFilename != null && !usageFilename.isEmpty()) {
			graph.addGraph(resourceSet.getResource(usageFilename).getContents().get(0));
			usage = PCMHelper.loadUsageModel(dirPath + "/" + usageFilename);
		}
	}
	
	public void loadModelsAndRules(String dirPath, String repositoryFilename, String henshinFilename, String resultFilename) {
		this.performanceModelsLoaded = false;
		this.usageModelLoaded = false;
		this.setParameters(dirPath, 
				repositoryFilename, 
				henshinFilename, 
				resultFilename);
		this.loadModels();
		this.loadRules();
	}
	
	public void loadModelsAndRules(String dirPath, 
			String repositoryFilename, String systemFilename, String resourceEnvironmentFilename, String allocationFilename,
			String henshinFilename, 
			String resultRepositoryFilename, String resultSystemFilename, String resultResourceEnvironmentFilename, String resultAllocationFilename) {
		this.performanceModelsLoaded = true;
		this.usageModelLoaded = false;
		this.setParameters(dirPath, 
				repositoryFilename, systemFilename, resourceEnvironmentFilename, allocationFilename,
				henshinFilename, 
				resultRepositoryFilename, resultSystemFilename, resultResourceEnvironmentFilename, resultAllocationFilename);
		this.loadModels();
		this.loadRules();
	}
	
	public void loadModelsAndRules(String dirPath, 
			String repositoryFilename, String systemFilename, String resourceEnvironmentFilename, String allocationFilename, String usageFilename,
			String henshinFilename, 
			String resultRepositoryFilename, String resultSystemFilename, String resultResourceEnvironmentFilename, String resultAllocationFilename, String resultUsageFilename) {
		this.performanceModelsLoaded = true;
		this.usageModelLoaded = true;
		this.setParameters(dirPath, 
				repositoryFilename, systemFilename, resourceEnvironmentFilename, allocationFilename, usageFilename, 
				henshinFilename, 
				resultRepositoryFilename, resultSystemFilename, resultResourceEnvironmentFilename, resultAllocationFilename, resultUsageFilename);
		this.loadModels();
		this.loadRules();
	}
	
	public abstract void loadRules();

	public abstract List<ArchitecturalVersion> run(boolean saveResult);
	
	public void run(String dirPath, 
			String repositoryFilename, 
			String henshinFilename, 
			String resultRepositoryFilename, 
			boolean saveResult) {
		this.loadModelsAndRules(dirPath, 
				repositoryFilename, 
				henshinFilename, 
				resultRepositoryFilename);
		this.run(saveResult);
	}
	
	public void run(String dirPath, 
			String repositoryFilename, String systemFilename, String resourceEnvironmentFilename, String allocationFilename,
			String henshinFilename, 
			String resultRepositoryFilename, String resultSystemFilename, String resultResourceEnvironmentFilename, String resultAllocationFilename,
			boolean saveResult) {
		this.loadModelsAndRules(dirPath, 
				repositoryFilename, systemFilename, resourceEnvironmentFilename, allocationFilename,
				henshinFilename, 
				resultRepositoryFilename, resultSystemFilename, resultResourceEnvironmentFilename, resultAllocationFilename);
		this.run(saveResult);
	}
	

	public List<ArchitecturalVersion> run(String dirPath, 
			String repositoryFilename, String systemFilename, String resourceEnvironmentFilename, String allocationFilename, String usageFilename,
			String henshinFilename, 
			String resultRepositoryFilename, String resultSystemFilename, String resultResourceEnvironmentFilename, String resultAllocationFilename, String resultUsageFilename,
			boolean saveResult) {
		this.loadModelsAndRules(dirPath, 
				repositoryFilename, systemFilename, resourceEnvironmentFilename, allocationFilename, usageFilename,
				henshinFilename, 
				resultRepositoryFilename, resultSystemFilename, resultResourceEnvironmentFilename, resultAllocationFilename, resultUsageFilename);
		return this.run(saveResult);
	}
	
	protected void addTactic(BasicComponent seed, EGraph tempGraph, Match match) {
		Tactic tactic = new Tactic();
		tactic.initialArchitecture = graph;
		tactic.seed = seed;
		tactic.seedName = seed != null ? seed.getEntityName() : "" ;
		tactic.resultingArchitecture = tempGraph;
		tactic.initialMatch = match;
		candidateTactics.add(tactic);
	}
}

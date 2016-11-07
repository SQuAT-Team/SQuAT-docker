package io.github.squat_team.model;

import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.usagemodel.UsageModel;

public class PCMArchitectureInstance {
	private String name;
	private Repository repository;
	private System system;
	private Allocation allocation;
	private ResourceEnvironment resourceEnvironment;
	private UsageModel usageModel;
	
	public PCMArchitectureInstance(String name, 
			Repository repository, System system, 
			Allocation allocation, ResourceEnvironment resourceEnvironment,
			UsageModel usageModel) {
		super();
		this.name = name;
		this.repository = repository;
		this.system = system;
		this.allocation = allocation;
		this.resourceEnvironment = resourceEnvironment;
		this.usageModel = usageModel;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Repository getRepository() {
		return repository;
	}

	public void setRepository(Repository repository) {
		this.repository = repository;
	}

	public System getSystem() {
		return system;
	}

	public void setSystem(System system) {
		this.system = system;
	}

	public Allocation getAllocation() {
		return allocation;
	}

	public void setAllocation(Allocation allocation) {
		this.allocation = allocation;
	}

	public ResourceEnvironment getResourceEnvironment() {
		return resourceEnvironment;
	}

	public void setResourceEnvironment(ResourceEnvironment resourceEnvironment) {
		this.resourceEnvironment = resourceEnvironment;
	}

	public UsageModel getUsageModel() {
		return usageModel;
	}

	public void setUsageModel(UsageModel usageModel) {
		this.usageModel = usageModel;
	}

	public void delete() {
		EcoreUtil.delete(this.getRepository(), true);
		EcoreUtil.delete(this.getSystem(), true);
		EcoreUtil.delete(this.getAllocation(), true);
		EcoreUtil.delete(this.getResourceEnvironment(), true);
		EcoreUtil.delete(this.getUsageModel(), true);
	}
	
	public Object clone() {
		Collection<EObject> collection = new ArrayList<EObject>();
		collection.add(this.getRepository());
		collection.add(this.getSystem());
		collection.add(this.getAllocation());
		collection.add(this.getResourceEnvironment());
		collection.add(this.getUsageModel());
		Collection<EObject> clonedCollection = EcoreUtil.copyAll(collection);
		List<EObject> clonedList = new ArrayList<EObject>(clonedCollection);
		PCMArchitectureInstance clone = new PCMArchitectureInstance(
				this.getName(), 
				(Repository) clonedList.get(0), 
				(System) clonedList.get(1), 
				(Allocation) clonedList.get(2), 
				(ResourceEnvironment) clonedList.get(3), 
				(UsageModel) clonedList.get(4)
		);
		return clone;
	}
}

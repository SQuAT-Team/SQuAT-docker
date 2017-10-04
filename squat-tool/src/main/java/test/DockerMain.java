package test;

import java.io.IOException;

import org.json.JSONException;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.palladiosimulator.pcm.allocation.AllocationPackage;
import org.palladiosimulator.pcm.repository.RepositoryPackage;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentPackage;
import org.palladiosimulator.pcm.system.SystemPackage;
import org.palladiosimulator.pcm.usagemodel.UsagemodelPackage;

import io.github.squat_team.json.JSONification;
import io.github.squat_team.model.PCMArchitectureInstance;
import io.github.squat_team.util.SQuATHelper;

@SpringBootApplication
public class DockerMain {

	public static void register() {
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
		EPackage.Registry.INSTANCE.put(RepositoryPackage.eNS_URI, RepositoryPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(ResourceenvironmentPackage.eNS_URI, ResourceenvironmentPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(SystemPackage.eNS_URI, SystemPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(AllocationPackage.eNS_URI, AllocationPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(UsagemodelPackage.eNS_URI, UsagemodelPackage.eINSTANCE);
	}

	public static void main(String[] args) throws IOException, JSONException {
		register();
//		Allocation allocation = SQuATHelper.loadAllocationModel(
//				"/home/roehrdor/Workspace.neon/headless-peropteryx/ExtendedSimpleTacticsExample/default.allocation");
//		Repository repository = SQuATHelper.loadRepositoryModel(
//				"/home/roehrdor/Workspace.neon/headless-peropteryx/ExtendedSimpleTacticsExample/default.repository");
//		org.palladiosimulator.pcm.system.System system = SQuATHelper.loadSystemModel(
//				"/home/roehrdor/Workspace.neon/headless-peropteryx/ExtendedSimpleTacticsExample/default.system");
//		ResourceEnvironment resourceenvironment = SQuATHelper.loadResourceEnvironmentModel(
//				"/home/roehrdor/Workspace.neon/headless-peropteryx/ExtendedSimpleTacticsExample/default.resourceenvironment");
//		UsageModel usageModel = SQuATHelper.loadUsageModel(
//				"/home/roehrdor/Workspace.neon/headless-peropteryx/ExtendedSimpleTacticsExample/default.usagemodel");
//
//		PCMArchitectureInstance architectureInstance = new PCMArchitectureInstance("TEST", repository, system,
//				allocation, resourceenvironment, usageModel);
//
//		JSONification jsoNification = new JSONification();
//		jsoNification.add(architectureInstance);
//		String res = jsoNification.toJSON();
//		System.out.println(res);
		SQuATMain.mainFn(args);
		SpringApplication.run(DockerMain.class, args);
	}
}

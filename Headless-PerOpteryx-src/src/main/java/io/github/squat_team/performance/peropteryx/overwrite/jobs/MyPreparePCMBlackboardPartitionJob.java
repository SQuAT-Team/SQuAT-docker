package io.github.squat_team.performance.peropteryx.overwrite.jobs;

import java.util.concurrent.Semaphore;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.opt4j.common.completer.ParallelIndividualCompleter;
import org.palladiosimulator.analyzer.workflow.blackboard.PCMResourceSetPartition;
import org.palladiosimulator.analyzer.workflow.configurations.AbstractPCMWorkflowRunConfiguration;

import de.uka.ipd.sdq.workflow.jobs.CleanupFailedException;
import de.uka.ipd.sdq.workflow.jobs.IBlackboardInteractingJob;
import de.uka.ipd.sdq.workflow.jobs.IJob;
import de.uka.ipd.sdq.workflow.jobs.JobFailedException;
import de.uka.ipd.sdq.workflow.jobs.UserCanceledException;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.MDSDBlackboard;
import io.github.squat_team.performance.peropteryx.environment.PalladioEclipseEnvironment;

/**
 * Creates and fills the PCM model partition.
 * 
 * @author Sebastian Lehrig
 */
public class MyPreparePCMBlackboardPartitionJob implements IJob, IBlackboardInteractingJob<MDSDBlackboard> {
	private static Semaphore semaphore = new Semaphore(1);
    private static final Logger LOGGER = Logger.getLogger(MyPreparePCMBlackboardPartitionJob.class);
    private static boolean init = true;
    private MDSDBlackboard blackboard;

    public static final URI PCM_PALLADIO_RESOURCE_TYPE_URI = URI
            .createURI("pathmap://PCM_MODELS/Palladio.resourcetype");
    public static final URI PCM_PALLADIO_PRIMITIVE_TYPE_REPOSITORY_URI = URI
            .createURI("pathmap://PCM_MODELS/PrimitiveTypes.repository");

    @Override
    public void execute(final IProgressMonitor monitor) throws JobFailedException, UserCanceledException {
    	try {
			semaphore.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Creating PCM Model Partition");
        }
        PCMResourceSetPartition pcmPartition = new MyPCMResourceSetPartition();
	    this.blackboard.addPartition(MyLoadPCMModelsIntoBlackboardJob.PCM_MODELS_PARTITION_ID, pcmPartition);

	    if (LOGGER.isDebugEnabled()) {
	         LOGGER.debug("Initializing PCM EPackages");
	    }
	    pcmPartition.getResourceSet().setURIConverter(PalladioEclipseEnvironment.INSTANCE.getUriConverter()); //ADDED
	    pcmPartition.initialiseResourceSetEPackages(AbstractPCMWorkflowRunConfiguration.PCM_EPACKAGES);
   
	    pcmPartition.loadModel(PCM_PALLADIO_PRIMITIVE_TYPE_REPOSITORY_URI);
	    pcmPartition.loadModel(PCM_PALLADIO_RESOURCE_TYPE_URI);

        
        if (!init) {
			for (int i = 0; i < ParallelIndividualCompleter.maxThreads; i++) {
				pcmPartition = new MyPCMResourceSetPartition();
				this.blackboard.addPartition(MyLoadPCMModelsIntoBlackboardJob.PCM_MODELS_PARTITION_ID + "_" + Integer.toString(i), pcmPartition);
			    pcmPartition.getResourceSet().setURIConverter(PalladioEclipseEnvironment.INSTANCE.getUriConverter()); //ADDED
				pcmPartition.initialiseResourceSetEPackages(AbstractPCMWorkflowRunConfiguration.PCM_EPACKAGES);
				pcmPartition.loadModel(PCM_PALLADIO_PRIMITIVE_TYPE_REPOSITORY_URI);
				pcmPartition.loadModel(PCM_PALLADIO_RESOURCE_TYPE_URI);
			}
		} else {
			
			init = !init;
		}
        semaphore.release();
    }

    @Override
    public String getName() {
        return "Prepare PCM Blackboard Partions";
    }

    @Override
    public void cleanup(final IProgressMonitor monitor) throws CleanupFailedException {
        this.blackboard.removePartition(MyLoadPCMModelsIntoBlackboardJob.PCM_MODELS_PARTITION_ID);
        
        this.blackboard.removePartition(MyLoadPCMModelsIntoBlackboardJob.PCM_MODELS_PARTITION_ID + "_0");
		this.blackboard.removePartition(MyLoadPCMModelsIntoBlackboardJob.PCM_MODELS_PARTITION_ID + "_1");
		this.blackboard.removePartition(MyLoadPCMModelsIntoBlackboardJob.PCM_MODELS_PARTITION_ID + "_2");
		this.blackboard.removePartition(MyLoadPCMModelsIntoBlackboardJob.PCM_MODELS_PARTITION_ID + "_3");
		
    }

    @Override
    public void setBlackboard(final MDSDBlackboard blackboard) {
        this.blackboard = blackboard;
    }

}

package io.github.squat_team.performance.peropteryx.overwrite.jobs;

import java.util.concurrent.Semaphore;

import org.eclipse.core.runtime.IProgressMonitor;
import org.palladiosimulator.analyzer.workflow.blackboard.PCMResourceSetPartition;
import org.palladiosimulator.analyzer.workflow.configurations.AbstractPCMWorkflowRunConfiguration;
import org.palladiosimulator.analyzer.workflow.jobs.LoadPCMModelsJob;

import de.uka.ipd.sdq.workflow.jobs.IBlackboardInteractingJob;
import de.uka.ipd.sdq.workflow.jobs.IJob;
import de.uka.ipd.sdq.workflow.jobs.JobFailedException;
import de.uka.ipd.sdq.workflow.jobs.SequentialBlackboardInteractingJob;
import de.uka.ipd.sdq.workflow.jobs.UserCanceledException;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.MDSDBlackboard;

/**
 * A job to be used in the SDQ workflow engine which fully loads a PCM model
 * instance into different MDSDBlackboard partitions. The first partition
 * contains the plain PCM model instance, the second one contains parametric
 * middleware completion components, etc.
 * 
 * @author Steffen Becker, Sebastian Lehrig
 */
public class MyLoadPCMModelsIntoBlackboardJob extends SequentialBlackboardInteractingJob<MDSDBlackboard>
		implements IJob, IBlackboardInteractingJob<MDSDBlackboard> {

	private static Semaphore semaphore = new Semaphore(1);

	/**
	 * ID of the blackboard partition containing the fully loaded PCM instance.
	 * The blackboard partition is ensured to be of type
	 * {@link PCMResourceSetPartition}
	 */
	public static final String PCM_MODELS_PARTITION_ID = "org.palladiosimulator.pcmmodels.partition";

	public static int getThreadId() {
		String name = Thread.currentThread().getName();
		return Integer.parseInt(name.substring(name.lastIndexOf('-') + 1)) - 1;
	}

	public static String getCurrentThreadIdentifier() {
		return Integer.toString(getThreadId());
	}

	/**
	 * Constructor of the PCM loader job
	 * 
	 * @param config
	 *            A PCM workflow configuration containing the list of URIs where
	 *            to find the PCM model files
	 */
	public MyLoadPCMModelsIntoBlackboardJob(final AbstractPCMWorkflowRunConfiguration config) {
		super(false);
		this.add(new MyPreparePCMBlackboardPartitionJob());
		this.add(new LoadPCMModelsJob(config));
	}

	@Override
	public void execute(IProgressMonitor monitor) throws JobFailedException, UserCanceledException {
		try {
			semaphore.acquire();
			super.execute(monitor);
			semaphore.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

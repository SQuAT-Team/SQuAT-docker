package io.github.squat_team.performance.peropteryx.overwrite.jobs;

import java.util.concurrent.Semaphore;

import org.eclipse.core.runtime.IProgressMonitor;

import de.uka.ipd.sdq.dsexplore.launch.MoveInitialPCMModelPartitionJob;
import de.uka.ipd.sdq.workflow.jobs.JobFailedException;
import de.uka.ipd.sdq.workflow.jobs.UserCanceledException;

public class MyMoveInitialPCMModelPartitionJob extends MoveInitialPCMModelPartitionJob{

	private static Semaphore semaphore = new Semaphore(1);
	
	@Override
	public void execute(IProgressMonitor monitor) throws JobFailedException, UserCanceledException{
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

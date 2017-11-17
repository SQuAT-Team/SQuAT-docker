package io.github.squat_team.performance.peropteryx.overwrite.analysis;

import java.util.Map;

import org.opt4j.core.Criterion;
import org.palladiosimulator.solver.lqn.LqnModelType;
import org.palladiosimulator.solver.models.PCMInstance;

import de.uka.ipd.sdq.dsexplore.analysis.AnalysisFailedException;
import de.uka.ipd.sdq.dsexplore.analysis.IStatisticAnalysisResult;
import de.uka.ipd.sdq.dsexplore.qml.pcm.datastructures.EvaluationAspectWithContext;
import de.uka.ipd.sdq.statistics.estimation.ConfidenceInterval;

/**
 * This class represents the result of a LQN Solver analysis. 
 * 
 * @author pmerkle
 *
 */
public class MyLQNSolverAnalysisResult extends MyLQNResult implements IStatisticAnalysisResult {
	

	public MyLQNSolverAnalysisResult(LqnModelType model, PCMInstance pcm, 
			Criterion criterion, Map<Criterion, EvaluationAspectWithContext> objectiveToAspect,
			MyLQNQualityAttributeDeclaration qualityAttributeInfo) throws AnalysisFailedException {

		super(pcm, model, criterion, objectiveToAspect, qualityAttributeInfo);

	}
	
	public ConfidenceInterval getConfidenceInterval(Criterion criterion) {
		return null;
	}



	public long getNumberOfObservations() {
		return 0;
	}

}



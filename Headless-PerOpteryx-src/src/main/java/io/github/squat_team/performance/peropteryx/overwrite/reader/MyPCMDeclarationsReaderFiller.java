package io.github.squat_team.performance.peropteryx.overwrite.reader;

import org.opt4j.core.SatisfactionConstraint;
import org.opt4j.core.Constraint.Direction;
import org.opt4j.core.Objective.Sign;

import de.uka.ipd.sdq.dsexplore.qml.contract.QMLContract.EnumOperator;
import de.uka.ipd.sdq.dsexplore.qml.contract.QMLContract.EvaluationAspect;
import de.uka.ipd.sdq.dsexplore.qml.contract.QMLContract.Goal;
import de.uka.ipd.sdq.dsexplore.qml.contract.QMLContract.NumericLiteral;
import de.uka.ipd.sdq.dsexplore.qml.contract.QMLContract.Restriction;
import de.uka.ipd.sdq.dsexplore.qml.contracttype.QMLContractType.EnumRelationSemantics;
import de.uka.ipd.sdq.dsexplore.qml.pcm.datastructures.EvaluationAspectWithContext;
import de.uka.ipd.sdq.dsexplore.qml.pcm.datastructures.builder.InfeasibilityConstraintBuilder;
import de.uka.ipd.sdq.dsexplore.qml.pcm.datastructures.builder.ObjectiveBuilder;
import de.uka.ipd.sdq.dsexplore.qml.pcm.datastructures.builder.SatisfactionConstraintBuilder;
import de.uka.ipd.sdq.dsexplore.qml.pcm.reader.PCMDeclarationsReader;


/**
 * 
 * Only use static methods!
 *
 */
public class MyPCMDeclarationsReaderFiller extends PCMDeclarationsReader{

	public MyPCMDeclarationsReaderFiller(String PCMProfilePath) {
		super(PCMProfilePath);
		// TODO Auto-generated constructor stub
	}

	/*
	 * The following methods are used to transform the QML definitions to 
	 * criteria objects that can be used for the optimization. Always get it translated here as it 
	 * won't be reversible otherwise.
	 */
	
	public static org.opt4j.core.InfeasibilityConstraint staticTranslateEvalAspectToInfeasibilityConstraint(EvaluationAspectWithContext aspectContext, InfeasibilityConstraintBuilder builder) {
		EvaluationAspect aspect = aspectContext.getEvaluationAspect();
		org.opt4j.core.InfeasibilityConstraint constraint;
		if(aspect.getAspectRequirement() instanceof Restriction){		
			if (((Restriction)aspect.getAspectRequirement()).getOperator() == EnumOperator.LESS) { 		
				if (((Restriction)aspect.getAspectRequirement()).getAspectRequirementLiteral() instanceof NumericLiteral) {
					constraint = builder.createInfeasibilityConstraint(aspect.getId(), 
							Direction.less, 
							((NumericLiteral)((Restriction)aspect.getAspectRequirement()).getAspectRequirementLiteral()).getValue());
				} else {
					//TODO: Handle Enums and Sets
					throw new RuntimeException("Unsupported Constraint literal in aspect. Only numeric literals are supported so far.");
				}
			} else {
				// TODO: Extend and remove Exception
				throw new RuntimeException("Unsupported constraint operator in aspect. Only LESS (<) supported so far.");
			}
		} else {
			throw new RuntimeException("Aspect must have aspect requirement of type Restriction to derive InfeasibilityConstraint.");
		}
		
		retranslationMap.put(constraint.getName(), aspectContext);
		return constraint;
	}
	
	public static SatisfactionConstraint staticTranslateEvalAspectToSatisfactionConstraint(EvaluationAspectWithContext aspectContext, org.opt4j.core.Objective objective, SatisfactionConstraintBuilder builder){
		EvaluationAspect aspect = aspectContext.getEvaluationAspect();
		SatisfactionConstraint constraint = null;
		if (((Goal)aspect.getAspectRequirement()) == null) {
			if(objective.getSign() == Sign.MIN) {
				constraint = builder.createSatisfactionConstraint(
						aspect.getId(), 
						Direction.less, 
						Double.NEGATIVE_INFINITY, 
						objective);
			} else {
				//Sign == MAX
				constraint = builder.createSatisfactionConstraint(
						aspect.getId(), 
						Direction.greater, 
						Double.POSITIVE_INFINITY, 
						objective);
			}
		} else if(aspect.getAspectRequirement() instanceof Goal){			
			if (((Goal)aspect.getAspectRequirement()).getAspectRequirementLiteral() instanceof NumericLiteral) {
				if(objective.getSign() == Sign.MIN) {
					constraint = builder.createSatisfactionConstraint(
							aspect.getId(), 
							Direction.less, 
							((NumericLiteral)((Goal)aspect.getAspectRequirement()).getAspectRequirementLiteral()).getValue(), 
							objective);
				} else {
					//Sign == MAX
					constraint = builder.createSatisfactionConstraint(
							aspect.getId(), 
							Direction.greater, 
							((NumericLiteral)((Goal)aspect.getAspectRequirement()).getAspectRequirementLiteral()).getValue(), 
							objective);
				}
			} else {
				//TODO: Handle Enums and Sets
				throw new RuntimeException("Unsupported Goal literal in aspect. Only numeric literals supported in Goal aspect requirements so far.");
			}
		} else {
			throw new RuntimeException("Aspect must have aspect requirement of type Goal to derive SatisfactionConstraint!");
		}
		
		retranslationMap.put(constraint.getName(), aspectContext);
		return constraint;
	}
	
	public static org.opt4j.core.Objective staticTranslateEvalAspectToObjective(String qualityAttribute, EvaluationAspectWithContext aspectContext, ObjectiveBuilder builder) {
		//Make sure, the aspect IS an objective
		org.opt4j.core.Objective objective;
		if(aspectContext.getDimension().getType().getRelationSemantics().getRelSem() == EnumRelationSemantics.DECREASING) {
			//FIXME: the mapping of dimensions in QML and objectives in Opt4J ist broken: the quality attribute, such as dsexplore.performance is used here as a String, which means that two dimensions throughput and response time cannot be distinguished here.  
			objective = builder.createObjective(qualityAttribute, org.opt4j.core.Objective.Sign.MIN);
		} else {
			//INCREASING
			objective = builder.createObjective(qualityAttribute, org.opt4j.core.Objective.Sign.MAX);
		}
		
		retranslationMap.put(objective.getName(), aspectContext);
		return objective;
	}
}

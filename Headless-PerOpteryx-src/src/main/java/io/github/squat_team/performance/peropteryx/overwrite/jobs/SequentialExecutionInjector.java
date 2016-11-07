package io.github.squat_team.performance.peropteryx.overwrite.jobs;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.opt4j.common.completer.IndividualCompleterModule;
import org.opt4j.core.Objective;
import org.opt4j.core.Value;
import org.opt4j.start.Opt4JTask;

import com.google.inject.Module;

import de.uka.ipd.sdq.dsexplore.analysis.IAnalysis;
import de.uka.ipd.sdq.dsexplore.helper.GenotypeReader;
import de.uka.ipd.sdq.dsexplore.launch.DSEWorkflowConfiguration;
import de.uka.ipd.sdq.dsexplore.opt4j.representation.DSEEvaluator;
import de.uka.ipd.sdq.dsexplore.opt4j.start.Opt4JStarter;
import de.uka.ipd.sdq.dsexplore.qml.pcm.datastructures.UsageScenarioBasedObjective;
import de.uka.ipd.sdq.tcfmoop.config.GivenParetoFrontIsReachedConfig;
import de.uka.ipd.sdq.tcfmoop.config.IConfiguration;
import de.uka.ipd.sdq.tcfmoop.config.InsignificantSetQualityImprovementConfig;
import de.uka.ipd.sdq.tcfmoop.config.MinimalQualityCriteriaValueConfig;
import de.uka.ipd.sdq.tcfmoop.config.InsignificantSetQualityImprovementConfig.UnresolvedValueDifference;
import de.uka.ipd.sdq.tcfmoop.config.InsignificantSetQualityImprovementConfig.ValueDifference;
import de.uka.ipd.sdq.tcfmoop.config.exceptions.InvalidConfigException;
import de.uka.ipd.sdq.tcfmoop.tcmanager.TerminationCriteriaManager;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.MDSDBlackboard;

/**
 * This class uses reflection to run Headless PerOpteryx sequential instead of
 * concurrent, because the analysis fails in concurrent mode. This class is
 * considered as hacky and should be replaced as soon as a better solution for
 * this problem is found.
 */
public class SequentialExecutionInjector {
    private static final Logger log = Logger.getLogger(SequentialExecutionInjector.class.getName());

	
	private DSEWorkflowConfiguration dseConfig;
	private List<IAnalysis> evaluators;
	private MDSDBlackboard blackboard;
	private IProgressMonitor monitor;

	public SequentialExecutionInjector(DSEWorkflowConfiguration dseConfig, List<IAnalysis> evaluators,
			MDSDBlackboard blackboard, IProgressMonitor monitor) {
		this.dseConfig = dseConfig;
		this.evaluators = evaluators;
		this.blackboard = blackboard;
		this.monitor = monitor;
	}

	public void inject() throws CoreException, NoSuchFieldException, SecurityException, IllegalArgumentException,
			IllegalAccessException {
		Opt4JTask task = extractTask();
		resetTask(task);
		injectSequentialModuleInto(task);
		resetEvaluator(task);
		fixTerminationCriteria(task);
	}

	private Opt4JTask extractTask()
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		Field task = Opt4JStarter.class.getDeclaredField("task");
		task.setAccessible(true);
		return (Opt4JTask) task.get(Opt4JStarter.class);
	}

	private Opt4JTask resetTask(Opt4JTask task)
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		task.close();

		Field isClosed = Opt4JTask.class.getDeclaredField("isClosed");
		isClosed.setAccessible(true);
		isClosed.set(task, false);

		return task;
	}

	private Opt4JTask injectSequentialModuleInto(Opt4JTask task) {
		Collection<Module> modules = new ArrayList<Module>();
		modules.add(buildSequentialCompleter());
		task.init(modules);
		task.open();
		return task;
	}

	private void resetEvaluator(Opt4JTask task) {
		DSEEvaluator ev = task.getInstance(DSEEvaluator.class);
		ev.init(evaluators, monitor, blackboard, dseConfig.isStopOnInitialFailure());
	}

	private IndividualCompleterModule buildSequentialCompleter() {
		IndividualCompleterModule completer = new IndividualCompleterModule();
		completer.setThreads(1);
		completer.setType(IndividualCompleterModule.Type.SEQUENTIAL);
		return completer;
	}

	private void fixTerminationCriteria(Opt4JTask task) throws CoreException {
		if (dseConfig.getUseTerminationCriteria()) {
			TerminationCriteriaManager tcm = task.getInstance(TerminationCriteriaManager.class);

			resolveObjectivesForTCM();

			tcm.initialize(dseConfig.getTCConfigurations());

			if (dseConfig.getActivateComposedTerminationCriteria()) {
				tcm.activateComposedCriterion();
				tcm.setComposedCriterionExpression(dseConfig.getComposedCriteriaExpression());
			}

			if (dseConfig.getRunInComparisionMode()) {
				tcm.activateTCComparisionMode();
			}

		}
	}

	/**
	 * Copied from Opt4JStarter.
	 * 
	 * Resolves Objectives in the Configuration classes by using their String
	 * representation. Also Builds a Pareto Front for the
	 * "A Given Pareto Front Is Reached" from a file if needed.
	 * 
	 * @author Atanas Dimitrov
	 * @throws CoreException
	 */
	private void resolveObjectivesForTCM() throws CoreException {

		for (IConfiguration conf : dseConfig.getTCConfigurations()) {

			switch (conf.getTerminationCriterionName()) {

			case MINIMAL_QUALITY_CIRTERIA_VALUE:
				Map<String, Value<?>> unresolvedObjectiveMap = ((MinimalQualityCriteriaValueConfig) conf)
						.getUnresolvedObjectiveMinimalValue();

				if (unresolvedObjectiveMap != null && !unresolvedObjectiveMap.isEmpty()) {

					Collection<Objective> objectives = Opt4JStarter.getDSEEvaluator().getObjectives();
					Set<String> unresolvedObjectives = unresolvedObjectiveMap.keySet();
					Map<Objective, Value<?>> configuredObjectives = new HashMap<Objective, Value<?>>();

					for (String unresolveObjective : unresolvedObjectives) {
						for (Objective o : objectives) {
							if (o instanceof UsageScenarioBasedObjective) {
								if (unresolveObjective
										.contains(((UsageScenarioBasedObjective) o).getUsageScenario().getId())) {
									configuredObjectives.put(o, unresolvedObjectiveMap.get(unresolveObjective));
									break;
								}
							} else {
								if (o.getName().contains(unresolveObjective)
										|| unresolveObjective.contains(o.getName())) {
									configuredObjectives.put(o, unresolvedObjectiveMap.get(unresolveObjective));
									break;
								}
							}

						}
					}

					unresolvedObjectiveMap.clear();

					try {
						((MinimalQualityCriteriaValueConfig) (conf)).setObjectiveMinimalValues(configuredObjectives);
					} catch (InvalidConfigException e) {
						log.log(Level.WARNING, e.getMessage());
					}
				}

				break;

			case INSIGNIFICANT_SET_QUALITY_IMPROVEMENT:
				List<UnresolvedValueDifference> unresolvedValueDifferences = ((InsignificantSetQualityImprovementConfig) conf)
						.getUnresolvedValueDifferences();

				if (unresolvedValueDifferences != null && !unresolvedValueDifferences.isEmpty()) {
					Collection<Objective> objectives = Opt4JStarter.getDSEEvaluator().getObjectives();
					List<ValueDifference> valueDifferences = new LinkedList<ValueDifference>();

					for (UnresolvedValueDifference uvd : unresolvedValueDifferences) {

						for (Objective o : objectives) {
							if (o instanceof UsageScenarioBasedObjective) {
								if (uvd.objective
										.contains(((UsageScenarioBasedObjective) o).getUsageScenario().getId())) {
									try {
										valueDifferences.add(
												((InsignificantSetQualityImprovementConfig) conf).new ValueDifference(o,
														uvd.averageImprovement, uvd.maxMinImprovement));
									} catch (InvalidConfigException e) {
										log.log(Level.WARNING, e.getMessage());
									}
									break;
								}
							} else {
								if (o.getName().contains(uvd.objective) || uvd.objective.contains(o.getName())) {
									try {
										valueDifferences.add(
												((InsignificantSetQualityImprovementConfig) conf).new ValueDifference(o,
														uvd.averageImprovement, uvd.maxMinImprovement));
									} catch (InvalidConfigException e) {
										log.log(Level.WARNING, e.getMessage());
									}
									break;
								}
							}
						}

					}

					unresolvedValueDifferences.clear();

					try {
						((InsignificantSetQualityImprovementConfig) conf).setValueDifferences(valueDifferences);
					} catch (InvalidConfigException e) {
						log.log(Level.WARNING, e.getMessage());
					}

				}

				break;

			case GIVEN_PARETO_FRONT_IS_REACHED:

				String paretoFrontFile = ((GivenParetoFrontIsReachedConfig) conf).getParetoFrontFile();
				URI filePath = URI.createURI(paretoFrontFile);
				if (filePath == null || !filePath.isPlatform()) {
					filePath = URI.createFileURI(paretoFrontFile);
				}

				if (filePath != null && !filePath.isEmpty()) {
					try {
						((GivenParetoFrontIsReachedConfig) conf).setParetoFront(GenotypeReader.getObjectives(filePath));
					} catch (InvalidConfigException e) {
						e.printStackTrace();
					}
				}

				((GivenParetoFrontIsReachedConfig) conf).setParetoFrontFile("");

				break;
			}

		}

	}

}

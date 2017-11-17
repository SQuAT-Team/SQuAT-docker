package io.github.squat_team.performance.peropteryx.export;

import java.util.Collection;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import de.uka.ipd.sdq.dsexplore.helper.IResultsHandler;
import de.uka.ipd.sdq.dsexplore.launch.IResultsHandlerLaunchConfigSettings;
import de.uka.ipd.sdq.dsexplore.opt4j.representation.DSEIndividual;
import io.github.squat_team.performance.peropteryx.start.OptimizationInfo;

public class PCMResultsHandler implements IResultsHandler {

	@Override
	public void initialize(ILaunchConfiguration arg0) {
		// Do nothing
	}

	@Override
	public void handleResults(Collection<DSEIndividual> individuals, int iteration) {
		OptimizationInfo.nextIteration();
		// Do nothing
	}

	@Override
	public IResultsHandlerLaunchConfigSettings getLaunchSettingsTab(Composite arg0, SelectionListener arg1,
			ModifyListener arg2, Shell arg3) {
		throw new RuntimeException("NYI");
	}

}

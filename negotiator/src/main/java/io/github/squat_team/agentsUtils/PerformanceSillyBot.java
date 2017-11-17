package io.github.squat_team.agentsUtils;

public class PerformanceSillyBot extends SillyBot {
	private float originalResponseTime;

	public PerformanceSillyBot(float originalResponseTime, String name,float scenarioThreshold) {
		super(name,scenarioThreshold);
		this.originalResponseTime = originalResponseTime;
	}

	/*@Override
	protected boolean makeImprovementRegardingOriginal(Proposal proposal) {
		return ((PerformanceProposal)proposal).getResponseTime()<=originalResponseTime;
	}*/

	/*@Override
	public boolean canConcede() {
		//can concede if the following option in the ranking is not worse than an arbitrary 5% decline
		if(orderedProposals.size()==(currentConcessionIndex+1))
			return false;//no more options
		float currentResponseTime=((PerformanceProposal)orderedProposals.get(currentConcessionIndex)).getResponseTime();
		float nextResponseTime=((PerformanceProposal)orderedProposals.get(currentConcessionIndex+1)).getResponseTime();
		return (((nextResponseTime/currentResponseTime)-1)*100)<=5;
	}*/

	/*@Override
	public float getUtilityFor(Proposal proposal) {
		float bestResponseTime=((PerformanceProposal)orderedProposals.get(0)).getResponseTime();
		float proposalResponseTime=((PerformanceProposal)this.getProposalForArchitecture(proposal.getArchitectureName())).getResponseTime();
		
		return bestResponseTime/proposalResponseTime;
	}*/
	
	@Override
	public float getResponse(Proposal p) {
		Proposal proposal=getProposalForArchitecture(p.getArchitectureName());
		return ((PerformanceProposal)proposal).getResponseTime();
	}
	@Override
	protected float getScenarioMeasureFor(Proposal proposal) {
		return ((PerformanceProposal)proposal).getResponseTime();
	}
}

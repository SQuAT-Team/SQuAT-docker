package io.github.squat_team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import io.github.squat_team.agentsUtils.ArchitectureInitializer;
import io.github.squat_team.agentsUtils.BotIntializer;
import io.github.squat_team.agentsUtils.ILoadHelper;
import io.github.squat_team.agentsUtils.LoadHelper;
import io.github.squat_team.agentsUtils.Proposal;
import io.github.squat_team.agentsUtils.SillyBot;
import io.github.squat_team.agentsUtils.transformations.ArchitecturalTransformationsFactory;
import io.github.squat_team.model.RestArchitecture;

public class SQuATSillyBotsNegotiator {

	// private List<PCMArchitectureInstance> architectureAlternatives;
	private List<SillyBot> sillyBots;
	private Proposal agreementProposal;
	private int currentLevelOfTransformations;
	private ArchitecturalTransformationsFactory archTransFactory;
	private int maxNumberOfLevels;
	private boolean noMoreAlternatives;
	private ILoadHelper loadHelper;

	public SQuATSillyBotsNegotiator() {
		agreementProposal = null;
		currentLevelOfTransformations = 1;
		RestArchitecture initialArchitecture = ArchitectureInitializer
				.loadSpecificModel(NegotiatorConfiguration.INITIAL_ARCHITECTURE_NAME);
		archTransFactory = new ArchitecturalTransformationsFactory(initialArchitecture);
		maxNumberOfLevels = 10;
		noMoreAlternatives = false;
		loadHelper = new LoadHelper();
	}

	public void setLoadHelper(ILoadHelper loadHelper) {
		this.loadHelper = loadHelper;
	}

	/**
	 * 
	 * @return false when a conflict is reached and true when there is an agreement
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public boolean negotiateBaseOnMultipleArchitectures() throws InterruptedException, ExecutionException {
		// initialize the bots for the case study.
		BotIntializer.initialize2P2MBots();
		// architectureAlternatives=loadArchitecturalAlternatives(); This should be done
		// for real. Now I'm hardcoding the results
		// Step 1: Collect initial proposals
		HashMap<SillyBot, Proposal> proposals = collectInitialProposals();
		boolean redoRequest = false;
		// Step 2: Loop until you reach an Agreement or a Conflict
		while ((!checkAgreement(proposals)) || redoRequest) {

			// Select Agent who has to concede
			List<SillyBot> shouldConcede = selectWhoHasToConcede();

			if (shouldConcede.isEmpty()) {
				// Step 4: CONFLICT REACHED
				createNegotiationResult(); // conflict
				return false;
			} else {
				System.out.println("Step 3.b: Agent/s who has to concede [#= " + shouldConcede.size() + "]=> "
						+ shouldConcede.toString());

				for (SillyBot concedingAg : shouldConcede) {
					// Make "concedingAg" to concede
					Proposal newProposal;

					newProposal = concedingAg.makeConcession();// I have to implement this method

					System.out.println("Step 3.c: New Proposal made by the agent => " + newProposal.toString());
					// Update proposals map
					proposals.put(concedingAg, newProposal);
				}
				if (checkAgreement(proposals)) {
					System.out.println("Agreement: " + agreementProposal);
					printCurrentState(agreementProposal);
					if (NegotiatorConfiguration.autoAccept()) {
						// if auto accept, redo if level is not reached
						redoRequest = currentLevelOfTransformations < NegotiatorConfiguration.autoAcceptLevel();
					} else {
						System.out.println("REDO? (Y/N): ");
						Scanner scan = new Scanner(System.in);
						String answer = scan.next();
						if (!answer.trim().toUpperCase().equals("Y"))
							redoRequest = false;
						else
							redoRequest = true;
						// scan.close();
					}
				}
			}
		}
		// At this point an agreement should have been found => return it
		printAgreement(proposals);
		return true;
	}

	private void printAgreement(HashMap<SillyBot, Proposal> proposals) {
		System.out.println("Agreement achieved!!!!!");
		System.out.println("Agreement: " + agreementProposal);
		printCurrentState(agreementProposal);
		printNonDominatedAlternatives();
	}

	private void printNonDominatedAlternatives() {
		List<Proposal> nonDominated = new ArrayList<>();

		List<Proposal> proposals = sillyBots.get(0).getOrderedProposals();
		for (Iterator<Proposal> iterator = proposals.iterator(); iterator.hasNext();) {
			Proposal proposal = (Proposal) iterator.next();
			if (!isDominatedByAtLeastOneProposal(proposal))
				nonDominated.add(proposal);
		}

		System.out.println("Non dominated alternatives");
		for (Iterator<Proposal> iterator = nonDominated.iterator(); iterator.hasNext();) {
			Proposal proposal = (Proposal) iterator.next();
			System.out.println(proposal);
		}
	}

	private boolean isDominatedByAtLeastOneProposal(Proposal proposal) {
		List<Proposal> otherProposals = new ArrayList<>(sillyBots.get(0).getOrderedProposals());
		otherProposals.remove(proposal);
		for (Iterator<Proposal> iterator2 = otherProposals.iterator(); iterator2.hasNext();) {
			Proposal proposal2 = (Proposal) iterator2.next();
			if (isDominatedBy(proposal, proposal2))
				return true;
		}
		return false;
	}

	private boolean isDominatedBy(Proposal proposal, Proposal proposal2) {

		boolean allEqual = true;
		for (Iterator<SillyBot> iterator = sillyBots.iterator(); iterator.hasNext();) {
			SillyBot bot = (SillyBot) iterator.next();
			// Si le gano en al menos uno entonces no es dominado
			if ((bot.getResponse(proposal) < bot.getResponse(proposal2)))
				return false;
			if ((bot.getResponse(proposal) != bot.getResponse(proposal2)))
				allEqual = false;
		}

		if (allEqual)
			return false;
		return true;
	}

	private void createNegotiationResult() {
		System.out.println("Conflict.");
		printCurrentState(null);
	}

	private void printCurrentState(Proposal agreementProposal) {
		for (Iterator<SillyBot> iterator = sillyBots.iterator(); iterator.hasNext();) {
			SillyBot bot = (SillyBot) iterator.next();
			System.out.print(bot.toString());
			System.out.print(" Current proposal " + bot.getCurrentProposal() + " ("
					+ (bot.getCurrentConcessionIndex() + 1) + ") ");
			String acceptedProposals = " AcceptedProposals: ";
			List<SillyBot> otherAgents = new ArrayList<>(sillyBots);
			otherAgents.remove(bot);
			for (Iterator<SillyBot> iterator2 = otherAgents.iterator(); iterator2.hasNext();) {
				SillyBot bot2 = (SillyBot) iterator2.next();
				if (bot.acceptableUtilityValue(bot2.getCurrentProposal()))
					acceptedProposals = acceptedProposals + bot2.toString() + " ";
			}
			System.out.println(acceptedProposals);
			if (agreementProposal != null) {
				System.out.println("Index of agreement proposal: "
						+ bot.getIndexForArchitecture(agreementProposal.getArchitectureName()));
				System.out.println(
						"Utility function for bot: " + bot.getUtilityFor(agreementProposal.getArchitectureName()));
			}
		}
	}

	private List<SillyBot> selectWhoHasToConcede() {
		List<SillyBot> shouldConcede = new ArrayList<>();
		List<SillyBot> concedingAgents = getAgentsThatCanConcede();
		if (concedingAgents.size() > 1) {
			SillyBot agMin = null;
			double minZi = Double.POSITIVE_INFINITY;

			// String zis = "";
			for (SillyBot ag : concedingAgents) {
				double agZi = this.getZiFor(ag, sillyBots);
				// zis+= ag.getID()+"= "+agZi+", ";
				if (agZi <= minZi && agZi != Double.POSITIVE_INFINITY) { // agZi != Double.POSITIVE_INFINITY just in
																			// case, we should never need this check if
																			// the agents in "agents" list can concede.
					if (agZi == minZi && agMin != null) // if A and B have the same Zi => store A in the list of agents
														// who should concede, and now B is the agMin.
						shouldConcede.add(agMin);
					else // agZi < minZi
						shouldConcede.clear(); // clear the list because there is a new minimum
					minZi = agZi;
					agMin = ag;
				}
			}
			shouldConcede.add(agMin); // the "last" minimum should be added to the list. If there were other agents
										// with the same Zi as the agMin then they were added to the shouldConcede list.
			// logger.info(">> Zi's: "+zis);
		} else if (concedingAgents.size() == 1)
			shouldConcede.add(concedingAgents.get(0));

		return shouldConcede;

	}

	/**
	 * WillingnessToRiskConflictStrategy (WRC)
	 * 
	 * @param ag
	 * @param sillyBots2
	 * @return
	 */
	private double getZiFor(SillyBot myAgent, List<SillyBot> allAgents) {
		List<SillyBot> otherAgents = new ArrayList<>(allAgents);
		otherAgents.remove(myAgent);

		double myUtility;

		myUtility = myAgent.getUtilityFor(myAgent.getCurrentProposal());

		if (myUtility == 0)
			return 1;
		else
			return ((myUtility - selectMinUtilityProposal(myAgent, otherAgents)) / myUtility); // zi
	}

	private double selectMinUtilityProposal(SillyBot myAgent, List<SillyBot> otherAgents) {
		double minUtility = Double.POSITIVE_INFINITY;

		for (SillyBot ag : otherAgents) {
			Proposal otherAgProp = ag.getCurrentProposal();
			if (myAgent.getUtilityFor(otherAgProp.getArchitectureName()) < minUtility) {
				minUtility = myAgent.getUtilityFor(otherAgProp.getArchitectureName());
			}
		}

		return minUtility;
	}

	private List<SillyBot> getAgentsThatCanConcede() {
		List<SillyBot> ret = new ArrayList<>();
		for (Iterator<SillyBot> iterator = sillyBots.iterator(); iterator.hasNext();) {
			SillyBot bot = (SillyBot) iterator.next();
			if (bot.canConcede())
				ret.add(bot);
		}

		return ret;
	}

	/**
	 * Checks if there is an agreement (between the agents) for one of the proposals
	 * 
	 * @param proposals
	 * @return
	 */
	private boolean checkAgreement(HashMap<SillyBot, Proposal> proposals) {
		for (Iterator<Proposal> iterator = proposals.values().iterator(); iterator.hasNext();) {
			Proposal proposal = (Proposal) iterator.next();
			if (checkAgreementForAgents(proposal)) {
				agreementProposal = proposal;
				return true;
			}
		}
		return false;
	}

	private boolean checkAgreementForAgents(Proposal proposal) {
		for (Iterator<SillyBot> iterator = sillyBots.iterator(); iterator.hasNext();) {
			SillyBot sillyBot = (SillyBot) iterator.next();
			if (!sillyBot.acceptableUtilityValue(proposal))
				return false;
		}
		return true;
	}

	private HashMap<SillyBot, Proposal> collectInitialProposals() throws InterruptedException, ExecutionException {
		// REMOVE THIS FIRST LINE. IT ONLY PURPOSES IS TO AVOID THE MODIFIABILITY
		// TACTICS THAT ARE NOT WORKING WITH THE COMPOSITE COMPONETS
		// new LoadHelper().loadBotsForArchitecturalAlternatives(new
		// ArrayList<ArchitecturalVersion>(),archTransFactory.getInitialArchitecture());

		HashMap<SillyBot, Proposal> ret = new HashMap<>();
		System.out.println("Level of transformations: " + currentLevelOfTransformations);
		List<RestArchitecture> versionsUntilLevel = archTransFactory
				.getArchitecturalTransformationsUntilLevel(currentLevelOfTransformations).get();
		List<RestArchitecture> versionsForLevel = archTransFactory
				.transformationsForLevel(currentLevelOfTransformations);

		if (versionsForLevel.size() == 0) {
			noMoreAlternatives = true;
		} else {
			System.out.println("Intial Proposals");

			// Each agent has to make a ranking with the alternatives and select the best
			// for its scenario
			sillyBots = loadHelper.generateSillyBotsAndAnalyze(versionsUntilLevel,
					archTransFactory.getInitialArchitecture());
			System.out.println("Total proposals: " + sillyBots.get(0).getOrderedProposals().size());
			for (Iterator<SillyBot> iterator = sillyBots.iterator(); iterator.hasNext();) {
				SillyBot bot = (SillyBot) iterator.next();
				ret.put(bot, bot.getBestProposal());
				System.out.println(bot + " " + bot.getBestProposal());
			}
		}
		return ret;
	}

	// TODO: PA! This will be the method for an external call.
	public void negotiatiateUntilAnAgreementIsReached() {
		NegotiatorConfiguration.printConfig();

		// start time measurements
		TimeMeasurements.startTotalTimeMeasurement();
		TimeMeasurements.continueNegotiationTimeMeasurement();

		boolean agreement = false;
		while (!agreement && (currentLevelOfTransformations <= maxNumberOfLevels) && !noMoreAlternatives) {
			try {
				agreement = negotiateBaseOnMultipleArchitectures();
			} catch (InterruptedException | ExecutionException e) {
				// TODO: PA! Do something better here... Fails because of waiting for results
				e.printStackTrace();
			}
			currentLevelOfTransformations++;
		}

		// end time time measurements
		TimeMeasurements.pauseNegotiationTimeMeasurement();
		TimeMeasurements.endTotalTimeMeasurement();
	}

	public static void main(String[] args) {
		new SQuATSillyBotsNegotiator().negotiatiateUntilAnAgreementIsReached();
	}

}

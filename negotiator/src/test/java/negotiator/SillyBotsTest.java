package negotiator;

import java.util.ArrayList;
import java.util.List;

import io.github.squat_team.SQuATSillyBotsNegotiator;
import io.github.squat_team.agentsUtils.ILoadHelper;
import io.github.squat_team.agentsUtils.ModifiabilityProposal;
import io.github.squat_team.agentsUtils.ModifiabilitySillyBot;
import io.github.squat_team.agentsUtils.PerformanceProposal;
import io.github.squat_team.agentsUtils.PerformanceSillyBot;
import io.github.squat_team.agentsUtils.SillyBot;
import io.github.squat_team.model.RestArchitecture;

public class SillyBotsTest {

	public void test() {
		SQuATSillyBotsNegotiator negotiator = new SQuATSillyBotsNegotiator();
		negotiator.setLoadHelper(new TestLoadHelper());
		negotiator.negotiateBaseOnMultipleArchitectures();
		// In SquatSillyBotNegotiator.collectInitialProposals the if has to be removed to work.
	}

	private class TestLoadHelper implements ILoadHelper {

		@Override
		public List<SillyBot> generateSillyBotsAndAnalyze(List<RestArchitecture> architecturalAlternatives,
				RestArchitecture initialArchitecture) {
			ModifiabilitySillyBot m1Bot = new ModifiabilitySillyBot(/* 3, */ 115f, "m1", 120f);
			ModifiabilitySillyBot m2Bot = new ModifiabilitySillyBot(/* 5, */ 190.5f, "m2", 300f);
			PerformanceSillyBot p1Bot = new PerformanceSillyBot(111.7639f, "p1", 30f);
			PerformanceSillyBot p2Bot = new PerformanceSillyBot(74.0173f, "p2", 40f);

			// First level - Modifiability tactics
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 111.0f, "stplus-mod-split(PaymentSystem)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-mod-wrapper(IExporter)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-mod-wrapper(ITripDB)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-mod-wrapper(IExternalPayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-mod-wrapper(IEmployeePayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-mod-wrapper(IBooking)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-mod-wrapper(IBusinessTrip)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 111.0f, "stplus-mod-split(PaymentSystem)-wrapper(IExporter)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 111.0f, "stplus-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 110.5f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 111.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 111.0f, "stplus-mod-split(PaymentSystem)-wrapper(IBooking)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 111.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));

			// First level - Performance tactics
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps1-258"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps1-281"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps1-338"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps1-340"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps1-397"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps1-404"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps1-436"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps1-444"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps1-494"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps1-64"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps2-209"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps2-22"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps2-325"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps2-330"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps2-358"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps2-366"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps2-416"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps2-476"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps2-479"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps2-480"));

			// Second level - Modifiability tactics applied over Performance candidates
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps1-258-mod-wrapper(IExporter)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps1-258-mod-wrapper(ITripDB)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps1-258-mod-wrapper(IExternalPayment)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps1-258-mod-wrapper(IEmployeePayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps1-258-mod-wrapper(IBooking)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps1-258-mod-wrapper(IBusinessTrip)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f, "stplus-ps1-258-mod-split(PaymentSystem)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps1-258-mod-split(PaymentSystem)-wrapper(IExporter)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps1-258-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 110.5f,
					"stplus-ps1-258-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps1-258-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps1-258-mod-split(PaymentSystem)-wrapper(IBooking)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps1-258-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps1-281-mod-wrapper(IExporter)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps1-281-mod-wrapper(ITripDB)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps1-281-mod-wrapper(IExternalPayment)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps1-281-mod-wrapper(IEmployeePayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps1-281-mod-wrapper(IBooking)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps1-281-mod-wrapper(IBusinessTrip)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f, "stplus-ps1-281-mod-split(PaymentSystem)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps1-281-mod-split(PaymentSystem)-wrapper(IExporter)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps1-281-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 110.5f,
					"stplus-ps1-281-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps1-281-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps1-281-mod-split(PaymentSystem)-wrapper(IBooking)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps1-281-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps1-338-mod-wrapper(IExporter)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps1-338-mod-wrapper(ITripDB)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps1-338-mod-wrapper(IExternalPayment)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps1-338-mod-wrapper(IEmployeePayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps1-338-mod-wrapper(IBooking)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps1-338-mod-wrapper(IBusinessTrip)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 111.0f, "stplus-ps1-338-mod-split(PaymentSystem)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 111.0f,
					"stplus-ps1-338-mod-split(PaymentSystem)-wrapper(IExporter)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 111.0f,
					"stplus-ps1-338-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 110.5f,
					"stplus-ps1-338-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 111.0f,
					"stplus-ps1-338-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 111.0f,
					"stplus-ps1-338-mod-split(PaymentSystem)-wrapper(IBooking)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 111.0f,
					"stplus-ps1-338-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps1-340-mod-wrapper(IExporter)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps1-340-mod-wrapper(ITripDB)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps1-340-mod-wrapper(IExternalPayment)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps1-340-mod-wrapper(IEmployeePayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps1-340-mod-wrapper(IBooking)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps1-340-mod-wrapper(IBusinessTrip)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f, "stplus-ps1-340-mod-split(PaymentSystem)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps1-340-mod-split(PaymentSystem)-wrapper(IExporter)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps1-340-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 110.5f,
					"stplus-ps1-340-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps1-340-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps1-340-mod-split(PaymentSystem)-wrapper(IBooking)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps1-340-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps1-397-mod-wrapper(IExporter)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps1-397-mod-wrapper(ITripDB)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps1-397-mod-wrapper(IExternalPayment)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps1-397-mod-wrapper(IEmployeePayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps1-397-mod-wrapper(IBooking)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps1-397-mod-wrapper(IBusinessTrip)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f, "stplus-ps1-397-mod-split(PaymentSystem)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps1-397-mod-split(PaymentSystem)-wrapper(IExporter)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps1-397-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 110.5f,
					"stplus-ps1-397-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps1-397-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps1-397-mod-split(PaymentSystem)-wrapper(IBooking)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps1-397-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps1-404-mod-wrapper(IExporter)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps1-404-mod-wrapper(ITripDB)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps1-404-mod-wrapper(IExternalPayment)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps1-404-mod-wrapper(IEmployeePayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps1-404-mod-wrapper(IBooking)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps1-404-mod-wrapper(IBusinessTrip)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 111.0f, "stplus-ps1-404-mod-split(PaymentSystem)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 111.0f,
					"stplus-ps1-404-mod-split(PaymentSystem)-wrapper(IExporter)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 111.0f,
					"stplus-ps1-404-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 110.5f,
					"stplus-ps1-404-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 111.0f,
					"stplus-ps1-404-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 111.0f,
					"stplus-ps1-404-mod-split(PaymentSystem)-wrapper(IBooking)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 111.0f,
					"stplus-ps1-404-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps1-436-mod-wrapper(IExporter)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps1-436-mod-wrapper(ITripDB)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps1-436-mod-wrapper(IExternalPayment)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps1-436-mod-wrapper(IEmployeePayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps1-436-mod-wrapper(IBooking)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps1-436-mod-wrapper(IBusinessTrip)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f, "stplus-ps1-436-mod-split(PaymentSystem)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps1-436-mod-split(PaymentSystem)-wrapper(IExporter)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps1-436-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 110.5f,
					"stplus-ps1-436-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps1-436-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps1-436-mod-split(PaymentSystem)-wrapper(IBooking)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps1-436-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps1-444-mod-wrapper(IExporter)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps1-444-mod-wrapper(ITripDB)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps1-444-mod-wrapper(IExternalPayment)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps1-444-mod-wrapper(IEmployeePayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps1-444-mod-wrapper(IBooking)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps1-444-mod-wrapper(IBusinessTrip)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f, "stplus-ps1-444-mod-split(PaymentSystem)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps1-444-mod-split(PaymentSystem)-wrapper(IExporter)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps1-444-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 110.5f,
					"stplus-ps1-444-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps1-444-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps1-444-mod-split(PaymentSystem)-wrapper(IBooking)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps1-444-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps1-494-mod-wrapper(IExporter)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps1-494-mod-wrapper(ITripDB)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps1-494-mod-wrapper(IExternalPayment)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps1-494-mod-wrapper(IEmployeePayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps1-494-mod-wrapper(IBooking)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps1-494-mod-wrapper(IBusinessTrip)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f, "stplus-ps1-494-mod-split(PaymentSystem)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps1-494-mod-split(PaymentSystem)-wrapper(IExporter)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps1-494-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 110.5f,
					"stplus-ps1-494-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps1-494-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps1-494-mod-split(PaymentSystem)-wrapper(IBooking)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps1-494-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps1-64-mod-wrapper(IExporter)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps1-64-mod-wrapper(ITripDB)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps1-64-mod-wrapper(IExternalPayment)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps1-64-mod-wrapper(IEmployeePayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps1-64-mod-wrapper(IBooking)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps1-64-mod-wrapper(IBusinessTrip)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f, "stplus-ps1-64-mod-split(PaymentSystem)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps1-64-mod-split(PaymentSystem)-wrapper(IExporter)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps1-64-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 110.5f,
					"stplus-ps1-64-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps1-64-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps1-64-mod-split(PaymentSystem)-wrapper(IBooking)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps1-64-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps2-209-mod-wrapper(IExporter)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps2-209-mod-wrapper(ITripDB)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps2-209-mod-wrapper(IExternalPayment)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps2-209-mod-wrapper(IEmployeePayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps2-209-mod-wrapper(IBooking)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps2-209-mod-wrapper(IBusinessTrip)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f, "stplus-ps2-209-mod-split(PaymentSystem)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps2-209-mod-split(PaymentSystem)-wrapper(IExporter)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps2-209-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 110.5f,
					"stplus-ps2-209-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps2-209-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps2-209-mod-split(PaymentSystem)-wrapper(IBooking)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps2-209-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps2-22-mod-wrapper(IExporter)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps2-22-mod-wrapper(ITripDB)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps2-22-mod-wrapper(IExternalPayment)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps2-22-mod-wrapper(IEmployeePayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps2-22-mod-wrapper(IBooking)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps2-22-mod-wrapper(IBusinessTrip)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 111.0f, "stplus-ps2-22-mod-split(PaymentSystem)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 111.0f,
					"stplus-ps2-22-mod-split(PaymentSystem)-wrapper(IExporter)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 111.0f,
					"stplus-ps2-22-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 110.5f,
					"stplus-ps2-22-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 111.0f,
					"stplus-ps2-22-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 111.0f,
					"stplus-ps2-22-mod-split(PaymentSystem)-wrapper(IBooking)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 111.0f,
					"stplus-ps2-22-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps2-325-mod-wrapper(IExporter)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps2-325-mod-wrapper(ITripDB)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps2-325-mod-wrapper(IExternalPayment)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps2-325-mod-wrapper(IEmployeePayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps2-325-mod-wrapper(IBooking)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps2-325-mod-wrapper(IBusinessTrip)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 111.0f, "stplus-ps2-325-mod-split(PaymentSystem)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 111.0f,
					"stplus-ps2-325-mod-split(PaymentSystem)-wrapper(IExporter)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 111.0f,
					"stplus-ps2-325-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 110.5f,
					"stplus-ps2-325-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 111.0f,
					"stplus-ps2-325-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 111.0f,
					"stplus-ps2-325-mod-split(PaymentSystem)-wrapper(IBooking)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 111.0f,
					"stplus-ps2-325-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps2-330-mod-wrapper(IExporter)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps2-330-mod-wrapper(ITripDB)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps2-330-mod-wrapper(IExternalPayment)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps2-330-mod-wrapper(IEmployeePayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps2-330-mod-wrapper(IBooking)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps2-330-mod-wrapper(IBusinessTrip)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f, "stplus-ps2-330-mod-split(PaymentSystem)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps2-330-mod-split(PaymentSystem)-wrapper(IExporter)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps2-330-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 110.5f,
					"stplus-ps2-330-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps2-330-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps2-330-mod-split(PaymentSystem)-wrapper(IBooking)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps2-330-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps2-358-mod-wrapper(IExporter)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps2-358-mod-wrapper(ITripDB)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps2-358-mod-wrapper(IExternalPayment)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps2-358-mod-wrapper(IEmployeePayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps2-358-mod-wrapper(IBooking)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps2-358-mod-wrapper(IBusinessTrip)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 111.0f, "stplus-ps2-358-mod-split(PaymentSystem)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 111.0f,
					"stplus-ps2-358-mod-split(PaymentSystem)-wrapper(IExporter)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 111.0f,
					"stplus-ps2-358-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 110.5f,
					"stplus-ps2-358-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 111.0f,
					"stplus-ps2-358-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 111.0f,
					"stplus-ps2-358-mod-split(PaymentSystem)-wrapper(IBooking)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 111.0f,
					"stplus-ps2-358-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps2-366-mod-wrapper(IExporter)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps2-366-mod-wrapper(ITripDB)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps2-366-mod-wrapper(IExternalPayment)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps2-366-mod-wrapper(IEmployeePayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps2-366-mod-wrapper(IBooking)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps2-366-mod-wrapper(IBusinessTrip)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 111.0f, "stplus-ps2-366-mod-split(PaymentSystem)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 111.0f,
					"stplus-ps2-366-mod-split(PaymentSystem)-wrapper(IExporter)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 111.0f,
					"stplus-ps2-366-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 110.5f,
					"stplus-ps2-366-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 111.0f,
					"stplus-ps2-366-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 111.0f,
					"stplus-ps2-366-mod-split(PaymentSystem)-wrapper(IBooking)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 111.0f,
					"stplus-ps2-366-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps2-416-mod-wrapper(IExporter)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps2-416-mod-wrapper(ITripDB)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps2-416-mod-wrapper(IExternalPayment)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps2-416-mod-wrapper(IEmployeePayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps2-416-mod-wrapper(IBooking)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps2-416-mod-wrapper(IBusinessTrip)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 111.0f, "stplus-ps2-416-mod-split(PaymentSystem)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 111.0f,
					"stplus-ps2-416-mod-split(PaymentSystem)-wrapper(IExporter)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 111.0f,
					"stplus-ps2-416-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 110.5f,
					"stplus-ps2-416-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 111.0f,
					"stplus-ps2-416-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 111.0f,
					"stplus-ps2-416-mod-split(PaymentSystem)-wrapper(IBooking)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 111.0f,
					"stplus-ps2-416-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps2-476-mod-wrapper(IExporter)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps2-476-mod-wrapper(ITripDB)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps2-476-mod-wrapper(IExternalPayment)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps2-476-mod-wrapper(IEmployeePayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps2-476-mod-wrapper(IBooking)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps2-476-mod-wrapper(IBusinessTrip)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f, "stplus-ps2-476-mod-split(PaymentSystem)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps2-476-mod-split(PaymentSystem)-wrapper(IExporter)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps2-476-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 110.5f,
					"stplus-ps2-476-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps2-476-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps2-476-mod-split(PaymentSystem)-wrapper(IBooking)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps2-476-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps2-479-mod-wrapper(IExporter)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps2-479-mod-wrapper(ITripDB)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps2-479-mod-wrapper(IExternalPayment)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps2-479-mod-wrapper(IEmployeePayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps2-479-mod-wrapper(IBooking)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-ps2-479-mod-wrapper(IBusinessTrip)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f, "stplus-ps2-479-mod-split(PaymentSystem)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps2-479-mod-split(PaymentSystem)-wrapper(IExporter)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps2-479-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 110.5f,
					"stplus-ps2-479-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps2-479-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps2-479-mod-split(PaymentSystem)-wrapper(IBooking)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-ps2-479-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps2-480-mod-wrapper(IExporter)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps2-480-mod-wrapper(ITripDB)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps2-480-mod-wrapper(IExternalPayment)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps2-480-mod-wrapper(IEmployeePayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps2-480-mod-wrapper(IBooking)"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-ps2-480-mod-wrapper(IBusinessTrip)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 111.0f, "stplus-ps2-480-mod-split(PaymentSystem)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 111.0f,
					"stplus-ps2-480-mod-split(PaymentSystem)-wrapper(IExporter)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 111.0f,
					"stplus-ps2-480-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 110.5f,
					"stplus-ps2-480-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 111.0f,
					"stplus-ps2-480-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 111.0f,
					"stplus-ps2-480-mod-split(PaymentSystem)-wrapper(IBooking)"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 111.0f,
					"stplus-ps2-480-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));

			// Second level - Performance tactics applied over Modifiability candidates
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f, "stplus-mod-split(PaymentSystem)-ps1-136"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f, "stplus-mod-split(PaymentSystem)-ps1-162"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f, "stplus-mod-split(PaymentSystem)-ps1-208"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f, "stplus-mod-split(PaymentSystem)-ps1-232"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f, "stplus-mod-split(PaymentSystem)-ps1-239"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f, "stplus-mod-split(PaymentSystem)-ps1-316"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f, "stplus-mod-split(PaymentSystem)-ps1-321"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f, "stplus-mod-split(PaymentSystem)-ps1-355"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f, "stplus-mod-split(PaymentSystem)-ps1-367"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f, "stplus-mod-split(PaymentSystem)-ps1-419"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IExporter)-ps1-66"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IExporter)-ps1-141"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IExporter)-ps1-227"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IExporter)-ps1-238"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IExporter)-ps1-275"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IExporter)-ps1-329"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IExporter)-ps1-450"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IExporter)-ps1-459"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IExporter)-ps1-494"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IExporter)-ps1-500"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(ITripDB)-ps1-161"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(ITripDB)-ps1-167"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(ITripDB)-ps1-228"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(ITripDB)-ps1-233"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(ITripDB)-ps1-353"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(ITripDB)-ps1-354"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(ITripDB)-ps1-357"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(ITripDB)-ps1-358"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(ITripDB)-ps1-432"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(ITripDB)-ps1-476"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-mod-wrapper(IExternalPayment)-ps1-195"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-mod-wrapper(IExternalPayment)-ps1-247"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-mod-wrapper(IExternalPayment)-ps1-263"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-mod-wrapper(IExternalPayment)-ps1-333"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-mod-wrapper(IExternalPayment)-ps1-350"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-mod-wrapper(IExternalPayment)-ps1-396"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-mod-wrapper(IExternalPayment)-ps1-405"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-mod-wrapper(IExternalPayment)-ps1-425"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-mod-wrapper(IExternalPayment)-ps1-426"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-mod-wrapper(IExternalPayment)-ps1-464"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IEmployeePayment)-ps1-109"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IEmployeePayment)-ps1-133"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IEmployeePayment)-ps1-138"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IEmployeePayment)-ps1-311"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IEmployeePayment)-ps1-330"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IEmployeePayment)-ps1-41"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IEmployeePayment)-ps1-416"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IEmployeePayment)-ps1-417"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IEmployeePayment)-ps1-447"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IEmployeePayment)-ps1-497"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IBooking)-ps1-154"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IBooking)-ps1-229"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IBooking)-ps1-237"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IBooking)-ps1-269"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IBooking)-ps1-277"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IBooking)-ps1-282"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IBooking)-ps1-316"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IBooking)-ps1-364"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IBooking)-ps1-404"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IBooking)-ps1-450"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IBusinessTrip)-ps1-110"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IBusinessTrip)-ps1-142"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IBusinessTrip)-ps1-216"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IBusinessTrip)-ps1-232"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IBusinessTrip)-ps1-295"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IBusinessTrip)-ps1-296"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IBusinessTrip)-ps1-442"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IBusinessTrip)-ps1-472"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IBusinessTrip)-ps1-474"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IBusinessTrip)-ps1-478"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps1-118"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps1-275"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps1-351"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps1-355"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps1-382"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps1-389"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps1-390"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps1-406"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps1-425"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps1-428"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps1-220"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps1-290"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps1-327"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps1-377"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps1-406"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps1-423"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps1-439"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps1-446"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps1-447"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps1-454"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 110.5f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps1-197"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 110.5f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps1-232"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 110.5f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps1-233"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 110.5f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps1-235"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 110.5f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps1-276"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 110.5f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps1-277"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 110.5f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps1-33"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 110.5f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps1-355"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 110.5f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps1-362"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 110.5f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps1-368"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps1-12"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps1-13"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps1-176"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps1-224"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps1-281"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps1-318"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps1-356"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps1-402"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps1-438"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps1-483"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps1-100"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps1-145"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps1-232"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps1-300"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps1-326"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps1-353"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps1-400"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps1-474"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps1-488"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps1-86"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps1-319"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps1-321"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps1-323"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps1-356"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps1-392"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps1-397"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps1-426"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps1-427"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps1-433"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps1-62"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f, "stplus-mod-split(PaymentSystem)-ps2-119"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f, "stplus-mod-split(PaymentSystem)-ps2-179"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f, "stplus-mod-split(PaymentSystem)-ps2-191"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f, "stplus-mod-split(PaymentSystem)-ps2-222"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f, "stplus-mod-split(PaymentSystem)-ps2-239"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f, "stplus-mod-split(PaymentSystem)-ps2-402"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f, "stplus-mod-split(PaymentSystem)-ps2-43"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f, "stplus-mod-split(PaymentSystem)-ps2-432"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f, "stplus-mod-split(PaymentSystem)-ps2-470"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f, "stplus-mod-split(PaymentSystem)-ps2-69"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IExporter)-ps2-146"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IExporter)-ps2-148"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IExporter)-ps2-162"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IExporter)-ps2-243"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IExporter)-ps2-270"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IExporter)-ps2-281"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IExporter)-ps2-318"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IExporter)-ps2-324"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IExporter)-ps2-52"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IExporter)-ps2-90"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(ITripDB)-ps2-143"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(ITripDB)-ps2-189"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(ITripDB)-ps2-192"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(ITripDB)-ps2-231"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(ITripDB)-ps2-309"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(ITripDB)-ps2-314"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(ITripDB)-ps2-33"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(ITripDB)-ps2-347"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(ITripDB)-ps2-354"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(ITripDB)-ps2-86"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-mod-wrapper(IExternalPayment)-ps2-145"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-mod-wrapper(IExternalPayment)-ps2-147"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-mod-wrapper(IExternalPayment)-ps2-270"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-mod-wrapper(IExternalPayment)-ps2-275"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-mod-wrapper(IExternalPayment)-ps2-324"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-mod-wrapper(IExternalPayment)-ps2-354"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-mod-wrapper(IExternalPayment)-ps2-369"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-mod-wrapper(IExternalPayment)-ps2-445"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-mod-wrapper(IExternalPayment)-ps2-449"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 3, */ 115.0f, "stplus-mod-wrapper(IExternalPayment)-ps2-453"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IEmployeePayment)-ps2-130"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IEmployeePayment)-ps2-237"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IEmployeePayment)-ps2-257"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IEmployeePayment)-ps2-310"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IEmployeePayment)-ps2-358"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IEmployeePayment)-ps2-361"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IEmployeePayment)-ps2-437"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IEmployeePayment)-ps2-441"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IEmployeePayment)-ps2-489"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IEmployeePayment)-ps2-496"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IBooking)-ps2-117"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IBooking)-ps2-143"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IBooking)-ps2-237"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IBooking)-ps2-286"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IBooking)-ps2-299"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IBooking)-ps2-330"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IBooking)-ps2-375"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IBooking)-ps2-376"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IBooking)-ps2-384"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IBooking)-ps2-401"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IBusinessTrip)-ps2-107"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IBusinessTrip)-ps2-130"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IBusinessTrip)-ps2-156"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IBusinessTrip)-ps2-238"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IBusinessTrip)-ps2-276"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IBusinessTrip)-ps2-282"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IBusinessTrip)-ps2-440"));
			m1Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IBusinessTrip)-ps2-442"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IBusinessTrip)-ps2-90"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 123.0f, "stplus-mod-wrapper(IBusinessTrip)-ps2-98"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps2-143"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps2-191"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps2-268"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps2-307"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps2-314"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps2-352"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps2-354"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps2-355"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps2-55"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps2-7"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps2-137"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps2-142"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps2-171"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps2-189"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps2-209"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps2-238"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps2-275"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps2-281"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps2-285"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps2-414"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 110.5f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps2-192"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 110.5f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps2-203"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 110.5f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps2-275"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 110.5f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps2-279"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 110.5f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps2-360"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 110.5f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps2-364"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 110.5f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps2-403"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 110.5f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps2-439"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 110.5f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps2-450"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 3, */ 110.5f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps2-492"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps2-250"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps2-321"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps2-33"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps2-359"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps2-360"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps2-369"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps2-370"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps2-456"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps2-499"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps2-501"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps2-226"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps2-325"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps2-401"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps2-415"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps2-452"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps2-456"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps2-462"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps2-505"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps2-506"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps2-508"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps2-119"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps2-242"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps2-278"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps2-344"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps2-373"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps2-407"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps2-408"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps2-439"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps2-447"));
			m1Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 119.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps2-482"));

			// First level - Modifiability tactics
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-mod-split(PaymentSystem)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-mod-wrapper(IExporter)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 110.5f, "stplus-mod-wrapper(ITripDB)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-mod-wrapper(IExternalPayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-mod-wrapper(IEmployeePayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-mod-wrapper(IBooking)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-mod-wrapper(IBusinessTrip)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-mod-split(PaymentSystem)-wrapper(IExporter)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 4, */ 110.5f, "stplus-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-mod-split(PaymentSystem)-wrapper(IBooking)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));

			// First level - Performance tactics
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 251.0f, "stplus-ps1-258"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps1-281"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps1-338"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps1-340"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps1-397"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps1-404"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps1-436"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps1-444"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 251.0f, "stplus-ps1-494"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps1-64"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.0f, "stplus-ps2-209"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps2-22"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps2-325"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 251.0f, "stplus-ps2-330"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.0f, "stplus-ps2-358"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps2-366"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 251.0f, "stplus-ps2-416"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 251.0f, "stplus-ps2-476"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 7, */ 291.5f, "stplus-ps2-479"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps2-480"));

			// Second level - Modifiability tactics applied over Performance candidates
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 251.0f, "stplus-ps1-258-mod-wrapper(IExporter)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 171.0f, "stplus-ps1-258-mod-wrapper(ITripDB)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 6, */ 251.0f, "stplus-ps1-258-mod-wrapper(IExternalPayment)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 6, */ 251.0f, "stplus-ps1-258-mod-wrapper(IEmployeePayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 251.0f, "stplus-ps1-258-mod-wrapper(IBooking)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 6, */ 251.0f, "stplus-ps1-258-mod-wrapper(IBusinessTrip)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 251.0f, "stplus-ps1-258-mod-split(PaymentSystem)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 251.0f,
					"stplus-ps1-258-mod-split(PaymentSystem)-wrapper(IExporter)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 171.0f,
					"stplus-ps1-258-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 251.0f,
					"stplus-ps1-258-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 251.0f,
					"stplus-ps1-258-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 251.0f,
					"stplus-ps1-258-mod-split(PaymentSystem)-wrapper(IBooking)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 251.0f,
					"stplus-ps1-258-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps1-281-mod-wrapper(IExporter)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 110.5f, "stplus-ps1-281-mod-wrapper(ITripDB)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps1-281-mod-wrapper(IExternalPayment)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps1-281-mod-wrapper(IEmployeePayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps1-281-mod-wrapper(IBooking)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps1-281-mod-wrapper(IBusinessTrip)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps1-281-mod-split(PaymentSystem)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps1-281-mod-split(PaymentSystem)-wrapper(IExporter)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 110.5f,
					"stplus-ps1-281-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps1-281-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps1-281-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps1-281-mod-split(PaymentSystem)-wrapper(IBooking)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps1-281-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps1-338-mod-wrapper(IExporter)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 110.5f, "stplus-ps1-338-mod-wrapper(ITripDB)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps1-338-mod-wrapper(IExternalPayment)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps1-338-mod-wrapper(IEmployeePayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps1-338-mod-wrapper(IBooking)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps1-338-mod-wrapper(IBusinessTrip)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps1-338-mod-split(PaymentSystem)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps1-338-mod-split(PaymentSystem)-wrapper(IExporter)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 110.5f,
					"stplus-ps1-338-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps1-338-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps1-338-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps1-338-mod-split(PaymentSystem)-wrapper(IBooking)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps1-338-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps1-340-mod-wrapper(IExporter)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 110.5f, "stplus-ps1-340-mod-wrapper(ITripDB)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps1-340-mod-wrapper(IExternalPayment)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps1-340-mod-wrapper(IEmployeePayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps1-340-mod-wrapper(IBooking)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps1-340-mod-wrapper(IBusinessTrip)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps1-340-mod-split(PaymentSystem)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps1-340-mod-split(PaymentSystem)-wrapper(IExporter)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 110.5f,
					"stplus-ps1-340-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps1-340-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps1-340-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps1-340-mod-split(PaymentSystem)-wrapper(IBooking)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps1-340-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps1-397-mod-wrapper(IExporter)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 110.5f, "stplus-ps1-397-mod-wrapper(ITripDB)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps1-397-mod-wrapper(IExternalPayment)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps1-397-mod-wrapper(IEmployeePayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps1-397-mod-wrapper(IBooking)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps1-397-mod-wrapper(IBusinessTrip)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps1-397-mod-split(PaymentSystem)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps1-397-mod-split(PaymentSystem)-wrapper(IExporter)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 110.5f,
					"stplus-ps1-397-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps1-397-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps1-397-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps1-397-mod-split(PaymentSystem)-wrapper(IBooking)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps1-397-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps1-404-mod-wrapper(IExporter)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 110.5f, "stplus-ps1-404-mod-wrapper(ITripDB)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps1-404-mod-wrapper(IExternalPayment)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps1-404-mod-wrapper(IEmployeePayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps1-404-mod-wrapper(IBooking)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps1-404-mod-wrapper(IBusinessTrip)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps1-404-mod-split(PaymentSystem)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps1-404-mod-split(PaymentSystem)-wrapper(IExporter)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 110.5f,
					"stplus-ps1-404-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps1-404-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps1-404-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps1-404-mod-split(PaymentSystem)-wrapper(IBooking)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps1-404-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps1-436-mod-wrapper(IExporter)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 110.5f, "stplus-ps1-436-mod-wrapper(ITripDB)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps1-436-mod-wrapper(IExternalPayment)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps1-436-mod-wrapper(IEmployeePayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps1-436-mod-wrapper(IBooking)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps1-436-mod-wrapper(IBusinessTrip)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps1-436-mod-split(PaymentSystem)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps1-436-mod-split(PaymentSystem)-wrapper(IExporter)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 110.5f,
					"stplus-ps1-436-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps1-436-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps1-436-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps1-436-mod-split(PaymentSystem)-wrapper(IBooking)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps1-436-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps1-444-mod-wrapper(IExporter)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 110.5f, "stplus-ps1-444-mod-wrapper(ITripDB)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps1-444-mod-wrapper(IExternalPayment)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps1-444-mod-wrapper(IEmployeePayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps1-444-mod-wrapper(IBooking)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps1-444-mod-wrapper(IBusinessTrip)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps1-444-mod-split(PaymentSystem)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps1-444-mod-split(PaymentSystem)-wrapper(IExporter)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 110.5f,
					"stplus-ps1-444-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps1-444-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps1-444-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps1-444-mod-split(PaymentSystem)-wrapper(IBooking)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps1-444-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 251.0f, "stplus-ps1-494-mod-wrapper(IExporter)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 171.0f, "stplus-ps1-494-mod-wrapper(ITripDB)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 6, */ 251.0f, "stplus-ps1-494-mod-wrapper(IExternalPayment)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 6, */ 251.0f, "stplus-ps1-494-mod-wrapper(IEmployeePayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 251.0f, "stplus-ps1-494-mod-wrapper(IBooking)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 6, */ 251.0f, "stplus-ps1-494-mod-wrapper(IBusinessTrip)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 251.0f, "stplus-ps1-494-mod-split(PaymentSystem)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 251.0f,
					"stplus-ps1-494-mod-split(PaymentSystem)-wrapper(IExporter)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 171.0f,
					"stplus-ps1-494-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 251.0f,
					"stplus-ps1-494-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 251.0f,
					"stplus-ps1-494-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 251.0f,
					"stplus-ps1-494-mod-split(PaymentSystem)-wrapper(IBooking)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 251.0f,
					"stplus-ps1-494-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps1-64-mod-wrapper(IExporter)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 110.5f, "stplus-ps1-64-mod-wrapper(ITripDB)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps1-64-mod-wrapper(IExternalPayment)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps1-64-mod-wrapper(IEmployeePayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps1-64-mod-wrapper(IBooking)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps1-64-mod-wrapper(IBusinessTrip)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps1-64-mod-split(PaymentSystem)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps1-64-mod-split(PaymentSystem)-wrapper(IExporter)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 110.5f,
					"stplus-ps1-64-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps1-64-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps1-64-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps1-64-mod-split(PaymentSystem)-wrapper(IBooking)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps1-64-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.0f, "stplus-ps2-209-mod-wrapper(IExporter)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 110.5f, "stplus-ps2-209-mod-wrapper(ITripDB)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 6, */ 231.0f, "stplus-ps2-209-mod-wrapper(IExternalPayment)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 6, */ 231.0f, "stplus-ps2-209-mod-wrapper(IEmployeePayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.0f, "stplus-ps2-209-mod-wrapper(IBooking)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 6, */ 231.0f, "stplus-ps2-209-mod-wrapper(IBusinessTrip)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.0f, "stplus-ps2-209-mod-split(PaymentSystem)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.0f,
					"stplus-ps2-209-mod-split(PaymentSystem)-wrapper(IExporter)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 110.5f,
					"stplus-ps2-209-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.0f,
					"stplus-ps2-209-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.0f,
					"stplus-ps2-209-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.0f,
					"stplus-ps2-209-mod-split(PaymentSystem)-wrapper(IBooking)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.0f,
					"stplus-ps2-209-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps2-22-mod-wrapper(IExporter)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 110.5f, "stplus-ps2-22-mod-wrapper(ITripDB)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps2-22-mod-wrapper(IExternalPayment)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps2-22-mod-wrapper(IEmployeePayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps2-22-mod-wrapper(IBooking)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps2-22-mod-wrapper(IBusinessTrip)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps2-22-mod-split(PaymentSystem)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps2-22-mod-split(PaymentSystem)-wrapper(IExporter)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 110.5f,
					"stplus-ps2-22-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps2-22-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps2-22-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps2-22-mod-split(PaymentSystem)-wrapper(IBooking)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps2-22-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps2-325-mod-wrapper(IExporter)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 110.5f, "stplus-ps2-325-mod-wrapper(ITripDB)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps2-325-mod-wrapper(IExternalPayment)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps2-325-mod-wrapper(IEmployeePayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps2-325-mod-wrapper(IBooking)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps2-325-mod-wrapper(IBusinessTrip)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps2-325-mod-split(PaymentSystem)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps2-325-mod-split(PaymentSystem)-wrapper(IExporter)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 110.5f,
					"stplus-ps2-325-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps2-325-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps2-325-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps2-325-mod-split(PaymentSystem)-wrapper(IBooking)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps2-325-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 251.0f, "stplus-ps2-330-mod-wrapper(IExporter)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 171.0f, "stplus-ps2-330-mod-wrapper(ITripDB)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 6, */ 251.0f, "stplus-ps2-330-mod-wrapper(IExternalPayment)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 6, */ 251.0f, "stplus-ps2-330-mod-wrapper(IEmployeePayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 251.0f, "stplus-ps2-330-mod-wrapper(IBooking)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 6, */ 251.0f, "stplus-ps2-330-mod-wrapper(IBusinessTrip)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 251.0f, "stplus-ps2-330-mod-split(PaymentSystem)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 251.0f,
					"stplus-ps2-330-mod-split(PaymentSystem)-wrapper(IExporter)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 171.0f,
					"stplus-ps2-330-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 251.0f,
					"stplus-ps2-330-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 251.0f,
					"stplus-ps2-330-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 251.0f,
					"stplus-ps2-330-mod-split(PaymentSystem)-wrapper(IBooking)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 251.0f,
					"stplus-ps2-330-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.0f, "stplus-ps2-358-mod-wrapper(IExporter)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 110.5f, "stplus-ps2-358-mod-wrapper(ITripDB)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 6, */ 231.0f, "stplus-ps2-358-mod-wrapper(IExternalPayment)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 6, */ 231.0f, "stplus-ps2-358-mod-wrapper(IEmployeePayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.0f, "stplus-ps2-358-mod-wrapper(IBooking)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 6, */ 231.0f, "stplus-ps2-358-mod-wrapper(IBusinessTrip)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.0f, "stplus-ps2-358-mod-split(PaymentSystem)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.0f,
					"stplus-ps2-358-mod-split(PaymentSystem)-wrapper(IExporter)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 110.5f,
					"stplus-ps2-358-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.0f,
					"stplus-ps2-358-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.0f,
					"stplus-ps2-358-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.0f,
					"stplus-ps2-358-mod-split(PaymentSystem)-wrapper(IBooking)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.0f,
					"stplus-ps2-358-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps2-366-mod-wrapper(IExporter)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 110.5f, "stplus-ps2-366-mod-wrapper(ITripDB)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps2-366-mod-wrapper(IExternalPayment)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps2-366-mod-wrapper(IEmployeePayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps2-366-mod-wrapper(IBooking)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps2-366-mod-wrapper(IBusinessTrip)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps2-366-mod-split(PaymentSystem)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps2-366-mod-split(PaymentSystem)-wrapper(IExporter)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 110.5f,
					"stplus-ps2-366-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps2-366-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps2-366-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps2-366-mod-split(PaymentSystem)-wrapper(IBooking)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps2-366-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 251.0f, "stplus-ps2-416-mod-wrapper(IExporter)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 171.0f, "stplus-ps2-416-mod-wrapper(ITripDB)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 6, */ 251.0f, "stplus-ps2-416-mod-wrapper(IExternalPayment)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 6, */ 251.0f, "stplus-ps2-416-mod-wrapper(IEmployeePayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 251.0f, "stplus-ps2-416-mod-wrapper(IBooking)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 6, */ 251.0f, "stplus-ps2-416-mod-wrapper(IBusinessTrip)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 251.0f, "stplus-ps2-416-mod-split(PaymentSystem)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 251.0f,
					"stplus-ps2-416-mod-split(PaymentSystem)-wrapper(IExporter)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 171.0f,
					"stplus-ps2-416-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 251.0f,
					"stplus-ps2-416-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 251.0f,
					"stplus-ps2-416-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 251.0f,
					"stplus-ps2-416-mod-split(PaymentSystem)-wrapper(IBooking)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 251.0f,
					"stplus-ps2-416-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 251.0f, "stplus-ps2-476-mod-wrapper(IExporter)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 171.0f, "stplus-ps2-476-mod-wrapper(ITripDB)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 6, */ 251.0f, "stplus-ps2-476-mod-wrapper(IExternalPayment)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 6, */ 251.0f, "stplus-ps2-476-mod-wrapper(IEmployeePayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 251.0f, "stplus-ps2-476-mod-wrapper(IBooking)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 6, */ 251.0f, "stplus-ps2-476-mod-wrapper(IBusinessTrip)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 251.0f, "stplus-ps2-476-mod-split(PaymentSystem)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 251.0f,
					"stplus-ps2-476-mod-split(PaymentSystem)-wrapper(IExporter)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 171.0f,
					"stplus-ps2-476-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 251.0f,
					"stplus-ps2-476-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 251.0f,
					"stplus-ps2-476-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 251.0f,
					"stplus-ps2-476-mod-split(PaymentSystem)-wrapper(IBooking)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 251.0f,
					"stplus-ps2-476-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 7, */ 291.5f, "stplus-ps2-479-mod-wrapper(IExporter)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 171.0f, "stplus-ps2-479-mod-wrapper(ITripDB)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 7, */ 291.5f, "stplus-ps2-479-mod-wrapper(IExternalPayment)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 7, */ 291.5f, "stplus-ps2-479-mod-wrapper(IEmployeePayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 7, */ 291.5f, "stplus-ps2-479-mod-wrapper(IBooking)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 7, */ 291.5f, "stplus-ps2-479-mod-wrapper(IBusinessTrip)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 7, */ 291.5f, "stplus-ps2-479-mod-split(PaymentSystem)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 7, */ 291.5f,
					"stplus-ps2-479-mod-split(PaymentSystem)-wrapper(IExporter)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 171.0f,
					"stplus-ps2-479-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 7, */ 291.5f,
					"stplus-ps2-479-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 7, */ 291.5f,
					"stplus-ps2-479-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 7, */ 291.5f,
					"stplus-ps2-479-mod-split(PaymentSystem)-wrapper(IBooking)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 7, */ 291.5f,
					"stplus-ps2-479-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps2-480-mod-wrapper(IExporter)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 110.5f, "stplus-ps2-480-mod-wrapper(ITripDB)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps2-480-mod-wrapper(IExternalPayment)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps2-480-mod-wrapper(IEmployeePayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps2-480-mod-wrapper(IBooking)"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps2-480-mod-wrapper(IBusinessTrip)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f, "stplus-ps2-480-mod-split(PaymentSystem)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps2-480-mod-split(PaymentSystem)-wrapper(IExporter)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 4, */ 110.5f,
					"stplus-ps2-480-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps2-480-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps2-480-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps2-480-mod-split(PaymentSystem)-wrapper(IBooking)"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 5, */ 190.5f,
					"stplus-ps2-480-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));

			// Second level - Performance tactics applied over Modifiability candidates
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-split(PaymentSystem)-ps1-136"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-split(PaymentSystem)-ps1-162"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-split(PaymentSystem)-ps1-208"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-split(PaymentSystem)-ps1-232"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-split(PaymentSystem)-ps1-239"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-split(PaymentSystem)-ps1-316"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-split(PaymentSystem)-ps1-321"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-split(PaymentSystem)-ps1-355"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-split(PaymentSystem)-ps1-367"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-split(PaymentSystem)-ps1-419"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IExporter)-ps1-66"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IExporter)-ps1-141"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IExporter)-ps1-227"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IExporter)-ps1-238"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IExporter)-ps1-275"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IExporter)-ps1-329"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IExporter)-ps1-450"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IExporter)-ps1-459"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IExporter)-ps1-494"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IExporter)-ps1-500"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.5f, "stplus-mod-wrapper(ITripDB)-ps1-161"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.5f, "stplus-mod-wrapper(ITripDB)-ps1-167"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.5f, "stplus-mod-wrapper(ITripDB)-ps1-228"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.5f, "stplus-mod-wrapper(ITripDB)-ps1-233"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.5f, "stplus-mod-wrapper(ITripDB)-ps1-353"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.5f, "stplus-mod-wrapper(ITripDB)-ps1-354"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.5f, "stplus-mod-wrapper(ITripDB)-ps1-357"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.5f, "stplus-mod-wrapper(ITripDB)-ps1-358"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.5f, "stplus-mod-wrapper(ITripDB)-ps1-432"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.5f, "stplus-mod-wrapper(ITripDB)-ps1-476"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IExternalPayment)-ps1-195"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IExternalPayment)-ps1-247"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IExternalPayment)-ps1-263"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IExternalPayment)-ps1-333"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IExternalPayment)-ps1-350"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IExternalPayment)-ps1-396"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IExternalPayment)-ps1-405"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IExternalPayment)-ps1-425"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IExternalPayment)-ps1-426"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IExternalPayment)-ps1-464"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IEmployeePayment)-ps1-109"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IEmployeePayment)-ps1-133"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IEmployeePayment)-ps1-138"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IEmployeePayment)-ps1-311"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IEmployeePayment)-ps1-330"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IEmployeePayment)-ps1-41"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IEmployeePayment)-ps1-416"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IEmployeePayment)-ps1-417"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IEmployeePayment)-ps1-447"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IEmployeePayment)-ps1-497"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IBooking)-ps1-154"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IBooking)-ps1-229"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IBooking)-ps1-237"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IBooking)-ps1-269"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IBooking)-ps1-277"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IBooking)-ps1-282"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IBooking)-ps1-316"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IBooking)-ps1-364"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IBooking)-ps1-404"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IBooking)-ps1-450"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IBusinessTrip)-ps1-110"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IBusinessTrip)-ps1-142"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IBusinessTrip)-ps1-216"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IBusinessTrip)-ps1-232"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IBusinessTrip)-ps1-295"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IBusinessTrip)-ps1-296"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IBusinessTrip)-ps1-442"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IBusinessTrip)-ps1-472"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IBusinessTrip)-ps1-474"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IBusinessTrip)-ps1-478"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps1-118"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps1-275"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps1-351"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps1-355"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps1-382"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps1-389"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps1-390"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps1-406"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps1-425"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps1-428"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.5f,
					"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps1-220"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.5f,
					"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps1-290"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.5f,
					"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps1-327"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.5f,
					"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps1-377"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.5f,
					"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps1-406"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.5f,
					"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps1-423"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.5f,
					"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps1-439"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.5f,
					"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps1-446"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.5f,
					"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps1-447"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.5f,
					"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps1-454"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps1-197"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps1-232"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps1-233"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps1-235"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps1-276"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps1-277"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps1-33"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps1-355"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps1-362"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps1-368"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps1-12"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps1-13"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps1-176"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps1-224"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps1-281"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps1-318"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps1-356"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps1-402"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps1-438"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps1-483"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps1-100"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps1-145"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps1-232"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps1-300"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps1-326"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps1-353"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps1-400"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps1-474"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps1-488"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps1-86"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps1-319"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps1-321"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps1-323"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps1-356"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps1-392"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps1-397"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps1-426"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps1-427"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps1-433"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps1-62"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-split(PaymentSystem)-ps2-119"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-split(PaymentSystem)-ps2-179"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-split(PaymentSystem)-ps2-191"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-split(PaymentSystem)-ps2-222"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-split(PaymentSystem)-ps2-239"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-split(PaymentSystem)-ps2-402"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-split(PaymentSystem)-ps2-43"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-split(PaymentSystem)-ps2-432"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-split(PaymentSystem)-ps2-470"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-split(PaymentSystem)-ps2-69"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IExporter)-ps2-146"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IExporter)-ps2-148"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IExporter)-ps2-162"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IExporter)-ps2-243"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IExporter)-ps2-270"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IExporter)-ps2-281"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IExporter)-ps2-318"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IExporter)-ps2-324"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IExporter)-ps2-52"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IExporter)-ps2-90"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.5f, "stplus-mod-wrapper(ITripDB)-ps2-143"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.5f, "stplus-mod-wrapper(ITripDB)-ps2-189"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.5f, "stplus-mod-wrapper(ITripDB)-ps2-192"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.5f, "stplus-mod-wrapper(ITripDB)-ps2-231"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.5f, "stplus-mod-wrapper(ITripDB)-ps2-309"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.5f, "stplus-mod-wrapper(ITripDB)-ps2-314"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.5f, "stplus-mod-wrapper(ITripDB)-ps2-33"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.5f, "stplus-mod-wrapper(ITripDB)-ps2-347"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.5f, "stplus-mod-wrapper(ITripDB)-ps2-354"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.5f, "stplus-mod-wrapper(ITripDB)-ps2-86"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IExternalPayment)-ps2-145"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IExternalPayment)-ps2-147"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IExternalPayment)-ps2-270"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IExternalPayment)-ps2-275"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IExternalPayment)-ps2-324"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IExternalPayment)-ps2-354"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IExternalPayment)-ps2-369"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IExternalPayment)-ps2-445"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IExternalPayment)-ps2-449"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IExternalPayment)-ps2-453"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IEmployeePayment)-ps2-130"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IEmployeePayment)-ps2-237"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IEmployeePayment)-ps2-257"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IEmployeePayment)-ps2-310"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IEmployeePayment)-ps2-358"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IEmployeePayment)-ps2-361"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IEmployeePayment)-ps2-437"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IEmployeePayment)-ps2-441"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IEmployeePayment)-ps2-489"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IEmployeePayment)-ps2-496"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IBooking)-ps2-117"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IBooking)-ps2-143"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IBooking)-ps2-237"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IBooking)-ps2-286"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IBooking)-ps2-299"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IBooking)-ps2-330"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IBooking)-ps2-375"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IBooking)-ps2-376"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IBooking)-ps2-384"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IBooking)-ps2-401"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IBusinessTrip)-ps2-107"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IBusinessTrip)-ps2-130"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IBusinessTrip)-ps2-156"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IBusinessTrip)-ps2-238"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IBusinessTrip)-ps2-276"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IBusinessTrip)-ps2-282"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IBusinessTrip)-ps2-440"));
			m2Bot.insertInOrder(
					new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IBusinessTrip)-ps2-442"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IBusinessTrip)-ps2-90"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f, "stplus-mod-wrapper(IBusinessTrip)-ps2-98"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps2-143"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps2-191"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps2-268"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps2-307"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps2-314"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps2-352"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps2-354"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps2-355"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps2-55"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps2-7"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.5f,
					"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps2-137"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.5f,
					"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps2-142"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.5f,
					"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps2-171"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.5f,
					"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps2-189"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.5f,
					"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps2-209"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.5f,
					"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps2-238"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.5f,
					"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps2-275"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.5f,
					"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps2-281"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.5f,
					"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps2-285"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 6, */ 231.5f,
					"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps2-414"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps2-192"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps2-203"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps2-275"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps2-279"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps2-360"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps2-364"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps2-403"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps2-439"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps2-450"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps2-492"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps2-250"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps2-321"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps2-33"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps2-359"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps2-360"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps2-369"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps2-370"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps2-456"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps2-499"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps2-501"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps2-226"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps2-325"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps2-401"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps2-415"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps2-452"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps2-456"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps2-462"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps2-505"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps2-506"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps2-508"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps2-119"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps2-242"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps2-278"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps2-344"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps2-373"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps2-407"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps2-408"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps2-439"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps2-447"));
			m2Bot.insertInOrder(new ModifiabilityProposal(/* 8, */ 352.0f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps2-482"));

			// First level - Modifiability tactics
			p1Bot.insertInOrder(new PerformanceProposal(111.7639f, "stplus-mod-split(PaymentSystem)"));
			p1Bot.insertInOrder(new PerformanceProposal(152.77259999999998f, "stplus-mod-wrapper(IExporter)"));
			p1Bot.insertInOrder(new PerformanceProposal(112.94460000000001f, "stplus-mod-wrapper(ITripDB)"));
			p1Bot.insertInOrder(new PerformanceProposal(119.5708f, "stplus-mod-wrapper(IExternalPayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(112.6332f, "stplus-mod-wrapper(IEmployeePayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(132.47140000000002f, "stplus-mod-wrapper(IBooking)"));
			p1Bot.insertInOrder(new PerformanceProposal(111.7639f, "stplus-mod-wrapper(IBusinessTrip)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(152.77259999999998f, "stplus-mod-split(PaymentSystem)-wrapper(IExporter)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(112.94460000000001f, "stplus-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(119.5708f, "stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(112.6332f, "stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(132.47140000000002f, "stplus-mod-split(PaymentSystem)-wrapper(IBooking)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(111.7639f, "stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));

			// First level - Performance tactics
			p1Bot.insertInOrder(new PerformanceProposal(10.83839f, "stplus-ps1-258"));
			p1Bot.insertInOrder(new PerformanceProposal(10.25568f, "stplus-ps1-281"));
			p1Bot.insertInOrder(new PerformanceProposal(12.312850000000001f, "stplus-ps1-338"));
			p1Bot.insertInOrder(new PerformanceProposal(13.67052f, "stplus-ps1-340"));
			p1Bot.insertInOrder(new PerformanceProposal(13.10291f, "stplus-ps1-397"));
			p1Bot.insertInOrder(new PerformanceProposal(14.126349999999999f, "stplus-ps1-404"));
			p1Bot.insertInOrder(new PerformanceProposal(8.29862f, "stplus-ps1-436"));
			p1Bot.insertInOrder(new PerformanceProposal(12.59697f, "stplus-ps1-444"));
			p1Bot.insertInOrder(new PerformanceProposal(10.77821f, "stplus-ps1-494"));
			p1Bot.insertInOrder(new PerformanceProposal(11.51662f, "stplus-ps1-64"));
			p1Bot.insertInOrder(new PerformanceProposal(14.51197f, "stplus-ps2-209"));
			p1Bot.insertInOrder(new PerformanceProposal(20.55931f, "stplus-ps2-22"));
			p1Bot.insertInOrder(new PerformanceProposal(26.76549f, "stplus-ps2-325"));
			p1Bot.insertInOrder(new PerformanceProposal(26.021819999999998f, "stplus-ps2-330"));
			p1Bot.insertInOrder(new PerformanceProposal(21.51209f, "stplus-ps2-358"));
			p1Bot.insertInOrder(new PerformanceProposal(21.00345f, "stplus-ps2-366"));
			p1Bot.insertInOrder(new PerformanceProposal(12.170570000000001f, "stplus-ps2-416"));
			p1Bot.insertInOrder(new PerformanceProposal(28.2282f, "stplus-ps2-476"));
			p1Bot.insertInOrder(new PerformanceProposal(18.98772f, "stplus-ps2-479"));
			p1Bot.insertInOrder(new PerformanceProposal(19.53763f, "stplus-ps2-480"));

			// Second level - Modifiability tactics applied over Performance candidates
			p1Bot.insertInOrder(new PerformanceProposal(10.93995f, "stplus-ps1-258-mod-wrapper(IExporter)"));
			p1Bot.insertInOrder(new PerformanceProposal(11.339970000000001f, "stplus-ps1-258-mod-wrapper(ITripDB)"));
			p1Bot.insertInOrder(new PerformanceProposal(14.3581f, "stplus-ps1-258-mod-wrapper(IExternalPayment)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(11.432549999999999f, "stplus-ps1-258-mod-wrapper(IEmployeePayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(14.3576f, "stplus-ps1-258-mod-wrapper(IBooking)"));
			p1Bot.insertInOrder(new PerformanceProposal(11.99521f, "stplus-ps1-258-mod-wrapper(IBusinessTrip)"));
			p1Bot.insertInOrder(new PerformanceProposal(10.83839f, "stplus-ps1-258-mod-split(PaymentSystem)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(10.93995f, "stplus-ps1-258-mod-split(PaymentSystem)-wrapper(IExporter)"));
			p1Bot.insertInOrder(new PerformanceProposal(11.339970000000001f,
					"stplus-ps1-258-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			p1Bot.insertInOrder(new PerformanceProposal(14.3581f,
					"stplus-ps1-258-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(11.432549999999999f,
					"stplus-ps1-258-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(14.3576f, "stplus-ps1-258-mod-split(PaymentSystem)-wrapper(IBooking)"));
			p1Bot.insertInOrder(new PerformanceProposal(11.99521f,
					"stplus-ps1-258-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			p1Bot.insertInOrder(new PerformanceProposal(10.60364f, "stplus-ps1-281-mod-wrapper(IExporter)"));
			p1Bot.insertInOrder(new PerformanceProposal(12.525360000000001f, "stplus-ps1-281-mod-wrapper(ITripDB)"));
			p1Bot.insertInOrder(new PerformanceProposal(11.65164f, "stplus-ps1-281-mod-wrapper(IExternalPayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(10.56804f, "stplus-ps1-281-mod-wrapper(IEmployeePayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(11.651209999999999f, "stplus-ps1-281-mod-wrapper(IBooking)"));
			p1Bot.insertInOrder(new PerformanceProposal(12.55792f, "stplus-ps1-281-mod-wrapper(IBusinessTrip)"));
			p1Bot.insertInOrder(new PerformanceProposal(10.25568f, "stplus-ps1-281-mod-split(PaymentSystem)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(10.60364f, "stplus-ps1-281-mod-split(PaymentSystem)-wrapper(IExporter)"));
			p1Bot.insertInOrder(new PerformanceProposal(12.525360000000001f,
					"stplus-ps1-281-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			p1Bot.insertInOrder(new PerformanceProposal(11.65164f,
					"stplus-ps1-281-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(10.56804f,
					"stplus-ps1-281-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(11.651209999999999f,
					"stplus-ps1-281-mod-split(PaymentSystem)-wrapper(IBooking)"));
			p1Bot.insertInOrder(new PerformanceProposal(12.55792f,
					"stplus-ps1-281-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			p1Bot.insertInOrder(new PerformanceProposal(12.44062f, "stplus-ps1-338-mod-wrapper(IExporter)"));
			p1Bot.insertInOrder(new PerformanceProposal(13.10613f, "stplus-ps1-338-mod-wrapper(ITripDB)"));
			p1Bot.insertInOrder(new PerformanceProposal(13.10167f, "stplus-ps1-338-mod-wrapper(IExternalPayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(12.4722f, "stplus-ps1-338-mod-wrapper(IEmployeePayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(13.55829f, "stplus-ps1-338-mod-wrapper(IBooking)"));
			p1Bot.insertInOrder(new PerformanceProposal(13.11522f, "stplus-ps1-338-mod-wrapper(IBusinessTrip)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(12.312850000000001f, "stplus-ps1-338-mod-split(PaymentSystem)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(12.44062f, "stplus-ps1-338-mod-split(PaymentSystem)-wrapper(IExporter)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(13.10613f, "stplus-ps1-338-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			p1Bot.insertInOrder(new PerformanceProposal(13.10167f,
					"stplus-ps1-338-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(12.4722f,
					"stplus-ps1-338-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(13.55829f, "stplus-ps1-338-mod-split(PaymentSystem)-wrapper(IBooking)"));
			p1Bot.insertInOrder(new PerformanceProposal(13.11522f,
					"stplus-ps1-338-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			p1Bot.insertInOrder(new PerformanceProposal(14.38377f, "stplus-ps1-340-mod-wrapper(IExporter)"));
			p1Bot.insertInOrder(new PerformanceProposal(14.128359999999999f, "stplus-ps1-340-mod-wrapper(ITripDB)"));
			p1Bot.insertInOrder(new PerformanceProposal(16.72106f, "stplus-ps1-340-mod-wrapper(IExternalPayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(14.30196f, "stplus-ps1-340-mod-wrapper(IEmployeePayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(13.91916f, "stplus-ps1-340-mod-wrapper(IBooking)"));
			p1Bot.insertInOrder(new PerformanceProposal(14.0997f, "stplus-ps1-340-mod-wrapper(IBusinessTrip)"));
			p1Bot.insertInOrder(new PerformanceProposal(13.67052f, "stplus-ps1-340-mod-split(PaymentSystem)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(14.38377f, "stplus-ps1-340-mod-split(PaymentSystem)-wrapper(IExporter)"));
			p1Bot.insertInOrder(new PerformanceProposal(14.128359999999999f,
					"stplus-ps1-340-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			p1Bot.insertInOrder(new PerformanceProposal(16.72106f,
					"stplus-ps1-340-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(14.30196f,
					"stplus-ps1-340-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(13.91916f, "stplus-ps1-340-mod-split(PaymentSystem)-wrapper(IBooking)"));
			p1Bot.insertInOrder(new PerformanceProposal(14.0997f,
					"stplus-ps1-340-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			p1Bot.insertInOrder(new PerformanceProposal(13.41306f, "stplus-ps1-397-mod-wrapper(IExporter)"));
			p1Bot.insertInOrder(new PerformanceProposal(14.82484f, "stplus-ps1-397-mod-wrapper(ITripDB)"));
			p1Bot.insertInOrder(new PerformanceProposal(14.13226f, "stplus-ps1-397-mod-wrapper(IExternalPayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(13.37513f, "stplus-ps1-397-mod-wrapper(IEmployeePayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(14.131920000000001f, "stplus-ps1-397-mod-wrapper(IBooking)"));
			p1Bot.insertInOrder(new PerformanceProposal(14.79026f, "stplus-ps1-397-mod-wrapper(IBusinessTrip)"));
			p1Bot.insertInOrder(new PerformanceProposal(13.10291f, "stplus-ps1-397-mod-split(PaymentSystem)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(13.41306f, "stplus-ps1-397-mod-split(PaymentSystem)-wrapper(IExporter)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(14.82484f, "stplus-ps1-397-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			p1Bot.insertInOrder(new PerformanceProposal(14.13226f,
					"stplus-ps1-397-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(13.37513f,
					"stplus-ps1-397-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(14.131920000000001f,
					"stplus-ps1-397-mod-split(PaymentSystem)-wrapper(IBooking)"));
			p1Bot.insertInOrder(new PerformanceProposal(14.79026f,
					"stplus-ps1-397-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			p1Bot.insertInOrder(new PerformanceProposal(14.279599999999999f, "stplus-ps1-404-mod-wrapper(IExporter)"));
			p1Bot.insertInOrder(new PerformanceProposal(14.92298f, "stplus-ps1-404-mod-wrapper(ITripDB)"));
			p1Bot.insertInOrder(new PerformanceProposal(18.77187f, "stplus-ps1-404-mod-wrapper(IExternalPayment)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(14.858799999999999f, "stplus-ps1-404-mod-wrapper(IEmployeePayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(18.76987f, "stplus-ps1-404-mod-wrapper(IBooking)"));
			p1Bot.insertInOrder(new PerformanceProposal(14.92561f, "stplus-ps1-404-mod-wrapper(IBusinessTrip)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(14.126349999999999f, "stplus-ps1-404-mod-split(PaymentSystem)"));
			p1Bot.insertInOrder(new PerformanceProposal(14.279599999999999f,
					"stplus-ps1-404-mod-split(PaymentSystem)-wrapper(IExporter)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(14.92298f, "stplus-ps1-404-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			p1Bot.insertInOrder(new PerformanceProposal(18.77187f,
					"stplus-ps1-404-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(14.858799999999999f,
					"stplus-ps1-404-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(18.76987f, "stplus-ps1-404-mod-split(PaymentSystem)-wrapper(IBooking)"));
			p1Bot.insertInOrder(new PerformanceProposal(14.92561f,
					"stplus-ps1-404-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			p1Bot.insertInOrder(new PerformanceProposal(8.492619999999999f, "stplus-ps1-436-mod-wrapper(IExporter)"));
			p1Bot.insertInOrder(new PerformanceProposal(9.56327f, "stplus-ps1-436-mod-wrapper(ITripDB)"));
			p1Bot.insertInOrder(new PerformanceProposal(9.05711f, "stplus-ps1-436-mod-wrapper(IExternalPayment)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(8.468589999999999f, "stplus-ps1-436-mod-wrapper(IEmployeePayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(9.05667f, "stplus-ps1-436-mod-wrapper(IBooking)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(9.557970000000001f, "stplus-ps1-436-mod-wrapper(IBusinessTrip)"));
			p1Bot.insertInOrder(new PerformanceProposal(8.29862f, "stplus-ps1-436-mod-split(PaymentSystem)"));
			p1Bot.insertInOrder(new PerformanceProposal(8.492619999999999f,
					"stplus-ps1-436-mod-split(PaymentSystem)-wrapper(IExporter)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(9.56327f, "stplus-ps1-436-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			p1Bot.insertInOrder(new PerformanceProposal(9.05711f,
					"stplus-ps1-436-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(8.468589999999999f,
					"stplus-ps1-436-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(9.05667f, "stplus-ps1-436-mod-split(PaymentSystem)-wrapper(IBooking)"));
			p1Bot.insertInOrder(new PerformanceProposal(9.557970000000001f,
					"stplus-ps1-436-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			p1Bot.insertInOrder(new PerformanceProposal(13.04698f, "stplus-ps1-444-mod-wrapper(IExporter)"));
			p1Bot.insertInOrder(new PerformanceProposal(15.27455f, "stplus-ps1-444-mod-wrapper(ITripDB)"));
			p1Bot.insertInOrder(new PerformanceProposal(14.2148f, "stplus-ps1-444-mod-wrapper(IExternalPayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(12.95466f, "stplus-ps1-444-mod-wrapper(IEmployeePayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(14.21432f, "stplus-ps1-444-mod-wrapper(IBooking)"));
			p1Bot.insertInOrder(new PerformanceProposal(13.72516f, "stplus-ps1-444-mod-wrapper(IBusinessTrip)"));
			p1Bot.insertInOrder(new PerformanceProposal(12.59697f, "stplus-ps1-444-mod-split(PaymentSystem)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(13.04698f, "stplus-ps1-444-mod-split(PaymentSystem)-wrapper(IExporter)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(15.27455f, "stplus-ps1-444-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			p1Bot.insertInOrder(new PerformanceProposal(14.2148f,
					"stplus-ps1-444-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(12.95466f,
					"stplus-ps1-444-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(14.21432f, "stplus-ps1-444-mod-split(PaymentSystem)-wrapper(IBooking)"));
			p1Bot.insertInOrder(new PerformanceProposal(13.72516f,
					"stplus-ps1-444-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			p1Bot.insertInOrder(new PerformanceProposal(10.870629999999998f, "stplus-ps1-494-mod-wrapper(IExporter)"));
			p1Bot.insertInOrder(new PerformanceProposal(11.384599999999999f, "stplus-ps1-494-mod-wrapper(ITripDB)"));
			p1Bot.insertInOrder(new PerformanceProposal(14.27804f, "stplus-ps1-494-mod-wrapper(IExternalPayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(11.34901f, "stplus-ps1-494-mod-wrapper(IEmployeePayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(14.277470000000001f, "stplus-ps1-494-mod-wrapper(IBooking)"));
			p1Bot.insertInOrder(new PerformanceProposal(11.38684f, "stplus-ps1-494-mod-wrapper(IBusinessTrip)"));
			p1Bot.insertInOrder(new PerformanceProposal(10.77821f, "stplus-ps1-494-mod-split(PaymentSystem)"));
			p1Bot.insertInOrder(new PerformanceProposal(10.870629999999998f,
					"stplus-ps1-494-mod-split(PaymentSystem)-wrapper(IExporter)"));
			p1Bot.insertInOrder(new PerformanceProposal(11.384599999999999f,
					"stplus-ps1-494-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			p1Bot.insertInOrder(new PerformanceProposal(14.27804f,
					"stplus-ps1-494-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(11.34901f,
					"stplus-ps1-494-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(14.277470000000001f,
					"stplus-ps1-494-mod-split(PaymentSystem)-wrapper(IBooking)"));
			p1Bot.insertInOrder(new PerformanceProposal(11.38684f,
					"stplus-ps1-494-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			p1Bot.insertInOrder(new PerformanceProposal(11.79327f, "stplus-ps1-64-mod-wrapper(IExporter)"));
			p1Bot.insertInOrder(new PerformanceProposal(13.17156f, "stplus-ps1-64-mod-wrapper(ITripDB)"));
			p1Bot.insertInOrder(new PerformanceProposal(12.65882f, "stplus-ps1-64-mod-wrapper(IExternalPayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(11.74493f, "stplus-ps1-64-mod-wrapper(IEmployeePayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(12.49846f, "stplus-ps1-64-mod-wrapper(IBooking)"));
			p1Bot.insertInOrder(new PerformanceProposal(13.93f, "stplus-ps1-64-mod-wrapper(IBusinessTrip)"));
			p1Bot.insertInOrder(new PerformanceProposal(11.51662f, "stplus-ps1-64-mod-split(PaymentSystem)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(11.79327f, "stplus-ps1-64-mod-split(PaymentSystem)-wrapper(IExporter)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(13.17156f, "stplus-ps1-64-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			p1Bot.insertInOrder(new PerformanceProposal(12.65882f,
					"stplus-ps1-64-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(11.74493f,
					"stplus-ps1-64-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(12.49846f, "stplus-ps1-64-mod-split(PaymentSystem)-wrapper(IBooking)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(13.93f, "stplus-ps1-64-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			p1Bot.insertInOrder(new PerformanceProposal(14.6781f, "stplus-ps2-209-mod-wrapper(IExporter)"));
			p1Bot.insertInOrder(new PerformanceProposal(15.27123f, "stplus-ps2-209-mod-wrapper(ITripDB)"));
			p1Bot.insertInOrder(new PerformanceProposal(22.63973f, "stplus-ps2-209-mod-wrapper(IExternalPayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(15.41826f, "stplus-ps2-209-mod-wrapper(IEmployeePayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(17.878999999999998f, "stplus-ps2-209-mod-wrapper(IBooking)"));
			p1Bot.insertInOrder(new PerformanceProposal(22.28275f, "stplus-ps2-209-mod-wrapper(IBusinessTrip)"));
			p1Bot.insertInOrder(new PerformanceProposal(14.51197f, "stplus-ps2-209-mod-split(PaymentSystem)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(14.6781f, "stplus-ps2-209-mod-split(PaymentSystem)-wrapper(IExporter)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(15.27123f, "stplus-ps2-209-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			p1Bot.insertInOrder(new PerformanceProposal(22.63973f,
					"stplus-ps2-209-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(15.41826f,
					"stplus-ps2-209-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(17.878999999999998f,
					"stplus-ps2-209-mod-split(PaymentSystem)-wrapper(IBooking)"));
			p1Bot.insertInOrder(new PerformanceProposal(22.28275f,
					"stplus-ps2-209-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			p1Bot.insertInOrder(new PerformanceProposal(21.613599999999998f, "stplus-ps2-22-mod-wrapper(IExporter)"));
			p1Bot.insertInOrder(new PerformanceProposal(21.21669f, "stplus-ps2-22-mod-wrapper(ITripDB)"));
			p1Bot.insertInOrder(new PerformanceProposal(20.92282f, "stplus-ps2-22-mod-wrapper(IExternalPayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(20.63689f, "stplus-ps2-22-mod-wrapper(IEmployeePayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(39.669200000000004f, "stplus-ps2-22-mod-wrapper(IBooking)"));
			p1Bot.insertInOrder(new PerformanceProposal(21.48976f, "stplus-ps2-22-mod-wrapper(IBusinessTrip)"));
			p1Bot.insertInOrder(new PerformanceProposal(20.55931f, "stplus-ps2-22-mod-split(PaymentSystem)"));
			p1Bot.insertInOrder(new PerformanceProposal(21.613599999999998f,
					"stplus-ps2-22-mod-split(PaymentSystem)-wrapper(IExporter)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(21.21669f, "stplus-ps2-22-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			p1Bot.insertInOrder(new PerformanceProposal(20.92282f,
					"stplus-ps2-22-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(20.63689f,
					"stplus-ps2-22-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(39.669200000000004f,
					"stplus-ps2-22-mod-split(PaymentSystem)-wrapper(IBooking)"));
			p1Bot.insertInOrder(new PerformanceProposal(21.48976f,
					"stplus-ps2-22-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			p1Bot.insertInOrder(new PerformanceProposal(29.002039999999997f, "stplus-ps2-325-mod-wrapper(IExporter)"));
			p1Bot.insertInOrder(new PerformanceProposal(27.29576f, "stplus-ps2-325-mod-wrapper(ITripDB)"));
			p1Bot.insertInOrder(new PerformanceProposal(42.5131f, "stplus-ps2-325-mod-wrapper(IExternalPayment)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(29.411260000000002f, "stplus-ps2-325-mod-wrapper(IEmployeePayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(42.5113f, "stplus-ps2-325-mod-wrapper(IBooking)"));
			p1Bot.insertInOrder(new PerformanceProposal(28.966f, "stplus-ps2-325-mod-wrapper(IBusinessTrip)"));
			p1Bot.insertInOrder(new PerformanceProposal(26.76549f, "stplus-ps2-325-mod-split(PaymentSystem)"));
			p1Bot.insertInOrder(new PerformanceProposal(29.002039999999997f,
					"stplus-ps2-325-mod-split(PaymentSystem)-wrapper(IExporter)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(27.29576f, "stplus-ps2-325-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			p1Bot.insertInOrder(new PerformanceProposal(42.5131f,
					"stplus-ps2-325-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(29.411260000000002f,
					"stplus-ps2-325-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(42.5113f, "stplus-ps2-325-mod-split(PaymentSystem)-wrapper(IBooking)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(28.966f, "stplus-ps2-325-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			p1Bot.insertInOrder(new PerformanceProposal(27.272199999999998f, "stplus-ps2-330-mod-wrapper(IExporter)"));
			p1Bot.insertInOrder(new PerformanceProposal(26.4703f, "stplus-ps2-330-mod-wrapper(ITripDB)"));
			p1Bot.insertInOrder(new PerformanceProposal(26.73758f, "stplus-ps2-330-mod-wrapper(IExternalPayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(26.17676f, "stplus-ps2-330-mod-wrapper(IEmployeePayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(26.73724f, "stplus-ps2-330-mod-wrapper(IBooking)"));
			p1Bot.insertInOrder(new PerformanceProposal(26.44102f, "stplus-ps2-330-mod-wrapper(IBusinessTrip)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(26.021819999999998f, "stplus-ps2-330-mod-split(PaymentSystem)"));
			p1Bot.insertInOrder(new PerformanceProposal(27.272199999999998f,
					"stplus-ps2-330-mod-split(PaymentSystem)-wrapper(IExporter)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(26.4703f, "stplus-ps2-330-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			p1Bot.insertInOrder(new PerformanceProposal(26.73758f,
					"stplus-ps2-330-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(26.17676f,
					"stplus-ps2-330-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(26.73724f, "stplus-ps2-330-mod-split(PaymentSystem)-wrapper(IBooking)"));
			p1Bot.insertInOrder(new PerformanceProposal(26.44102f,
					"stplus-ps2-330-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			p1Bot.insertInOrder(new PerformanceProposal(21.623089999999998f, "stplus-ps2-358-mod-wrapper(IExporter)"));
			p1Bot.insertInOrder(new PerformanceProposal(22.13669f, "stplus-ps2-358-mod-wrapper(ITripDB)"));
			p1Bot.insertInOrder(new PerformanceProposal(22.90915f, "stplus-ps2-358-mod-wrapper(IExternalPayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(21.77399f, "stplus-ps2-358-mod-wrapper(IEmployeePayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(23.031850000000002f, "stplus-ps2-358-mod-wrapper(IBooking)"));
			p1Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-ps2-358-mod-wrapper(IBusinessTrip)"));
			p1Bot.insertInOrder(new PerformanceProposal(21.51209f, "stplus-ps2-358-mod-split(PaymentSystem)"));
			p1Bot.insertInOrder(new PerformanceProposal(21.623089999999998f,
					"stplus-ps2-358-mod-split(PaymentSystem)-wrapper(IExporter)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(22.13669f, "stplus-ps2-358-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			p1Bot.insertInOrder(new PerformanceProposal(22.90915f,
					"stplus-ps2-358-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(21.77399f,
					"stplus-ps2-358-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(23.031850000000002f,
					"stplus-ps2-358-mod-split(PaymentSystem)-wrapper(IBooking)"));
			p1Bot.insertInOrder(new PerformanceProposal(999999999f,
					"stplus-ps2-358-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			p1Bot.insertInOrder(new PerformanceProposal(22.05999f, "stplus-ps2-366-mod-wrapper(IExporter)"));
			p1Bot.insertInOrder(new PerformanceProposal(21.504649999999998f, "stplus-ps2-366-mod-wrapper(ITripDB)"));
			p1Bot.insertInOrder(new PerformanceProposal(21.84139f, "stplus-ps2-366-mod-wrapper(IExternalPayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(21.20684f, "stplus-ps2-366-mod-wrapper(IEmployeePayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(40.1109f, "stplus-ps2-366-mod-wrapper(IBooking)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(22.730919999999998f, "stplus-ps2-366-mod-wrapper(IBusinessTrip)"));
			p1Bot.insertInOrder(new PerformanceProposal(21.00345f, "stplus-ps2-366-mod-split(PaymentSystem)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(22.05999f, "stplus-ps2-366-mod-split(PaymentSystem)-wrapper(IExporter)"));
			p1Bot.insertInOrder(new PerformanceProposal(21.504649999999998f,
					"stplus-ps2-366-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			p1Bot.insertInOrder(new PerformanceProposal(21.84139f,
					"stplus-ps2-366-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(21.20684f,
					"stplus-ps2-366-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(40.1109f, "stplus-ps2-366-mod-split(PaymentSystem)-wrapper(IBooking)"));
			p1Bot.insertInOrder(new PerformanceProposal(22.730919999999998f,
					"stplus-ps2-366-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			p1Bot.insertInOrder(new PerformanceProposal(12.539290000000001f, "stplus-ps2-416-mod-wrapper(IExporter)"));
			p1Bot.insertInOrder(new PerformanceProposal(12.706700000000001f, "stplus-ps2-416-mod-wrapper(ITripDB)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(13.599920000000001f, "stplus-ps2-416-mod-wrapper(IExternalPayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(12.46173f, "stplus-ps2-416-mod-wrapper(IEmployeePayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(12.475200000000001f, "stplus-ps2-416-mod-wrapper(IBooking)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(14.056719999999999f, "stplus-ps2-416-mod-wrapper(IBusinessTrip)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(12.170570000000001f, "stplus-ps2-416-mod-split(PaymentSystem)"));
			p1Bot.insertInOrder(new PerformanceProposal(12.539290000000001f,
					"stplus-ps2-416-mod-split(PaymentSystem)-wrapper(IExporter)"));
			p1Bot.insertInOrder(new PerformanceProposal(12.706700000000001f,
					"stplus-ps2-416-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			p1Bot.insertInOrder(new PerformanceProposal(13.599920000000001f,
					"stplus-ps2-416-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(12.46173f,
					"stplus-ps2-416-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(12.475200000000001f,
					"stplus-ps2-416-mod-split(PaymentSystem)-wrapper(IBooking)"));
			p1Bot.insertInOrder(new PerformanceProposal(14.056719999999999f,
					"stplus-ps2-416-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			p1Bot.insertInOrder(new PerformanceProposal(29.4814f, "stplus-ps2-476-mod-wrapper(IExporter)"));
			p1Bot.insertInOrder(new PerformanceProposal(28.5854f, "stplus-ps2-476-mod-wrapper(ITripDB)"));
			p1Bot.insertInOrder(new PerformanceProposal(31.0958f, "stplus-ps2-476-mod-wrapper(IExternalPayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(28.7494f, "stplus-ps2-476-mod-wrapper(IEmployeePayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(31.095399999999998f, "stplus-ps2-476-mod-wrapper(IBooking)"));
			p1Bot.insertInOrder(new PerformanceProposal(34.3263f, "stplus-ps2-476-mod-wrapper(IBusinessTrip)"));
			p1Bot.insertInOrder(new PerformanceProposal(28.2282f, "stplus-ps2-476-mod-split(PaymentSystem)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(29.4814f, "stplus-ps2-476-mod-split(PaymentSystem)-wrapper(IExporter)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(28.5854f, "stplus-ps2-476-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			p1Bot.insertInOrder(new PerformanceProposal(31.0958f,
					"stplus-ps2-476-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(28.7494f,
					"stplus-ps2-476-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(31.095399999999998f,
					"stplus-ps2-476-mod-split(PaymentSystem)-wrapper(IBooking)"));
			p1Bot.insertInOrder(new PerformanceProposal(34.3263f,
					"stplus-ps2-476-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			p1Bot.insertInOrder(new PerformanceProposal(19.110860000000002f, "stplus-ps2-479-mod-wrapper(IExporter)"));
			p1Bot.insertInOrder(new PerformanceProposal(19.60118f, "stplus-ps2-479-mod-wrapper(ITripDB)"));
			p1Bot.insertInOrder(new PerformanceProposal(24.37365f, "stplus-ps2-479-mod-wrapper(IExternalPayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(19.68762f, "stplus-ps2-479-mod-wrapper(IEmployeePayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(22.90472f, "stplus-ps2-479-mod-wrapper(IBooking)"));
			p1Bot.insertInOrder(new PerformanceProposal(28.797f, "stplus-ps2-479-mod-wrapper(IBusinessTrip)"));
			p1Bot.insertInOrder(new PerformanceProposal(18.98772f, "stplus-ps2-479-mod-split(PaymentSystem)"));
			p1Bot.insertInOrder(new PerformanceProposal(19.110860000000002f,
					"stplus-ps2-479-mod-split(PaymentSystem)-wrapper(IExporter)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(19.60118f, "stplus-ps2-479-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			p1Bot.insertInOrder(new PerformanceProposal(24.37365f,
					"stplus-ps2-479-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(19.68762f,
					"stplus-ps2-479-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(22.90472f, "stplus-ps2-479-mod-split(PaymentSystem)-wrapper(IBooking)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(28.797f, "stplus-ps2-479-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			p1Bot.insertInOrder(new PerformanceProposal(20.71125f, "stplus-ps2-480-mod-wrapper(IExporter)"));
			p1Bot.insertInOrder(new PerformanceProposal(20.15777f, "stplus-ps2-480-mod-wrapper(ITripDB)"));
			p1Bot.insertInOrder(new PerformanceProposal(25.29176f, "stplus-ps2-480-mod-wrapper(IExternalPayment)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(20.668599999999998f, "stplus-ps2-480-mod-wrapper(IEmployeePayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(25.29034f, "stplus-ps2-480-mod-wrapper(IBooking)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(20.173589999999997f, "stplus-ps2-480-mod-wrapper(IBusinessTrip)"));
			p1Bot.insertInOrder(new PerformanceProposal(19.53763f, "stplus-ps2-480-mod-split(PaymentSystem)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(20.71125f, "stplus-ps2-480-mod-split(PaymentSystem)-wrapper(IExporter)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(20.15777f, "stplus-ps2-480-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			p1Bot.insertInOrder(new PerformanceProposal(25.29176f,
					"stplus-ps2-480-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			p1Bot.insertInOrder(new PerformanceProposal(20.668599999999998f,
					"stplus-ps2-480-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			p1Bot.insertInOrder(
					new PerformanceProposal(25.29034f, "stplus-ps2-480-mod-split(PaymentSystem)-wrapper(IBooking)"));
			p1Bot.insertInOrder(new PerformanceProposal(20.173589999999997f,
					"stplus-ps2-480-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));

			// Second level - Performance tactics applied over Modifiability candidates
			p1Bot.insertInOrder(new PerformanceProposal(10.04971f, "stplus-mod-split(PaymentSystem)-ps1-136"));
			p1Bot.insertInOrder(
					new PerformanceProposal(6.5349699999999995f, "stplus-mod-split(PaymentSystem)-ps1-162"));
			p1Bot.insertInOrder(new PerformanceProposal(12.32199f, "stplus-mod-split(PaymentSystem)-ps1-208"));
			p1Bot.insertInOrder(new PerformanceProposal(10.79514f, "stplus-mod-split(PaymentSystem)-ps1-232"));
			p1Bot.insertInOrder(new PerformanceProposal(8.561119999999999f, "stplus-mod-split(PaymentSystem)-ps1-239"));
			p1Bot.insertInOrder(new PerformanceProposal(12.3828f, "stplus-mod-split(PaymentSystem)-ps1-316"));
			p1Bot.insertInOrder(new PerformanceProposal(6.941560000000001f, "stplus-mod-split(PaymentSystem)-ps1-321"));
			p1Bot.insertInOrder(new PerformanceProposal(11.79441f, "stplus-mod-split(PaymentSystem)-ps1-355"));
			p1Bot.insertInOrder(new PerformanceProposal(13.64124f, "stplus-mod-split(PaymentSystem)-ps1-367"));
			p1Bot.insertInOrder(new PerformanceProposal(12.05651f, "stplus-mod-split(PaymentSystem)-ps1-419"));
			p1Bot.insertInOrder(new PerformanceProposal(10.40522f, "stplus-mod-wrapper(IExporter)-ps1-66"));
			p1Bot.insertInOrder(new PerformanceProposal(11.73922f, "stplus-mod-wrapper(IExporter)-ps1-141"));
			p1Bot.insertInOrder(new PerformanceProposal(8.78465f, "stplus-mod-wrapper(IExporter)-ps1-227"));
			p1Bot.insertInOrder(new PerformanceProposal(15.20515f, "stplus-mod-wrapper(IExporter)-ps1-238"));
			p1Bot.insertInOrder(new PerformanceProposal(7.17182f, "stplus-mod-wrapper(IExporter)-ps1-275"));
			p1Bot.insertInOrder(new PerformanceProposal(14.83456f, "stplus-mod-wrapper(IExporter)-ps1-329"));
			p1Bot.insertInOrder(new PerformanceProposal(9.21671f, "stplus-mod-wrapper(IExporter)-ps1-450"));
			p1Bot.insertInOrder(new PerformanceProposal(14.18517f, "stplus-mod-wrapper(IExporter)-ps1-459"));
			p1Bot.insertInOrder(new PerformanceProposal(15.31011f, "stplus-mod-wrapper(IExporter)-ps1-494"));
			p1Bot.insertInOrder(new PerformanceProposal(14.83456f, "stplus-mod-wrapper(IExporter)-ps1-500"));
			p1Bot.insertInOrder(new PerformanceProposal(10.730160000000001f, "stplus-mod-wrapper(ITripDB)-ps1-161"));
			p1Bot.insertInOrder(new PerformanceProposal(11.44492f, "stplus-mod-wrapper(ITripDB)-ps1-167"));
			p1Bot.insertInOrder(new PerformanceProposal(10.24227f, "stplus-mod-wrapper(ITripDB)-ps1-228"));
			p1Bot.insertInOrder(new PerformanceProposal(15.11572f, "stplus-mod-wrapper(ITripDB)-ps1-233"));
			p1Bot.insertInOrder(new PerformanceProposal(14.350739999999998f, "stplus-mod-wrapper(ITripDB)-ps1-353"));
			p1Bot.insertInOrder(new PerformanceProposal(14.54304f, "stplus-mod-wrapper(ITripDB)-ps1-354"));
			p1Bot.insertInOrder(new PerformanceProposal(16.56364f, "stplus-mod-wrapper(ITripDB)-ps1-357"));
			p1Bot.insertInOrder(new PerformanceProposal(14.57493f, "stplus-mod-wrapper(ITripDB)-ps1-358"));
			p1Bot.insertInOrder(new PerformanceProposal(17.087899999999998f, "stplus-mod-wrapper(ITripDB)-ps1-432"));
			p1Bot.insertInOrder(new PerformanceProposal(14.57493f, "stplus-mod-wrapper(ITripDB)-ps1-476"));
			p1Bot.insertInOrder(new PerformanceProposal(13.27216f, "stplus-mod-wrapper(IExternalPayment)-ps1-195"));
			p1Bot.insertInOrder(new PerformanceProposal(11.68532f, "stplus-mod-wrapper(IExternalPayment)-ps1-247"));
			p1Bot.insertInOrder(new PerformanceProposal(13.27216f, "stplus-mod-wrapper(IExternalPayment)-ps1-263"));
			p1Bot.insertInOrder(new PerformanceProposal(11.6333f, "stplus-mod-wrapper(IExternalPayment)-ps1-333"));
			p1Bot.insertInOrder(
					new PerformanceProposal(15.613790000000002f, "stplus-mod-wrapper(IExternalPayment)-ps1-350"));
			p1Bot.insertInOrder(new PerformanceProposal(15.03256f, "stplus-mod-wrapper(IExternalPayment)-ps1-396"));
			p1Bot.insertInOrder(new PerformanceProposal(11.4723f, "stplus-mod-wrapper(IExternalPayment)-ps1-405"));
			p1Bot.insertInOrder(new PerformanceProposal(11.89181f, "stplus-mod-wrapper(IExternalPayment)-ps1-425"));
			p1Bot.insertInOrder(new PerformanceProposal(15.17168f, "stplus-mod-wrapper(IExternalPayment)-ps1-426"));
			p1Bot.insertInOrder(
					new PerformanceProposal(11.033100000000001f, "stplus-mod-wrapper(IExternalPayment)-ps1-464"));
			p1Bot.insertInOrder(new PerformanceProposal(9.41847f, "stplus-mod-wrapper(IEmployeePayment)-ps1-109"));
			p1Bot.insertInOrder(
					new PerformanceProposal(8.638819999999999f, "stplus-mod-wrapper(IEmployeePayment)-ps1-133"));
			p1Bot.insertInOrder(new PerformanceProposal(9.27844f, "stplus-mod-wrapper(IEmployeePayment)-ps1-138"));
			p1Bot.insertInOrder(new PerformanceProposal(9.79492f, "stplus-mod-wrapper(IEmployeePayment)-ps1-311"));
			p1Bot.insertInOrder(
					new PerformanceProposal(9.095369999999999f, "stplus-mod-wrapper(IEmployeePayment)-ps1-330"));
			p1Bot.insertInOrder(new PerformanceProposal(8.65301f, "stplus-mod-wrapper(IEmployeePayment)-ps1-41"));
			p1Bot.insertInOrder(new PerformanceProposal(8.55433f, "stplus-mod-wrapper(IEmployeePayment)-ps1-416"));
			p1Bot.insertInOrder(new PerformanceProposal(8.51107f, "stplus-mod-wrapper(IEmployeePayment)-ps1-417"));
			p1Bot.insertInOrder(
					new PerformanceProposal(8.448640000000001f, "stplus-mod-wrapper(IEmployeePayment)-ps1-447"));
			p1Bot.insertInOrder(new PerformanceProposal(8.50751f, "stplus-mod-wrapper(IEmployeePayment)-ps1-497"));
			p1Bot.insertInOrder(new PerformanceProposal(10.35059f, "stplus-mod-wrapper(IBooking)-ps1-154"));
			p1Bot.insertInOrder(new PerformanceProposal(11.29287f, "stplus-mod-wrapper(IBooking)-ps1-229"));
			p1Bot.insertInOrder(new PerformanceProposal(10.48482f, "stplus-mod-wrapper(IBooking)-ps1-237"));
			p1Bot.insertInOrder(new PerformanceProposal(8.936499999999999f, "stplus-mod-wrapper(IBooking)-ps1-269"));
			p1Bot.insertInOrder(new PerformanceProposal(10.36743f, "stplus-mod-wrapper(IBooking)-ps1-277"));
			p1Bot.insertInOrder(new PerformanceProposal(12.14968f, "stplus-mod-wrapper(IBooking)-ps1-282"));
			p1Bot.insertInOrder(new PerformanceProposal(11.48506f, "stplus-mod-wrapper(IBooking)-ps1-316"));
			p1Bot.insertInOrder(new PerformanceProposal(8.79734f, "stplus-mod-wrapper(IBooking)-ps1-364"));
			p1Bot.insertInOrder(new PerformanceProposal(8.93372f, "stplus-mod-wrapper(IBooking)-ps1-404"));
			p1Bot.insertInOrder(new PerformanceProposal(11.738990000000001f, "stplus-mod-wrapper(IBooking)-ps1-450"));
			p1Bot.insertInOrder(new PerformanceProposal(17.74953f, "stplus-mod-wrapper(IBusinessTrip)-ps1-110"));
			p1Bot.insertInOrder(new PerformanceProposal(17.79329f, "stplus-mod-wrapper(IBusinessTrip)-ps1-142"));
			p1Bot.insertInOrder(new PerformanceProposal(12.92977f, "stplus-mod-wrapper(IBusinessTrip)-ps1-216"));
			p1Bot.insertInOrder(new PerformanceProposal(17.79329f, "stplus-mod-wrapper(IBusinessTrip)-ps1-232"));
			p1Bot.insertInOrder(new PerformanceProposal(14.67119f, "stplus-mod-wrapper(IBusinessTrip)-ps1-295"));
			p1Bot.insertInOrder(new PerformanceProposal(9.75304f, "stplus-mod-wrapper(IBusinessTrip)-ps1-296"));
			p1Bot.insertInOrder(new PerformanceProposal(16.50094f, "stplus-mod-wrapper(IBusinessTrip)-ps1-442"));
			p1Bot.insertInOrder(
					new PerformanceProposal(14.296690000000002f, "stplus-mod-wrapper(IBusinessTrip)-ps1-472"));
			p1Bot.insertInOrder(new PerformanceProposal(9.75304f, "stplus-mod-wrapper(IBusinessTrip)-ps1-474"));
			p1Bot.insertInOrder(new PerformanceProposal(17.79329f, "stplus-mod-wrapper(IBusinessTrip)-ps1-478"));
			p1Bot.insertInOrder(
					new PerformanceProposal(10.55321f, "stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps1-118"));
			p1Bot.insertInOrder(
					new PerformanceProposal(10.89984f, "stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps1-275"));
			p1Bot.insertInOrder(new PerformanceProposal(11.575510000000001f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps1-351"));
			p1Bot.insertInOrder(
					new PerformanceProposal(10.89984f, "stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps1-355"));
			p1Bot.insertInOrder(
					new PerformanceProposal(10.95584f, "stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps1-382"));
			p1Bot.insertInOrder(
					new PerformanceProposal(12.6481f, "stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps1-389"));
			p1Bot.insertInOrder(
					new PerformanceProposal(9.01331f, "stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps1-390"));
			p1Bot.insertInOrder(
					new PerformanceProposal(12.41395f, "stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps1-406"));
			p1Bot.insertInOrder(
					new PerformanceProposal(10.54378f, "stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps1-425"));
			p1Bot.insertInOrder(new PerformanceProposal(10.924479999999999f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps1-428"));
			p1Bot.insertInOrder(
					new PerformanceProposal(11.99257f, "stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps1-220"));
			p1Bot.insertInOrder(new PerformanceProposal(14.564689999999999f,
					"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps1-290"));
			p1Bot.insertInOrder(
					new PerformanceProposal(11.38384f, "stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps1-327"));
			p1Bot.insertInOrder(new PerformanceProposal(11.514140000000001f,
					"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps1-377"));
			p1Bot.insertInOrder(
					new PerformanceProposal(12.10687f, "stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps1-406"));
			p1Bot.insertInOrder(
					new PerformanceProposal(16.73878f, "stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps1-423"));
			p1Bot.insertInOrder(
					new PerformanceProposal(8.94556f, "stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps1-439"));
			p1Bot.insertInOrder(
					new PerformanceProposal(13.581f, "stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps1-446"));
			p1Bot.insertInOrder(new PerformanceProposal(11.785350000000001f,
					"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps1-447"));
			p1Bot.insertInOrder(new PerformanceProposal(15.923829999999999f,
					"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps1-454"));
			p1Bot.insertInOrder(new PerformanceProposal(13.6663f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps1-197"));
			p1Bot.insertInOrder(new PerformanceProposal(9.45898f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps1-232"));
			p1Bot.insertInOrder(new PerformanceProposal(10.60521f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps1-233"));
			p1Bot.insertInOrder(new PerformanceProposal(11.07619f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps1-235"));
			p1Bot.insertInOrder(new PerformanceProposal(11.07619f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps1-276"));
			p1Bot.insertInOrder(new PerformanceProposal(9.45898f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps1-277"));
			p1Bot.insertInOrder(new PerformanceProposal(9.45898f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps1-33"));
			p1Bot.insertInOrder(new PerformanceProposal(11.07619f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps1-355"));
			p1Bot.insertInOrder(new PerformanceProposal(13.68803f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps1-362"));
			p1Bot.insertInOrder(new PerformanceProposal(10.24548f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps1-368"));
			p1Bot.insertInOrder(new PerformanceProposal(11.83248f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps1-12"));
			p1Bot.insertInOrder(new PerformanceProposal(11.869720000000001f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps1-13"));
			p1Bot.insertInOrder(new PerformanceProposal(12.59074f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps1-176"));
			p1Bot.insertInOrder(new PerformanceProposal(9.81963f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps1-224"));
			p1Bot.insertInOrder(new PerformanceProposal(12.85938f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps1-281"));
			p1Bot.insertInOrder(new PerformanceProposal(13.27472f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps1-318"));
			p1Bot.insertInOrder(new PerformanceProposal(12.569939999999999f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps1-356"));
			p1Bot.insertInOrder(new PerformanceProposal(12.15383f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps1-402"));
			p1Bot.insertInOrder(new PerformanceProposal(9.06743f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps1-438"));
			p1Bot.insertInOrder(new PerformanceProposal(13.27856f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps1-483"));
			p1Bot.insertInOrder(
					new PerformanceProposal(11.52449f, "stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps1-100"));
			p1Bot.insertInOrder(
					new PerformanceProposal(16.99513f, "stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps1-145"));
			p1Bot.insertInOrder(
					new PerformanceProposal(12.05522f, "stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps1-232"));
			p1Bot.insertInOrder(
					new PerformanceProposal(12.18489f, "stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps1-300"));
			p1Bot.insertInOrder(
					new PerformanceProposal(18.4973f, "stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps1-326"));
			p1Bot.insertInOrder(new PerformanceProposal(9.548680000000001f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps1-353"));
			p1Bot.insertInOrder(
					new PerformanceProposal(17.16592f, "stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps1-400"));
			p1Bot.insertInOrder(
					new PerformanceProposal(17.69796f, "stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps1-474"));
			p1Bot.insertInOrder(
					new PerformanceProposal(12.23377f, "stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps1-488"));
			p1Bot.insertInOrder(
					new PerformanceProposal(16.99513f, "stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps1-86"));
			p1Bot.insertInOrder(new PerformanceProposal(11.93386f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps1-319"));
			p1Bot.insertInOrder(new PerformanceProposal(12.003530000000001f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps1-321"));
			p1Bot.insertInOrder(new PerformanceProposal(13.041530000000002f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps1-323"));
			p1Bot.insertInOrder(new PerformanceProposal(10.82422f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps1-356"));
			p1Bot.insertInOrder(
					new PerformanceProposal(12.916f, "stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps1-392"));
			p1Bot.insertInOrder(new PerformanceProposal(10.8733f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps1-397"));
			p1Bot.insertInOrder(new PerformanceProposal(12.798860000000001f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps1-426"));
			p1Bot.insertInOrder(new PerformanceProposal(11.69247f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps1-427"));
			p1Bot.insertInOrder(new PerformanceProposal(12.632090000000002f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps1-433"));
			p1Bot.insertInOrder(new PerformanceProposal(14.583750000000002f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps1-62"));
			p1Bot.insertInOrder(new PerformanceProposal(11.84615f, "stplus-mod-split(PaymentSystem)-ps2-119"));
			p1Bot.insertInOrder(
					new PerformanceProposal(23.426869999999997f, "stplus-mod-split(PaymentSystem)-ps2-179"));
			p1Bot.insertInOrder(new PerformanceProposal(24.7006f, "stplus-mod-split(PaymentSystem)-ps2-191"));
			p1Bot.insertInOrder(new PerformanceProposal(10.85057f, "stplus-mod-split(PaymentSystem)-ps2-222"));
			p1Bot.insertInOrder(new PerformanceProposal(19.1965f, "stplus-mod-split(PaymentSystem)-ps2-239"));
			p1Bot.insertInOrder(new PerformanceProposal(27.5474f, "stplus-mod-split(PaymentSystem)-ps2-402"));
			p1Bot.insertInOrder(new PerformanceProposal(10.3179f, "stplus-mod-split(PaymentSystem)-ps2-43"));
			p1Bot.insertInOrder(new PerformanceProposal(9.86241f, "stplus-mod-split(PaymentSystem)-ps2-432"));
			p1Bot.insertInOrder(new PerformanceProposal(10.3179f, "stplus-mod-split(PaymentSystem)-ps2-470"));
			p1Bot.insertInOrder(new PerformanceProposal(24.44907f, "stplus-mod-split(PaymentSystem)-ps2-69"));
			p1Bot.insertInOrder(new PerformanceProposal(23.334919999999997f, "stplus-mod-wrapper(IExporter)-ps2-146"));
			p1Bot.insertInOrder(new PerformanceProposal(15.51483f, "stplus-mod-wrapper(IExporter)-ps2-148"));
			p1Bot.insertInOrder(new PerformanceProposal(20.381999999999998f, "stplus-mod-wrapper(IExporter)-ps2-162"));
			p1Bot.insertInOrder(new PerformanceProposal(26.0003f, "stplus-mod-wrapper(IExporter)-ps2-243"));
			p1Bot.insertInOrder(new PerformanceProposal(22.17638f, "stplus-mod-wrapper(IExporter)-ps2-270"));
			p1Bot.insertInOrder(new PerformanceProposal(15.51483f, "stplus-mod-wrapper(IExporter)-ps2-281"));
			p1Bot.insertInOrder(new PerformanceProposal(26.0003f, "stplus-mod-wrapper(IExporter)-ps2-318"));
			p1Bot.insertInOrder(new PerformanceProposal(26.17153f, "stplus-mod-wrapper(IExporter)-ps2-324"));
			p1Bot.insertInOrder(new PerformanceProposal(14.068439999999999f, "stplus-mod-wrapper(IExporter)-ps2-52"));
			p1Bot.insertInOrder(new PerformanceProposal(21.003230000000002f, "stplus-mod-wrapper(IExporter)-ps2-90"));
			p1Bot.insertInOrder(new PerformanceProposal(18.83982f, "stplus-mod-wrapper(ITripDB)-ps2-143"));
			p1Bot.insertInOrder(new PerformanceProposal(30.08127f, "stplus-mod-wrapper(ITripDB)-ps2-189"));
			p1Bot.insertInOrder(new PerformanceProposal(25.0017f, "stplus-mod-wrapper(ITripDB)-ps2-192"));
			p1Bot.insertInOrder(new PerformanceProposal(14.58174f, "stplus-mod-wrapper(ITripDB)-ps2-231"));
			p1Bot.insertInOrder(new PerformanceProposal(14.57002f, "stplus-mod-wrapper(ITripDB)-ps2-309"));
			p1Bot.insertInOrder(new PerformanceProposal(30.08307f, "stplus-mod-wrapper(ITripDB)-ps2-314"));
			p1Bot.insertInOrder(new PerformanceProposal(10.27182f, "stplus-mod-wrapper(ITripDB)-ps2-33"));
			p1Bot.insertInOrder(new PerformanceProposal(14.58174f, "stplus-mod-wrapper(ITripDB)-ps2-347"));
			p1Bot.insertInOrder(new PerformanceProposal(11.79799f, "stplus-mod-wrapper(ITripDB)-ps2-354"));
			p1Bot.insertInOrder(new PerformanceProposal(19.60906f, "stplus-mod-wrapper(ITripDB)-ps2-86"));
			p1Bot.insertInOrder(new PerformanceProposal(18.27704f, "stplus-mod-wrapper(IExternalPayment)-ps2-145"));
			p1Bot.insertInOrder(new PerformanceProposal(21.43855f, "stplus-mod-wrapper(IExternalPayment)-ps2-147"));
			p1Bot.insertInOrder(new PerformanceProposal(21.59252f, "stplus-mod-wrapper(IExternalPayment)-ps2-270"));
			p1Bot.insertInOrder(new PerformanceProposal(19.26154f, "stplus-mod-wrapper(IExternalPayment)-ps2-275"));
			p1Bot.insertInOrder(new PerformanceProposal(21.43855f, "stplus-mod-wrapper(IExternalPayment)-ps2-324"));
			p1Bot.insertInOrder(new PerformanceProposal(8.91685f, "stplus-mod-wrapper(IExternalPayment)-ps2-354"));
			p1Bot.insertInOrder(new PerformanceProposal(20.19721f, "stplus-mod-wrapper(IExternalPayment)-ps2-369"));
			p1Bot.insertInOrder(
					new PerformanceProposal(18.612299999999998f, "stplus-mod-wrapper(IExternalPayment)-ps2-445"));
			p1Bot.insertInOrder(
					new PerformanceProposal(11.603349999999999f, "stplus-mod-wrapper(IExternalPayment)-ps2-449"));
			p1Bot.insertInOrder(new PerformanceProposal(18.99147f, "stplus-mod-wrapper(IExternalPayment)-ps2-453"));
			p1Bot.insertInOrder(new PerformanceProposal(13.3154f, "stplus-mod-wrapper(IEmployeePayment)-ps2-130"));
			p1Bot.insertInOrder(new PerformanceProposal(23.4043f, "stplus-mod-wrapper(IEmployeePayment)-ps2-237"));
			p1Bot.insertInOrder(new PerformanceProposal(14.80395f, "stplus-mod-wrapper(IEmployeePayment)-ps2-257"));
			p1Bot.insertInOrder(new PerformanceProposal(16.12517f, "stplus-mod-wrapper(IEmployeePayment)-ps2-310"));
			p1Bot.insertInOrder(new PerformanceProposal(18.05954f, "stplus-mod-wrapper(IEmployeePayment)-ps2-358"));
			p1Bot.insertInOrder(new PerformanceProposal(17.42301f, "stplus-mod-wrapper(IEmployeePayment)-ps2-361"));
			p1Bot.insertInOrder(new PerformanceProposal(14.83183f, "stplus-mod-wrapper(IEmployeePayment)-ps2-437"));
			p1Bot.insertInOrder(
					new PerformanceProposal(15.425529999999998f, "stplus-mod-wrapper(IEmployeePayment)-ps2-441"));
			p1Bot.insertInOrder(new PerformanceProposal(16.24689f, "stplus-mod-wrapper(IEmployeePayment)-ps2-489"));
			p1Bot.insertInOrder(new PerformanceProposal(16.12517f, "stplus-mod-wrapper(IEmployeePayment)-ps2-496"));
			p1Bot.insertInOrder(new PerformanceProposal(18.99069f, "stplus-mod-wrapper(IBooking)-ps2-117"));
			p1Bot.insertInOrder(new PerformanceProposal(27.483310000000003f, "stplus-mod-wrapper(IBooking)-ps2-143"));
			p1Bot.insertInOrder(new PerformanceProposal(10.28473f, "stplus-mod-wrapper(IBooking)-ps2-237"));
			p1Bot.insertInOrder(new PerformanceProposal(24.34285f, "stplus-mod-wrapper(IBooking)-ps2-286"));
			p1Bot.insertInOrder(new PerformanceProposal(18.10264f, "stplus-mod-wrapper(IBooking)-ps2-299"));
			p1Bot.insertInOrder(new PerformanceProposal(28.910330000000002f, "stplus-mod-wrapper(IBooking)-ps2-330"));
			p1Bot.insertInOrder(new PerformanceProposal(21.06936f, "stplus-mod-wrapper(IBooking)-ps2-375"));
			p1Bot.insertInOrder(new PerformanceProposal(27.192f, "stplus-mod-wrapper(IBooking)-ps2-376"));
			p1Bot.insertInOrder(new PerformanceProposal(19.27492f, "stplus-mod-wrapper(IBooking)-ps2-384"));
			p1Bot.insertInOrder(new PerformanceProposal(8.11522f, "stplus-mod-wrapper(IBooking)-ps2-401"));
			p1Bot.insertInOrder(
					new PerformanceProposal(11.553609999999999f, "stplus-mod-wrapper(IBusinessTrip)-ps2-107"));
			p1Bot.insertInOrder(
					new PerformanceProposal(10.155619999999999f, "stplus-mod-wrapper(IBusinessTrip)-ps2-130"));
			p1Bot.insertInOrder(
					new PerformanceProposal(8.057590000000001f, "stplus-mod-wrapper(IBusinessTrip)-ps2-156"));
			p1Bot.insertInOrder(new PerformanceProposal(13.61196f, "stplus-mod-wrapper(IBusinessTrip)-ps2-238"));
			p1Bot.insertInOrder(
					new PerformanceProposal(8.057590000000001f, "stplus-mod-wrapper(IBusinessTrip)-ps2-276"));
			p1Bot.insertInOrder(new PerformanceProposal(9.42589f, "stplus-mod-wrapper(IBusinessTrip)-ps2-282"));
			p1Bot.insertInOrder(new PerformanceProposal(20.69903f, "stplus-mod-wrapper(IBusinessTrip)-ps2-440"));
			p1Bot.insertInOrder(new PerformanceProposal(18.55714f, "stplus-mod-wrapper(IBusinessTrip)-ps2-442"));
			p1Bot.insertInOrder(new PerformanceProposal(23.81316f, "stplus-mod-wrapper(IBusinessTrip)-ps2-90"));
			p1Bot.insertInOrder(new PerformanceProposal(23.67343f, "stplus-mod-wrapper(IBusinessTrip)-ps2-98"));
			p1Bot.insertInOrder(
					new PerformanceProposal(13.63445f, "stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps2-143"));
			p1Bot.insertInOrder(
					new PerformanceProposal(23.10754f, "stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps2-191"));
			p1Bot.insertInOrder(new PerformanceProposal(22.945610000000002f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps2-268"));
			p1Bot.insertInOrder(
					new PerformanceProposal(11.8173f, "stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps2-307"));
			p1Bot.insertInOrder(
					new PerformanceProposal(21.74946f, "stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps2-314"));
			p1Bot.insertInOrder(
					new PerformanceProposal(13.63445f, "stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps2-352"));
			p1Bot.insertInOrder(new PerformanceProposal(13.485199999999999f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps2-354"));
			p1Bot.insertInOrder(
					new PerformanceProposal(11.8173f, "stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps2-355"));
			p1Bot.insertInOrder(
					new PerformanceProposal(18.58637f, "stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps2-55"));
			p1Bot.insertInOrder(
					new PerformanceProposal(11.8173f, "stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps2-7"));
			p1Bot.insertInOrder(new PerformanceProposal(14.355039999999999f,
					"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps2-137"));
			p1Bot.insertInOrder(
					new PerformanceProposal(17.69748f, "stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps2-142"));
			p1Bot.insertInOrder(
					new PerformanceProposal(30.74481f, "stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps2-171"));
			p1Bot.insertInOrder(
					new PerformanceProposal(15.45364f, "stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps2-189"));
			p1Bot.insertInOrder(
					new PerformanceProposal(16.93228f, "stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps2-209"));
			p1Bot.insertInOrder(
					new PerformanceProposal(20.4381f, "stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps2-238"));
			p1Bot.insertInOrder(
					new PerformanceProposal(25.03268f, "stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps2-275"));
			p1Bot.insertInOrder(new PerformanceProposal(31.998109999999997f,
					"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps2-281"));
			p1Bot.insertInOrder(
					new PerformanceProposal(18.38324f, "stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps2-285"));
			p1Bot.insertInOrder(
					new PerformanceProposal(30.83079f, "stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps2-414"));
			p1Bot.insertInOrder(new PerformanceProposal(9.339870000000001f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps2-192"));
			p1Bot.insertInOrder(new PerformanceProposal(13.24279f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps2-203"));
			p1Bot.insertInOrder(new PerformanceProposal(17.38232f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps2-275"));
			p1Bot.insertInOrder(new PerformanceProposal(14.62181f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps2-279"));
			p1Bot.insertInOrder(new PerformanceProposal(17.47016f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps2-360"));
			p1Bot.insertInOrder(new PerformanceProposal(14.12246f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps2-364"));
			p1Bot.insertInOrder(new PerformanceProposal(10.32142f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps2-403"));
			p1Bot.insertInOrder(new PerformanceProposal(8.33966f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps2-439"));
			p1Bot.insertInOrder(new PerformanceProposal(18.67914f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps2-450"));
			p1Bot.insertInOrder(new PerformanceProposal(8.422609999999999f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps2-492"));
			p1Bot.insertInOrder(new PerformanceProposal(14.597290000000001f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps2-250"));
			p1Bot.insertInOrder(new PerformanceProposal(11.92469f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps2-321"));
			p1Bot.insertInOrder(new PerformanceProposal(11.336359999999999f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps2-33"));
			p1Bot.insertInOrder(new PerformanceProposal(11.72219f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps2-359"));
			p1Bot.insertInOrder(new PerformanceProposal(11.82668f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps2-360"));
			p1Bot.insertInOrder(new PerformanceProposal(11.45097f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps2-369"));
			p1Bot.insertInOrder(new PerformanceProposal(11.65258f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps2-370"));
			p1Bot.insertInOrder(new PerformanceProposal(11.72219f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps2-456"));
			p1Bot.insertInOrder(new PerformanceProposal(11.82894f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps2-499"));
			p1Bot.insertInOrder(new PerformanceProposal(10.92929f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps2-501"));
			p1Bot.insertInOrder(
					new PerformanceProposal(16.95612f, "stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps2-226"));
			p1Bot.insertInOrder(
					new PerformanceProposal(22.38673f, "stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps2-325"));
			p1Bot.insertInOrder(
					new PerformanceProposal(22.36142f, "stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps2-401"));
			p1Bot.insertInOrder(
					new PerformanceProposal(21.08694f, "stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps2-415"));
			p1Bot.insertInOrder(
					new PerformanceProposal(21.23776f, "stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps2-452"));
			p1Bot.insertInOrder(
					new PerformanceProposal(19.33157f, "stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps2-456"));
			p1Bot.insertInOrder(
					new PerformanceProposal(23.11579f, "stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps2-462"));
			p1Bot.insertInOrder(
					new PerformanceProposal(21.07343f, "stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps2-505"));
			p1Bot.insertInOrder(
					new PerformanceProposal(22.36152f, "stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps2-506"));
			p1Bot.insertInOrder(
					new PerformanceProposal(25.8517f, "stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps2-508"));
			p1Bot.insertInOrder(new PerformanceProposal(27.011720000000004f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps2-119"));
			p1Bot.insertInOrder(new PerformanceProposal(24.317149999999998f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps2-242"));
			p1Bot.insertInOrder(new PerformanceProposal(17.993949999999998f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps2-278"));
			p1Bot.insertInOrder(new PerformanceProposal(26.8183f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps2-344"));
			p1Bot.insertInOrder(new PerformanceProposal(25.25987f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps2-373"));
			p1Bot.insertInOrder(new PerformanceProposal(26.8183f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps2-407"));
			p1Bot.insertInOrder(new PerformanceProposal(26.90352f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps2-408"));
			p1Bot.insertInOrder(new PerformanceProposal(21.89577f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps2-439"));
			p1Bot.insertInOrder(new PerformanceProposal(25.367890000000003f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps2-447"));
			p1Bot.insertInOrder(new PerformanceProposal(10.92604f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps2-482"));

			// First level - Modifiability tactics
			p2Bot.insertInOrder(new PerformanceProposal(74.0173f, "stplus-mod-split(PaymentSystem)"));
			p2Bot.insertInOrder(new PerformanceProposal(1289.96f, "stplus-mod-wrapper(IExporter)"));
			p2Bot.insertInOrder(new PerformanceProposal(115.82230000000001f, "stplus-mod-wrapper(ITripDB)"));
			p2Bot.insertInOrder(new PerformanceProposal(78.7857f, "stplus-mod-wrapper(IExternalPayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(148.847f, "stplus-mod-wrapper(IEmployeePayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(82.4342f, "stplus-mod-wrapper(IBooking)"));
			p2Bot.insertInOrder(new PerformanceProposal(74.0173f, "stplus-mod-wrapper(IBusinessTrip)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(74.0173f, "stplus-mod-split(PaymentSystem)-wrapper(IExporter)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(115.82230000000001f, "stplus-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(78.7857f, "stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(148.847f, "stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(82.4342f, "stplus-mod-split(PaymentSystem)-wrapper(IBooking)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(74.0173f, "stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));

			// First level - Performance tactics
			p2Bot.insertInOrder(new PerformanceProposal(16.94575f, "stplus-ps1-258"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-ps1-281"));
			p2Bot.insertInOrder(new PerformanceProposal(29.7949f, "stplus-ps1-338"));
			p2Bot.insertInOrder(new PerformanceProposal(17.64059f, "stplus-ps1-340"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-ps1-397"));
			p2Bot.insertInOrder(new PerformanceProposal(31.0628f, "stplus-ps1-404"));
			p2Bot.insertInOrder(new PerformanceProposal(1240.1419999999998f, "stplus-ps1-436"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-ps1-444"));
			p2Bot.insertInOrder(new PerformanceProposal(9.77246f, "stplus-ps1-494"));
			p2Bot.insertInOrder(new PerformanceProposal(89.18619999999999f, "stplus-ps1-64"));
			p2Bot.insertInOrder(new PerformanceProposal(26.2866f, "stplus-ps2-209"));
			p2Bot.insertInOrder(new PerformanceProposal(25.7885f, "stplus-ps2-22"));
			p2Bot.insertInOrder(new PerformanceProposal(25.16403f, "stplus-ps2-325"));
			p2Bot.insertInOrder(new PerformanceProposal(26.9824f, "stplus-ps2-330"));
			p2Bot.insertInOrder(new PerformanceProposal(26.2043f, "stplus-ps2-358"));
			p2Bot.insertInOrder(new PerformanceProposal(22.348979999999997f, "stplus-ps2-366"));
			p2Bot.insertInOrder(new PerformanceProposal(16.59996f, "stplus-ps2-416"));
			p2Bot.insertInOrder(new PerformanceProposal(26.82446f, "stplus-ps2-476"));
			p2Bot.insertInOrder(new PerformanceProposal(26.341700000000003f, "stplus-ps2-479"));
			p2Bot.insertInOrder(new PerformanceProposal(23.42628f, "stplus-ps2-480"));

			// Second level - Modifiability tactics applied over Performance candidates
			p2Bot.insertInOrder(new PerformanceProposal(17.41649f, "stplus-ps1-258-mod-wrapper(IExporter)"));
			p2Bot.insertInOrder(new PerformanceProposal(19.987569999999998f, "stplus-ps1-258-mod-wrapper(ITripDB)"));
			p2Bot.insertInOrder(new PerformanceProposal(19.34795f, "stplus-ps1-258-mod-wrapper(IExternalPayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(17.37807f, "stplus-ps1-258-mod-wrapper(IEmployeePayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(19.34726f, "stplus-ps1-258-mod-wrapper(IBooking)"));
			p2Bot.insertInOrder(new PerformanceProposal(17.97024f, "stplus-ps1-258-mod-wrapper(IBusinessTrip)"));
			p2Bot.insertInOrder(new PerformanceProposal(16.94575f, "stplus-ps1-258-mod-split(PaymentSystem)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(17.41649f, "stplus-ps1-258-mod-split(PaymentSystem)-wrapper(IExporter)"));
			p2Bot.insertInOrder(new PerformanceProposal(19.987569999999998f,
					"stplus-ps1-258-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			p2Bot.insertInOrder(new PerformanceProposal(19.34795f,
					"stplus-ps1-258-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(17.37807f,
					"stplus-ps1-258-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(19.34726f, "stplus-ps1-258-mod-split(PaymentSystem)-wrapper(IBooking)"));
			p2Bot.insertInOrder(new PerformanceProposal(17.97024f,
					"stplus-ps1-258-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-ps1-281-mod-wrapper(IExporter)"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-ps1-281-mod-wrapper(ITripDB)"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-ps1-281-mod-wrapper(IExternalPayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-ps1-281-mod-wrapper(IEmployeePayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-ps1-281-mod-wrapper(IBooking)"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-ps1-281-mod-wrapper(IBusinessTrip)"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-ps1-281-mod-split(PaymentSystem)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(999999999f, "stplus-ps1-281-mod-split(PaymentSystem)-wrapper(IExporter)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(999999999f, "stplus-ps1-281-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f,
					"stplus-ps1-281-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f,
					"stplus-ps1-281-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(999999999f, "stplus-ps1-281-mod-split(PaymentSystem)-wrapper(IBooking)"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f,
					"stplus-ps1-281-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			p2Bot.insertInOrder(new PerformanceProposal(31.5006f, "stplus-ps1-338-mod-wrapper(IExporter)"));
			p2Bot.insertInOrder(new PerformanceProposal(47.1184f, "stplus-ps1-338-mod-wrapper(ITripDB)"));
			p2Bot.insertInOrder(new PerformanceProposal(30.4747f, "stplus-ps1-338-mod-wrapper(IExternalPayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(29.9416f, "stplus-ps1-338-mod-wrapper(IEmployeePayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(30.8239f, "stplus-ps1-338-mod-wrapper(IBooking)"));
			p2Bot.insertInOrder(new PerformanceProposal(47.1098f, "stplus-ps1-338-mod-wrapper(IBusinessTrip)"));
			p2Bot.insertInOrder(new PerformanceProposal(29.7949f, "stplus-ps1-338-mod-split(PaymentSystem)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(31.5006f, "stplus-ps1-338-mod-split(PaymentSystem)-wrapper(IExporter)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(47.1184f, "stplus-ps1-338-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			p2Bot.insertInOrder(new PerformanceProposal(30.4747f,
					"stplus-ps1-338-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(29.9416f,
					"stplus-ps1-338-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(30.8239f, "stplus-ps1-338-mod-split(PaymentSystem)-wrapper(IBooking)"));
			p2Bot.insertInOrder(new PerformanceProposal(47.1098f,
					"stplus-ps1-338-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			p2Bot.insertInOrder(new PerformanceProposal(18.29758f, "stplus-ps1-340-mod-wrapper(IExporter)"));
			p2Bot.insertInOrder(new PerformanceProposal(21.8758f, "stplus-ps1-340-mod-wrapper(ITripDB)"));
			p2Bot.insertInOrder(new PerformanceProposal(20.00966f, "stplus-ps1-340-mod-wrapper(IExternalPayment)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(18.138550000000002f, "stplus-ps1-340-mod-wrapper(IEmployeePayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(19.97666f, "stplus-ps1-340-mod-wrapper(IBooking)"));
			p2Bot.insertInOrder(new PerformanceProposal(21.89328f, "stplus-ps1-340-mod-wrapper(IBusinessTrip)"));
			p2Bot.insertInOrder(new PerformanceProposal(17.64059f, "stplus-ps1-340-mod-split(PaymentSystem)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(18.29758f, "stplus-ps1-340-mod-split(PaymentSystem)-wrapper(IExporter)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(21.8758f, "stplus-ps1-340-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			p2Bot.insertInOrder(new PerformanceProposal(20.00966f,
					"stplus-ps1-340-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(18.138550000000002f,
					"stplus-ps1-340-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(19.97666f, "stplus-ps1-340-mod-split(PaymentSystem)-wrapper(IBooking)"));
			p2Bot.insertInOrder(new PerformanceProposal(21.89328f,
					"stplus-ps1-340-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-ps1-397-mod-wrapper(IExporter)"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-ps1-397-mod-wrapper(ITripDB)"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-ps1-397-mod-wrapper(IExternalPayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-ps1-397-mod-wrapper(IEmployeePayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-ps1-397-mod-wrapper(IBooking)"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-ps1-397-mod-wrapper(IBusinessTrip)"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-ps1-397-mod-split(PaymentSystem)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(999999999f, "stplus-ps1-397-mod-split(PaymentSystem)-wrapper(IExporter)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(999999999f, "stplus-ps1-397-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f,
					"stplus-ps1-397-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f,
					"stplus-ps1-397-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(999999999f, "stplus-ps1-397-mod-split(PaymentSystem)-wrapper(IBooking)"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f,
					"stplus-ps1-397-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			p2Bot.insertInOrder(new PerformanceProposal(32.7703f, "stplus-ps1-404-mod-wrapper(IExporter)"));
			p2Bot.insertInOrder(new PerformanceProposal(48.4423f, "stplus-ps1-404-mod-wrapper(ITripDB)"));
			p2Bot.insertInOrder(new PerformanceProposal(33.9226f, "stplus-ps1-404-mod-wrapper(IExternalPayment)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(31.572599999999998f, "stplus-ps1-404-mod-wrapper(IEmployeePayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(33.9213f, "stplus-ps1-404-mod-wrapper(IBooking)"));
			p2Bot.insertInOrder(new PerformanceProposal(48.4371f, "stplus-ps1-404-mod-wrapper(IBusinessTrip)"));
			p2Bot.insertInOrder(new PerformanceProposal(31.0628f, "stplus-ps1-404-mod-split(PaymentSystem)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(32.7703f, "stplus-ps1-404-mod-split(PaymentSystem)-wrapper(IExporter)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(48.4423f, "stplus-ps1-404-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			p2Bot.insertInOrder(new PerformanceProposal(33.9226f,
					"stplus-ps1-404-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(31.572599999999998f,
					"stplus-ps1-404-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(33.9213f, "stplus-ps1-404-mod-split(PaymentSystem)-wrapper(IBooking)"));
			p2Bot.insertInOrder(new PerformanceProposal(48.4371f,
					"stplus-ps1-404-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-ps1-436-mod-wrapper(IExporter)"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-ps1-436-mod-wrapper(ITripDB)"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-ps1-436-mod-wrapper(IExternalPayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-ps1-436-mod-wrapper(IEmployeePayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-ps1-436-mod-wrapper(IBooking)"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-ps1-436-mod-wrapper(IBusinessTrip)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(1240.1419999999998f, "stplus-ps1-436-mod-split(PaymentSystem)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(999999999f, "stplus-ps1-436-mod-split(PaymentSystem)-wrapper(IExporter)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(999999999f, "stplus-ps1-436-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f,
					"stplus-ps1-436-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f,
					"stplus-ps1-436-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(999999999f, "stplus-ps1-436-mod-split(PaymentSystem)-wrapper(IBooking)"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f,
					"stplus-ps1-436-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-ps1-444-mod-wrapper(IExporter)"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-ps1-444-mod-wrapper(ITripDB)"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-ps1-444-mod-wrapper(IExternalPayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-ps1-444-mod-wrapper(IEmployeePayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-ps1-444-mod-wrapper(IBooking)"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-ps1-444-mod-wrapper(IBusinessTrip)"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-ps1-444-mod-split(PaymentSystem)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(999999999f, "stplus-ps1-444-mod-split(PaymentSystem)-wrapper(IExporter)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(999999999f, "stplus-ps1-444-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f,
					"stplus-ps1-444-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f,
					"stplus-ps1-444-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(999999999f, "stplus-ps1-444-mod-split(PaymentSystem)-wrapper(IBooking)"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f,
					"stplus-ps1-444-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			p2Bot.insertInOrder(new PerformanceProposal(22.95597f, "stplus-ps1-494-mod-wrapper(IExporter)"));
			p2Bot.insertInOrder(new PerformanceProposal(29.20285f, "stplus-ps1-494-mod-wrapper(ITripDB)"));
			p2Bot.insertInOrder(new PerformanceProposal(24.4602f, "stplus-ps1-494-mod-wrapper(IExternalPayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(22.50902f, "stplus-ps1-494-mod-wrapper(IEmployeePayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(24.459600000000002f, "stplus-ps1-494-mod-wrapper(IBooking)"));
			p2Bot.insertInOrder(new PerformanceProposal(29.20745f, "stplus-ps1-494-mod-wrapper(IBusinessTrip)"));
			p2Bot.insertInOrder(new PerformanceProposal(22.02855f, "stplus-ps1-494-mod-split(PaymentSystem)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(22.95597f, "stplus-ps1-494-mod-split(PaymentSystem)-wrapper(IExporter)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(29.20285f, "stplus-ps1-494-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			p2Bot.insertInOrder(new PerformanceProposal(24.4602f,
					"stplus-ps1-494-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(22.50902f,
					"stplus-ps1-494-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(24.459600000000002f,
					"stplus-ps1-494-mod-split(PaymentSystem)-wrapper(IBooking)"));
			p2Bot.insertInOrder(new PerformanceProposal(29.20745f,
					"stplus-ps1-494-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			p2Bot.insertInOrder(new PerformanceProposal(107.4466f, "stplus-ps1-64-mod-wrapper(IExporter)"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-ps1-64-mod-wrapper(ITripDB)"));
			p2Bot.insertInOrder(new PerformanceProposal(90.0727f, "stplus-ps1-64-mod-wrapper(IExternalPayment)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(89.37719999999999f, "stplus-ps1-64-mod-wrapper(IEmployeePayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-ps1-64-mod-wrapper(IBooking)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(91.12819999999999f, "stplus-ps1-64-mod-wrapper(IBusinessTrip)"));
			p2Bot.insertInOrder(new PerformanceProposal(89.18619999999999f, "stplus-ps1-64-mod-split(PaymentSystem)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(107.4466f, "stplus-ps1-64-mod-split(PaymentSystem)-wrapper(IExporter)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(999999999f, "stplus-ps1-64-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			p2Bot.insertInOrder(new PerformanceProposal(90.0727f,
					"stplus-ps1-64-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(89.37719999999999f,
					"stplus-ps1-64-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(999999999f, "stplus-ps1-64-mod-split(PaymentSystem)-wrapper(IBooking)"));
			p2Bot.insertInOrder(new PerformanceProposal(91.12819999999999f,
					"stplus-ps1-64-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			p2Bot.insertInOrder(new PerformanceProposal(27.3495f, "stplus-ps2-209-mod-wrapper(IExporter)"));
			p2Bot.insertInOrder(new PerformanceProposal(35.370000000000005f, "stplus-ps2-209-mod-wrapper(ITripDB)"));
			p2Bot.insertInOrder(new PerformanceProposal(31.1693f, "stplus-ps2-209-mod-wrapper(IExternalPayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(26.9658f, "stplus-ps2-209-mod-wrapper(IEmployeePayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(28.8855f, "stplus-ps2-209-mod-wrapper(IBooking)"));
			p2Bot.insertInOrder(new PerformanceProposal(31.6697f, "stplus-ps2-209-mod-wrapper(IBusinessTrip)"));
			p2Bot.insertInOrder(new PerformanceProposal(26.2866f, "stplus-ps2-209-mod-split(PaymentSystem)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(27.3495f, "stplus-ps2-209-mod-split(PaymentSystem)-wrapper(IExporter)"));
			p2Bot.insertInOrder(new PerformanceProposal(35.370000000000005f,
					"stplus-ps2-209-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			p2Bot.insertInOrder(new PerformanceProposal(31.1693f,
					"stplus-ps2-209-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(26.9658f,
					"stplus-ps2-209-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(28.8855f, "stplus-ps2-209-mod-split(PaymentSystem)-wrapper(IBooking)"));
			p2Bot.insertInOrder(new PerformanceProposal(31.6697f,
					"stplus-ps2-209-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			p2Bot.insertInOrder(new PerformanceProposal(26.680799999999998f, "stplus-ps2-22-mod-wrapper(IExporter)"));
			p2Bot.insertInOrder(new PerformanceProposal(34.913799999999995f, "stplus-ps2-22-mod-wrapper(ITripDB)"));
			p2Bot.insertInOrder(new PerformanceProposal(30.5328f, "stplus-ps2-22-mod-wrapper(IExternalPayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(26.6729f, "stplus-ps2-22-mod-wrapper(IEmployeePayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(33.806200000000004f, "stplus-ps2-22-mod-wrapper(IBooking)"));
			p2Bot.insertInOrder(new PerformanceProposal(26.6179f, "stplus-ps2-22-mod-wrapper(IBusinessTrip)"));
			p2Bot.insertInOrder(new PerformanceProposal(25.7885f, "stplus-ps2-22-mod-split(PaymentSystem)"));
			p2Bot.insertInOrder(new PerformanceProposal(26.680799999999998f,
					"stplus-ps2-22-mod-split(PaymentSystem)-wrapper(IExporter)"));
			p2Bot.insertInOrder(new PerformanceProposal(34.913799999999995f,
					"stplus-ps2-22-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			p2Bot.insertInOrder(new PerformanceProposal(30.5328f,
					"stplus-ps2-22-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(26.6729f,
					"stplus-ps2-22-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(33.806200000000004f,
					"stplus-ps2-22-mod-split(PaymentSystem)-wrapper(IBooking)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(26.6179f, "stplus-ps2-22-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			p2Bot.insertInOrder(new PerformanceProposal(26.663130000000002f, "stplus-ps2-325-mod-wrapper(IExporter)"));
			p2Bot.insertInOrder(new PerformanceProposal(28.56223f, "stplus-ps2-325-mod-wrapper(ITripDB)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(33.092510000000004f, "stplus-ps2-325-mod-wrapper(IExternalPayment)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(26.724330000000002f, "stplus-ps2-325-mod-wrapper(IEmployeePayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(33.09066f, "stplus-ps2-325-mod-wrapper(IBooking)"));
			p2Bot.insertInOrder(new PerformanceProposal(27.10814f, "stplus-ps2-325-mod-wrapper(IBusinessTrip)"));
			p2Bot.insertInOrder(new PerformanceProposal(25.16403f, "stplus-ps2-325-mod-split(PaymentSystem)"));
			p2Bot.insertInOrder(new PerformanceProposal(26.663130000000002f,
					"stplus-ps2-325-mod-split(PaymentSystem)-wrapper(IExporter)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(28.56223f, "stplus-ps2-325-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			p2Bot.insertInOrder(new PerformanceProposal(33.092510000000004f,
					"stplus-ps2-325-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(26.724330000000002f,
					"stplus-ps2-325-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(33.09066f, "stplus-ps2-325-mod-split(PaymentSystem)-wrapper(IBooking)"));
			p2Bot.insertInOrder(new PerformanceProposal(27.10814f,
					"stplus-ps2-325-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			p2Bot.insertInOrder(new PerformanceProposal(28.1041f, "stplus-ps2-330-mod-wrapper(IExporter)"));
			p2Bot.insertInOrder(new PerformanceProposal(29.896f, "stplus-ps2-330-mod-wrapper(ITripDB)"));
			p2Bot.insertInOrder(new PerformanceProposal(27.65328f, "stplus-ps2-330-mod-wrapper(IExternalPayment)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(27.119369999999996f, "stplus-ps2-330-mod-wrapper(IEmployeePayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(27.653f, "stplus-ps2-330-mod-wrapper(IBooking)"));
			p2Bot.insertInOrder(new PerformanceProposal(29.8716f, "stplus-ps2-330-mod-wrapper(IBusinessTrip)"));
			p2Bot.insertInOrder(new PerformanceProposal(26.9824f, "stplus-ps2-330-mod-split(PaymentSystem)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(28.1041f, "stplus-ps2-330-mod-split(PaymentSystem)-wrapper(IExporter)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(29.896f, "stplus-ps2-330-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			p2Bot.insertInOrder(new PerformanceProposal(27.65328f,
					"stplus-ps2-330-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(27.119369999999996f,
					"stplus-ps2-330-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(27.653f, "stplus-ps2-330-mod-split(PaymentSystem)-wrapper(IBooking)"));
			p2Bot.insertInOrder(new PerformanceProposal(29.8716f,
					"stplus-ps2-330-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			p2Bot.insertInOrder(new PerformanceProposal(26.9436f, "stplus-ps2-358-mod-wrapper(IExporter)"));
			p2Bot.insertInOrder(new PerformanceProposal(31.8175f, "stplus-ps2-358-mod-wrapper(ITripDB)"));
			p2Bot.insertInOrder(new PerformanceProposal(27.393f, "stplus-ps2-358-mod-wrapper(IExternalPayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(26.4363f, "stplus-ps2-358-mod-wrapper(IEmployeePayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(27.468f, "stplus-ps2-358-mod-wrapper(IBooking)"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-ps2-358-mod-wrapper(IBusinessTrip)"));
			p2Bot.insertInOrder(new PerformanceProposal(26.2043f, "stplus-ps2-358-mod-split(PaymentSystem)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(26.9436f, "stplus-ps2-358-mod-split(PaymentSystem)-wrapper(IExporter)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(31.8175f, "stplus-ps2-358-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			p2Bot.insertInOrder(new PerformanceProposal(27.393f,
					"stplus-ps2-358-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(26.4363f,
					"stplus-ps2-358-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(27.468f, "stplus-ps2-358-mod-split(PaymentSystem)-wrapper(IBooking)"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f,
					"stplus-ps2-358-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			p2Bot.insertInOrder(new PerformanceProposal(23.27813f, "stplus-ps2-366-mod-wrapper(IExporter)"));
			p2Bot.insertInOrder(new PerformanceProposal(25.2026f, "stplus-ps2-366-mod-wrapper(ITripDB)"));
			p2Bot.insertInOrder(new PerformanceProposal(23.04414f, "stplus-ps2-366-mod-wrapper(IExternalPayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(22.53669f, "stplus-ps2-366-mod-wrapper(IEmployeePayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(30.4235f, "stplus-ps2-366-mod-wrapper(IBooking)"));
			p2Bot.insertInOrder(new PerformanceProposal(23.8112f, "stplus-ps2-366-mod-wrapper(IBusinessTrip)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(22.348979999999997f, "stplus-ps2-366-mod-split(PaymentSystem)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(23.27813f, "stplus-ps2-366-mod-split(PaymentSystem)-wrapper(IExporter)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(25.2026f, "stplus-ps2-366-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			p2Bot.insertInOrder(new PerformanceProposal(23.04414f,
					"stplus-ps2-366-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(22.53669f,
					"stplus-ps2-366-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(30.4235f, "stplus-ps2-366-mod-split(PaymentSystem)-wrapper(IBooking)"));
			p2Bot.insertInOrder(new PerformanceProposal(23.8112f,
					"stplus-ps2-366-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			p2Bot.insertInOrder(new PerformanceProposal(16.98077f, "stplus-ps2-416-mod-wrapper(IExporter)"));
			p2Bot.insertInOrder(new PerformanceProposal(20.74776f, "stplus-ps2-416-mod-wrapper(ITripDB)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(17.827939999999998f, "stplus-ps2-416-mod-wrapper(IExternalPayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(16.83071f, "stplus-ps2-416-mod-wrapper(IEmployeePayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(18.914369999999998f, "stplus-ps2-416-mod-wrapper(IBooking)"));
			p2Bot.insertInOrder(new PerformanceProposal(18.26914f, "stplus-ps2-416-mod-wrapper(IBusinessTrip)"));
			p2Bot.insertInOrder(new PerformanceProposal(16.59996f, "stplus-ps2-416-mod-split(PaymentSystem)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(16.98077f, "stplus-ps2-416-mod-split(PaymentSystem)-wrapper(IExporter)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(20.74776f, "stplus-ps2-416-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			p2Bot.insertInOrder(new PerformanceProposal(17.827939999999998f,
					"stplus-ps2-416-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(16.83071f,
					"stplus-ps2-416-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(18.914369999999998f,
					"stplus-ps2-416-mod-split(PaymentSystem)-wrapper(IBooking)"));
			p2Bot.insertInOrder(new PerformanceProposal(18.26914f,
					"stplus-ps2-416-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			p2Bot.insertInOrder(new PerformanceProposal(27.95384f, "stplus-ps2-476-mod-wrapper(IExporter)"));
			p2Bot.insertInOrder(new PerformanceProposal(28.2396f, "stplus-ps2-476-mod-wrapper(ITripDB)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(28.722299999999997f, "stplus-ps2-476-mod-wrapper(IExternalPayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(27.23682f, "stplus-ps2-476-mod-wrapper(IEmployeePayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(28.721899999999998f, "stplus-ps2-476-mod-wrapper(IBooking)"));
			p2Bot.insertInOrder(new PerformanceProposal(30.6774f, "stplus-ps2-476-mod-wrapper(IBusinessTrip)"));
			p2Bot.insertInOrder(new PerformanceProposal(26.82446f, "stplus-ps2-476-mod-split(PaymentSystem)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(27.95384f, "stplus-ps2-476-mod-split(PaymentSystem)-wrapper(IExporter)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(28.2396f, "stplus-ps2-476-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			p2Bot.insertInOrder(new PerformanceProposal(28.722299999999997f,
					"stplus-ps2-476-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(27.23682f,
					"stplus-ps2-476-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(28.721899999999998f,
					"stplus-ps2-476-mod-split(PaymentSystem)-wrapper(IBooking)"));
			p2Bot.insertInOrder(new PerformanceProposal(30.6774f,
					"stplus-ps2-476-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			p2Bot.insertInOrder(new PerformanceProposal(27.0407f, "stplus-ps2-479-mod-wrapper(IExporter)"));
			p2Bot.insertInOrder(new PerformanceProposal(31.6378f, "stplus-ps2-479-mod-wrapper(ITripDB)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(29.962200000000003f, "stplus-ps2-479-mod-wrapper(IExternalPayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(26.8958f, "stplus-ps2-479-mod-wrapper(IEmployeePayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(28.9174f, "stplus-ps2-479-mod-wrapper(IBooking)"));
			p2Bot.insertInOrder(new PerformanceProposal(32.2499f, "stplus-ps2-479-mod-wrapper(IBusinessTrip)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(26.341700000000003f, "stplus-ps2-479-mod-split(PaymentSystem)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(27.0407f, "stplus-ps2-479-mod-split(PaymentSystem)-wrapper(IExporter)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(31.6378f, "stplus-ps2-479-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			p2Bot.insertInOrder(new PerformanceProposal(29.962200000000003f,
					"stplus-ps2-479-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(26.8958f,
					"stplus-ps2-479-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(28.9174f, "stplus-ps2-479-mod-split(PaymentSystem)-wrapper(IBooking)"));
			p2Bot.insertInOrder(new PerformanceProposal(32.2499f,
					"stplus-ps2-479-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));
			p2Bot.insertInOrder(new PerformanceProposal(24.31005f, "stplus-ps2-480-mod-wrapper(IExporter)"));
			p2Bot.insertInOrder(new PerformanceProposal(31.4533f, "stplus-ps2-480-mod-wrapper(ITripDB)"));
			p2Bot.insertInOrder(new PerformanceProposal(27.1398f, "stplus-ps2-480-mod-wrapper(IExternalPayment)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(24.192079999999997f, "stplus-ps2-480-mod-wrapper(IEmployeePayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(27.1388f, "stplus-ps2-480-mod-wrapper(IBooking)"));
			p2Bot.insertInOrder(new PerformanceProposal(31.4772f, "stplus-ps2-480-mod-wrapper(IBusinessTrip)"));
			p2Bot.insertInOrder(new PerformanceProposal(23.42628f, "stplus-ps2-480-mod-split(PaymentSystem)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(24.31005f, "stplus-ps2-480-mod-split(PaymentSystem)-wrapper(IExporter)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(31.4533f, "stplus-ps2-480-mod-split(PaymentSystem)-wrapper(ITripDB)"));
			p2Bot.insertInOrder(new PerformanceProposal(27.1398f,
					"stplus-ps2-480-mod-split(PaymentSystem)-wrapper(IExternalPayment)"));
			p2Bot.insertInOrder(new PerformanceProposal(24.192079999999997f,
					"stplus-ps2-480-mod-split(PaymentSystem)-wrapper(IEmployeePayment)"));
			p2Bot.insertInOrder(
					new PerformanceProposal(27.1388f, "stplus-ps2-480-mod-split(PaymentSystem)-wrapper(IBooking)"));
			p2Bot.insertInOrder(new PerformanceProposal(31.4772f,
					"stplus-ps2-480-mod-split(PaymentSystem)-wrapper(IBusinessTrip)"));

			// Second level - Performance tactics applied over Modifiability candidates
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-mod-split(PaymentSystem)-ps1-136"));
			p2Bot.insertInOrder(new PerformanceProposal(33.38085f, "stplus-mod-split(PaymentSystem)-ps1-162"));
			p2Bot.insertInOrder(new PerformanceProposal(20.1494f, "stplus-mod-split(PaymentSystem)-ps1-208"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-mod-split(PaymentSystem)-ps1-232"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-mod-split(PaymentSystem)-ps1-239"));
			p2Bot.insertInOrder(new PerformanceProposal(204.012f, "stplus-mod-split(PaymentSystem)-ps1-316"));
			p2Bot.insertInOrder(new PerformanceProposal(92.86609999999999f, "stplus-mod-split(PaymentSystem)-ps1-321"));
			p2Bot.insertInOrder(new PerformanceProposal(26.17173f, "stplus-mod-split(PaymentSystem)-ps1-355"));
			p2Bot.insertInOrder(
					new PerformanceProposal(205.01930000000002f, "stplus-mod-split(PaymentSystem)-ps1-367"));
			p2Bot.insertInOrder(new PerformanceProposal(26.40862f, "stplus-mod-split(PaymentSystem)-ps1-419"));
			p2Bot.insertInOrder(new PerformanceProposal(15.64816f, "stplus-mod-wrapper(IExporter)-ps1-66"));
			p2Bot.insertInOrder(new PerformanceProposal(16.73034f, "stplus-mod-wrapper(IExporter)-ps1-141"));
			p2Bot.insertInOrder(new PerformanceProposal(20.76079f, "stplus-mod-wrapper(IExporter)-ps1-227"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-mod-wrapper(IExporter)-ps1-238"));
			p2Bot.insertInOrder(new PerformanceProposal(111.7664f, "stplus-mod-wrapper(IExporter)-ps1-275"));
			p2Bot.insertInOrder(new PerformanceProposal(63.4805f, "stplus-mod-wrapper(IExporter)-ps1-329"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-mod-wrapper(IExporter)-ps1-450"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-mod-wrapper(IExporter)-ps1-459"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-mod-wrapper(IExporter)-ps1-494"));
			p2Bot.insertInOrder(new PerformanceProposal(63.4805f, "stplus-mod-wrapper(IExporter)-ps1-500"));
			p2Bot.insertInOrder(new PerformanceProposal(27.36168f, "stplus-mod-wrapper(ITripDB)-ps1-161"));
			p2Bot.insertInOrder(new PerformanceProposal(20.01599f, "stplus-mod-wrapper(ITripDB)-ps1-167"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-mod-wrapper(ITripDB)-ps1-228"));
			p2Bot.insertInOrder(new PerformanceProposal(28.367f, "stplus-mod-wrapper(ITripDB)-ps1-233"));
			p2Bot.insertInOrder(new PerformanceProposal(27.599690000000002f, "stplus-mod-wrapper(ITripDB)-ps1-353"));
			p2Bot.insertInOrder(new PerformanceProposal(71.07209999999999f, "stplus-mod-wrapper(ITripDB)-ps1-354"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-mod-wrapper(ITripDB)-ps1-357"));
			p2Bot.insertInOrder(new PerformanceProposal(71.1042f, "stplus-mod-wrapper(ITripDB)-ps1-358"));
			p2Bot.insertInOrder(new PerformanceProposal(72.6475f, "stplus-mod-wrapper(ITripDB)-ps1-432"));
			p2Bot.insertInOrder(new PerformanceProposal(71.1042f, "stplus-mod-wrapper(ITripDB)-ps1-476"));
			p2Bot.insertInOrder(
					new PerformanceProposal(111.78269999999999f, "stplus-mod-wrapper(IExternalPayment)-ps1-195"));
			p2Bot.insertInOrder(new PerformanceProposal(22.81521f, "stplus-mod-wrapper(IExternalPayment)-ps1-247"));
			p2Bot.insertInOrder(
					new PerformanceProposal(111.78269999999999f, "stplus-mod-wrapper(IExternalPayment)-ps1-263"));
			p2Bot.insertInOrder(
					new PerformanceProposal(127.65870000000001f, "stplus-mod-wrapper(IExternalPayment)-ps1-333"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-mod-wrapper(IExternalPayment)-ps1-350"));
			p2Bot.insertInOrder(new PerformanceProposal(188.7133f, "stplus-mod-wrapper(IExternalPayment)-ps1-396"));
			p2Bot.insertInOrder(new PerformanceProposal(32.8668f, "stplus-mod-wrapper(IExternalPayment)-ps1-405"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-mod-wrapper(IExternalPayment)-ps1-425"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-mod-wrapper(IExternalPayment)-ps1-426"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-mod-wrapper(IExternalPayment)-ps1-464"));
			p2Bot.insertInOrder(new PerformanceProposal(34.44582f, "stplus-mod-wrapper(IEmployeePayment)-ps1-109"));
			p2Bot.insertInOrder(new PerformanceProposal(24.53481f, "stplus-mod-wrapper(IEmployeePayment)-ps1-133"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-mod-wrapper(IEmployeePayment)-ps1-138"));
			p2Bot.insertInOrder(new PerformanceProposal(42.7341f, "stplus-mod-wrapper(IEmployeePayment)-ps1-311"));
			p2Bot.insertInOrder(new PerformanceProposal(58.8442f, "stplus-mod-wrapper(IEmployeePayment)-ps1-330"));
			p2Bot.insertInOrder(new PerformanceProposal(18.59981f, "stplus-mod-wrapper(IEmployeePayment)-ps1-41"));
			p2Bot.insertInOrder(new PerformanceProposal(24.47238f, "stplus-mod-wrapper(IEmployeePayment)-ps1-416"));
			p2Bot.insertInOrder(
					new PerformanceProposal(28.883899999999997f, "stplus-mod-wrapper(IEmployeePayment)-ps1-417"));
			p2Bot.insertInOrder(new PerformanceProposal(30.94428f, "stplus-mod-wrapper(IEmployeePayment)-ps1-447"));
			p2Bot.insertInOrder(new PerformanceProposal(24.4206f, "stplus-mod-wrapper(IEmployeePayment)-ps1-497"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-mod-wrapper(IBooking)-ps1-154"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-mod-wrapper(IBooking)-ps1-229"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-mod-wrapper(IBooking)-ps1-237"));
			p2Bot.insertInOrder(new PerformanceProposal(16.95303f, "stplus-mod-wrapper(IBooking)-ps1-269"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-mod-wrapper(IBooking)-ps1-277"));
			p2Bot.insertInOrder(new PerformanceProposal(20.1749f, "stplus-mod-wrapper(IBooking)-ps1-282"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-mod-wrapper(IBooking)-ps1-316"));
			p2Bot.insertInOrder(new PerformanceProposal(16.83476f, "stplus-mod-wrapper(IBooking)-ps1-364"));
			p2Bot.insertInOrder(new PerformanceProposal(23.23754f, "stplus-mod-wrapper(IBooking)-ps1-404"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-mod-wrapper(IBooking)-ps1-450"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-mod-wrapper(IBusinessTrip)-ps1-110"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-mod-wrapper(IBusinessTrip)-ps1-142"));
			p2Bot.insertInOrder(new PerformanceProposal(59.5406f, "stplus-mod-wrapper(IBusinessTrip)-ps1-216"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-mod-wrapper(IBusinessTrip)-ps1-232"));
			p2Bot.insertInOrder(
					new PerformanceProposal(676.8499999999999f, "stplus-mod-wrapper(IBusinessTrip)-ps1-295"));
			p2Bot.insertInOrder(new PerformanceProposal(27.61938f, "stplus-mod-wrapper(IBusinessTrip)-ps1-296"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-mod-wrapper(IBusinessTrip)-ps1-442"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-mod-wrapper(IBusinessTrip)-ps1-472"));
			p2Bot.insertInOrder(new PerformanceProposal(27.61938f, "stplus-mod-wrapper(IBusinessTrip)-ps1-474"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f, "stplus-mod-wrapper(IBusinessTrip)-ps1-478"));
			p2Bot.insertInOrder(new PerformanceProposal(21.945320000000002f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps1-118"));
			p2Bot.insertInOrder(
					new PerformanceProposal(38.7785f, "stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps1-275"));
			p2Bot.insertInOrder(
					new PerformanceProposal(23.42909f, "stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps1-351"));
			p2Bot.insertInOrder(
					new PerformanceProposal(38.7785f, "stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps1-355"));
			p2Bot.insertInOrder(
					new PerformanceProposal(23.14489f, "stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps1-382"));
			p2Bot.insertInOrder(new PerformanceProposal(24.387050000000002f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps1-389"));
			p2Bot.insertInOrder(
					new PerformanceProposal(999999999f, "stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps1-390"));
			p2Bot.insertInOrder(
					new PerformanceProposal(34.0922f, "stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps1-406"));
			p2Bot.insertInOrder(
					new PerformanceProposal(26.9446f, "stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps1-425"));
			p2Bot.insertInOrder(
					new PerformanceProposal(27.2395f, "stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps1-428"));
			p2Bot.insertInOrder(
					new PerformanceProposal(41.4988f, "stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps1-220"));
			p2Bot.insertInOrder(
					new PerformanceProposal(999999999f, "stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps1-290"));
			p2Bot.insertInOrder(
					new PerformanceProposal(41.09465f, "stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps1-327"));
			p2Bot.insertInOrder(
					new PerformanceProposal(41.22681f, "stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps1-377"));
			p2Bot.insertInOrder(
					new PerformanceProposal(41.5635f, "stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps1-406"));
			p2Bot.insertInOrder(
					new PerformanceProposal(101.8545f, "stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps1-423"));
			p2Bot.insertInOrder(new PerformanceProposal(22.372300000000003f,
					"stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps1-439"));
			p2Bot.insertInOrder(
					new PerformanceProposal(999999999f, "stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps1-446"));
			p2Bot.insertInOrder(
					new PerformanceProposal(41.4456f, "stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps1-447"));
			p2Bot.insertInOrder(
					new PerformanceProposal(44.4581f, "stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps1-454"));
			p2Bot.insertInOrder(new PerformanceProposal(22.32345f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps1-197"));
			p2Bot.insertInOrder(new PerformanceProposal(225.6522f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps1-232"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps1-233"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps1-235"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps1-276"));
			p2Bot.insertInOrder(new PerformanceProposal(225.6522f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps1-277"));
			p2Bot.insertInOrder(new PerformanceProposal(225.6522f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps1-33"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps1-355"));
			p2Bot.insertInOrder(new PerformanceProposal(22.35012f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps1-362"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps1-368"));
			p2Bot.insertInOrder(new PerformanceProposal(97.78909999999999f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps1-12"));
			p2Bot.insertInOrder(new PerformanceProposal(22.792949999999998f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps1-13"));
			p2Bot.insertInOrder(new PerformanceProposal(19.30361f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps1-176"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps1-224"));
			p2Bot.insertInOrder(new PerformanceProposal(19.549709999999997f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps1-281"));
			p2Bot.insertInOrder(new PerformanceProposal(19.72704f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps1-318"));
			p2Bot.insertInOrder(new PerformanceProposal(19.27683f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps1-356"));
			p2Bot.insertInOrder(new PerformanceProposal(21.18271f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps1-402"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps1-438"));
			p2Bot.insertInOrder(new PerformanceProposal(19.73084f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps1-483"));
			p2Bot.insertInOrder(
					new PerformanceProposal(34.9658f, "stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps1-100"));
			p2Bot.insertInOrder(new PerformanceProposal(117.64510000000001f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps1-145"));
			p2Bot.insertInOrder(
					new PerformanceProposal(75.4641f, "stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps1-232"));
			p2Bot.insertInOrder(
					new PerformanceProposal(28.88559f, "stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps1-300"));
			p2Bot.insertInOrder(
					new PerformanceProposal(35.153f, "stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps1-326"));
			p2Bot.insertInOrder(
					new PerformanceProposal(999999999f, "stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps1-353"));
			p2Bot.insertInOrder(
					new PerformanceProposal(79.4713f, "stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps1-400"));
			p2Bot.insertInOrder(
					new PerformanceProposal(999999999f, "stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps1-474"));
			p2Bot.insertInOrder(
					new PerformanceProposal(23.85635f, "stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps1-488"));
			p2Bot.insertInOrder(new PerformanceProposal(117.64510000000001f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps1-86"));
			p2Bot.insertInOrder(new PerformanceProposal(46.3283f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps1-319"));
			p2Bot.insertInOrder(new PerformanceProposal(46.3343f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps1-321"));
			p2Bot.insertInOrder(new PerformanceProposal(40.141999999999996f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps1-323"));
			p2Bot.insertInOrder(new PerformanceProposal(38.4923f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps1-356"));
			p2Bot.insertInOrder(new PerformanceProposal(47.1008f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps1-392"));
			p2Bot.insertInOrder(
					new PerformanceProposal(38.539f, "stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps1-397"));
			p2Bot.insertInOrder(new PerformanceProposal(47.0098f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps1-426"));
			p2Bot.insertInOrder(new PerformanceProposal(999999999f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps1-427"));
			p2Bot.insertInOrder(new PerformanceProposal(46.8977f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps1-433"));
			p2Bot.insertInOrder(
					new PerformanceProposal(41.13f, "stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps1-62"));
			p2Bot.insertInOrder(new PerformanceProposal(25.94296f, "stplus-mod-split(PaymentSystem)-ps2-119"));
			p2Bot.insertInOrder(new PerformanceProposal(24.397f, "stplus-mod-split(PaymentSystem)-ps2-179"));
			p2Bot.insertInOrder(new PerformanceProposal(24.67808f, "stplus-mod-split(PaymentSystem)-ps2-191"));
			p2Bot.insertInOrder(new PerformanceProposal(17.94107f, "stplus-mod-split(PaymentSystem)-ps2-222"));
			p2Bot.insertInOrder(new PerformanceProposal(26.00405f, "stplus-mod-split(PaymentSystem)-ps2-239"));
			p2Bot.insertInOrder(new PerformanceProposal(28.2605f, "stplus-mod-split(PaymentSystem)-ps2-402"));
			p2Bot.insertInOrder(new PerformanceProposal(19.39684f, "stplus-mod-split(PaymentSystem)-ps2-43"));
			p2Bot.insertInOrder(new PerformanceProposal(17.11546f, "stplus-mod-split(PaymentSystem)-ps2-432"));
			p2Bot.insertInOrder(new PerformanceProposal(19.39684f, "stplus-mod-split(PaymentSystem)-ps2-470"));
			p2Bot.insertInOrder(new PerformanceProposal(24.694049999999997f, "stplus-mod-split(PaymentSystem)-ps2-69"));
			p2Bot.insertInOrder(new PerformanceProposal(25.67541f, "stplus-mod-wrapper(IExporter)-ps2-146"));
			p2Bot.insertInOrder(new PerformanceProposal(18.67745f, "stplus-mod-wrapper(IExporter)-ps2-148"));
			p2Bot.insertInOrder(new PerformanceProposal(23.8537f, "stplus-mod-wrapper(IExporter)-ps2-162"));
			p2Bot.insertInOrder(new PerformanceProposal(25.79572f, "stplus-mod-wrapper(IExporter)-ps2-243"));
			p2Bot.insertInOrder(new PerformanceProposal(23.861150000000002f, "stplus-mod-wrapper(IExporter)-ps2-270"));
			p2Bot.insertInOrder(new PerformanceProposal(18.67745f, "stplus-mod-wrapper(IExporter)-ps2-281"));
			p2Bot.insertInOrder(new PerformanceProposal(25.79572f, "stplus-mod-wrapper(IExporter)-ps2-318"));
			p2Bot.insertInOrder(new PerformanceProposal(27.550530000000002f, "stplus-mod-wrapper(IExporter)-ps2-324"));
			p2Bot.insertInOrder(new PerformanceProposal(17.7536f, "stplus-mod-wrapper(IExporter)-ps2-52"));
			p2Bot.insertInOrder(new PerformanceProposal(26.46633f, "stplus-mod-wrapper(IExporter)-ps2-90"));
			p2Bot.insertInOrder(new PerformanceProposal(20.67713f, "stplus-mod-wrapper(ITripDB)-ps2-143"));
			p2Bot.insertInOrder(new PerformanceProposal(26.2009f, "stplus-mod-wrapper(ITripDB)-ps2-189"));
			p2Bot.insertInOrder(new PerformanceProposal(24.5475f, "stplus-mod-wrapper(ITripDB)-ps2-192"));
			p2Bot.insertInOrder(new PerformanceProposal(25.33347f, "stplus-mod-wrapper(ITripDB)-ps2-231"));
			p2Bot.insertInOrder(new PerformanceProposal(25.32289f, "stplus-mod-wrapper(ITripDB)-ps2-309"));
			p2Bot.insertInOrder(new PerformanceProposal(26.2027f, "stplus-mod-wrapper(ITripDB)-ps2-314"));
			p2Bot.insertInOrder(new PerformanceProposal(17.87123f, "stplus-mod-wrapper(ITripDB)-ps2-33"));
			p2Bot.insertInOrder(new PerformanceProposal(25.33347f, "stplus-mod-wrapper(ITripDB)-ps2-347"));
			p2Bot.insertInOrder(new PerformanceProposal(19.13054f, "stplus-mod-wrapper(ITripDB)-ps2-354"));
			p2Bot.insertInOrder(new PerformanceProposal(19.9279f, "stplus-mod-wrapper(ITripDB)-ps2-86"));
			p2Bot.insertInOrder(
					new PerformanceProposal(18.066119999999998f, "stplus-mod-wrapper(IExternalPayment)-ps2-145"));
			p2Bot.insertInOrder(new PerformanceProposal(21.32157f, "stplus-mod-wrapper(IExternalPayment)-ps2-147"));
			p2Bot.insertInOrder(
					new PerformanceProposal(20.506819999999998f, "stplus-mod-wrapper(IExternalPayment)-ps2-270"));
			p2Bot.insertInOrder(new PerformanceProposal(23.0327f, "stplus-mod-wrapper(IExternalPayment)-ps2-275"));
			p2Bot.insertInOrder(new PerformanceProposal(21.32157f, "stplus-mod-wrapper(IExternalPayment)-ps2-324"));
			p2Bot.insertInOrder(new PerformanceProposal(17.82469f, "stplus-mod-wrapper(IExternalPayment)-ps2-354"));
			p2Bot.insertInOrder(new PerformanceProposal(21.38961f, "stplus-mod-wrapper(IExternalPayment)-ps2-369"));
			p2Bot.insertInOrder(new PerformanceProposal(18.00947f, "stplus-mod-wrapper(IExternalPayment)-ps2-445"));
			p2Bot.insertInOrder(
					new PerformanceProposal(20.941789999999997f, "stplus-mod-wrapper(IExternalPayment)-ps2-449"));
			p2Bot.insertInOrder(
					new PerformanceProposal(19.967129999999997f, "stplus-mod-wrapper(IExternalPayment)-ps2-453"));
			p2Bot.insertInOrder(new PerformanceProposal(21.79397f, "stplus-mod-wrapper(IEmployeePayment)-ps2-130"));
			p2Bot.insertInOrder(new PerformanceProposal(21.97094f, "stplus-mod-wrapper(IEmployeePayment)-ps2-237"));
			p2Bot.insertInOrder(new PerformanceProposal(15.27712f, "stplus-mod-wrapper(IEmployeePayment)-ps2-257"));
			p2Bot.insertInOrder(new PerformanceProposal(16.28472f, "stplus-mod-wrapper(IEmployeePayment)-ps2-310"));
			p2Bot.insertInOrder(new PerformanceProposal(18.92522f, "stplus-mod-wrapper(IEmployeePayment)-ps2-358"));
			p2Bot.insertInOrder(
					new PerformanceProposal(18.475929999999998f, "stplus-mod-wrapper(IEmployeePayment)-ps2-361"));
			p2Bot.insertInOrder(new PerformanceProposal(21.3711f, "stplus-mod-wrapper(IEmployeePayment)-ps2-437"));
			p2Bot.insertInOrder(new PerformanceProposal(21.65672f, "stplus-mod-wrapper(IEmployeePayment)-ps2-441"));
			p2Bot.insertInOrder(new PerformanceProposal(21.78982f, "stplus-mod-wrapper(IEmployeePayment)-ps2-489"));
			p2Bot.insertInOrder(new PerformanceProposal(16.28472f, "stplus-mod-wrapper(IEmployeePayment)-ps2-496"));
			p2Bot.insertInOrder(new PerformanceProposal(19.5718f, "stplus-mod-wrapper(IBooking)-ps2-117"));
			p2Bot.insertInOrder(new PerformanceProposal(26.408720000000002f, "stplus-mod-wrapper(IBooking)-ps2-143"));
			p2Bot.insertInOrder(new PerformanceProposal(23.629199999999997f, "stplus-mod-wrapper(IBooking)-ps2-237"));
			p2Bot.insertInOrder(new PerformanceProposal(24.28332f, "stplus-mod-wrapper(IBooking)-ps2-286"));
			p2Bot.insertInOrder(new PerformanceProposal(22.2757f, "stplus-mod-wrapper(IBooking)-ps2-299"));
			p2Bot.insertInOrder(new PerformanceProposal(26.63727f, "stplus-mod-wrapper(IBooking)-ps2-330"));
			p2Bot.insertInOrder(new PerformanceProposal(26.114240000000002f, "stplus-mod-wrapper(IBooking)-ps2-375"));
			p2Bot.insertInOrder(new PerformanceProposal(25.13125f, "stplus-mod-wrapper(IBooking)-ps2-376"));
			p2Bot.insertInOrder(new PerformanceProposal(25.0918f, "stplus-mod-wrapper(IBooking)-ps2-384"));
			p2Bot.insertInOrder(new PerformanceProposal(21.34015f, "stplus-mod-wrapper(IBooking)-ps2-401"));
			p2Bot.insertInOrder(new PerformanceProposal(25.18987f, "stplus-mod-wrapper(IBusinessTrip)-ps2-107"));
			p2Bot.insertInOrder(new PerformanceProposal(26.54798f, "stplus-mod-wrapper(IBusinessTrip)-ps2-130"));
			p2Bot.insertInOrder(new PerformanceProposal(14.58621f, "stplus-mod-wrapper(IBusinessTrip)-ps2-156"));
			p2Bot.insertInOrder(new PerformanceProposal(26.51155f, "stplus-mod-wrapper(IBusinessTrip)-ps2-238"));
			p2Bot.insertInOrder(new PerformanceProposal(14.58621f, "stplus-mod-wrapper(IBusinessTrip)-ps2-276"));
			p2Bot.insertInOrder(new PerformanceProposal(15.70985f, "stplus-mod-wrapper(IBusinessTrip)-ps2-282"));
			p2Bot.insertInOrder(new PerformanceProposal(23.9996f, "stplus-mod-wrapper(IBusinessTrip)-ps2-440"));
			p2Bot.insertInOrder(new PerformanceProposal(20.26565f, "stplus-mod-wrapper(IBusinessTrip)-ps2-442"));
			p2Bot.insertInOrder(new PerformanceProposal(25.71659f, "stplus-mod-wrapper(IBusinessTrip)-ps2-90"));
			p2Bot.insertInOrder(new PerformanceProposal(27.26067f, "stplus-mod-wrapper(IBusinessTrip)-ps2-98"));
			p2Bot.insertInOrder(new PerformanceProposal(23.244500000000002f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps2-143"));
			p2Bot.insertInOrder(
					new PerformanceProposal(25.32114f, "stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps2-191"));
			p2Bot.insertInOrder(
					new PerformanceProposal(22.69615f, "stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps2-268"));
			p2Bot.insertInOrder(
					new PerformanceProposal(22.1229f, "stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps2-307"));
			p2Bot.insertInOrder(
					new PerformanceProposal(23.17691f, "stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps2-314"));
			p2Bot.insertInOrder(new PerformanceProposal(23.244500000000002f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps2-352"));
			p2Bot.insertInOrder(
					new PerformanceProposal(22.82142f, "stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps2-354"));
			p2Bot.insertInOrder(
					new PerformanceProposal(22.1229f, "stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps2-355"));
			p2Bot.insertInOrder(new PerformanceProposal(25.055799999999998f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps2-55"));
			p2Bot.insertInOrder(
					new PerformanceProposal(22.1229f, "stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps2-7"));
			p2Bot.insertInOrder(
					new PerformanceProposal(18.8551f, "stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps2-137"));
			p2Bot.insertInOrder(
					new PerformanceProposal(21.95852f, "stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps2-142"));
			p2Bot.insertInOrder(
					new PerformanceProposal(30.25407f, "stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps2-171"));
			p2Bot.insertInOrder(
					new PerformanceProposal(19.60371f, "stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps2-189"));
			p2Bot.insertInOrder(
					new PerformanceProposal(26.7557f, "stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps2-209"));
			p2Bot.insertInOrder(
					new PerformanceProposal(20.05168f, "stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps2-238"));
			p2Bot.insertInOrder(
					new PerformanceProposal(25.74126f, "stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps2-275"));
			p2Bot.insertInOrder(
					new PerformanceProposal(31.11766f, "stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps2-281"));
			p2Bot.insertInOrder(
					new PerformanceProposal(28.0131f, "stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps2-285"));
			p2Bot.insertInOrder(
					new PerformanceProposal(31.1847f, "stplus-mod-split(PaymentSystem)-wrapper(ITripDB)-ps2-414"));
			p2Bot.insertInOrder(new PerformanceProposal(20.67632f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps2-192"));
			p2Bot.insertInOrder(new PerformanceProposal(22.45086f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps2-203"));
			p2Bot.insertInOrder(new PerformanceProposal(22.4233f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps2-275"));
			p2Bot.insertInOrder(new PerformanceProposal(20.013109999999998f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps2-279"));
			p2Bot.insertInOrder(new PerformanceProposal(21.9542f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps2-360"));
			p2Bot.insertInOrder(new PerformanceProposal(23.8709f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps2-364"));
			p2Bot.insertInOrder(new PerformanceProposal(21.55081f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps2-403"));
			p2Bot.insertInOrder(new PerformanceProposal(23.67698f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps2-439"));
			p2Bot.insertInOrder(new PerformanceProposal(19.46049f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps2-450"));
			p2Bot.insertInOrder(new PerformanceProposal(23.78969f,
					"stplus-mod-split(PaymentSystem)-wrapper(IExternalPayment)-ps2-492"));
			p2Bot.insertInOrder(new PerformanceProposal(17.555030000000002f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps2-250"));
			p2Bot.insertInOrder(new PerformanceProposal(18.10486f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps2-321"));
			p2Bot.insertInOrder(new PerformanceProposal(17.77739f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps2-33"));
			p2Bot.insertInOrder(new PerformanceProposal(17.962629999999997f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps2-359"));
			p2Bot.insertInOrder(new PerformanceProposal(18.05766f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps2-360"));
			p2Bot.insertInOrder(new PerformanceProposal(17.7723f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps2-369"));
			p2Bot.insertInOrder(new PerformanceProposal(17.90887f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps2-370"));
			p2Bot.insertInOrder(new PerformanceProposal(17.962629999999997f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps2-456"));
			p2Bot.insertInOrder(new PerformanceProposal(17.97129f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps2-499"));
			p2Bot.insertInOrder(new PerformanceProposal(17.41653f,
					"stplus-mod-split(PaymentSystem)-wrapper(IEmployeePayment)-ps2-501"));
			p2Bot.insertInOrder(new PerformanceProposal(24.289299999999997f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps2-226"));
			p2Bot.insertInOrder(
					new PerformanceProposal(25.9407f, "stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps2-325"));
			p2Bot.insertInOrder(
					new PerformanceProposal(25.9099f, "stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps2-401"));
			p2Bot.insertInOrder(
					new PerformanceProposal(24.40802f, "stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps2-415"));
			p2Bot.insertInOrder(
					new PerformanceProposal(23.22956f, "stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps2-452"));
			p2Bot.insertInOrder(
					new PerformanceProposal(25.34497f, "stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps2-456"));
			p2Bot.insertInOrder(
					new PerformanceProposal(26.0076f, "stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps2-462"));
			p2Bot.insertInOrder(
					new PerformanceProposal(25.3459f, "stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps2-505"));
			p2Bot.insertInOrder(
					new PerformanceProposal(22.37433f, "stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps2-506"));
			p2Bot.insertInOrder(
					new PerformanceProposal(26.5158f, "stplus-mod-split(PaymentSystem)-wrapper(IBooking)-ps2-508"));
			p2Bot.insertInOrder(new PerformanceProposal(24.89035f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps2-119"));
			p2Bot.insertInOrder(new PerformanceProposal(22.41572f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps2-242"));
			p2Bot.insertInOrder(new PerformanceProposal(27.062600000000003f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps2-278"));
			p2Bot.insertInOrder(new PerformanceProposal(25.4423f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps2-344"));
			p2Bot.insertInOrder(new PerformanceProposal(26.54004f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps2-373"));
			p2Bot.insertInOrder(new PerformanceProposal(25.4423f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps2-407"));
			p2Bot.insertInOrder(new PerformanceProposal(27.482509999999998f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps2-408"));
			p2Bot.insertInOrder(new PerformanceProposal(22.26184f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps2-439"));
			p2Bot.insertInOrder(new PerformanceProposal(26.18786f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps2-447"));
			p2Bot.insertInOrder(new PerformanceProposal(25.58386f,
					"stplus-mod-split(PaymentSystem)-wrapper(IBusinessTrip)-ps2-482"));

			List<SillyBot> ret = new ArrayList<>();
			ret.add(m1Bot);
			ret.add(m2Bot);
			ret.add(p1Bot);
			ret.add(p2Bot);

			/*
			 * System.out.println(m1Bot.getUtilityFor(
			 * "stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps1-355"));
			 * System.out.println(m2Bot.getUtilityFor(
			 * "stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps1-355"));
			 * System.out.println(p1Bot.getUtilityFor(
			 * "stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps1-355"));
			 * System.out.println(p2Bot.getUtilityFor(
			 * "stplus-mod-split(PaymentSystem)-wrapper(IExporter)-ps1-355"));
			 */

			m1Bot.printUtilies();
			m2Bot.printUtilies();
			p1Bot.printUtilies();
			p2Bot.printUtilies();
			return ret;
		}


	}

}

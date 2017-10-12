package io.github.squat_team.modifiability.kamp;

import io.github.squat_team.model.OptimizationType;
import io.github.squat_team.model.PCMResult;
import io.github.squat_team.model.ResponseMeasureType;
import io.github.squat_team.modifiability.ModifiabilityElement;
import io.github.squat_team.modifiability.ModifiabilityInstruction;
import io.github.squat_team.modifiability.ModifiabilityOperation;
import io.github.squat_team.modifiability.ModifiabilityPCMScenario;

/**
 * Contains some modifiability scenarios for the stplus model for testing.
 */
public class StplusScenarios {

	public ModifiabilityPCMScenario createModifiabilityScenarioS1(ResponseMeasureType type, Comparable<Float> response) {
		ModifiabilityPCMScenario scenario = new ModifiabilityPCMScenario(OptimizationType.MINIMIZATION);
		PCMResult expectedResult = new PCMResult(type);
		expectedResult.setResponse(response);
		scenario.setExpectedResponse(expectedResult);

		ModifiabilityInstruction i1 = new ModifiabilityInstruction();
		i1.operation = ModifiabilityOperation.MODIFY;
		i1.element = ModifiabilityElement.INTERFACE;
		i1.parameters.put("name", "IExternalPayment");
		scenario.addChange(i1);
		ModifiabilityInstruction i2 = new ModifiabilityInstruction();
		i2.operation = ModifiabilityOperation.MODIFY;
		i2.element = ModifiabilityElement.COMPONENT;
		i2.parameters.put("name", "BusinessTripMgmt");
		scenario.addChange(i2);

		return scenario;
	}

	public ModifiabilityPCMScenario createModifiabilityScenarioS2(ResponseMeasureType type, Comparable<Float> response) {
		ModifiabilityPCMScenario scenario = new ModifiabilityPCMScenario(OptimizationType.MINIMIZATION);
		PCMResult expectedResult = new PCMResult(type);
		expectedResult.setResponse(response);
		scenario.setExpectedResponse(expectedResult);

		ModifiabilityInstruction i1 = new ModifiabilityInstruction();
		i1.operation = ModifiabilityOperation.MODIFY;
		i1.element = ModifiabilityElement.INTERFACE;
		i1.parameters.put("name", "ITripDB");
		scenario.addChange(i1);
		ModifiabilityInstruction i2 = new ModifiabilityInstruction();
		i2.operation = ModifiabilityOperation.CREATE;
		i2.element = ModifiabilityElement.INTERFACE;
		i2.parameters.put("name", "Analytics");
		scenario.addChange(i2);
		ModifiabilityInstruction i3 = new ModifiabilityInstruction();
		i3.operation = ModifiabilityOperation.CREATE;
		i3.element = ModifiabilityElement.OPERATION;
		i3.parameters.put("iname", "Analytics");
		i3.parameters.put("oname", "getLastTrips");
		scenario.addChange(i3);
		ModifiabilityInstruction i4 = new ModifiabilityInstruction();
		i4.operation = ModifiabilityOperation.CREATE;
		i4.element = ModifiabilityElement.COMPONENT;
		i4.parameters.put("name", "Insights");
		scenario.addChange(i4);
		ModifiabilityInstruction i5 = new ModifiabilityInstruction();
		i5.operation = ModifiabilityOperation.CREATE;
		i5.element = ModifiabilityElement.PROVIDEDROLE;
		i5.parameters.put("cname", "Insights");
		i5.parameters.put("iname", "Analytics");
		scenario.addChange(i5);
		ModifiabilityInstruction i6 = new ModifiabilityInstruction();
		i6.operation = ModifiabilityOperation.CREATE;
		i6.element = ModifiabilityElement.REQUIREDROLE;
		i6.parameters.put("cname", "Insights");
		i6.parameters.put("iname", "ITripDB");
		scenario.addChange(i6);

		return scenario;
	}

	public ModifiabilityPCMScenario createModifiabilityScenarioS3(ResponseMeasureType type, Comparable<Float> response) {
		ModifiabilityPCMScenario scenario = new ModifiabilityPCMScenario(OptimizationType.MINIMIZATION);
		PCMResult expectedResult = new PCMResult(type);
		expectedResult.setResponse(response);
		scenario.setExpectedResponse(expectedResult);

		ModifiabilityInstruction i1 = new ModifiabilityInstruction();
		i1.operation = ModifiabilityOperation.MODIFY;
		i1.element = ModifiabilityElement.INTERFACE;
		i1.parameters.put("name", "IBusiness Trip");
		scenario.addChange(i1);
		ModifiabilityInstruction i2 = new ModifiabilityInstruction();
		i2.operation = ModifiabilityOperation.CREATE;
		i2.element = ModifiabilityElement.INTERFACE;
		i2.parameters.put("name", "IUserManagement");
		scenario.addChange(i2);
		ModifiabilityInstruction i3a = new ModifiabilityInstruction();
		i3a.operation = ModifiabilityOperation.CREATE;
		i3a.element = ModifiabilityElement.OPERATION;
		i3a.parameters.put("iname", "IUserManagement");
		i3a.parameters.put("oname", "verifyLoginData");
		scenario.addChange(i3a);
		ModifiabilityInstruction i3b = new ModifiabilityInstruction();
		i3b.operation = ModifiabilityOperation.CREATE;
		i3b.element = ModifiabilityElement.OPERATION;
		i3b.parameters.put("iname", "IUserManagement");
		i3b.parameters.put("oname", "updateUser");
		scenario.addChange(i3b);
		ModifiabilityInstruction i4 = new ModifiabilityInstruction();
		i4.operation = ModifiabilityOperation.CREATE;
		i4.element = ModifiabilityElement.COMPONENT;
		i4.parameters.put("name", "UserManagement");
		scenario.addChange(i4);
		ModifiabilityInstruction i5 = new ModifiabilityInstruction();
		i5.operation = ModifiabilityOperation.CREATE;
		i5.element = ModifiabilityElement.PROVIDEDROLE;
		i5.parameters.put("cname", "UserManagement");
		i5.parameters.put("iname", "IUserManagement");
		scenario.addChange(i5);
		ModifiabilityInstruction i6 = new ModifiabilityInstruction();
		i6.operation = ModifiabilityOperation.CREATE;
		i6.element = ModifiabilityElement.REQUIREDROLE;
		i6.parameters.put("cname", "BusinessTripMgmt");
		i6.parameters.put("iname", "IUserManagement");
		scenario.addChange(i6);

		return scenario;
	}

	public ModifiabilityPCMScenario createModifiabilityScenarioS4(ResponseMeasureType type, Comparable<Float> response) {
		ModifiabilityPCMScenario scenario = new ModifiabilityPCMScenario(OptimizationType.MINIMIZATION);
		PCMResult expectedResult = new PCMResult(type);
		expectedResult.setResponse(response);
		scenario.setExpectedResponse(expectedResult);

		ModifiabilityInstruction i1 = new ModifiabilityInstruction();
		i1.operation = ModifiabilityOperation.MODIFY;
		i1.element = ModifiabilityElement.INTERFACE;
		i1.parameters.put("name", "ITripDB");
		scenario.addChange(i1);
		ModifiabilityInstruction i2 = new ModifiabilityInstruction();
		i2.operation = ModifiabilityOperation.MODIFY;
		i2.element = ModifiabilityElement.INTERFACE;
		i2.parameters.put("name", "IBooking");
		scenario.addChange(i2);
		ModifiabilityInstruction i3 = new ModifiabilityInstruction();
		i3.operation = ModifiabilityOperation.MODIFY;
		i3.element = ModifiabilityElement.COMPONENT;
		i3.parameters.put("name", "BusinessTripMgmt");
		scenario.addChange(i3);

		return scenario;
	}

}

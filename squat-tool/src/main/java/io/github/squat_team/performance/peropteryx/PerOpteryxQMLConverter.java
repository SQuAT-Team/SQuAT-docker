package io.github.squat_team.performance.peropteryx;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import io.github.squat_team.performance.PerformanceMetric;
import io.github.squat_team.performance.AbstractPerformancePCMScenario ;

public class PerOpteryxQMLConverter {
	private static final String FILE_EXTENSION = ".qmldeclarations";
	private static final String FILE_NAME = "AUTOGEN_";

	public static String convert(String usageModelPath, AbstractPerformancePCMScenario  scenario) throws IOException {
		String usageModelFileName = getFileName(usageModelPath);
		String usageModelID = searchForUsageModelID(usageModelPath);
		String objectiveID = chooseObjectictiveID(scenario.getMetric());
		String outputFilePath = getFilePath(usageModelPath);
		String outputFileName = generateOutputFileName(scenario.getMetric());

		List<String> lines = generateFileData(usageModelFileName, usageModelID, objectiveID);
		createFile(outputFilePath + outputFileName, lines);
		return outputFilePath + outputFileName;
	}

	private static String generateOutputFileName(PerformanceMetric metric) {
		return FILE_NAME + metric.toString() + FILE_EXTENSION;
	}

	private static void createFile(String outputPath, List<String> lines) throws IOException {
		Path file = Paths.get(outputPath);
		Files.write(file, lines, Charset.forName("UTF-8"));
	}

	private static String getFilePath(String usageModelPath) {
		File file = new File(usageModelPath);
		return file.getParentFile() + File.separator;
	}

	private static String getFileName(String usageModelPath) {
		File file = new File(usageModelPath);
		return file.getName();
	}

	private static String chooseObjectictiveID(PerformanceMetric metric) {
		String objectiveID;
		switch (metric) {
		case MAX_CPU_UTILIZATION:
			objectiveID = "Dimension_maxCPUUtilization.qmlcontracttype" + "#" + "_YhNVUMioEeCp_vzD4x7GMA";
			break;
		case RESPONSE_TIME:
			objectiveID = "Dimension_responsetime.qmlcontracttype" + "#" + "_r_iVkKCrEd-s9uTaURbrKA";
			break;
		case THROUGHPUT:
			objectiveID = "Dimension_throughput.qmlcontracttype" + "#" + "_r_iVkKCrEd-s9uTaURbrKA2";
			break;
		default:
			objectiveID = "Dimension_responsetime.qmlcontracttype#" + "#" + "_r_iVkKCrEd-s9uTaURbrKA";
		}
		return objectiveID;
	}

	private static String searchForUsageModelID(String usageModelPath) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get(usageModelPath), StandardCharsets.UTF_8);
		for (String line : lines) {
			if (line.contains("usageScenario_UsageModel")) {
				return extractUsageModelID(line);
			}
		}
		return null;
	}

	private static String extractUsageModelID(String line) {
		String id;
		id = line.replaceAll(".*id=\"", "");
		id = id.replaceAll("\".*", "");
		return id;
	}

	private static List<String> generateFileData(String usageModelFileName, String usageModelID, String objectiveID) {
		List<String> lines = new ArrayList<String>();

		lines.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		lines.add(
				"<QMLDeclarations:QMLDeclarations xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:QMLContract=\"http:///QMLContract.ecore\" xmlns:QMLDeclarations=\"http:///QMLDeclarations.ecore\" xmlns:QMLProfile=\"http:///QMLProfile.ecore\" id=\"__FjEYJyKEd-NNcIT2kpSGQ3\">");
		lines.add(
				"<qmlDeclarations xsi:type=\"QMLProfile:SimpleQMLProfile\" id=\"__Gb1MJyKEd-NNcIT2kpGSQ\" entityName=\"PCMProfile\">");
		lines.add("<usageModel href=\"" + usageModelFileName + "#/\"/>");
		lines.add(
				"<requirements xsi:type=\"QMLProfile:UsageScenarioRequirement\" id=\"__IEz85yKEd-NNcIT2kpGQS\" requireContract=\"_FqNZYaCrEd-s9uTaURbrKAz\">");
		lines.add("<usageScenario href=\"" + usageModelFileName + "#" + usageModelID + "\"/>");
		lines.add("</requirements>");
		lines.add("</qmlDeclarations>");
		lines.add(
				"<qmlDeclarations xsi:type=\"QMLContract:SimpleQMLContract\" id=\"_FqNZYaCrEd-s9uTaURbrKAz\" entityName=\"PCMStandardQMLContract2\">");
		lines.add("<criteria xsi:type=\"QMLContract:Objective\" id=\"_FqNZZqCrEd-s9uTaURbrKAz\">");
		lines.add("<dimension href=\"pathmap://PCM_MODELS/" + objectiveID + "\"/>");
		lines.add("<aspects xsi:type=\"QMLContract:Mean\" id=\"_FqNZa6CrEd-s9uTaURbrKAz\"/>");
		lines.add("</criteria>");
		lines.add(
				"<contractType href=\"pathmap://PCM_MODELS/PCMStandardQMLContractType.qmldeclarations#_r_iViKCrEd-s9uTaURbrKA\"/>");
		lines.add("</qmlDeclarations>");
		lines.add("</QMLDeclarations:QMLDeclarations>");

		return lines;
	}

}

package io.github.squat_team.agentsUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Objects;

import org.json.JSONObject;

import io.github.squat_team.model.RestArchitecture;

/**
 * Loads and initializes the initial architecture.
 */
public class ArchitectureInitializer {
	/**
	 * Create a {@link JSONObject} for the given key and file
	 *
	 * @param key
	 *            the key to use for this file
	 * @param file
	 *            the file whose content to add
	 * @return the object or null if file could not be read
	 */
	private static JSONObject create(String key, File file) {
		Objects.requireNonNull(key);
		Objects.requireNonNull(file);
		try {
			JSONObject obj = new JSONObject();
			byte[] fileContent = Files.readAllBytes(file.toPath());
			String encoded = Base64.getEncoder().encodeToString(fileContent);
			obj.put("filename", file.getName());
			obj.put("filecontent", encoded);
			return obj;
		} catch (IOException e) {
			e.printStackTrace(System.err);
		}
		return null;
	}

	/**
	 * Put the file content and name into parent with the specific key if the file
	 * exists and can be read
	 *
	 * @param parent
	 *            the parent to put the values into
	 * @param key
	 *            the key to use
	 * @param file
	 *            the file to be read
	 */
	private static void putIfNotNull(JSONObject parent, String key, File file) {
		JSONObject child = create(key, file);
		if (child != null)
			parent.put(key, child);
	}

	/**
	 * Load the initial architecture
	 *
	 * @param name
	 *            the name of the architecture
	 * @return the created {@link RestArchitecture} instance
	 */
	public static RestArchitecture loadSpecificModel(String name) {
		final String MODEL_NAME = "default";
		// TODO: PA! Replace this with NegotiatorConfiguration variable
		final String MODEL_PATH = "/home/roehrdor/Workspace-oxygen/SQuAT-docker/negotiator/pcm";
		final String ALTERNATIVE_REPOSITORY_PATH = "/home/roehrdor/Workspace-oxygen/SQuAT-docker/negotiator/pcm/alternativeRepository.repository";
		String BASE = MODEL_PATH;
		String basicPath = MODEL_PATH + "/default"; // TODO: PA! Wouldnt it be better to have sth like File.separator +
													// name?

		// Architecture
		JSONObject architecture = new JSONObject();
		architecture.put("name", name);
		putIfNotNull(architecture, "repository", new File(basicPath + ".repository"));
		putIfNotNull(architecture, "system", new File(basicPath + ".system"));
		putIfNotNull(architecture, "allocation", new File(basicPath + ".allocation"));
		putIfNotNull(architecture, "resource-environment", new File(basicPath + ".resourceenvironment"));
		putIfNotNull(architecture, "usage-model", new File(basicPath + ".usagemodel"));
		putIfNotNull(architecture, "repository-with-alternatives",
				new File(BASE + "/" + "alternativeRepository" + ".repository"));

		// Optional architecture part
		JSONObject cost = create("cost", new File("" + basicPath + ".cost"));
		JSONObject insinter = create("insinter-modular", new File("" + BASE + "/insinter-modular.henshin"));
		JSONObject splitrespn = create("splitrespn-modular", new File("" + BASE + "/splitrespn-modular.henshin"));
		JSONObject wrapper = create("wrapper-modular", new File("" + BASE + "/wrapper-modular.henshin"));

		return new RestArchitecture(name, architecture, cost, insinter, splitrespn, wrapper);
	}
}

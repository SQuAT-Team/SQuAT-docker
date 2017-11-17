package io.github.squat_team.performance.peropteryx.overwrite;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.palladiosimulator.solver.lqn.DocumentRoot;
import org.palladiosimulator.solver.lqn.LqnModelType;

/**
 * Copied from {@link #LqnXmlHandler}, but logs errors instead of printing them to the
 * console.
 */
public class MyLqnXmlHandler {

	private static Logger logger = Logger.getLogger(MyLqnXmlHandler.class.getName());

	private MyLqnXmlHandler() {
	}

	/**
	 * Restores the corresponding Ecore model, previously serialized via
	 * {@link #saveModelToXMI(String)}.
	 * 
	 * @param fileName
	 * @return A representation of the model object 'LQN Model Type'; null if
	 *         the file don't exists or when there were problems reading the
	 *         file.
	 */
	public static LqnModelType loadModelFromXMI(String fileName) {

		LqnModelType lqnModel = null;

		try {
			Resource resource = loadIntoResourceSet(fileName);
			DocumentRoot documentRoot = (DocumentRoot) resource.getContents().get(0);
			lqnModel = documentRoot.getLqnModel();

		} catch (IOException e) {
			logger.error(e.getMessage());
		}

		// Can be null if there were problems retrieving the model
		return lqnModel;
	}

	private static Resource loadIntoResourceSet(String fileName) throws IOException {

		URI fileURI = URI.createFileURI(new File(fileName).getAbsolutePath());

		ResourceSet resourceSet = new ResourceSetImpl();

		Resource resource = resourceSet.createResource(fileURI);

		resource.load(Collections.EMPTY_MAP);

		return resource;

	}

	/**
	 * Fix encoding line (fix proposed by Greg Franks for LINE solver, not clear
	 * whether still needed).
	 * 
	 * @param filename
	 */
	public static void fixXMLFile(String filename) {
		String content = readContentFromFile(filename);

		/* Possibly only needed for LINE */
		content = content.replaceAll("xml version=\"1.0\" encoding=\"ASCII\"",
				"xml version=\"1.0\" encoding=\"us-ascii\"");

		writeContentToFile(filename, content);

	}

	private static void writeContentToFile(String filename, String content) {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(filename);
			fos.write(content.getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	private static String readContentFromFile(String filename) {
		FileInputStream fis = null;
		byte b[] = null;
		try {
			fis = new FileInputStream(filename);
			int x = 0;
			x = fis.available();
			b = new byte[x];
			fis.read(b);
			fis.close();
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

		String content = new String(b);
		return content;
	}

}

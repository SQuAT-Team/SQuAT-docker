package io.github.squat_team.json;

import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Function;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

public class JSONUtils {

	/**
	 * 
	 * @param resource
	 * @return
	 */
	public static String fromEResource(Resource resource) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			resource.save(bos, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new String(bos.toByteArray());
	}

	/**
	 * Write the given content to a file on the file system and parse it using the
	 * given function
	 * 
	 * @param content
	 *            the content to be written
	 * @param fn
	 *            the parsing function to call
	 * @return the created object
	 * @throws IOException
	 */
	public static <T> T writeToFileAndLoad(String content, Function<String, T> fn) throws IOException {
		final String filename = "./pcm/" + String.valueOf(java.lang.System.currentTimeMillis())
				+ String.valueOf((long) (Math.random() * Long.MAX_VALUE));
		T ret = null;
		try (FileWriter fw = new FileWriter(filename)) {
			fw.write(content);
		}
		ret = fn.apply(filename);
		// File file = new File(filename);
		// if (file.exists()) {
		// file.delete();
		// }
		return ret;
	}

	/**
	 * Load an {@link EObject} from the given file
	 * 
	 * @param file
	 *            the file to load
	 * @return the created object
	 */
	public static EObject load(String file) {
		URI resourceURI = URI.createURI(file);
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource resource = resourceSet.getResource(resourceURI, true);
		EObject content = resource.getContents().get(0);
		return content;
	}

	/**
	 * Deserialize the given file
	 * 
	 * @param s
	 *            the file to load
	 * @return the created object
	 */
	@SuppressWarnings("unchecked")
	public static <T extends EObject> T loadResource(String s) {
		return (T) load(s);
	}
}

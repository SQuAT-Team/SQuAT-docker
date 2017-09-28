package io.github.squat_team.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryComponent;

import io.github.squat_team.model.PCMArchitectureInstance;

/**
 * Provides methods for merging and separating the components of repositories.
 */
public class PCMRepositoryModifier {
	private PCMArchitectureInstance architecture;

	/**
	 * The IDs of the alternatives in the architecture.
	 */
	private List<String> addedIDs = new ArrayList<>();

	/**
	 * An instance of the Repository Modifier. Searchs for alternative
	 * components in the system model and splits (another or the same)
	 * repository based on the remaining alternative, unsused components.
	 * 
	 * @param architecture
	 *            this should be the parent architecture with alternative
	 *            components.
	 */
	public PCMRepositoryModifier(PCMArchitectureInstance architecture) {
		this.architecture = architecture;
		addedIDs = getIDs(architecture.getRepositoryWithAlternatives());
	}

	private List<String> getIDs(Repository repository) {
		List<String> ids = new ArrayList<String>();

		if (repository != null) {
			EList<RepositoryComponent> components = repository.getComponents__Repository();

			for (RepositoryComponent component : components) {
				ids.add(component.getId());
			}
		}

		return ids;
	}

	private List<String> getComponents(File file) throws IOException {
		List<String> alternativeLines = new ArrayList<String>();
		String alternativeLine = null;

		FileReader alternativeFileReader;
		alternativeFileReader = new FileReader(file);

		BufferedReader alternativeBufferedReader = new BufferedReader(alternativeFileReader);
		boolean isComponentLine = false;
		while ((alternativeLine = alternativeBufferedReader.readLine()) != null) {
			// start of component
			if (alternativeLine.contains("<components__Repository")) {
				isComponentLine = true;
			}
			// in component
			if (isComponentLine) {
				alternativeLines.add(alternativeLine);
			}
			// end of component
			if (alternativeLine.contains("</components__Repository")) {
				isComponentLine = false;
			}
		}

		alternativeFileReader.close();
		alternativeBufferedReader.close();

		return alternativeLines;
	}

	private List<String> getFileAndAddComponents(File file, List<String> components) throws IOException {
		List<String> lines = new ArrayList<String>();
		String line2 = null;

		FileReader fr2 = new FileReader(file);
		BufferedReader br2 = new BufferedReader(fr2);
		boolean written = false;
		while ((line2 = br2.readLine()) != null) {
			// write alternative components before interfaces
			if (!written && line2.contains("<interfaces__Repository")) {
				lines.addAll(components);
				written = true;
			}
			lines.add(line2);
		}
		fr2.close();
		br2.close();

		return lines;
	}

	private void writeToFile(File file, List<String> content) throws IOException {
		FileWriter fw = new FileWriter(file);
		BufferedWriter out = new BufferedWriter(fw);
		for (String s : content) {
			out.write(s);
			out.newLine();
		}
		out.flush();
		out.close();
	}

	/**
	 * Merges all alternative components into the repository.
	 */
	public void mergeRepositories() {
		if (architecture.getRepositoryWithAlternatives() == null) {
			return;
		}
		try {
			Repository mainRepo = architecture.getRepository();
			Repository alternativeRepo = architecture.getRepositoryWithAlternatives();

			File alternativeRepoFile = new File(alternativeRepo.eResource().getURI().toFileString());
			List<String> alternativeComponentsLines = getComponents(alternativeRepoFile);

			// Read in main Repo File
			File mainRepoFile = new File(mainRepo.eResource().getURI().toFileString());
			List<String> mainRepoLines = getFileAndAddComponents(mainRepoFile, alternativeComponentsLines);

			// write to main Repo File
			writeToFile(mainRepoFile, mainRepoLines);

			// reload
			architecture.setRepository(SQuATHelper.loadRepositoryModel(mainRepoFile.toURI().toString()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Splits the repository of the childArchitecture based on the unused
	 * alternative components of the initial architecture.
	 * 
	 * @param childArchitecture
	 *            the alternative repository will be generated for this.
	 */
	public void separateRepository(PCMArchitectureInstance childArchitecture) {
		List<String> unusedIDs = findUnusedAlternativeComponents(childArchitecture);
		try {
			createAlternativeRepository(childArchitecture, unusedIDs);
			updateBaseRepository(childArchitecture, unusedIDs);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createAlternativeRepository(PCMArchitectureInstance childArchitecture, List<String> unusedIDs)
			throws IOException {
		// create file
		File repositoryModelFile = new File(childArchitecture.getRepository().eResource().getURI().toFileString());
		File alternativeRepositoryFile = new File(repositoryModelFile.getParent(), "alternativeRepository.repository");
		Files.copy(repositoryModelFile.toPath(), alternativeRepositoryFile.toPath(),
				StandardCopyOption.REPLACE_EXISTING);

		childArchitecture.setRepositoryWithAlternatives(
				SQuATHelper.loadRepositoryModel(alternativeRepositoryFile.toURI().toString()));

		Repository alternativeRepository = childArchitecture.getRepositoryWithAlternatives();
		EList<RepositoryComponent> components = alternativeRepository.getComponents__Repository();

		RepositoryComponent component = null;
		for (Iterator<RepositoryComponent> iterator = components.iterator(); iterator.hasNext();) {
			component = iterator.next();
			if (!unusedIDs.contains(component.getId())) {
				iterator.remove();
			}
		}

		childArchitecture.saveModel();
	}

	private void updateBaseRepository(PCMArchitectureInstance architecture, List<String> unusedIDs) {
		EList<RepositoryComponent> components = architecture.getRepository().getComponents__Repository();

		RepositoryComponent component = null;
		for (Iterator<RepositoryComponent> iterator = components.iterator(); iterator.hasNext();) {
			component = iterator.next();
			if (unusedIDs.contains(component.getId())) {
				iterator.remove();
			}
		}

		architecture.saveModel();
	}

	private List<String> findUnusedAlternativeComponents(PCMArchitectureInstance childArchitecture) {
		Set<String> usedIDs = new HashSet<String>();
		org.palladiosimulator.pcm.system.System system = childArchitecture.getSystem();
		File systemFile = new File(system.eResource().getURI().toFileString());

		// search lines for ids
		try (BufferedReader br = new BufferedReader(new FileReader(systemFile))) {
			String line = br.readLine();

			while (line != null) {
				for (String id : addedIDs) {
					if (line.contains(id)) {
						usedIDs.add(id);
					}
				}
				line = br.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// create list with unused component ids
		List<String> unusedIDs = new ArrayList<String>();
		for (String id : addedIDs) {
			if (!usedIDs.contains(id)) {
				unusedIDs.add(id);
			}
		}

		return unusedIDs;
	}

}
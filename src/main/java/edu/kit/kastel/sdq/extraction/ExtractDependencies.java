package edu.kit.kastel.sdq.extraction;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ExtractDependencies implements IExtractDependencies {

	private final String manifest = File.separator+"META-INF"+File.separator+"MANIFEST.MF";
	private String[] exclusionCriteria = {"Bundle-ActivationPolicy",
												"Bundle-RequiredExecutionEnvironment", 
												"Export-Package",
												"Bundle-Vendor"};
	private boolean fileEnd;
	private boolean requiresBlockReached;
	
	
	@Override
	public List<String> extractDependenciesFromMetadata(String path) throws FileNotFoundException {
		List<String> dependencies = new ArrayList<>();
		if(isEcipsePluginProject(path)) {
			loadAndReadDependenciesOfFile(path, dependencies);
		}
		return dependencies;
	}


	private void loadAndReadDependenciesOfFile(String path, List<String> dependencies) throws FileNotFoundException {
		path += manifest;
		File manifestFile = new File(path);
		if(manifestFile.canRead()) {
			readDependenciesOfFile(dependencies, manifestFile);
		}
	}


	private void readDependenciesOfFile(List<String> dependencies, File manifestFile) throws FileNotFoundException {
		Scanner s = new Scanner(manifestFile);
		requiresBlockReached = false;
		fileEnd = false;
		while(s.hasNext()) {
			String line = s.nextLine();
			if(line.contains("Require-Bundle:")) {
				requiresBlockReached = true;
				line = line.replace("Require-Bundle: ", "");
			}
			
			extractDependencyFromLine(dependencies, requiresBlockReached, line);
		}
		s.close();
	}


	private boolean extractDependencyFromLine(List<String> dependencies, boolean requiresBlockReached, String line) {
		if(requiresBlockReached) {
			for(String excl : Arrays.asList(exclusionCriteria)) {
				if(line.contains(excl))
					fileEnd = true;
			}
			if(!fileEnd) {
				if(line.contains(";")) {
					line = line.substring(0, line.indexOf(';'));
				}
				dependencies.add(line);
			}
		}
		return fileEnd;
	}

	private boolean isEcipsePluginProject(String path) {
		File f = new File(path + manifest);
		if(f.isFile()) {
			return true;
		} else {
			return false;
		}
	}
}

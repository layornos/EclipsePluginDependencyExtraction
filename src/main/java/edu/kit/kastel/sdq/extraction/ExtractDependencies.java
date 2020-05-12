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
	@Override
	public List<String> extractDependenciesFromMetadata(String path) throws FileNotFoundException {
			List<String> dependencies = new ArrayList<>();
			if(isEcipsePluginProject(path)) {
				path += manifest;
				File f = new File(path);
				if(f.canRead()) {
					Scanner s = new Scanner(f);
					boolean requiresBlockReached = false;
					boolean fileEnd = false;
					while(s.hasNext()) {
						String line = s.nextLine();
						if(line.contains("Require-Bundle:")) {
							requiresBlockReached = true;
							line = line.replace("Require-Bundle: ", "");
						}
						
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
					}
				}
			}
			return dependencies;
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

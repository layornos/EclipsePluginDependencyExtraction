package edu.kit.kastel.sdq.extraction;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public interface IExtractDependencies {
	
	public List<String> extractDependenciesFromMetadata(String path) throws FileNotFoundException;
}

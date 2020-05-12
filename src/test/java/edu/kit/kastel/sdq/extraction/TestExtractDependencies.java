package edu.kit.kastel.sdq.extraction;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.jupiter.api.Test;

class TestExtractDependencies {

	@Test
	void TestManifestExtraction() throws FileNotFoundException {
		ExtractDependencies ed = new ExtractDependencies();
		List<String> l = ed.extractDependenciesFromMetadata("/Users/layornos/Projects/Palladio-Analyzer-SimuLizar/bundles/org.palladiosimulator.simulizar");
		assertEquals(31, l.size());
		
	}

}

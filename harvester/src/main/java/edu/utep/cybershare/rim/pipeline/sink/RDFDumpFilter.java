package edu.utep.cybershare.rim.pipeline.sink;

import java.io.File;

import org.semanticweb.owlapi.io.FileDocumentTarget;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import edu.utep.cybershare.rim.ontology.Factory;
import edu.utep.cybershare.vlc.pipeline.Pipeline.DumpFilter;

public class RDFDumpFilter implements DumpFilter {

	public static final String OWL_ONTOLOGY_BASE_URI = "http://ontology.cybershare.utep.edu/RIM/linked-data/";
	public static final String OWL_FILENAME = "projects.owl";
	public static final String OWL_ONTOLOGY_URI = OWL_ONTOLOGY_BASE_URI + OWL_FILENAME;
	public static final String OWL_FILENAME_FILTERED = "filtered-projects.owl";	
	public static final String DUMP_DIRECTORY = "../ontology.cybershare.utep.edu/RIM/linked-data/";
	
	private File dumpFile;
	
	public RDFDumpFilter(File dumpFile){
		if(dumpFile == null)
			throw new IllegalArgumentException("Specified file is null!");
		
		this.dumpFile = dumpFile;
	}
	
	public void dump (Factory factory) {	//set the tools that the axiom setters will use to populate the ontology
		//convert each project to a set of ontology axioms
		//print out the resulting ontology
		System.out.println("Dumping owl axioms as RDF to: " + dumpFile.getAbsolutePath());
		
		FileDocumentTarget target = new FileDocumentTarget(dumpFile);
		try {
			factory.getOwlOntology().getOWLOntologyManager().saveOntology(factory.getOwlOntology(), target);
		} catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}

package edu.utep.cybershare.rim.pipeline;

import java.io.File;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import edu.utep.cybershare.rim.build.FilePath;
import edu.utep.cybershare.rim.build.Inventory;
import edu.utep.cybershare.rim.ontology.Factory;
import edu.utep.cybershare.rim.pipeline.filter.CollectionsFilter;
import edu.utep.cybershare.rim.pipeline.filter.InstitutionGeocodeFilter;
import edu.utep.cybershare.rim.pipeline.filter.ProjectRelationshipsFilter;
import edu.utep.cybershare.rim.pipeline.filter.ProjectsFilter;
import edu.utep.cybershare.rim.pipeline.sink.RDFDumpFilter;
import edu.utep.cybershare.rim.pipeline.sink.VLCProjectsUploader;
import edu.utep.cybershare.rim.pipeline.source.ProjectSourceFilter;

public class Harvester {

	public static void main(String[] args){
		//create ontology manager
		OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();
		
		//create nsf source filter
		ProjectSourceFilter nsfSourceFilter = new ProjectSourceFilter();
		
		//create rdf dump filter
		RDFDumpFilter rdfDumpFilter = null;
		
		//OWL individual creation factory
		Factory factory = null;
		
		//create VLC upload filter
		//VLCProjectsUploader vlcProjectsUploader = new VLCProjectsUploader();
		try{
			OWLOntology ontology = ontologyManager.createOntology((IRI.create(FilePath.DOCUMENT_URL)));
			factory = new Factory(ontology);
			rdfDumpFilter = new RDFDumpFilter(new File(FilePath.DUMP_PATH));
		}
		catch(Exception e){
			e.printStackTrace();
			System.exit(0);
		}
		
		// create deana filter
		ProjectsFilter deanaFilter = new ProjectsFilter();
		
		// create add to collections filter
		CollectionsFilter collectionsFilter = new CollectionsFilter();
		
		// create project to project filter
		ProjectRelationshipsFilter relationshipsFilter = new ProjectRelationshipsFilter();
		
		// create geocoder filter
		InstitutionGeocodeFilter geocoderFilter = new InstitutionGeocodeFilter();

		Inventory inventory = new Inventory(FilePath.URI_PREFIX);
		Pipeline harvestingPipeline = new Pipeline(nsfSourceFilter, rdfDumpFilter, factory, inventory);
		//Pipeline harvestingPipeline = new Pipeline(nsfSourceFilter, vlcProjectsUploader);
		
		
		harvestingPipeline.addFilter(deanaFilter);
		harvestingPipeline.addFilter(geocoderFilter);
		harvestingPipeline.addFilter(collectionsFilter);
		harvestingPipeline.addFilter(relationshipsFilter);
		
		harvestingPipeline.execute();
	}
}

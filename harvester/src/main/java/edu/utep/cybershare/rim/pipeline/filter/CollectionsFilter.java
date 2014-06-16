package edu.utep.cybershare.rim.pipeline.filter;

import java.util.Collection;
import java.util.List;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import edu.utep.cybershare.rim.ontology.Factory;
import edu.utep.cybershare.rim.ontology.Project;
import edu.utep.cybershare.rim.pipeline.Pipeline.Filter;

public class CollectionsFilter implements Filter {

	// contains collection information as well as
	private Relationships relationships;
	
	private OWLOntology ontology;
	private OWLDataFactory dataFactory;
	private OWLOntologyManager manager;
	
	public CollectionsFilter(){
		relationships = new Relationships(FilterData.getProjectRelationships());
	}
	
	public void filter(Factory factory) {
		dataFactory = factory.getOwlOntology().getOWLOntologyManager().getOWLDataFactory();
		manager = factory.getOwlOntology().getOWLOntologyManager();
		ontology = factory.getOwlOntology();
		
		List<String> collectionIDs;
		List<String> subjects;
		Collection<Project> projects = (Collection<Project>)factory.getAllProjectInstances();
		
		for(Project aProject : projects){
			collectionIDs = relationships.getParentCollections(aProject);
			subjects = relationships.getParentSubjects(aProject);
			
			for(String collectionID : collectionIDs){
				aProject.addIdentifiedByCollectionID(collectionID);
			}
			
			for(String subject: subjects){
				addSubject(aProject, subject);
			}
		}
	}
	
	
	private void addSubject(Project project, String subject){
		OWLDataProperty dcmiSubject = dataFactory.getOWLDataProperty(IRI.create("http://purl.org/dc/terms/subject"));
		OWLLiteral subjectLiteral = dataFactory.getOWLLiteral(subject);
		
		OWLAxiom axiom = dataFactory.getOWLDataPropertyAssertionAxiom(dcmiSubject, project.getOwlIndividual(), subjectLiteral);
		manager.addAxiom(ontology, axiom);
		
	}
}

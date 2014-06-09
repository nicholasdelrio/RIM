package edu.utep.cybershare.rim.pipeline.filter;

import java.util.Collection;
import java.util.List;

import edu.utep.cybershare.rim.ontology.Factory;
import edu.utep.cybershare.rim.ontology.Project;
import edu.utep.cybershare.vlc.pipeline.Pipeline.Filter;

public class CollectionsFilter implements Filter {

	// contains collection information as well as
	private Relationships relationships;
	
	public CollectionsFilter(){
		relationships = new Relationships(FilterData.getProjectRelationships());
	}
	
	public void filter(Factory factory) {
		List<String> collectionIDs;
		Collection<Project> projects = (Collection<Project>)factory.getAllProjectInstances();
		
		for(Project aProject : projects){
			collectionIDs = relationships.getParentCollections(aProject);
			for(String collectionID : collectionIDs)
				aProject.addIdentifiedByCollectionID(collectionID);
		}
	}
}

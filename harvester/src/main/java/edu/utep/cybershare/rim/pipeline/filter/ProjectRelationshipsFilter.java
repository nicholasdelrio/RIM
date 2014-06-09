package edu.utep.cybershare.rim.pipeline.filter;

import java.util.Collection;
import java.util.List;

import edu.utep.cybershare.rim.ontology.Factory;
import edu.utep.cybershare.rim.ontology.Project;
import edu.utep.cybershare.rim.pipeline.Pipeline.Filter;
/**
 * Work in progress...may not be necessary
 * @author Nicholas Del Rio
 *
 */
public class ProjectRelationshipsFilter implements Filter {

	// contains collection information as well as
	private Relationships relationships;
	
	public ProjectRelationshipsFilter(){
		relationships = new Relationships(FilterData.getProjectRelationships());
	}
	
	public void filter(Factory factory) {
		List<String> relatedProjectTitles;
		
		for(Project aProject : (Collection<Project>)factory.getAllProjectInstances()){
			relatedProjectTitles = relationships.getRelatedProjectTitles(aProject);
			for(String aTitle : relatedProjectTitles){
				for(Project anotherProject : (Collection<Project>)factory.getAllPersonInstances()){
					if(aTitle.equals(anotherProject.getIdentifiedByTitle().iterator().next())){
						aProject.addRelatedToResource(anotherProject);
					}
				}
			}
		}
	}
}

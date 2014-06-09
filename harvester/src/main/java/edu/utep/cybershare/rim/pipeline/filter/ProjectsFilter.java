package edu.utep.cybershare.rim.pipeline.filter;

import java.io.File;
import java.io.FileReader;
import java.net.URI;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
import edu.utep.cybershare.rim.ontology.Factory;
import edu.utep.cybershare.rim.ontology.Person;
import edu.utep.cybershare.rim.ontology.Project;
import edu.utep.cybershare.rim.build.source.Awards;
import edu.utep.cybershare.rim.pipeline.Pipeline.Filter;

public class ProjectsFilter implements Filter {
	
	private Hashtable<String, Boolean> projectsOfInterest;
	private Hashtable<String, Boolean> peopleOfInterest;
	
	public ProjectsFilter(){
		populateProjectsOfInterest();
		populatePeopleOfInterest();
	}
	
	public void filter(Factory factory){
		
		factory.getAllPersonInstances();
		
		for(Project aProject : (Collection<Project>)factory.getAllPersonInstances()){
			if(!isTargetProject(aProject))
				factory.getAllPersonInstances().remove(aProject);
		}		
	}
	
	private void populatePeopleOfInterest(){
		peopleOfInterest = new Hashtable<String, Boolean>();
		
		File csvFile = FilterData.getPeople();
		
		try{
			CSVReader reader = new CSVReader(new FileReader(csvFile));
			List<String[]> records = reader.readAll();
		    String firstName;
		    String lastName;
		    String properName;
		    for(String[] record : records){
		    	firstName = record[0];
		    	lastName = record[1];
		    	properName = getProperName(firstName, lastName);
		    	peopleOfInterest.put(properName, new Boolean(true));
		    }
			reader.close();
		}catch(Exception e){e.printStackTrace();}
	}
	
	private static String getProperName(String firstName, String lastName){
		return lastName + "," + firstName;
	}
	
	private void populateProjectsOfInterest(){
		projectsOfInterest = new Hashtable<String, Boolean>();
		
		File csvFile = FilterData.getWaterSustainabilityProjects();
		
		try{
			CSVReader reader = new CSVReader(new FileReader(csvFile));
			List<String[]> records = reader.readAll();
			String projectTitle;
		    for(String[] record : records){
		    	projectTitle = record[1];
		    	projectsOfInterest.put(projectTitle, new Boolean(true));
		    }
			reader.close();
		}catch(Exception e){e.printStackTrace();}

	}
	
	private boolean isTargetProject(Project aProject){
		//get project title
		String title = aProject.getIdentifiedByTitle().iterator().next();
		
		return this.projectsOfInterest.get(title) != null || isTargetProjectViaPeople(aProject) || isFundedByAgency(Awards.getAGENCY_NASA(), aProject);
	}
	
	private boolean isFundedByAgency(URI uri, Project aProject){
		if(aProject.getFundedByAgency().iterator().next().getOwlIndividual().asOWLNamedIndividual().getIRI().toString().equals(uri.toASCIIString()))
			return true;
		return false;
	}
	
	private boolean isTargetProjectViaPeople(Project aProject){
		String firstName;
		String lastName;
		
		boolean isTargetProject = false;
		
		if(aProject.getInvestigatedByPI().iterator().next() == null){
			return false;
		}
		
		//check if pi is target first
		Person pi = aProject.getInvestigatedByPI().iterator().next();
		firstName = pi.getHasFirstName().iterator().next();	
		lastName = pi.getHasLastName().iterator().next();
		isTargetProject |= isTargetPerson(firstName, lastName);
		
		//check co-pis
		Collection<Person> coPIS = (Collection<Person>)aProject.getInvestigatedByCoPI();
		for(Person aPerson : coPIS){
			firstName = aPerson.getHasFirstName().iterator().next();
			lastName = aPerson.getHasLastName().iterator().next();
			isTargetProject |= isTargetPerson(firstName, lastName);
		}
		
		return isTargetProject;
	}
	
	private boolean isTargetPerson(String firstName, String lastName){
		if(peopleOfInterest.get(getProperName(firstName, lastName)) == null)
			return false;
		return true;
	}

}

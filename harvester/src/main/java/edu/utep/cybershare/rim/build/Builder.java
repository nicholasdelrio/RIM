package edu.utep.cybershare.rim.build;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.xml.bind.DatatypeConverter;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import edu.utep.cybershare.rim.ontology.Agency;
import edu.utep.cybershare.rim.ontology.Factory;
import edu.utep.cybershare.rim.ontology.Institution;
import edu.utep.cybershare.rim.ontology.Person;
import edu.utep.cybershare.rim.ontology.Project;
import edu.utep.cybershare.rim.util.StringManipulation;


public abstract class Builder{
	
	private static final String PROJECT_LABEL = "project";
	
	protected ArrayList<Person> coPrincipalInvestigators;
	protected Person principalInvestigator;
	protected Institution hostingInstitution;
	protected ArrayList<Institution> institutions;
	protected Agency fundingAgency;
	
	protected Factory factory;
	protected Inventory inventory;
	protected OWLDataFactory dataFactory;
	
	public Builder(Factory factory, Inventory inventory){
		this.factory = factory;
		this.inventory = inventory;
		OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();
		this.dataFactory = ontologyManager.getOWLDataFactory();
		
		reset();
	}
	
	protected void reset(){
		coPrincipalInvestigators = new ArrayList<Person>();
		institutions = new ArrayList<Institution>();
		principalInvestigator = null;
		hostingInstitution = null;
		fundingAgency = null;
	}
	
	public void buildAgency(URI uri){
		fundingAgency = factory.createAgency(uri.toASCIIString());
	}
		
	public void buildInstitution(String name, String city, String state, String zipCode, String address){
		String uri = inventory.getInstanceURI(name);
		Institution institution = factory.createInstitution(uri);
		institution.addHasCity(city);
		institution.addHasState(state);
		institution.addHasZipCode(zipCode);
		institution.addHasInstitutionName(name);
		this.institutions.add(institution);
	}
	
	public void buildHostingInstitution(String name){
		String uri = inventory.getInstanceURI(name);
		Institution institution = factory.createInstitution(uri);
		institution.addHasInstitutionName(name);
		this.hostingInstitution = institution;
	}
	
	public void buildCoPrincipalInvestigator(String firstName, String lastName, String email){
		this.coPrincipalInvestigators.add(this.getPerson(firstName, lastName, email));
	}
	
	public void buildPrincipalInvestigator(String firstName, String lastName, String email){
		this.principalInvestigator = this.getPerson(firstName, lastName, email);
	}
	
	public void buildProject(
			String title,
			String summary,
			GregorianCalendar startDate,
			GregorianCalendar endDate,
			int awardAmount,
			String grantID,
			URL awardHomepage){
		
		//build project URI
		String id = getProjectID(grantID, title);
		
		String uri = inventory.getInstanceURI(id);
		
		// create the new project 
		Project project = factory.createProject(uri); 
		
		//inventory.addToInventory(project);
		
		//populate the project
		populateProject(project, title, summary, startDate, endDate, awardAmount, grantID, awardHomepage);

		populateWithBuiltParts(project);
		reset();
	}
	
	private String getProjectID(String grantID, String title){
		String projectID;
		if(grantID != null)
			projectID = title + grantID;
		else
			projectID = title;
		
		return projectID + "-" + PROJECT_LABEL;
	}
	
	private void populateProject(
			Project project,
			String title,
			String summary,
			GregorianCalendar startDate,
			GregorianCalendar endDate,
			int awardAmount,
			String grantID,
			URL awardHomepage){
		
		project.addIdentifiedByTitle(title);
		project.addDescribedByAbstract(summary);
		project.addStartedAtDate(getOWLLiteral(startDate));
		project.addEndedAtDate(getOWLLiteral(endDate));
		project.addWasAwardedAmount(awardAmount);
		project.addIdentifiedByGrantID(grantID);
		project.addHasAwardHomePage(awardHomepage.toString());		
	}
	
	protected abstract void populateWithBuiltParts(Project aProject);
	
	private Person getPerson(String firstName, String lastName, String email){		
		String uri = inventory.getInstanceURI(lastName + "-" + firstName);
		Person person = factory.createPerson(uri);
		person.addHasFirstName(firstName);
		person.addHasLastName(lastName);
		
		try{
			String shaSum = StringManipulation.SHAsum(email.getBytes("UTF-8"));
			person.addHasEmailSHA1Sum(shaSum);
		}
		catch(Exception e){/*do nothing, email is either null or corrupted and I can't do anything about that*/}
		
		//set email
		return person;
	}
	
	private OWLLiteral getOWLLiteral(Calendar date){
		OWLDatatype dateTimeType = dataFactory.getOWLDatatype(IRI.create("http://www.w3.org/2001/XMLSchema#dateTime"));
		OWLLiteral dateTimeLiteral = dataFactory.getOWLLiteral(DatatypeConverter.printDateTime(date), dateTimeType);
		return dateTimeLiteral;
	}
}
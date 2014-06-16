package edu.utep.cybershare.rim.build;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.utep.cybershare.rim.build.source.Awards;
import edu.utep.cybershare.rim.build.source.NSFAwards;
import edu.utep.cybershare.rim.util.StringManipulation;

public class NSFDirector {
	
	private NSFBuilder builder;
	private HashMap<String,Boolean> awardSignatures;
	
	public NSFDirector(NSFBuilder builder){
		this.builder = builder;
		awardSignatures = new HashMap<String, Boolean>();
	}
	
	public void construct(NSFAwards awardsXML){
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		Document awardsDoc;
		try{
			dBuilder = dbFactory.newDocumentBuilder();
			for(File awardFile : awardsXML){
				System.out.println("Constructing Projects from file: " + awardFile.getName());
				awardsDoc = dBuilder.parse(awardFile);
				populateProjects(awardsDoc);
				dBuilder.reset();
			}
		}catch(Exception e){e.printStackTrace();}
	}
	
	private void populateProjects(Document awardsDoc){
		NodeList awardsList = awardsDoc.getElementsByTagName("Award");
		
		for (int i = 0; i < awardsList.getLength(); i++) {
			Node awardNode = awardsList.item(i);
			if(!alreadyProcessed(awardNode)){
				populateProject(awardNode);
			}
		}
	}
	
	private boolean alreadyProcessed(Node awardNode){
		String awardText = awardNode.getTextContent();
		String shaSum = StringManipulation.SHAsum(awardText.getBytes());
		Boolean value = this.awardSignatures.get(shaSum);
		if(value == null){
			awardSignatures.put(shaSum, new Boolean(true));
			return false;
		}
		else
			return true;
	}
		
	private void populateProject(Node awardNode){
		Element awardElement = (Element) awardNode;

		// need to call these builder methods in this order
		
		builder.buildAgency(Awards.getAGENCY_NSF());
		
		buildInstitution(awardElement);
		buildPrincipalInvestigator(awardElement);
		buildCoPrincipalInvestigators(awardElement);
		buildProject(awardElement);
	}
	
	private void buildInstitution(Element awardElement){
		String institutionName = awardElement.getElementsByTagName("Organization").item(0).getTextContent();		
		String city = awardElement.getElementsByTagName("OrganizationCity").item(0).getTextContent();		
		String state = awardElement.getElementsByTagName("OrganizationState").item(0).getTextContent();				
		String zipCode = awardElement.getElementsByTagName("OrganizationZip").item(0).getTextContent();
		String address = awardElement.getElementsByTagName("OrganizationStreet").item(0).getTextContent();		

		builder.buildInstitution(institutionName, city, state, zipCode, address);
	}
	
	private void buildPrincipalInvestigator(Element awardElement){
		String piName = getPI(awardElement);
		String piEmail = getEmail(awardElement);
		if(piName != null)
			builder.buildPrincipalInvestigator(NSFAwardsUtils.getFirstName(piName), NSFAwardsUtils.getLastName(piName), piEmail);
	}
	
	private void buildProject(Element awardElement){
		String title = awardElement.getElementsByTagName("Title").item(0).getTextContent();
		String abstractText = awardElement.getElementsByTagName("Abstract").item(0).getTextContent();				
		String startDateString = awardElement.getElementsByTagName("StartDate").item(0).getTextContent();
		String endDateString = awardElement.getElementsByTagName("ExpirationDate").item(0).getTextContent();
		String awardAmountString = awardElement.getElementsByTagName("AwardedAmountToDate").item(0).getTextContent();
		String grantID = awardElement.getElementsByTagName("AwardNumber").item(0).getTextContent();

		int awardAmount = new Integer(awardAmountString);
		URL awardHomepage = NSFAwardsUtils.getAwardHomepageURL(grantID);
		GregorianCalendar startDate = NSFAwardsUtils.getDate(startDateString);
		GregorianCalendar endDate = NSFAwardsUtils.getDate(endDateString);
		
		List<String> programs = this.getProgram(awardElement);

		builder.setPrograms(programs);
		builder.buildProject(title, abstractText, startDate, endDate, awardAmount, grantID, awardHomepage);
	}

	private void buildCoPrincipalInvestigators(Element awardElement){
		List<String> coiNames = getCoInvestigators(awardElement);

		for(String coiName : coiNames){
			builder.buildCoPrincipalInvestigator(NSFAwardsUtils.getFirstName(coiName), NSFAwardsUtils.getLastName(coiName), null);
		}
	}
	
	
	// XML extractor methods
	private List<String> getCoInvestigators(Element awardElement){
		NodeList pis = awardElement.getElementsByTagName("Co-PIName");
		List<String> pisList = NSFAwardsUtils.nodeListConverter(pis);		
		return pisList;
	}
	
	private String getEmail(Element awardElement){
		String email = null;
		NodeList emails = awardElement.getElementsByTagName("PIEmailAddress");
		
		if(emails.getLength() > 0)
			email = emails.item(0).getTextContent();
		
		return email;
	}
	
	private List<String> getProgram(Element awardElement){
		
		ArrayList<String> programs = new ArrayList<String>();
		NodeList programElements = awardElement.getElementsByTagName("Program");

		for(int i = 0; i < programElements.getLength(); i ++)
			programs.add(programElements.item(i).getTextContent());
				
		return programs;
	}
	
	private String getPI(Element awardElement){
		String pi = null;
		NodeList pis = awardElement.getElementsByTagName("PrincipalInvestigator");
		if(pis.getLength() > 0)
			pi = pis.item(0).getTextContent();
		
		return pi;
	}
}
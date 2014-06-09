package edu.utep.cybershare.rim.build;

import java.io.File;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import edu.utep.cybershare.rim.build.source.Awards;
import edu.utep.cybershare.rim.build.source.NASAAwards;


public class NASADirector {
	private static String AWARDS_BASE_URL = "https://earthdata.nasa.gov";
	private NASABuilder builder;
	
	public NASADirector(NASABuilder builder){
		this.builder = builder;
	}
	
	public void construct(NASAAwards awards){
		Document awardDocument = null;
		File awardFile;
		for(int i = 0; i < awards.size(); i ++){
			awardFile = awards.get(i);
			try{
				awardDocument = Jsoup.parse(awardFile, null);
				System.out.println("Constructing Projects from file: " + awardFile.getName());
				populateProject(awardDocument, getCategory(awardFile));
			}
			catch(Exception e){e.printStackTrace();}
		}			
	}
	
	private String getCategory(File awardFile){
		File parentDirectory = new File(awardFile.getParent());
		return parentDirectory.getName();
	}
	
	private String getProjectTitle(Document awardDocument){
		for(Element metaElement : awardDocument.getElementsByTag("meta")){
			String title = metaElement.attributes().get("content").trim();
			String aboutURI = metaElement.attributes().get("about").trim();
			if(title != null && aboutURI != null && !aboutURI.isEmpty()){
				return title;
			}
		}
		return null;
	}
	
	private URL getAwardHomepage(Document awardDocument){
		for(Element metaElement : awardDocument.getElementsByTag("meta")){
			String title = metaElement.attributes().get("content").trim();
			String aboutURI = metaElement.attributes().get("about").trim();
			if(title != null && aboutURI != null && !aboutURI.isEmpty()){
				try{return new URL(AWARDS_BASE_URL + aboutURI);}
				catch(Exception e){e.printStackTrace();}
			}
		}
		return null;
	}
	
	private void populateProject(Document awardDocument, String category){

		String projectTitle = getProjectTitle(awardDocument);
		
		Element content = null;
		try{content = awardDocument.getElementsByClass("field-item").get(0);}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Offending Project that doesn't have field-item class: " + projectTitle);
		}
		
		String abstractText = getAbstractText(content);
		URL awardPageURL = this.getAwardHomepage(awardDocument);
		
		buildPIs(content);
		buildInstitutions(abstractText);
		builder.buildAgency(Awards.getAGENCY_NASA());
		builder.buildProject(projectTitle, abstractText, null, null, 0, null, awardPageURL);		
	}
	
	private String getAbstractText(Element innerDiv){
		String abstractText = "No abstract found";
		for(Element pTags : innerDiv.getElementsByTag("p" )){
			String text = pTags.ownText().trim();
			if(text != null && text.length() > 0){
				abstractText = text;
				break;
			}
		}
		return abstractText;
	}
	
	private void buildInstitutions(String abstractText){
		//the damn institutions are embedded within the abstract text :(
		
		String[] abstractParts = abstractText.split(" - ");
		String piPlusInstitution;
		
		// skip first segment because it is the actual abstract
		for(int i = 1; i < abstractParts.length; i ++){
			piPlusInstitution = abstractParts[i].trim();
			if(piPlusInstitution.startsWith("PI") || piPlusInstitution.startsWith("Co-PI")){
				String[] piAndInstitution = piPlusInstitution.split(",");
				String role = piAndInstitution[0].trim();
				String institution = piAndInstitution[1].trim();
			
				if(role.equals("PI")){
					builder.buildHostingInstitution(institution);
				}
				else if(role.equals("Co-PI")){
					builder.buildInstitution(institution, null, null, null, null);
				}
			}
		}
	}
	
	private void buildPIs(Element content){
		Element pTag = content.getElementsByTag("p").get(0);
		
		String piName;
		String firstName;
		String lastName;
		String email;

		boolean firstPersonEncountered = true;
		for(Element anchor : pTag.getElementsByTag("a")){
			if(anchor.attributes().get("href").startsWith("mailto:") && firstPersonEncountered){
				firstPersonEncountered = false;
				piName = anchor.ownText().trim();
				firstName = NSFAwardsUtils.getFirstName(piName);
				lastName = NSFAwardsUtils.getLastName(piName);
				email = anchor.attributes().get("href").substring(7);
				builder.buildPrincipalInvestigator(firstName, lastName, email);
			}
			else if(anchor.attributes().get("href").startsWith("mailto:") && !firstPersonEncountered){
				piName = anchor.ownText().trim();
				firstName = NSFAwardsUtils.getFirstName(piName);
				lastName = NSFAwardsUtils.getLastName(piName);
				email = anchor.attributes().get("href").substring(7);
				builder.buildCoPrincipalInvestigator(firstName, lastName, email);
			}
		}
	}
}
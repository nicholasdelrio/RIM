package edu.utep.cybershare.rim.build.source;

import java.io.File;
import java.io.FileWriter;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import edu.utep.cybershare.vlc.util.StringManipulation;


public class NASAProjectsDownloader {

	private static int TIMEOUT = 5000;
	private static String BASE_URL = "https://earthdata.nasa.gov";
	private static String ACCESS_PROJECTS = "https://earthdata.nasa.gov/our-community/community-data-system-programs/access-projects";
	
	
	public void download(){
		Document awardsHTML = null;
		try{
			Connection connection = Jsoup.connect(ACCESS_PROJECTS);
			connection.timeout(TIMEOUT);
			awardsHTML = connection.get();
		}
		catch(Exception e){e.printStackTrace();}
		
		Elements categorizations = awardsHTML.getElementsByTag("h3");
		String categoryName;
		Element projectList;
		String link;
		String projectTitle;
		for(Element category : categorizations){
			categoryName = category.ownText();
			
			projectList = category.nextElementSibling();
			
			Elements listElements = projectList.getElementsByTag("li");
						
			for(Element listElement : listElements){
				Element aTag = listElement.children().get(0);
				link = aTag.attributes().get("href");
				projectTitle = aTag.ownText();
				populateProject(categoryName, projectTitle, link);
			}
		}
	}
	
	
	private void populateProject(String category, String projectTitle, String relativeProjectPageLink){
		Document projectHTML = null;
		String projectPageLink = BASE_URL + relativeProjectPageLink;
		System.out.println(projectPageLink);
		try{
			Connection connection = Jsoup.connect(projectPageLink);
			connection.timeout(TIMEOUT);
			projectHTML = connection.get();
			
			String html = projectHTML.html();

			String cleanedCategory = StringManipulation.makeURICompliantFragment(category, "http://test.edu/");
			String cleanedTitle = StringManipulation.makeURICompliantFragment(projectTitle, "http://test.edu/");
			
			File categoryDirectory = new File(Awards.NASA + "/" + cleanedCategory);
			categoryDirectory.mkdir();
			
			File awardFile = new File(categoryDirectory.getAbsoluteFile() + "/" + cleanedTitle + ".html");
			System.out.println("Writing File: " + awardFile.getAbsolutePath());
			
			FileWriter wtr = new FileWriter(awardFile);
			wtr.write(html);
			wtr.close();
		}
		catch(Exception e){e.printStackTrace();}
	}
	
	
	public static void main(String[] args){
		NASAProjectsDownloader dir = new NASAProjectsDownloader();
		dir.download();
	}	
}
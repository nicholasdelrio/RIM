package edu.utep.cybershare.rim.build;

import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.GregorianCalendar;
import java.util.List;

import org.w3c.dom.NodeList;

public class NSFAwardsUtils {
	
	public static String getFirstName(String properName){
		String firstName = null;
		String[] nameParts;
		if(properName != null){
			nameParts = properName.split(" ");
			if(nameParts.length > 1)
				firstName = nameParts[0];
		}
		return firstName;
	}
	
	public static String getLastName(String properName){
		String lastName = null;
		String[] nameParts;
		if(properName != null){
			nameParts = properName.split(" ");
			if(nameParts.length == 2)
				lastName = nameParts[1];
			else if(nameParts.length > 2)
				lastName = nameParts[nameParts.length - 1];
		}
		return lastName;
	}
	
	public static List<String> nodeListConverter(NodeList nodeList){
		ArrayList<String> stringList = new ArrayList<String>();
		for(int i = 0; i < nodeList.getLength(); i ++)
			stringList.add(nodeList.item(i).getTextContent());
		return stringList;
	}
	
	public static URL getAwardHomepageURL(String awardID){
		URL homepageURL = null;
		try{homepageURL = new URL("http://www.nsf.gov/awardsearch/showAward?HistoricalAwards=false&AWD_ID=" + awardID);}
		catch(Exception e){e.printStackTrace();}
		return homepageURL;
	}
	
	public static GregorianCalendar getDate(String stringDate){
		String[] dateParts = stringDate.split("/");
		int month = Integer.valueOf(dateParts[0]);
		int day = Integer.valueOf(dateParts[1]);
		int year = Integer.valueOf(dateParts[2]);
		
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.set(year, month, day);
		return calendar;
	}
}

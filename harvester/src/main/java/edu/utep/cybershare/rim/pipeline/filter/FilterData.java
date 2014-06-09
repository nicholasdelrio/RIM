package edu.utep.cybershare.rim.pipeline.filter;

import java.io.File;

public class FilterData {
	
	private static final String FILTER_DATA_DIR = "./filter-data/";
	
	public static File getGeocodedInstitutions(){
		return new File(FILTER_DATA_DIR + "geocoded-institutions.csv");
	}
	public static File getPeople(){
		return new File(FILTER_DATA_DIR + "people.csv");
	}
	public static File getProjectRelationships(){
		return new File(FILTER_DATA_DIR + "project-relationships.csv");
	}
	public static File getWaterSustainabilityProjects(){
		return new File(FILTER_DATA_DIR + "water-sustainability-projects.csv");
	}
}

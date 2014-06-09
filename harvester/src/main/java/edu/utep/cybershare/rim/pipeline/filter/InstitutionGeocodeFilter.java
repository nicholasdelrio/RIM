package edu.utep.cybershare.rim.pipeline.filter;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
import edu.utep.cybershare.rim.ontology.Factory;
import edu.utep.cybershare.rim.ontology.Institution;
import edu.utep.cybershare.rim.pipeline.Pipeline.Filter;

public class InstitutionGeocodeFilter implements Filter {

	private HashMap<String, Point2D.Double> institutionToCoordinate;

	public InstitutionGeocodeFilter(){
		institutionToCoordinate = new HashMap<String, Point2D.Double>();
		populateMappings(FilterData.getGeocodedInstitutions());
	}
	
	private void populateMappings(File mappingsFile){
		try{
			CSVReader reader = new CSVReader(new FileReader(mappingsFile));
			List<String[]> geocodedRecords = reader.readAll();
		    String[] record;
		    Double lat;
		    Double lon;
		    String name;
		    
		    for(int i = 1; i < geocodedRecords.size(); i ++){
				record = geocodedRecords.get(i);
		    	name = record[4];
				lat = Double.valueOf(record[29]);
		    	lon = Double.valueOf(record[30]);
		    	institutionToCoordinate.put(name, new Point2D.Double(lon, lat));
		    }
			reader.close();
		}catch(Exception e){e.printStackTrace();}
	}
	
	private void setInstitutionCoordinates(Collection<Institution> institutions){
		String name;
		Point2D.Double coordinates;
		for(Institution anInstitution : institutions){
			name = anInstitution.getHasInstitutionName().iterator().next();
			coordinates = getCoordinates(name);
			if(coordinates != null){
				anInstitution.addHasLongitude(coordinates.getX());
				anInstitution.addHasLatitude(coordinates.getY());
			}
		}
	}
	
	private Point2D.Double getCoordinates(String institutionName){
		String cleanedName = institutionName.replaceAll(",", " ");
		Point2D.Double coordinates = institutionToCoordinate.get(cleanedName);
		
		if(coordinates != null)
			return coordinates;
		
		return null;
	}		
	
	public void filter(Factory factory) {
		setInstitutionCoordinates((Collection<Institution>)factory.getAllInstitutionInstances());;
	}
}

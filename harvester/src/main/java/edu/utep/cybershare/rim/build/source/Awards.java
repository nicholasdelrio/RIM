package edu.utep.cybershare.rim.build.source;

import java.io.File;
import java.net.URI;

public class Awards {
	static final File CACHED_AWARD_DATA = new File("./cached-award-data/");
	static final File NSF = new File(CACHED_AWARD_DATA + "/nsf/");
	static final File NASA = new File(CACHED_AWARD_DATA + "/nasa/");
		
	public static URI getAGENCY_NASA(){
		URI nasa = null;
		try{nasa = new URI("http://www.nasa.gov/");}
		catch(Exception e){e.printStackTrace();}
		return nasa;
	}
	
	public static URI getAGENCY_NSF(){
		URI nsf = null;
		try{nsf = new URI("http://www.nsf.gov/");}
		catch(Exception e){e.printStackTrace();}
		return nsf;
	}
}

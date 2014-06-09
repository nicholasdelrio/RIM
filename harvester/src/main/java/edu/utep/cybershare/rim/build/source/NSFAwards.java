package edu.utep.cybershare.rim.build.source;

import java.io.File;
import java.util.ArrayList;

public class NSFAwards extends ArrayList<File> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
			
	public NSFAwards(){
		super();
		populate();
	}
	
	private void populate(){
		for(File cachedAward : Awards.NSF.listFiles())
			add(cachedAward);
	}
}
package edu.utep.cybershare.rim.build.source;

import java.io.File;
import java.util.ArrayList;

public class NASAAwards extends ArrayList<File> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NASAAwards(){
		populate();
	}
	
	private void populate(){
		for(File projectDir : Awards.NASA.listFiles()){
			if(projectDir.isDirectory()){
				for(File awardFile : projectDir.listFiles()){
					add(awardFile);
				}
			}
		}
	}
}

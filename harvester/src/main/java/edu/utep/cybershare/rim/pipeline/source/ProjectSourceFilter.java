package edu.utep.cybershare.rim.pipeline.source;

import edu.utep.cybershare.rim.build.Inventory;
import edu.utep.cybershare.rim.build.NSFBuilder;
import edu.utep.cybershare.rim.build.NSFDirector;
import edu.utep.cybershare.rim.build.source.NASAAwards;
import edu.utep.cybershare.rim.build.source.NSFAwards;
import edu.utep.cybershare.rim.ontology.Factory;
import edu.utep.cybershare.rim.pipeline.Pipeline.SourceFilter;

public class ProjectSourceFilter implements SourceFilter {
	
	private NSFAwards nsfAwards;
	private NASAAwards nasaAwards;
	public ProjectSourceFilter(){
		// hard coded XML NSF awards to process
		nsfAwards = new NSFAwards();
		nasaAwards = new NASAAwards();
	}
	
	public void populate(Factory factory, Inventory inventory){
		
		// instantiate builders
		NSFBuilder nsfBuilder = new NSFBuilder(factory, inventory);
		//NASABuilder nasaBuilder = new NASABuilder(product);

		NSFDirector nsfDirector = new NSFDirector(nsfBuilder);
		nsfDirector.construct(nsfAwards);
		
		//NASADirector nasaDirector = new NASADirector(nasaBuilder);
		//nasaDirector.construct(nasaAwards);		
	}
}

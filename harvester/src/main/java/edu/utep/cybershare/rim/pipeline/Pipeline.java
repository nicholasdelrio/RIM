package edu.utep.cybershare.rim.pipeline;

import java.util.ArrayList;

import edu.utep.cybershare.rim.build.Inventory;
import edu.utep.cybershare.rim.ontology.Factory;

public class Pipeline {
	
	private SourceFilter modelSource;
	private ArrayList<Filter> modelFilters;
	private DumpFilter modelDump;
	private Factory factory;
	private Inventory inventory;
	
	public Pipeline(SourceFilter source, DumpFilter dump, Factory factory, Inventory inventory){
		modelSource = source;
		modelDump = dump;
		this.inventory = inventory;
		this.factory = factory;
		modelFilters = new ArrayList<Filter>();
	}

	public void addFilter(Filter aFilter){
		modelFilters.add(aFilter);
	}
	
	public void execute(){
		System.out.println("Constructing model...");
		modelSource.populate(factory, inventory);

		System.out.println("Applying filters...");
		for(Filter aFilter : modelFilters)
			aFilter.filter(factory);
		
		System.out.println("Dumping model...");
		modelDump.dump(factory);
	}
	
	public static interface SourceFilter{
		public void populate(Factory factory, Inventory inventory);
	}
	
	public static interface DumpFilter {	
		public void dump(Factory factory);
	}
	
	public static interface Filter {
		public void filter(Factory factory);
	}
}

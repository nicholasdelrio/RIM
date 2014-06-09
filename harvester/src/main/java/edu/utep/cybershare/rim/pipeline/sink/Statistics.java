package edu.utep.cybershare.rim.pipeline.sink;

import edu.utep.cybershare.rim.ontology.Factory;
import edu.utep.cybershare.rim.pipeline.Pipeline.DumpFilter;

public class Statistics implements DumpFilter {

	public void dump(Factory factory) {
		int insts = factory.getAllInstitutionInstances().size();
		int agencies = factory.getAllAgencyInstances().size();
		int people = factory.getAllPersonInstances().size();
		int projects = factory.getAllProjectInstances().size();
		
		System.out.println("instituions: " + insts);
		System.out.println("agencies: " + agencies);
		System.out.println("people: " + people);
		System.out.println("projects: " + projects);
	}
}

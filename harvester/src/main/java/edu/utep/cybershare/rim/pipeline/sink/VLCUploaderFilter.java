package edu.utep.cybershare.rim.pipeline.sink;

import java.util.Collection;

import edu.utep.cybershare.rim.ontology.Factory;
import edu.utep.cybershare.rim.ontology.Project;
import edu.utep.cybershare.rim.pipeline.Pipeline.DumpFilter;

public class VLCUploaderFilter implements DumpFilter {

	public void dump(Factory factory) {
		VLCProjectsUploader uploader = new VLCProjectsUploader();
		uploader.setProjects((Collection<Project>)factory.getAllProjectInstances());
		uploader.upload();
	}
}

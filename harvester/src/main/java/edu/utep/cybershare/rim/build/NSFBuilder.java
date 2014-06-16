package edu.utep.cybershare.rim.build;

import java.util.List;

import edu.utep.cybershare.rim.ontology.Factory;
import edu.utep.cybershare.rim.ontology.Person;
import edu.utep.cybershare.rim.ontology.Project;


public class NSFBuilder extends Builder {

	private List<String> programs;
	
	public NSFBuilder(Factory factory, Inventory inventory) {
		super(factory, inventory);
		// TODO Auto-generated constructor stub
	}
	
	public void setPrograms(List<String> programs){
		this.programs = programs;
	}
	
	
	@Override
	protected void reset(){
		super.reset();
		programs = null;
	}

	@Override
	protected void populateWithBuiltParts(Project project) {
		//add programs
		for(String program : programs)
			project.addAssociatedWithAgencyProgram(program);
		
		fundingAgency.addFundsProject(project);
		project.addFundedByAgency(fundingAgency);
				
		if(this.principalInvestigator != null){
			project.addInvestigatedByPI(this.principalInvestigator);
			project.addHostedByInstitution(institutions.get(0));
			this.principalInvestigator.addAffiliatedWithInstitution(institutions.get(0));			
		}
		
		for(Person coPrincipalInvestigator : this.coPrincipalInvestigators)
			project.addInvestigatedByCoPI(coPrincipalInvestigator);
	}
}
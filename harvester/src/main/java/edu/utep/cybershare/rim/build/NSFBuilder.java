package edu.utep.cybershare.rim.build;

import edu.utep.cybershare.rim.ontology.Factory;
import edu.utep.cybershare.rim.ontology.Person;
import edu.utep.cybershare.rim.ontology.Project;


public class NSFBuilder extends Builder {

	public NSFBuilder(Factory factory, Inventory inventory) {
		super(factory, inventory);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void populateWithBuiltParts(Project project) {
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
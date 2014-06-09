package edu.utep.cybershare.rim.build;

import edu.utep.cybershare.rim.ontology.Factory;
import edu.utep.cybershare.rim.ontology.Person;
import edu.utep.cybershare.rim.ontology.Project;
import edu.utep.cybershare.rim.ontology.Institution;

public class NASABuilder extends Builder {

	public NASABuilder(Factory factory, Inventory inventory) {
		super(factory, inventory);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void populateWithBuiltParts(Project project){
		fundingAgency.addFundsProject(project);
	
		if(this.principalInvestigator != null){
			project.addInvestigatedByPI(this.principalInvestigator);
			project.addHostedByInstitution(this.hostingInstitution);
		}
		
		for(int i = 0; i < coPrincipalInvestigators.size(); i ++){
			Person aPerson = coPrincipalInvestigators.get(i);			
			project.addInvestigatedByCoPI(aPerson);
			
			// add affiliated institution to co pi
			Institution anInstitution = institutions.get(i);			
			aPerson.addAffiliatedWithInstitution(anInstitution);
		}
	}
}

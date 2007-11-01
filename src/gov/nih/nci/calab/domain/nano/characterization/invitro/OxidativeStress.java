package gov.nih.nci.calab.domain.nano.characterization.invitro;

import gov.nih.nci.calab.domain.nano.characterization.toxicity.Toxicity;

public class OxidativeStress extends Toxicity {

	private static final long serialVersionUID = 1234567890L;

	public OxidativeStress() {
		
		
	}
	
	public String getClassification() {
		return INVITRO_CHARACTERIZATION;
	}
	public String getName() {
		return TOXICITY_OXIDATIVE_STRESS;
	}
}

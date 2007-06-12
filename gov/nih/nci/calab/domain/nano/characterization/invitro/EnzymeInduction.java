package gov.nih.nci.calab.domain.nano.characterization.invitro;

import gov.nih.nci.calab.domain.nano.characterization.toxicity.Toxicity;

public class EnzymeInduction extends Toxicity {

	private static final long serialVersionUID = 1234567890L;

	public EnzymeInduction() {
		super();
	}

	public String getName() {
		return TOXICITY_ENZYME_INDUCTION;
	}
}

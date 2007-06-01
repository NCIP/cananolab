package gov.nih.nci.calab.domain.nano.characterization.toxicity;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;

public class Toxicity extends Characterization {

	private static final long serialVersionUID = 1234567890L;

	public String getClassification() {
		return TOXICITY_CHARACTERIZATION;
	}

	public String getName() {
		return TOXICITY;
	}

}

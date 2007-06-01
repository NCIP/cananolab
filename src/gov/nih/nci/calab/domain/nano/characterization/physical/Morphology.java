package gov.nih.nci.calab.domain.nano.characterization.physical;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;

public class Morphology extends Characterization {

	private static final long serialVersionUID = 1234567890L;

	private String type;

	public Morphology() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getClassification() {
		return PHYSICAL_CHARACTERIZATION;
	}

	public String getName() {
		return PHYSICAL_MORPHOLOGY;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}

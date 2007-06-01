package gov.nih.nci.calab.domain.nano.characterization.physical;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;

public class Size extends Characterization {

	private static final long serialVersionUID = 1234567890L;

	public Size() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public String getClassification() {
		return PHYSICAL_CHARACTERIZATION;
	}

	public String getName() {
		return PHYSICAL_SIZE;
	}
}

package gov.nih.nci.calab.domain.nano.characterization.invitro;

import gov.nih.nci.calab.domain.nano.characterization.toxicity.Cytotoxicity;

public class Caspase3Activation extends Cytotoxicity {
	private static final long serialVersionUID = 1234567890L;

	public Caspase3Activation() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getClassification() {
		return INVITRO_CHARACTERIZATION;
	}

	public String getName() {
		return CYTOTOXICITY_CASPASE3_ACTIVIATION;
	}

	public String getCellDeathMethod() {
		return NECROSIS_CELL_DEATH_METHOD_CYTOXICITY_CHARACTERIZATION;
	}
}

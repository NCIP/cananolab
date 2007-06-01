package gov.nih.nci.calab.domain.nano.characterization.invitro;

import gov.nih.nci.calab.domain.nano.characterization.toxicity.Cytotoxicity;

public class CellViability extends Cytotoxicity {
	private static final long serialVersionUID = 1234567890L;

	public CellViability() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getClassification() {
		return INVITRO_CHARACTERIZATION;
	}

	public String getName() {
		return CYTOTOXICITY_CELL_VIABILITY;
	}

	public String getCellDeathMethod() {
		return APOPTOSIS_CELL_DEATH_METHOD_CYTOXICITY_CHARACTERIZATION;		
	}
}

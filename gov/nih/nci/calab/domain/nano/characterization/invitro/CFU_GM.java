package gov.nih.nci.calab.domain.nano.characterization.invitro;

import gov.nih.nci.calab.domain.nano.characterization.toxicity.ImmunoToxicity;

public class CFU_GM extends ImmunoToxicity {
	private static final long serialVersionUID = 1234567890L;

	public CFU_GM() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getImmunotoxiticyType() {
		return IMMUNE_CELL_FUNCTION_IMMUNOTOXICITY_CHARACTERIZATION;
	}

	public String getClassification() {
		return INVITRO_CHARACTERIZATION;
	}

	public String getName() {
		return IMMUNOCELLFUNCTOX_CFU_GM;
	}
}

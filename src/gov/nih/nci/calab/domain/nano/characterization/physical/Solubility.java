package gov.nih.nci.calab.domain.nano.characterization.physical;

import gov.nih.nci.calab.domain.Measurement;
import gov.nih.nci.calab.domain.nano.characterization.Characterization;

public class Solubility extends Characterization {

	private static final long serialVersionUID = 1234567890L;

	private String solvent;

	private Measurement criticalConcentration;

	private Boolean isSoluble;

	public Solubility() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return PHYSICAL_SOLUBILITY;
	}


	public Measurement getCriticalConcentration() {
		return criticalConcentration;
	}

	public void setCriticalConcentration(Measurement criticalConcentration) {
		this.criticalConcentration = criticalConcentration;
	}

	public Boolean getIsSoluble() {
		return isSoluble;
	}

	public void setIsSoluble(Boolean isSoluble) {
		this.isSoluble = isSoluble;
	}

	public String getSolvent() {
		return solvent;
	}

	public void setSolvent(String solvent) {
		this.solvent = solvent;
	}
}

package gov.nih.nci.calab.domain.nano.characterization.physical;

import gov.nih.nci.calab.domain.Measurement;
import gov.nih.nci.calab.domain.nano.characterization.Characterization;

public class Solubility extends Characterization {

	private static final long serialVersionUID = 1234567890L;

	private String solvent;

	private Measurement criticalConcentration;

	private Boolean isSoluble;

	public Solubility() {

	}

	public String getName() {
		return PHYSICAL_SOLUBILITY;
	}

	public String getClassification() {
		return PHYSICAL_CHARACTERIZATION;
	}
	
	public Measurement getCriticalConcentration() {
		return this.criticalConcentration;
	}

	public void setCriticalConcentration(Measurement criticalConcentration) {
		this.criticalConcentration = criticalConcentration;
	}

	public Boolean getIsSoluble() {
		return this.isSoluble;
	}

	public void setIsSoluble(Boolean isSoluble) {
		this.isSoluble = isSoluble;
	}

	public String getSolvent() {
		return this.solvent;
	}

	public void setSolvent(String solvent) {
		this.solvent = solvent;
	}
}

package gov.nih.nci.calab.domain.nano.characterization.physical;

import gov.nih.nci.calab.domain.Measurement;
import gov.nih.nci.calab.domain.nano.characterization.Characterization;

import java.util.ArrayList;
import java.util.Collection;

public class Surface extends Characterization {

	private static final long serialVersionUID = 1234567890L;

	private Measurement surfaceArea;

	private Measurement surfaceCharge;

	// private Measurement zetaPotential;
	private Float zetaPotential;

	private Measurement charge;

	private Boolean isHydrophobic;

	private Collection<SurfaceChemistry> surfaceChemistryCollection = new ArrayList<SurfaceChemistry>();

	public Surface() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getClassification() {
		return PHYSICAL_CHARACTERIZATION;
	}


	public String getName() {
		return PHYSICAL_SURFACE;
	}

	public Measurement getCharge() {
		return charge;
	}

	public void setCharge(Measurement charge) {
		this.charge = charge;
	}

	public Boolean getIsHydrophobic() {
		return isHydrophobic;
	}

	public void setIsHydrophobic(Boolean isHydrophobic) {
		this.isHydrophobic = isHydrophobic;
	}

	public Measurement getSurfaceArea() {
		return surfaceArea;
	}

	public void setSurfaceArea(Measurement surfaceArrea) {
		this.surfaceArea = surfaceArrea;
	}

	public Measurement getSurfaceCharge() {
		return surfaceCharge;
	}

	public void setSurfaceCharge(Measurement surfaceCharge) {
		this.surfaceCharge = surfaceCharge;
	}

	/*
	 * public Measurement getZetaPotential() { return zetaPotential; }
	 * 
	 * public void setZetaPotential(Measurement zetaPotential) {
	 * this.zetaPotential = zetaPotential; }
	 */
	public Collection<SurfaceChemistry> getSurfaceChemistryCollection() {
		return surfaceChemistryCollection;
	}

	public void setSurfaceChemistryCollection(
			Collection<SurfaceChemistry> surfaceChemistryCollection) {
		this.surfaceChemistryCollection = surfaceChemistryCollection;
	}

	public Float getZetaPotential() {
		return zetaPotential;
	}

	public void setZetaPotential(Float zetaPotential) {
		this.zetaPotential = zetaPotential;
	}
}

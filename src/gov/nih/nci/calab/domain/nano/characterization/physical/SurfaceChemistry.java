package gov.nih.nci.calab.domain.nano.characterization.physical;

import gov.nih.nci.calab.domain.Measurement;

public class SurfaceChemistry {

	private String molecule;
	private Measurement density;
	
	public SurfaceChemistry() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Measurement getDensity() {
		return density;
	}

	public void setDensity(Measurement density) {
		this.density = density;
	}

	public String getMolecule() {
		return molecule;
	}

	public void setMolecule(String molecule) {
		this.molecule = molecule;
	}

	
}

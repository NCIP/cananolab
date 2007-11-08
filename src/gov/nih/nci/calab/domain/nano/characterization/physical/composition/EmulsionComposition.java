package gov.nih.nci.calab.domain.nano.characterization.physical.composition;

public class EmulsionComposition extends ParticleComposition {
	private static final long serialVersionUID = 1234567890L;

	private String type;

	private String molecularFormula;

	private Boolean polymerized;

	private String polymerName;

	public EmulsionComposition() {

	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMolecularFormula() {
		return this.molecularFormula;
	}

	public void setMolecularFormula(String molecularFormula) {
		this.molecularFormula = molecularFormula;
	}

	public String getPolymerName() {
		return this.polymerName;
	}

	public void setPolymerName(String polymerName) {
		this.polymerName = polymerName;
	}

	public String getClassification() {
		return PHYSICAL_CHARACTERIZATION;
	}

	public String getName() {
		return PHYSICAL_COMPOSITION;
	}

	public Boolean getPolymerized() {
		return this.polymerized;
	}

	public void setPolymerized(Boolean polymerized) {
		this.polymerized = polymerized;
	}
}

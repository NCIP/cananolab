package gov.nih.nci.calab.domain.nano.characterization.physical;

public class SurfaceChemistry implements java.io.Serializable {

	private static final long serialVersionUID = 1234567890L;

	private Long id;

	private String molecularFormulaType;

	private String moleculeName;

	private Integer numberOfMolecule;

	public SurfaceChemistry() {

	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMoleculeName() {
		return this.moleculeName;
	}

	public void setMoleculeName(String molecule) {
		this.moleculeName = molecule;
	}

	public Integer getNumberOfMolecule() {
		return this.numberOfMolecule;
	}

	public void setNumberOfMolecule(Integer numberOfMolecule) {
		this.numberOfMolecule = numberOfMolecule;
	}

	public String getMolecularFormulaType() {
		return this.molecularFormulaType;
	}

	public void setMolecularFormulaType(String molecularFormulaType) {
		this.molecularFormulaType = molecularFormulaType;
	}
}

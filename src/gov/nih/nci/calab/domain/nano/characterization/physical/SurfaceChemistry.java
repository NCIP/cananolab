package gov.nih.nci.calab.domain.nano.characterization.physical;

public class SurfaceChemistry {

	private Long id;

	private String moleculeName;

	private Integer numberOfMolecule;

	public SurfaceChemistry() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMoleculeName() {
		return moleculeName;
	}

	public void setMoleculeName(String molecule) {
		this.moleculeName = molecule;
	}

	public Integer getNumberOfMolecule() {
		return numberOfMolecule;
	}

	public void setNumberOfMolecule(Integer numberOfMolecule) {
		this.numberOfMolecule = numberOfMolecule;
	}
}

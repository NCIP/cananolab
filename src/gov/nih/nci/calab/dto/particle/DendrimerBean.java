package gov.nih.nci.calab.dto.particle;

public class DendrimerBean extends ParticleBean {
	private String core;

	private String branch;

	private String repeatUnit;

	private String generation;
	
	private String numberOfSurfaceGroups;
	
	private String molecularFormula;

	public DendrimerBean() {		
	}
	
	public String getBranch() {
		return branch;
	}

	public String getCore() {
		return core;
	}

	public String getGeneration() {
		return generation;
	}

	public String getRepeatUnit() {
		return repeatUnit;
	}

	public String getNumberOfSurfaceGroups() {
		return numberOfSurfaceGroups;
	}

	public String getMolecularFormula() {
		return molecularFormula;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public void setCore(String core) {
		this.core = core;
	}

	public void setGeneration(String generation) {
		this.generation = generation;
	}

	public void setMolecularFormula(String molecularFormula) {
		this.molecularFormula = molecularFormula;
	}

	public void setNumberOfSurfaceGroups(String numberOfSurfaceGroups) {
		this.numberOfSurfaceGroups = numberOfSurfaceGroups;
	}

	public void setRepeatUnit(String repeatUnit) {
		this.repeatUnit = repeatUnit;
	}
}

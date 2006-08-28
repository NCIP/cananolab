package gov.nih.nci.calab.dto.characterization.composition;

public class EmulsionBean extends CompositionBean {
	private String emulsionType;
	private String molecularFormula;
	private String dropletSize;
	private String polymerized;
	private String polymerName;
	
	public EmulsionBean() {
		super();
	}
	
	public String getDropletSize() {
		return dropletSize;
	}
	public void setDropletSize(String dropletSize) {
		this.dropletSize = dropletSize;
	}
	public String getEmulsionType() {
		return emulsionType;
	}
	public void setEmulsionType(String emulsionType) {
		this.emulsionType = emulsionType;
	}
	public String getMolecularFormula() {
		return molecularFormula;
	}
	public void setMolecularFormula(String molecularFormula) {
		this.molecularFormula = molecularFormula;
	}
	public String getPolymerized() {
		return polymerized;
	}
	public void setPolymerized(String polymerized) {
		this.polymerized = polymerized;
	}
	public String getPolymerName() {
		return polymerName;
	}
	public void setPolymerName(String polymerName) {
		this.polymerName = polymerName;
	}
	
}

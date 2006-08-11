package gov.nih.nci.calab.dto.particle;

public class LiposomeBean extends ParticleBean {
	private String polymerized;
	private String numberOfComponents;

	public LiposomeBean() {
	}

	public String getNumberOfComponents() {
		return numberOfComponents;
	}

	public String getPolymerized() {
		return polymerized;
	}

	public void setNumberOfComponents(String numberOfComponents) {
		this.numberOfComponents = numberOfComponents;
	}

	public void setPolymerized(String polymerized) {
		this.polymerized = polymerized;
	}
	
}

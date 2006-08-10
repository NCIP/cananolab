package gov.nih.nci.calab.dto.particle;

public class DendrimerBean extends ParticleBean {
	private String core;

	private String branch;

	private String repeatUnit;

	private String generation;
	
	private String numberOfSurfaceGroups;

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
}

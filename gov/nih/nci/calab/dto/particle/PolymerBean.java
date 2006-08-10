package gov.nih.nci.calab.dto.particle;

public class PolymerBean extends ParticleBean {
	private String crosslinked;

	private String crosslinkDegree;

	private String initiator;
	
	private String numberOfMonomers;

	public PolymerBean() {
	}

	public String getCrosslinkDegree() {
		return crosslinkDegree;
	}

	public String getCrosslinked() {
		return crosslinked;
	}

	public String getInitiator() {
		return initiator;
	}

	public String getNumberOfMonomers() {
		return numberOfMonomers;
	}

}

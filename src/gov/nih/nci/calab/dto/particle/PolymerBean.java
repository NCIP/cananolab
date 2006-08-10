package gov.nih.nci.calab.dto.particle;

public class PolymerBean extends ParticleBean {
	private String crosslinked;

	private String crosslinkDegree;

	private String initiator;
	
	private String numberOfMonomers;

	public PolymerBean() {
	}

	public String getCrossLinkDegree() {
		return crosslinkDegree;
	}

	public String getCrossLinked() {
		return crosslinked;
	}

	public String getInitiator() {
		return initiator;
	}

	public String getNumberOfMonomers() {
		return numberOfMonomers;
	}

}

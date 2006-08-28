package gov.nih.nci.calab.dto.characterization.composition;


public class PolymerBean extends CompositionBean {
	private String crosslinked;

	private String crosslinkDegree;

	private String initiator;

	public PolymerBean() {
		super();				
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

	public void setCrosslinkDegree(String crosslinkDegree) {
		this.crosslinkDegree = crosslinkDegree;
	}

	public void setCrosslinked(String crosslinked) {
		this.crosslinked = crosslinked;
	}

	public void setInitiator(String initiator) {
		this.initiator = initiator;
	}
}

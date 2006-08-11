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

	public void setCrosslinkDegree(String crosslinkDegree) {
		this.crosslinkDegree = crosslinkDegree;
	}

	public void setCrosslinked(String crosslinked) {
		this.crosslinked = crosslinked;
	}

	public void setInitiator(String initiator) {
		this.initiator = initiator;
	}

	public void setNumberOfMonomers(String numberOfMonomers) {
		this.numberOfMonomers = numberOfMonomers;
	}

}

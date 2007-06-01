package gov.nih.nci.calab.domain.nano.characterization.physical.composition;

public class PolymerComposition extends ParticleComposition {

	private static final long serialVersionUID = 1234567890L;

	private Boolean crossLinked;

	private Float crossLinkDegree;

	private String initiator;

	public PolymerComposition() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Float getCrossLinkDegree() {
		return crossLinkDegree;
	}

	public void setCrossLinkDegree(Float crossLinkDegree) {
		this.crossLinkDegree = crossLinkDegree;
	}

	public String getInitiator() {
		return initiator;
	}

	public void setInitiator(String initiator) {
		this.initiator = initiator;
	}

	public String getClassification() {
		return PHYSICAL_CHARACTERIZATION;
	}

	public String getName() {
		return PHYSICAL_COMPOSITION;
	}

	public Boolean getCrossLinked() {
		return crossLinked;
	}

	public void setCrossLinked(Boolean crossLinked) {
		this.crossLinked = crossLinked;
	}

}

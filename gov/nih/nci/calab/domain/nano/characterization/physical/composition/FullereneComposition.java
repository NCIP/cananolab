package gov.nih.nci.calab.domain.nano.characterization.physical.composition;


public class FullereneComposition extends ParticleComposition {

	private static final long serialVersionUID = 1234567890L;


	private Integer numberOfCarbon;

	public FullereneComposition() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getNumberOfCarbon() {
		return numberOfCarbon;
	}

	public void setNumberOfCarbon(Integer numberOfCarbon) {
		this.numberOfCarbon = numberOfCarbon;
	}

	public String getClassification() {
		return PHYSICAL_CHARACTERIZATION;
	}

	public String getName() {
		return PHYSICAL_COMPOSITION;
	}
}

package gov.nih.nci.calab.domain.nano.characterization.physical.composition;


public class ComplexComposition extends ParticleComposition {

	private static final long serialVersionUID = 1234567890L;
	public ComplexComposition() {
	}

	public String getName() {
		return PHYSICAL_COMPOSITION;
	}
	public String getClassification() {
		return PHYSICAL_CHARACTERIZATION;
	}
}

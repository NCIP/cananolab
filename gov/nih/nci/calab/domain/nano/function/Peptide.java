package gov.nih.nci.calab.domain.nano.function;

public class Peptide extends Agent {
	private static final long serialVersionUID = 1234567890L;

	private String sequence;

	public Peptide() {

	}

	public String getSequence() {
		return this.sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
}

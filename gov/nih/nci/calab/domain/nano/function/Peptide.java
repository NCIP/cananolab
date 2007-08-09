package gov.nih.nci.calab.domain.nano.function;

import java.util.ArrayList;
import java.util.Collection;

public class Peptide extends Agent {
	private static final long serialVersionUID = 1234567890L;

	private String sequence;

	public Peptide() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
}

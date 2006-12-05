package gov.nih.nci.calab.dto.function;

import gov.nih.nci.calab.domain.nano.function.DNA;

/**
 * This class represents properties of DNA to be shown the function view pages.
 * 
 * @author pansu
 * 
 */
public class DNABean extends AgentBean {
	private String sequence;

	public DNABean() {
		super();
	}

	public DNABean(DNA dna) {
		super(dna);
		this.sequence = dna.getSequence();
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	
	public DNA getDomainObj() {
		DNA dna = new DNA();
		super.updateDomainObj(dna);
		dna.setSequence(sequence);
		return dna;
	}
}

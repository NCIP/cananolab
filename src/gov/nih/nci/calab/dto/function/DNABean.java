package gov.nih.nci.calab.dto.function;

import gov.nih.nci.calab.domain.nano.function.DNA;

/**
 * This class represents properties of DNA to be shown the function view pages.
 * 
 * @author pansu
 * 
 */
public class DNABean extends BaseAgentBean {
	private String sequence;

	public DNABean() {		
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
	
	public void updateDomainObj(DNA dna) {		
		// super has been called in AgentBean level, so no need to call again here.
		//		super.updateDomainObj(dna);
		dna.setSequence(sequence);		
	}
}

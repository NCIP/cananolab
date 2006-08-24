package gov.nih.nci.calab.dto.characterization.composition;

import gov.nih.nci.calab.dto.characterization.CharacterizationBean;

public class BuckeyballBean extends CharacterizationBean {
	private String structure;
	
	public BuckeyballBean(){
	}

	public String getStructure() {
		return structure;
	}

	public void setStructure(String structure) {
		this.structure = structure;
	}
	
}

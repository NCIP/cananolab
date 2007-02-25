package gov.nih.nci.calab.domain.nano.characterization.toxicity;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;

public class Cytotoxicity extends Toxicity {
private static final long serialVersionUID = 1234567890L;
	
	private String cellLine;
	private String cellDeathMethod;
	
	public String getCellDeathMethod() {
		return this.cellDeathMethod;
	}

	public void setCellDeathMethod(String cellDeathMethod) {
		this.cellDeathMethod = cellDeathMethod;
	}

	public String getCellLine() {
		return cellLine;
	}

	public void setCellLine(String cellLine) {
		this.cellLine = cellLine;
	}

}

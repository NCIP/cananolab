package gov.nih.nci.calab.domain.nano.characterization.toxicity;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;

public interface Cytotoxicity extends Characterization {
	public String getCellLine();
	public void setCellLine(String cellLine);
	public String getCellDeathMethod();
	public void setCellDeathMethod(String cellDeathMethod);
}

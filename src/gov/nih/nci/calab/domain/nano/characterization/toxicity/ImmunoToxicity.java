package gov.nih.nci.calab.domain.nano.characterization.toxicity;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;

public interface ImmunoToxicity extends Characterization {
	public String getImmunotoxiticyType();
	public void setImmunotoxiticyType(String type);
}

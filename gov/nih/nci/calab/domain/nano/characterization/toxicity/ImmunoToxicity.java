package gov.nih.nci.calab.domain.nano.characterization.toxicity;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;

public class ImmunoToxicity extends Toxicity {

	private static final long serialVersionUID = 1234567890L;
	
	private String immunotoxicityType;
	
	/* (non-Javadoc)
	 * @see gov.nih.nci.calab.domain.nano.characterization.toxicity.ImmunoToxicity#getImmunotoxiticyType()
	 */
	public String getImmunotoxiticyType() {
		return this.immunotoxicityType;
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.calab.domain.nano.characterization.toxicity.ImmunoToxicity#setImmunotoxiticyType(java.lang.String)
	 */
	public void setImmunotoxiticyType(String type) {
		// TODO Auto-generated method stub
		this.immunotoxicityType = type;

	}

}

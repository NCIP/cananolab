package gov.nih.nci.calab.domain.nano.characterization.toxicity;

public class ImmunoToxicity extends Toxicity {

	private static final long serialVersionUID = 1234567890L;

	private String immunotoxicityType;

	public String getImmunotoxiticyType() {
		return this.immunotoxicityType;
	}

	public void setImmunotoxiticyType(String type) {
		// TODO Auto-generated method stub
		this.immunotoxicityType = type;
	}

	public String getImmunotoxicityType() {
		return immunotoxicityType;
	}

	public void setImmunotoxicityType(String immunotoxicityType) {
		this.immunotoxicityType = immunotoxicityType;
	}

	public String getName() {
		return IMMUNOTOXICITY;
	}
}

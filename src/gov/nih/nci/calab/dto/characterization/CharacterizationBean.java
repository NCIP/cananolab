package gov.nih.nci.calab.dto.characterization;

public class CharacterizationBean {
	private String characterizationClassification;
	private String characterizationSource;

	public String getCharacterizationSource() {
		return characterizationSource;
	}

	public void setCharacterizationSource(String characterizationSource) {
		this.characterizationSource = characterizationSource;
	}

	public String getCharacterizationClassification() {
		return characterizationClassification;
	}

	public void setCharacterizationClassification(
			String characterizationClassification) {
		this.characterizationClassification = characterizationClassification;
	}
}

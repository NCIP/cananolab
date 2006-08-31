package gov.nih.nci.calab.dto.characterization;

/**
 * This class represents shared characterization properties to be shown in
 * characterization view pages.
 * 
 * @author pansu
 * 
 */
public class CharacterizationBean {
	private String characterizationClassification;

	private String characterizationSource;

	// used to distinguish different instances of characterizations, which are
	// shown as different links on the view pages.
	private String viewTitle;

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

	public String getViewTitle() {
		return viewTitle;
	}

	public void setViewTitle(String viewTitle) {
		this.viewTitle = viewTitle;
	}


}

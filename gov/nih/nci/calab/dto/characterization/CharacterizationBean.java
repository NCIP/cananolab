package gov.nih.nci.calab.dto.characterization;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;

/**
 * This class represents shared characterization properties to be shown in
 * characterization view pages.
 * 
 * @author pansu
 * 
 */
public abstract class CharacterizationBean {
	private String id;
	
	private String characterizationSource;
	
	// used to distinguish different instances of characterizations, which are
	// shown as different links on the view pages.
	private String viewTitle;
	
	private String description;

	public String getCharacterizationSource() {
		return characterizationSource;
	}

	public void setCharacterizationSource(String characterizationSource) {
		this.characterizationSource = characterizationSource;
	}

	public String getViewTitle() {
		return viewTitle;
	}

	public void setViewTitle(String viewTitle) {
		this.viewTitle = viewTitle;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Get the domain characterization object from the dto bean properties
	 * @return
	 */
	public abstract Characterization getDomainObj();
	
	/**
	 * Get the classification of characterization
	 * @return
	 */
	public abstract String getCharacterizationClassification();

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}

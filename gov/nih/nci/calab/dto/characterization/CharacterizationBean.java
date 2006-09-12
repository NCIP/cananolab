package gov.nih.nci.calab.dto.characterization;

import java.util.Date;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;

/**
 * This class represents shared characterization properties to be shown in
 * characterization view pages.
 * 
 * @author pansu
 * 
 */
public class CharacterizationBean {
	private String id;

	private String characterizationSource;

	// used to distinguish different instances of characterizations, which are
	// shown as different links on the view pages.
	private String viewTitle;

	private String description;

	//not set by application
	private String name; 

	//not set by application
	private String classification;
	
	private String createdBy;
	
	private Date createdDate;

	public CharacterizationBean() {

	}

	public CharacterizationBean(String id, String name, String viewTitle) {
		this.id = id;
		this.name = name;
		this.viewTitle = viewTitle;
	}

	public CharacterizationBean(Characterization characterization) {
		this.setId(characterization.getId().toString());
		this.setViewTitle(characterization.getIdentificationName());
		this.setCharacterizationSource(characterization.getSource());
		this.setCreatedBy(characterization.getCreatedBy());
		this.setCreatedDate(characterization.getCreatedDate());
	}

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
	 * Update the domain characterization object from the dto bean properties
	 * 
	 * @return
	 */
	public void updateDomainObj(Characterization aChar) {
		if (getId() != null && getId().length() > 0) {
			aChar.setId(new Long(getId()));
		}
		aChar.setSource(getCharacterizationSource());
		aChar.setIdentificationName(getViewTitle());
		aChar.setDescription(getDescription());
		aChar.setCreatedBy(getCreatedBy());
		aChar.setCreatedDate(getCreatedDate());
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getClassification() {
		return classification;
	}

	public String getName() {
		return name;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	public Characterization getDomainObj() {
		return null;
	}
}

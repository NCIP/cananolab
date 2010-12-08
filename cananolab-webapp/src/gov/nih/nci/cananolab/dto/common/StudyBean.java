package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.dto.particle.SampleBean;

public class StudyBean {
	
	private String studyName;
	private String studyType;
	private SampleBean studySample;
	private String pointOfContact;
	private String diseaseName;
	private String keywords;
	private String ownerName;
	private Boolean isAnimalStudy;
	
	public String getStudyName() {
		return studyName;
	}
	public void setStudyName(String studyName) {
		this.studyName = studyName;
	}
	public String getStudyType() {
		return studyType;
	}
	public void setStudyType(String studyType) {
		this.studyType = studyType;
	}
	public SampleBean getStudySample() {
		return studySample;
	}
	public void setStudySample(SampleBean studySample) {
		this.studySample = studySample;
	}
	public String getPointOfContact() {
		return pointOfContact;
	}
	public void setPointOfContact(String pointOfContact) {
		this.pointOfContact = pointOfContact;
	}
	public String getDiseaseName() {
		return diseaseName;
	}
	public void setDiseaseName(String diseaseName) {
		this.diseaseName = diseaseName;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public Boolean getIsAnimalStudy() {
		return isAnimalStudy;
	}
	public void setIsAnimalStudy(Boolean isAnimalStudy) {
		this.isAnimalStudy = isAnimalStudy;
	}
}

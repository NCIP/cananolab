package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.dto.particle.SampleBean;

import java.util.Date;

public class StudyBean extends SecuredDataBean {
	
	//private Study study;

	private String name;
	private String title;
	private String type;
	private SampleBean studySample;
	private String pointOfContact;
	private String diseaseNames;
	private String diseaseType;
	private String ownerName;
	private Boolean isAnimalStudy;
	private String designTypes;
	private String outcome;
	private Date startDate;
	private Date endDate;
	private Date publicReleaseDate;
	private Date submissionDate;
	private String description;
	private String factorType;
	private String factorName;
	
	public StudyBean(){
		
	}
	public StudyBean(String id){
		
	}
	

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getPublicReleaseDate() {
		return publicReleaseDate;
	}

	public void setPublicReleaseDate(Date publicReleaseDate) {
		this.publicReleaseDate = publicReleaseDate;
	}

	public String getDiseaseType() {
		return diseaseType;
	}

	public void setDiseaseType(String diseaseType) {
		this.diseaseType = diseaseType;
	}

	public Date getSubmissionDate() {
		return submissionDate;
	}

	public void setSubmissionDate(Date submissionDate) {
		this.submissionDate = submissionDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getDiseaseNames() {
		return diseaseNames;
	}

	public void setDiseaseNames(String diseaseNames) {
		this.diseaseNames = diseaseNames;
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

	public String getDesignTypes() {
		return designTypes;
	}

	public void setDesignTypes(String designTypes) {
		this.designTypes = designTypes;
	}

	public String getOutcome() {
		return outcome;
	}

	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}

	public String getFactorType() {
		return factorType;
	}

	public void setFactorType(String factorType) {
		this.factorType = factorType;
	}

	public String getFactorName() {
		return factorName;
	}

	public void setFactorName(String factorName) {
		this.factorName = factorName;
	}
}

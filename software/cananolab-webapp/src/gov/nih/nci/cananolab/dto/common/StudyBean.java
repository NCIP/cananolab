package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.domain.common.Study;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class StudyBean extends SecuredDataBean {

	private String name;
	private String title;
	private String type;
	private List<SampleBean> studySamples;
	private PointOfContactBean primaryPOCBean = new PointOfContactBean();
	private String diseaseNames;
	private String diseaseType;
	private String ownerName;
	private Boolean isAnimalStudy;
	private String designTypes;
	private String outcome;
	private String description;
	private String factorType;
	private String factorName;
	private Boolean hasPublications = false;
	private Boolean hasSamples = false;
	private Boolean hasProtocols = false;
	private Boolean hasCharacterizations = false;
	private Study domain = new Study();
	private PointOfContactBean thePOC = new PointOfContactBean();
	private List<PointOfContactBean> otherPOCBeans = new ArrayList<PointOfContactBean>();
	private String startDateStr;
	private String endDateStr;
	private String submissionDateStr;
	private String publicReleaseDateStr;

	public Study getDomain() {
		return domain;
	}

	public void setDomain(Study domain) {
		this.domain = domain;
		this.name = domain.getName();
		PointOfContact primaryPOC = domain.getPrimaryPointOfContact();
		this.primaryPOCBean = new PointOfContactBean(primaryPOC);
		this.title = domain.getTitle();
		startDateStr = DateUtils.convertDateToString(domain.getStartDate(),
				Constants.DATE_FORMAT);
		endDateStr = DateUtils.convertDateToString(domain.getEndDate(),
				Constants.DATE_FORMAT);
		submissionDateStr = DateUtils.convertDateToString(domain
				.getSubmissionDate(), Constants.DATE_FORMAT);
		publicReleaseDateStr = DateUtils.convertDateToString(domain
				.getPublicReleaseDate(), Constants.DATE_FORMAT);
		this.diseaseNames = domain.getDiseases();
	}

	public StudyBean() {

	}

	public StudyBean(String studyId) {
		domain.setId(new Long(studyId));
	}

	public StudyBean(Study study) {
		this.domain = study;

		if (study.getSampleCollection() != null
				&& !study.getSampleCollection().isEmpty()) {
			hasSamples = true;
		}
		if (study.getCharacterizationCollection() != null
				&& !study.getCharacterizationCollection().isEmpty()) {
			hasCharacterizations = true;
		}
		if (study.getPublicationCollection() != null
				&& !study.getPublicationCollection().isEmpty()) {
			hasPublications = true;
		}
		if (study.getProtocolCollection() != null
				&& !study.getProtocolCollection().isEmpty()) {
			hasProtocols = true;
		}
	}

	public String getDiseaseType() {
		return diseaseType;
	}

	public void setDiseaseType(String diseaseType) {
		this.diseaseType = diseaseType;
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

	public List<SampleBean> getStudySamples() {
		return studySamples;
	}

	public Boolean getHasPublications() {
		return hasPublications;
	}

	public void setHasPublications(Boolean hasPublications) {
		this.hasPublications = hasPublications;
	}

	public Boolean getHasSamples() {
		return hasSamples;
	}

	public void setHasSamples(Boolean hasSamples) {
		this.hasSamples = hasSamples;
	}

	public Boolean getHasProtocols() {
		return hasProtocols;
	}

	public void setHasProtocols(Boolean hasProtocols) {
		this.hasProtocols = hasProtocols;
	}

	public Boolean getHasCharacterizations() {
		return hasCharacterizations;
	}

	public void setHasCharacterizations(Boolean hasCharacterizations) {
		this.hasCharacterizations = hasCharacterizations;
	}

	public void setStudySample(List<SampleBean> studySamples) {
		this.studySamples = studySamples;
	}

	public PointOfContactBean getPrimaryPOCBean() {
		return primaryPOCBean;
	}

	public void setPrimaryPOCBean(PointOfContactBean primaryPOCBean) {
		this.primaryPOCBean = primaryPOCBean;
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

	public PointOfContactBean getThePOC() {
		return thePOC;
	}

	public void setThePOC(PointOfContactBean thePOC) {
		this.thePOC = thePOC;
	}

	public List<PointOfContactBean> getOtherPOCBeans() {
		return otherPOCBeans;
	}

	public void setOtherPOCBeans(List<PointOfContactBean> otherPOCBeans) {
		this.otherPOCBeans = otherPOCBeans;
	}

	public String getSubmissionDateStr() {
		return submissionDateStr;
	}

	public void setSubmissionDateStr(String submissionDateStr) {
		this.submissionDateStr = submissionDateStr;
	}

	public String getPublicReleaseDateStr() {
		return publicReleaseDateStr;
	}

	public void setPublicReleaseDateStr(String publicReleaseDateStr) {
		this.publicReleaseDateStr = publicReleaseDateStr;
	}

	public void setEndDateStr(String endDateStr) {
		this.endDateStr = endDateStr;
	}

	public String getStartDateStr() {
		return startDateStr;
	}

	public String getEndDateStr() {
		return endDateStr;
	}
}

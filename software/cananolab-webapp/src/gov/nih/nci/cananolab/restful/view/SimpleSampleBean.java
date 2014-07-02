package gov.nih.nci.cananolab.restful.view;

import gov.nih.nci.cananolab.dto.particle.SampleBean;

import java.util.Date;
import java.util.Map;

public class SimpleSampleBean {
	long sampleId;
	String sampleName;
	String pointOfContact;
	String Composition;
	String[] functions;
	String[] characterizations;
	String dataAvailability;
	
	Map<String, String> pointOfContactInfo;
	
	Date createdDate;

	public long getSampleId() {
		return sampleId;
	}

	public void setSampleId(long sampleId) {
		this.sampleId = sampleId;
	}

	public String getSampleName() {
		return sampleName;
	}

	public void setSampleName(String sampleName) {
		this.sampleName = sampleName;
	}

	public String getPointOfContact() {
		return pointOfContact;
	}

	public void setPointOfContact(String pointOfContact) {
		this.pointOfContact = pointOfContact;
	}

	public String getComposition() {
		return Composition;
	}

	public void setComposition(String composition) {
		Composition = composition;
	}

	public String[] getFunctions() {
		return functions;
	}

	public void setFunctions(String[] functions) {
		this.functions = functions;
	}

	public String[] getCharacterizations() {
		return characterizations;
	}

	public void setCharacterizations(String[] characterizations) {
		this.characterizations = characterizations;
	}

	public String getDataAvailability() {
		return dataAvailability;
	}

	public void setDataAvailability(String dataAvailability) {
		this.dataAvailability = dataAvailability;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	public Map<String, String> getPointOfContactInfo() {
		return pointOfContactInfo;
	}

	public void setPointOfContactInfo(Map<String, String> pointOfContactInfo) {
		this.pointOfContactInfo = pointOfContactInfo;
	}

	public void transferSampleBeanForBasicResultView(SampleBean sampleBean) {
		
		if (sampleBean == null) return;
		setSampleName(sampleBean.getDomain().getName());
		setCharacterizations(sampleBean.getCharacterizationClassNames());
		setComposition(sampleBean.getDomain().getSampleComposition().getSample().getName());
		setCreatedDate(new Date());
		setDataAvailability(sampleBean.getDataAvailabilityMetricsScore());
		setFunctions(sampleBean.getFunctionalizingEntityClassNames());
		setPointOfContact(sampleBean.getThePOC().getOrganizationDisplayName());
		setSampleId(sampleBean.getDomain().getId());
	}
}

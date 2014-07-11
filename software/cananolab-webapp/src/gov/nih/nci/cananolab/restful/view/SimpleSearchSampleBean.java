package gov.nih.nci.cananolab.restful.view;

import gov.nih.nci.cananolab.dto.particle.SampleBean;

import java.util.Date;

public class SimpleSearchSampleBean {
	
	long sampleId;
	String sampleName;
	String pointOfContact;
	String composition;
	String[] functions;
	String[] characterizations;
	String dataAvailability;
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
		return composition;
	}
	public void setComposition(String composition) {
		this.composition = composition;
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
	
public void transferSampleBeanForBasicResultView(SampleBean sampleBean) {
		
		if (sampleBean == null) return;
		setSampleId(sampleBean.getDomain().getId());
		setSampleName(sampleBean.getDomain().getName());
		setPointOfContact(sampleBean.getThePOC().getOrganizationDisplayName());
		setComposition(sampleBean.getDomain().getSampleComposition().getSample().getName());
		setFunctions(sampleBean.getFunctionClassNames());
		setCharacterizations(sampleBean.getCharacterizationClassNames());
		setDataAvailability(sampleBean.getDataAvailabilityMetricsScore());
		
		setCreatedDate(sampleBean.getPrimaryPOCBean().getDomain().getCreatedDate());
		
	}
}

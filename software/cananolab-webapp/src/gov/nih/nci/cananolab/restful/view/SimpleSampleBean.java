package gov.nih.nci.cananolab.restful.view;

import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleSampleBean {
	long sampleId;
	String sampleName;
	String pointOfContact;
	String Composition;
	String[] functions;
	String[] characterizations;
	String dataAvailability;
	long pocBeanDomainId;
	
	public List<PointOfContactBean> getOtherPOCBeans() {
		return otherPOCBeans;
	}

	public void setOtherPOCBeans(List<PointOfContactBean> otherPOCBeans) {
		this.otherPOCBeans = otherPOCBeans;
	}

	private List<PointOfContactBean> otherPOCBeans = new ArrayList<PointOfContactBean>();
	
		public long getPocBeanDomainId() {
		return pocBeanDomainId;
	}

	public void setPocBeanDomainId(long pocBeanDomainId) {
		this.pocBeanDomainId = pocBeanDomainId;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	String keywords;
	
	public Map<String, String> pointOfContactInfo;
	
	String createdDate;

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

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
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
		setCreatedDate(new Date().toString());
		setDataAvailability(sampleBean.getDataAvailabilityMetricsScore());
		setFunctions(sampleBean.getFunctionalizingEntityClassNames());
		setPointOfContact(sampleBean.getThePOC().getOrganizationDisplayName());
		setSampleId(sampleBean.getDomain().getId());
	}
	
	public void transferSampleBeanForSummaryView(SampleBean sampleBean) {
		
		if (sampleBean == null) return;
		setSampleName(sampleBean.getDomain().getName());
		setCreatedDate(sampleBean.getPrimaryPOCBean().getDomain().getCreatedDate().toString());
		setKeywords(sampleBean.getKeywordsDisplayName());
		setPocBeanDomainId(sampleBean.getPrimaryPOCBean().getDomain().getId());
		setOtherPOCBeans(sampleBean.getOtherPOCBeans());
		
		pointOfContactInfo = new HashMap<String, String>();
		pointOfContactInfo.put("contactPerson", sampleBean.getPrimaryPOCBean().getPersonDisplayName());
		pointOfContactInfo.put("organiztionRole", sampleBean.getPrimaryPOCBean().getOrganizationDisplayName());
		pointOfContactInfo.put("primaryContact", sampleBean.getPrimaryPOCBean().getPrimaryStatus().toString());
		setPointOfContactInfo(pointOfContactInfo);

		
	}
    
}

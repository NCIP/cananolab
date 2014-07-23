package gov.nih.nci.cananolab.restful.view.edit;

import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;

import java.util.ArrayList;
import java.util.List;

public class SampleEditGeneralBean {
	
	String sampleName;
	long sampleId;
	
	List<SimplePointOfContactBean> pointOfContacts;
	
	List<String> keywords;
	
	List<AccessBean> accessToSample;
	
	DataAvailabilityBean dataAvailability;

	List<String> errors = new ArrayList<String>();

	public String getSampleName() {
		return sampleName;
	}

	public void setSampleName(String sampleName) {
		this.sampleName = sampleName;
	}

	public long getSampleId() {
		return sampleId;
	}

	public void setSampleId(long sampleId) {
		this.sampleId = sampleId;
	}

	public List<SimplePointOfContactBean> getPointOfContacts() {
		return pointOfContacts;
	}

	public void setPointOfContacts(List<SimplePointOfContactBean> pointOfContacts) {
		this.pointOfContacts = pointOfContacts;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	public List<AccessBean> getAccessToSample() {
		return accessToSample;
	}

	public void setAccessToSample(List<AccessBean> accessToSample) {
		this.accessToSample = accessToSample;
	}

	public DataAvailabilityBean getDataAvailability() {
		return dataAvailability;
	}

	public void setDataAvailability(DataAvailabilityBean dataAvailability) {
		this.dataAvailability = dataAvailability;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
	
	public void transferSampleBeanData(SampleBean sampleBean) {
		
		this.sampleName = sampleBean.getDomain().getName();
		this.sampleId = sampleBean.getDomain().getId();
		
		transferPointOfContactData(sampleBean);
	}
	
	protected void transferPointOfContactData(SampleBean sampleBean) {
		pointOfContacts = new ArrayList<SimplePointOfContactBean>();
		PointOfContact samplePOC = sampleBean.getPrimaryPOCBean().getDomain();
		if (samplePOC != null && samplePOC.getId() > 0) {
			SimplePointOfContactBean poc = new SimplePointOfContactBean();
			transferPointOfContactData(samplePOC, poc);
			pointOfContacts.add(poc);
		}
		
		List<PointOfContactBean> others = sampleBean.getOtherPOCBeans();
		for (PointOfContactBean aPoc : others) {
			
		}
	}
	
	protected void transferPointOfContactData(PointOfContact samplePOC, SimplePointOfContactBean poc) {
		poc.setPrimaryContact(true);
		poc.setFirstName(samplePOC.getFirstName());
		poc.setLastName(samplePOC.getLastName());
		poc.setOrganizationName(samplePOC.getOrganization().getName());
		poc.setRole(samplePOC.getRole());
		poc.setId(samplePOC.getId());
	}
}

package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.Study;

import java.util.ArrayList;
import java.util.List;

public class StudyBean {
	private Study domain;
	private PointOfContactBean thePOC = new PointOfContactBean();
	private List<PointOfContactBean> otherPOCBeans = new ArrayList<PointOfContactBean>();
	private String location; // e.g. NCICB, NCL, WUSTL, etc.
	
	public StudyBean(Study study) {
		domain=study;		
	}
	
	public Study getDomain() {
		return domain;
	}
	public void setDomain(Study domain) {
		this.domain = domain;
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
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}	
}

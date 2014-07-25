package gov.nih.nci.cananolab.restful.view;

import java.util.Date;

import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.dto.common.PublicationBean;

public class SimpleSearchPublicationBean {
	
	String displayName;
	String publicationType;
	String[] researchAreas;
	String[] sampleNames;
	String descriptionDetail;
	String status;
	Date createdDate;
	
	
	public String getDisplayName() {
		return displayName;
	}


	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}


	public String getPublicationType() {
		return publicationType;
	}

	public String[] getResearchAreas() {
		return researchAreas;
	}


	public void setResearchAreas(String[] researchAreas) {
		this.researchAreas = researchAreas;
	}



	public void setPublicationType(String publicationType) {
		this.publicationType = publicationType;
	}


	public String[] getSampleNames() {
		return sampleNames;
	}


	public void setSampleNames(String[] sampleNames) {
		this.sampleNames = sampleNames;
	}


	public String getDescriptionDetail() {
		return descriptionDetail;
	}


	public void setDescriptionDetail(String descriptionDetail) {
		this.descriptionDetail = descriptionDetail;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public Date getCreatedDate() {
		return createdDate;
	}


	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}


	public void transferSampleBeanForBasicResultView(PublicationBean bean) {
		if (bean == null) return;
		
		setDisplayName(bean.getDisplayName());
		Publication pub = (Publication) bean.getDomainFile();
		setPublicationType(pub.getCategory());
		setResearchAreas(bean.getResearchAreas());
		setSampleNames(bean.getSampleNames());
		setDescriptionDetail(pub.getDescription());
		setStatus(pub.getStatus());
		setCreatedDate(pub.getCreatedDate());
		
		
		System.out.println("***** Publication Data view*******");
		System.out.println("Bibliography info  "+bean.getDisplayName());
		System.out.println("Publication Type "+pub.getCategory());
		System.out.println("Reserach Areas  "+bean.getResearchAreas());
		String[] name = bean.getSampleNames();
		for(int i=0;i<name.length;i++){
			
		System.out.println("Sample Names  "+name[i]);
		}
		System.out.println("Status  "+pub.getStatus());
		System.out.println("Created Date  "+pub.getCreatedDate());
		System.out.println("DescriptionDetail  "+pub.getDescription());
		
		
		
	
	}

}

package gov.nih.nci.cananolab.restful.view;

import java.util.Date;

import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.restful.util.SecurityUtil;
import gov.nih.nci.cananolab.service.security.UserBean;

public class SimpleSearchPublicationBean {
	
	long id;
	String displayName;
	String publicationType;
	String[] researchAreas;
	String[] sampleNames;
	String descriptionDetail;
	String status;
	Date createdDate;
	boolean userDeletable = false;
	
	public boolean getUserDeletable() {
		return userDeletable;
	}

	public void setUserDeletable(boolean userDeletable) {
		this.userDeletable = userDeletable;
	}


	boolean editable = false;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isEditable() {
		return editable;
	}


	public void setEditable(boolean editable) {
		this.editable = editable;
	}


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


	public void transferSampleBeanForBasicResultView(PublicationBean bean, UserBean user) {
		try{
			if (bean == null) return;

			setDisplayName(bean.getDisplayName());
			Publication pub = (Publication) bean.getDomainFile();
			setPublicationType(pub.getCategory());
			setResearchAreas(bean.getResearchAreas());
			setSampleNames(bean.getSampleNames());
			setDescriptionDetail(pub.getDescription());
			setStatus(pub.getStatus());
			setCreatedDate(pub.getCreatedDate());
			setUserDeletable(bean.getUserDeletable());
			setEditable(bean.getUserUpdatable());
		//	editable = SecurityUtil.isEntityEditableForUser(bean.getUserAccesses(), user);
			id = bean.getDomainFile().getId();
		}catch(Exception e){
			System.out.println("error while setting up simple bean  "+e);
			e.printStackTrace();
		}

	}

}

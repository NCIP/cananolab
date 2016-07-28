package gov.nih.nci.cananolab.restful.view;

import java.util.Date;

import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.dto.common.PublicationBean;

public class SimpleSearchPublicationBean {
	
	long id;
	String displayName;
	String publicationType;
	String[] researchAreas;
	String[] sampleNames;
	String descriptionDetail;
	String status;
	String title;
	Date createdDate;
	boolean userDeletable = false;
	long pubmedId;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getPubmedId() {
		return pubmedId;
	}

	public void setPubmedId(long pubmedId) {
		this.pubmedId = pubmedId;
	}

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


	public void transferSampleBeanForBasicResultView(PublicationBean bean) {
		try{
			if (bean == null) return;

			this.setDisplayName(bean.getDisplayName());
			Publication pub = (Publication) bean.getDomainFile();
			this.setPublicationType(pub.getCategory());
			this.setResearchAreas(bean.getResearchAreas());
			this.setSampleNames(bean.getSampleNames());
			this.setDescriptionDetail(pub.getDescription());
			this.setStatus(pub.getStatus());
			this.setCreatedDate(pub.getCreatedDate());
			this.setUserDeletable(bean.getUserDeletable());
			this.setEditable(bean.getUserUpdatable());
			this.setTitle(pub.getTitle());
			if(pub.getPubMedId()!=null)
				this.setPubmedId(pub.getPubMedId());
		//	editable = SecurityUtil.isEntityEditableForUser(bean.getUserAccesses(), user);
			this.setId(bean.getDomainFile().getId());
		}catch(Exception e){
			System.out.println("error while setting up simple bean  "+e);
			e.printStackTrace();
		}

	}

}

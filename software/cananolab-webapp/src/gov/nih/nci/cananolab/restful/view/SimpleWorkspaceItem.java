package gov.nih.nci.cananolab.restful.view;

import gov.nih.nci.cananolab.dto.common.AccessibilityBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SimpleWorkspaceItem {
	
	boolean isEditable; 
	String name;
	long id;
	String submisstionStatus;
	Date createdDate;
	long fileId;  //for protocol
	
	String externalURL;
	
	String comments;
	String pubMedId;
	String access;

	List<String> actions = new ArrayList<String>();
	
	public List<String> getActions() {
		return actions;
	}

	public void setActions(List<String> actions) {
		this.actions = actions;
	}

	public String getExternalURL() {
		return externalURL;
	}

	public void setExternalURL(String externalURL) {
		this.externalURL = externalURL;
	}

	public boolean isEditable() {
		return isEditable;
	}

	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSubmisstionStatus() {
		return submisstionStatus;
	}

	public void setSubmisstionStatus(String submisstionStatus) {
		this.submisstionStatus = submisstionStatus;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getPubMedId() {
		return pubMedId;
	}

	public void setPubMedId(String pubMedId) {
		this.pubMedId = pubMedId;
	}

	public String getAccess() {
		return access;
	}

	public void setAccess(String access) {
		this.access = access;
	}
	
	public long getFileId() {
		return fileId;
	}

	public void setFileId(long fileId) {
		this.fileId = fileId;
	}	

}

package gov.nih.nci.cananolab.restful.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SimpleWorkspaceItem {
	
	boolean editable; 
	String name;
	long id;
	String submisstionStatus;
	Date createdDate;
	
	long fileId;  //for protocol
	String externalURL; //for protocol
	
	String comments;
	String pubMedDOIId;
	String access;
	boolean isOwner;
	
	public boolean isOwner() {
		return isOwner;
	}

	public void setOwner(boolean isOwner) {
		this.isOwner = isOwner;
	}

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
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
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

	public String getPubMedDOIId() {
		return pubMedDOIId;
	}

	public void setPubMedDOIId(String pubMedDOIId) {
		this.pubMedDOIId = pubMedDOIId;
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

package gov.nih.nci.cananolab.restful.view.edit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.security.AccessControlInfo;

public class SimpleSubmitProtocolBean {

	String type = "";
	String name = "";
	String abbreviation = "";
	String version = "";
	Long fileId;
	Long id = 0L;
	String fileTitle = "";
	String fileName = "";
	String fileDescription = "";
	List<AccessControlInfo> groupAccesses = new ArrayList<AccessControlInfo>();
	List<AccessControlInfo> userAccesses =  new ArrayList<AccessControlInfo>();
	String protectedData = "";
	Boolean isPublic = false;
	Boolean isOwner = false;
	String ownerName = "";
	String createdBy = "";
	Date createdDate;
	AccessControlInfo theAccess;
	Boolean userDeletable = false;
	List<String> errors;
	Boolean userUpdatable = false;
	String uri = "";
	Boolean uriExternal = false;
	String externalUrl = "";
	Boolean review =false;
	byte[] newFileData;
	
	public byte[] getNewFileData() {
		return newFileData;
	}
	public void setNewFileData(byte[] newFileData) {
		this.newFileData = newFileData;
	}
	public Boolean getReview() {
		return review;
	}
	public void setReview(Boolean review) {
		this.review = review;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public Boolean getUriExternal() {
		return uriExternal;
	}
	public void setUriExternal(Boolean uriExternal) {
		this.uriExternal = uriExternal;
	}
	public String getExternalUrl() {
		return externalUrl;
	}
	public void setExternalUrl(String externalUrl) {
		this.externalUrl = externalUrl;
	}
	public Boolean getUserUpdatable() {
		return userUpdatable;
	}
	public void setUserUpdatable(Boolean userUpdatable) {
		this.userUpdatable = userUpdatable;
	}
	public List<String> getErrors() {
		return errors;
	}
	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
	public Boolean getUserDeletable() {
		return userDeletable;
	}
	public void setUserDeletable(Boolean userDeletable) {
		this.userDeletable = userDeletable;
	}
	public AccessControlInfo getTheAccess() {
		return theAccess;
	}
	public void setTheAccess(AccessControlInfo theAccess) {
		this.theAccess = theAccess;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAbbreviation() {
		return abbreviation;
	}
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public Long getFileId() {
		return fileId;
	}
	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}
	
	public String getFileTitle() {
		return fileTitle;
	}
	public void setFileTitle(String fileTitle) {
		this.fileTitle = fileTitle;
	}
	public String getFileDescription() {
		return fileDescription;
	}
	public void setFileDescription(String fileDescription) {
		this.fileDescription = fileDescription;
	}
	public List<AccessControlInfo> getGroupAccesses() {
		return groupAccesses;
	}
	public void setGroupAccesses(List<AccessControlInfo> groupAccesses) {
		this.groupAccesses = groupAccesses;
	}
	public List<AccessControlInfo> getUserAccesses() {
		return userAccesses;
	}
	public void setUserAccesses(List<AccessControlInfo> userAccesses) {
		this.userAccesses = userAccesses;
	}
	public String getProtectedData() {
		return protectedData;
	}
	public void setProtectedData(String protectedData) {
		this.protectedData = protectedData;
	}
	public Boolean getIsPublic() {
		return isPublic;
	}
	public void setIsPublic(Boolean isPublic) {
		this.isPublic = isPublic;
	}
	public Boolean getIsOwner() {
		return isOwner;
	}
	public void setIsOwner(Boolean isOwner) {
		this.isOwner = isOwner;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
}

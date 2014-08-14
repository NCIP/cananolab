package gov.nih.nci.cananolab.restful.view.edit;

import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.dto.common.ProtocolBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SimpleSubmitProtocolBean {

	String type = "";
	String name = "";
	String abbreviation = "";
	String version = "";
	Long fileId;
	Long id;
	String fileTitle = "";
	String fileDescription = "";
	List<AccessibilityBean> groupAccesses = new ArrayList<AccessibilityBean>();
	List<AccessibilityBean> userAccesses =  new ArrayList<AccessibilityBean>();
	String protectedData = "";
	Boolean isPublic = false;
	Boolean isOwner = false;
	String ownerName = "";
	String createdBy = "";
	Date createdDate;
	AccessibilityBean theAccess;
	Boolean userDeletable = false;
	List<String> errors;
	Boolean userUpdatable = false;
	String uri = "";
	Boolean uriExternal = false;
	String externalUrl = "";
	
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
	public AccessibilityBean getTheAccess() {
		return theAccess;
	}
	public void setTheAccess(AccessibilityBean theAccess) {
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
	public List<AccessibilityBean> getGroupAccesses() {
		return groupAccesses;
	}
	public void setGroupAccesses(List<AccessibilityBean> groupAccesses) {
		this.groupAccesses = groupAccesses;
	}
	public List<AccessibilityBean> getUserAccesses() {
		return userAccesses;
	}
	public void setUserAccesses(List<AccessibilityBean> userAccesses) {
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
	public void transferProtocolBeanForEdit(ProtocolBean bean) {
		setAbbreviation(bean.getDomain().getAbbreviation());
		setCreatedBy(bean.getDomain().getCreatedBy());
		setCreatedDate(bean.getDomain().getCreatedDate());
		if(bean.getDomain().getFile()!= null){
		setFileDescription(bean.getDomain().getFile().getDescription());
		setFileId(bean.getDomain().getFile().getId());
		setFileTitle(bean.getDomain().getFile().getTitle());
		}
		setGroupAccesses(bean.getGroupAccesses());
		setUserAccesses(bean.getUserAccesses());
		setTheAccess(bean.getTheAccess());
		setId(bean.getDomain().getId());
		setIsOwner(bean.getUserIsOwner());
		setIsPublic(bean.getPublicStatus());
		setName(bean.getDomain().getName());
		setType(bean.getDomain().getType());
		setUserDeletable(bean.getUserDeletable());
		setVersion(bean.getDomain().getVersion());
		setUserUpdatable(bean.getUserUpdatable());
		setUri(bean.getDomain().getFile().getUri());
		setUriExternal(bean.getDomain().getFile().getUriExternal());
		
	}
	
	
}

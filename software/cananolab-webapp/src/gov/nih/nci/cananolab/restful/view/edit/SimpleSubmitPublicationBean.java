package gov.nih.nci.cananolab.restful.view.edit;

import gov.nih.nci.cananolab.domain.common.Author;
import gov.nih.nci.cananolab.dto.common.AccessibilityBean;

import java.util.ArrayList;
import java.util.List;

public class SimpleSubmitPublicationBean {

	String sampleTitle = "";
	String title = "";
	String category = "";
	String status = "";
	Long pubMedId;
	String digitalObjectId = "";
	String journalName = "";
	Integer year;
	String volume = "";
	String startPage ="";
	String endPage = "";
	List<Author> authors = new ArrayList<Author>();
	String firstName = "";
	String lastName = "";
	String initial = "";
	String keywordsStr = "";
	String description = "";
	String researchAreas;
	String uri = "";
	Boolean uriExternal = false;
	Long fileId;
	String sampleId = "";
	String sampleNamesStr;
	List<AccessibilityBean> groupAccesses = new ArrayList<AccessibilityBean>();
	List<AccessibilityBean> userAccesses =  new ArrayList<AccessibilityBean>();
	String protectedData = "";
	Boolean isPublic = false;
	Boolean isOwner = false;
	String ownerName = "";
	String createdBy = "";
	Boolean userDeletable = false;
	private AccessibilityBean theAccess = new AccessibilityBean();
	String externalUrl = "";
	
	
	public String getExternalUrl() {
		return externalUrl;
	}
	public void setExternalUrl(String externalUrl) {
		this.externalUrl = externalUrl;
	}
	public AccessibilityBean getTheAccess() {
		return theAccess;
	}
	public void setTheAccess(AccessibilityBean theAccess) {
		this.theAccess = theAccess;
	}
	public String getSampleTitle() {
		return sampleTitle;
	}
	public void setSampleTitle(String sampleTitle) {
		this.sampleTitle = sampleTitle;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getPubMedId() {
		return pubMedId;
	}
	public void setPubMedId(Long pubMedId) {
		this.pubMedId = pubMedId;
	}
	public String getDigitalObjectId() {
		return digitalObjectId;
	}
	public void setDigitalObjectId(String digitalObjectId) {
		this.digitalObjectId = digitalObjectId;
	}
	public String getJournalName() {
		return journalName;
	}
	public void setJournalName(String journalName) {
		this.journalName = journalName;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public String getVolume() {
		return volume;
	}
	public void setVolume(String volume) {
		this.volume = volume;
	}
	public String getStartPage() {
		return startPage;
	}
	public void setStartPage(String startPage) {
		this.startPage = startPage;
	}
	public String getEndPage() {
		return endPage;
	}
	public void setEndPage(String endPage) {
		this.endPage = endPage;
	}
	public List<Author> getAuthors() {
		return authors;
	}
	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getInitial() {
		return initial;
	}
	public void setInitial(String initial) {
		this.initial = initial;
	}
	public String getKeywordsStr() {
		return keywordsStr;
	}
	public void setKeywordsStr(String keywordsStr) {
		this.keywordsStr = keywordsStr;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getResearchAreas() {
		return researchAreas;
	}
	public void setResearchAreas(String researchAreas) {
		this.researchAreas = researchAreas;
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
	public Long getFileId() {
		return fileId;
	}
	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}
	public String getSampleId() {
		return sampleId;
	}
	public void setSampleId(String sampleId) {
		this.sampleId = sampleId;
	}
	
	public String getSampleNamesStr() {
		return sampleNamesStr;
	}
	public void setSampleNamesStr(String sampleNamesStr) {
		this.sampleNamesStr = sampleNamesStr;
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
	public Boolean getUserDeletable() {
		return userDeletable;
	}
	public void setUserDeletable(Boolean userDeletable) {
		this.userDeletable = userDeletable;
	}
	
	
}

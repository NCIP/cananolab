package gov.nih.nci.cananolab.restful.view.edit;

import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.dto.common.PublicationBean;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimplePublicationEditBean {

	String sampleTitle;
	String title;
	String category;
	String status;
	Long pubMedId;
	String digitalObjectId;
	String journalName;
	Integer year;
	String volume;
	String startPage;
	String endPage;
	Map<String, String> authors;
	String authorId;
	String firstName;
	String lastName;
	String initial;
	String keywordsStr;
	String description;
	String[] researchAreas;
	String uri;
	Boolean uriExternal;
	Long fileId;
	String sampleId;
	String associatedSampleNames;
	List<AccessibilityBean> groupAccesses;
	List<AccessibilityBean> userAccesses;
	String protectedData;
	Boolean isPublic;
	Boolean isOwner;
	String ownerName;
	String createdBy;
	Boolean userDeletable;
	
	
	public Boolean getUserDeletable() {
		return userDeletable;
	}


	public void setUserDeletable(Boolean userDeletable) {
		this.userDeletable = userDeletable;
	}


	public Boolean getUriExternal() {
		return uriExternal;
	}


	public void setUriExternal(Boolean uriExternal) {
		this.uriExternal = uriExternal;
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


	public Map<String, String> getAuthors() {
		return authors;
	}


	public void setAuthors(Map<String, String> authors) {
		this.authors = authors;
	}


	public String getAuthorId() {
		return authorId;
	}


	public void setAuthorId(String authorId) {
		this.authorId = authorId;
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


	public String[] getResearchAreas() {
		return researchAreas;
	}


	public void setResearchAreas(String[] researchAreas) {
		this.researchAreas = researchAreas;
	}


	public String getUri() {
		return uri;
	}


	public void setUri(String uri) {
		this.uri = uri;
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


	public String getAssociatedSampleNames() {
		return associatedSampleNames;
	}


	public void setAssociatedSampleNames(String associatedSampleNames) {
		this.associatedSampleNames = associatedSampleNames;
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


	public void transferPublicationBeanForEdit(PublicationBean pubBean) {
		// TODO Auto-generated method stub
		authors = new HashMap<String, String>();
		Publication pub = (Publication) pubBean.getDomainFile();
		setCategory(pub.getCategory());
		setStatus(pub.getStatus());
		setPubMedId(pub.getPubMedId());
		setDigitalObjectId(pub.getDigitalObjectId());
		setTitle(pub.getTitle());
		setJournalName(pub.getJournalName());
		setYear(pub.getYear());
		setVolume(pub.getVolume());
		setStartPage(pub.getStartPage());
		setEndPage(pub.getEndPage());
		for(int i=0;i<pubBean.getAuthors().size();i++){
		authors.put("firstName", pubBean.getAuthors().get(i).getFirstName());
		authors.put("lastName", pubBean.getAuthors().get(i).getLastName());
		authors.put("initial", pubBean.getAuthors().get(i).getInitial());
		}
		
		setKeywordsStr(pubBean.getKeywordsStr());
		setDescription(pub.getDescription());
		setResearchAreas(pubBean.getResearchAreas());
		setUri(pub.getUri());
		setFileId(pub.getId());
		setUriExternal(pub.getUriExternal());
		setAssociatedSampleNames(pubBean.getSampleNamesStr());
		setGroupAccesses(pubBean.getGroupAccesses());
		setUserAccesses(pubBean.getUserAccesses());
		setIsPublic(pubBean.getPublicStatus());
		setIsOwner(pubBean.getUserIsOwner());
		setCreatedBy(pub.getCreatedBy());
		setUserDeletable(pubBean.getUserDeletable());
		
	}
	
}

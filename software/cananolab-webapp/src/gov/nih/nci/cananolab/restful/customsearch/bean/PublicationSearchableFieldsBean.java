package gov.nih.nci.cananolab.restful.customsearch.bean;

import java.util.Date;
import java.util.List;

public class PublicationSearchableFieldsBean {
	
	private String pubTitle;
	private String pubmedId;
	private String doiId;
	private List<String> authors;
	private List<String> pubKeywords;
	private List<String> sampleName;
	private String pubDesc;
	private String nanoEntity;
	private String funcEntity;
	private String function;
	private String publicationId;
	private Date createdDate; 
	
	public String getPubTitle() {
		return pubTitle;
	}
	public void setPubTitle(String pubTitle) {
		this.pubTitle = pubTitle;
	}
	public String getPubmedId() {
		return pubmedId;
	}
	public void setPubmedId(String pubmedId) {
		this.pubmedId = pubmedId;
	}
	public String getDoiId() {
		return doiId;
	}
	public void setDoiId(String doiId) {
		this.doiId = doiId;
	}
	
	public List<String> getAuthors() {
		return authors;
	}
	public void setAuthors(List<String> authors) {
		this.authors = authors;
	}
	public List<String> getPubKeywords() {
		return pubKeywords;
	}
	public void setPubKeywords(List<String> pubKeywords) {
		this.pubKeywords = pubKeywords;
	}
	public List<String> getSampleName() {
		return sampleName;
	}
	public void setSampleName(List<String> sampleName) {
		this.sampleName = sampleName;
	}
	public String getPubDesc() {
		return pubDesc;
	}
	public void setPubDesc(String pubDesc) {
		this.pubDesc = pubDesc;
	}
	public String getNanoEntity() {
		return nanoEntity;
	}
	public void setNanoEntity(String nanoEntity) {
		this.nanoEntity = nanoEntity;
	}
	public String getFuncEntity() {
		return funcEntity;
	}
	public void setFuncEntity(String funcEntity) {
		this.funcEntity = funcEntity;
	}
	public String getFunction() {
		return function;
	}
	public void setFunction(String function) {
		this.function = function;
	}
	public String getPublicationId() {
		return publicationId;
	}
	public void setPublicationId(String publicationId) {
		this.publicationId = publicationId;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

}

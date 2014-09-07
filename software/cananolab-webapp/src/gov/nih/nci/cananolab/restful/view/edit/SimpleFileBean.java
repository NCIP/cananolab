package gov.nih.nci.cananolab.restful.view.edit;

import gov.nih.nci.cananolab.dto.common.FileBean;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class SimpleFileBean {

	boolean uriExternal =false;
	String uri = "";
	String type = "";
	String title = "";
	String description = "";
	String keywordsStr = "";
	Long id;
	String createdBy = "";
	Date createdDate;
	String sampleId = "";
	List<String> errors;
	String externalUrl = "";
	
	public String getExternalUrl() {
		return externalUrl;
	}
	public void setExternalUrl(String externalUrl) {
		this.externalUrl = externalUrl;
	}
	public String getSampleId() {
		return sampleId;
	}
	public void setSampleId(String sampleId) {
		this.sampleId = sampleId;
	}
	public List<String> getErrors() {
		return errors;
	}
	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public boolean getUriExternal() {
		return uriExternal;
	}
	public void setUriExternal(boolean uriExternal) {
		this.uriExternal = uriExternal;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getKeywordsStr() {
		return keywordsStr;
	}
	public void setKeywordsStr(String keywordsStr) {
		this.keywordsStr = keywordsStr;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void transferSimpleFileBean(FileBean simpleBean,
			HttpServletRequest request) {

		this.setDescription(simpleBean.getDescription());
		this.setId(simpleBean.getDomainFile().getId());
		this.setKeywordsStr(simpleBean.getKeywordsStr());
		this.setTitle(simpleBean.getDomainFile().getTitle());
		this.setType(simpleBean.getDomainFile().getType());
		this.setUri(simpleBean.getDomainFile().getUri());
		this.setUriExternal(simpleBean.getDomainFile().getUriExternal());
		this.setExternalUrl(simpleBean.getExternalUrl());
		this.setSampleId((String) request.getSession().getAttribute("sampleId"));
		this.setCreatedBy(simpleBean.getDomainFile().getCreatedBy());
		this.setCreatedDate(simpleBean.getDomainFile().getCreatedDate());
		
	}
	
	
}


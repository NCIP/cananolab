package gov.nih.nci.cananolab.restful.customsearch.bean;

import java.util.Date;
import java.util.List;

public class SampleSearchableFieldsBean {

	private String sampleName;
	private String samplePocName;
	private List<String> sampleKeywords;
	private String nanoEntityName;
	private String nanoEntityDesc;
	private String funcEntityName;
	private String function;
	private String characterization;
	private String sampleId;
	private Date createdDate;

	public String getSampleName() {
		return sampleName;
	}
	public void setSampleName(String sampleName) {
		this.sampleName = sampleName;
	}
	public String getSamplePocName() {
		return samplePocName;
	}
	public void setSamplePocName(String samplePocName) {
		this.samplePocName = samplePocName;
	}

	public List<String> getSampleKeywords() {
		return sampleKeywords;
	}
	public void setSampleKeywords(List<String> sampleKeywords) {
		this.sampleKeywords = sampleKeywords;
	}
	public String getNanoEntityName() {
		return nanoEntityName;
	}
	public void setNanoEntityName(String nanoEntityName) {
		this.nanoEntityName = nanoEntityName;
	}
	public String getNanoEntityDesc() {
		return nanoEntityDesc;
	}
	public void setNanoEntityDesc(String nanoEntityDesc) {
		this.nanoEntityDesc = nanoEntityDesc;
	}
	public String getFuncEntityName() {
		return funcEntityName;
	}
	public void setFuncEntityName(String funcEntityName) {
		this.funcEntityName = funcEntityName;
	}
	public String getFunction() {
		return function;
	}
	public void setFunction(String function) {
		this.function = function;
	}
	public String getCharacterization() {
		return characterization;
	}
	public void setCharacterization(String characterization) {
		this.characterization = characterization;
	}
	public String getSampleId() {
		return sampleId;
	}
	public void setSampleId(String sampleId) {
		this.sampleId = sampleId;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	
}

package gov.nih.nci.cananolab.ui.form;

import gov.nih.nci.cananolab.dto.particle.SampleBean;

public class SearchPublicationForm {

	SampleBean sampleBean;
	String[] researchArea = new String[0];
	String[] nanomaterialEntityTypes = new String[0];
	String[] functionalizingEntityTypes = new String[0];		
	String[] functionTypes = new String[0];
	String title = "";
	String category = "";
	String authorsStr = "";
	String pubMedId = "";
	String digitalObjectId = "";
	String keywordsStr = "";
	String sampleName = "";
	String titleOperand = "";
	String nameOperand = "";
	int page;
	public SampleBean getSampleBean() {
		return sampleBean;
	}
	public void setSampleBean(SampleBean sampleBean) {
		this.sampleBean = sampleBean;
	}
	public String[] getResearchArea() {
		return researchArea;
	}
	public void setResearchArea(String[] researchArea) {
		this.researchArea = researchArea;
	}
	public String[] getNanomaterialEntityTypes() {
		return nanomaterialEntityTypes;
	}
	public void setNanomaterialEntityTypes(String[] nanomaterialEntityTypes) {
		this.nanomaterialEntityTypes = nanomaterialEntityTypes;
	}
	public String[] getFunctionalizingEntityTypes() {
		return functionalizingEntityTypes;
	}
	public void setFunctionalizingEntityTypes(String[] functionalizingEntityTypes) {
		this.functionalizingEntityTypes = functionalizingEntityTypes;
	}
	public String[] getFunctionTypes() {
		return functionTypes;
	}
	public void setFunctionTypes(String[] functionTypes) {
		this.functionTypes = functionTypes;
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
	public String getAuthorsStr() {
		return authorsStr;
	}
	public void setAuthorsStr(String authorsStr) {
		this.authorsStr = authorsStr;
	}
	public String getPubMedId() {
		return pubMedId;
	}
	public void setPubMedId(String pubMedId) {
		this.pubMedId = pubMedId;
	}
	public String getDigitalObjectId() {
		return digitalObjectId;
	}
	public void setDigitalObjectId(String digitalObjectId) {
		this.digitalObjectId = digitalObjectId;
	}
	public String getKeywordsStr() {
		return keywordsStr;
	}
	public void setKeywordsStr(String keywordsStr) {
		this.keywordsStr = keywordsStr;
	}
	public String getSampleName() {
		return sampleName;
	}
	public void setSampleName(String sampleName) {
		this.sampleName = sampleName;
	}
	public String getTitleOperand() {
		return titleOperand;
	}
	public void setTitleOperand(String titleOperand) {
		this.titleOperand = titleOperand;
	}
	public String getNameOperand() {
		return nameOperand;
	}
	public void setNameOperand(String nameOperand) {
		this.nameOperand = nameOperand;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
}

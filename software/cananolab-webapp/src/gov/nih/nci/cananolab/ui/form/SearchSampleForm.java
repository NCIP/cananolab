package gov.nih.nci.cananolab.ui.form;

public class SearchSampleForm {
	
	String samplePointOfContact = "";
	String pocOperand = "";
	String[] nanomaterialEntityTypes;
	String[] functionalizingEntityTypes;
	String[] functionTypes;
	String characterizationType = "";
	String[] characterizations;
	String sampleName = "";
	String nameOperand = "";
	String text = "";
	int page;
	
	public String getSamplePointOfContact() {
		return samplePointOfContact;
	}
	public void setSamplePointOfContact(String samplePointOfContact) {
		this.samplePointOfContact = samplePointOfContact;
	}
	public String getPocOperand() {
		return pocOperand;
	}
	public void setPocOperand(String pocOperand) {
		this.pocOperand = pocOperand;
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
	public String getCharacterizationType() {
		return characterizationType;
	}
	public void setCharacterizationType(String characterizationType) {
		this.characterizationType = characterizationType;
	}
	public String[] getCharacterizations() {
		return characterizations;
	}
	public void setCharacterizations(String[] characterizations) {
		this.characterizations = characterizations;
	}
	public String getSampleName() {
		return sampleName;
	}
	public void setSampleName(String sampleName) {
		this.sampleName = sampleName;
	}
	public String getNameOperand() {
		return nameOperand;
	}
	public void setNameOperand(String nameOperand) {
		this.nameOperand = nameOperand;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	
	

}

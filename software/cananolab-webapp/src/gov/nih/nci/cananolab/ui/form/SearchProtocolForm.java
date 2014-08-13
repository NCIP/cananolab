package gov.nih.nci.cananolab.ui.form;

public class SearchProtocolForm {

	String fileTitle = "";
	String titleOperand = "";
	String protocolType = "";
	String protocolName = "";
	String nameOperand = "";
	String protocolAbbreviation = "";
	String abbreviationOperand = "";
	int page;
	
	public String getFileTitle() {
		return fileTitle;
	}
	public void setFileTitle(String fileTitle) {
		this.fileTitle = fileTitle;
	}
	public String getTitleOperand() {
		return titleOperand;
	}
	public void setTitleOperand(String titleOperand) {
		this.titleOperand = titleOperand;
	}
	public String getProtocolType() {
		return protocolType;
	}
	public void setProtocolType(String protocolType) {
		this.protocolType = protocolType;
	}
	public String getProtocolName() {
		return protocolName;
	}
	public void setProtocolName(String protocolName) {
		this.protocolName = protocolName;
	}
	public String getNameOperand() {
		return nameOperand;
	}
	public void setNameOperand(String nameOperand) {
		this.nameOperand = nameOperand;
	}
	public String getProtocolAbbreviation() {
		return protocolAbbreviation;
	}
	public void setProtocolAbbreviation(String protocolAbbreviation) {
		this.protocolAbbreviation = protocolAbbreviation;
	}
	public String getAbbreviationOperand() {
		return abbreviationOperand;
	}
	public void setAbbreviationOperand(String abbreviationOperand) {
		this.abbreviationOperand = abbreviationOperand;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}

}

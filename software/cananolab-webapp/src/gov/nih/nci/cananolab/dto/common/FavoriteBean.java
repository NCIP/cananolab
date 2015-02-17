package gov.nih.nci.cananolab.dto.common;

import java.util.Date;

public class FavoriteBean {

	Long id;
	String dataType;
	String dataName;
	String dataId;
	String loginName;
	String pubmedId;
	String protocolFileId;
	boolean editable;
	
	
	public boolean getEditable() {
		return editable;
	}
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getDataName() {
		return dataName;
	}
	public void setDataName(String dataName) {
		this.dataName = dataName;
	}
	public String getDataId() {
		return dataId;
	}
	public void setDataId(String dataId) {
		this.dataId = dataId;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getPubmedId() {
		return pubmedId;
	}
	public void setPubmedId(String pubmedId) {
		this.pubmedId = pubmedId;
	}
	public String getProtocolFileId() {
		return protocolFileId;
	}
	public void setProtocolFileId(String protocolFileId) {
		this.protocolFileId = protocolFileId;
	}
	
}

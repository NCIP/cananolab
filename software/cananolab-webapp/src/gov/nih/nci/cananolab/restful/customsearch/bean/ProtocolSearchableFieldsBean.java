package gov.nih.nci.cananolab.restful.customsearch.bean;

import java.util.Date;

public class ProtocolSearchableFieldsBean {

	private String protocolName;
	private String protocolFileName;
	private String protocolId;
	private String protocolFileDesc;
	private Date createdDate;
	
	public String getProtocolName() {
		return protocolName;
	}
	public void setProtocolName(String protocolName) {
		this.protocolName = protocolName;
	}
	public String getProtocolFileName() {
		return protocolFileName;
	}
	public void setProtocolFileName(String protocolFileName) {
		this.protocolFileName = protocolFileName;
	}
	public String getProtocolId() {
		return protocolId;
	}
	public void setProtocolId(String protocolId) {
		this.protocolId = protocolId;
	}
	public String getProtocolFileDesc() {
		return protocolFileDesc;
	}
	public void setProtocolFileDesc(String protocolFileDesc) {
		this.protocolFileDesc = protocolFileDesc;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
}

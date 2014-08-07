package gov.nih.nci.cananolab.ui.form;

import gov.nih.nci.cananolab.dto.common.ProtocolBean;

public class ProtocolForm {

	String dispatch;
	int page;
	ProtocolBean protocol;
	
	public String getDispatch() {
		return dispatch;
	}
	public void setDispatch(String dispatch) {
		this.dispatch = dispatch;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public ProtocolBean getProtocol() {
		return protocol;
	}
	public void setProtocol(ProtocolBean protocol) {
		this.protocol = protocol;
	}
	
	
}

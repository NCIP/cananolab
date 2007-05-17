/**
 * 
 */
package gov.nih.nci.calab.dto.common;

import gov.nih.nci.calab.domain.ProtocolFile;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;

/**
 * @author zengje
 * 
 */
public class ProtocolFileBean extends LabFileBean {
	private ProtocolBean protocolBean = new ProtocolBean();

	/**
	 * 
	 */
	public ProtocolFileBean() {
		super();
	}

	public ProtocolFileBean(ProtocolFile protocolFile) {
		super(protocolFile, CaNanoLabConstants.PROTOCOL_FILE);
		protocolBean=new ProtocolBean(protocolFile.getProtocol());
	}

	public ProtocolBean getProtocolBean() {
		return protocolBean;
	}

	public void setProtocolBean(ProtocolBean protocolBean) {
		this.protocolBean = protocolBean;
	}
}

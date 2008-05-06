/**
 * 
 */
package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.Protocol;
import gov.nih.nci.cananolab.domain.common.ProtocolFile;

/**
 * Protocol File view bean
 * 
 * @author pansu
 * 
 */
public class ProtocolFileBean extends LabFileBean {
	/**
	 * 
	 */
	public ProtocolFileBean() {
		super();
		domainFile = new ProtocolFile();
		((ProtocolFile) domainFile).setProtocol(new Protocol());
	}

	public ProtocolFileBean(ProtocolFile protocolFile) {
		super(protocolFile);
	}

	public String getDisplayName() {
		return ((ProtocolFile) domainFile).getProtocol().getName() + "-"
				+ domainFile.getVersion();
	}

	public boolean equals(Object obj) {
		boolean eq = false;
		if (obj instanceof ProtocolFileBean) {
			ProtocolFileBean c = (ProtocolFileBean) obj;
			Long thisId = domainFile.getId();
			// String name = this.getName();
			if (thisId != null
					&& thisId.equals(c.getDomainFile().getId())) { // &&
				// name != null && name.equals(c.getName())) {
				eq = true;
			}
		}
		return eq;
	}
}

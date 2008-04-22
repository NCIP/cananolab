/**
 * 
 */
package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.ProtocolFile;

/**
 * Protocol File view bean
 * @author pansu
 * 
 */
public class ProtocolFileBean {
	private String displayName;

	private ProtocolFile domainProtocolFile;

	/**
	 * 
	 */
	public ProtocolFileBean() {
	}

	public ProtocolFileBean(ProtocolFile protocolFile) {
		this.domainProtocolFile = protocolFile;
	}

	public String getDisplayName() {
		displayName = domainProtocolFile.getProtocol().getName() + "-"
				+ domainProtocolFile.getVersion();
		return displayName;
	}

	public ProtocolFile getDomainProtocolFile() {
		return domainProtocolFile;
	}

	public boolean equals(Object obj) {
		boolean eq = false;
		if (obj instanceof ProtocolFileBean) {
			ProtocolFileBean c = (ProtocolFileBean) obj;
			Long thisId = domainProtocolFile.getId();
			// String name = this.getName();
			if (thisId != null
					&& thisId.equals(c.getDomainProtocolFile().getId())) { // &&
				// name != null && name.equals(c.getName())) {
				eq = true;
			}
		}
		return eq;
	}
}

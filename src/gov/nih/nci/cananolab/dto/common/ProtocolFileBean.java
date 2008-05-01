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
	private ProtocolFile domainProtocolFile = new ProtocolFile();

	/**
	 * 
	 */
	public ProtocolFileBean() {
		super();
		domainProtocolFile.setUriExternal(domainFile.getUriExternal());
		domainProtocolFile.setProtocol(new Protocol());
	}

	public ProtocolFileBean(ProtocolFile protocolFile) {
		super(protocolFile);
		this.domainProtocolFile = (ProtocolFile) domainFile;
	}

	public String getDisplayName() {
		return domainProtocolFile.getProtocol().getName() + "-"
				+ domainProtocolFile.getVersion();
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

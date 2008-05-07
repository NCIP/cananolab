/**
 * 
 */
package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.Protocol;
import gov.nih.nci.cananolab.domain.common.ProtocolFile;

import java.util.Date;
import java.util.HashSet;

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

	// for dwr ajax in bodySubmitProtocol.jsp
	public String getDomainFileId() {
		return domainFile.getId().toString();
	}

	// for dwr ajax in bodySubmitProtocol.jsp
	public String getDomainFileVersion() {
		return domainFile.getVersion();
	}

	public boolean equals(Object obj) {
		boolean eq = false;
		if (obj instanceof ProtocolFileBean) {
			ProtocolFileBean c = (ProtocolFileBean) obj;
			Long thisId = domainFile.getId();
			// String name = this.getName();
			if (thisId != null && thisId.equals(c.getDomainFile().getId())) { // &&
				// name != null && name.equals(c.getName())) {
				eq = true;
			}
		}
		return eq;
	}

	public void setupDomainFile(String createdBy) throws Exception {
		super.setupDomainFile(createdBy);
		Protocol protocol = ((ProtocolFile) domainFile).getProtocol();
		if (protocol.getId() == null) {
			protocol.setCreatedBy(createdBy);
			protocol.setCreatedDate(new Date());
		}
		if (protocol.getProtocolFileCollection() == null) {
			protocol.setProtocolFileCollection(new HashSet<ProtocolFile>());
		}
		protocol.getProtocolFileCollection().add((ProtocolFile) domainFile);
		if (((ProtocolFile) domainFile).getProtocol() == null) {
			((ProtocolFile) domainFile).setProtocol(protocol);
		}
	}
}

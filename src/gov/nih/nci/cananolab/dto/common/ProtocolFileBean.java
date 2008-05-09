/**
 * 
 */
package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.Protocol;
import gov.nih.nci.cananolab.domain.common.ProtocolFile;

import java.util.Date;

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
	String domainFileId;

	public ProtocolFileBean() {
		super();
		domainFile = new ProtocolFile();
		((ProtocolFile) domainFile).setProtocol(new Protocol());
		domainFile.setUriExternal(false);
	}

	public ProtocolFileBean(ProtocolFile protocolFile) {
		super(protocolFile);
		domainFileId = protocolFile.getId().toString();
	}

	public String getDisplayName() {
		return ((ProtocolFile) domainFile).getProtocol().getName() + "-"
				+ domainFile.getVersion();
	}

	public String getDomainFileId() {
		return domainFileId;
	}

	public void setDomainFileId(String domainFileId) {
		try {
			Long id = new Long(domainFileId);
			domainFile.setId(id);
		} catch (Exception e) {
			domainFile.setVersion(domainFileId);
		}
		this.domainFileId = domainFileId;
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
//		if (protocol.getProtocolFileCollection() == null) {
//			protocol.setProtocolFileCollection(new HashSet<ProtocolFile>());
//		}
//		protocol.getProtocolFileCollection().add((ProtocolFile) domainFile);
//		if (((ProtocolFile) domainFile).getProtocol() == null) {
//			((ProtocolFile) domainFile).setProtocol(protocol);
//		}
	}
}

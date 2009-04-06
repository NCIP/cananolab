/**
 *
 */
package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.Protocol;

/**
 * Protocol view bean
 *
 * @author pansu
 *
 */
public class ProtocolBean {
	private FileBean fileBean;

	private Protocol domain = new Protocol();
	String domainFileId; // used for ajax

	String domainFileVersion; // used for ajax

	String updatedFileUri; // used for ajax

	String updatedFileName; // used for ajax

	String updatedFileVersion; // used for ajax

	public ProtocolBean() {
		fileBean.getDomainFile().setUriExternal(false);
	}

	public ProtocolBean(Protocol protocol) {
		domain = protocol;
		fileBean = new FileBean(protocol.getFile());
		domainFileId = fileBean.getDomainFile().getId().toString();
	}

	public String getDisplayName() {
		if (domain.getName() != null) {
			return (domain.getName() + "-" + domain.getVersion());
		} else {
			return "";
		}
	}

	public String getDomainFileId() {
		return domainFileId;
	}

	public void setDomainFileId(String domainFileId) {
		this.domainFileId = domainFileId;
	}

	public String getUpdatedFileUri() {
		return updatedFileUri;
	}

	public void setUpdatedFileUri(String domainFileUri) {
		this.updatedFileUri = domainFileUri;
	}

	public String getDomainFileVersion() {
		return domain.getVersion();
	}

	public void setDomainFileVersion(String domainFileVersion) {
		this.domainFileVersion = domainFileVersion;
	}

	public String getUpdatedFileName() {
		return updatedFileName;
	}

	public void setUpdatedFileName(String domainFileName) {
		this.updatedFileName = domainFileName;
	}

	public String getUpdatedFileVersion() {
		return updatedFileVersion;
	}

	public void setUpdatedFileVersion(String updatedFileVersion) {
		this.updatedFileVersion = updatedFileVersion;
	}

	public boolean equals(Object obj) {
		boolean eq = false;
		if (obj instanceof ProtocolBean) {
			ProtocolBean c = (ProtocolBean) obj;
			Long thisId = domain.getFile().getId();
			// String name = this.getName();
			if (thisId != null
					&& thisId.equals(c.getFileBean().getDomainFile().getId())) { // &&
				// name != null && name.equals(c.getName())) {
				eq = true;
			}
		}
		return eq;
	}

	public void setupDomain(String internalUriPath, String createdBy)
			throws Exception {
		// if (updatedFileUri != null && updatedFileUri.trim().length() > 0) {
		// domainFile.setUri(updatedFileUri);
		// }
		// if (updatedFileName != null && updatedFileName.trim().length() > 0) {
		// domainFile.setName(updatedFileName);
		// }
		// if (updatedFileVersion.length() > 0) {
		// domainFile.setVersion(updatedFileVersion);
		// }
		// try {
		// Long id = new Long(domainFileId);
		// domainFile.setId(id);
		// } catch (Exception e) {
		// domainFile.setVersion(domainFileId);
		// }
		// super.setupDomainFile(internalUriPath, createdBy, 0);
		// Protocol protocol = ((ProtocolFile) domainFile).getProtocol();
		// if (protocol.getId() == null) {
		// protocol.setCreatedBy(createdBy);
		// protocol.setCreatedDate(new Date());
		// }
	}

	public FileBean getFileBean() {
		return fileBean;
	}

	public Protocol getDomain() {
		return domain;
	}
}

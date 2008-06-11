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
	String domainFileId; // used for ajax

	String domainFileVersion; // used for ajax

	String updatedFileUri; // used for ajax

	String updatedFileName; // used for ajax

	String updatedFileVersion; // used for ajax

	public ProtocolFileBean() {
		super();
		domainFile = new ProtocolFile();
		((ProtocolFile) domainFile).setProtocol(new Protocol());
		domainFile.setUriExternal(false);
	}

	public ProtocolFileBean(ProtocolFile protocolFile) {
		super(protocolFile);
		domainFile = protocolFile;
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
		this.domainFileId = domainFileId;
	}

	public String getUpdatedFileUri() {
		return updatedFileUri;
	}

	public void setUpdatedFileUri(String domainFileUri) {
		this.updatedFileUri = domainFileUri;
	}

	public String getDomainFileVersion() {
		return domainFile.getVersion();
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

	public void setupDomainFile(String internalUriPath, String createdBy)
			throws Exception {
		if (updatedFileUri!=null && updatedFileUri.trim().length()>0){
			domainFile.setUri(updatedFileUri);
		}
		if (updatedFileName!=null && updatedFileName.trim().length()>0){
			domainFile.setName(updatedFileName);
		}
		if (updatedFileVersion.length() > 0) {
			domainFile.setVersion(updatedFileVersion);
		}
		try {
			Long id = new Long(domainFileId);
			domainFile.setId(id);
		} catch (Exception e) {
			domainFile.setVersion(domainFileId);
		}
		super.setupDomainFile(internalUriPath, createdBy);
		Protocol protocol = ((ProtocolFile) domainFile).getProtocol();
		if (protocol.getId() == null) {
			protocol.setCreatedBy(createdBy);
			protocol.setCreatedDate(new Date());
		}
	}
}

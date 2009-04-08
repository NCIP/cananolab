/**
 *
 */
package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.Protocol;

import java.util.Date;

/**
 * Protocol view bean
 *
 * @author pansu
 *
 */
public class ProtocolBean {
	private FileBean fileBean = new FileBean();

	private Protocol domain = new Protocol();
	String domainFileId; // used for ajax

	String domainFileVersion; // used for ajax

	String updatedFileUri; // used for ajax

	String updatedFileName; // used for ajax

	String location;

	public ProtocolBean() {
		if (fileBean.getDomainFile() != null)
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

	public String getUpdatedFileName() {
		return updatedFileName;
	}

	public void setUpdatedFileName(String domainFileName) {
		this.updatedFileName = domainFileName;
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
		domain.setFile(fileBean.getDomainFile());
		if (updatedFileUri != null && updatedFileUri.trim().length() > 0) {
			domain.getFile().setUri(updatedFileUri);
		}
		if (updatedFileName != null && updatedFileName.trim().length() > 0) {
			domain.getFile().setName(updatedFileName);
		}
		try {
			Long id = new Long(domainFileId);
			domain.getFile().setId(id);
		} catch (Exception e) {

		}
		fileBean.setupDomainFile(internalUriPath, createdBy, 0);
		if (domain.getId() == null) {
			domain.setCreatedBy(createdBy);
			domain.setCreatedDate(new Date());
		}
	}

	public FileBean getFileBean() {
		return fileBean;
	}

	public Protocol getDomain() {
		return domain;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}

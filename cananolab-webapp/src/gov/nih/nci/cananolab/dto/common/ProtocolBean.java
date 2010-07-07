/**
 *
 */
package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.Protocol;
import gov.nih.nci.cananolab.util.StringUtils;

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
	private String[] visibilityGroups = new String[0];

	public ProtocolBean() {
		if (fileBean.getDomainFile() != null)
			fileBean.getDomainFile().setUriExternal(false);
	}

	public ProtocolBean(Protocol protocol) {
		domain = protocol;
		if (protocol.getFile() != null) {
			fileBean = new FileBean(protocol.getFile());
		} else {
			fileBean = new FileBean();
		}
	}

	public String getDisplayName() {
		String displayName = "";
		if (!StringUtils.isEmpty(domain.getName())) {
			displayName += domain.getName();
			if (!StringUtils.isEmpty(domain.getAbbreviation())) {
				displayName += " (" + domain.getAbbreviation() + ")";
			}
			displayName += ", version " + domain.getVersion();
		}
		return displayName;
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
		fileBean.setupDomainFile(internalUriPath, createdBy);
		if (StringUtils.isEmpty(fileBean.getDomainFile().getUri())
				&& StringUtils.isEmpty(fileBean.getDomainFile().getTitle())
				&& StringUtils.isEmpty(fileBean.getDomainFile()
						.getDescription())) {
			fileBean = null;
			domain.setFile(null);
		} else {
			domain.setFile(fileBean.getDomainFile());
		}
		if (domain.getId() == 0) {
			domain.setId(null);
		}
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

	public String[] getVisibilityGroups() {
		return visibilityGroups;
	}

	public void setVisibilityGroups(String[] visibilityGroups) {
		this.visibilityGroups = visibilityGroups;
	}
}

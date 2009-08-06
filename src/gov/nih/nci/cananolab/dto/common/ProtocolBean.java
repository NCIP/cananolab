/**
 *
 */
package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.Protocol;

import java.util.Date;

import org.apache.axis.utils.StringUtils;

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
	private String location;

	public ProtocolBean() {
		if (fileBean.getDomainFile() != null)
			fileBean.getDomainFile().setUriExternal(false);
	}

	public ProtocolBean(Protocol protocol) {
		domain = protocol;
		fileBean = new FileBean(protocol.getFile());
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
		domain.setFile(fileBean.getDomainFile());
		fileBean.setupDomainFile(internalUriPath, createdBy, 0);
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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String[] getVisibilityGroups() {
		return visibilityGroups;
	}

	public void setVisibilityGroups(String[] visibilityGroups) {
		this.visibilityGroups = visibilityGroups;
	}
}

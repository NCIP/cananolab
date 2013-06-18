/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

/**
 *
 */
package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.Protocol;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.DateUtils;
import gov.nih.nci.cananolab.util.SortableName;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.Date;

/**
 * Protocol view bean
 *
 * @author pansu
 *
 */
public class ProtocolBean extends SecuredDataBean {
	private FileBean fileBean = new FileBean();
	private Protocol domain = new Protocol();
	private String[] visibilityGroups = new String[0];
	private String createdDateStr;
	private String downloadUrl;

	public ProtocolBean() {
		if (fileBean.getDomainFile() != null)
			fileBean.getDomainFile().setUriExternal(false);
	}

	public ProtocolBean(Protocol protocol) {
		domain = protocol;
		this.createdBy = protocol.getCreatedBy();
		if (protocol.getFile() != null) {
			fileBean = new FileBean(protocol.getFile());
		} else {
			fileBean = new FileBean();
		}
	}
	
	public String getCreatedDateStr() {
		createdDateStr = DateUtils.convertDateToString(domain.getCreatedDate(), Constants.DATE_FORMAT);
		return createdDateStr;
	}

	public String getDownloadUrl(){
		SortableName sortableLink=null;
		
		if (fileBean != null) {
			String fileName = fileBean.getDomainFile().getName();
			if (!StringUtils.isEmpty(fileName)) {
				StringBuilder sb = new StringBuilder("<a href=");
				sb.append("protocol.do?dispatch=download&fileId=");
				sb.append(fileBean.getDomainFile().getId()).append(">");
				String fileTitle = fileBean.getDomainFile().getTitle();
				if (StringUtils.isEmpty(fileTitle)) {
					fileTitle = fileBean.getDomainFile().getUri();
				}
				sb.append(fileTitle);
				sb.append("</a>");
				sortableLink = new SortableName(fileName, sb.toString());
			} else {
				String fileTitle = fileBean.getDomainFile().getTitle();
				if (!StringUtils.isEmpty(fileTitle)) {
					sortableLink = new SortableName(fileTitle);
				} else {
					sortableLink = new SortableName("");
				}
			}
		} else {
			sortableLink = new SortableName("");
		}
		return sortableLink.toString();
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
			if (thisId != null
					&& thisId.equals(c.getFileBean().getDomainFile().getId())) {
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

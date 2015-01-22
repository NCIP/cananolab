package gov.nih.nci.cananolab.restful.view;

import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.common.ProtocolBean;
import gov.nih.nci.cananolab.restful.util.SecurityUtil;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SimpleSearchProtocolBean {

	String type;
	String viewName;
	String abbreviation;
	String version;
	Date createdDate;
	String fileInfo;
	long id;
	boolean editable = false;
	boolean userDeletable = false;
	
	public boolean getUserDeletable() {
		return userDeletable;
	}

	public void setUserDeletable(boolean userDeletable) {
		this.userDeletable = userDeletable;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public boolean isEditable() {
		return editable;
	}
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getViewName() {
		return viewName;
	}
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}
	public String getAbbreviation() {
		return abbreviation;
	}
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getFileInfo() {
		return fileInfo;
	}
	public void setFileInfo(String fileInfo) {
		this.fileInfo = fileInfo;
	}
	public void transferProtocolBeanForBasicResultView(ProtocolBean bean,
			UserBean user) {
		
		setType(bean.getDomain().getType());
		setViewName(bean.getDomain().getName());
		setAbbreviation(bean.getDomain().getAbbreviation());
		setVersion(bean.getDomain().getVersion());
		String fileInformation = fileInfo(bean);
		setFileInfo(fileInformation);
		setCreatedDate(bean.getDomain().getCreatedDate());
		setUserDeletable(bean.getUserDeletable());
		setEditable(bean.getUserUpdatable()); //SecurityUtil.isEntityEditableForUser(bean.getAllAccesses(), user);
		id = bean.getDomain().getId();
	}
	private String fileInfo(ProtocolBean protocol) {
		// TODO Auto-generated method stub
		FileBean file = protocol.getFileBean();
		StringBuilder sb = new StringBuilder();
		if (file != null && file.getDomainFile() != null) {
			// file uri is null, not a valid file return empty string
			if (StringUtils.isEmpty(file.getDomainFile().getUri())) {
				return "";
			}
			if (!StringUtils.isEmpty(file.getDomainFile().getTitle())) {
				sb.append("Title:").append("<br>")
						.append(file.getDomainFile().getTitle()).append("<br>");
			}
			String description = protocol.getDomain().getFile()
					.getDescription();
			if (!StringUtils.isEmpty(description)) {
				sb.append("<br>")
						.append("Description:")
						.append("<br>")
						.append(StringUtils
								.escapeXmlButPreserveLineBreaks(description)).append("<br>");
			}
			if (file.getDomainFile().getId() != null) {
				String link = "rest/protocol/download?fileId="
						+ file.getDomainFile().getId();
				String linkText = "View";
				sb.append("<br>").append("<a href=");
				sb.append(link).append(" target='new'>");
				sb.append(linkText);
				sb.append("</a>");
			}
		}
		return sb.toString();
	}
	
}

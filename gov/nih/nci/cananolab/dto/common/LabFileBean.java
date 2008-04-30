package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.Keyword;
import gov.nih.nci.cananolab.domain.common.LabFile;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;
import gov.nih.nci.cananolab.util.PropertyReader;
import gov.nih.nci.cananolab.util.StringUtils;

import java.io.File;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * This class represents attributes of a lab file to be viewed in a view page.
 * 
 * @author pansu
 * 
 */
public class LabFileBean {
	protected LabFile domainFile = new LabFile();

	private String[] visibilityGroups = new String[0];

	private String visibilityStr;

	private byte[] fileContent;

	/*
	 * name to be displayed as a part of the drop-down list
	 */
	private String displayName;

	private String timeStampedName;

	private boolean hidden = false;

	private boolean image = false;

	private String keywordsStr;

	private boolean external = false;

	private String fullPath;

	public LabFileBean() {
	}

	public LabFileBean(LabFile labFile) {
		this.domainFile = labFile;
		SortedSet<String> keywordStrs = new TreeSet<String>();
		if (domainFile.getKeywordCollection() != null) {
			for (Keyword keyword : domainFile.getKeywordCollection()) {
				keywordStrs.add(keyword.getName());
			}
		}
		keywordsStr = StringUtils.join(keywordStrs, "\r\n");
	}

	public String[] getVisibilityGroups() {
		return this.visibilityGroups;
	}

	public void setVisibilityGroups(String[] visibilityGroups) {
		this.visibilityGroups = visibilityGroups;
	}

	public String getDisplayName() {
		if (this.domainFile.getUri() != null) {
			this.displayName = this.domainFile.getUri().replaceAll(
					"/decompressedFiles", "");
		} else {
			this.displayName = "";
		}
		return this.displayName;
	}

	public String getVisibilityStr() {
		this.visibilityStr = StringUtils.join(this.visibilityGroups, "<br>");
		return this.visibilityStr;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public void setVisibilityStr(String visibilityStr) {
		this.visibilityStr = visibilityStr;
	}

	public byte[] getFileContent() {
		return this.fileContent;
	}

	public void setFileContent(byte[] fileContent) {
		this.fileContent = fileContent;
	}

	public String getTimeStampedName() {
		return this.timeStampedName;
	}

	public void setTimeStampedName(String timeStampedName) {
		this.timeStampedName = timeStampedName;
	}

	public boolean isHidden() {
		return this.hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public boolean isImage() {
		// if file is external don't show image either
		if (getDomainFile().getName() != null && !isExternal()) {
			image = StringUtils.isImgFileExt(getDomainFile().getName());
		}
		return image;
	}

	public LabFile getDomainFile() {
		return domainFile;
	}

	public String getKeywordsStr() {
		return keywordsStr;
	}

	public void setKeywordsStr(String keywordsStr) {
		this.keywordsStr = keywordsStr;
	}

	public boolean isExternal() {
		if (getDomainFile().getUri() != null
				&& getDomainFile().getUri().startsWith("http")) {
			external = true;
		} else {
			external = false;
		}
		return external;
	}

	public String getFullPath() {
		if (!isExternal()) {
			String fileRoot = PropertyReader
					.getProperty(CaNanoLabConstants.FILEUPLOAD_PROPERTY,
							"fileRepositoryDir");
			fullPath = fileRoot + File.separator + getDomainFile().getUri();
		} else {
			fullPath = null;
		}
		return fullPath;
	}
}

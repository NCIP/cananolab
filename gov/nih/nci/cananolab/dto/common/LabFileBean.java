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

	private boolean hidden = false;

	private boolean image = false;

	private String keywordsStr;

	private String externalUrl;

	private String uploadedFile;
	
	public String getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(String uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public LabFileBean() {
		domainFile.setUriExternal(false);
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
		if (labFile.getUriExternal()) {
			externalUrl = labFile.getUri();
		}
	}

	public String[] getVisibilityGroups() {
		return this.visibilityGroups;
	}

	public void setVisibilityGroups(String[] visibilityGroups) {
		this.visibilityGroups = visibilityGroups;
	}

	public String getDisplayName() {
		return getDomainFile().getTitle();
	}

	public String getVisibilityStr() {
		this.visibilityStr = StringUtils.join(this.visibilityGroups, "<br>");
		return this.visibilityStr;
	}

	public void setVisibilityStr(String visibilityStr) {
		this.visibilityStr = visibilityStr;
	}

	public boolean isHidden() {
		return this.hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public boolean isImage() {
		image = StringUtils.isImgFileExt(getDomainFile().getUri());
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

	public String getFullPath() {
		if (!getDomainFile().getUriExternal()) {
			String fileRoot = PropertyReader
					.getProperty(CaNanoLabConstants.FILEUPLOAD_PROPERTY,
							"fileRepositoryDir");
			return fileRoot + File.separator + getDomainFile().getUri();
		} else {
			return getDomainFile().getUri();
		}
	}

	public String getExternalUrl() {
		return externalUrl;
	}

	public void setExternalUrl(String externalUrl) {
		this.externalUrl = externalUrl;
	}

	public String getUrlTarget() {
		if (getDomainFile().getUriExternal()) {
			return "pop";
		}
		return "_self";
	}
}

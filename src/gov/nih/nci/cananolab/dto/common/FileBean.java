package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.common.Keyword;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.DateUtils;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.Date;
import java.util.HashSet;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.struts.upload.FormFile;

/**
 * This class represents attributes of a lab file to be viewed in a view page.
 * 
 * @author pansu
 * 
 */
public class FileBean {
	protected File domainFile = new File();

	private String[] visibilityGroups = new String[0];

	private String visibilityStr;

	private boolean image = false;

	private String keywordsStr;

	private String externalUrl; // url as an external link

	private FormFile uploadedFile;

	private byte[] newFileData; // data from uploadedFile if upload happened

	private String location; // e.g. local, caNanoLab-WashU

	private String createdDateStr;

	public FileBean() {
		domainFile.setUriExternal(false);
	}

	public FileBean(File file) {
		this.domainFile = file;
		if (file != null) {
			SortedSet<String> keywordStrs = new TreeSet<String>();
			if (domainFile.getKeywordCollection() != null) {
				for (Keyword keyword : domainFile.getKeywordCollection()) {
					keywordStrs.add(keyword.getName());
				}
			}
			keywordsStr = StringUtils.join(keywordStrs, "\r\n");
			if (file.getUriExternal() != null && file.getUriExternal()) {
				externalUrl = file.getUri();
			}
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

	public boolean isImage() {
		image = StringUtils.isImgFileExt(getDomainFile().getUri());
		return image;
	}

	public File getDomainFile() {
		return domainFile;
	}

	public String getKeywordsStr() {
		return keywordsStr;
	}

	public void setKeywordsStr(String keywordsStr) {
		this.keywordsStr = keywordsStr;
	}

	public String getExternalUrl() {
		return externalUrl;
	}

	public void setExternalUrl(String externalUrl) {
		this.externalUrl = externalUrl;
	}

	public String getUrlTarget() {
		if (domainFile.getUriExternal() != null && domainFile.getUriExternal()) {
			return "pop";
		}
		return "_self";
	}

	public FormFile getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(FormFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public void setupDomainFile(String internalUriPath, String createdBy,
			int index) throws Exception {
		if (domainFile.getId() != null && domainFile.getId() == 0) {
			domainFile.setId(null);
		}
		if (domainFile.getId() == null
				|| !StringUtils.isEmpty(domainFile.getCreatedBy())
				&& domainFile.getCreatedBy().equals(
						Constants.AUTO_COPY_ANNOTATION_PREFIX)) {
			domainFile.setCreatedBy(createdBy);
			// domainFile.setCreatedDate(new Date());
			// fix for MySQL database, which supports precision only up to
			// seconds
			domainFile.setCreatedDate(DateUtils.addSecondsToCurrentDate(index));
		}
		if (uploadedFile != null
				&& !StringUtils.isEmpty(uploadedFile.getFileName())) {
			domainFile.setName(uploadedFile.getFileName());
			newFileData = uploadedFile.getFileData();
		} else {
			newFileData = null;
		}
		// if entered external url
		if (domainFile.getUriExternal() && !StringUtils.isEmpty(externalUrl)) {
			domainFile.setUri(externalUrl);
			domainFile.setName(externalUrl);
		} else {
			String timestamp = DateUtils.convertDateToString(new Date(),
					"yyyyMMdd_HH-mm-ss-SSS");
			// if uploaded new file
			if (newFileData != null) {
				domainFile.setUri(internalUriPath + "/" + timestamp + "_"
						+ domainFile.getName());
			}
		}

		if (!StringUtils.isEmpty(keywordsStr)) {
			if (domainFile.getKeywordCollection() != null) {
				domainFile.getKeywordCollection().clear();
			} else {
				domainFile.setKeywordCollection(new HashSet<Keyword>());
			}
			String[] strs = keywordsStr.split("\r\n");
			for (String str : strs) {
				// change to upper case
				Keyword keyword = new Keyword();
				keyword.setName(str.toUpperCase());
				domainFile.getKeywordCollection().add(keyword);
			}
		}
	}

	public byte[] getNewFileData() {
		return newFileData;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setDomainFile(File domainFile) {
		this.domainFile = domainFile;
	}

	public String getCreatedDateStr() {
		createdDateStr = DateUtils.convertDateToString(domainFile
				.getCreatedDate(), Constants.DATE_FORMAT);
		return createdDateStr;
	}

	public FileBean copy() {
		FileBean copy = new FileBean();
		copy.getDomainFile().setType(domainFile.getType());
		copy.getDomainFile().setTitle(domainFile.getTitle());
		copy.getDomainFile().setDescription(domainFile.getDescription());
		copy.getDomainFile().setUriExternal(domainFile.getUriExternal());
		copy.setKeywordsStr(keywordsStr);
		copy.setVisibilityGroups(visibilityGroups);
		copy.setUploadedFile(uploadedFile);
		copy.setExternalUrl(externalUrl);
		copy.getDomainFile().setId(domainFile.getId());
		copy.getDomainFile().setUri(domainFile.getUri());
		return copy;
	}

	public boolean equals(Object obj) {
		boolean eq = false;
		if (obj instanceof FileBean) {
			FileBean f = (FileBean) obj;
			Long thisId = getDomainFile().getId();
			if (thisId != null && thisId.equals(f.getDomainFile().getId())) {
				eq = true;
			}
		}
		return eq;
	}

	public void setNewFileData(byte[] newFileData) {
		this.newFileData = newFileData;
	}
}

package gov.nih.nci.cananolab.dto.common;

import java.util.SortedSet;
import java.util.TreeSet;

import gov.nih.nci.cananolab.domain.common.Keyword;
import gov.nih.nci.cananolab.domain.common.LabFile;
import gov.nih.nci.cananolab.util.StringUtils;

/**
 * This class represents attributes of a lab file to be viewed in a view page.
 * 
 * @author pansu
 * 
 */
public class LabFileBean {
	private LabFile domainFile=new LabFile();
	
	private String[] visibilityGroups = new String[0];

	private String visibilityStr;

	private String gridNode;

	private byte[] fileContent;

	/*
	 * name to be displayed as a part of the drop-down list
	 */
	private String displayName;

	private String instanceType; // type of instance, protocol, output,
	// report, associatedFile, etc

	private String timeStampedName;

	private boolean hidden=true;

	private boolean image=false;
	
	private String uploadedFile;
	
	private String keywordsStr;

	public String getInstanceType() {
		return this.instanceType;
	}

	public void setInstanceType(String instanceType) {
		this.instanceType = instanceType;
	}

	public LabFileBean() {
	}

	public LabFileBean(LabFile labFile) {
		this.domainFile=labFile;
		SortedSet<String> keywordStrs = new TreeSet<String>();
		if (domainFile.getKeywordCollection() != null) {
			for (Keyword keyword : domainFile.getKeywordCollection()) {
				keywordStrs.add(keyword.getName());
			}
		}
		keywordsStr = StringUtils.join(keywordStrs, "\r\n");
	}

	public LabFileBean(LabFile charFile, String gridNodeHost) {
		this(charFile);
		this.gridNode = gridNodeHost;
	}

	
	public String[] getVisibilityGroups() {
		return this.visibilityGroups;
	}

	public void setVisibilityGroups(String[] visibilityGroups) {
		this.visibilityGroups = visibilityGroups;
	}

	public String getDisplayName() {
		if (this.domainFile.getUri() != null) {
			this.displayName = this.domainFile.getUri().replaceAll("/decompressedFiles", "");
		} else {
			this.displayName = "";
		}
		return this.displayName;
	}

	public String getVisibilityStr() {
		this.visibilityStr = StringUtils.join(this.visibilityGroups, "<br>");
		return this.visibilityStr;
	}

	public String getGridNode() {
		return this.gridNode;
	}

	public void setGridNode(String gridNode) {
		this.gridNode = gridNode;
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
		return image;
	}

	public void setImage(boolean image) {
		this.image = image;
	}

	public LabFile getDomainFile() {
		return domainFile;
	}

	public String getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(String uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public String getKeywordsStr() {
		return keywordsStr;
	}

	public void setKeywordsStr(String keywordsStr) {
		this.keywordsStr = keywordsStr;
	}
}

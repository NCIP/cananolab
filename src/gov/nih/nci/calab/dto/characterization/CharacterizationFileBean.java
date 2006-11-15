package gov.nih.nci.calab.dto.characterization;

import gov.nih.nci.calab.domain.DerivedDataFile;
import gov.nih.nci.calab.domain.LabFile;

import java.util.Date;

/**
 * This class represents attributes of a characteriztaion table file to be
 * viewed in a view page.
 * 
 * @author pansu
 * 
 */
public class CharacterizationFileBean {
	private String title;

	private String description;

	private String comments;

	private String[] keywords;

	private String[] visibilityGroups;

	private Date createdDate;

	private String createdBy;

	private String id;

	private String path;

	private String name;
	
	private String fileId;

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public CharacterizationFileBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String[] getKeywords() {
		return keywords;
	}

	public void setKeywords(String[] keywords) {
		this.keywords = keywords;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String[] getVisibilityGroups() {
		return visibilityGroups;
	}

	public void setVisibilityGroups(String[] visibilityGroups) {
		this.visibilityGroups = visibilityGroups;
	}

	public DerivedDataFile getDomainObject(){
		DerivedDataFile labfile = new DerivedDataFile();
		if (getFileId() != null && getFileId().length() > 0) {
			labfile.setId(new Long(getFileId()));
		}
		labfile.setCreatedBy(createdBy);
		labfile.setCreatedDate(createdDate);
		labfile.setDescription(description);
		labfile.setFilename(name);
		labfile.setPath(path);
		labfile.setTitle(title);
		
		return labfile;
	}

}

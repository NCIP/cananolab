package gov.nih.nci.calab.dto.common;

import gov.nih.nci.calab.domain.DerivedDataFile;
import gov.nih.nci.calab.domain.Keyword;
import gov.nih.nci.calab.domain.LabFile;
import gov.nih.nci.calab.service.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * This class represents attributes of a lab file to be
 * viewed in a view page.
 * 
 * @author pansu
 * 
 */
public class LabFileBean {
	private String title;

	private String description;

	private String comments;

	private String[] keywords = new String[0];

	private String[] visibilityGroups;

	private Date createdDate;

	private String createdBy;

	private String id;

	private String path;

	private String name;
	
	private String type;
	
	private String keywordsStr;
	
	private String visibilityStr;
	
	/*
	 * name to be displayed as a part of the drop-down list
	 */
	private String displayName;

	public LabFileBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LabFileBean(LabFile charFile) {
		this.id=charFile.getId().toString();
		this.name=charFile.getFilename();
		this.path=charFile.getPath();
		this.title=charFile.getTitle();
		this.description=charFile.getDescription();
	    this.createdBy=charFile.getCreatedBy();
	    this.createdDate=charFile.getCreatedDate();
	    if (charFile instanceof DerivedDataFile){
	    	List<String> allkeywords = new ArrayList<String>();
	    	for (Keyword keyword: ((DerivedDataFile)charFile).getKeywordCollection()) {
	    		allkeywords.add(keyword.getName());
	    	}
	        allkeywords.toArray(this.keywords);
	    }
	    
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
		if (id != null && id.length() > 0) {
			labfile.setId(new Long(id));
		}
		labfile.setCreatedBy(createdBy);
		labfile.setCreatedDate(createdDate);
		labfile.setDescription(description);
		labfile.setFilename(name);
		labfile.setPath(path);
		labfile.setTitle(title);
		
		return labfile;
	}

	public String getDisplayName() {
		displayName=path.replaceAll("/decompressedFiles", "");
		return displayName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getKeywordsStr() {
		keywordsStr=StringUtils.join(keywords, "<br>");
		return keywordsStr;
	}

	public String getVisibilityStr() {
		visibilityStr=StringUtils.join(visibilityGroups, ", ");	
		return visibilityStr;
	}
}

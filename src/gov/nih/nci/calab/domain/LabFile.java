package gov.nih.nci.calab.domain;

/**
 * <!-- LICENSE_TEXT_START -->
 * <!-- LICENSE_TEXT_END -->
 */

/**
 * 
 */

public class LabFile implements java.io.Serializable {
	private static final long serialVersionUID = 1234567890L;

	private java.lang.Long id;

	private String type;

	private String comments;

	private String title;

	private String description;

	private String extension;

	private java.lang.String createdBy;

	private java.util.Date createdDate;

	private java.lang.String version;

	private java.lang.String uri;

	private String filename;

	private gov.nih.nci.calab.domain.DataStatus dataStatus;

	public java.lang.Long getId() {
		return id;
	}

	public void setId(java.lang.Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public java.lang.String getExtension() {
		return extension;
	}

	public void setExtension(java.lang.String extension) {
		this.extension = extension;
	}

	public java.lang.String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(java.lang.String createdBy) {
		this.createdBy = createdBy;
	}

	public java.util.Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(java.util.Date createdDate) {
		this.createdDate = createdDate;
	}

	public java.lang.String getVersion() {
		return version;
	}

	public void setVersion(java.lang.String version) {
		this.version = version;
	}

	public java.lang.String getUri() {
		return uri;
	}

	public void setUri(java.lang.String uri) {
		this.uri = uri;
	}

	public String getFilename() {
		return this.filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public gov.nih.nci.calab.domain.DataStatus getDataStatus() {
		return this.dataStatus;
	}

	public void setDataStatus(DataStatus dataStatus) {
		this.dataStatus = dataStatus;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean equals(Object obj) {
		boolean eq = false;
		if (obj instanceof InputFile) {
			InputFile c = (InputFile) obj;
			Long thisId = getId();

			if (thisId != null && thisId.equals(c.getId())) {
				eq = true;
			}

		}
		return eq;
	}

	public int hashCode() {
		int h = 0;

		if (getId() != null) {
			h += getId().hashCode();
		}

		return h;
	}

}
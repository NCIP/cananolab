package gov.nih.nci.cananolab.util;


import java.util.Date;

/**
 * This class represents a data link under the particle tree
 * 
 * @author pansu
 * 
 */
public class DataLinkBean {
	// the data type displayed in the tree, e.g. Carbon Nanotube
	private String dataDisplayType;

	// e.g. #1: Dendrimer
	private String viewTitle;

	// the short class name representing the data, e.g. CarbonNanotube
	private String dataClassName;

	// the id for the persisted data
	private String dataId;

	// e.g. physicalCharacterization
	private String dataLink;

	// Composition, Physical Characterization, In Vitro Characterization
	private String dataCategory;

	private Date createdDate;

	private String createdBy;

	// used for link color on the side menu
	private String viewColor;

	public DataLinkBean(String dataDisplayType, String dataClassName,
			String dataLink, String dataCategory) {
		this.dataDisplayType = dataDisplayType;
		this.dataClassName = dataClassName;
		this.dataLink = dataLink;
		this.dataCategory = dataCategory;
	}

	public DataLinkBean(String dataId, String category, String dataLink) {
		this.dataId = dataId;
		this.dataCategory = category;
		this.dataLink = dataLink;
	}

	public DataLinkBean(String dataId, String category,
			String dataLink, String createdBy, Date createdDate) {
		this(dataId, category, dataLink);
		this.createdBy = createdBy;
		this.createdDate = createdDate;
	}

	public String getDataLink() {
		return dataLink;
	}

	public void setDataLink(String dataLink) {
		this.dataLink = dataLink;
	}

	public String getDataClassName() {
		return dataClassName;
	}

	public void setDataClassName(String dataClassName) {
		this.dataClassName = dataClassName;
	}

	public String getDataDisplayType() {
		return dataDisplayType;
	}

	public void setDataDisplayType(String dataDisplayType) {
		this.dataDisplayType = dataDisplayType;
	}

	public String getDataId() {
		return dataId;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	public String getDataCategory() {
		return dataCategory;
	}

	public void setDataCategory(String dataCategory) {
		this.dataCategory = dataCategory;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getViewColor() {
		if (createdBy.equals(Constants.AUTO_COPY_ANNOTATION_PREFIX)
				|| this.viewTitle != null
				&& this.viewTitle.matches("^copy_\\d{15}?")) {
			this.viewColor = Constants.AUTO_COPY_ANNNOTATION_VIEW_COLOR;
		}
		return this.viewColor;
	}

	public String getViewTitle() {
		return viewTitle;
	}

	public void setViewTitle(String viewTitle) {
		this.viewTitle = viewTitle;
	}
}

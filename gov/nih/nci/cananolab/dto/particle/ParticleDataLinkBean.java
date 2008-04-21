package gov.nih.nci.cananolab.dto.particle;

/**
 * This class represents a data link under the particle tree
 * @author pansu
 *
 */
public class ParticleDataLinkBean {
	//the data type displayed in the tree, e.g. Carbon Nanotube
	private String dataDisplayType;

	//the short class name representing the data, e.g. CarbonNanotube
	private String dataClassName;

	//the id for the persisted data
	private String dataId;

	private String dataLink;

	//Composition, Physical Characterization, In Vitro Characterization
	private String dataCategory;

	public ParticleDataLinkBean(String dataDisplayType, String dataClassName,
			String dataLink, String dataCategory) {
		this.dataDisplayType = dataDisplayType;
		this.dataClassName = dataClassName;
		this.dataLink = dataLink;
		this.dataCategory = dataCategory;
	}

	public ParticleDataLinkBean(String dataId, String category, String dataLink) {
		this.dataId=dataId;
		this.dataCategory = category;
		this.dataLink = dataLink;
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

}

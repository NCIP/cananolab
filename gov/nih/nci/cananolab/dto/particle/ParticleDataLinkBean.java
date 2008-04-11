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

	private String dataActionName;

	private String dataCategory;

	public ParticleDataLinkBean(String category) {
		dataCategory = category;
	}

	public String getDataActionName() {
		return dataActionName;
	}

	public void setDataActionName(String dataActionName) {
		this.dataActionName = dataActionName;
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

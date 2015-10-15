package gov.nih.nci.cananolab.restful.bean;

import gov.nih.nci.cananolab.restful.view.SimpleCharacterizationsByTypeBean;

import java.util.List;

public class SimpleAdvancedResultCellUnitBean {
	
	long parentSampleId;
	long dataId;
	
	String displayName;
	String relatedEntityType;
	long relatedEntityId;
	

	public long getParentSampleId() {
		return parentSampleId;
	}

	public void setParentSampleId(long parentSampleId) {
		this.parentSampleId = parentSampleId;
	}

	public long getDataId() {
		return dataId;
	}

	public void setDataId(long dataId) {
		this.dataId = dataId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getRelatedEntityType() {
		return relatedEntityType;
	}

	public void setRelatedEntityType(String relatedEntityType) {
		this.relatedEntityType = relatedEntityType;
	}

	public long getRelatedEntityId() {
		return relatedEntityId;
	}

	public void setRelatedEntityId(long relatedEntityId) {
		this.relatedEntityId = relatedEntityId;
	}
}

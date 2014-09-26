package gov.nih.nci.cananolab.restful.bean.characterization;

import gov.nih.nci.cananolab.restful.view.SimpleCharacterizationsByTypeBean;

import java.util.List;

public class SimpleCharacterizationAdvancedSearchResultBean {
	
	long parentSampleId;
	long charId;
	
	String displayName;

	public long getParentSampleId() {
		return parentSampleId;
	}

	public void setParentSampleId(long parentSampleId) {
		this.parentSampleId = parentSampleId;
	}

	public long getCharId() {
		return charId;
	}

	public void setCharId(long charId) {
		this.charId = charId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
}

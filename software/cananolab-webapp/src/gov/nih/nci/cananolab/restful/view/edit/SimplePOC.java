package gov.nih.nci.cananolab.restful.view.edit;

import gov.nih.nci.cananolab.dto.common.PointOfContactBean;

public class SimplePOC {
	long id;
	String displayName;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public void transferFromPointOfContactBean(PointOfContactBean pocBean) {
		id = pocBean.getDomain().getId();
		displayName = pocBean.getDisplayName();
	}
}

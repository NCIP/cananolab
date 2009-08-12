package gov.nih.nci.cananolab.dto.particle;

import gov.nih.nci.cananolab.dto.BaseQueryBean;

/**
 * Information for the sample query form
 * 
 * @author pansu
 * 
 */
public class SampleQueryBean extends BaseQueryBean {
	private String nameType;
	private String name;
	public String getNameType() {
		return nameType;
	}
	public void setNameType(String nameType) {
		this.nameType = nameType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}

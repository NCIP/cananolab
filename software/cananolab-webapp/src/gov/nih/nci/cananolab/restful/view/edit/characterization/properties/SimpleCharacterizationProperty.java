package gov.nih.nci.cananolab.restful.view.edit.characterization.properties;

import javax.servlet.http.HttpServletRequest;

public abstract class SimpleCharacterizationProperty {
	
	String propertyName;
	String propertyDisplayName;
	
	public String getPropertyName() {
		return propertyName;
	}
	
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	
	public String getPropertyDisplayName() {
		return propertyDisplayName;
	}
	public void setPropertyDisplayName(String propertyDisplayName) {
		this.propertyDisplayName = propertyDisplayName;
	}
	
	public abstract void setLookups(HttpServletRequest request) throws Exception;
}

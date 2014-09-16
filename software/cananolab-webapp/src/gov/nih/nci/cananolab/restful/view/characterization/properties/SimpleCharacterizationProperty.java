package gov.nih.nci.cananolab.restful.view.characterization.properties;

import java.util.List;

import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.cananolab.util.StringUtils;

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
	
	public void transferFromPropertyBean(HttpServletRequest request, CharacterizationBean charBean, boolean needOptions) 
			throws Exception {
		this.propertyName = ClassUtils.getShortClassNameFromDisplayName(charBean.getCharacterizationName());
		this.propertyDisplayName = StringUtils.getCamelCaseFormatInWords(this.propertyName);
	}
	
	public abstract void transferToPropertyBean(CharacterizationBean charBean) throws Exception;
	public abstract List<String> getPropertyViewTitles();
	public abstract List<String> getPropertyViewValues();
}

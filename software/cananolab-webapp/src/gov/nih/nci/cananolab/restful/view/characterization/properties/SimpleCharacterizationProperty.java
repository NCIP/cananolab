package gov.nih.nci.cananolab.restful.view.characterization.properties;

import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;

@JsonTypeInfo(
use = JsonTypeInfo.Id.NAME,
include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
@JsonSubTypes.Type(value = SimpleCytotoxicity.class, name = "SimpleCytotoxicity"),
@JsonSubTypes.Type(value = SimpleEnzymeInduction.class, name = "SimpleEnzymeInduction"),
@JsonSubTypes.Type(value = SimplePhysicalState.class, name = "SimplePhysicalState"),
@JsonSubTypes.Type(value = SimpleShape.class, name = "SimpleShape"),
@JsonSubTypes.Type(value = SimpleSolubility.class, name = "SimpleSolubility"),
@JsonSubTypes.Type(value = SimpleSurface.class, name = "SimpleSurface"),
@JsonSubTypes.Type(value = SimpleTransfection.class, name = "SimpleTransfection")
})
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

package gov.nih.nci.cananolab.dto.search;

import gov.nih.nci.cananolab.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Information for the characterization query form
 * 
 * @author pansu
 * 
 */
public class CharacterizationQueryBean extends BaseQueryBean {
	private String characterizationType = "";
	private String characterizationName = "";
	private String assayType = "";
	private Boolean datumValueBoolean = false;
	private String datumName = "";
	private String datumValue = "";
	private String datumValueUnit = "";

	public String getCharacterizationType() {
		return characterizationType;
	}

	public void setCharacterizationType(String characterizationType) {
		this.characterizationType = characterizationType;
	}

	public String getCharacterizationName() {
		return characterizationName;
	}

	public void setCharacterizationName(String characterizationName) {
		this.characterizationName = characterizationName;
	}

	public String getAssayType() {
		return assayType;
	}

	public void setAssayType(String assayType) {
		this.assayType = assayType;
	}

	public String getDatumName() {
		return datumName;
	}

	public void setDatumName(String datumName) {
		this.datumName = datumName;
	}

	public String getDatumValue() {
		return datumValue;
	}

	public void setDatumValue(String datumValue) {
		this.datumValue = datumValue.trim();
	}

	public String getDatumValueUnit() {
		return datumValueUnit;
	}

	public void setDatumValueUnit(String datumValueUnit) {
		this.datumValueUnit = datumValueUnit;
	}

	public Boolean getDatumValueBoolean() {
		return datumValueBoolean;
	}

	public void setDatumValueBoolean(Boolean datumValueBoolean) {
		this.datumValueBoolean = datumValueBoolean;
	}

	public String getDisplayName() {
		List<String> strs = new ArrayList<String>();
		strs.add(characterizationType);
		if (!StringUtils.isEmpty(assayType)) {
			strs.add(characterizationName + ":" + assayType);
		} else {
			strs.add(characterizationName);
		}
		strs.add(datumName);
		strs.add(getOperand());
		strs.add(datumValue);
		strs.add(datumValueUnit);
		return StringUtils.join(strs, " ");
	}

	public String getQueryAsColumnName() {
		if (StringUtils.isEmpty(datumName) && StringUtils.isEmpty(datumValue)) {
			return characterizationType;
		} else {
			return characterizationType + "<br>" + characterizationName
					+ "<br>" + datumName;
		}
	}
}

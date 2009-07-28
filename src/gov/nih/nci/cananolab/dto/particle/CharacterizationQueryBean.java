package gov.nih.nci.cananolab.dto.particle;

import gov.nih.nci.cananolab.dto.BaseQueryBean;

/**
 * Information for the characterization query form
 * 
 * @author pansu
 * 
 */
public class CharacterizationQueryBean extends BaseQueryBean {
	private String characterizationType;
	private String characterizationName;
	private String characterizationAssay;
	private String datumName;
	private String datumValue;
	private String datumValueUnit;

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

	public String getCharacterizationAssay() {
		return characterizationAssay;
	}

	public void setCharacterizationAssay(String characterizationAssay) {
		this.characterizationAssay = characterizationAssay;
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
		this.datumValue = datumValue;
	}

	public String getDatumValueUnit() {
		return datumValueUnit;
	}

	public void setDatumValueUnit(String datumValueUnit) {
		this.datumValueUnit = datumValueUnit;
	}
}

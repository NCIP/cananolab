package gov.nih.nci.calab.dto.characterization.physical;

import gov.nih.nci.calab.domain.Measurement;
import gov.nih.nci.calab.domain.nano.characterization.physical.Stressor;
import gov.nih.nci.calab.service.util.StringUtils;


/**
 * This class represents the Stressor information
 * 
 * @author chande
 * 
 */
public class StressorBean {
	private String id;
	private String type;
	private String value;
	private String valueUnit;
	private String description;

	private String otherType;
	
	public String getOtherType() {
		return otherType;
	}

	public void setOtherType(String otherType) {
		this.otherType = otherType;
	}

	public StressorBean() {
	}
	
	public StressorBean(Stressor aChar) {
		this.id = aChar.getId().toString();
		this.type = aChar.getType();
		if (aChar.getValue() != null) {
			this.value = StringUtils.convertToString(aChar.getValue().getValue());
			this.valueUnit = aChar.getValue().getUnitOfMeasurement().toString();
		}
		this.description = aChar.getDescription();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValueUnit() {
		return valueUnit;
	}

	public void setValueUnit(String valueUnit) {
		this.valueUnit = valueUnit;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public Stressor getDomainObj() {
		Stressor stressor = new Stressor();
		
		if (this.id != null && this.id.length() > 0) 
			stressor.setId(new Long(this.id));
		
		if (this.type.equalsIgnoreCase("other") && !this.otherType.equalsIgnoreCase(""))
			stressor.setType(this.otherType);
		else
			stressor.setType(this.type);
		stressor.setValue(new Measurement(new Float(this.value), this.valueUnit));
		stressor.setDescription(this.description);
		
		return stressor;
	}
}

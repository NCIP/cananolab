package gov.nih.nci.calab.dto.characterization;

import gov.nih.nci.calab.domain.Measurement;
import gov.nih.nci.calab.domain.nano.characterization.Condition;
import gov.nih.nci.calab.service.util.StringUtils;

/**
 * This class represents the data within a characterization file to be shown in
 * the view page.
 * 
 * @author chand
 * 
 */
public class ConditionBean {
	private String id;

	private String type;

	private String value;

	private String unit;

	public ConditionBean() {

	}
	
	public ConditionBean(Condition condition) {
		this.id = condition.getId().toString();
		this.type = condition.getType();
		this.value = (condition.getValue()!= null)?StringUtils.convertToString(condition.getValue().getValue()):"";
		this.unit = (condition.getValue()!= null)?condition.getValue().getUnitOfMeasurement():"";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Condition getDomainObj() {
		Condition tableData = new Condition();
		if (getId() != null && getId().length() > 0) {
			tableData.setId(new Long(getId()));
		}
		tableData.setType(type);
		Measurement measurement=new Measurement();
		measurement.setValue(new Float(value));
		measurement.setUnitOfMeasurement(unit);
		tableData.setValue(measurement);
		return tableData;
	}
}

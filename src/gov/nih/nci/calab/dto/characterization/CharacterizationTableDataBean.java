package gov.nih.nci.calab.dto.characterization;

import gov.nih.nci.calab.domain.Measurement;
import gov.nih.nci.calab.domain.nano.characterization.TableData;

/**
 * This class represents the data within a characterization file to be shown in
 * the view page.
 * 
 * @author pansu
 * 
 */
public class CharacterizationTableDataBean {
	private String id;

	private String type;

	private String value;

	private String valueUnit;

	public CharacterizationTableDataBean() {

	}
	
	public CharacterizationTableDataBean(TableData tableData) {
		this.id = tableData.getId().toString();
		this.type = tableData.getType();
		this.value = tableData.getValue().getValue();
		this.valueUnit = tableData.getValue().getUnitOfMeasurement();
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

	public String getValueUnit() {
		return valueUnit;
	}

	public void setValueUnit(String valueUnit) {
		this.valueUnit = valueUnit;
	}

	public TableData getDomainObj() {
		TableData tableData = new TableData();
		if (getId() != null && getId().length() > 0) {
			tableData.setId(new Long(getId()));
		}
		tableData.setType(type);
		Measurement measurement=new Measurement();
		measurement.setValue(value);
		measurement.setUnitOfMeasurement(valueUnit);
		tableData.setValue(measurement);
		return tableData;
	}
}

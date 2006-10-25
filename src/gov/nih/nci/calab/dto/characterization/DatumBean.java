package gov.nih.nci.calab.dto.characterization;

import gov.nih.nci.calab.domain.Measurement;
import gov.nih.nci.calab.domain.nano.characterization.Datum;
import gov.nih.nci.calab.service.util.StringUtils;

/**
 * This class represents the data within a characterization file to be shown in
 * the view page.
 * 
 * @author chand
 * 
 */
public class DatumBean {
	private String id;

	private String type;

	private String value;

	private String valueUnit;

	public DatumBean() {

	}
	
	public DatumBean(Datum datum) {
		this.id = datum.getId().toString();
		this.type = datum.getType();
		this.value = (datum.getValue()!=null)?StringUtils.convertToString(datum.getValue().getValue()):"";
		this.valueUnit = (datum.getValue()!=null)?StringUtils.convertToString(datum.getValue().getUnitOfMeasurement()):"";
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

	public Datum getDomainObj() {
		Datum tableData = new Datum();
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

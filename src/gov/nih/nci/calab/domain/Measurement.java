/**
 * 
 */
package gov.nih.nci.calab.domain;

import java.io.Serializable;

/**
 * @author zengje
 * 
 */
public class Measurement implements Serializable {

	private static final long serialVersionUID = 1234567890L;

	private Float standardDeviation;

	private String valueType;

	public Float getStandardDeviation() {
		return standardDeviation;
	}

	public void setStandardDeviation(Float standardDeviation) {
		this.standardDeviation = standardDeviation;
	}

	/**
	 * 
	 */
	public Measurement() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Measurement(Float value, String unit) {
		this.setValue(value);
		this.setUnitOfMeasurement(unit);
	}

	private java.lang.Long id;

	public java.lang.Long getId() {
		return id;
	}

	public void setId(java.lang.Long id) {
		this.id = id;
	}

	private java.lang.Float value;

	public java.lang.Float getValue() {
		return value;
	}

	public void setValue(java.lang.Float value) {
		this.value = value;
	}

	private java.lang.String unitOfMeasurement;

	public java.lang.String getUnitOfMeasurement() {
		return unitOfMeasurement;
	}

	public void setUnitOfMeasurement(java.lang.String unitOfMeasurement) {
		this.unitOfMeasurement = unitOfMeasurement;
	}

	public String getValueType() {
		return valueType;
	}

	public void setValueType(String valueType) {
		this.valueType = valueType;
	}
}
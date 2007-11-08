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

	private java.lang.Long id;

	private java.lang.Float value;

	private String statisticsType;

	/**
	 * 
	 */
	public Measurement() {

	}

	public Measurement(Float value, String unit) {
		this.setValue(value);
		this.setUnitOfMeasurement(unit);
	}

	public java.lang.Long getId() {
		return this.id;
	}

	public void setId(java.lang.Long id) {
		this.id = id;
	}

	public java.lang.Float getValue() {
		return this.value;
	}

	public void setValue(java.lang.Float value) {
		this.value = value;
	}

	private java.lang.String unitOfMeasurement;

	public java.lang.String getUnitOfMeasurement() {
		return this.unitOfMeasurement;
	}

	public void setUnitOfMeasurement(java.lang.String unitOfMeasurement) {
		this.unitOfMeasurement = unitOfMeasurement;
	}

	public String getStatisticsType() {
		return this.statisticsType;
	}

	public void setStatisticsType(String statsType) {
		this.statisticsType = statsType;
	}
}
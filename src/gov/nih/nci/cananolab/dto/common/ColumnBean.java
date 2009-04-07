package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.Condition;
import gov.nih.nci.cananolab.domain.common.Datum;

/**
 * View bean for Data Column
 *
 * @author tanq
 *
 */
public class ColumnBean {
	private Long id;
	private String name;
	private String property;
	private String value;
	private String valueType;
	private String valueUnit;
	private String datumOrCondition;
	private String columnLabel;


	public ColumnBean() {

	}

	public ColumnBean(Datum datum) {
		this.id = datum.getId();
		this.name = datum.getName();
		this.value = datum.getValue();
		this.valueType = datum.getValueType();
		this.valueUnit = datum.getValueUnit();
		this.datumOrCondition = "Datum";
	}

	public ColumnBean(Condition condition) {
		this.id = condition.getId();
		this.name = condition.getName();
		this.property = condition.getProperty();
		this.value = condition.getValue();
		this.valueType = condition.getValueType();
		this.valueUnit = condition.getValueUnit();
		this.datumOrCondition = "Condition";
	}
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the property
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * @param property
	 *            the property to set
	 */
	public void setProperty(String property) {
		this.property = property;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the valueType
	 */
	public String getValueType() {
		return valueType;
	}

	/**
	 * @param valueType
	 *            the valueType to set
	 */
	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

	/**
	 * @return the valueUnit
	 */
	public String getValueUnit() {
		return valueUnit;
	}

	/**
	 * @param valueUnit
	 *            the valueUnit to set
	 */
	public void setValueUnit(String valueUnit) {
		this.valueUnit = valueUnit;
	}

	/**
	 * @return the columnLabel
	 */
	public String getColumnLabel() {
		columnLabel = name;
		if (property != null && property.trim().length() > 0) {
			columnLabel += " "+property;
		}
		if ((valueType != null && valueType.trim().length() > 0)
				|| (valueUnit != null && valueUnit.trim().length() > 0)) {
			columnLabel += " (";
			if (valueType != null && valueType.trim().length() > 0) {
				columnLabel += valueType;
				if (valueUnit != null && valueUnit.trim().length() > 0) {
					columnLabel += ",";
				}
			}
			if (valueUnit != null && valueUnit.trim().length() > 0) {
				columnLabel += valueUnit;
			}
			columnLabel += ")";
		}
		return columnLabel;
	}

	/**
	 * @param columnLabel
	 *            the columnLabel to set
	 */
	public void setColumnLabel(String columnLabel) {
		this.columnLabel = columnLabel;
	}

	/**
	 * Compares <code>obj</code> to it self and returns true if they both are
	 * same
	 *
	 * @param obj
	 **/
	public boolean equals(Object obj) {
		if (obj instanceof ColumnBean) {
			ColumnBean c = (ColumnBean) obj;
			if (getId() != null && getId().equals(c.getId()))
				return true;
		}
		return false;
	}

	/**
	 * Returns hash code for the primary key of the object
	 **/
	public int hashCode() {
		if (getId() != null)
			return getId().hashCode();
		return 0;
	}

	/**
	 * @return the datumOrCondition
	 */
	public String getDatumOrCondition() {
		return datumOrCondition;
	}

	/**
	 * @param datumOrCondition
	 *            the datumOrCondition to set
	 */
	public void setDatumOrCondition(String datumOrCondition) {
		this.datumOrCondition = datumOrCondition;
	}

}

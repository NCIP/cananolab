package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.Condition;
import gov.nih.nci.cananolab.domain.common.Datum;

/**
 * View bean representing a column header in a matrix
 * column
 *
 * @author pansu
 *
 */
public class ColumnHeader {
	private String name;
	private String conditionProperty;
	private String valueType;
	private String valueUnit;
	private String datumOrCondition;
	private String displayName;
	private String constantValue;

	public ColumnHeader(Datum datum) {
		this.name = datum.getName();
		this.valueType = datum.getValueType();
		this.valueUnit = datum.getValueUnit();
		this.datumOrCondition = "Datum";
	}

	public ColumnHeader(Condition condition) {
		this.name = condition.getName();
		this.conditionProperty = condition.getProperty();
		this.valueType = condition.getValueType();
		this.valueUnit = condition.getValueUnit();
		this.datumOrCondition = "Condition";
	}

	public ColumnHeader() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getConditionProperty() {
		return conditionProperty;
	}

	public void setConditionProperty(String property) {
		this.conditionProperty = property;
	}

	public String getValueType() {
		return valueType;
	}

	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

	public String getValueUnit() {
		return valueUnit;
	}

	public void setValueUnit(String valueUnit) {
		this.valueUnit = valueUnit;
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		displayName = name;
		if (conditionProperty != null && conditionProperty.trim().length() > 0) {
			displayName += " " + conditionProperty;
		}
		if ((valueType != null && valueType.trim().length() > 0)
				|| (valueUnit != null && valueUnit.trim().length() > 0)) {
			displayName += " (";
			if (valueType != null && valueType.trim().length() > 0) {
				displayName += valueType;
				if (valueUnit != null && valueUnit.trim().length() > 0) {
					displayName += ",";
				}
			}
			if (valueUnit != null && valueUnit.trim().length() > 0) {
				displayName += valueUnit;
			}
			displayName += ")";
		}
		return displayName;
	}

	/**
	 * @param displayName
	 *            the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Compares <code>obj</code> to it self and returns true if they both are
	 * same
	 *
	 * @param obj
	 */
	public boolean equals(Object obj) {
		if (obj instanceof ColumnHeader) {
			ColumnHeader c = (ColumnHeader) obj;
			if (getDisplayName().equals(c.getDisplayName()))
				return true;
		}
		return false;
	}

	/**
	 * Returns hash code for the primary key of the object
	 */
	public int hashCode() {
		if (getDisplayName() != null)
			return getDisplayName().hashCode();
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

	public String getConstantValue() {
		return constantValue;
	}

	public void setConstantValue(String constantValue) {
		this.constantValue = constantValue;
	}

}

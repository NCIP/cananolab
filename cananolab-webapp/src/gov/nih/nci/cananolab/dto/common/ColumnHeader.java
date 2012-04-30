package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.Condition;
import gov.nih.nci.cananolab.domain.common.Datum;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.Date;

/**
 * View bean representing a column header in a matrix column
 *
 * @author pansu
 *
 */
public class ColumnHeader {
	private String columnName;
	private String conditionProperty;
	private String valueType;
	private String valueUnit;
	private String columnType;
	private String displayName;
	private String constantValue="";

	// FR# 26194, matrix column order.
	private Integer columnOrder;
	private Date createdDate;

	public ColumnHeader(Datum datum) {
		this.columnName = datum.getName();
		this.valueType = datum.getValueType();
		this.valueUnit = datum.getValueUnit();
		this.columnType = FindingBean.DATUM_TYPE;
		this.createdDate = datum.getCreatedDate();
	}

	public ColumnHeader(Condition condition) {
		this.columnName = condition.getName();
		this.conditionProperty = condition.getProperty();
		this.valueType = condition.getValueType();
		this.valueUnit = condition.getValueUnit();
		this.columnType = FindingBean.CONDITION_TYPE;
		this.createdDate = condition.getCreatedDate();
	}

	public ColumnHeader() {

	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String name) {
		this.columnName = name;
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
		displayName = columnName;
		if (!StringUtils.isEmpty(conditionProperty)) {
			displayName += " " + conditionProperty;
		}
		if (!StringUtils.isEmpty(valueType) || !StringUtils.isEmpty(valueUnit)) {
			displayName += "<br>(";
			if (!StringUtils.isEmpty(valueType)) {
				displayName += valueType;
				if (!StringUtils.isEmpty(valueUnit)) {
					displayName += ",";
				}
			}
			if (!StringUtils.isEmpty(valueUnit)) {
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
			if (getDisplayName().equals(c.getDisplayName())
					&& this.getConstantValue().equals(c.getConstantValue()))
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
	 * @return the columnType
	 */
	public String getColumnType() {
		return columnType;
	}

	/**
	 * @param datumOrCondition
	 *            the datumOrCondition to set
	 */
	public void setColumnType(String datumOrCondition) {
		this.columnType = datumOrCondition;
	}

	public String getConstantValue() {
		return constantValue;
	}

	public void setConstantValue(String constantValue) {
		this.constantValue = constantValue;
	}

	public Integer getColumnOrder() {
		return columnOrder;
	}

	public void setColumnOrder(Integer columnOrder) {
		this.columnOrder = columnOrder;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
}

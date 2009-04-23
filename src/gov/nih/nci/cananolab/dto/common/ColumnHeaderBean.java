package gov.nih.nci.cananolab.dto.common;


/**
 * View bean representing a column header for a datum column or a condition column
 *
 * @author pansu
 *
 */
public class ColumnHeaderBean {
	private String name;
	private String property;
	private String valueType;
	private String valueUnit;
	private String datumOrCondition;
	private String displayName;

	public ColumnHeaderBean() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
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
		if (property != null && property.trim().length() > 0) {
			displayName += " " + property;
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
		if (obj instanceof ColumnHeaderBean) {
			ColumnHeaderBean c = (ColumnHeaderBean) obj;
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
}

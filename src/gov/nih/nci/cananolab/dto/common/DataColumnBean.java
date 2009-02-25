package gov.nih.nci.cananolab.dto.common;



/**
 * View bean for Data Column
 *
 * @author tanq
 *
 */
public class DataColumnBean {
	private Long id;
	private String name;
	private String property;
	private String value;
	private String valueType;
	private String valueUnit;
	private String datumOrCondition;
	private String displayName;
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
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
	 * @param name the name to set
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
	 * @param property the property to set
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
	 * @param value the value to set
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
	 * @param valueType the valueType to set
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
	 * @param valueUnit the valueUnit to set
	 */
	public void setValueUnit(String valueUnit) {
		this.valueUnit = valueUnit;
	}
	
	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return name+"("+valueUnit+")";
	}
	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	
	/**
	* Compares <code>obj</code> to it self and returns true if they both are same
	*
	* @param obj
	**/
	public boolean equals(Object obj)
	{
		if(obj instanceof DataColumnBean) 
		{
			DataColumnBean c =(DataColumnBean)obj; 			 
			if(getId() != null && getId().equals(c.getId()))
				return true;
		}
		return false;
	}
		
	/**
	* Returns hash code for the primary key of the object
	**/
	public int hashCode()
	{
		if(getId() != null)
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
	 * @param datumOrCondition the datumOrCondition to set
	 */
	public void setDatumOrCondition(String datumOrCondition) {
		this.datumOrCondition = datumOrCondition;
	}
	
}

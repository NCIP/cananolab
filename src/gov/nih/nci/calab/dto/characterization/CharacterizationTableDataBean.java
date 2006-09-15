package gov.nih.nci.calab.dto.characterization;

/**
 * This class represents the data within a characterization file to be
 * shown in the view page.
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
	
	
}

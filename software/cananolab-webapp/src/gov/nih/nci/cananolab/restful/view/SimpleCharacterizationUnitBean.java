package gov.nih.nci.cananolab.restful.view;

public class SimpleCharacterizationUnitBean {
	
	String name;
	Object value;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	
	public SimpleCharacterizationUnitBean(String name, Object value) {
		this.name = name;
		this.value = value;
	}
}

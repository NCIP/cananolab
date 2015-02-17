package gov.nih.nci.cananolab.restful.bean;

public class LabelValueBean {
	String label;
	String value;
	
	public LabelValueBean() {}
	
	public LabelValueBean(String label, String value) {
		this.label = label;
		this.value = value;
	}
	@Override
	public boolean equals(Object obj) {
		
		return this.label.equals(((LabelValueBean)obj).getLabel());
	}

	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	

}

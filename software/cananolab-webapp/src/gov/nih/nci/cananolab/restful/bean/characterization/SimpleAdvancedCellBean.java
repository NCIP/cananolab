package gov.nih.nci.cananolab.restful.bean.characterization;

public class SimpleAdvancedCellBean {
	
	String type;
	
	Object val;
	
	public SimpleAdvancedCellBean(String type, Object val) {
		this.type = type;
		this.val = val;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Object getVal() {
		return val;
	}

	public void setVal(Object val) {
		this.val = val;
	}
	
	

}

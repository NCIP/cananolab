package gov.nih.nci.calab.dto.characterization;

import gov.nih.nci.calab.domain.nano.characterization.Control;

/**
 * This class represents the data within a characterization file to be shown in
 * the view page.
 * 
 * @author chand
 * 
 */
public class ControlBean {
	private String id;
	private String type;
	private String name;

	public ControlBean() {
	}
	
	public ControlBean(Control control) {
		this.id = control.getId().toString();
		this.type = control.getType();
		this.name = control.getName();
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Control getDomainObj() {
		Control tableData = new Control();
		if (getId() != null && getId().length() > 0) {
			tableData.setId(new Long(getId()));
		}
		tableData.setType(type);
		tableData.setName(name);
		return tableData;
	}
}

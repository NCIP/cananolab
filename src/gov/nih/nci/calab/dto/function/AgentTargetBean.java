package gov.nih.nci.calab.dto.function;

/**
 * This class represents the data associated with an agent target be shown in
 * the view page.
 * 
 * @author pansu
 *
 */
public class AgentTargetBean {
	private String description;

	private String name;
	
	private String type;

	public AgentTargetBean() {
		
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}

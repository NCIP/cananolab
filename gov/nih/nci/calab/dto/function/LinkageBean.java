/**
 * 
 */
package gov.nih.nci.calab.dto.function;

/**
 * @author zengje
 *
 */
public class LinkageBean {
	
	private String id;
	private String description;
	private String type="Attachment";
	private String value;
	
	private AgentBean agent = new AgentBean();
	
	/**
	 * 
	 */
	public LinkageBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String linkageProperty) {
		this.value = linkageProperty;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public AgentBean getAgent() {
		return agent;
	}

	public void setAgent(AgentBean agent) {
		this.agent = agent;
	}

}

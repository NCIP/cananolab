package gov.nih.nci.calab.domain.nano.function;

public class Linkage implements java.io.Serializable {

	private static final long serialVersionUID = 1234567890L;

	private Long id;

	private String description;

	private Agent agent;

	public Linkage() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}

}

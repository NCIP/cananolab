package gov.nih.nci.calab.domain.nano.function;

public class UnclassifiedLinkage implements Linkage {
	
	private Long id;
	private String description;
	private Agent agent;
	
	public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	public UnclassifiedLinkage() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}

package gov.nih.nci.calab.domain.nano.function;

public class UnclassifiedAgentTarget implements AgentTarget {

	private Long id;
	private String description;

	public UnclassifiedAgentTarget() {
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

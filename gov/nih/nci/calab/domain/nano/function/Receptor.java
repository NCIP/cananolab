package gov.nih.nci.calab.domain.nano.function;

public class Receptor implements AgentTarget {
	private static final long serialVersionUID = 1234567890L;

	private Long id;

	private String description;

	private String name;

	public Receptor() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

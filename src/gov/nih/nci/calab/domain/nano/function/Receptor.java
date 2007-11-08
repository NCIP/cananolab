package gov.nih.nci.calab.domain.nano.function;

public class Receptor extends AgentTarget {
	private static final long serialVersionUID = 1234567890L;

	private Long id;

	private String description;

	private String name;

	public Receptor() {

	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}

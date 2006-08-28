package gov.nih.nci.calab.dto.characterization.composition;

public class ComplexParticleBean extends CompositionBean {
	private String name;

	private String description;

	public ComplexParticleBean() {
		super();
		// TODO Auto-generated constructor stub
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
}

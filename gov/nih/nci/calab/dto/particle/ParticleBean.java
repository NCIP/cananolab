package gov.nih.nci.calab.dto.particle;

public class ParticleBean {
	private String id;

	private String name;

	public ParticleBean(String id, String name) {
		super();
		// TODO Auto-generated constructor stub
		this.id = id;
		this.name = name;
	}

	public ParticleBean() {
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}

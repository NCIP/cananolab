package gov.nih.nci.calab.domain.nano.characterization;

import java.io.Serializable;

public class Control implements Serializable {

	private static final long serialVersionUID = 1234567890L;

	private Long id;

	private String name;

	private String type;

	public Control() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

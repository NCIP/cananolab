package gov.nih.nci.calab.domain;

import java.io.Serializable;

public class Manufacturer implements Serializable{

	private static final long serialVersionUID = 1234567890L;
	
	private Long id;
	private String name;
	
	public Manufacturer() {
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

}

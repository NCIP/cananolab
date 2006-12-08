package gov.nih.nci.calab.domain;

import java.util.Collection;
import java.util.HashSet;

public class InstrumentType {

	private static final long serialVersionUID = 1234567890L;
	
	private Long id;
	private String name;
	private String description;
	private String abbreviation;
	
	private Collection<Manufacturer> manufacturerCollection = new HashSet<Manufacturer>();
	
	public InstrumentType() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Collection<Manufacturer> getManufacturerCollection() {
		return manufacturerCollection;
	}

	public void setManufacturerCollection(
			Collection<Manufacturer> manufacturerCollection) {
		this.manufacturerCollection = manufacturerCollection;
	}

}

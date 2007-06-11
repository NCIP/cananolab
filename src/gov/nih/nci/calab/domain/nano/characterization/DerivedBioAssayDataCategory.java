package gov.nih.nci.calab.domain.nano.characterization;

import java.util.HashSet;
import java.util.Set;

public class DerivedBioAssayDataCategory {
	private static final long serialVersionUID = 1234567890L;

	private Long id;
	
	private String characterizationName;
	
	private String name;
	
	private Set<DatumName> datumNameCollection=new HashSet<DatumName>();
	
	public String getCharacterizationName() {
		return characterizationName;
	}

	public void setCharacterizationName(String characterizationName) {
		this.characterizationName = characterizationName;
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

	public Set<DatumName> getDatumNameCollection() {
		return datumNameCollection;
	}

	public void setDatumNameCollection(Set<DatumName> datumNameCollection) {
		this.datumNameCollection = datumNameCollection;
	}
	

}

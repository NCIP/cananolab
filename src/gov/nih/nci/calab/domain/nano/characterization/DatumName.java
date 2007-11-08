package gov.nih.nci.calab.domain.nano.characterization;

public class DatumName {
	private static final long serialVersionUID = 1234567890L;

	private Long id;

	private Boolean datumParsed;

	private String name;

	private String characterizationName;

	public Boolean getDatumParsed() {
		return this.datumParsed;
	}

	public void setDatumParsed(Boolean datumParsed) {
		this.datumParsed = datumParsed;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCharacterizationName() {
		return this.characterizationName;
	}

	public void setCharacterizationName(String characterizationName) {
		this.characterizationName = characterizationName;
	}

}

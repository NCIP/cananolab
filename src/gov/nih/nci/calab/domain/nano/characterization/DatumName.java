package gov.nih.nci.calab.domain.nano.characterization;

public class DatumName {
	private static final long serialVersionUID = 1234567890L;

	private Long id;

	private Boolean datumParsed;

	private String name;

	private DerivedBioAssayDataCategory derivedBioAssayDataCategory;

	public DerivedBioAssayDataCategory getDerivedBioAssayDataCategory() {
		return derivedBioAssayDataCategory;
	}

	public void setDerivedBioAssayDataCategory(
			DerivedBioAssayDataCategory derivedBioAssayDataCategory) {
		this.derivedBioAssayDataCategory = derivedBioAssayDataCategory;
	}

	public Boolean getDatumParsed() {
		return datumParsed;
	}

	public void setDatumParsed(Boolean datumParsed) {
		this.datumParsed = datumParsed;
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

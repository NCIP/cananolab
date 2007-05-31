package gov.nih.nci.calab.dto.characterization;

/**
 * This class represents an instrument used for characterization
 * @author pansu
 *
 */
public class InstrumentBean {
	private String id;
	private String type;
	private String description;
	private String manufacturer;
	private String abbreviation;
	private String otherInstrumentType;
	private String otherManufacturer;
	
	public InstrumentBean() {
		
	}
	
	public InstrumentBean(String type, String description, String manufacturer, String abbreviation) {
		super();	
		this.type = type;
		this.description = description;
		this.manufacturer = manufacturer;
		this.abbreviation = abbreviation;
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

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public String getOtherInstrumentType() {
		return otherInstrumentType;
	}

	public void setOtherInstrumentType(String otherInstrumentType) {
		this.otherInstrumentType = otherInstrumentType;
	}

	public String getOtherManufacturer() {
		return otherManufacturer;
	}

	public void setOtherManufacturer(String otherManufacturer) {
		this.otherManufacturer = otherManufacturer;
	}
}
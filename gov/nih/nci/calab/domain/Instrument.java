/**
 * 
 */
package gov.nih.nci.calab.domain;

/**
 * @author zengje
 *
 */
public class Instrument {
	private static final long serialVersionUID = 1234567890L;

	private Long id;
	private String type;
	private String description;
	private String manufacturer;
	private String abbreviation;
	/**
	 * 
	 */
	public Instrument() {
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

}

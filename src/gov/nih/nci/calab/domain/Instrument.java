/**
 * 
 */
package gov.nih.nci.calab.domain;

import java.io.Serializable;

/**
 * @author zengje
 *
 */
public class Instrument implements Serializable{
	private static final long serialVersionUID = 1234567890L;

	private Long id;
	private InstrumentType instrumentType;
	private String description;
	private Manufacturer manufacturer;
	/**
	 * 
	 */
	public Instrument() {
		super();
		// TODO Auto-generated constructor stub
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
	public InstrumentType getInstrumentType() {
		return instrumentType;
	}
	public void setInstrumentType(InstrumentType instrumentType) {
		this.instrumentType = instrumentType;
	}
	public Manufacturer getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(Manufacturer manufacturer) {
		this.manufacturer = manufacturer;
	}

}

/**
 * 
 */
package gov.nih.nci.calab.domain;

import java.io.Serializable;

/**
 * @author pansu
 * 
 */
public class InstrumentConfiguration implements Serializable {
	private static final long serialVersionUID = 1234567890L;

	private Long id;

	private Instrument instrument;

	private String description;

	/**
	 * 
	 */
	public InstrumentConfiguration() {
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

	public Instrument getInstrument() {
		return instrument;
	}

	public void setInstrument(Instrument instrument) {
		this.instrument = instrument;
	}
}

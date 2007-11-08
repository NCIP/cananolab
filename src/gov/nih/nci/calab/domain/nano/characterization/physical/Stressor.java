/**
 * 
 */
package gov.nih.nci.calab.domain.nano.characterization.physical;

import gov.nih.nci.calab.domain.Measurement;

/**
 * @author zengje
 * 
 */
public class Stressor {

	private static final long serialVersionUID = 1234567890L;

	private Long id;

	private String type;

	private Measurement value;

	private String description;

	/**
	 * 
	 */
	public Stressor() {

	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Measurement getValue() {
		return this.value;
	}

	public void setValue(Measurement value) {
		this.value = value;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}

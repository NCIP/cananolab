/**
 * 
 */
package gov.nih.nci.calab.domain.nano.characterization.physical.composition;


/**
 * @author Zeng
 *
 */
public class Coating {
	private Long id;
	private String type;
	private Float percentage;
	private Composition composition;
	
	/**
	 * 
	 */
	public Coating() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Float getPercentage() {
		return percentage;
	}

	public void setPercentage(Float percentage) {
		this.percentage = percentage;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Composition getComposition() {
		return composition;
	}

	public void setComposition(Composition composition) {
		this.composition = composition;
	}

}

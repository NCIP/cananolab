/**
 * 
 */
package gov.nih.nci.calab.domain.nano.characterization.physical.composition;


/**
 * @author Zeng
 *
 */
public class Shell {
	private Long id;
	private String type;
	private Composition composition;
	/**
	 * 
	 */
	public Shell() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Composition getComposition() {
		return composition;
	}
	public void setComposition(Composition composition) {
		this.composition = composition;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

}

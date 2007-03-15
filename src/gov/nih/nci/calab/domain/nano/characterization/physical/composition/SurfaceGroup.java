/**
 * 
 */
package gov.nih.nci.calab.domain.nano.characterization.physical.composition;

/**
 * @author Zeng
 * 
 */
public class SurfaceGroup implements java.io.Serializable {

	private static final long serialVersionUID = 1234567890L;

	private Long id;

	private String name;

	private String modifier;

	private DendrimerComposition dendrimerComposition;

	/**
	 * 
	 */
	public SurfaceGroup() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DendrimerComposition getDendrimerComposition() {
		return dendrimerComposition;
	}

	public void setDendrimerComposition(
			DendrimerComposition dendrimerComposition) {
		this.dendrimerComposition = dendrimerComposition;
	}

}

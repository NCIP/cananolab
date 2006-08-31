package gov.nih.nci.calab.dto.characterization.composition;

/**
 * This class represents properties of a Complext Nanoparticle composition to be shown in
 * the view page.
 * @author pansu
 *
 */
public class ComplexParticleBean extends CompositionBean {
	private String name;

	private String description;

	public ComplexParticleBean() {
		super();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

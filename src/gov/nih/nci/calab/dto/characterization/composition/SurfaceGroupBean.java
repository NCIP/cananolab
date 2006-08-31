package gov.nih.nci.calab.dto.characterization.composition;
/**
 * This class represents properties of a SurfaceGroup to be shown in the view page.
 * @author pansu
 *
 */
public class SurfaceGroupBean {
	private String id;
	private String name;
	private String modifier;
	
	public SurfaceGroupBean() {
		
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
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
}

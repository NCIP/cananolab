package gov.nih.nci.calab.dto.characterization.composition;

/**
 * This class represents the properties of a ComposingElement to be shown in the view page.
 * @author pansu
 *
 */
public class ComposingElementBean {
	private String chemicalName;
	private String elementType;
	private String description;
	
	public ComposingElementBean(){		
	}
	
	public String getChemicalName() {
		return chemicalName;
	}
	public void setChemicalName(String chemicalName) {
		this.chemicalName = chemicalName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getElementType() {
		return elementType;
	}
	public void setElementType(String elementType) {
		this.elementType = elementType;
	}
}

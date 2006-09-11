package gov.nih.nci.calab.dto.characterization.composition;

import gov.nih.nci.calab.domain.nano.characterization.physical.composition.ComposingElement;

/**
 * This class represents the properties of a ComposingElement to be shown in the view page.
 * @author pansu
 *
 */
public class ComposingElementBean {
	private String id;
	private String chemicalName;
	private String elementType;
	private String description;
	
	public ComposingElementBean(){		
	}
	
	public ComposingElementBean(ComposingElement element) {
		this.id=element.getId().toString();
		this.chemicalName=element.getChemicalName();
		this.elementType=element.getElementType();
		this.description=element.getDescription();
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
	
	public ComposingElement getDomainObj() {
		ComposingElement element=new ComposingElement();
		if (getId()!=null&&getId().length() > 0) {
			element.setId(new Long(getId()));
		}		
		element.setChemicalName(chemicalName);
		element.setDescription(description);
		element.setElementType(elementType);
		return element;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}

package gov.nih.nci.cananolab.restful.view.edit;

import gov.nih.nci.cananolab.domain.particle.ComposingElement;

public class SimpleAssociatedElement {

	String compositionType;

	String entityId; // dendrimer id, small molecule id

	String entityDisplayName; // dendrimer, small molecule

	String className; // SmallMolecule, ComposingElement

	SimpleComposingElementBean composingElement;

	public String getCompositionType() {
		return compositionType;
	}

	public void setCompositionType(String compositionType) {
		this.compositionType = compositionType;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public String getEntityDisplayName() {
		return entityDisplayName;
	}

	public void setEntityDisplayName(String entityDisplayName) {
		this.entityDisplayName = entityDisplayName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public SimpleComposingElementBean getComposingElement() {
		return composingElement;
	}

	public void setComposingElement(SimpleComposingElementBean composingElement) {
		this.composingElement = composingElement;
	}
	
	
}

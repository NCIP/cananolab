package gov.nih.nci.cananolab.dto.particle.composition;

import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.ComposingElement;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.chemicalassociation.AssociatedElement;
import gov.nih.nci.cananolab.util.ClassUtils;

import java.util.Map;

public class AssociatedElementBean {
	// eg. Nanoparticle Entity, Functionalizing Entity
	private String compositionType;

	private String entityId; // dendrimer id, small molecule id

	private String entityDisplayName; // dendrimer, small molecule

	private String entityClassName; // SmallMolecule

	private ComposingElement composingElement = new ComposingElement();

	private AssociatedElement domainElement;

	public AssociatedElementBean() {
	}

	public AssociatedElementBean(AssociatedElement element) {
		domainElement = element;
		if (element instanceof ComposingElement) {
			composingElement = (ComposingElement) element;
		} else {
			entityClassName = ClassUtils.getShortClassName(element.getClass()
					.getName());
		}
	}

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

	public ComposingElement getComposingElement() {
		domainElement = composingElement;
		return composingElement;
	}

	public AssociatedElement getDomainElement() {
		return domainElement;
	}

	public void setupDomainElement(Map<String, String> typeToClass,
			String createdBy) throws Exception {
		//domain element is a functionalizing entity
		if (composingElement.getId() == null) {
			entityClassName = typeToClass.get(entityDisplayName);
			Class clazz = ClassUtils.getFullClass(entityClassName);
			domainElement = (AssociatedElement) clazz.newInstance();
			domainElement.setId(new Long(entityId));
		}
	}

	public String getEntityDisplayName() {
		return entityDisplayName;
	}

	public void setEntityDisplayName(String entityDisplayName) {
		this.entityDisplayName = entityDisplayName;
	}
}

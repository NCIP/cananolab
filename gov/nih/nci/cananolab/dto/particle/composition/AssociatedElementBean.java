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

	private String className; // SmallMolecule, ComposingElement

	private ComposingElement composingElement = new ComposingElement();

	private AssociatedElement domainElement;

	public AssociatedElementBean() {
	}

	public AssociatedElementBean(AssociatedElement element) {
		domainElement = element;
		if (element instanceof ComposingElement) {
			composingElement = (ComposingElement) element;
			entityId = composingElement.getNanoparticleEntity().getId()
					.toString();
		} else {
			className = ClassUtils.getShortClassName(element.getClass()
					.getName());
			entityId = element.getId().toString();
		}
	}

	public String getCompositionType() {
		return compositionType;
	}

	public String getEntityId() {
		return entityId;
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
		// domain element is a functionalizing entity
		if (composingElement.getId() == null) {
			className = typeToClass.get(entityDisplayName);
			Class clazz = ClassUtils.getFullClass(className);
			domainElement = (AssociatedElement) clazz.newInstance();
			domainElement.setId(new Long(entityId));
		} else if (composingElement.getId() != null
				&& !className.equals("ComposingElement")) {
			domainElement = composingElement;
		}

	}

	public String getEntityDisplayName() {
		return entityDisplayName;
	}

	public String getClassName() {
		return className;
	}

	public void setEntityDisplayName(String entityDisplayName) {
		this.entityDisplayName = entityDisplayName;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public void updateType(Map<String, String> classToType) {
		if (composingElement.getId() != null) {
			compositionType = classToType.get("NanoparticleEntity");
			String entityClassName = className = ClassUtils
					.getShortClassName(composingElement.getNanoparticleEntity()
							.getClass().getName());
			entityDisplayName = classToType.get(entityClassName);
		} else {
			compositionType = classToType.get("FunctionalizingEntity");
			entityDisplayName = classToType.get(className);
		}
	}
}

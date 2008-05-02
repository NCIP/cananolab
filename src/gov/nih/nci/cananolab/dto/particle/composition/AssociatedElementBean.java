package gov.nih.nci.cananolab.dto.particle.composition;

import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.ComposingElement;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.chemicalassociation.AssociatedElement;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.FunctionalizingEntity;
import gov.nih.nci.cananolab.util.ClassUtils;

import java.util.Date;
import java.util.Map;

public class AssociatedElementBean {
	// eg. Nanoparticle Entity, Functionalizing Entity
	private String compositionType;

	private String entityId;

	private String entityClassName;

	private ComposingElement composingElement = new ComposingElement();

	private AssociatedElement domainElement;

	private String className;

	public AssociatedElementBean() {
	}

	public AssociatedElementBean(AssociatedElement element) {
		domainElement = element;
		if (element instanceof ComposingElement) {
			composingElement = (ComposingElement) element;
		}
		entityClassName = ClassUtils.getShortClassName(element.getClass()
				.getName());
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
		//className = typeToClass.get(type);
		if (domainElement == null) {
			Class clazz = ClassUtils.getFullClass(entityClassName);
			domainElement = (FunctionalizingEntity) clazz.newInstance();
		}
		if (domainElement.getId() == null) {
			domainElement.setCreatedBy(createdBy);
			domainElement.setCreatedDate(new Date());
		}
	}

	public String getEntityClassName() {
		return entityClassName;
	}

	public void setEntityClassName(String entityClassName) {
		this.entityClassName = entityClassName;
	}

}

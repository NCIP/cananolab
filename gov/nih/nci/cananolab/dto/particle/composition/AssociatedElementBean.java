package gov.nih.nci.cananolab.dto.particle.composition;

import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.ComposingElement;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.chemicalassociation.AssociatedElement;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.FunctionalizingEntity;
import gov.nih.nci.cananolab.util.ClassUtils;

import java.util.Date;

public class AssociatedElementBean {
	private String childType;

	private String entityType;

	private String entityClassName;

	private ComposingElement composingElement;

	private AssociatedElement domainElement;

	private String createdBy;

	public AssociatedElementBean(AssociatedElement element) {
		domainElement = element;
		if (element instanceof ComposingElement) {
			composingElement = (ComposingElement) element;
		}
		entityClassName = ClassUtils.getShortClassName(element.getClass()
				.getName());
	}

	public String getChildType() {
		return childType;
	}

	public void setChildType(String childType) {
		this.childType = childType;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public ComposingElement getComposingElement() {
		domainElement = composingElement;
		return composingElement;
	}

	public AssociatedElement getDomainElement() {
		return domainElement;
	}

	public void setDomainElement() throws Exception {
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

package gov.nih.nci.cananolab.dto.particle.composition;

import gov.nih.nci.cananolab.domain.agentmaterial.OtherFunctionalizingEntity;
import gov.nih.nci.cananolab.domain.nanomaterial.OtherNanomaterialEntity;
import gov.nih.nci.cananolab.domain.particle.AssociatedElement;
import gov.nih.nci.cananolab.domain.particle.ComposingElement;
import gov.nih.nci.cananolab.domain.particle.NanomaterialEntity;
import gov.nih.nci.cananolab.util.ClassUtils;

public class AssociatedElementBean {
	// eg. Nanomaterial Entity, Functionalizing Entity
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
		className = ClassUtils.getShortClassName(element.getClass().getName());
		if (element instanceof ComposingElement) {
			composingElement = (ComposingElement) element;
			if (composingElement.getNanomaterialEntity() != null) {
				entityId = composingElement.getNanomaterialEntity().getId()
						.toString();
			}
		} else {
			if (element.getId() != null)
				entityId = element.getId().toString();
		}
		updateType();
	}

	public String getCompositionType() {
		return compositionType;
	}

	public String getEntityId() {
		return entityId;
	}

	public ComposingElement getComposingElement() {
		return composingElement;
	}

	public AssociatedElement getDomainElement() {
		return domainElement;
	}

	public void setupDomainElement(String createdBy) throws Exception {
		// domain element is a functionalizing entity
		if (compositionType.equals("functionalizing entity")) {
			className = ClassUtils
					.getShortClassNameFromDisplayName(entityDisplayName);
			Class clazz = ClassUtils.getFullClass("agentmaterial." + className);
			if (clazz == null) {
				clazz = OtherFunctionalizingEntity.class;
			}
			try {
				domainElement = (AssociatedElement) clazz.newInstance();
			} catch (ClassCastException ex) {
				String tmpType = compositionType;
				this.setCompositionType(null);
				throw new ClassCastException(tmpType);
			}
			domainElement.setId(new Long(entityId));
		} else {
			domainElement = composingElement;
		}
		if (domainElement.getId() != null && domainElement.getId() == 0) {
			domainElement.setId(null);
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

	private void updateType() {
		if (composingElement.getId() != null) {
			compositionType = ClassUtils.getDisplayName("NanomaterialEntity");
			NanomaterialEntity entity = composingElement
					.getNanomaterialEntity();
			if (entity instanceof OtherNanomaterialEntity) {
				entityDisplayName = ((OtherNanomaterialEntity) entity)
						.getType();
			} else {
				String entityClassName = ClassUtils
						.getShortClassName(composingElement
								.getNanomaterialEntity().getClass().getName());
				entityDisplayName = ClassUtils.getDisplayName(entityClassName);
			}
		} else {
			compositionType = ClassUtils
					.getDisplayName("FunctionalizingEntity");
			if (domainElement instanceof OtherFunctionalizingEntity) {
				entityDisplayName = ((OtherFunctionalizingEntity) domainElement)
						.getType();
			} else {
				entityDisplayName = ClassUtils.getDisplayName(className);
			}
		}
	}

	public void setCompositionType(String compositionType) {
		this.compositionType = compositionType;
	}
}

package gov.nih.nci.cananolab.dto.particle.composition;

import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.FunctionalizingEntity;

public class FunctionalizingEntityBean {
	private String entityType;

	private FunctionalizingEntity functionalizingEntity;

	private Integer entityNumber;

	public FunctionalizingEntityBean(FunctionalizingEntity functionalizingEntity) {
		entityType = functionalizingEntity.getClass().getCanonicalName();
	}

	public int compareTo(FunctionalizingEntityBean other) {
		return entityType.compareTo(other.getEntityType());
	}

	public Integer getEntityNumber() {
		return entityNumber;
	}

	public void setEntityNumber(Integer entityNumber) {
		this.entityNumber = entityNumber;
	}

	public FunctionalizingEntity getFunctionalizingEntity() {
		return functionalizingEntity;
	}

	public void setFunctionalizingEntity(
			FunctionalizingEntity functionalizingEntity) {
		this.functionalizingEntity = functionalizingEntity;
	}

	public String getEntityType() {
		return entityType;
	}

}

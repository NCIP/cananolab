package gov.nih.nci.cananolab.dto.particle.composition;

import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.FunctionalizingEntity;
import gov.nih.nci.cananolab.util.ClassUtils;

public class FunctionalizingEntityBean {
	private String entityType;

	private FunctionalizingEntity functionalizingEntity;

	private Integer entityNumber;

	private String entityClassName;

	public FunctionalizingEntityBean(FunctionalizingEntity functionalizingEntity) {
		entityClassName = ClassUtils.getShortClassName(functionalizingEntity
				.getClass().getCanonicalName());
		this.functionalizingEntity = functionalizingEntity;
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

	public String getEntityClassName() {
		return entityClassName;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

}

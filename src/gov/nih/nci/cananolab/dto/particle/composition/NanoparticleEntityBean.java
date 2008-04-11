package gov.nih.nci.cananolab.dto.particle.composition;

import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.NanoparticleEntity;

public class NanoparticleEntityBean {
	private String entityType;

	private NanoparticleEntity nanoparticleEntity;

	private Integer entityNumber;

	public NanoparticleEntityBean(NanoparticleEntity nanoparticleEntity) {
		this.nanoparticleEntity = nanoparticleEntity;
		entityType = nanoparticleEntity.getClass().getCanonicalName();
	}

	public int compareTo(NanoparticleEntityBean other) {
		return entityType.compareTo(other.getEntityType());
	}

	public Integer getEntityNumber() {
		return entityNumber;
	}

	public void setEntityNumber(Integer entityNumber) {
		this.entityNumber = entityNumber;
	}

	public NanoparticleEntity getNanoparticleEntity() {
		return nanoparticleEntity;
	}

	public void setNanoparticleEntity(NanoparticleEntity nanoparticleEntity) {
		this.nanoparticleEntity = nanoparticleEntity;
	}

	public String getEntityType() {
		return entityType;
	}

}

package gov.nih.nci.cananolab.security.enums;

public enum SecureClassesEnum
{
	SAMPLE(gov.nih.nci.cananolab.dto.particle.SampleBean.class),
	COMPOSITION(gov.nih.nci.cananolab.dto.particle.composition.CompositionBean.class),
	NANO(gov.nih.nci.cananolab.dto.particle.composition.NanomaterialEntityBean.class),
	COMPOSINGELEMENT(gov.nih.nci.cananolab.dto.particle.composition.ComposingElementBean.class),
	FUNCTIONALIZING(gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean.class),
	FUNCTION(gov.nih.nci.cananolab.dto.particle.composition.FunctionBean.class),
	CHEMASSOC(gov.nih.nci.cananolab.dto.particle.composition.ChemicalAssociationBean.class),
	CHAR(gov.nih.nci.cananolab.domain.particle.Characterization.class),
	CONFIG(gov.nih.nci.cananolab.domain.common.ExperimentConfig.class),
	FINDING(gov.nih.nci.cananolab.domain.common.Finding.class),
	PROTOCOL(gov.nih.nci.cananolab.dto.common.ProtocolBean.class),
	PUBLICATION(gov.nih.nci.cananolab.dto.common.PublicationBean.class),
	AUTHOR(gov.nih.nci.cananolab.domain.common.Author.class),
	FILE(gov.nih.nci.cananolab.dto.common.FileBean.class),
	ORG(gov.nih.nci.cananolab.domain.common.Organization.class),
	POC(gov.nih.nci.cananolab.domain.common.PointOfContact.class),
	COLLABORATIONGRP(gov.nih.nci.cananolab.dto.common.CollaborationGroupBean.class);
	
	private Class clazz;
	
	private SecureClassesEnum(Class clazz) {
		this.clazz = clazz;
	}
	
	public Class getClazz() {
		return clazz;
	}

	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}	

}

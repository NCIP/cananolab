package gov.nih.nci.cananolab.dto.particle.composition;

import gov.nih.nci.cananolab.domain.common.LabFile;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.chemicalassociation.Attachment;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.chemicalassociation.ChemicalAssociation;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.chemicalassociation.OtherChemicalAssociation;
import gov.nih.nci.cananolab.dto.common.LabFileBean;
import gov.nih.nci.cananolab.util.ClassUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class ChemicalAssociationBean {
	private String type;

	private String description;

	private String className;

	private ChemicalAssociation domainAssociation;

	private Attachment attachment = new Attachment();

	private List<LabFileBean> files = new ArrayList<LabFileBean>();

	private AssociatedElementBean associatedElementA;

	private AssociatedElementBean associatedElementB;

	private String createdBy;

	public ChemicalAssociationBean(ChemicalAssociation chemicalAssociation) {
		domainAssociation = chemicalAssociation;
		if (chemicalAssociation instanceof Attachment) {
			attachment = (Attachment) chemicalAssociation;
		} else if (chemicalAssociation instanceof OtherChemicalAssociation) {
			type = ((OtherChemicalAssociation) chemicalAssociation).getType();
		}
		description = chemicalAssociation.getDescription();
		className = ClassUtils.getShortClassName(chemicalAssociation.getClass()
				.getName());
		if (chemicalAssociation.getLabFileCollection() != null) {
			for (LabFile file : chemicalAssociation.getLabFileCollection()) {
				files.add(new LabFileBean(file));
			}
		}
		associatedElementA = new AssociatedElementBean(chemicalAssociation
				.getAssociatedElementA());
		associatedElementB = new AssociatedElementBean(chemicalAssociation
				.getAssociatedElementB());
	}

	public ChemicalAssociation getDomainAssociation() {
		return domainAssociation;
	}

	public void setDomainAssociation() throws Exception {
		if (domainAssociation == null) {
			Class clazz = ClassUtils.getFullClass(className);
			domainAssociation = (ChemicalAssociation) clazz.newInstance();
		}
		if (domainAssociation.getId() == null) {
			domainAssociation.setCreatedBy(createdBy);
			domainAssociation.setCreatedDate(new Date());
		}
		domainAssociation.setDescription(description);
		domainAssociation.setAssociatedElementA(associatedElementA
				.getDomainElement());
		domainAssociation.setAssociatedElementB(associatedElementB
				.getDomainElement());
		if (domainAssociation.getLabFileCollection() != null) {
			domainAssociation.getLabFileCollection().clear();
		} else {
			domainAssociation.setLabFileCollection(new HashSet<LabFile>());
		}
		for (LabFileBean file : files) {
			file.getDomainFile().setCreatedBy(createdBy);
			file.getDomainFile().setCreatedDate(new Date());
			domainAssociation.getLabFileCollection().add(file.getDomainFile());
		}
	}

	public String getType() {
		return type;
	}

	public Attachment getAttachment() {
		return attachment;
	}

	public List<LabFileBean> getFiles() {
		return files;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public AssociatedElementBean getAssociatedElementA() {
		return associatedElementA;
	}

	public AssociatedElementBean getAssociatedElementB() {
		return associatedElementB;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

}

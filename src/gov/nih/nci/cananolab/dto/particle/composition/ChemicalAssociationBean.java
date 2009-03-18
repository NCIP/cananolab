package gov.nih.nci.cananolab.dto.particle.composition;

import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.linkage.Attachment;
import gov.nih.nci.cananolab.domain.linkage.OtherChemicalAssociation;
import gov.nih.nci.cananolab.domain.particle.ChemicalAssociation;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.cananolab.util.Comparators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class ChemicalAssociationBean {
	private String type;

	private String description;

	private String className;

	private ChemicalAssociation domainAssociation;

	private Attachment attachment = new Attachment();

	private List<FileBean> files = new ArrayList<FileBean>();

	private AssociatedElementBean associatedElementA = new AssociatedElementBean();

	private AssociatedElementBean associatedElementB = new AssociatedElementBean();

	public ChemicalAssociationBean() {
	}

	public ChemicalAssociationBean(ChemicalAssociation chemicalAssociation) {
		domainAssociation = chemicalAssociation;
		if (chemicalAssociation instanceof Attachment) {
			attachment = (Attachment) chemicalAssociation;
		}
		description = chemicalAssociation.getDescription();
		className = ClassUtils.getShortClassName(chemicalAssociation.getClass()
				.getName());
		if (chemicalAssociation.getFileCollection() != null) {
			for (File file : chemicalAssociation.getFileCollection()) {
				files.add(new FileBean(file));
			}
			Collections.sort(files,
					new Comparators.FileBeanDateComparator());
		}
		associatedElementA = new AssociatedElementBean(chemicalAssociation
				.getAssociatedElementA());
		associatedElementB = new AssociatedElementBean(chemicalAssociation
				.getAssociatedElementB());
	}

	public ChemicalAssociation getDomainAssociation() {
		return domainAssociation;
	}

	public void setupDomainAssociation(Map<String, String> typeToClass,
			String createdBy, String internalUriPath) throws Exception {
		className = typeToClass.get(type.toLowerCase());
		Class clazz = null;
		if (className == null) {
			clazz = OtherChemicalAssociation.class;
		} else {
			clazz = ClassUtils.getFullClass(className);
		}
		if (domainAssociation == null) {
			try {
				domainAssociation = (ChemicalAssociation) clazz.newInstance();
			}catch (ClassCastException ex) {
				String tmpType = type;
				this.setType(null);
				throw new ClassCastException(tmpType);
			}
		}
		if (domainAssociation instanceof OtherChemicalAssociation) {
			((OtherChemicalAssociation) domainAssociation).setType(type);
		} else if (domainAssociation instanceof Attachment) {
			domainAssociation = attachment;
		}
		if (domainAssociation.getId() == null) {
			domainAssociation.setCreatedBy(createdBy);
			domainAssociation.setCreatedDate(new Date());
		}
		domainAssociation.setDescription(description);
		associatedElementA.setupDomainElement(typeToClass, createdBy);
		associatedElementB.setupDomainElement(typeToClass, createdBy);
		domainAssociation.setAssociatedElementA(associatedElementA
				.getDomainElement());
		domainAssociation.setAssociatedElementB(associatedElementB
				.getDomainElement());
		if (domainAssociation.getFileCollection() != null) {
			domainAssociation.getFileCollection().clear();
		} else {
			domainAssociation.setFileCollection(new HashSet<File>());
		}
		int i=0;
		for (FileBean file : files) {
			file.setupDomainFile(internalUriPath, createdBy, i);
			domainAssociation.getFileCollection().add(file.getDomainFile());
			i++;
		}
	}

	public String getType() {
		return type;
	}

	public Attachment getAttachment() {
		return attachment;
	}

	public List<FileBean> getFiles() {
		return files;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getClassName() {
		return className;
	}

	public AssociatedElementBean getAssociatedElementA() {
		return associatedElementA;
	}

	public AssociatedElementBean getAssociatedElementB() {
		return associatedElementB;
	}

	public void addFile() {
		files.add(new FileBean());
	}

	public void removeFile(int ind) {
		files.remove(ind);
	}

	public void setType(String type) {
		this.type = type;
	}

	public void updateType(Map<String, String> classToType) {
		if (domainAssociation instanceof OtherChemicalAssociation) {
			type = ((OtherChemicalAssociation) domainAssociation).getType();
		} else {
			type = classToType.get(className);
		}
		associatedElementA.updateType(classToType);
		associatedElementB.updateType(classToType);
	}
}

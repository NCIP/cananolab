package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.ChemicalAssociation;
import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.linkage.Attachment;
import gov.nih.nci.cananolab.domain.linkage.OtherChemicalAssociation;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.cananolab.util.Comparators;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;

public class ChemicalAssociationBean extends MaterialBean {
	private ChemicalAssociation domain;
	private String type;
	private String className;

	private Attachment attachment = new Attachment();

	private MaterialBean materialA = new MaterialBean();

	private MaterialBean materialB = new MaterialBean();

	public ChemicalAssociationBean() {
	}

	public ChemicalAssociationBean(ChemicalAssociation chemicalAssociation) {
		domain = chemicalAssociation;
		if (chemicalAssociation instanceof Attachment) {
			attachment = (Attachment) chemicalAssociation;
		}
		className = ClassUtils.getShortClassName(chemicalAssociation.getClass()
				.getName());
		if (chemicalAssociation.getFileCollection() != null) {
			for (File file : chemicalAssociation.getFileCollection()) {
				files.add(new FileBean(file));
			}
			Collections.sort(files, new Comparators.FileBeanDateComparator());
		}
		materialA = new MaterialBean(chemicalAssociation.getMaterialA());
		materialB = new MaterialBean(chemicalAssociation.getMaterialB());
		updateType();
	}

	public void setDomain(ChemicalAssociation domain) {
		this.domain = domain;
	}

	public String getType() {
		return type;
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

	public MaterialBean getMaterialA() {
		return materialA;
	}

	public void setMaterialA(MaterialBean materialA) {
		this.materialA = materialA;
	}

	public MaterialBean getMaterialB() {
		return materialB;
	}

	public void setMaterialB(MaterialBean materialB) {
		this.materialB = materialB;
	}

	public void setAttachment(Attachment attachment) {
		this.attachment = attachment;
	}

	public ChemicalAssociation getdomain() {
		return domain;
	}

	public void setupdomain(String createdBy) throws Exception {
		className = ClassUtils.getShortClassNameFromDisplayName(type);
		Class clazz = ClassUtils.getFullClass(className);
		if (clazz == null) {
			clazz = OtherChemicalAssociation.class;
		}
		if (domain == null) {
			try {
				domain = (ChemicalAssociation) clazz.newInstance();
			} catch (ClassCastException ex) {
				String tmpType = type;
				this.setType(null);
				throw new ClassCastException(tmpType);
			}
		}
		if (domain instanceof OtherChemicalAssociation) {
			((OtherChemicalAssociation) domain).setType(type);
		} else if (domain instanceof Attachment) {
			domain = attachment;
		}
		if (domain.getId() == null) {
			domain.setCreatedBy(createdBy);
			domain.setCreatedDate(new Date());
		}
		materialA.setupDomain(createdBy);
		materialB.setupDomain(createdBy);
		domain.setMaterialA(materialA.getDomain());
		domain.setMaterialB(materialB.getDomain());
		if (domain.getFileCollection() != null) {
			domain.getFileCollection().clear();
		} else {
			domain.setFileCollection(new HashSet<File>());
		}
		for (FileBean file : files) {
			domain.getFileCollection().add(file.getDomainFile());
		}
	}

	public Attachment getAttachment() {
		return attachment;
	}

	private void updateType() {
		if (domain instanceof OtherChemicalAssociation) {
			type = ((OtherChemicalAssociation) domain).getType();
		} else {
			type = ClassUtils.getDisplayName(className);
		}
	}
}

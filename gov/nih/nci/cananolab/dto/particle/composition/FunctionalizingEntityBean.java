package gov.nih.nci.cananolab.dto.particle.composition;

import gov.nih.nci.cananolab.domain.common.LabFile;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.Function;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.NanoparticleEntity;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.ActivationMethod;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.Antibody;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.Biopolymer;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.FunctionalizingEntity;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.OtherFunctionalizingEntity;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.SmallMolecule;
import gov.nih.nci.cananolab.util.ClassUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents the view bean for the FunctionalizingEntity domain object
 * 
 * @author pansu
 * 
 */
public class FunctionalizingEntityBean {
	private String type;

	private String description;

	private String molecularFormula;

	private String molecularFormulaType;

	private String name;

	private Float value;

	private String valueUnit;

	private SmallMolecule smallMolecule = new SmallMolecule();

	private Antibody antibody = new Antibody();

	private Biopolymer biopolymer = new Biopolymer();

	private FunctionalizingEntity domainEntity;

	private String className;

	private List<FunctionBean> functions = new ArrayList<FunctionBean>();

	private Set<LabFile> files = new HashSet<LabFile>();

	private ActivationMethod activationMethod = new ActivationMethod();

	private String createdBy;

	public FunctionalizingEntityBean() {

	}

	public FunctionalizingEntityBean(FunctionalizingEntity functionalizingEntity) {
		description = functionalizingEntity.getDescription();
		name = functionalizingEntity.getName();
		molecularFormula = functionalizingEntity.getMolecularFormula();
		molecularFormulaType = functionalizingEntity.getMolecularFormulaType();
		value = functionalizingEntity.getValue();
		valueUnit = functionalizingEntity.getValueUnit();
		domainEntity = functionalizingEntity;
		if (functionalizingEntity instanceof Antibody) {
			antibody = (Antibody) functionalizingEntity;
		} else if (functionalizingEntity instanceof SmallMolecule) {
			smallMolecule = (SmallMolecule) functionalizingEntity;
		} else if (functionalizingEntity instanceof Biopolymer) {
			biopolymer = (Biopolymer) functionalizingEntity;
		} else if (functionalizingEntity instanceof OtherFunctionalizingEntity) {
			type = ((OtherFunctionalizingEntity) functionalizingEntity)
					.getType();
		}
		className = ClassUtils.getShortClassName(functionalizingEntity
				.getClass().getName());
		for (Function function : functionalizingEntity.getFunctionCollection()) {
			functions.add(new FunctionBean(function));
		}
		activationMethod = functionalizingEntity.getActivationMethod();
	}

	public FunctionalizingEntity getDomainEntity() {
		return domainEntity;
	}

	public String getType() {
		return type;
	}

	public String getClassName() {
		return className;
	}

	public void setType(String entityType) {
		this.type = entityType;
	}

	public Antibody getAntibody() {
		domainEntity = antibody;
		return antibody;
	}

	public Biopolymer getBiopolymer() {
		domainEntity = biopolymer;
		return biopolymer;
	}

	public SmallMolecule getSmallMolecule() {
		domainEntity = smallMolecule;
		return smallMolecule;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public List<FunctionBean> getFunctions() {
		return functions;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMolecularFormula() {
		return molecularFormula;
	}

	public void setMolecularFormula(String molecularFormula) {
		this.molecularFormula = molecularFormula;
	}

	public String getMolecularFormulaType() {
		return molecularFormulaType;
	}

	public void setMolecularFormulaType(String molecularFormulaType) {
		this.molecularFormulaType = molecularFormulaType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Float getValue() {
		return value;
	}

	public void setValue(Float value) {
		this.value = value;
	}

	public String getValueUnit() {
		return valueUnit;
	}

	public void setValueUnit(String valueUnit) {
		this.valueUnit = valueUnit;
	}

	public Set<LabFile> getFiles() {
		return files;
	}

	public ActivationMethod getActivationMethod() {
		return activationMethod;
	}

	public void setDomainEntity() {
		try {
			// take care of nanoparticle entities that don't have any special
			// properties shown in the form, e.g. OtherFunctionalizingEntity
			if (domainEntity == null) {
				Class clazz = ClassUtils.getFullClass(className);
				domainEntity = (FunctionalizingEntity) clazz.newInstance();
			}
			if (domainEntity.getId() == null) {
				domainEntity.setCreatedBy(createdBy);
				domainEntity.setCreatedDate(new Date());
			}
			if (domainEntity instanceof OtherFunctionalizingEntity) {
				((OtherFunctionalizingEntity) domainEntity).setType(type);
			}
			domainEntity.setDescription(description);
			domainEntity.setMolecularFormula(molecularFormula);
			domainEntity.setMolecularFormulaType(molecularFormulaType);
			domainEntity.setName(name);
			domainEntity.setValue(value);
			domainEntity.setValueUnit(valueUnit);
			domainEntity.setActivationMethod(activationMethod);

			if (domainEntity.getId() == null) {
				domainEntity.setCreatedBy(createdBy);
				domainEntity.setCreatedDate(new Date());
			}

			if (domainEntity.getFunctionCollection() != null) {
				domainEntity.getFunctionCollection().clear();
			} else {
				domainEntity.setFunctionCollection(new HashSet<Function>());
			}
			for (FunctionBean functionBean : functions) {
				domainEntity.getFunctionCollection().add(
						functionBean.getDomainFunction());
				// TODO set function date
			}
			if (domainEntity.getLabFileCollection() != null) {
				domainEntity.getLabFileCollection().clear();
			} else {
				domainEntity.setLabFileCollection(new HashSet<LabFile>());
			}
			for (LabFile file : files) {
				file.setCreatedBy(createdBy);
				file.setCreatedDate(new Date());
				domainEntity.getLabFileCollection().add(file);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addFunction() {
		functions.add(new FunctionBean());
	}

	public void removeFunction(int ind) {
		functions.remove(ind);
	}

	public void addFile() {
		files.add(new LabFile());
	}

	public void removeFile(int ind) {
		files.remove(ind);
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
}

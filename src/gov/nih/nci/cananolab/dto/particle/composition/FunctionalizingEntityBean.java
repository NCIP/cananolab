package gov.nih.nci.cananolab.dto.particle.composition;

import gov.nih.nci.cananolab.domain.common.LabFile;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.Function;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.ActivationMethod;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.Antibody;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.Biopolymer;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.FunctionalizingEntity;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.OtherFunctionalizingEntity;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.SmallMolecule;
import gov.nih.nci.cananolab.util.ClassUtils;

import java.util.ArrayList;
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

	private SmallMolecule smallMolecule;

	private Antibody antibody;

	private Biopolymer biopolymer;

	private OtherFunctionalizingEntity otherEntity;

	private FunctionalizingEntity domainEntity;

	private String className;

	private List<FunctionBean> functions = new ArrayList<FunctionBean>();

	private Set<LabFile> files = new HashSet<LabFile>();

	private ActivationMethod activationMethod = new ActivationMethod();

	public FunctionalizingEntityBean() {

	}

	public FunctionalizingEntityBean(FunctionalizingEntity functionalizingEntity) {
		description = functionalizingEntity.getDescription();
		name = functionalizingEntity.getName();
		molecularFormula = functionalizingEntity.getMolecularFormula();
		molecularFormulaType = functionalizingEntity.getMolecularFormulaType();
		value = functionalizingEntity.getValue();
		valueUnit = functionalizingEntity.getValueUnit();
		if (functionalizingEntity instanceof Antibody) {
			antibody = (Antibody) functionalizingEntity;
			domainEntity = antibody;
			className = ClassUtils.getShortClassName(Antibody.class.getName());
		} else if (functionalizingEntity instanceof SmallMolecule) {
			smallMolecule = (SmallMolecule) functionalizingEntity;
			domainEntity = smallMolecule;
			className = ClassUtils.getShortClassName(SmallMolecule.class
					.getName());
		} else if (functionalizingEntity instanceof Biopolymer) {
			biopolymer = (Biopolymer) functionalizingEntity;
			domainEntity = biopolymer;
			className = ClassUtils
					.getShortClassName(Biopolymer.class.getName());
		} else if (functionalizingEntity instanceof OtherFunctionalizingEntity) {
			otherEntity = (OtherFunctionalizingEntity) functionalizingEntity;
			domainEntity = otherEntity;
			className = ClassUtils
					.getShortClassName(OtherFunctionalizingEntity.class
							.getName());
		}
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
		setSharedInfo();
		return antibody;
	}

	public Biopolymer getBiopolymer() {
		domainEntity = biopolymer;
		setSharedInfo();
		return biopolymer;
	}

	public OtherFunctionalizingEntity getOtherEntity() {
		domainEntity = otherEntity;
		setSharedInfo();
		return otherEntity;
	}

	public SmallMolecule getSmallMolecule() {
		domainEntity = smallMolecule;
		setSharedInfo();
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

	public void setSharedInfo() {
		domainEntity.setDescription(description);
		domainEntity.setActivationMethod(activationMethod);
		domainEntity.setMolecularFormula(molecularFormula);
		domainEntity.setMolecularFormulaType(molecularFormulaType);
		domainEntity.setValue(value);
		domainEntity.setValueUnit(valueUnit);
		domainEntity.setName(name);
		
		domainEntity.getFunctionCollection().clear();
		for (FunctionBean functionBean : functions) {
			domainEntity.getFunctionCollection().add(
					functionBean.getDomainFunction());
		}

		domainEntity.setLabFileCollection(files);
		if (domainEntity.getId() != null && domainEntity.getId() == 0) {
			domainEntity.setId(null);
		}
	}
	
	public void addFunction() {
		functions.add(new FunctionBean());
	}
	
	public void removeFunction(int ind) {
		functions.remove(ind);
	}
}

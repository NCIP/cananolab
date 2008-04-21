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

	private String value;

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
		className = ClassUtils.getShortClassName(functionalizingEntity
				.getClass().getCanonicalName());
		this.domainEntity = functionalizingEntity;
	}

	public int compareTo(FunctionalizingEntityBean other) {
		return type.compareTo(other.getType());
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
		return antibody;
	}

	public void setAntibody(Antibody antibody) {
		this.antibody = antibody;
		domainEntity = antibody;
	}

	public Biopolymer getBiopolymer() {
		return biopolymer;
	}

	public void setBiopolymer(Biopolymer biopolymer) {
		this.biopolymer = biopolymer;
		domainEntity = biopolymer;
	}

	public OtherFunctionalizingEntity getOtherEntity() {
		return otherEntity;
	}

	public void setOtherEntity(OtherFunctionalizingEntity otherEntity) {
		this.otherEntity = otherEntity;
		domainEntity = otherEntity;
	}

	public SmallMolecule getSmallMolecule() {
		return smallMolecule;
	}

	public void setSmallMolecule(SmallMolecule smallMolecule) {
		this.smallMolecule = smallMolecule;
		domainEntity = smallMolecule;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public List<FunctionBean> getFunctions() {
		return functions;
	}

	public void setFunctions(List<FunctionBean> functions) {
		this.functions = functions;
		Set<Function> domainFunctions = new HashSet<Function>();
		for (FunctionBean functionBean : functions) {
			domainFunctions.add(functionBean.getDomainFunction());
		}
		domainEntity.setFunctionCollection(domainFunctions);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		domainEntity.setDescription(description);
	}

	public String getMolecularFormula() {
		return molecularFormula;
	}

	public void setMolecularFormula(String molecularFormula) {
		this.molecularFormula = molecularFormula;
		domainEntity.setMolecularFormula(molecularFormula);
	}

	public String getMolecularFormulaType() {
		return molecularFormulaType;
	}

	public void setMolecularFormulaType(String molecularFormulaType) {
		this.molecularFormulaType = molecularFormulaType;
		domainEntity.setMolecularFormulaType(molecularFormulaType);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		domainEntity.setName(name);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
		domainEntity.setValue(new Float(value));
	}

	public String getValueUnit() {
		return valueUnit;
	}

	public void setValueUnit(String valueUnit) {
		this.valueUnit = valueUnit;
		domainEntity.setValueUnit(valueUnit);
	}

	public Set<LabFile> getFiles() {
		return files;
	}

	public void setFiles(Set<LabFile> files) {
		this.files = files;
		domainEntity.setLabFileCollection(files);
	}

	public ActivationMethod getActivationMethod() {
		return activationMethod;
	}

	public void setActivationMethod(ActivationMethod activationMethod) {
		this.activationMethod = activationMethod;
		domainEntity.setActivationMethod(activationMethod);
	}
}

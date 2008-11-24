package gov.nih.nci.cananolab.dto.particle.composition;

import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.Function;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.Target;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.TargetingFunction;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.ActivationMethod;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.Antibody;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.Biopolymer;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.FunctionalizingEntity;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.OtherFunctionalizingEntity;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.SmallMolecule;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.util.CaNanoLabComparators;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;
import gov.nih.nci.cananolab.util.ClassUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

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

	private List<FileBean> files = new ArrayList<FileBean>();

	private ActivationMethod activationMethod = new ActivationMethod();

	public FunctionalizingEntityBean() {
		if (functions.size() == 0) {
			FunctionBean funcBean = new FunctionBean();
			functions.add(funcBean);
		}
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
		}
		className = ClassUtils.getShortClassName(functionalizingEntity
				.getClass().getName());
		if (functionalizingEntity.getFunctionCollection() != null) {
			for (Function function : functionalizingEntity
					.getFunctionCollection()) {
				functions.add(new FunctionBean(function));
			}
		}
		Collections.sort(functions,
				new CaNanoLabComparators.FunctionBeanDateComparator());

		if (functionalizingEntity.getActivationMethod() != null) {
			activationMethod = functionalizingEntity.getActivationMethod();
		}
		if (functionalizingEntity.getFileCollection() != null) {
			for (File file : functionalizingEntity.getFileCollection()) {
				files.add(new FileBean(file));
			}
		}
		Collections.sort(files,
				new CaNanoLabComparators.FileBeanDateComparator());

	}

	public FunctionalizingEntity getDomainCopy() {
		FunctionalizingEntity copy = (FunctionalizingEntity) ClassUtils
				.deepCopy(domainEntity);
		// clear Id
		copy.setId(null);
		copy.setCreatedBy(CaNanoLabConstants.AUTO_COPY_ANNOTATION_PREFIX);
		copy.setCreatedDate(new Date());
		copy.getActivationMethod().setId(null);
		if (copy.getFunctionCollection().isEmpty()) {
			copy.setFunctionCollection(null);
		} else {
			Collection<Function> functions = copy.getFunctionCollection();
			copy.setFunctionCollection(new HashSet<Function>());
			copy.getFunctionCollection().addAll(functions);
			for (Function function : copy.getFunctionCollection()) {
				function.setId(null);
				function
						.setCreatedBy(CaNanoLabConstants.AUTO_COPY_ANNOTATION_PREFIX);
				function.setCreatedDate(new Date());
				if (function instanceof TargetingFunction) {
					if (((TargetingFunction) function).getTargetCollection()
							.isEmpty()) {
						((TargetingFunction) function)
								.setTargetCollection(null);
					} else {
						Collection<Target> targets = ((TargetingFunction) function)
								.getTargetCollection();
						((TargetingFunction) function)
								.setTargetCollection(new HashSet<Target>());
						((TargetingFunction) function).getTargetCollection()
								.addAll(targets);
						for (Target target : ((TargetingFunction) function)
								.getTargetCollection()) {
							target.setId(null);
							target
									.setCreatedBy(CaNanoLabConstants.AUTO_COPY_ANNOTATION_PREFIX);
							target.setCreatedDate(new Date());
						}
					}
				}
			}
		}
		if (copy.getFileCollection().isEmpty()) {
			copy.setFileCollection(null);
		} else {
			Collection<File> files = copy.getFileCollection();
			copy.setFileCollection(new HashSet<File>());
			copy.getFileCollection().addAll(files);
			for (File file : copy.getFileCollection()) {
				file.setId(null);
				file
						.setCreatedBy(CaNanoLabConstants.AUTO_COPY_ANNOTATION_PREFIX);
				file.setCreatedDate(new Date());
			}
		}
		return copy;
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

	public Antibody getAntibody() {
		return antibody;
	}

	public Biopolymer getBiopolymer() {
		return biopolymer;
	}

	public SmallMolecule getSmallMolecule() {
		return smallMolecule;
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

	public List<FileBean> getFiles() {
		return files;
	}

	public ActivationMethod getActivationMethod() {
		return activationMethod;
	}

	public void setupDomainEntity(Map<String, String> typeToClass,
			String createdBy, String internalUriPath) throws Exception {
		className = typeToClass.get(type.toLowerCase());
		Class clazz = null;
		if (className == null) {
			clazz = OtherFunctionalizingEntity.class;
		} else {
			clazz = ClassUtils.getFullClass("functionalization." + className);
		}
		if (domainEntity == null) {
			domainEntity = (FunctionalizingEntity) clazz.newInstance();
		}
		if (domainEntity instanceof OtherFunctionalizingEntity) {
			((OtherFunctionalizingEntity) domainEntity).setType(type);
		} else if (domainEntity instanceof Antibody) {
			domainEntity = antibody;
		} else if (domainEntity instanceof SmallMolecule) {
			domainEntity = smallMolecule;
		} else if (domainEntity instanceof Biopolymer) {
			domainEntity = biopolymer;
		}
		domainEntity.setDescription(description);
		domainEntity.setMolecularFormula(molecularFormula);
		domainEntity.setMolecularFormulaType(molecularFormulaType);
		domainEntity.setName(name);
		domainEntity.setValue(value);
		domainEntity.setValueUnit(valueUnit);
		
		if (activationMethod != null
				&& ((activationMethod.getActivationEffect() != null && activationMethod
						.getActivationEffect().trim().length() > 0) || (activationMethod
						.getType() != null && activationMethod.getType().trim()
						.length() > 0))) {
			domainEntity.setActivationMethod(activationMethod);
		} else {
			domainEntity.setActivationMethod(null);
		}		
		
		if (domainEntity.getId() == null
				|| domainEntity.getCreatedBy() != null
				&& domainEntity.getCreatedBy().equals(
						CaNanoLabConstants.AUTO_COPY_ANNOTATION_PREFIX)) {
			domainEntity.setCreatedBy(createdBy);
			domainEntity.setCreatedDate(new Date());
		}
		if (domainEntity.getFunctionCollection() != null) {
			domainEntity.getFunctionCollection().clear();
		} else {
			domainEntity.setFunctionCollection(new HashSet<Function>());
		}
		int i = 0;
		for (FunctionBean functionBean : functions) {
			functionBean.setupDomainFunction(typeToClass, createdBy, i);
			domainEntity.getFunctionCollection().add(
					functionBean.getDomainFunction());
			i++;
		}
		if (domainEntity.getFileCollection() != null) {
			domainEntity.getFileCollection().clear();
		} else {
			domainEntity.setFileCollection(new HashSet<File>());
		}
		int j=0;
		for (FileBean file : files) {
			file.setupDomainFile(internalUriPath, createdBy, j);
			domainEntity.getFileCollection().add(file.getDomainFile());
			j++;
		}
	}

	public void addFunction() {
		functions.add(new FunctionBean());
	}

	public void removeFunction(int ind) {
		functions.remove(ind);
	}

	public void addFile() {
		files.add(new FileBean());
	}

	public void removeFile(int ind) {
		files.remove(ind);
	}

	public void updateType(Map<String, String> classToType) {
		if (domainEntity instanceof OtherFunctionalizingEntity) {
			type = ((OtherFunctionalizingEntity) domainEntity).getType();
		} else {
			type = classToType.get(className);
		}
		// set function type and target type
		for (FunctionBean functionBean : getFunctions()) {
			functionBean.updateType(classToType);
		}
	}

	public void setType(String type) {
		this.type = type;
	}

}

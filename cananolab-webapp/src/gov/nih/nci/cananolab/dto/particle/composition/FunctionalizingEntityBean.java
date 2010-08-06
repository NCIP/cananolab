package gov.nih.nci.cananolab.dto.particle.composition;

import gov.nih.nci.cananolab.domain.agentmaterial.Antibody;
import gov.nih.nci.cananolab.domain.agentmaterial.Biopolymer;
import gov.nih.nci.cananolab.domain.agentmaterial.OtherFunctionalizingEntity;
import gov.nih.nci.cananolab.domain.agentmaterial.SmallMolecule;
import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.function.ImagingFunction;
import gov.nih.nci.cananolab.domain.function.TargetingFunction;
import gov.nih.nci.cananolab.domain.particle.ActivationMethod;
import gov.nih.nci.cananolab.domain.particle.Function;
import gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.cananolab.util.Comparators;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.SampleConstants;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Represents the view bean for the FunctionalizingEntity domain object
 *
 * @author pansu
 *
 */
public class FunctionalizingEntityBean extends BaseCompositionEntityBean {
	private String molecularFormula;

	private String molecularFormulaType;

	private String name;

	private String value;

	private String valueUnit;

	private String pubChemDataSourceName;

	private String pubChemId;

	private SmallMolecule smallMolecule = new SmallMolecule();

	private Antibody antibody = new Antibody();

	private Biopolymer biopolymer = new Biopolymer();

	private FunctionalizingEntity domainEntity;

	private List<FunctionBean> functions = new ArrayList<FunctionBean>();

	private ActivationMethod activationMethod = new ActivationMethod();

	private boolean withProperties = false;

	private boolean withImagingFunction = false;

	private boolean withTargetingFunction = false;

	private FunctionBean theFunction = new FunctionBean();

	private String pubChemLink;

	public FunctionalizingEntityBean() {
	}

	public FunctionalizingEntityBean(FunctionalizingEntity functionalizingEntity) {
		description = functionalizingEntity.getDescription();
		name = functionalizingEntity.getName();
		molecularFormula = functionalizingEntity.getMolecularFormula();
		molecularFormulaType = functionalizingEntity.getMolecularFormulaType();
		if (functionalizingEntity.getValue() != null) {
			value = functionalizingEntity.getValue().toString();
		}
		valueUnit = functionalizingEntity.getValueUnit();
		pubChemDataSourceName = functionalizingEntity
				.getPubChemDataSourceName();
		if (functionalizingEntity.getPubChemId() != null) {
			pubChemId = functionalizingEntity.getPubChemId().toString();
		}
		domainEntity = functionalizingEntity;
		if (functionalizingEntity instanceof Antibody) {
			antibody = (Antibody) functionalizingEntity;
			withProperties = true;
		} else if (functionalizingEntity instanceof SmallMolecule) {
			smallMolecule = (SmallMolecule) functionalizingEntity;
			withProperties = true;
		} else if (functionalizingEntity instanceof Biopolymer) {
			biopolymer = (Biopolymer) functionalizingEntity;
			withProperties = true;
		}
		className = ClassUtils.getShortClassName(functionalizingEntity
				.getClass().getName());
		Collection<Function> funcs = functionalizingEntity
				.getFunctionCollection();
		if (funcs != null) {
			for (Function function : funcs) {
				functions.add(new FunctionBean(function));
				if (function instanceof ImagingFunction) {
					withImagingFunction = true;
				}
				if (function instanceof TargetingFunction) {
					withTargetingFunction = true;
				}
			}
		}
		Collections.sort(functions,
				new Comparators.FunctionBeanDateComparator());

		if (functionalizingEntity.getActivationMethod() != null) {
			activationMethod = functionalizingEntity.getActivationMethod();
		}
		if (functionalizingEntity.getFileCollection() != null) {
			for (File file : functionalizingEntity.getFileCollection()) {
				files.add(new FileBean(file));
			}
		}
		Collections.sort(files, new Comparators.FileBeanDateComparator());
		updateType();
	}

	public FunctionalizingEntity getDomainCopy() {
		FunctionalizingEntity copy = (FunctionalizingEntity) ClassUtils
				.deepCopy(domainEntity);
		resetDomainCopy(copy);
		return copy;
	}

	public void resetDomainCopy(FunctionalizingEntity copy) {
		// append original ID to assist with chemical association copy
		copy.setCreatedBy(Constants.AUTO_COPY_ANNOTATION_PREFIX + ":"
				+ copy.getId());
		// clear Id
		copy.setId(null);
		// because activationMethod can't be shared.
		if (copy.getActivationMethod() != null) {
			copy.getActivationMethod().setId(null);
		}
		Collection<Function> oldFunctions = copy.getFunctionCollection();
		if (oldFunctions == null || oldFunctions.isEmpty()) {
			copy.setFunctionCollection(null);
		} else {
			copy.setFunctionCollection(new HashSet<Function>(oldFunctions));
			for (Function function : copy.getFunctionCollection()) {
				FunctionBean functionBean = new FunctionBean(function);
				functionBean.resetDomainCopy(function);
			}
		}
		Collection<File> oldFiles = copy.getFileCollection();
		if (oldFiles == null || oldFiles.isEmpty()) {
			copy.setFileCollection(null);
		} else {
			copy.setFileCollection(new HashSet<File>(oldFiles));
			for (File file : copy.getFileCollection()) {
				FileBean fileBean = new FileBean(file);
				fileBean.resetDomainCopy(file);
			}
		}
	}

	public FunctionalizingEntity getDomainEntity() {
		return domainEntity;
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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValueUnit() {
		return valueUnit;
	}

	public void setValueUnit(String valueUnit) {
		this.valueUnit = valueUnit;
	}

	public ActivationMethod getActivationMethod() {
		return activationMethod;
	}

	public void setupDomainEntity(String createdBy) throws Exception {
		className = ClassUtils.getShortClassNameFromDisplayName(type);
		Class clazz = ClassUtils.getFullClass("agentmaterial." + className);
		if (clazz == null) {
			clazz = OtherFunctionalizingEntity.class;
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
		if (!StringUtils.isEmpty(value)) {
			domainEntity.setValue(new Float(value));
		} else {
			domainEntity.setValue(null);
		}
		domainEntity.setValueUnit(valueUnit);
		domainEntity.setPubChemDataSourceName(pubChemDataSourceName);
		if (!StringUtils.isEmpty(pubChemId)) {
			domainEntity.setPubChemId(new Long(pubChemId));
		} else {
			domainEntity.setPubChemId(null);
		}
		if (activationMethod != null
				&& ((activationMethod.getActivationEffect() != null && activationMethod
						.getActivationEffect().trim().length() > 0) || (activationMethod
						.getType() != null && activationMethod.getType().trim()
						.length() > 0))) {
			domainEntity.setActivationMethod(activationMethod);
		} else {
			domainEntity.setActivationMethod(null);
		}

		// updated created_date and created_by if id is null
		if (domainEntity.getId() == null) {
			domainEntity.setCreatedBy(createdBy);
			domainEntity.setCreatedDate(Calendar.getInstance().getTime());
		}
		// updated created_by if created_by contains copy, but keep the original
		// created_date
		if (domainEntity.getId() != null
				|| !StringUtils.isEmpty(domainEntity.getCreatedBy())
				&& domainEntity.getCreatedBy().contains(
						Constants.AUTO_COPY_ANNOTATION_PREFIX)) {
			domainEntity.setCreatedBy(createdBy);
		}
		if (domainEntity.getFunctionCollection() != null) {
			domainEntity.getFunctionCollection().clear();
		} else {
			domainEntity.setFunctionCollection(new HashSet<Function>());
		}
		for (FunctionBean functionBean : functions) {
			domainEntity.getFunctionCollection().add(
					functionBean.getDomainFunction());
			functionBean.getDomainFunction().setFunctionalizingEntity(
					domainEntity);
		}
		if (domainEntity.getFileCollection() != null) {
			domainEntity.getFileCollection().clear();
		} else {
			domainEntity.setFileCollection(new HashSet<File>());
		}
		for (FileBean file : files) {
			domainEntity.getFileCollection().add(file.getDomainFile());
		}
	}

	private void updateType() {
		if (domainEntity instanceof OtherFunctionalizingEntity) {
			type = ((OtherFunctionalizingEntity) domainEntity).getType();
		} else {
			type = ClassUtils.getDisplayName(className);
		}
	}

	public String getMolecularFormulaDisplayName() {
		StringBuffer buffer = new StringBuffer();
		if (!StringUtils.isEmpty(molecularFormula)) {
			buffer.append(molecularFormula);
			if (!StringUtils.isEmpty(molecularFormulaType)) {
				buffer.append(" (");
				buffer.append(molecularFormulaType);
				buffer.append(")");
			}
		}
		return buffer.toString();
	}

	public String[] getFunctionDisplayNames() {
		List<String> displayNames = new ArrayList<String>();
		for (FunctionBean function : functions) {
			displayNames.add(function.getDisplayName());
		}
		if (displayNames.isEmpty()) {
			return null;
		}
		return displayNames.toArray(new String[displayNames.size()]);
	}

	public boolean isWithProperties() {
		return withProperties;
	}

	public String getActivationMethodDisplayName() {
		StringBuffer buffer = new StringBuffer();
		if (getDomainEntity().getActivationMethod() != null) {
			if (!StringUtils.isEmpty(getDomainEntity().getActivationMethod()
					.getType())) {
				buffer
						.append(getDomainEntity().getActivationMethod()
								.getType());
			}
			if (!StringUtils.isEmpty(getDomainEntity().getActivationMethod()
					.getActivationEffect())) {
				buffer.append(", ");
				buffer.append(getDomainEntity().getActivationMethod()
						.getActivationEffect());
			}
		}
		return buffer.toString();
	}

	// for use in chemical association submission form
	public String getDisplayName() {
		StringBuffer buffer = new StringBuffer();
		if (!StringUtils.isEmpty(getType())) {
			buffer.append(getType());
		}
		if (!StringUtils.isEmpty(getDomainEntity().getName())) {
			buffer.append(" (name: ");
			buffer.append(getDomainEntity().getName());
			if (getDomainEntity().getValue() != null) {
				buffer.append(", amount: ");
				buffer.append(getDomainEntity().getValue());
				if (!StringUtils.isEmpty(getDomainEntity().getValueUnit())) {
					buffer.append(" ");
					buffer.append(getDomainEntity().getValueUnit());
				}
			}
			buffer.append(")");
		}
		return buffer.toString();
	}

	public String getAdvancedSearchDisplayName() {
		StringBuffer buffer = new StringBuffer();
		if (!StringUtils.isEmpty(getType())) {
			buffer.append(getType());
		}
		if (!StringUtils.isEmpty(getDomainEntity().getName())) {
			buffer.append(" (");
			buffer.append(getDomainEntity().getName());
			buffer.append(")");
		}
		return buffer.toString();
	}

	public boolean isWithImagingFunction() {
		return withImagingFunction;
	}

	public boolean isWithTargetingFunction() {
		return withTargetingFunction;
	}

	public FunctionBean getTheFunction() {
		return theFunction;
	}

	public void setTheFunction(FunctionBean theFunction) {
		this.theFunction = theFunction;
	}

	public void addFunction(FunctionBean function) {
		int index = functions.indexOf(function);
		if (index != -1) {
			functions.remove(function);
			// retain the original order
			functions.add(index, function);
		} else {
			functions.add(function);
		}
	}

	public void removeFunction(FunctionBean function) {
		functions.remove(function);
	}

	// used for DWR ajax in bodySubmitChemicalAssociation.jsp
	public String getDomainId() {
		if (getDomainEntity().getId() != null) {
			return getDomainEntity().getId().toString();
		} else {
			return null;
		}
	}

	public String getPubChemDataSourceName() {
		return pubChemDataSourceName;
	}

	public String getPubChemId() {
		return pubChemId;
	}

	public void setPubChemDataSourceName(String pubChemDataSourceName) {
		this.pubChemDataSourceName = pubChemDataSourceName;
	}

	public void setPubChemId(String pubChemId) {
		this.pubChemId = pubChemId;
	}

	public String getPubChemLink() {
		if (!StringUtils.isEmpty(pubChemId)
				&& !StringUtils.isEmpty(pubChemDataSourceName)) {
			pubChemLink = CompositionBean.getPubChemURL(pubChemDataSourceName,
					new Long(pubChemId));
		}
		return pubChemLink;
	}

	/**
	 * Get PubChem URL in format of
	 * http://pubchem.ncbi.nlm.nih.gov/summary/summary.cgi?pubChemDS=pubchemId
	 *
	 * @param pubChemDS
	 * @param pubChemId
	 * @return PubChem URL
	 */
	public static String getPubChemURL(String pubChemDS, Long pubChemId) {
		StringBuffer sb = new StringBuffer(SampleConstants.PUBCHEM_URL);

		if (SampleConstants.BIOASSAY.equals(pubChemDS)) {
			sb.append(SampleConstants.BIOASSAY_ID);
		} else if (SampleConstants.COMPOUND.equals(pubChemDS)) {
			sb.append(SampleConstants.COMPOUND_ID);
		} else if (SampleConstants.SUBSTANCE.equals(pubChemDS)) {
			sb.append(SampleConstants.SUBSTANCE_ID);
		}

		sb.append('=').append(pubChemId);

		return sb.toString();
	}

}

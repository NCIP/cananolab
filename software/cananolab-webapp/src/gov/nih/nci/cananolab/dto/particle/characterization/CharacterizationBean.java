package gov.nih.nci.cananolab.dto.particle.characterization;

import gov.nih.nci.cananolab.domain.characterization.OtherCharacterization;
import gov.nih.nci.cananolab.domain.characterization.invitro.Cytotoxicity;
import gov.nih.nci.cananolab.domain.characterization.invitro.EnzymeInduction;
import gov.nih.nci.cananolab.domain.characterization.invitro.Transfection;
import gov.nih.nci.cananolab.domain.characterization.physical.PhysicalState;
import gov.nih.nci.cananolab.domain.characterization.physical.Shape;
import gov.nih.nci.cananolab.domain.characterization.physical.Solubility;
import gov.nih.nci.cananolab.domain.characterization.physical.Surface;
import gov.nih.nci.cananolab.domain.common.ExperimentConfig;
import gov.nih.nci.cananolab.domain.common.Finding;
import gov.nih.nci.cananolab.domain.common.Instrument;
import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.dto.common.ExperimentConfigBean;
import gov.nih.nci.cananolab.dto.common.FindingBean;
import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.dto.common.ProtocolBean;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.cananolab.util.Comparators;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.DateUtils;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * This class represents shared characterization properties to be shown in
 * characterization view pages.
 *
 * @author pansu
 *
 */
public class CharacterizationBean {
	private PointOfContactBean pocBean = new PointOfContactBean();

	private String conclusion;

	private String description;

	private String assayType;

	private ExperimentConfigBean theExperimentConfig = new ExperimentConfigBean();

	private FindingBean theFinding = new FindingBean();

	private Instrument theInstrument = new Instrument();

	private List<ExperimentConfigBean> experimentConfigs = new ArrayList<ExperimentConfigBean>();

	private List<FindingBean> findings = new ArrayList<FindingBean>();

	private ProtocolBean protocolBean = new ProtocolBean();

	private Characterization domainChar;

	private String className;

	private String dateString;

	private String characterizationType;

	private String characterizationName;

	private Cytotoxicity cytotoxicity = new Cytotoxicity();

	private PhysicalState physicalState = new PhysicalState();

	private Shape shape = new Shape();

	private Solubility solubility = new Solubility();

	private Surface surface = new Surface();

	private EnzymeInduction enzymeInduction = new EnzymeInduction();

	private boolean withProperties = false;

	private Transfection transfection = new Transfection();

	private String isSoluble; // Data holder for Boolean field in Solubility.

	public CharacterizationBean() {
		// initialize finding matrix
		// theFinding.setNumberOfColumns(1);
		// theFinding.setNumberOfRows(1);
		// theFinding.updateMatrix(theFinding.getNumberOfColumns(), theFinding
		// .getNumberOfRows());
	}

	public CharacterizationBean(Characterization chara) {
		domainChar = chara;
		className = ClassUtils.getShortClassName(chara.getClass().getName());
		this.description = chara.getDesignMethodsDescription();
		this.assayType = chara.getAssayType();
		this.conclusion = chara.getAnalysisConclusion();
		if (chara != null) {
			PointOfContact poc = chara.getPointOfContact();
			if (poc != null)
				pocBean = new PointOfContactBean(poc);
		}

		this.dateString = DateUtils.convertDateToString(chara.getDate(),
				Constants.DATE_FORMAT);

		if (chara.getFindingCollection() != null) {
			for (Finding finding : chara.getFindingCollection()) {
				findings.add(new FindingBean(finding));
			}
		}
		Collections.sort(findings, new Comparators.FindingBeanDateComparator());
		if (chara.getProtocol() != null) {
			protocolBean = new ProtocolBean(chara.getProtocol());
		}

		if (chara.getExperimentConfigCollection() != null) {
			for (ExperimentConfig config : chara
					.getExperimentConfigCollection()) {
				experimentConfigs.add(new ExperimentConfigBean(config));
			}
		}
		Collections.sort(experimentConfigs,
				new Comparators.ExperimentConfigBeanDateComparator());
		if (chara instanceof Shape) {
			shape = (Shape) chara;
			withProperties = true;
		} else if (chara instanceof PhysicalState) {
			physicalState = (PhysicalState) chara;
			withProperties = true;
		} else if (chara instanceof Solubility) {
			solubility = (Solubility) chara;
			withProperties = true;
		} else if (chara instanceof Surface) {
			surface = (Surface) chara;
			withProperties = true;
		} else if (chara instanceof Cytotoxicity) {
			cytotoxicity = (Cytotoxicity) chara;
			withProperties = true;
		} else if (chara instanceof EnzymeInduction) {
			enzymeInduction = (EnzymeInduction) chara;
			withProperties = true;
		} else if (chara instanceof Transfection) {
			transfection = (Transfection) chara;
			withProperties = true;
		} else {
			withProperties = false;
		}
		updateType();
		updateName();
	}

	public Characterization getDomainCopy(String createdBy, boolean copyData) {
		Characterization copy = (Characterization) ClassUtils
				.deepCopy(domainChar);
		resetDomainCopy(createdBy, copy, copyData);
		return copy;
	}

	public void resetDomainCopy(String createdBy, Characterization copy,
			boolean copyData) {
		// clear Ids, reset createdBy add prefix to
		copy.setId(null);
		copy.setCreatedBy(createdBy + ":"
				+ Constants.AUTO_COPY_ANNOTATION_PREFIX);

		// copy experiment config
		Collection<ExperimentConfig> oldConfigs = copy
				.getExperimentConfigCollection();
		if (oldConfigs == null || oldConfigs.isEmpty()) {
			copy.setExperimentConfigCollection(null);
		} else {
			copy.setExperimentConfigCollection(new HashSet<ExperimentConfig>(
					oldConfigs));
			for (ExperimentConfig config : copy.getExperimentConfigCollection()) {
				ExperimentConfigBean configBean = new ExperimentConfigBean(
						config);
				configBean.resetDomainCopy(createdBy, config);
			}

		}

		// copy finding
		Collection<Finding> oldFindings = copy.getFindingCollection();
		if (oldFindings == null || oldFindings.isEmpty()) {
			copy.setFindingCollection(null);
		} else {
			copy.setFindingCollection(new HashSet<Finding>(oldFindings));
			for (Finding finding : copy.getFindingCollection()) {
				FindingBean findingBean = new FindingBean(finding);
				findingBean.resetDomainCopy(createdBy, finding, copyData);
			}
		}
	}

	public void setupDomain(String createdBy) throws Exception {
		className = ClassUtils
				.getShortClassNameFromDisplayName(characterizationName);
		Class clazz = ClassUtils.getFullClass(className);
		if (clazz == null) {
			clazz = OtherCharacterization.class;
		}
		if (domainChar == null) {
			domainChar = (Characterization) clazz.newInstance();
		}
		if (domainChar instanceof OtherCharacterization) {
			((OtherCharacterization) domainChar).setName(characterizationName);
			((OtherCharacterization) domainChar)
					.setAssayCategory(characterizationType);
		} else if (domainChar instanceof Shape) {
			domainChar = shape;
		} else if (domainChar instanceof Solubility) {
			domainChar = solubility;
		} else if (domainChar instanceof PhysicalState) {
			domainChar = physicalState;
		} else if (domainChar instanceof Surface) {
			domainChar = surface;
		} else if (domainChar instanceof Cytotoxicity) {
			domainChar = cytotoxicity;
		} else if (domainChar instanceof EnzymeInduction) {
			domainChar = enzymeInduction;
		} else if (domainChar instanceof Transfection) {
			domainChar = transfection;
		}
		// updated created_date and created_by if id is null
		if (domainChar.getId() == null) {
			domainChar.setCreatedBy(createdBy);
			domainChar.setCreatedDate(Calendar.getInstance().getTime());
		}
		// updated created_by if created_by contains copy, but keep the original
		// created_date
		if (domainChar.getId() != null
				|| !StringUtils.isEmpty(domainChar.getCreatedBy())
				&& domainChar.getCreatedBy().contains(
						Constants.AUTO_COPY_ANNOTATION_PREFIX)) {
			domainChar.setCreatedBy(createdBy);
		}
		domainChar.setDesignMethodsDescription(description);
		domainChar.setAssayType(assayType);
		domainChar.setAnalysisConclusion(conclusion);
		if (pocBean != null && pocBean.getDomain().getId() != null
				&& pocBean.getDomain().getId() != 0) {
			domainChar.setPointOfContact(pocBean.getDomain());
		} else {
			domainChar.setPointOfContact(null);
		}
		domainChar.setDate(DateUtils.convertToDate(dateString,
				Constants.DATE_FORMAT));

		if (domainChar.getExperimentConfigCollection() != null) {
			domainChar.getExperimentConfigCollection().clear();
		} else {
			domainChar
					.setExperimentConfigCollection(new HashSet<ExperimentConfig>());
		}
		for (ExperimentConfigBean config : experimentConfigs) {
			domainChar.getExperimentConfigCollection().add(config.getDomain());
		}

		if (protocolBean != null && protocolBean.getDomain().getId() != null
				&& protocolBean.getDomain().getId().longValue() != 0) {
			domainChar.setProtocol(protocolBean.getDomain());
		} else {
			domainChar.setProtocol(null);
		}
		if (domainChar.getFindingCollection() != null) {
			domainChar.getFindingCollection().clear();
		} else {
			domainChar.setFindingCollection(new HashSet<Finding>());
		}
		for (FindingBean findingBean : findings) {
			domainChar.getFindingCollection().add(findingBean.getDomain());
		}
	}

	public String getDescription() {
		return StringUtils.escapeXmlButPreserveLineBreaks(description);
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ProtocolBean getProtocolBean() {
		return protocolBean;
	}

	public Characterization getDomainChar() {
		return domainChar;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) throws Exception {
		this.className = className;
	}

	public List<ExperimentConfigBean> getExperimentConfigs() {
		return experimentConfigs;
	}

	public ExperimentConfigBean getTheExperimentConfig() {
		return theExperimentConfig;
	}

	public void setTheExperimentConfig(ExperimentConfigBean theExperimentConfig) {
		this.theExperimentConfig = theExperimentConfig;
	}

	public void addExperimentConfig(ExperimentConfigBean experimentConfigBean) {
		// if an old one exists, remove it first
		if (experimentConfigs.contains(experimentConfigBean)) {
			removeExperimentConfig(experimentConfigBean);
		}
		experimentConfigs.add(experimentConfigBean);
	}

	public void removeExperimentConfig(ExperimentConfigBean experimentConfigBean) {
		experimentConfigs.remove(experimentConfigBean);
	}

	public Instrument getTheInstrument() {
		return theInstrument;
	}

	public void setTheInstrument(Instrument theInstrument) {
		this.theInstrument = theInstrument;
	}

	public String getDateString() {
		return dateString;
	}

	public void setDateString(String dateString) {
		this.dateString = dateString;
	}

	public PointOfContactBean getPocBean() {
		return pocBean;
	}

	public void setPocBean(PointOfContactBean pocBean) {
		this.pocBean = pocBean;
	}

	/**
	 * @return the theFinding
	 */
	public FindingBean getTheFinding() {
		return theFinding;
	}

	/**
	 * @param theFinding
	 *            the theFinding to set
	 */
	public void setTheFinding(FindingBean theFinding) {
		this.theFinding = theFinding;
	}

	public void addFinding(FindingBean dataSetBean) {
		// if an old one exists, remove it first
		int index = findings.indexOf(dataSetBean);
		if (index != -1) {
			findings.remove(dataSetBean);
			// retain the original order
			findings.add(index, dataSetBean);
		} else {
			findings.add(dataSetBean);
		}
	}

	public void removeFinding(FindingBean dataSetBean) {
		findings.remove(dataSetBean);
	}

	/**
	 * @return the findings
	 */
	public List<FindingBean> getFindings() {
		return findings;
	}

	public String getAssayCategory() {
		return characterizationType;
	}

	public void setAssayCategory(String assayCategory) {
		this.characterizationType = assayCategory;
	}

	public Cytotoxicity getCytotoxicity() {
		return cytotoxicity;
	}

	public void setCytotoxicity(Cytotoxicity cytotoxicity) {
		this.cytotoxicity = cytotoxicity;
	}

	public PhysicalState getPhysicalState() {
		return physicalState;
	}

	public void setPhysicalState(PhysicalState physicalState) {
		this.physicalState = physicalState;
	}

	public Shape getShape() {
		return shape;
	}

	public void setShape(Shape shape) {
		this.shape = shape;
	}

	public Solubility getSolubility() {
		return solubility;
	}

	public void setSolubility(Solubility solubility) {
		this.solubility = solubility;
	}

	public String getCharacterizationType() {
		return characterizationType;
	}

	public void setCharacterizationType(String characterizationType) {
		this.characterizationType = characterizationType;
	}

	public String getCharacterizationName() {
		return characterizationName;
	}

	public void setCharacterizationName(String characterizationName) {
		this.characterizationName = characterizationName;
	}

	public String getConclusion() {
		return conclusion;
	}

	public void setConclusion(String conclusion) {
		this.conclusion = conclusion;
	}

	public String getAssayType() {
		return assayType;
	}

	public void setAssayType(String assayType) {
		this.assayType = assayType;
	}

	public EnzymeInduction getEnzymeInduction() {
		return enzymeInduction;
	}

	public void setEnzymeInduction(EnzymeInduction enzymeInduction) {
		this.enzymeInduction = enzymeInduction;
	}

	public Surface getSurface() {
		return surface;
	}

	public void setSurface(Surface surface) {
		this.surface = surface;
	}

	public boolean isWithProperties() {
		return withProperties;
	}

	public Transfection getTransfection() {
		return transfection;
	}

	public void setTransfection(Transfection transfection) {
		this.transfection = transfection;
	}

	public void updateType() {
		if (domainChar instanceof OtherCharacterization) {
			characterizationType = ((OtherCharacterization) domainChar)
					.getAssayCategory();
		} else {
			String superClassShortName = ClassUtils
					.getShortClassName(domainChar.getClass().getSuperclass()
							.getName());
			characterizationType = ClassUtils
					.getDisplayName(superClassShortName);
		}
	}

	public void updateName() {
		if (domainChar instanceof OtherCharacterization) {
			characterizationName = ((OtherCharacterization) domainChar)
					.getName();
		} else {
			characterizationName = ClassUtils.getDisplayName(className);
		}
	}

	public String getIsSoluble() {
		return isSoluble;
	}

	public void setIsSoluble(String isSoluble) {
		this.isSoluble = isSoluble;
	}
}

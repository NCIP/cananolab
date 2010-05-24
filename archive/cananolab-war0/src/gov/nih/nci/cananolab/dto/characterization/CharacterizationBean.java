package gov.nih.nci.cananolab.dto.characterization;

import gov.nih.nci.cananolab.domain.characterization.Characterization;
import gov.nih.nci.cananolab.domain.characterization.OtherCharacterization;
import gov.nih.nci.cananolab.domain.characterization.invitro.Cytotoxicity;
import gov.nih.nci.cananolab.domain.characterization.invitro.EnzymeInduction;
import gov.nih.nci.cananolab.domain.characterization.invitro.InvitroCharacterization;
import gov.nih.nci.cananolab.domain.characterization.invitro.Transfection;
import gov.nih.nci.cananolab.domain.characterization.physical.PhysicalState;
import gov.nih.nci.cananolab.domain.characterization.physical.Purity;
import gov.nih.nci.cananolab.domain.characterization.physical.Shape;
import gov.nih.nci.cananolab.domain.characterization.physical.Solubility;
import gov.nih.nci.cananolab.domain.characterization.physical.Surface;
import gov.nih.nci.cananolab.domain.common.ExperimentConfig;
import gov.nih.nci.cananolab.domain.common.Instrument;
import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.dto.common.DataConditionMatrixBean;
import gov.nih.nci.cananolab.dto.common.ExperimentConfigBean;
import gov.nih.nci.cananolab.dto.common.FileBean;
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
import java.util.Set;

/**
 * This class represents shared characterization properties to be shown in
 * characterization view pages.
 * 
 * @author pansu
 * 
 */
public class CharacterizationBean {
	private String characterizationType;

	private String characterizationName;

	private String className;

	private String invitroAssayType;

	private String startDateString;

	private String isSoluble; // Data holder for Boolean field in Solubility.

	private boolean withProperties = false;

	private PointOfContactBean pocBean = new PointOfContactBean();

	private ExperimentConfigBean theExperimentConfig = new ExperimentConfigBean();

	private DataConditionMatrixBean theDataMatrix = new DataConditionMatrixBean();

	private Instrument theInstrument = new Instrument();

	private List<ExperimentConfigBean> experimentConfigs = new ArrayList<ExperimentConfigBean>();

	private List<FileBean> files = new ArrayList<FileBean>();

	private ProtocolBean protocolBean = new ProtocolBean();

	private Characterization domain = new Characterization();

	private Cytotoxicity cytotoxicity = new Cytotoxicity();

	private PhysicalState physicalState = new PhysicalState();

	private Shape shape = new Shape();

	private Solubility solubility = new Solubility();

	private Surface surface = new Surface();

	private Purity purity = new Purity();

	private EnzymeInduction enzymeInduction = new EnzymeInduction();

	private Transfection transfection = new Transfection();

	public CharacterizationBean() {
		// initialize finding matrix
		// theFinding.setNumberOfColumns(1);
		// theFinding.setNumberOfRows(1);
		// theFinding.updateMatrix(theFinding.getNumberOfColumns(), theFinding
		// .getNumberOfRows());
	}

	public CharacterizationBean(Characterization chara) {
		domain = chara;
		className = ClassUtils.getShortClassName(chara.getClass().getName());
		if (chara instanceof InvitroCharacterization) {
			invitroAssayType = ((InvitroCharacterization) chara).getAssayType();
		}
		if (chara != null) {
			PointOfContact poc = chara.getPointOfContact();
			if (poc != null)
				pocBean = new PointOfContactBean(poc);
		}

		this.startDateString = DateUtils.convertDateToString(chara
				.getStartDate(), Constants.DATE_FORMAT);

		// if (chara.getFindingCollection() != null) {
		// for (Finding finding : chara.getFindingCollection()) {
		// findings.add(new CharacterizationResultBean(finding));
		// }
		// }
		// Collections.sort(findings, new
		// Comparators.FindingBeanDateComparator());
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
		} else if (chara instanceof Purity) {
			purity = (Purity) chara;
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

	public Characterization getDomainCopy(boolean copyData) {
		Characterization copy = (Characterization) ClassUtils.deepCopy(domain);
		// clear Ids, reset createdBy and createdDate, add prefix to
		copy.setId(null);
		copy.setCreatedBy(Constants.AUTO_COPY_ANNOTATION_PREFIX);
		Collection<ExperimentConfig> oldConfigs = copy
				.getExperimentConfigCollection();
		if (oldConfigs == null || oldConfigs.isEmpty()) {
			copy.setExperimentConfigCollection(null);
		} else {
			/**
			 * Create new set for configs, otherwise will lost old configs in
			 * old bean.
			 */
			Set<ExperimentConfig> newConfigs = new HashSet<ExperimentConfig>(
					oldConfigs);
			for (ExperimentConfig newConfig : newConfigs) {
				newConfig.setId(null);
				newConfig.setCreatedBy(Constants.AUTO_COPY_ANNOTATION_PREFIX);
				// don't need to set instrument and technique ID's to null
				// because
				// they are reused.
			}
			copy.setExperimentConfigCollection(newConfigs);
		}
		// Collection<Finding> oldFindings = copy.getFindingCollection();
		// if (oldFindings == null || oldFindings.isEmpty()) {
		// copy.setFindingCollection(null);
		// } else {
		// /**
		// * Create new set for finding, otherwise will lost old finding in
		// * old bean.
		// */
		// Set<Finding> newFindings = new HashSet<Finding>(oldFindings);
		// for (Finding finding : newFindings) {
		// finding.setId(null);
		// finding.setCreatedBy(Constants.AUTO_COPY_ANNOTATION_PREFIX);
		// if (copyData) {
		// Collection<Datum> oldDatums = finding.getDatumCollection();
		// if (oldDatums == null || oldDatums.isEmpty()) {
		// finding.setDatumCollection(null);
		// } else {
		// Set<Datum> newDatums = new HashSet<Datum>(oldDatums);
		// for (Datum datum : newDatums) {
		// datum.setId(null);
		// // keep the bogus place holder if empty datum
		// if (StringUtils.isEmpty(datum.getCreatedBy())
		// || !datum
		// .getCreatedBy()
		// .equals(
		// Constants.PLACEHOLDER_DATUM_CONDITION_CREATED_BY)
		// && datum.getValue() != -1) {
		// datum
		// .setCreatedBy(Constants.AUTO_COPY_ANNOTATION_PREFIX);
		// }
		// // conditions
		// Collection<Condition> oldConditions = datum
		// .getConditionCollection();
		// if (oldConditions == null
		// || oldConditions.isEmpty()) {
		// datum.setConditionCollection(null);
		// } else {
		// Set<Condition> newConditions = new HashSet<Condition>(
		// oldConditions);
		// for (Condition condition : newConditions) {
		// condition.setId(null);
		// // keep the bogus place holder if empty
		// // condition
		// if (StringUtils.isEmpty(condition
		// .getCreatedBy())
		// || !Constants.PLACEHOLDER_DATUM_CONDITION_CREATED_BY
		// .equals(condition
		// .getCreatedBy())
		// && !condition
		// .getValue()
		// .equals(
		// Constants.PLACEHOLDER_DATUM_CONDITION_CREATED_BY)) {
		// condition
		// .setCreatedBy(Constants.AUTO_COPY_ANNOTATION_PREFIX);
		// }
		// }
		// }
		// }
		// finding.setDatumCollection(newDatums);
		// }
		// } else {
		// finding.setDatumCollection(null);
		// }
		// Collection<File> oldFiles = finding.getFileCollection();
		// if (oldFiles == null || oldFiles.isEmpty()) {
		// finding.setFileCollection(null);
		// } else {
		// Set<File> newFiles = new HashSet<File>(oldFiles);
		// for (File file : newFiles) {
		// file.setId(null);
		// file
		// .setCreatedBy(Constants.AUTO_COPY_ANNOTATION_PREFIX);
		// Collection<Keyword> keywords = file
		// .getKeywordCollection();
		// file.setKeywordCollection(new HashSet<Keyword>());
		// file.getKeywordCollection().addAll(keywords);
		// for (Keyword keyword : file.getKeywordCollection()) {
		// keyword.setId(null);
		// }
		// }
		// finding.setFileCollection(newFiles);
		// }
		// }
		// copy.setFindingCollection(newFindings);
		// }
		return copy;
	}

	public void setupDomain(String createdBy) throws Exception {
		className = ClassUtils
				.getShortClassNameFromDisplayName(characterizationName);
		Class clazz = ClassUtils.getFullClass(className);		
		if (clazz == null) {
			clazz = OtherCharacterization.class;
		}
		//set common attributes of a characterization
		//if setting up a brand new domain
		if (domain instanceof Characterization) {
			domain = (Characterization) clazz.newInstance();
		}
		if (domain instanceof OtherCharacterization) {
			((OtherCharacterization) domain).setName(characterizationName);
			((OtherCharacterization) domain)
					.setCharacterizationCategory(characterizationType);
		} else if (domain instanceof Shape) {
			domain = shape;
		} else if (domain instanceof Solubility) {
			domain = solubility;
		} else if (domain instanceof PhysicalState) {
			domain = physicalState;
		} else if (domain instanceof Surface) {
			domain = surface;
		} else if (domain instanceof Purity) {
			domain = purity;
		} else if (domain instanceof Cytotoxicity) {
			domain = cytotoxicity;
		} else if (domain instanceof EnzymeInduction) {
			domain = enzymeInduction;
		} else if (domain instanceof Transfection) {
			domain = transfection;
		}
		if (domain.getId() == null
				|| !StringUtils.isEmpty(domain.getCreatedBy())
				&& domain.getCreatedBy().equals(
						Constants.AUTO_COPY_ANNOTATION_PREFIX)) {
			domain.setCreatedBy(createdBy);
			domain.setCreatedDate(Calendar.getInstance().getTime());
		}
//		domain.setDesignMethodsDescription(description);
//		domain.setAssayType(invitroAssayType);
//		domain.setAnalysisConclusion(conclusion);
//		if (pocBean != null && pocBean.getDomain().getId() != null
//				&& pocBean.getDomain().getId() != 0) {
//			domain.setPointOfContact(pocBean.getDomain());
//		} else {
//			domain.setPointOfContact(null);
//		}
//		domain.setDate(DateUtils.convertToDate(startDateString,
//				Constants.DATE_FORMAT));
//
//		if (domain.getExperimentConfigCollection() != null) {
//			domain.getExperimentConfigCollection().clear();
//		} else {
//			domain
//					.setExperimentConfigCollection(new HashSet<ExperimentConfig>());
//		}
//		for (ExperimentConfigBean config : experimentConfigs) {
//			domain.getExperimentConfigCollection().add(config.getDomain());
//		}
//
//		if (protocolBean != null && protocolBean.getDomain().getId() != null
//				&& protocolBean.getDomain().getId().longValue() != 0) {
//			domain.setProtocol(protocolBean.getDomain());
//		} else {
//			domain.setProtocol(null);
//		}
//		if (domain.getFindingCollection() != null) {
//			domain.getFindingCollection().clear();
//		} else {
//			domain.setFindingCollection(new HashSet<Finding>());
//		}
//		for (CharacterizationResultBean characterizationResultBean : findings) {
//			domain.getFindingCollection().add(
//					characterizationResultBean.getDomain());
//		}
	}

	public ProtocolBean getProtocolBean() {
		return protocolBean;
	}

	public Characterization getDomainChar() {
		return domain;
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
		return startDateString;
	}

	public void setDateString(String dateString) {
		this.startDateString = dateString;
	}

	public PointOfContactBean getPocBean() {
		return pocBean;
	}

	public void setPocBean(PointOfContactBean pocBean) {
		this.pocBean = pocBean;
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

	public String getAssayType() {
		return invitroAssayType;
	}

	public void setAssayType(String assayType) {
		this.invitroAssayType = assayType;
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
//		if (domain instanceof OtherCharacterization) {
//			characterizationType = ((OtherCharacterization) domain)
//					.getAssayCategory();
//		} else {
//			String superClassShortName = ClassUtils.getShortClassName(domain
//					.getClass().getSuperclass().getName());
//			characterizationType = ClassUtils
//					.getDisplayName(superClassShortName);
//		}
	}

	public void updateName() {
		if (domain instanceof OtherCharacterization) {
			characterizationName = ((OtherCharacterization) domain).getName();
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

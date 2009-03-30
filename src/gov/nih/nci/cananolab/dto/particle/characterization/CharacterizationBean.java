package gov.nih.nci.cananolab.dto.particle.characterization;

import gov.nih.nci.cananolab.domain.characterization.invitro.Cytotoxicity;
import gov.nih.nci.cananolab.domain.characterization.invitro.EnzymeInduction;
import gov.nih.nci.cananolab.domain.characterization.physical.PhysicalState;
import gov.nih.nci.cananolab.domain.characterization.physical.Shape;
import gov.nih.nci.cananolab.domain.characterization.physical.Solubility;
import gov.nih.nci.cananolab.domain.common.DataSet;
import gov.nih.nci.cananolab.domain.common.Datum;
import gov.nih.nci.cananolab.domain.common.ExperimentConfig;
import gov.nih.nci.cananolab.domain.common.Instrument;
import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.domain.common.ProtocolFile;
import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.dto.common.DataRowBean;
import gov.nih.nci.cananolab.dto.common.DataSetBean;
import gov.nih.nci.cananolab.dto.common.ExperimentConfigBean;
import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.dto.common.ProtocolFileBean;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.cananolab.util.Comparators;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.DateUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

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

	private DataSetBean theDataSet = new DataSetBean();

	private Instrument theInstrument = new Instrument();

	private List<ExperimentConfigBean> experimentConfigs = new ArrayList<ExperimentConfigBean>();

	private List<DataSetBean> dataSets = new ArrayList<DataSetBean>();

	private ProtocolFileBean protocolFileBean = new ProtocolFileBean();

	private Characterization domainChar;

	private String className;

	private String dateString;

	private String characterizationType;

	private String characterizationName;

	private Cytotoxicity cytotoxicity = new Cytotoxicity();

	private PhysicalState physicalState = new PhysicalState();

	private Shape shape = new Shape();

	private Solubility solubility = new Solubility();

	private EnzymeInduction enzymeInduction = new EnzymeInduction();

	public CharacterizationBean() {
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

		if (chara.getDatumCollection() != null) {
			convertToDataSets(new ArrayList<Datum>(chara.getDatumCollection()));
		}
		if (chara.getProtocolFile() != null) {
			protocolFileBean = new ProtocolFileBean(chara.getProtocolFile());
		}

		if (chara.getExperimentConfigCollection() != null) {
			for (ExperimentConfig config : chara
					.getExperimentConfigCollection()) {
				experimentConfigs.add(new ExperimentConfigBean(config));
			}
		}
		if (chara instanceof Shape) {
			shape = (Shape) chara;
		} else if (chara instanceof PhysicalState) {
			physicalState = (PhysicalState) chara;
		} else if (chara instanceof Solubility) {
			solubility = (Solubility) chara;
		} else if (chara instanceof Cytotoxicity) {
			cytotoxicity = (Cytotoxicity) chara;
		} else if (chara instanceof EnzymeInduction) {
			enzymeInduction = (EnzymeInduction) chara;
		}
	}

	private void convertToDataSets(List<Datum> data) {
		Collections.sort(data, new Comparators.DatumDateComparator());
		// get all DataSets in order of creation date
		List<DataSet> dataSetList = new ArrayList<DataSet>();
		for (Datum datum : data) {
			if (!dataSetList.contains(datum.getDataSet())) {
				dataSetList.add(datum.getDataSet());
			}
		}
		Map<DataSet, List<Datum>> dataMap = new HashMap<DataSet, List<Datum>>();
		List<Datum> dataPerSet = null;
		for (Datum datum : data) {
			if (dataMap.containsKey(datum.getDataSet())) {
				dataPerSet = dataMap.get(datum.getDataSet());
			} else {
				dataPerSet = new ArrayList<Datum>();
				dataMap.put(datum.getDataSet(), dataPerSet);
			}
			dataPerSet.add(datum);
		}

		for (DataSet set : dataSetList) {
			if (set != null) {
				DataSetBean dataSetBean = new DataSetBean(dataMap.get(set));
				dataSets.add(dataSetBean);
			}
		}
	}

	public Characterization getDomainCopy(boolean copyDerivedDatum) {
		Characterization copy = (Characterization) ClassUtils
				.deepCopy(domainChar);
		// clear Ids, reset createdBy and createdDate, add prefix to
		copy.setId(null);
		copy.setCreatedBy(Constants.AUTO_COPY_ANNOTATION_PREFIX);
		copy.setCreatedDate(new Date());
		if (copy.getExperimentConfigCollection().isEmpty()) {
			copy.setExperimentConfigCollection(null);
		} else {
			Collection<ExperimentConfig> configs = copy
					.getExperimentConfigCollection();
			copy.setExperimentConfigCollection(new HashSet<ExperimentConfig>());
			copy.getExperimentConfigCollection().addAll(configs);
			for (ExperimentConfig config : copy.getExperimentConfigCollection()) {
				config.setId(null);
				config.setCreatedBy(Constants.AUTO_COPY_ANNOTATION_PREFIX);
				config.setCreatedDate(new Date());
			}
		}

		if (copy.getDatumCollection().isEmpty()) {
			copy.setDatumCollection(null);
		} else {
			Collection<Datum> data = copy.getDatumCollection();
			copy.setDatumCollection(new HashSet<Datum>());
			copy.getDatumCollection().addAll(data);
			for (Datum datum : copy.getDatumCollection()) {
				datum.setId(null);
				datum.setCreatedBy(Constants.AUTO_COPY_ANNOTATION_PREFIX);
				datum.setCreatedDate(new Date());
				// TODO::
				// if (bioassay.getFile() != null) {
				//
				// bioassay.getFile().setId(null);
				// bioassay.getFile().setCreatedBy(
				// Constants.AUTO_COPY_ANNOTATION_PREFIX);
				// bioassay.getFile().setCreatedDate(new Date());
				// }
				// if (bioassay.getDerivedDatumCollection().isEmpty()
				// || !copyDerivedDatum) {
				// bioassay.setDerivedDatumCollection(null);
				// } else {
				// Collection<DerivedDatum> data = bioassay
				// .getDerivedDatumCollection();
				// bioassay
				// .setDerivedDatumCollection(new HashSet<DerivedDatum>());
				// bioassay.getDerivedDatumCollection().addAll(data);
				// for (DerivedDatum datum : bioassay
				// .getDerivedDatumCollection()) {
				// datum.setId(null);
				// datum
				// .setCreatedBy(Constants.AUTO_COPY_ANNOTATION_PREFIX);
				// datum.setCreatedDate(new Date());
				// }
				// }
			}
		}
		return copy;
	}

	public void setupDomainChar(String createdBy, String internalUriPath)
			throws Exception {
		// take care of characterizations that don't have any special
		// properties shown in the form, e.g. Size
		if (domainChar == null) {
			Class clazz = ClassUtils.getFullClass(className);
			domainChar = (Characterization) clazz.newInstance();
		}

		if (domainChar instanceof Shape) {
			domainChar = shape;
		} else if (domainChar instanceof Solubility) {
			domainChar = solubility;
		} else if (domainChar instanceof PhysicalState) {
			domainChar = physicalState;
		} else if (domainChar instanceof Cytotoxicity) {
			domainChar = cytotoxicity;
		} else if (domainChar instanceof EnzymeInduction) {
			domainChar = enzymeInduction;
		}

		if (domainChar.getId() == null
				|| domainChar.getCreatedBy() != null
				&& domainChar.getCreatedBy().equals(
						Constants.AUTO_COPY_ANNOTATION_PREFIX)) {
			domainChar.setCreatedBy(createdBy);
			domainChar.setCreatedDate(new Date());
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

		if (protocolFileBean != null
				&& protocolFileBean.getDomainFile() != null
				&& protocolFileBean.getDomainFile().getId() != null
				&& protocolFileBean.getDomainFile().getId() != 0) {
			domainChar.setProtocolFile(((ProtocolFile) protocolFileBean
					.getDomainFile()));
		} else {
			domainChar.setProtocolFile(null);
		}
		if (domainChar.getDatumCollection() != null) {
			domainChar.getDatumCollection().clear();
		} else {
			domainChar.setDatumCollection(new HashSet<Datum>());
		}

		if (domainChar.getDatumCollection() != null) {
			domainChar.getDatumCollection().clear();
		} else {
			domainChar.setDatumCollection(new HashSet<Datum>());
		}
		for (DataSetBean dataSetBean : dataSets) {
			for (DataRowBean dataRowBean : dataSetBean.getDataRows()) {
				for (Datum datum : dataRowBean.getData()) {
					domainChar.getDatumCollection().add(datum);
				}
			}
		}
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ProtocolFileBean getProtocolFileBean() {
		return protocolFileBean;
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
	 * @return the theDataSet
	 */
	public DataSetBean getTheDataSet() {
		return theDataSet;
	}

	/**
	 * @param theDataSet
	 *            the theDataSet to set
	 */
	public void setTheDataSet(DataSetBean theDataSet) {
		this.theDataSet = theDataSet;
	}

	public void addDataSet(DataSetBean dataSetBean) {
		// if an old one exists, remove it first
		int index = dataSets.indexOf(dataSetBean);
		if (index != -1) {
			dataSets.remove(dataSetBean);
			// retain the original order
			dataSets.add(index, dataSetBean);
		} else {
			dataSets.add(dataSetBean);
		}
	}

	public void removeDataSet(DataSetBean dataSetBean) {
		dataSets.remove(dataSetBean);
	}

	/**
	 * @return the dataSets
	 */
	public List<DataSetBean> getDataSets() {
		return dataSets;
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
}

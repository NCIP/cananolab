package gov.nih.nci.cananolab.dto.particle.characterization;

import gov.nih.nci.cananolab.domain.common.DataSet;
import gov.nih.nci.cananolab.domain.common.Instrument;
import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.domain.common.ProtocolFile;
import gov.nih.nci.cananolab.domain.particle.characterization.Characterization;
import gov.nih.nci.cananolab.domain.particle.characterization.Datum;
import gov.nih.nci.cananolab.domain.particle.characterization.ExperimentConfig;
import gov.nih.nci.cananolab.dto.common.DataRowBean;
import gov.nih.nci.cananolab.dto.common.DataSetBean;
import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.dto.common.ProtocolFileBean;
import gov.nih.nci.cananolab.util.CaNanoLabComparators;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.cananolab.util.StringUtils;

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
	// private String characterizationSource;
	private PointOfContactBean pocBean=new PointOfContactBean();

	// used to distinguish different instances of characterizations, which are
	// shown as different links on the view pages.
	private String viewTitle;

	private String description;

	private ExperimentConfigBean theExperimentConfig = new ExperimentConfigBean();

	private DataSetBean theDataSet = new DataSetBean();

	private Instrument theInstrument = new Instrument();

	private List<ExperimentConfigBean> experimentConfigs = new ArrayList<ExperimentConfigBean>();

	private List<DataSetBean> dataSets = new ArrayList<DataSetBean>();

	private ProtocolFileBean protocolFileBean = new ProtocolFileBean();

	protected Characterization domainChar;

	private String className;

	protected String dateString;

	public CharacterizationBean() {
	}

	public CharacterizationBean(Characterization chara) {
		domainChar = chara;
		className = ClassUtils.getShortClassName(chara.getClass().getName());
		this.description = chara.getDescription();
		this.viewTitle = chara.getIdentificationName();
		if (chara != null) {
			PointOfContact poc = chara.getPointOfContact();
			if (poc != null) 
				pocBean = new PointOfContactBean(poc);
		}

		this.dateString = StringUtils.convertDateToString(chara.getDate(),
				CaNanoLabConstants.DATE_FORMAT);

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
	}

	private void convertToDataSets(List<Datum> data) {
		Collections.sort(data, new CaNanoLabComparators.DatumDateComparator());
		// get all DataSets in order of creation date
		List<DataSet> dataSetList = new ArrayList<DataSet>();
		for (Datum datum : data) {
			if (!dataSets.contains(datum.getDataSet())) {
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
			DataSetBean dataSetBean = new DataSetBean(dataMap.get(set));
			dataSets.add(dataSetBean);
		}
	}

	public Characterization getDomainCopy(boolean copyDerivedDatum) {
		Characterization copy = (Characterization) ClassUtils
				.deepCopy(domainChar);
		// clear Ids, reset createdBy and createdDate, add prefix to
		copy.setId(null);
		copy.setCreatedBy(CaNanoLabConstants.AUTO_COPY_ANNOTATION_PREFIX);
		copy.setCreatedDate(new Date());
		copy
				.setIdentificationName(CaNanoLabConstants.AUTO_COPY_ANNOTATION_PREFIX
						+ "_"
						+ StringUtils.convertDateToString(new Date(),
								"yyyyMMdd_HH-mm-ss-SSS"));

		if (copy.getExperimentConfigCollection().isEmpty()) {
			copy.setExperimentConfigCollection(null);
		} else {
			Collection<ExperimentConfig> configs = copy
					.getExperimentConfigCollection();
			copy.setExperimentConfigCollection(new HashSet<ExperimentConfig>());
			copy.getExperimentConfigCollection().addAll(configs);
			for (ExperimentConfig config : copy.getExperimentConfigCollection()) {
				config.setId(null);
				config
						.setCreatedBy(CaNanoLabConstants.AUTO_COPY_ANNOTATION_PREFIX);
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
				datum
						.setCreatedBy(CaNanoLabConstants.AUTO_COPY_ANNOTATION_PREFIX);
				datum.setCreatedDate(new Date());
				// TODO::
				// if (bioassay.getFile() != null) {
				//
				// bioassay.getFile().setId(null);
				// bioassay.getFile().setCreatedBy(
				// CaNanoLabConstants.AUTO_COPY_ANNOTATION_PREFIX);
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
				// .setCreatedBy(CaNanoLabConstants.AUTO_COPY_ANNOTATION_PREFIX);
				// datum.setCreatedDate(new Date());
				// }
				// }
			}
		}
		return copy;
	}

	public void setupDomainChar(Map<String, String> typeToClass,
			String createdBy, String internalUriPath) throws Exception {
		// take care of characterizations that don't have any special
		// properties shown in the form, e.g. Size
		if (domainChar.getId() == null
				|| domainChar.getCreatedBy() != null
				&& domainChar.getCreatedBy().equals(
						CaNanoLabConstants.AUTO_COPY_ANNOTATION_PREFIX)) {
			domainChar.setCreatedBy(createdBy);
			domainChar.setCreatedDate(new Date());
		}
		domainChar.setDescription(description);
		domainChar.setIdentificationName(viewTitle);
		if (pocBean != null && pocBean.getDomain().getId() != null
				&& pocBean.getDomain().getId() != 0) {
			domainChar.setPointOfContact(pocBean.getDomain());
		} else {
			domainChar.setPointOfContact(null);
		}
		domainChar.setDate(StringUtils.convertToDate(dateString,
				CaNanoLabConstants.DATE_FORMAT));

		if (domainChar.getExperimentConfigCollection() != null) {
			domainChar.getExperimentConfigCollection().clear();
		} else {
			domainChar
					.setExperimentConfigCollection(new HashSet<ExperimentConfig>());
		}
		for (ExperimentConfigBean config : experimentConfigs) {
			domainChar.getExperimentConfigCollection().add(config.getDomain());
			if (domainChar.getId() != null) {
				config.getDomain().setCharacterization(domainChar);
			}
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

		for (DataSetBean dataSetBean : dataSets) {
			dataSetBean.setupDomain(createdBy);
			for (DataRowBean dataRowBean : dataSetBean.getDataRows()) {
				for (Datum datum : dataRowBean.getData()) {
					domainChar.getDatumCollection().add(datum);
					if (domainChar.getId() != null) {
						datum.setCharacterization(domainChar);
					}
				}
			}
		}
	}

	public String getViewTitle() {
		// get only the first number of characters of the title
		if (this.viewTitle != null
				&& this.viewTitle.length() > CaNanoLabConstants.MAX_VIEW_TITLE_LENGTH) {
			return this.viewTitle.substring(0,
					CaNanoLabConstants.MAX_VIEW_TITLE_LENGTH);
		}
		return this.viewTitle;
	}

	public void setViewTitle(String viewTitle) {
		this.viewTitle = viewTitle;
	}

	// public void addDerivedBioAssayData() {
	// derivedBioAssayDataList.add(new DerivedBioAssayDataBean());
	// }
	//
	// public void removeDerivedBioAssayData(int ind) {
	// derivedBioAssayDataList.remove(ind);
	// }

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	// public List<DerivedBioAssayDataBean> getDerivedBioAssayDataList() {
	// return this.derivedBioAssayDataList;
	// }

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
		if (dataSets.contains(dataSetBean)) {
			removeDataSet(dataSetBean);
		}
		dataSets.add(dataSetBean);
	}

	public void removeDataSet(DataSetBean dataSetBean) {
		dataSets.remove(dataSetBean);
	}
}

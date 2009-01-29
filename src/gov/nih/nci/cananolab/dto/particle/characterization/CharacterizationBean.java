package gov.nih.nci.cananolab.dto.particle.characterization;

import gov.nih.nci.cananolab.domain.common.Instrument;
import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.domain.common.ProtocolFile;
import gov.nih.nci.cananolab.domain.particle.characterization.Characterization;
import gov.nih.nci.cananolab.domain.particle.characterization.Datum;
import gov.nih.nci.cananolab.domain.particle.characterization.ExperimentConfig;
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
	//private String characterizationSource;
	private PointOfContactBean pocBean;

	// used to distinguish different instances of characterizations, which are
	// shown as different links on the view pages.
	private String viewTitle;

	private String description;

	private ExperimentConfigBean theExperimentConfig = new ExperimentConfigBean();

	private Instrument theInstrument = new Instrument();

	private List<ExperimentConfigBean> experimentConfigs = new ArrayList<ExperimentConfigBean>();

	private List<DatumBean> datumCollection = new ArrayList<DatumBean>();

	private ProtocolFileBean protocolFileBean = new ProtocolFileBean();

	protected Characterization domainChar;
	
	private String className;

	protected String dateString;

	
	

	public CharacterizationBean() {
		pocBean = new PointOfContactBean();
	}

	public CharacterizationBean(Characterization chara) {
		domainChar = chara;
		className = ClassUtils.getShortClassName(chara.getClass().getName());
		this.description = chara.getDescription();
		this.viewTitle = chara.getIdentificationName();
		if (chara != null) {
			PointOfContact poc = chara.getPointOfContact();
			pocBean = new PointOfContactBean(poc);
		}
		if (pocBean != null) {
			domainChar.setPointOfContact(pocBean.getDomain());
		}
		this.dateString = StringUtils.convertDateToString(chara.getDate(),
				CaNanoLabConstants.DATE_FORMAT);

		if (chara.getDatumCollection() != null) {
			for (Datum datum : chara
					.getDatumCollection()) {
				datumCollection.add(new DatumBean(
						datum));
			}
			//TODO: may not need to sort, or sort by dataRow id
//			Collections
//					.sort(
//							datumCollection,
//							new CaNanoLabComparators.DatumBeanDateComparator());
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
			Collection<Datum> data = copy
					.getDatumCollection();
			copy
					.setDatumCollection(new HashSet<Datum>());
			copy.getDatumCollection().addAll(data);
			for (Datum datum : copy.getDatumCollection()) {
				datum.setId(null);
				datum
						.setCreatedBy(CaNanoLabConstants.AUTO_COPY_ANNOTATION_PREFIX);
				datum.setCreatedDate(new Date());
				//TODO:: 
//				if (bioassay.getFile() != null) {
//
//					bioassay.getFile().setId(null);
//					bioassay.getFile().setCreatedBy(
//							CaNanoLabConstants.AUTO_COPY_ANNOTATION_PREFIX);
//					bioassay.getFile().setCreatedDate(new Date());
//				}
//				if (bioassay.getDerivedDatumCollection().isEmpty()
//						|| !copyDerivedDatum) {
//					bioassay.setDerivedDatumCollection(null);
//				} else {
//					Collection<DerivedDatum> data = bioassay
//							.getDerivedDatumCollection();
//					bioassay
//							.setDerivedDatumCollection(new HashSet<DerivedDatum>());
//					bioassay.getDerivedDatumCollection().addAll(data);
//					for (DerivedDatum datum : bioassay
//							.getDerivedDatumCollection()) {
//						datum.setId(null);
//						datum
//								.setCreatedBy(CaNanoLabConstants.AUTO_COPY_ANNOTATION_PREFIX);
//						datum.setCreatedDate(new Date());
//					}
//				}
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
		if (pocBean != null) {
			domainChar.setPointOfContact(pocBean.getDomain());
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
			domainChar
					.setDatumCollection(new HashSet<Datum>());
		}
		// set createdBy and createdDate
		int i = 0;
		//TODO::
		for (DatumBean datumBean : datumCollection) {
//			if (bioAssayData.getDomainBioAssayData().getId() == null) {
//				bioAssayData.getDomainBioAssayData().setCreatedBy(createdBy);
//				bioAssayData.getDomainBioAssayData().setCreatedDate(new Date());
//			}
//			bioAssayData.setupDomainBioAssayData(typeToClass, createdBy,
//					internalUriPath, i);
//			domainChar.getDerivedBioAssayDataCollection().add(
//					bioAssayData.getDomainBioAssayData());
			i++;
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

//	public void addDerivedBioAssayData() {
//		derivedBioAssayDataList.add(new DerivedBioAssayDataBean());
//	}
//
//	public void removeDerivedBioAssayData(int ind) {
//		derivedBioAssayDataList.remove(ind);
//	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

//	public List<DerivedBioAssayDataBean> getDerivedBioAssayDataList() {
//		return this.derivedBioAssayDataList;
//	}

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
	 * @return the datumCollection
	 */
	public List<DatumBean> getDatumCollection() {
		return datumCollection;
	}

	/**
	 * @param datumCollection the datumCollection to set
	 */
	public void setDatumCollection(List<DatumBean> datumCollection) {
		this.datumCollection = datumCollection;
	}
}

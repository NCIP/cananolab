package gov.nih.nci.cananolab.dto.particle.characterization;

import gov.nih.nci.cananolab.domain.common.DerivedBioAssayData;
import gov.nih.nci.cananolab.domain.common.DerivedDatum;
import gov.nih.nci.cananolab.domain.common.Instrument;
import gov.nih.nci.cananolab.domain.common.InstrumentConfiguration;
import gov.nih.nci.cananolab.domain.particle.characterization.Characterization;
import gov.nih.nci.cananolab.dto.common.ProtocolFileBean;
import gov.nih.nci.cananolab.util.CaNanoLabComparators;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;

import java.util.ArrayList;
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
	private String characterizationSource;

	// used to distinguish different instances of characterizations, which are
	// shown as different links on the view pages.
	private String viewTitle;

	// used for link color on the side menu
	private String viewColor;

	private String description;

	private InstrumentConfiguration instrumentConfig = new InstrumentConfiguration();

	private List<DerivedBioAssayData> derivedBioAssayDataList = new ArrayList<DerivedBioAssayData>();

	private ProtocolFileBean protocolFileBean = new ProtocolFileBean();

	protected Characterization domainChar;

	private String className;

	public CharacterizationBean() {
		instrumentConfig.setInstrument(new Instrument());
	}

	public CharacterizationBean(Characterization achar) {
		this.description = achar.getDescription();
		this.viewTitle = achar.getIdentificationName();
		instrumentConfig = achar.getInstrumentConfiguration();
		for (DerivedBioAssayData bioassayData : achar
				.getDerivedBioAssayDataCollection()) {
			derivedBioAssayDataList.add(bioassayData);
		}
		protocolFileBean = new ProtocolFileBean(achar.getProtocolFile());
	}

	public void setDomainChar() {
		domainChar.setDescription(description);
		domainChar.setIdentificationName(viewTitle);
		if (instrumentConfig.getInstrument() != null
				&& instrumentConfig.getInstrument().getType() != null
				&& instrumentConfig.getInstrument().getType().length() > 0)
			domainChar.setInstrumentConfiguration(instrumentConfig);
		domainChar.setProtocolFile(protocolFileBean.getDomainProtocolFile());
		if (domainChar.getDerivedBioAssayDataCollection() != null) {
			domainChar.getDerivedBioAssayDataCollection().clear();
		} else {
			domainChar
					.setDerivedBioAssayDataCollection(new HashSet<DerivedBioAssayData>());
		}
		for (DerivedBioAssayData bioAssayData : derivedBioAssayDataList) {
			domainChar.getDerivedBioAssayDataCollection().add(bioAssayData);
		}
	}

	/**
	 * Copy constructor
	 * 
	 * @param charBean
	 */
	public CharacterizationBean(CharacterizationBean charBean) {
		this.viewTitle = charBean.getViewTitle();
		this.characterizationSource = charBean.getCharacterizationSource();
		this.derivedBioAssayDataList = charBean.getDerivedBioAssayDataList();
		this.description = charBean.getDescription();
		this.instrumentConfig = charBean.getInstrumentConfiguration();
		this.protocolFileBean = charBean.getProtocolFileBean();
	}

	// public CharacterizationBean(Characterization characterization) {
	// this.setViewTitle(characterization.getIdentificationName());
	// this.setCharacterizationSource(characterization.getSource());
	// this.setCreatedBy(characterization.getCreatedBy());
	// this.setCreatedDate(characterization.getCreatedDate());
	//		
	// this.setDescription(characterization.getDescription());
	// InstrumentConfiguration instrumentConfigObj = characterization
	// .getInstrumentConfiguration();
	// if (instrumentConfigObj != null) {
	// this.instrumentConfigBean = new InstrumentConfigBean(
	// instrumentConfigObj);
	// }
	// for (DerivedBioAssayData table : characterization
	// .getDerivedBioAssayDataCollection()) {
	// DerivedBioAssayDataBean ctBean = new DerivedBioAssayDataBean(table);
	// this.getDerivedBioAssayDataList().add(ctBean);
	// }
	// ProtocolFile protocolFile = characterization.getProtocolFile();
	// if (protocolFile != null) {
	// this.protocolFileBean = new ProtocolFileBean(protocolFile);
	// }
	// }

	public String getCharacterizationSource() {
		return this.characterizationSource;
	}

	public void setCharacterizationSource(String characterizationSource) {
		this.characterizationSource = characterizationSource;
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

	public void addDerivedBioAssayData() {
		derivedBioAssayDataList.add(new DerivedBioAssayData());
	}

	public void removeDerivedBioAssayData(int ind) {
		derivedBioAssayDataList.remove(ind);
	}

	public void addDerivedDatum(int ind) {
		DerivedBioAssayData bioAssayData = derivedBioAssayDataList.get(ind);
		bioAssayData.getDerivedDatumCollection().add(new DerivedDatum());
	}

	public void removeDerivedDatum(int ind, int dataInd) {
		DerivedBioAssayData bioAssayData = derivedBioAssayDataList.get(ind);
		List<DerivedDatum> sortedDatumList = new ArrayList<DerivedDatum>(
				bioAssayData.getDerivedDatumCollection());
		Collections.sort(sortedDatumList,
				new CaNanoLabComparators.DerivedDatumDateComparator());
		DerivedDatum datum = sortedDatumList.remove(dataInd);
		bioAssayData.getDerivedDatumCollection().remove(datum);
	}

	/**
	 * Update the domain characterization object from the dto bean properties
	 * 
	 * @return
	 */
	// public void updateDomainObj(Characterization doChar) {
	// doChar.setSource(getCharacterizationSource());
	// doChar.setIdentificationName(getViewTitle());
	// doChar.setDescription(getDescription());
	// doChar.setCreatedBy(getCreatedBy());
	// doChar.setCreatedDate(getCreatedDate());
	// updateDerivedBioAssayData(doChar);
	// }
	// update domain object's derivedBioAssayData collection
	// private void updateDerivedBioAssayData(Characterization doChar) {
	// // copy collection
	// List<DerivedBioAssayData> doDerivedDataList = new
	// ArrayList<DerivedBioAssayData>(
	// doChar.getDerivedBioAssayDataCollection());
	// // clear the existing collection
	// doChar.getDerivedBioAssayDataCollection().clear();
	// for (DerivedBioAssayDataBean derivedBioAssayDataBean :
	// getDerivedBioAssayDataList()) {
	// DerivedBioAssayData doDerivedBioAssayData = null;
	// // if no id, add new domain object
	// if (derivedBioAssayDataBean.getId() == null) {
	// doDerivedBioAssayData = new DerivedBioAssayData();
	// } else {
	// // find domain object with the same ID and add the updated
	// // domain object
	// for (DerivedBioAssayData doData : doDerivedDataList) {
	// if (doData.getId().equals(
	// new Long(derivedBioAssayDataBean.getId()))) {
	// doDerivedBioAssayData = doData;
	// break;
	// }
	// }
	// }
	// derivedBioAssayDataBean.updateDomainObj(doDerivedBioAssayData);
	// doChar.getDerivedBioAssayDataCollection()
	// .add(doDerivedBioAssayData);
	// }
	// }
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<DerivedBioAssayData> getDerivedBioAssayDataList() {
		return this.derivedBioAssayDataList;
	}

	public String getViewColor() {
		if (this.viewTitle != null && this.viewTitle.matches("^copy_\\d{15}?")) {
			this.viewColor = CaNanoLabConstants.AUTO_COPY_ANNNOTATION_VIEW_COLOR;
		}
		return this.viewColor;
	}

	/**
	 * Create a new instance of CharacterizationBean with the same metadata
	 * except DerivedBioAssayDataList and InstrumentConfig.
	 * 
	 * @return
	 */
	// public CharacterizationBean copy(boolean copyData) {
	// CharacterizationBean newCharBean = new CharacterizationBean(this);
	// // unset id
	// newCharBean.setId(null);
	// // set InstrumentConfig, DerivedBioAssayDataList
	// InstrumentConfigBean newInstrumentConfigBean = this.instrumentConfigBean
	// .copy();
	// newCharBean.setInstrumentConfigBean(newInstrumentConfigBean);
	//
	// List<DerivedBioAssayDataBean> newDerivedBioAssayDataList = new
	// ArrayList<DerivedBioAssayDataBean>();
	// for (DerivedBioAssayDataBean derivedBioAssayDataBean :
	// this.derivedBioAssayDataList) {
	// DerivedBioAssayDataBean newDerivedBioAssayDataBean =
	// derivedBioAssayDataBean
	// .copy(copyData);
	// newDerivedBioAssayDataList.add(newDerivedBioAssayDataBean);
	// }
	// newCharBean.setDerivedBioAssayDataList(newDerivedBioAssayDataList);
	// return newCharBean;
	// }
	public InstrumentConfiguration getInstrumentConfiguration() {
		return instrumentConfig;
	}

	public ProtocolFileBean getProtocolFileBean() {
		return protocolFileBean;
	}

	public void setViewColor(String viewColor) {
		this.viewColor = viewColor;
	}

	public Characterization getDomainChar() {
		return domainChar;
	}

	// public String getExportFileName() {
	// List<String> nameParts = new ArrayList<String>();
	// nameParts.add(particle.getSampleName());
	// nameParts.add(getDispatchActionName());
	// nameParts.add("detailView");
	// nameParts.add(StringUtils.convertDateToString(new Date(),
	// "yyyyMMdd_HH-mm-ss-SSS"));
	// exportFileName = StringUtils.join(nameParts, "_");
	// return exportFileName;
	// }

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
}

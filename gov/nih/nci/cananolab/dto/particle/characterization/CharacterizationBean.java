package gov.nih.nci.cananolab.dto.particle.characterization;

import gov.nih.nci.cananolab.domain.common.DerivedBioAssayData;
import gov.nih.nci.cananolab.domain.common.DerivedDatum;
import gov.nih.nci.cananolab.domain.common.Instrument;
import gov.nih.nci.cananolab.domain.common.InstrumentConfiguration;
import gov.nih.nci.cananolab.domain.particle.characterization.Characterization;
import gov.nih.nci.cananolab.dto.common.ProtocolFileBean;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;
import gov.nih.nci.cananolab.util.ClassUtils;

import java.util.ArrayList;
import java.util.Date;
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

	private List<DerivedBioAssayDataBean> derivedBioAssayDataList = new ArrayList<DerivedBioAssayDataBean>();

	private ProtocolFileBean protocolFileBean = new ProtocolFileBean();

	protected Characterization domainChar;

	private String className;

	private String createdBy;

	public CharacterizationBean() {
		instrumentConfig.setInstrument(new Instrument());

		// DerivedBioAssayDataBean bioAssayData = new DerivedBioAssayDataBean();
		// bioAssayData.getDomainBioAssayData().setDerivedDatumCollection(
		// new HashSet<DerivedDatum>());
		// derivedBioAssayDataList.add(bioAssayData);
	}

	public CharacterizationBean(Characterization chara) {
		domainChar = chara;
		className = ClassUtils.getShortClassName(chara.getClass().getName());
		this.description = chara.getDescription();
		this.viewTitle = chara.getIdentificationName();
		if (chara.getInstrumentConfiguration() != null) {
			instrumentConfig = chara.getInstrumentConfiguration();
		}
		if (instrumentConfig.getInstrument() == null) {
			instrumentConfig.setInstrument(new Instrument());
		}
		if (chara.getDerivedBioAssayDataCollection() != null) {
			for (DerivedBioAssayData bioassayData : chara
					.getDerivedBioAssayDataCollection()) {
				derivedBioAssayDataList.add(new DerivedBioAssayDataBean(
						bioassayData));
			}
		}
		if (chara.getProtocolFile() != null) {
			protocolFileBean = new ProtocolFileBean(chara.getProtocolFile());
		}
	}

	public void setDomainChar() {
		try {
			// take care of characterizations that don't have any special
			// properties shown in the form, e.g. Size
			if (domainChar == null) {
				Class clazz = ClassUtils.getFullClass(className);
				domainChar = (Characterization) clazz.newInstance();
			}
			if (domainChar.getId() == null) {
				domainChar.setCreatedBy(createdBy);
				domainChar.setCreatedDate(new Date());
			}
			domainChar.setDescription(description);
			domainChar.setIdentificationName(viewTitle);

			if (instrumentConfig.getInstrument() != null
					&& instrumentConfig.getInstrument().getType() != null
					&& instrumentConfig.getInstrument().getType().length() > 0) {
				if (instrumentConfig.getId() == null) {
					instrumentConfig.setCreatedBy(createdBy);
					instrumentConfig.setCreatedDate(new Date());
				}
				domainChar.setInstrumentConfiguration(instrumentConfig);
			}
			// domainChar
			// .setProtocolFile(protocolFileBean.getDomainProtocolFile());
			if (domainChar.getDerivedBioAssayDataCollection() != null) {
				domainChar.getDerivedBioAssayDataCollection().clear();
			} else {
				domainChar
						.setDerivedBioAssayDataCollection(new HashSet<DerivedBioAssayData>());
			}
			// set createdBy and createdDate
			for (DerivedBioAssayDataBean bioAssayData : derivedBioAssayDataList) {
				if (bioAssayData.getDomainBioAssayData().getId() == null) {
					bioAssayData.getDomainBioAssayData()
							.setCreatedBy(createdBy);
					bioAssayData.getDomainBioAssayData().setCreatedDate(
							new Date());
					domainChar.getDerivedBioAssayDataCollection().add(
							bioAssayData.getDomainBioAssayData());
					for (DerivedDatum datum : bioAssayData.getDatumList()) {
						if (datum.getId() == null) {
							datum.setCreatedBy(createdBy);
							datum.setCreatedDate(new Date());
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// /**
	// * Copy constructor
	// *
	// * @param charBean
	// */
	// public CharacterizationBean(CharacterizationBean charBean) {
	// this.viewTitle = charBean.getViewTitle();
	// this.characterizationSource = charBean.getCharacterizationSource();
	// this.derivedBioAssayDataList = charBean.getDerivedBioAssayDataList();
	// this.description = charBean.getDescription();
	// this.instrumentConfig = charBean.getInstrumentConfiguration();
	// this.protocolFileBean = charBean.getProtocolFileBean();
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
		derivedBioAssayDataList.add(new DerivedBioAssayDataBean());
	}

	public void removeDerivedBioAssayData(int ind) {
		derivedBioAssayDataList.remove(ind);
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<DerivedBioAssayDataBean> getDerivedBioAssayDataList() {
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

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
}

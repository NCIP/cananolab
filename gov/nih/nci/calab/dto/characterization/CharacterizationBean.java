package gov.nih.nci.calab.dto.characterization;

import gov.nih.nci.calab.domain.InstrumentConfiguration;
import gov.nih.nci.calab.domain.ProtocolFile;
import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.DerivedBioAssayData;
import gov.nih.nci.calab.dto.common.InstrumentConfigBean;
import gov.nih.nci.calab.dto.common.ProtocolFileBean;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.service.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class represents shared characterization properties to be shown in
 * characterization view pages.
 * 
 * @author pansu
 * 
 */
public class CharacterizationBean {
	private String id;

	private String characterizationSource;

	// used to distinguish different instances of characterizations, which are
	// shown as different links on the view pages.
	private String viewTitle;

	// used for link color on the side menu
	private String viewColor;

	private String description;

	// not set by application
	private String name;

	// Abbreviation
	private String abbr;

	// not set by application
	private String classification;

	private String createdBy;

	private Date createdDate;

	private InstrumentConfigBean instrumentConfigBean = new InstrumentConfigBean();

	private List<DerivedBioAssayDataBean> derivedBioAssayDataList = new ArrayList<DerivedBioAssayDataBean>();

	private ProtocolFileBean protocolFileBean = new ProtocolFileBean();

	private String actionName;

	private String particleName;
	
	private String particleType;

	public String getActionName() {
		actionName = StringUtils.getOneWordLowerCaseFirstLetter(name);
		return actionName;
	}

	public CharacterizationBean() {

	}

	/** Used in the side menu tree */
	public CharacterizationBean(String name, String abbr) {
		this.name = name;
		this.abbr = abbr;
	}

	/**
	 * Copy constructor
	 * 
	 * @param charBean
	 */
	public CharacterizationBean(CharacterizationBean charBean) {
		this.id = charBean.getId();
		this.name = charBean.getName();
		this.viewTitle = charBean.getViewTitle();
		this.characterizationSource = charBean.getCharacterizationSource();
		this.classification = charBean.getClassification();
		this.createdBy = charBean.getCreatedBy();
		this.createdDate = charBean.getCreatedDate();
		this.derivedBioAssayDataList = charBean.getDerivedBioAssayDataList();
		this.description = charBean.getDescription();
		this.instrumentConfigBean = charBean.getInstrumentConfigBean();
		this.protocolFileBean = charBean.getProtocolFileBean();
		this.particleType=charBean.getParticleType();
		this.particleName=charBean.getParticleName();
	}

	public CharacterizationBean(String id, String name, String viewTitle) {
		this.id = id;
		this.name = name;
		setAbbr(name);
		this.viewTitle = viewTitle;
	}

	public CharacterizationBean(Characterization characterization) {
		this.setId(characterization.getId().toString());
		this.setViewTitle(characterization.getIdentificationName());
		this.setCharacterizationSource(characterization.getSource());
		this.setCreatedBy(characterization.getCreatedBy());
		this.setCreatedDate(characterization.getCreatedDate());
		this.name = characterization.getName();
		setAbbr(name);

		this.setDescription(characterization.getDescription());
		InstrumentConfiguration instrumentConfigObj = characterization
				.getInstrumentConfiguration();
		if (instrumentConfigObj != null) {
			instrumentConfigBean = new InstrumentConfigBean(instrumentConfigObj);
		}
		for (DerivedBioAssayData table : characterization
				.getDerivedBioAssayDataCollection()) {
			DerivedBioAssayDataBean ctBean = new DerivedBioAssayDataBean(table);
			this.getDerivedBioAssayDataList().add(ctBean);
		}
		ProtocolFile protocolFile = characterization.getProtocolFile();
		if (protocolFile != null) {
			protocolFileBean = new ProtocolFileBean(protocolFile);
		}
	}

	public String getCharacterizationSource() {
		return characterizationSource;
	}

	public void setCharacterizationSource(String characterizationSource) {
		this.characterizationSource = characterizationSource;
	}

	public String getViewTitle() {
		// get only the first number of characters of the title
		if (viewTitle != null
				&& viewTitle.length() > CaNanoLabConstants.MAX_VIEW_TITLE_LENGTH) {
			return viewTitle.substring(0,
					CaNanoLabConstants.MAX_VIEW_TITLE_LENGTH);
		}
		return viewTitle;
	}

	public void setViewTitle(String viewTitle) {
		this.viewTitle = viewTitle;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Update the domain characterization object from the dto bean properties
	 * 
	 * @return
	 */
	public void updateDomainObj(Characterization doChar) {
		doChar.setSource(getCharacterizationSource());
		doChar.setIdentificationName(getViewTitle());
		doChar.setDescription(getDescription());
		doChar.setCreatedBy(getCreatedBy());
		doChar.setCreatedDate(getCreatedDate());
		updateDerivedBioAssayData(doChar);				
	}	

	// update domain object's derivedBioAssayData collection
	private void updateDerivedBioAssayData(Characterization doChar) {
		// copy collection
		List<DerivedBioAssayData> doDerivedDataList = new ArrayList<DerivedBioAssayData>(
				doChar.getDerivedBioAssayDataCollection());
		// clear the existing collection
		doChar.getDerivedBioAssayDataCollection().clear();
		for (DerivedBioAssayDataBean derivedBioAssayDataBean : getDerivedBioAssayDataList()) {
			DerivedBioAssayData doDerivedBioAssayData = null;
			// if no id, add new domain object
			if (derivedBioAssayDataBean.getId() == null) {
				doDerivedBioAssayData = new DerivedBioAssayData();
			} else {
				// find domain object with the same ID and add the updated
				// domain object
				for (DerivedBioAssayData doData : doDerivedDataList) {
					if (doData.getId().equals(
							new Long(derivedBioAssayDataBean.getId()))) {
						doDerivedBioAssayData = doData;
						break;
					}
				}
			}
			derivedBioAssayDataBean.updateDomainObj(doDerivedBioAssayData);
			doChar.getDerivedBioAssayDataCollection()
					.add(doDerivedBioAssayData);
		}
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getClassification() {
		return classification;
	}

	public String getName() {
		return name;
	}

	public void setAbbr(String abbr) {
		this.abbr = abbr;
	}

	public String getAbbr() {
		return abbr;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public List<DerivedBioAssayDataBean> getDerivedBioAssayDataList() {
		return derivedBioAssayDataList;
	}

	public void setDerivedBioAssayDataList(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		this.derivedBioAssayDataList = derivedBioAssayData;
	}

	public InstrumentConfigBean getInstrumentConfigBean() {
		return instrumentConfigBean;
	}

	public void setInstrumentConfigBean(
			InstrumentConfigBean instrumentConfigBean) {
		this.instrumentConfigBean = instrumentConfigBean;
	}

	public ProtocolFileBean getProtocolFileBean() {
		return protocolFileBean;
	}

	public void setProtocolFileBean(ProtocolFileBean protocolFileBean) {
		this.protocolFileBean = protocolFileBean;
	}

	public String getViewColor() {
		if (viewTitle.matches("^copy_\\d{15}?")) {
			viewColor = CaNanoLabConstants.AUTO_COPY_CHARACTERIZATION_VIEW_COLOR;
		}
		return viewColor;
	}

	public String getParticleName() {
		return particleName;
	}

	public void setParticleName(String particleName) {
		this.particleName = particleName;
	}

	public String getParticleType() {
		return particleType;
	}

	public void setParticleType(String particleType) {
		this.particleType = particleType;
	}

	/**
	 * Create a new instance of CharacterizationBean with the same metadata
	 * except DerivedBioAssayDataList and InstrumentConfig.
	 * 
	 * @return
	 */
	public CharacterizationBean copy(boolean copyData) {
		CharacterizationBean newCharBean = new CharacterizationBean(this);
		// unset id
		newCharBean.setId(null);
		// set InstrumentConfig, DerivedBioAssayDataList
		InstrumentConfigBean newInstrumentConfigBean = instrumentConfigBean
				.copy();
		newCharBean.setInstrumentConfigBean(newInstrumentConfigBean);

		List<DerivedBioAssayDataBean> newDerivedBioAssayDataList = new ArrayList<DerivedBioAssayDataBean>();
		for (DerivedBioAssayDataBean derivedBioAssayDataBean : derivedBioAssayDataList) {
			DerivedBioAssayDataBean newDerivedBioAssayDataBean = derivedBioAssayDataBean
					.copy(copyData);
			newDerivedBioAssayDataList.add(newDerivedBioAssayDataBean);
		}
		newCharBean.setDerivedBioAssayDataList(newDerivedBioAssayDataList);
		return newCharBean;
	}
}

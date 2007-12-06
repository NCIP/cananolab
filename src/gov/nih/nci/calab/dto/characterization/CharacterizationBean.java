package gov.nih.nci.calab.dto.characterization;

import gov.nih.nci.calab.domain.InstrumentConfiguration;
import gov.nih.nci.calab.domain.ProtocolFile;
import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.DerivedBioAssayData;
import gov.nih.nci.calab.dto.common.InstrumentConfigBean;
import gov.nih.nci.calab.dto.common.ProtocolFileBean;
import gov.nih.nci.calab.dto.particle.ParticleBean;
import gov.nih.nci.calab.dto.sample.SampleBean;
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

	private ParticleBean particle = new ParticleBean();

	public String getActionName() {
		this.actionName = StringUtils.getOneWordLowerCaseFirstLetter(this.name);
		return this.actionName;
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
		this.particle = charBean.getParticle();
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
		setAbbr(this.name);

		this.setDescription(characterization.getDescription());
		InstrumentConfiguration instrumentConfigObj = characterization
				.getInstrumentConfiguration();
		if (instrumentConfigObj != null) {
			this.instrumentConfigBean = new InstrumentConfigBean(
					instrumentConfigObj);
		}
		for (DerivedBioAssayData table : characterization
				.getDerivedBioAssayDataCollection()) {
			DerivedBioAssayDataBean ctBean = new DerivedBioAssayDataBean(table);
			this.getDerivedBioAssayDataList().add(ctBean);
		}
		ProtocolFile protocolFile = characterization.getProtocolFile();
		if (protocolFile != null) {
			this.protocolFileBean = new ProtocolFileBean(protocolFile);
		}
	}

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

	public String getId() {
		return this.id;
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
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getClassification() {
		return this.classification;
	}

	public String getName() {
		return this.name;
	}

	public void setAbbr(String abbr) {
		this.abbr = abbr;
	}

	public String getAbbr() {
		return this.abbr;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public List<DerivedBioAssayDataBean> getDerivedBioAssayDataList() {
		return this.derivedBioAssayDataList;
	}

	public void setDerivedBioAssayDataList(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		this.derivedBioAssayDataList = derivedBioAssayData;
	}

	public InstrumentConfigBean getInstrumentConfigBean() {
		return this.instrumentConfigBean;
	}

	public void setInstrumentConfigBean(
			InstrumentConfigBean instrumentConfigBean) {
		this.instrumentConfigBean = instrumentConfigBean;
	}

	public ProtocolFileBean getProtocolFileBean() {
		return this.protocolFileBean;
	}

	public void setProtocolFileBean(ProtocolFileBean protocolFileBean) {
		this.protocolFileBean = protocolFileBean;
	}

	public String getViewColor() {
		if (this.viewTitle != null && this.viewTitle.matches("^copy_\\d{15}?")) {
			this.viewColor = CaNanoLabConstants.AUTO_COPY_CHARACTERIZATION_VIEW_COLOR;
		}
		return this.viewColor;
	}

	public ParticleBean getParticle() {
		return particle;
	}

	public void setParticle(ParticleBean particle) {
		this.particle = particle;
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
		InstrumentConfigBean newInstrumentConfigBean = this.instrumentConfigBean
				.copy();
		newCharBean.setInstrumentConfigBean(newInstrumentConfigBean);

		List<DerivedBioAssayDataBean> newDerivedBioAssayDataList = new ArrayList<DerivedBioAssayDataBean>();
		for (DerivedBioAssayDataBean derivedBioAssayDataBean : this.derivedBioAssayDataList) {
			DerivedBioAssayDataBean newDerivedBioAssayDataBean = derivedBioAssayDataBean
					.copy(copyData);
			newDerivedBioAssayDataList.add(newDerivedBioAssayDataBean);
		}
		newCharBean.setDerivedBioAssayDataList(newDerivedBioAssayDataList);
		return newCharBean;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean equals(Object obj) {
		boolean eq = false;
		if (obj instanceof CharacterizationBean) {
			CharacterizationBean c = (CharacterizationBean) obj;
			String thisId = getId();

			if (thisId != null && thisId.equals(c.getId())) {
				eq = true;
			}
		}
		return eq;
	}

	public int hashCode() {
		int h = 0;
		if (getId() != null) {
			h += getId().hashCode();
		}
		return h;
	}
}

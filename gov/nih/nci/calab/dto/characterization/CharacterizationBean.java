package gov.nih.nci.calab.dto.characterization;

import gov.nih.nci.calab.domain.Instrument;
import gov.nih.nci.calab.domain.InstrumentConfiguration;
import gov.nih.nci.calab.domain.ProtocolFile;
import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.DerivedBioAssayData;
import gov.nih.nci.calab.dto.common.InstrumentBean;
import gov.nih.nci.calab.dto.common.InstrumentConfigBean;
import gov.nih.nci.calab.dto.common.ProtocolFileBean;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;

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

	private String numberOfDerivedBioAssayData;

	private ProtocolFileBean protocolFileBean = new ProtocolFileBean();

	public CharacterizationBean() {

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
		this.setNumberOfDerivedBioAssayData(Integer.valueOf(
				characterization.getDerivedBioAssayDataCollection().size())
				.toString());
		for (DerivedBioAssayData table : characterization
				.getDerivedBioAssayDataCollection()) {
			DerivedBioAssayDataBean ctBean = new DerivedBioAssayDataBean(table);
			this.getDerivedBioAssayDataList().add(ctBean);
		}
		ProtocolFile protocolFile = characterization.getProtocolFile();
		if (protocolFile != null) {
			protocolFileBean = new ProtocolFileBean(protocolFile);
		}
		this.numberOfDerivedBioAssayData = derivedBioAssayDataList.size() + "";
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
	public void updateDomainObj(Characterization aChar) {
		if (getId() != null && getId().length() > 0) {
			aChar.setId(new Long(getId()));
		}
		aChar.setSource(getCharacterizationSource());
		aChar.setIdentificationName(getViewTitle());
		aChar.setDescription(getDescription());
		aChar.setCreatedBy(getCreatedBy());
		aChar.setCreatedDate(getCreatedDate());

		for (DerivedBioAssayDataBean table : getDerivedBioAssayDataList()) {
			aChar.getDerivedBioAssayDataCollection().add(table.getDomainObj());
		}

		InstrumentConfiguration instrumentConfig = new InstrumentConfiguration();
		if (instrumentConfigBean.getId() != null
				&& instrumentConfigBean.getId().length() > 0) {
			instrumentConfig.setId(new Long(instrumentConfigBean.getId()));
		}

		instrumentConfig.setDescription(instrumentConfigBean.getDescription());

		InstrumentBean instrumentBean = instrumentConfigBean
				.getInstrumentBean();
		if (instrumentConfigBean.getInstrumentBean() != null) {
			Instrument instrument = new Instrument();
			if (instrumentBean.getId() != null
					&& instrumentBean.getId().length() > 0) {
				instrument.setId(new Long(instrumentBean.getId()));
			}
			// set instrument only if type and manufacturer information are not
			// empty
			if (instrumentBean.getType().length() > 0
					&& instrumentBean.getManufacturer().length() > 0) {

				instrument.setAbbreviation(instrumentBean.getAbbreviation());
				instrument.setManufacturer(instrumentBean.getManufacturer());
				instrument.setType(instrumentBean.getType());
				instrumentConfig.setInstrument(instrument);
			}
		}
		if (instrumentConfig.getInstrument()!=null) {
			aChar.setInstrumentConfiguration(instrumentConfig);
		}

		if (protocolFileBean.getId() != null
				&& protocolFileBean.getId().length() > 0) {
			ProtocolFile protocolFile = new ProtocolFile();
			protocolFile.setId(new Long(protocolFileBean.getId()));
			aChar.setProtocolFile(protocolFile);
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

	private void setAbbr(String name) {
		if (name.equals(CaNanoLabConstants.PHYSICAL_COMPOSITION)) {
			this.abbr = CaNanoLabConstants.ABBR_COMPOSITION;
		} else if (name.equals(CaNanoLabConstants.PHYSICAL_SIZE)) {
			this.abbr = CaNanoLabConstants.ABBR_SIZE;
		} else if (name.equals(CaNanoLabConstants.PHYSICAL_MOLECULAR_WEIGHT)) {
			this.abbr = CaNanoLabConstants.ABBR_MOLECULAR_WEIGHT;
		} else if (name.equals(CaNanoLabConstants.PHYSICAL_MORPHOLOGY)) {
			this.abbr = CaNanoLabConstants.ABBR_MORPHOLOGY;
		} else if (name.equals(CaNanoLabConstants.PHYSICAL_SHAPE)) {
			this.abbr = CaNanoLabConstants.ABBR_SHAPE;
		} else if (name.equals(CaNanoLabConstants.PHYSICAL_SOLUBILITY)) {
			this.abbr = CaNanoLabConstants.ABBR_SOLUBILITY;
		} else if (name.equals(CaNanoLabConstants.PHYSICAL_SURFACE)) {
			this.abbr = CaNanoLabConstants.ABBR_SURFACE;
		} else if (name.equals(CaNanoLabConstants.PHYSICAL_PURITY)) {
			this.abbr = CaNanoLabConstants.ABBR_PURITY;
		} else if (name.equals(CaNanoLabConstants.TOXICITY_OXIDATIVE_STRESS)) {
			this.abbr = CaNanoLabConstants.ABBR_OXIDATIVE_STRESS;
		} else if (name.equals(CaNanoLabConstants.TOXICITY_ENZYME_FUNCTION)) {
			this.abbr = CaNanoLabConstants.ABBR_ENZYME_FUNCTION;
		} else if (name.equals(CaNanoLabConstants.CYTOTOXICITY_CELL_VIABILITY)) {
			this.abbr = CaNanoLabConstants.ABBR_CELL_VIABILITY;
		} else if (name
				.equals(CaNanoLabConstants.CYTOTOXICITY_CASPASE3_ACTIVIATION)) {
			this.abbr = CaNanoLabConstants.ABBR_CASPASE3_ACTIVATION;
		} else if (name
				.equals(CaNanoLabConstants.BLOODCONTACTTOX_PLATE_AGGREGATION)) {
			this.abbr = CaNanoLabConstants.ABBR_PLATELET_AGGREGATION;
		} else if (name.equals(CaNanoLabConstants.BLOODCONTACTTOX_HEMOLYSIS)) {
			this.abbr = CaNanoLabConstants.ABBR_HEMOLYSIS;
		} else if (name
				.equals(CaNanoLabConstants.BLOODCONTACTTOX_PLASMA_PROTEIN_BINDING)) {
			this.abbr = CaNanoLabConstants.ABBR_PLASMA_PROTEIN_BINDING;
		} else if (name.equals(CaNanoLabConstants.BLOODCONTACTTOX_COAGULATION)) {
			this.abbr = CaNanoLabConstants.ABBR_COAGULATION;
		} else if (name
				.equals(CaNanoLabConstants.IMMUNOCELLFUNCTOX_OXIDATIVE_BURST)) {
			this.abbr = CaNanoLabConstants.ABBR_OXIDATIVE_BURST;
		} else if (name.equals(CaNanoLabConstants.IMMUNOCELLFUNCTOX_CHEMOTAXIS)) {
			this.abbr = CaNanoLabConstants.ABBR_CHEMOTAXIS;
		} else if (name
				.equals(CaNanoLabConstants.IMMUNOCELLFUNCTOX_LEUKOCYTE_PROLIFERATION)) {
			this.abbr = CaNanoLabConstants.ABBR_LEUKOCYTE_PROLIFERATION;
		} else if (name
				.equals(CaNanoLabConstants.IMMUNOCELLFUNCTOX_PHAGOCYTOSIS)) {
			this.abbr = CaNanoLabConstants.ABBR_PHAGOCYTOSIS;
		} else if (name
				.equals(CaNanoLabConstants.IMMUNOCELLFUNCTOX_CYTOKINE_INDUCTION)) {
			this.abbr = CaNanoLabConstants.ABBR_CYTOKINE_INDUCTION;
		} else if (name.equals(CaNanoLabConstants.IMMUNOCELLFUNCTOX_CFU_GM)) {
			this.abbr = CaNanoLabConstants.ABBR_CFU_GM;
		} else if (name
				.equals(CaNanoLabConstants.IMMUNOCELLFUNCTOX_COMPLEMENT_ACTIVATION)) {
			this.abbr = CaNanoLabConstants.ABBR_COMPLEMENT_ACTIVATION;
		} else if (name
				.equals(CaNanoLabConstants.IMMUNOCELLFUNCTOX_NKCELL_CYTOTOXIC_ACTIVITY)) {
			this.abbr = CaNanoLabConstants.ABBR_NKCELL_CYTOTOXIC_ACTIVITY;
		} else {
			this.abbr = CaNanoLabConstants.OTHER; // shouldn't happen at all.
		}
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

	public Characterization getDomainObj() {
		return null;
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

	public String getNumberOfDerivedBioAssayData() {
		return numberOfDerivedBioAssayData;
	}

	public void setNumberOfDerivedBioAssayData(
			String numberOfDerivedBioAssayData) {
		this.numberOfDerivedBioAssayData = numberOfDerivedBioAssayData;
	}

	public ProtocolFileBean getProtocolFileBean() {
		return protocolFileBean;
	}

	public void setProtocolFileBean(ProtocolFileBean protocolFileBean) {
		this.protocolFileBean = protocolFileBean;
	}
}

package gov.nih.nci.calab.dto.characterization;

import gov.nih.nci.calab.domain.Instrument;
import gov.nih.nci.calab.domain.InstrumentType;
import gov.nih.nci.calab.domain.Manufacturer;
import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.CharacterizationProtocol;
import gov.nih.nci.calab.domain.nano.characterization.DerivedBioAssayData;
import gov.nih.nci.calab.service.util.CananoConstants;
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

	private String description;

	// not set by application
	private String name;

	// Abbreviation
	private String abbr;
	
	// not set by application
	private String classification;

	private String createdBy;

	private Date createdDate;

	private InstrumentBean instrument = new InstrumentBean();

	private List<DerivedBioAssayDataBean> derivedBioAssayDataList = new ArrayList<DerivedBioAssayDataBean>();

	private String numberOfDerivedBioAssayData;

	private CharacterizationProtocolBean characterizationProtocol = new CharacterizationProtocolBean();

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

		this.setDescription(characterization.getDescription());
		Instrument instrument = characterization.getInstrument();
		if (instrument != null) {
			this.getInstrument().setType(
					(instrument.getInstrumentType() != null) ? instrument
							.getInstrumentType().getName() : "");
			this.getInstrument().setDescription(
					StringUtils.convertToString(instrument.getDescription()));
			this.getInstrument().setManufacturer(
					(instrument.getManufacturer() != null) ? instrument
							.getManufacturer().getName() : "");
		}
		this.setNumberOfDerivedBioAssayData(Integer.valueOf(
				characterization.getDerivedBioAssayDataCollection().size())
				.toString());
		for (DerivedBioAssayData table : characterization
				.getDerivedBioAssayDataCollection()) {
			DerivedBioAssayDataBean ctBean = new DerivedBioAssayDataBean(table);
			this.getDerivedBioAssayDataList().add(ctBean);
		}
		CharacterizationProtocol protocol = characterization
				.getCharacterizationProtocol();
		if (protocol != null) {
			this.getCharacterizationProtocol().setId(protocol.getId());
			this.getCharacterizationProtocol().setName(protocol.getName());
			this.getCharacterizationProtocol()
					.setVersion(protocol.getVersion());
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
		if (viewTitle!=null &&viewTitle.length() > CananoConstants.MAX_VIEW_TITLE_LENGTH) {
			return viewTitle.substring(0, CananoConstants.MAX_VIEW_TITLE_LENGTH);
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

		Instrument instrument = new Instrument();

		if (getInstrument().getId() != null)
			instrument.setId(new Long(getInstrument().getId()));

		instrument.setDescription(getInstrument().getDescription());

		String iType = getInstrument().getType();
		String manuf = getInstrument().getManufacturer();

		if (iType != null && manuf != null) {
			InstrumentType instrumentType = new InstrumentType();
			if (iType.equals(CananoConstants.OTHER))
				instrumentType
						.setName(getInstrument().getOtherInstrumentType());
			else
				instrumentType.setName(getInstrument().getType());

			Manufacturer manufacturer = new Manufacturer();

			if (manuf.equals(CananoConstants.OTHER))
				manufacturer.setName(getInstrument().getOtherManufacturer());
			else
				manufacturer.setName(getInstrument().getManufacturer());

			instrument.setInstrumentType(instrumentType);
			instrument.setManufacturer(manufacturer);

			aChar.setInstrument(instrument);
		}

		CharacterizationProtocolBean characterizationProtocolBean = getCharacterizationProtocol();

		if (characterizationProtocolBean.getName() != null
				&& characterizationProtocolBean.getName() != ""
				&& characterizationProtocolBean.getVersion() != null
				&& characterizationProtocolBean.getVersion() != "") {

			CharacterizationProtocol characterizationProtocol = new CharacterizationProtocol();
			characterizationProtocol.setName(characterizationProtocolBean
					.getName());
			characterizationProtocol.setVersion(characterizationProtocolBean
					.getVersion());
			characterizationProtocol
					.setId(characterizationProtocolBean.getId());

			aChar.setCharacterizationProtocol(characterizationProtocol);
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

	private void setAbbr(String name){
		if (name.equals(CananoConstants.PHYSICAL_COMPOSITION)) {
			this.abbr =  CananoConstants.ABBR_COMPOSITION;
		} else if (name.equals(CananoConstants.PHYSICAL_SIZE)) {
			this.abbr =   CananoConstants.ABBR_SIZE;
		} else if (name.equals(CananoConstants.PHYSICAL_MOLECULAR_WEIGHT)) {
			this.abbr =   CananoConstants.ABBR_MOLECULAR_WEIGHT;
		} else if (name.equals(CananoConstants.PHYSICAL_MORPHOLOGY)) {
			this.abbr =   CananoConstants.ABBR_MORPHOLOGY;
		} else if (name.equals(CananoConstants.PHYSICAL_SHAPE)) {
			this.abbr =   CananoConstants.ABBR_SHAPE;
		} else if (name.equals(CananoConstants.PHYSICAL_SOLUBILITY)) {
			this.abbr =   CananoConstants.ABBR_SOLUBILITY;
		} else if (name.equals(CananoConstants.PHYSICAL_SURFACE)) {
			this.abbr =   CananoConstants.ABBR_SURFACE;
		} else if (name.equals(CananoConstants.PHYSICAL_PURITY)) {
			this.abbr =   CananoConstants.ABBR_PURITY;
		} else if (name.equals(CananoConstants.TOXICITY_OXIDATIVE_STRESS)) {
			this.abbr =   CananoConstants.ABBR_OXIDATIVE_STRESS;
		} else if (name.equals(CananoConstants.TOXICITY_ENZYME_FUNCTION)) {
			this.abbr =   CananoConstants.ABBR_ENZYME_FUNCTION;
		} else if (name.equals(CananoConstants.CYTOTOXICITY_CELL_VIABILITY)) {
			this.abbr =   CananoConstants.ABBR_CELL_VIABILITY;
		} else if (name.equals(CananoConstants.CYTOTOXICITY_CASPASE3_ACTIVIATION)) {
			this.abbr =   CananoConstants.ABBR_CASPASE3_ACTIVATION;
		} else if (name.equals(CananoConstants.BLOODCONTACTTOX_PLATE_AGGREGATION)) {
			this.abbr =   CananoConstants.ABBR_PLATELET_AGGREGATION;
		} else if (name.equals(CananoConstants.BLOODCONTACTTOX_HEMOLYSIS)) {
			this.abbr =   CananoConstants.ABBR_HEMOLYSIS;
		} else if (name.equals(CananoConstants.BLOODCONTACTTOX_PLASMA_PROTEIN_BINDING)) {
			this.abbr =   CananoConstants.ABBR_PLASMA_PROTEIN_BINDING;
		} else if (name.equals(CananoConstants.BLOODCONTACTTOX_COAGULATION)) {
			this.abbr =   CananoConstants.ABBR_COAGULATION;
		} else if (name.equals(CananoConstants.IMMUNOCELLFUNCTOX_OXIDATIVE_BURST)) {
			this.abbr =   CananoConstants.ABBR_OXIDATIVE_BURST;
		} else if (name.equals(CananoConstants.IMMUNOCELLFUNCTOX_CHEMOTAXIS)) {
			this.abbr =   CananoConstants.ABBR_CHEMOTAXIS;
		} else if (name.equals(CananoConstants.IMMUNOCELLFUNCTOX_LEUKOCYTE_PROLIFERATION)) {
			this.abbr =   CananoConstants.ABBR_LEUKOCYTE_PROLIFERATION;
		} else if (name.equals(CananoConstants.IMMUNOCELLFUNCTOX_PHAGOCYTOSIS)) {
			this.abbr =   CananoConstants.ABBR_PHAGOCYTOSIS;
		} else if (name.equals(CananoConstants.IMMUNOCELLFUNCTOX_CYTOKINE_INDUCTION)) {
			this.abbr =   CananoConstants.ABBR_CYTOKINE_INDUCTION;
		} else if (name.equals(CananoConstants.IMMUNOCELLFUNCTOX_CFU_GM)) {
			this.abbr =   CananoConstants.ABBR_CFU_GM;
		} else if (name.equals(CananoConstants.IMMUNOCELLFUNCTOX_COMPLEMENT_ACTIVATION)) {
			this.abbr =   CananoConstants.ABBR_COMPLEMENT_ACTIVATION;
		} else if (name.equals(CananoConstants.IMMUNOCELLFUNCTOX_NKCELL_CYTOTOXIC_ACTIVITY)) {
			this.abbr =   CananoConstants.ABBR_NKCELL_CYTOTOXIC_ACTIVITY;
		} else {
			this.abbr = CananoConstants.OTHER; // shouldn't happen at all.
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

	public InstrumentBean getInstrument() {
		return instrument;
	}

	public void setInstrument(InstrumentBean instrument) {
		this.instrument = instrument;
	}

	public String getNumberOfDerivedBioAssayData() {
		return numberOfDerivedBioAssayData;
	}

	public void setNumberOfDerivedBioAssayData(
			String numberOfDerivedBioAssayData) {
		this.numberOfDerivedBioAssayData = numberOfDerivedBioAssayData;
	}

	public CharacterizationProtocolBean getCharacterizationProtocol() {
		return characterizationProtocol;
	}

	public void setCharacterizationProtocol(
			CharacterizationProtocolBean characterizationProtocol) {
		this.characterizationProtocol = characterizationProtocol;
	}
}

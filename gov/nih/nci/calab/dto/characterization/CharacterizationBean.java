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

	// not set by application
	private String classification;

	private String createdBy;

	private Date createdDate;

	private InstrumentBean instrument=new InstrumentBean();

	private List<DerivedBioAssayDataBean> derivedBioAssayDataList = new ArrayList<DerivedBioAssayDataBean>();

	private String numberOfDerivedBioAssayData;
	
	private CharacterizationProtocolBean characterizationProtocol = new CharacterizationProtocolBean();
	
	public CharacterizationBean() {

	}

	public CharacterizationBean(String id, String name, String viewTitle) {
		this.id = id;
		this.name = name;
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
			this.getInstrument().setType((instrument.getInstrumentType()!=null)?instrument.getInstrumentType().getName():"");
			this.getInstrument().setDescription(StringUtils.convertToString(instrument.getDescription()));
			this.getInstrument().setManufacturer((instrument.getManufacturer()!=null)?instrument.getManufacturer().getName():"");
		}
		this.setNumberOfDerivedBioAssayData(Integer.valueOf(characterization.getDerivedBioAssayDataCollection().size()).toString());
		for (DerivedBioAssayData table : characterization.getDerivedBioAssayDataCollection()) {
			DerivedBioAssayDataBean ctBean = new DerivedBioAssayDataBean(table);
			this.getDerivedBioAssayDataList().add(ctBean);
		}
		CharacterizationProtocol protocol = characterization.getCharacterizationProtocol();
		if (protocol != null) {
			this.getCharacterizationProtocol().setId(protocol.getId());
			this.getCharacterizationProtocol().setName(protocol.getName());
			this.getCharacterizationProtocol().setVersion(protocol.getVersion());
		}
		this.numberOfDerivedBioAssayData=derivedBioAssayDataList.size()+"";
	}

	public String getCharacterizationSource() {
		return characterizationSource;
	}

	public void setCharacterizationSource(String characterizationSource) {
		this.characterizationSource = characterizationSource;
	}

	public String getViewTitle() {
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
			aChar.getDerivedBioAssayDataCollection().add(
					table.getDomainObj());
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
				instrumentType.setName(getInstrument().getOtherInstrumentType());
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
		
		if (characterizationProtocolBean.getName() != null && characterizationProtocolBean.getName() != "" &&
			characterizationProtocolBean.getVersion() != null && characterizationProtocolBean.getVersion() != "" ) {
			
			CharacterizationProtocol characterizationProtocol  = new CharacterizationProtocol();
			characterizationProtocol.setName(characterizationProtocolBean.getName());
			characterizationProtocol.setVersion(characterizationProtocolBean.getVersion());
			characterizationProtocol.setId(characterizationProtocolBean.getId());

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

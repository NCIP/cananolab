package gov.nih.nci.calab.dto.characterization;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.service.search.SearchSampleService;
import gov.nih.nci.calab.domain.Instrument;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * This class represents shared characterization properties to be shown in
 * characterization view pages.
 * 
 * @author pansu
 * 
 */
public class CharacterizationBean {
	private static Logger logger = Logger.getLogger(CharacterizationBean.class);

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

	private List<DerivedBioAssayDataBean> derivedBioAssayData = new ArrayList<DerivedBioAssayDataBean>();

	private String numberOfDerivedBioAssayData;
	
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
		
		for (DerivedBioAssayDataBean table : getDerivedBioAssayData()) {
			aChar.getDerivedBioAssayDataCollection().add(
					table.getDomainObj());
		}

		Instrument instrument = new Instrument(); 
		
		if (getInstrument().getId() != null)
			instrument.setId(new Long(getInstrument().getId()));
		instrument.setDescription(getInstrument().getDescription());
		instrument.setManufacturer(getInstrument().getManufacturer());
		instrument.setType(getInstrument().getType());
		aChar.setInstrument(instrument);
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

	public List<DerivedBioAssayDataBean> getDerivedBioAssayData() {
		return derivedBioAssayData;
	}

	public void setDerivedBioAssayData(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		this.derivedBioAssayData = derivedBioAssayData;
	}

	public InstrumentBean getInstrument() {
		return instrument;
	}

	public void setInstrument(InstrumentBean instrument) {
		this.instrument = instrument;
	}
	
	public DerivedBioAssayDataBean getData(int ind) {
		return derivedBioAssayData.get(ind);
	}

	public void setData(int ind, DerivedBioAssayDataBean table) {
		derivedBioAssayData.set(ind, table);
	}

	public String getNumberOfDerivedBioAssayData() {
		return numberOfDerivedBioAssayData;
	}

	public void setNumberOfDerivedBioAssayData(
			String numberOfDerivedBioAssayData) {
		this.numberOfDerivedBioAssayData = numberOfDerivedBioAssayData;
	}
}

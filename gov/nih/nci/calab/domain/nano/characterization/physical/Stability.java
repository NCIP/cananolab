/**
 * 
 */
package gov.nih.nci.calab.domain.nano.characterization.physical;

import gov.nih.nci.calab.domain.Instrument;
import gov.nih.nci.calab.domain.Measurement;
import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.CharacterizationProtocol;
import gov.nih.nci.calab.domain.nano.characterization.DerivedBioAssayData;
import gov.nih.nci.calab.domain.nano.particle.Nanoparticle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * @author zengje
 *
 */
public class Stability implements Characterization {

	private static final long serialVersionUID = 1234567890L;

	private Long id;
	private Measurement longTermStorage;
	private Measurement shortTermStorage;
	private String stressResult;
	private String releaseKineticsDescription;
	private String measurementType;
	private Stressor stressor;
	
	private String source;
	private String classification;
	private String description;
	private String identificationName;
	private String name;
	private String createdBy;
	private Date createdDate;
	private Collection<Nanoparticle> nanoparticleCollection;
	private Collection<DerivedBioAssayData> derivedBioAssayDataCollection = new ArrayList<DerivedBioAssayData>();
	private Instrument instrument;
	private CharacterizationProtocol characterizationProtocol;
	/**
	 * 
	 */
	public Stability() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Measurement getLongTermStorage() {
		return longTermStorage;
	}
	public void setLongTermStorage(Measurement longTermStorage) {
		this.longTermStorage = longTermStorage;
	}
	public String getMeasurementType() {
		return measurementType;
	}
	public void setMeasurementType(String measurementType) {
		this.measurementType = measurementType;
	}
	public String getReleaseKineticsDescription() {
		return releaseKineticsDescription;
	}
	public void setReleaseKineticsDescription(String releaseKineticsDescription) {
		this.releaseKineticsDescription = releaseKineticsDescription;
	}
	public Measurement getShortTermStorage() {
		return shortTermStorage;
	}
	public void setShortTermStorage(Measurement shortTermStorage) {
		this.shortTermStorage = shortTermStorage;
	}
	public Stressor getStressor() {
		return stressor;
	}
	public void setStressor(Stressor stressor) {
		this.stressor = stressor;
	}
	public String getStressResult() {
		return stressResult;
	}
	public void setStressResult(String stressResult) {
		this.stressResult = stressResult;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSource() {
		return this.source;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public String getClassification() {
		return PHYSICAL_CHARACTERIZATION;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIdentificationName() {
		return identificationName;
	}

	public void setIdentificationName(String identificationName) {
		this.identificationName = identificationName;
	}

	public String getName() {
		return PHYSICAL_STABILITY;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNanoparticleCollection(Collection<Nanoparticle> particles) {
		this.nanoparticleCollection = particles;
	}

	public Collection<Nanoparticle> getNanoparticleCollection() {
		return this.nanoparticleCollection;
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

	public Collection<DerivedBioAssayData> getDerivedBioAssayDataCollection() {
		return derivedBioAssayDataCollection;
	}

	public void setDerivedBioAssayDataCollection(
			Collection<DerivedBioAssayData> derivedBioAssayDataCollection) {
		this.derivedBioAssayDataCollection = derivedBioAssayDataCollection;
	}
	public Instrument getInstrument() {
		return instrument;
	}
	public void setInstrument(Instrument instrument) {
		this.instrument = instrument;
	}
	public CharacterizationProtocol getCharacterizationProtocol() {
		return characterizationProtocol;
	}
	public void setCharacterizationProtocol(
			CharacterizationProtocol characterizationProtocol) {
		this.characterizationProtocol = characterizationProtocol;
	}
}

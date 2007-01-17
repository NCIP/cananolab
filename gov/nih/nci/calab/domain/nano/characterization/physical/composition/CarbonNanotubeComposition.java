package gov.nih.nci.calab.domain.nano.characterization.physical.composition;

import gov.nih.nci.calab.domain.Instrument;
import gov.nih.nci.calab.domain.nano.characterization.CharacterizationProtocol;
import gov.nih.nci.calab.domain.nano.characterization.DerivedBioAssayData;
import gov.nih.nci.calab.domain.nano.particle.Nanoparticle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class CarbonNanotubeComposition implements ParticleComposition {

	private static final long serialVersionUID = 1234567890L;

	private String chirality;

	private Float growthDiameter;

	private Float averageLength;

	private String wallType;

	private Long id;

	private String source;

	private String description;

	private String identificationName;

	private String createdBy;

	private Date createdDate;

	private Collection<Nanoparticle> nanoparticleCollection;

	private Collection<ComposingElement> composingElementCollection = new ArrayList<ComposingElement>();

	private Collection<DerivedBioAssayData> derivedBioAssayDataCollection = new ArrayList<DerivedBioAssayData>();

	private Instrument instrument;

	private CharacterizationProtocol characterizationProtocol;

	public CarbonNanotubeComposition() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSource() {
		return this.source;
	}

	// public void setClassification(String classification) {
	// this.classification = classification;
	// }

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
		return PHYSICAL_COMPOSITION;
	}

	public Float getAverageLength() {
		return averageLength;
	}

	public void setAverageLength(Float averageLength) {
		this.averageLength = averageLength;
	}

	public String getChirality() {
		return chirality;
	}

	public void setChirality(String chirality) {
		this.chirality = chirality;
	}

	public Float getGrowthDiameter() {
		return growthDiameter;
	}

	public String getWallType() {
		return wallType;
	}

	public void setWallType(String wallType) {
		this.wallType = wallType;
	}

	public void setGrowthDiameter(Float growthDiameter) {
		this.growthDiameter = growthDiameter;
	}

	public void setNanoparticleCollection(
			Collection<Nanoparticle> particleCollcection) {
		this.nanoparticleCollection = particleCollcection;
	}

	public Collection<Nanoparticle> getNanoparticleCollection() {
		return this.nanoparticleCollection;
	}

	public void setComposingElementCollection(
			Collection<ComposingElement> element) {
		this.composingElementCollection = element;
	}

	public Collection<ComposingElement> getComposingElementCollection() {
		return this.composingElementCollection;
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

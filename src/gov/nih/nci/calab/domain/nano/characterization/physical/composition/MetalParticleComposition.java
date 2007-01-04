package gov.nih.nci.calab.domain.nano.characterization.physical.composition;

import gov.nih.nci.calab.domain.Instrument;
import gov.nih.nci.calab.domain.nano.characterization.CharacterizationProtocol;
import gov.nih.nci.calab.domain.nano.characterization.DerivedBioAssayData;
import gov.nih.nci.calab.domain.nano.particle.Nanoparticle;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class MetalParticleComposition implements ParticleComposition {
	
	private static final long serialVersionUID = 1234567890L;

	private Long id;
	private String source;
	private String description;
	private String identificationName;
	private String classification;
	private String name;
	private String createdBy;
	private Date createdDate;
	private Collection<Nanoparticle> nanoparticleCollection;
	private Collection<ComposingElement> composingElementCollection = new ArrayList<ComposingElement>();
	private Collection<DerivedBioAssayData> derivedBioAssayDataCollection = new ArrayList<DerivedBioAssayData>();
	private Instrument instrument;
	private CharacterizationProtocol characterizationProtocol;

	public MetalParticleComposition() {
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

	public String getClassification() {
		return CaNanoLabConstants.PHYSICAL_CHARACTERIZATION;
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

	public String getName() {
		return CaNanoLabConstants.PHYSICAL_COMPOSITION;
	}

	public void setIdentificationName(String identificationName) {
		this.identificationName = identificationName;
	}

	public void setNanoparticleCollection(Collection<Nanoparticle> particleCollection) {
		this.nanoparticleCollection = particleCollection;
	}

	public Collection<Nanoparticle> getNanoparticleCollection() {
		return this.nanoparticleCollection;
	}
	
	public void setComposingElementCollection(Collection<ComposingElement> element){
		this.composingElementCollection = element;
	}
	
	public Collection<ComposingElement> getComposingElementCollection(){
		return this.composingElementCollection;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public void setName(String name) {
		this.name = name;
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

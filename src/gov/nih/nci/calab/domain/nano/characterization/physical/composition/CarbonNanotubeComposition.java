package gov.nih.nci.calab.domain.nano.characterization.physical.composition;

import gov.nih.nci.calab.domain.nano.particle.Nanoparticle;
import gov.nih.nci.calab.service.util.CananoConstants;

import java.util.Collection;
import java.util.HashSet;

public class CarbonNanotubeComposition implements ParticleComposition {
	private Float chirality;
	private Float growthDiameter;
	private Float averageLength;
	private String wallType;
	
	private String classification;
	private String name;
	private Long id;
	private String source;
//	private String classification;
	private String description;
	private String identificationName;
//	private String name;
	private Collection<Nanoparticle> nanoparticleCollection;
	private Collection<ComposingElement> composingElementCollection = new HashSet<ComposingElement>();
		
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

//	public void setClassification(String classification) {
//		this.classification = classification;
//	}

	public String getClassification() {
		return CananoConstants.PHYSICAL_CHARACTERIZATION;
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
		return CananoConstants.PHYSICAL_COMPOSITION;
	}

	public Float getAverageLength() {
		return averageLength;
	}

	public void setAverageLength(Float averageLength) {
		this.averageLength = averageLength;
	}

	public Float getChirality() {
		return chirality;
	}

	public void setChirality(Float chirality) {
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

	public void setNanoparticleCollection(Collection<Nanoparticle> particleCollcection) {
		this.nanoparticleCollection = particleCollcection;
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
}

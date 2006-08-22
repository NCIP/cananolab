package gov.nih.nci.calab.domain.nano.characterization.physical.composition;

import gov.nih.nci.calab.domain.nano.particle.Nanoparticle;

import java.util.Collection;
import java.util.HashSet;

public class FullereneComposition implements Composition {
	private Float chirality;
	private Float growthDiameter;
	private Float averageLength;
	private Long id;
	private String source;
	private String classification;
	private Collection<Nanoparticle> particleCollection;
	private Collection<ComposingElement> composingElementCollection = new HashSet<ComposingElement>();
		
	public FullereneComposition() {
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

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public String getClassification() {
		return this.classification;
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

	public void setGrowthDiameter(Float growthDiameter) {
		this.growthDiameter = growthDiameter;
	}

	public void setParticleCollection(Collection<Nanoparticle> particles) {
		this.particleCollection = particles;
	}

	public Collection<Nanoparticle> getParticleCollection() {
		return this.particleCollection;
	}
	
	public void setComposingElementCollection(Collection<ComposingElement> element){
		this.composingElementCollection = element;
	}
	
	public Collection<ComposingElement> getComposingElementCollection(){
		return this.composingElementCollection;
	}
}

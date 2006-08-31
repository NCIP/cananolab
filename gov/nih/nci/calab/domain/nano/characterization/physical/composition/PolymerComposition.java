package gov.nih.nci.calab.domain.nano.characterization.physical.composition;

import gov.nih.nci.calab.domain.nano.particle.Nanoparticle;

import java.util.Collection;
import java.util.HashSet;

public class PolymerComposition implements Composition {
	private Long id;
	private String source;
	private String classification;
	private Collection<Nanoparticle> nanoparticleCollection;
	private Collection<ComposingElement> composingElementCollection = new HashSet<ComposingElement>();

	private Boolean isCrossLink;
	private Float crossLinkDegree;
	private String initiator;
	
	public PolymerComposition() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Float getCrossLinkDegree() {
		return crossLinkDegree;
	}

	public void setCrossLinkDegree(Float crossLinkDegree) {
		this.crossLinkDegree = crossLinkDegree;
	}

	public String getInitiator() {
		return initiator;
	}

	public void setInitiator(String initiator) {
		this.initiator = initiator;
	}

	public Boolean getIsCrossLink() {
		return isCrossLink;
	}

	public void setIsCrossLink(Boolean isCrossLink) {
		this.isCrossLink = isCrossLink;
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
}

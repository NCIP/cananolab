package gov.nih.nci.calab.domain.nano.characterization.physical.composition;

import gov.nih.nci.calab.domain.nano.particle.Nanoparticle;

import java.util.Collection;
import java.util.HashSet;

public class EmulsionComposition implements Composition {
	private Long id;
	private String source;
	private String classification;
	private Collection<Nanoparticle> particleCollection;
	private Collection<ComposingElement> composingElementCollection = new HashSet<ComposingElement>();
	
	private String type;
	private String molecularFormula;
	private Float dropletSize;
	
	public EmulsionComposition() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Float getDropletSize() {
		return dropletSize;
	}

	public void setDropletSize(Float dropletSize) {
		this.dropletSize = dropletSize;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMolecularFormula() {
		return molecularFormula;
	}

	public void setMolecularFormula(String molecularFormula) {
		this.molecularFormula = molecularFormula;
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

package gov.nih.nci.calab.domain.nano.characterization.physical;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.particle.Nanoparticle;

import java.util.Collection;

public class Size implements Characterization {
	private Long id;
	private String source;
	private String classification;
	private Collection<Nanoparticle> particleCollection;
	
	private String size;
	private String sizeDistribution;
	private String radiusMoments;
	private String polydispersityIndex;

	public Size() {
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

	public void setParticleCollection(Collection<Nanoparticle> particles) {
		this.particleCollection = particles;
	}

	public Collection<Nanoparticle> getParticleCollection() {
		return this.particleCollection;
	}

	public String getPolydispersityIndex() {
		return polydispersityIndex;
	}

	public void setPolydispersityIndex(String polydispersityIndex) {
		this.polydispersityIndex = polydispersityIndex;
	}

	public String getRadiusMoments() {
		return radiusMoments;
	}

	public void setRadiusMoments(String radiusMoments) {
		this.radiusMoments = radiusMoments;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getSizeDistribution() {
		return sizeDistribution;
	}

	public void setSizeDistribution(String sizeDistribution) {
		this.sizeDistribution = sizeDistribution;
	}

}

package gov.nih.nci.calab.domain.nano.characterization.physical;

import gov.nih.nci.calab.domain.nano.characterization.BaseCharacterization;
import gov.nih.nci.calab.domain.nano.particle.BaseNanoParticle;

import java.util.Collection;

public class Size implements BaseCharacterization {
	private Long id;
	private String origin;
	private String characterizationCategory;
	private String size;
	private String sizeDistribution;
	private String radiusMoments;
	private String polydispersityIndex;
	private Collection<BaseNanoParticle> particleCollection;
	
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

	public void setOrigin(String origin) {
		this.origin = origin;

	}

	public String getOrigin() {
		return this.origin;
	}

	public void setCharacterizationCategory(String category) {
		this.characterizationCategory = category;
	}

	public String getCharacterizationCategory() {
		return this.characterizationCategory;
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

	public Collection<BaseNanoParticle> getParticleCollection() {
		return particleCollection;
	}

	public void setParticleCollection(
			Collection<BaseNanoParticle> particleCollection) {
		this.particleCollection = particleCollection;
	}

}

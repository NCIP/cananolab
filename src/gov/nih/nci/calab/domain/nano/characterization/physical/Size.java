package gov.nih.nci.calab.domain.nano.characterization.physical;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.particle.Nanoparticle;
import gov.nih.nci.calab.service.util.CananoConstants;

import java.util.Collection;
import java.util.Date;

public class Size implements Characterization {
	private Long id;
	private String source;
	private String classification;
	private String description;
	private String identificationName;
	private String name;
	private String createdBy;
	private Date createdDate;
	private Collection<Nanoparticle> nanoparticleCollection;
	
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
		return CananoConstants.PHYSICAL_SIZE;
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

}

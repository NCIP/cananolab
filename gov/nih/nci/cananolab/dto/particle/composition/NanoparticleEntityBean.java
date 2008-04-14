package gov.nih.nci.cananolab.dto.particle.composition;

import gov.nih.nci.cananolab.domain.common.LabFile;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.Biopolymer;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.CarbonNanotube;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.ComposingElement;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.Dendrimer;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.Emulsion;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.Fullerene;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.Liposome;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.NanoparticleEntity;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.OtherNanoparticleEntity;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.Polymer;

import java.util.ArrayList;
import java.util.List;

public class NanoparticleEntityBean {
	private String entityType;

	private String description;
	
	private String entityClassName;

	private String entityId;

	private Polymer polymer = new Polymer();

	private Biopolymer biopolymer = new Biopolymer();

	private Dendrimer dendrimer = new Dendrimer();

	private CarbonNanotube carbonNanotube = new CarbonNanotube();

	private Liposome liposome = new Liposome();

	private Emulsion emulsion = new Emulsion();

	private Fullerene fullerene = new Fullerene();

	private OtherNanoparticleEntity otherEntity = new OtherNanoparticleEntity();

	private List<ComposingElement> composingElements = new ArrayList<ComposingElement>();

	private List<LabFile> files = new ArrayList<LabFile>();

	public NanoparticleEntityBean() {
	}

	public NanoparticleEntityBean(NanoparticleEntity nanoparticleEntity) {
		entityClassName = nanoparticleEntity.getClass().getCanonicalName();
		if (nanoparticleEntity instanceof Dendrimer) {
			dendrimer = (Dendrimer) nanoparticleEntity;
		} else if (nanoparticleEntity instanceof CarbonNanotube) {
			carbonNanotube = (CarbonNanotube) nanoparticleEntity;
		} else if (nanoparticleEntity instanceof Emulsion) {
			emulsion = (Emulsion) nanoparticleEntity;
		} else if (nanoparticleEntity instanceof Fullerene) {
			fullerene = (Fullerene) nanoparticleEntity;
		} else if (nanoparticleEntity instanceof Biopolymer) {
			biopolymer = (Biopolymer) nanoparticleEntity;
		} else if (nanoparticleEntity instanceof OtherNanoparticleEntity) {
			otherEntity = (OtherNanoparticleEntity) nanoparticleEntity;
		}
		entityId = nanoparticleEntity.getId().toString();
	}

	public int compareTo(NanoparticleEntityBean other) {
		return entityType.compareTo(other.getEntityType());
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getEntityClassName() {
		return entityClassName;
	}

	public Dendrimer getDendrimer() {
		return dendrimer;
	}

	public void setDendrimer(Dendrimer dendrimer) {
		this.dendrimer = dendrimer;
		dendrimer.setComposingElementCollection(composingElements);
		dendrimer.setLabFileCollection(files);
	}

	public Biopolymer getBiopolymer() {
		return biopolymer;
	}

	public void setBiopolymer(Biopolymer biopolymer) {
		this.biopolymer = biopolymer;
		biopolymer.setComposingElementCollection(composingElements);
		biopolymer.setLabFileCollection(files);
		biopolymer.setDescription(description);
	}

	public CarbonNanotube getCarbonNanotube() {
		return carbonNanotube;
	}

	public void setCarbonNanotube(CarbonNanotube carbonNanotube) {
		this.carbonNanotube = carbonNanotube;
		carbonNanotube.setComposingElementCollection(composingElements);
		carbonNanotube.setLabFileCollection(files);
		carbonNanotube.setDescription(description);
	}

	public List<ComposingElement> getComposingElements() {
		return composingElements;
	}

	public void setComposingElements(List<ComposingElement> composingElements) {
		this.composingElements = composingElements;
	}

	public Emulsion getEmulsion() {
		return emulsion;
	}

	public void setEmulsion(Emulsion emulsion) {
		this.emulsion = emulsion;
		emulsion.setComposingElementCollection(composingElements);
		emulsion.setLabFileCollection(files);
		emulsion.setDescription(description);
	}

	public List<LabFile> getFiles() {
		return files;
	}

	public void setFiles(List<LabFile> files) {
		this.files = files;
	}

	public Fullerene getFullerene() {
		return fullerene;
	}

	public void setFullerene(Fullerene fullerene) {
		this.fullerene = fullerene;
		fullerene.setComposingElementCollection(composingElements);
		fullerene.setLabFileCollection(files);
		fullerene.setDescription(description);
	}

	public Liposome getLiposome() {
		return liposome;
	}

	public void setLiposome(Liposome liposome) {
		this.liposome = liposome;
		liposome.setComposingElementCollection(composingElements);
		liposome.setLabFileCollection(files);
		liposome.setDescription(description);
	}

	public Polymer getPolymer() {
		return polymer;
	}

	public void setPolymer(Polymer polymer) {
		this.polymer = polymer;
		polymer.setComposingElementCollection(composingElements);
		polymer.setLabFileCollection(files);
		polymer.setDescription(description);
	}

	public OtherNanoparticleEntity getOtherEntity() {
		return otherEntity;
	}

	public void setOtherEntity(OtherNanoparticleEntity otherEntity) {
		this.otherEntity = otherEntity;
		otherEntity.setType(entityType);
		otherEntity.setComposingElementCollection(composingElements);
		otherEntity.setLabFileCollection(files);
		otherEntity.setDescription(description);
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public void setEntityClassName(String entityClassName) {
		this.entityClassName = entityClassName;
	}

	public String getDescription() {
		return description;
	}

	public void setEntityDescription(String description) {
		this.description = description;
	}
}

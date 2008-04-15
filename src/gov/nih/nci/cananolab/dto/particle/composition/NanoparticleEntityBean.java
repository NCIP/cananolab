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
import gov.nih.nci.cananolab.util.ClassUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents the view bean for the NanoparticleEntity domain object
 * 
 * @author pansu
 *
 */
public class NanoparticleEntityBean {
	private String type;

	private String description;

	private String className;

	private Polymer polymer = new Polymer();

	private Biopolymer biopolymer = new Biopolymer();

	private Dendrimer dendrimer = new Dendrimer();

	private CarbonNanotube carbonNanotube = new CarbonNanotube();

	private Liposome liposome = new Liposome();

	private Emulsion emulsion = new Emulsion();

	private Fullerene fullerene = new Fullerene();

	private OtherNanoparticleEntity otherEntity = new OtherNanoparticleEntity();

	private List<ComposingElementBean> composingElements = new ArrayList<ComposingElementBean>();

	private Set<ComposingElement> domainComposingElements = new HashSet<ComposingElement>();

	private Set<LabFile> files = new HashSet<LabFile>();

	private NanoparticleEntity domainEntity=new NanoparticleEntity();

	public NanoparticleEntityBean() {
	}

	public NanoparticleEntityBean(NanoparticleEntity nanoparticleEntity) {
		className = ClassUtils.getShortClassName(nanoparticleEntity.getClass()
				.getCanonicalName());
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
		for (ComposingElement composingElement : nanoparticleEntity
				.getComposingElementCollection()) {
			composingElements.add(new ComposingElementBean(composingElement));
		}		
		domainEntity = nanoparticleEntity;
	}

	public int compareTo(NanoparticleEntityBean other) {
		return type.compareTo(other.getType());
	}

	public String getType() {
		return type;
	}

	public void setType(String entityType) {
		this.type = entityType;
	}

	public String getClassName() {
		return className;
	}

	public Dendrimer getDendrimer() {
		return dendrimer;
	}

	public void setDendrimer(Dendrimer dendrimer) {
		this.dendrimer = dendrimer;
		domainEntity = dendrimer;
	}

	public Biopolymer getBiopolymer() {
		return biopolymer;
	}

	public void setBiopolymer(Biopolymer biopolymer) {
		this.biopolymer = biopolymer;
		domainEntity = biopolymer;
	}

	public CarbonNanotube getCarbonNanotube() {
		return carbonNanotube;
	}

	public void setCarbonNanotube(CarbonNanotube carbonNanotube) {
		this.carbonNanotube = carbonNanotube;
		domainEntity = carbonNanotube;
	}

	public List<ComposingElementBean> getComposingElements() {
		return composingElements;
	}

	public void setComposingElements(
			List<ComposingElementBean> composingElements) {
		this.composingElements = composingElements;
		for (ComposingElementBean composingElementBean : composingElements) {
			ComposingElement domainComposingElement = composingElementBean
					.getDomainComposingElement();
			domainComposingElement.setNanoparticleEntity(domainEntity);
			domainComposingElements.add(domainComposingElement);
		}
		domainEntity.setComposingElementCollection(domainComposingElements);
	}

	public Emulsion getEmulsion() {
		return emulsion;
	}

	public void setEmulsion(Emulsion emulsion) {
		this.emulsion = emulsion;
		domainEntity = emulsion;
	}

	public Set<LabFile> getFiles() {
		return files;
	}

	public void setFiles(Set<LabFile> files) {
		this.files = files;
		domainEntity.setLabFileCollection(files);
	}

	public Fullerene getFullerene() {
		return fullerene;
	}

	public void setFullerene(Fullerene fullerene) {
		this.fullerene = fullerene;
		domainEntity = fullerene;
	}

	public Liposome getLiposome() {
		return liposome;
	}

	public void setLiposome(Liposome liposome) {
		this.liposome = liposome;
		domainEntity = liposome;
	}

	public Polymer getPolymer() {
		return polymer;
	}

	public void setPolymer(Polymer polymer) {
		this.polymer = polymer;
		domainEntity = polymer;
	}

	public OtherNanoparticleEntity getOtherEntity() {
		return otherEntity;
	}

	public void setOtherEntity(OtherNanoparticleEntity otherEntity) {
		this.otherEntity = otherEntity;
		otherEntity.setType(type);
		domainEntity = otherEntity;
	}

	public void setClassName(String entityClassName) {
		this.className = entityClassName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		domainEntity.setDescription(description);
	}

	public NanoparticleEntity getDomainEntity() {
		return domainEntity;
	}
}

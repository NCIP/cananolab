package gov.nih.nci.cananolab.dto.particle.composition;

import gov.nih.nci.cananolab.domain.common.LabFile;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.Biopolymer;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.CarbonNanotube;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.ComposingElement;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.Dendrimer;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.Emulsion;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.Fullerene;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.Liposome;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.MetalParticle;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.NanoparticleEntity;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.OtherNanoparticleEntity;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.Polymer;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.QuantumDot;
import gov.nih.nci.cananolab.util.ClassUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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

	private MetalParticle metalParticle = new MetalParticle();

	private QuantumDot quantumDot = new QuantumDot();

	private OtherNanoparticleEntity otherEntity = new OtherNanoparticleEntity();

	private List<ComposingElementBean> composingElements = new ArrayList<ComposingElementBean>();

	private List<LabFile> files = new ArrayList<LabFile>();

	private NanoparticleEntity domainEntity = new NanoparticleEntity();

	public NanoparticleEntityBean() {
	}

	public NanoparticleEntityBean(NanoparticleEntity nanoparticleEntity) {
		description = nanoparticleEntity.getDescription();
		if (nanoparticleEntity instanceof Dendrimer) {
			dendrimer = (Dendrimer) nanoparticleEntity;
			domainEntity = dendrimer;
			className = ClassUtils.getShortClassName(Dendrimer.class.getName());
		} else if (nanoparticleEntity instanceof CarbonNanotube) {
			carbonNanotube = (CarbonNanotube) nanoparticleEntity;
			domainEntity = carbonNanotube;
			className = ClassUtils.getShortClassName(CarbonNanotube.class
					.getName());
		} else if (nanoparticleEntity instanceof Emulsion) {
			emulsion = (Emulsion) nanoparticleEntity;
			domainEntity = emulsion;
			className = ClassUtils.getShortClassName(Emulsion.class.getName());
		} else if (nanoparticleEntity instanceof Fullerene) {
			fullerene = (Fullerene) nanoparticleEntity;
			domainEntity = fullerene;
			className = ClassUtils.getShortClassName(Fullerene.class.getName());
		} else if (nanoparticleEntity instanceof Biopolymer) {
			biopolymer = (Biopolymer) nanoparticleEntity;
			domainEntity = biopolymer;
			className = ClassUtils
					.getShortClassName(Biopolymer.class.getName());
		} else if (nanoparticleEntity instanceof QuantumDot) {
			quantumDot = (QuantumDot) nanoparticleEntity;
			domainEntity = quantumDot;
			className = ClassUtils
					.getShortClassName(QuantumDot.class.getName());
		} else if (nanoparticleEntity instanceof MetalParticle) {
			metalParticle = (MetalParticle) nanoparticleEntity;
			domainEntity = metalParticle;
			className = ClassUtils.getShortClassName(MetalParticle.class
					.getName());
		} else if (nanoparticleEntity instanceof OtherNanoparticleEntity) {
			otherEntity = (OtherNanoparticleEntity) nanoparticleEntity;
			domainEntity = otherEntity;
			className = ClassUtils
					.getShortClassName(OtherNanoparticleEntity.class.getName());
		}
		for (ComposingElement composingElement : nanoparticleEntity
				.getComposingElementCollection()) {
			composingElements.add(new ComposingElementBean(composingElement));
		}
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
		domainEntity = dendrimer;
		setSharedInfo();
		return dendrimer;
	}

	public Biopolymer getBiopolymer() {
		domainEntity = biopolymer;
		setSharedInfo();
		return biopolymer;
	}

	public CarbonNanotube getCarbonNanotube() {
		domainEntity = carbonNanotube;
		setSharedInfo();
		return carbonNanotube;
	}

	public QuantumDot getQuantumDot() {
		domainEntity = quantumDot;
		metalParticle.setDescription(description);
		setSharedInfo();
		return quantumDot;
	}

	public MetalParticle getMetalParticle() {
		domainEntity = metalParticle;		
		setSharedInfo();
		return metalParticle;
	}

	public MetalParticle getOtherNanoparticle() {
		domainEntity = otherEntity;
		((OtherNanoparticleEntity) domainEntity).setType(type);
		metalParticle.setDescription(description);
		setSharedInfo();
		return metalParticle;
	}

	public List<ComposingElementBean> getComposingElements() {
		return composingElements;
	}

	public Emulsion getEmulsion() {
		domainEntity = emulsion;
		setSharedInfo();
		return emulsion;
	}

	public List<LabFile> getFiles() {
		return files;
	}

	public Fullerene getFullerene() {
		domainEntity = fullerene;
		setSharedInfo();
		return fullerene;
	}

	public Liposome getLiposome() {
		domainEntity = liposome;
		setSharedInfo();
		return liposome;
	}

	public Polymer getPolymer() {
		domainEntity = polymer;
		setSharedInfo();
		return polymer;
	}

	public void setClassName(String entityClassName) {
		this.className = entityClassName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;

	}

	public NanoparticleEntity getDomainEntity() {
		return domainEntity;
	}

	public void setSharedInfo() {
		domainEntity.setDescription(description);		
		if (domainEntity.getComposingElementCollection() != null) {
			domainEntity.getComposingElementCollection().clear();
		} else {
			domainEntity
					.setComposingElementCollection(new HashSet<ComposingElement>());
		}
		for (ComposingElementBean composingElementBean : composingElements) {
			ComposingElement domainComposingElement = composingElementBean
					.getDomainComposingElement();
			domainComposingElement.setNanoparticleEntity(domainEntity);
			domainEntity.getComposingElementCollection().add(
					domainComposingElement);
		}
		if (domainEntity.getLabFileCollection() != null) {
			domainEntity.getLabFileCollection().clear();
		} else {
			domainEntity.setLabFileCollection(new HashSet<LabFile>());
		}
		for (LabFile file : files) {
			domainEntity.getLabFileCollection().add(file);
		}
	}

	public void addComposingElement() {
		composingElements.add(new ComposingElementBean());
	}

	public void removeComposingElement(int ind) {
		ComposingElementBean elementToRemove = composingElements.get(ind);
		elementToRemove.getDomainComposingElement().setNanoparticleEntity(null);
		// remove the one at the index
		composingElements.remove(elementToRemove);
	}

	public NanoparticleEntityBean copy() {
		NanoparticleEntityBean copiedEntity = new NanoparticleEntityBean();
		copiedEntity.setType(type);
		copiedEntity.setDescription(description);
		copiedEntity.setClassName(className);
		return copiedEntity;
	}
}

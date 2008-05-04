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
import gov.nih.nci.cananolab.dto.common.LabFileBean;
import gov.nih.nci.cananolab.util.ClassUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

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

	private List<ComposingElementBean> composingElements = new ArrayList<ComposingElementBean>();

	private List<LabFileBean> files = new ArrayList<LabFileBean>();

	private NanoparticleEntity domainEntity;

	public NanoparticleEntityBean() {
	}

	public NanoparticleEntityBean(NanoparticleEntity nanoparticleEntity) {
		description = nanoparticleEntity.getDescription();
		domainEntity = nanoparticleEntity;
		if (domainEntity instanceof Biopolymer) {
			biopolymer = (Biopolymer) domainEntity;
		} else if (domainEntity instanceof Dendrimer) {
			dendrimer = (Dendrimer) domainEntity;
		} else if (domainEntity instanceof CarbonNanotube) {
			carbonNanotube = (CarbonNanotube) domainEntity;
		} else if (domainEntity instanceof Liposome) {
			liposome = (Liposome) domainEntity;
		} else if (domainEntity instanceof Emulsion) {
			emulsion = (Emulsion) domainEntity;
		} else if (domainEntity instanceof Fullerene) {
			fullerene = (Fullerene) domainEntity;
		}
		className = ClassUtils.getShortClassName(nanoparticleEntity.getClass()
				.getName());
		for (ComposingElement composingElement : nanoparticleEntity
				.getComposingElementCollection()) {
			composingElements.add(new ComposingElementBean(composingElement));
		}
	}

	public String getType() {
		return type;
	}

	public String getClassName() {
		return className;
	}

	public Dendrimer getDendrimer() {
		domainEntity = dendrimer;
		return dendrimer;
	}

	public Biopolymer getBiopolymer() {
		domainEntity = biopolymer;
		return biopolymer;
	}

	public CarbonNanotube getCarbonNanotube() {
		domainEntity = carbonNanotube;
		return carbonNanotube;
	}

	public List<ComposingElementBean> getComposingElements() {
		return composingElements;
	}

	public Emulsion getEmulsion() {
		domainEntity = emulsion;
		return emulsion;
	}

	public List<LabFileBean> getFiles() {
		return files;
	}

	public Fullerene getFullerene() {
		domainEntity = fullerene;
		return fullerene;
	}

	public Liposome getLiposome() {
		domainEntity = liposome;
		return liposome;
	}

	public Polymer getPolymer() {
		domainEntity = polymer;
		return polymer;
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

	public void setupDomainEntity(Map<String, String> typeToClass,
			String createdBy) throws Exception {
		// take care of nanoparticle entities that don't have any special
		// properties shown in the form, e.g. Metal Particle
		className = typeToClass.get(type);
		if (domainEntity == null) {
			Class clazz = ClassUtils.getFullClass(className);
			domainEntity = (NanoparticleEntity) clazz.newInstance();
		}
		if (domainEntity.getId() == null) {
			domainEntity.setCreatedBy(createdBy);
			domainEntity.setCreatedDate(new Date());
		}
		domainEntity.setDescription(description);
		if (domainEntity instanceof OtherNanoparticleEntity) {
			((OtherNanoparticleEntity) domainEntity).setType(type);
		}
		if (domainEntity.getComposingElementCollection() != null) {
			domainEntity.getComposingElementCollection().clear();
		} else {
			domainEntity
					.setComposingElementCollection(new HashSet<ComposingElement>());
		}
		for (ComposingElementBean composingElementBean : composingElements) {
			composingElementBean.setupDomainComposingElement(typeToClass,
					createdBy);
			ComposingElement domainComposingElement = composingElementBean
					.getDomainComposingElement();
			if (domainComposingElement.getId() == null) {
				domainComposingElement.setCreatedBy(createdBy);
				domainComposingElement.setCreatedDate(new Date());
			}
			domainComposingElement.setNanoparticleEntity(domainEntity);
			domainEntity.getComposingElementCollection().add(
					domainComposingElement);
		}
		if (domainEntity.getLabFileCollection() != null) {
			domainEntity.getLabFileCollection().clear();
		} else {
			domainEntity.setLabFileCollection(new HashSet<LabFile>());
		}
		for (LabFileBean file : files) {
			file.getDomainFile().setCreatedBy(createdBy);
			file.getDomainFile().setCreatedDate(new Date());
			domainEntity.getLabFileCollection().add(file.getDomainFile());
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

	public void addFile() {
		files.add(new LabFileBean());
	}

	public void removeFile(int ind) {
		files.remove(ind);
	}

	public NanoparticleEntityBean copy() {
		NanoparticleEntityBean copiedEntity = new NanoparticleEntityBean();
		copiedEntity.type = type;
		copiedEntity.setDescription(description);
		copiedEntity.className = className;
		return copiedEntity;
	}

	public void updateType(Map<String, String> classToType) {
		if (domainEntity instanceof OtherNanoparticleEntity) {
			type = ((OtherNanoparticleEntity) domainEntity).getType();
		} else {
			type = classToType.get(className);
		}
		// set composing element function type
		for (ComposingElementBean compElementBean : getComposingElements()) {
			for (FunctionBean functionBean : compElementBean
					.getInherentFunctions()) {
				functionBean.updateType(classToType);
			}
		}
	}

	public void setType(String type) {
		this.type = type;
	}

}

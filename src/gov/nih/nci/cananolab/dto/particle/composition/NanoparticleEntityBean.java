package gov.nih.nci.cananolab.dto.particle.composition;

import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.function.TargetingFunction;
import gov.nih.nci.cananolab.domain.nanomaterial.Biopolymer;
import gov.nih.nci.cananolab.domain.nanomaterial.CarbonNanotube;
import gov.nih.nci.cananolab.domain.nanomaterial.Dendrimer;
import gov.nih.nci.cananolab.domain.nanomaterial.Emulsion;
import gov.nih.nci.cananolab.domain.nanomaterial.Fullerene;
import gov.nih.nci.cananolab.domain.nanomaterial.Liposome;
import gov.nih.nci.cananolab.domain.nanomaterial.OtherNanoparticleEntity;
import gov.nih.nci.cananolab.domain.nanomaterial.Polymer;
import gov.nih.nci.cananolab.domain.particle.ComposingElement;
import gov.nih.nci.cananolab.domain.particle.Function;
import gov.nih.nci.cananolab.domain.particle.NanoparticleEntity;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.cananolab.util.Comparators;
import gov.nih.nci.cananolab.util.Constants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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

	private List<FileBean> files = new ArrayList<FileBean>();

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
		} else if (domainEntity instanceof Polymer) {
			polymer = (Polymer) domainEntity;
		} else if (domainEntity instanceof Fullerene) {
			fullerene = (Fullerene) domainEntity;
		}
		className = ClassUtils.getShortClassName(nanoparticleEntity.getClass()
				.getName());
		for (ComposingElement composingElement : nanoparticleEntity
				.getComposingElementCollection()) {
			composingElements.add(new ComposingElementBean(composingElement));
		}
		Collections.sort(composingElements,
				new Comparators.ComposingElementBeanDateComparator());
		if (nanoparticleEntity.getFileCollection()!=null){
			for (File file : nanoparticleEntity.getFileCollection()) {
				files.add(new FileBean(file));
			}
		}
		Collections.sort(files,
				new Comparators.FileBeanDateComparator());
	}

	public NanoparticleEntity getDomainCopy() {
		NanoparticleEntity copy = (NanoparticleEntity) ClassUtils.deepCopy(this
				.getDomainEntity());
		// clear Ids, reset createdBy and createdDate, add prefix to
		copy.setId(null);
		copy.setCreatedBy(Constants.AUTO_COPY_ANNOTATION_PREFIX);
		copy.setCreatedDate(new Date());
		if (copy.getComposingElementCollection().isEmpty()) {
			copy.setComposingElementCollection(null);
		} else {
			// have to create a new collection otherwise Hibernate complains
			Collection<ComposingElement> ces = copy
					.getComposingElementCollection();
			copy.setComposingElementCollection(new HashSet<ComposingElement>());
			copy.getComposingElementCollection().addAll(ces);
			for (ComposingElement ce : copy.getComposingElementCollection()) {
				ce.setId(null);
				ce.setCreatedBy(Constants.AUTO_COPY_ANNOTATION_PREFIX);
				ce.setCreatedDate(new Date());
				if (ce.getInherentFunctionCollection().isEmpty()) {
					ce.setInherentFunctionCollection(null);
				} else {
					Collection<Function> functions = ce
							.getInherentFunctionCollection();
					ce.setInherentFunctionCollection(new HashSet<Function>());
					ce.getInherentFunctionCollection().addAll(functions);
					for (Function function : ce.getInherentFunctionCollection()) {
						function.setId(null);
						function
								.setCreatedBy(Constants.AUTO_COPY_ANNOTATION_PREFIX);
						function.setCreatedDate(new Date());
						if (function instanceof TargetingFunction) {
							((TargetingFunction) function)
									.setTargetCollection(null);
						}
					}
				}
			}
		}
		if (copy.getFileCollection().isEmpty()) {
			copy.setFileCollection(null);
		} else {
			Collection<File> files = copy.getFileCollection();
			copy.setFileCollection(new HashSet<File>());
			copy.getFileCollection().addAll(files);
			for (File file : copy.getFileCollection()) {
				file.setId(null);
				file
						.setCreatedBy(Constants.AUTO_COPY_ANNOTATION_PREFIX);
				file.setCreatedDate(new Date());
			}
		}
		return copy;
	}

	public String getType() {
		return type;
	}

	public String getClassName() {
		return className;
	}

	public Dendrimer getDendrimer() {
		return dendrimer;
	}

	public Biopolymer getBiopolymer() {
		return biopolymer;
	}

	public CarbonNanotube getCarbonNanotube() {
		return carbonNanotube;
	}

	public List<ComposingElementBean> getComposingElements() {
		return composingElements;
	}

	public Emulsion getEmulsion() {
		return emulsion;
	}

	public List<FileBean> getFiles() {
		return files;
	}

	public Fullerene getFullerene() {
		return fullerene;
	}

	public Liposome getLiposome() {
		return liposome;
	}

	public Polymer getPolymer() {
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
			String createdBy, String internalUriPath) throws Exception {
		className = typeToClass.get(type.toLowerCase());
		Class clazz = null;
		if (className == null) {
			clazz = OtherNanoparticleEntity.class;
		} else {
			clazz = ClassUtils.getFullClass(className);
		}
		if (domainEntity == null) {
			domainEntity = (NanoparticleEntity) clazz.newInstance();
		}
		if (domainEntity instanceof OtherNanoparticleEntity) {
			((OtherNanoparticleEntity) domainEntity).setType(type);
		} else if (domainEntity instanceof Biopolymer) {
			domainEntity = biopolymer;
		} else if (domainEntity instanceof Dendrimer) {
			domainEntity = dendrimer;
		} else if (domainEntity instanceof CarbonNanotube) {
			domainEntity = carbonNanotube;
		} else if (domainEntity instanceof Liposome) {
			domainEntity = liposome;
		} else if (domainEntity instanceof Emulsion) {
			domainEntity = emulsion;
		} else if (domainEntity instanceof Polymer) {
			domainEntity = polymer;
		} else if (domainEntity instanceof Fullerene) {
			domainEntity = fullerene;
		}
		if (domainEntity.getId() == null
				|| domainEntity.getCreatedBy() != null
				&& domainEntity.getCreatedBy().equals(
						Constants.AUTO_COPY_ANNOTATION_PREFIX)) {
			domainEntity.setCreatedBy(createdBy);
			domainEntity.setCreatedDate(new Date());
		}
		domainEntity.setDescription(description);
		if (domainEntity.getComposingElementCollection() != null) {
			domainEntity.getComposingElementCollection().clear();
		} else {
			domainEntity
					.setComposingElementCollection(new HashSet<ComposingElement>());
		}
		int i=0;
		for (ComposingElementBean composingElementBean : composingElements) {
			composingElementBean.setupDomainComposingElement(typeToClass,
					createdBy, i);
			ComposingElement domainComposingElement = composingElementBean
					.getDomainComposingElement();
			if (domainComposingElement.getId() == null) {
				domainComposingElement.setCreatedBy(createdBy);
				domainComposingElement.setCreatedDate(new Date());
			}
			domainComposingElement.setNanoparticleEntity(domainEntity);
			domainEntity.getComposingElementCollection().add(
					domainComposingElement);
			i++;
		}
		if (domainEntity.getFileCollection() != null) {
			domainEntity.getFileCollection().clear();
		} else {
			domainEntity.setFileCollection(new HashSet<File>());
		}
		int j=0;
		for (FileBean file : files) {
			file.setupDomainFile(internalUriPath, createdBy, j);
			domainEntity.getFileCollection().add(file.getDomainFile());
			j++;
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
		files.add(new FileBean());
	}

	public void removeFile(int ind) {
		files.remove(ind);
	}

	public NanoparticleEntityBean copy() throws Exception {
		NanoparticleEntityBean copiedEntity = (NanoparticleEntityBean) ClassUtils
				.deepCopy(this);
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

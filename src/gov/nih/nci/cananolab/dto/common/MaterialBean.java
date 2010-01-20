package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.common.Function;
import gov.nih.nci.cananolab.domain.common.Keyword;
import gov.nih.nci.cananolab.domain.common.Material;
import gov.nih.nci.cananolab.domain.common.MaterialComponent;
import gov.nih.nci.cananolab.domain.function.Target;
import gov.nih.nci.cananolab.domain.function.TargetingFunction;
import gov.nih.nci.cananolab.domain.material.Antibody;
import gov.nih.nci.cananolab.domain.material.Biopolymer;
import gov.nih.nci.cananolab.domain.material.CarbonNanotube;
import gov.nih.nci.cananolab.domain.material.Dendrimer;
import gov.nih.nci.cananolab.domain.material.Emulsion;
import gov.nih.nci.cananolab.domain.material.Fullerene;
import gov.nih.nci.cananolab.domain.material.Liposome;
import gov.nih.nci.cananolab.domain.material.OtherMaterial;
import gov.nih.nci.cananolab.domain.material.Polymer;
import gov.nih.nci.cananolab.domain.material.SmallMolecule;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class MaterialBean {
	private String type;

	private String className;

	private Material domain = new Material();

	protected FileBean theFile = new FileBean();

	protected List<FileBean> files = new ArrayList<FileBean>();

	private MaterialComponentBean theMaterialComponent = new MaterialComponentBean();

	private List<MaterialComponentBean> materialComponents = new ArrayList<MaterialComponentBean>();

	protected String domainId, displayName; // used for DWR ajax in
	// bodySubmitChemicalAssociation.jsp

	private Polymer polymer = new Polymer();

	private Dendrimer dendrimer = new Dendrimer();

	private CarbonNanotube carbonNanotube = new CarbonNanotube();

	private Liposome liposome = new Liposome();

	private Emulsion emulsion = new Emulsion();

	private Fullerene fullerene = new Fullerene();

	private SmallMolecule smallMolecule = new SmallMolecule();

	private Antibody antibody = new Antibody();

	private Biopolymer biopolymer = new Biopolymer();

	private boolean withProperties = false;

	public MaterialBean() {
	}

	public MaterialBean(Material material) {
		domain = material;
		if (domain instanceof Biopolymer) {
			biopolymer = (Biopolymer) domain;
			withProperties = true;
		} else if (domain instanceof Dendrimer) {
			dendrimer = (Dendrimer) domain;
			withProperties = true;
		} else if (domain instanceof CarbonNanotube) {
			carbonNanotube = (CarbonNanotube) domain;
			withProperties = true;
		} else if (domain instanceof Liposome) {
			liposome = (Liposome) domain;
			withProperties = true;
		} else if (domain instanceof Emulsion) {
			emulsion = (Emulsion) domain;
			withProperties = true;
		} else if (domain instanceof Polymer) {
			polymer = (Polymer) domain;
			withProperties = true;
		} else if (domain instanceof Fullerene) {
			fullerene = (Fullerene) domain;
			withProperties = true;
		} else if (domain instanceof Antibody) {
			antibody = (Antibody) domain;
			withProperties = true;
		} else if (domain instanceof SmallMolecule) {
			smallMolecule = (SmallMolecule) domain;
			withProperties = true;
		}
	}

	public void addFile(FileBean file) {
		// if an old one exists, remove it first
		int index = files.indexOf(file);
		if (index != -1) {
			files.remove(file);
			// retain the original order
			files.add(index, file);
		} else {
			files.add(file);
		}
	}

	public void removeFile(FileBean file) {
		files.remove(file);
	}

	public FileBean getTheFile() {
		return theFile;
	}

	public void setTheFile(FileBean theFile) {
		this.theFile = theFile;
	}

	public List<FileBean> getFiles() {
		return files;
	}

	public void setFiles(List<FileBean> files) {
		this.files = files;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getDomainId() {
		return domainId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public MaterialComponentBean getTheMaterialComponent() {
		return theMaterialComponent;
	}

	public void setTheMaterialComponent(
			MaterialComponentBean theMaterialComponent) {
		this.theMaterialComponent = theMaterialComponent;
	}

	public List<MaterialComponentBean> getMaterialCompoments() {
		return materialComponents;
	}

	public void setMaterialCompoments(
			List<MaterialComponentBean> materialCompoments) {
		this.materialComponents = materialCompoments;
	}

	public boolean isWithProperties() {
		return withProperties;
	}

	public void setWithProperties(boolean withProperties) {
		this.withProperties = withProperties;
	}

	public Material getDomain() {
		return domain;
	}

	public Polymer getPolymer() {
		return polymer;
	}

	public Dendrimer getDendrimer() {
		return dendrimer;
	}

	public CarbonNanotube getCarbonNanotube() {
		return carbonNanotube;
	}

	public Liposome getLiposome() {
		return liposome;
	}

	public Emulsion getEmulsion() {
		return emulsion;
	}

	public Fullerene getFullerene() {
		return fullerene;
	}

	public SmallMolecule getSmallMolecule() {
		return smallMolecule;
	}

	public Antibody getAntibody() {
		return antibody;
	}

	public Biopolymer getBiopolymer() {
		return biopolymer;
	}

	public String getType() {
		return type;
	}

	public Material getDomainCopy() {
		Material copy = (Material) ClassUtils.deepCopy(this.getDomain());
		// clear Ids, reset createdBy and createdDate, add prefix to
		copy.setId(null);
		copy.setCreatedBy(Constants.AUTO_COPY_ANNOTATION_PREFIX);
		if (copy.getMaterialComponentCollection() == null
				|| copy.getMaterialComponentCollection().isEmpty()) {
			copy.setMaterialComponentCollection(null);
		} else {
			// have to create a new collection otherwise Hibernate complains
			Collection<MaterialComponent> ces = copy
					.getMaterialComponentCollection();
			copy
					.setMaterialComponentCollection(new HashSet<MaterialComponent>());
			copy.getMaterialComponentCollection().addAll(ces);
			for (MaterialComponent mc : copy.getMaterialComponentCollection()) {
				mc.setId(null);
				mc.setCreatedBy(Constants.AUTO_COPY_ANNOTATION_PREFIX);
				if (mc.getFunctionCollection().isEmpty()) {
					mc.setFunctionCollection(null);
				} else {
					Collection<Function> functions = mc.getFunctionCollection();
					mc.setFunctionCollection(new HashSet<Function>());
					mc.getFunctionCollection().addAll(functions);
					for (Function function : mc.getFunctionCollection()) {
						function.setId(null);
						function
								.setCreatedBy(Constants.AUTO_COPY_ANNOTATION_PREFIX);
						if (function instanceof TargetingFunction) {
							((TargetingFunction) function)
									.setTargetCollection(null);
							if (((TargetingFunction) function)
									.getTargetCollection() == null
									|| ((TargetingFunction) function)
											.getTargetCollection().isEmpty()) {
								((TargetingFunction) function)
										.setTargetCollection(null);
							} else {
								Collection<Target> targets = ((TargetingFunction) function)
										.getTargetCollection();
								((TargetingFunction) function)
										.setTargetCollection(new HashSet<Target>());
								((TargetingFunction) function)
										.getTargetCollection().addAll(targets);
								for (Target target : ((TargetingFunction) function)
										.getTargetCollection()) {
									target.setId(null);
									target
											.setCreatedBy(Constants.AUTO_COPY_ANNOTATION_PREFIX);
								}
							}
						}
					}
				}
			}
		}
		if (copy.getFileCollection() == null
				|| copy.getFileCollection().isEmpty()) {
			copy.setFileCollection(null);
		} else {
			Collection<File> files = copy.getFileCollection();
			copy.setFileCollection(new HashSet<File>());
			copy.getFileCollection().addAll(files);
			for (File file : copy.getFileCollection()) {
				file.setId(null);
				file.setCreatedBy(Constants.AUTO_COPY_ANNOTATION_PREFIX);
				if (file.getKeywordCollection().isEmpty()) {
					file.setKeywordCollection(null);
				} else {
					Collection<Keyword> keywords = file.getKeywordCollection();
					file.setKeywordCollection(new HashSet<Keyword>());
					file.getKeywordCollection().addAll(keywords);
					for (Keyword keyword : file.getKeywordCollection()) {
						keyword.setId(null);
					}
				}
			}
		}
		return copy;
	}

	public void setupDomain(String createdBy) throws Exception {
		className = ClassUtils.getShortClassNameFromDisplayName(type);
		Class clazz = ClassUtils.getFullClass(className);
		if (clazz == null) {
			clazz = OtherMaterial.class;
		}
		if (domain == null) {
			domain = (Material) clazz.newInstance();
		}
		if (domain instanceof OtherMaterial) {
			((OtherMaterial) domain).setType(type);
		} else if (domain instanceof Biopolymer) {
			domain = biopolymer;
		} else if (domain instanceof Dendrimer) {
			domain = dendrimer;
		} else if (domain instanceof CarbonNanotube) {
			domain = carbonNanotube;
		} else if (domain instanceof Liposome) {
			domain = liposome;
		} else if (domain instanceof Emulsion) {
			domain = emulsion;
		} else if (domain instanceof Polymer) {
			domain = polymer;
		} else if (domain instanceof Fullerene) {
			domain = fullerene;
		} else if (domain instanceof Antibody) {
			domain = antibody;
		} else if (domain instanceof SmallMolecule) {
			domain = smallMolecule;
		} else if (domain instanceof Biopolymer) {
			domain = biopolymer;
		}
		if (domain.getId() == null
				|| !StringUtils.isEmpty(domain.getCreatedBy())
				&& domain.getCreatedBy().equals(
						Constants.AUTO_COPY_ANNOTATION_PREFIX)) {
			domain.setCreatedBy(createdBy);
			domain.setCreatedDate(new Date());
		}
		if (domain.getMaterialComponentCollection() != null) {
			domain.getMaterialComponentCollection().clear();
		} else {
			domain
					.setMaterialComponentCollection(new HashSet<MaterialComponent>());
		}
		for (MaterialComponentBean MaterialComponentBean : materialComponents) {
			domain.getMaterialComponentCollection().add(
					MaterialComponentBean.getDomain());
		}
		if (files.isEmpty()) {
			domain.setFileCollection(null);
		} else if (domain.getFileCollection() != null) {
			domain.getFileCollection().clear();
		} else {
			domain.setFileCollection(new HashSet<File>());
		}
		for (FileBean file : files) {
			domain.getFileCollection().add(file.getDomainFile());
		}
	}
}

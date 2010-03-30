package gov.nih.nci.cananolab.dto.particle.composition;

import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.particle.ChemicalAssociation;
import gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity;
import gov.nih.nci.cananolab.domain.particle.NanomaterialEntity;
import gov.nih.nci.cananolab.domain.particle.SampleComposition;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.util.Comparators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class CompositionBean {
	public static final String NANOMATERIAL_SELECTION = "nanomaterial entity";
	public static final String FUNCTIONALIZING_SELECTION = "functionalizing entity";
	public static final String CHEMICAL_SELECTION = "chemical association";
	public static final String FILE_SELECTION = "composition file";
	public static final String[] ALL_COMPOSITION_SECTIONS = new String[] {
			NANOMATERIAL_SELECTION, FUNCTIONALIZING_SELECTION,
			CHEMICAL_SELECTION, FILE_SELECTION };

	private List<NanomaterialEntityBean> nanomaterialEntities = new ArrayList<NanomaterialEntityBean>();
	private List<FunctionalizingEntityBean> functionalizingEntities = new ArrayList<FunctionalizingEntityBean>();
	private List<ChemicalAssociationBean> chemicalAssociations = new ArrayList<ChemicalAssociationBean>();
	private List<FileBean> files = new ArrayList<FileBean>();
	private SampleComposition domain;
	private List<String> compositionSections = new ArrayList<String>();
	protected FileBean theFile = new FileBean();
	private Map<String, SortedSet<NanomaterialEntityBean>> type2NanoEntities = new HashMap<String, SortedSet<NanomaterialEntityBean>>();
	private Map<String, SortedSet<FunctionalizingEntityBean>> type2FuncEntities = new HashMap<String, SortedSet<FunctionalizingEntityBean>>();
	private SortedSet<String> nanoEntityTypes = new TreeSet<String>();
	private SortedSet<String> funcEntityTypes = new TreeSet<String>();

	public CompositionBean() {

	}

	public CompositionBean(SampleComposition comp) {
		domain = comp;
		if (comp.getNanomaterialEntityCollection() != null) {
			for (NanomaterialEntity entity : comp
					.getNanomaterialEntityCollection()) {
				nanomaterialEntities.add(new NanomaterialEntityBean(entity));
			}
		}
		Collections.sort(nanomaterialEntities,
				new Comparators.NanomaterialEntityBeanTypeDateComparator());
		if (comp.getFunctionalizingEntityCollection() != null) {
			for (FunctionalizingEntity entity : comp
					.getFunctionalizingEntityCollection()) {
				functionalizingEntities.add(new FunctionalizingEntityBean(
						entity));
			}
		}
		Collections.sort(functionalizingEntities,
				new Comparators.FunctionalizingEntityBeanTypeDateComparator());
		if (comp.getChemicalAssociationCollection() != null) {
			for (ChemicalAssociation assoc : comp
					.getChemicalAssociationCollection()) {
				chemicalAssociations.add(new ChemicalAssociationBean(assoc));
			}
		}
		Collections.sort(chemicalAssociations,
				new Comparators.ChemicalAssociationBeanTypeDateComparator());
		if (comp.getFileCollection() != null) {
			for (File file : comp.getFileCollection()) {
				files.add(new FileBean(file));
			}
		}
		Collections.sort(files, new Comparators.FileBeanDateComparator());

		if (!nanomaterialEntities.isEmpty()) {
			compositionSections.add(NANOMATERIAL_SELECTION);
		}
		if (!functionalizingEntities.isEmpty()) {
			compositionSections.add(FUNCTIONALIZING_SELECTION);
		}
		if (!chemicalAssociations.isEmpty()) {
			compositionSections.add(CHEMICAL_SELECTION);
		}
		if (!files.isEmpty()) {
			compositionSections.add(FILE_SELECTION);
		}

		SortedSet<NanomaterialEntityBean> typeNanoEntities = null;
		for (NanomaterialEntityBean entity : nanomaterialEntities) {
			String type = entity.getType();
			if (type2NanoEntities.get(type) != null) {
				typeNanoEntities = type2NanoEntities.get(type);
			} else {
				typeNanoEntities = new TreeSet<NanomaterialEntityBean>(
						new Comparators.NanomaterialEntityBeanTypeDateComparator());
				type2NanoEntities.put(type, typeNanoEntities);
			}
			typeNanoEntities.add(entity);
			nanoEntityTypes.add(type);
		}

		SortedSet<FunctionalizingEntityBean> typeFuncEntities = null;
		for (FunctionalizingEntityBean entity : functionalizingEntities) {
			String type = entity.getType();
			if (type2FuncEntities.get(type) != null) {
				typeFuncEntities = type2FuncEntities.get(type);
			} else {
				typeFuncEntities = new TreeSet<FunctionalizingEntityBean>(
						new Comparators.FunctionalizingEntityBeanTypeDateComparator());
				type2FuncEntities.put(type, typeFuncEntities);
			}
			typeFuncEntities.add(entity);
			funcEntityTypes.add(type);
		}
	}

	public List<NanomaterialEntityBean> getNanomaterialEntities() {
		return nanomaterialEntities;
	}

	public void setNanomaterialEntities(
			List<NanomaterialEntityBean> nanomaterialEntities) {
		this.nanomaterialEntities = nanomaterialEntities;
	}

	public List<FunctionalizingEntityBean> getFunctionalizingEntities() {
		return functionalizingEntities;
	}

	public void setFunctionalizingEntities(
			List<FunctionalizingEntityBean> functionalizingEntities) {
		this.functionalizingEntities = functionalizingEntities;
	}

	public List<ChemicalAssociationBean> getChemicalAssociations() {
		return chemicalAssociations;
	}

	public void setChemicalAssociations(
			List<ChemicalAssociationBean> chemicalAssociations) {
		this.chemicalAssociations = chemicalAssociations;
	}

	public List<FileBean> getFiles() {
		return files;
	}

	public void setFiles(List<FileBean> files) {
		this.files = files;
	}

	public SampleComposition getDomain() {
		return domain;
	}

	public List<String> getCompositionSections() {
		return compositionSections;
	}

	public FileBean getTheFile() {
		return theFile;
	}

	public void setTheFile(FileBean theFile) {
		this.theFile = theFile;
	}

	public SampleComposition resetDomainCopy(SampleComposition copy) {
		copy.setId(null);

		// need to set chemical association copy first for associated element
		// IDs are set to null after setting nanomaterial entity and
		// functionalizing entity
		Collection<ChemicalAssociation> oldAssocs = copy
				.getChemicalAssociationCollection();
		if (oldAssocs == null || oldAssocs.isEmpty()) {
			copy.setChemicalAssociationCollection(null);
		} else {
			copy
					.setChemicalAssociationCollection(new HashSet<ChemicalAssociation>(
							oldAssocs));
			for (ChemicalAssociation assoc : copy
					.getChemicalAssociationCollection()) {
				ChemicalAssociationBean assocBean = new ChemicalAssociationBean(
						assoc);
				assocBean.resetDomainCopy(assoc);
			}
		}
		Collection<NanomaterialEntity> oldNanoEntities = copy
				.getNanomaterialEntityCollection();
		if (oldNanoEntities == null || oldNanoEntities.isEmpty()) {
			copy.setNanomaterialEntityCollection(null);
		} else {
			copy
					.setNanomaterialEntityCollection(new HashSet<NanomaterialEntity>(
							oldNanoEntities));
			for (NanomaterialEntity entity : copy
					.getNanomaterialEntityCollection()) {
				NanomaterialEntityBean entityBean = new NanomaterialEntityBean(
						entity);
				entityBean.resetDomainCopy(entity);
			}
		}

		Collection<FunctionalizingEntity> oldFuncEntities = copy
				.getFunctionalizingEntityCollection();
		if (oldFuncEntities == null || oldFuncEntities.isEmpty()) {
			copy.setFunctionalizingEntityCollection(null);
		} else {
			copy
					.setFunctionalizingEntityCollection(new HashSet<FunctionalizingEntity>(
							oldFuncEntities));
			for (FunctionalizingEntity entity : copy
					.getFunctionalizingEntityCollection()) {
				FunctionalizingEntityBean entityBean = new FunctionalizingEntityBean(
						entity);
				entityBean.resetDomainCopy(entity);
			}
		}

		Collection<File> oldFiles = copy.getFileCollection();
		if (oldFiles == null || oldFiles.isEmpty()) {
			copy.setFileCollection(null);
		} else {
			copy.setFileCollection(new HashSet<File>(oldFiles));
			for (File file : copy.getFileCollection()) {
				FileBean fileBean = new FileBean(file);
				fileBean.resetDomainCopy(file);
			}
		}

		return copy;
	}

	public Map<String, SortedSet<NanomaterialEntityBean>> getType2NanoEntities() {
		return type2NanoEntities;
	}

	public Map<String, SortedSet<FunctionalizingEntityBean>> getType2FuncEntities() {
		return type2FuncEntities;
	}

	public SortedSet<String> getNanoEntityTypes() {
		return nanoEntityTypes;
	}

	public SortedSet<String> getFuncEntityTypes() {
		return funcEntityTypes;
	}
}

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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
		if (copy.getNanomaterialEntityCollection() == null
				|| copy.getNanomaterialEntityCollection().isEmpty()) {
			copy.setNanomaterialEntityCollection(null);
		} else {
			Collection<NanomaterialEntity> nanoEntities = copy
					.getNanomaterialEntityCollection();
			copy
					.setNanomaterialEntityCollection(new HashSet<NanomaterialEntity>());
			copy.getNanomaterialEntityCollection().addAll(nanoEntities);
			for (NanomaterialEntity entity : copy
					.getNanomaterialEntityCollection()) {
				NanomaterialEntityBean entityBean = new NanomaterialEntityBean(
						entity);
				entityBean.resetDomainCopy(entity);
			}
		}

		if (copy.getFunctionalizingEntityCollection() == null
				|| copy.getFunctionalizingEntityCollection().isEmpty()) {
			copy.setFunctionalizingEntityCollection(null);
		} else {
			Collection<FunctionalizingEntity> funcEntities = copy
					.getFunctionalizingEntityCollection();
			copy
					.setFunctionalizingEntityCollection(new HashSet<FunctionalizingEntity>());
			copy.getFunctionalizingEntityCollection().addAll(funcEntities);
			for (FunctionalizingEntity entity : copy
					.getFunctionalizingEntityCollection()) {
				FunctionalizingEntityBean entityBean = new FunctionalizingEntityBean(
						entity);
				entityBean.resetDomainCopy(entity);
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
				FileBean fileBean = new FileBean(file);
				fileBean.resetDomainCopy(file);
			}
		}
		
		return copy;
	}
}

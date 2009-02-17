package gov.nih.nci.cananolab.dto.particle.composition;

import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.particle.ChemicalAssociation;
import gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity;
import gov.nih.nci.cananolab.domain.particle.NanoparticleEntity;
import gov.nih.nci.cananolab.domain.particle.SampleComposition;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.util.CaNanoLabComparators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompositionBean {
	private List<NanoparticleEntityBean> nanoparticleEntities = new ArrayList<NanoparticleEntityBean>();
	private List<FunctionalizingEntityBean> functionalizingEntities = new ArrayList<FunctionalizingEntityBean>();
	private List<ChemicalAssociationBean> chemicalAssociations = new ArrayList<ChemicalAssociationBean>();
	private List<FileBean> files = new ArrayList<FileBean>();
	private SampleComposition domain;

	public CompositionBean() {

	}

	public CompositionBean(SampleComposition comp) {
		domain=comp;
		if (comp.getNanoparticleEntityCollection() != null) {
			for (NanoparticleEntity entity : comp
					.getNanoparticleEntityCollection()) {
				nanoparticleEntities.add(new NanoparticleEntityBean(entity));
			}
		}
		Collections
				.sort(
						nanoparticleEntities,
						new CaNanoLabComparators.NanoparticleEntityBeanTypeDateComparator());
		if (comp.getFunctionalizingEntityCollection() != null) {
			for (FunctionalizingEntity entity : comp
					.getFunctionalizingEntityCollection()) {
				functionalizingEntities.add(new FunctionalizingEntityBean(
						entity));
			}
		}
		Collections
				.sort(
						functionalizingEntities,
						new CaNanoLabComparators.FunctionalizingEntityBeanTypeDateComparator());
		if (comp.getChemicalAssociationCollection() != null) {
			for (ChemicalAssociation assoc : comp
					.getChemicalAssociationCollection()) {
				chemicalAssociations.add(new ChemicalAssociationBean(assoc));
			}
		}
		Collections
				.sort(
						chemicalAssociations,
						new CaNanoLabComparators.ChemicalAssociationBeanTypeDateComparator());
		if (comp.getFileCollection() != null) {
			for (File file : comp.getFileCollection()) {
				files.add(new FileBean(file));
			}
		}
		Collections
				.sort(
						files,
						new CaNanoLabComparators.FileBeanTypeDateComparator());
	}

	public List<NanoparticleEntityBean> getNanoparticleEntities() {
		return nanoparticleEntities;
	}

	public void setNanoparticleEntities(
			List<NanoparticleEntityBean> nanoparticleEntities) {
		this.nanoparticleEntities = nanoparticleEntities;
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
}

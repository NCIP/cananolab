package gov.nih.nci.calab.dto.characterization.composition;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.DendrimerComposition;
import gov.nih.nci.calab.service.util.CalabConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents properties of a Dendrimer composition to be shown in
 * the view page.
 * 
 * @author pansu
 * 
 */

public class DendrimerBean extends CompositionBean {
	private String branch;

	private String repeatUnit;

	private String generation;

	private String numberOfSurfaceGroups;

	private String molecularFormula;

	private List<SurfaceGroupBean> surfaceGroups;

	private ComposingElementBean core;

	public DendrimerBean() {
		super();
		surfaceGroups = new ArrayList<SurfaceGroupBean>();
		List<ComposingElementBean> composingElements = getComposingElements();
		core = new ComposingElementBean();
		core.setElementType("core");
		composingElements.add(core);
		setComposingElements(composingElements);
		setNumberOfElements("1");
	}

	public String getBranch() {
		return branch;
	}

	public String getGeneration() {
		return generation;
	}

	public String getRepeatUnit() {
		return repeatUnit;
	}

	public String getNumberOfSurfaceGroups() {
		return numberOfSurfaceGroups;
	}

	public String getMolecularFormula() {
		return molecularFormula;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public void setGeneration(String generation) {
		this.generation = generation;
	}

	public void setMolecularFormula(String molecularFormula) {
		this.molecularFormula = molecularFormula;
	}

	public void setNumberOfSurfaceGroups(String numberOfSurfaceGroups) {
		this.numberOfSurfaceGroups = numberOfSurfaceGroups;
	}

	public void setRepeatUnit(String repeatUnit) {
		this.repeatUnit = repeatUnit;
	}

	public List<SurfaceGroupBean> getSurfaceGroups() {
		return surfaceGroups;
	}

	public void setSurfaceGroups(List<SurfaceGroupBean> surfaceGroups) {
		this.surfaceGroups = surfaceGroups;
	}

	public SurfaceGroupBean getSurfaceGroup(int ind) {
		return surfaceGroups.get(ind);
	}

	public void setSurfaceGroup(int ind, SurfaceGroupBean surfaceGroup) {
		surfaceGroups.set(ind, surfaceGroup);
	}

	public ComposingElementBean getCore() {
		return core;
	}

	public void setCore(ComposingElementBean core) {
		this.core = core;
	}

	public Characterization getDomainObj() {
		DendrimerComposition doComp = new DendrimerComposition();
		if (generation.length() > 0) {
			doComp.setGeneration(new Float(generation));
		}
		doComp.setBranch(branch);
		doComp.setRepeatUnit(repeatUnit);
		doComp.setMolecularFormula(molecularFormula);
		for (SurfaceGroupBean surfaceGroup : surfaceGroups) {
			doComp.getSurfaceGroupCollection().add(surfaceGroup.getDomainObj());
		}		
		doComp.setSource(getCharacterizationSource());
		doComp.setClassification(getCharacterizationClassification());
		doComp.setIdentificationName(getViewTitle());
		doComp.setDescription(getDescription());
		doComp.setName(CalabConstants.COMPOSITION_CHARACTERIZATION);
		for (ComposingElementBean element : getComposingElements()) {
			doComp.getComposingElementCollection().add(element.getDomainObj());
		}
		return doComp;
	}
}

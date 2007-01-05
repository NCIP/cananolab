package gov.nih.nci.calab.dto.characterization.composition;

import gov.nih.nci.calab.domain.nano.characterization.physical.composition.DendrimerComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.SurfaceGroup;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;

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
	
	private String otherBranch;

	private String repeatUnit;

	private String generation;
	
	private String otherGeneration;

	private String numberOfSurfaceGroups;

	private String molecularFormula;

	private List<SurfaceGroupBean> surfaceGroups = new ArrayList<SurfaceGroupBean>();;

	private ComposingElementBean core = new ComposingElementBean();

	public DendrimerBean() {
		super();
		surfaceGroups = new ArrayList<SurfaceGroupBean>();		
		core.setElementType(CaNanoLabConstants.CORE);
		getComposingElements().add(core);			
	}

	public DendrimerBean(DendrimerComposition dendrimer) {
		super(dendrimer);		
		this.branch = dendrimer.getBranch();
		this.generation = (dendrimer.getGeneration() == null) ? "" : dendrimer
				.getGeneration().toString();
		this.molecularFormula = dendrimer.getMolecularFormula();
		this.repeatUnit = dendrimer.getRepeatUnit();

		for (ComposingElementBean element : getComposingElements()) {
			core = element;
		}
		for (SurfaceGroup surface : dendrimer.getSurfaceGroupCollection()) {
			SurfaceGroupBean surfaceBean = new SurfaceGroupBean(surface);
			surfaceGroups.add(surfaceBean);
		}
		this.numberOfSurfaceGroups = surfaceGroups.size() + "";
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

	public ComposingElementBean getCore() {
		return core;
	}

	public void setCore(ComposingElementBean core) {
		this.core = core;
	}

	public String getOtherBranch() {
		return otherBranch;
	}

	public void setOtherBranch(String otherBranch) {
		this.otherBranch = otherBranch;
	}

	public String getOtherGeneration() {
		return otherGeneration;
	}

	public void setOtherGeneration(String otherGeneration) {
		this.otherGeneration = otherGeneration;
	}

	public DendrimerComposition getDomainObj() {
		DendrimerComposition doComp = new DendrimerComposition();
		super.updateDomainObj(doComp);

		if (generation.length() > 0) {
			if (generation.equalsIgnoreCase(CaNanoLabConstants.OTHER)){
				generation = otherGeneration;
			}
			doComp.setGeneration(new Float(generation));
		}
		if (branch.equalsIgnoreCase(CaNanoLabConstants.OTHER)) {
			doComp.setBranch(otherBranch);
		}
		else {
			doComp.setBranch(branch);
		}
		doComp.setRepeatUnit(repeatUnit);
		doComp.setMolecularFormula(molecularFormula);
		for (SurfaceGroupBean surfaceGroup : surfaceGroups) {
			doComp.getSurfaceGroupCollection().add(surfaceGroup.getDomainObj());
		}
		return doComp;
	}
	
}

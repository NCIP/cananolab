package gov.nih.nci.calab.dto.characterization.composition;

import gov.nih.nci.calab.domain.nano.characterization.physical.composition.DendrimerComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.SurfaceGroup;

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

	private List<SurfaceGroupBean> surfaceGroups = new ArrayList<SurfaceGroupBean>();;

	public DendrimerBean() {
	}

	public DendrimerBean(DendrimerComposition dendrimer) {
		super(dendrimer);
		this.branch = dendrimer.getBranch();
		this.generation = (dendrimer.getGeneration() == null) ? "" : dendrimer
				.getGeneration().toString();
		this.molecularFormula = dendrimer.getMolecularFormula();
		this.repeatUnit = dendrimer.getRepeatUnit();
		for (SurfaceGroup surface : dendrimer.getSurfaceGroupCollection()) {
			SurfaceGroupBean surfaceBean = new SurfaceGroupBean(surface);
			this.surfaceGroups.add(surfaceBean);
		}
		this.numberOfSurfaceGroups = this.surfaceGroups.size() + "";
	}

	public String getBranch() {
		return this.branch;
	}

	public String getGeneration() {
		return this.generation;
	}

	public String getRepeatUnit() {
		return this.repeatUnit;
	}

	public String getNumberOfSurfaceGroups() {
		return this.numberOfSurfaceGroups;
	}

	public String getMolecularFormula() {
		return this.molecularFormula;
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
		return this.surfaceGroups;
	}

	public void setSurfaceGroups(List<SurfaceGroupBean> surfaceGroups) {
		this.surfaceGroups = surfaceGroups;
	}

	public void updateDomainObj(DendrimerComposition doComp) {		
		super.updateDomainObj(doComp);
		if (this.generation.length() > 0) {
			doComp.setGeneration(new Float(this.generation));
		}
		doComp.setBranch(this.branch);
		doComp.setRepeatUnit(this.repeatUnit);
		doComp.setMolecularFormula(this.molecularFormula);
		updateSurfaceGroups(doComp);
	}

	// update domain object's surface group collection
	private void updateSurfaceGroups(DendrimerComposition doComp) {
		// copy collection
		List<SurfaceGroup> doSurfaceGroupList = new ArrayList<SurfaceGroup>(
				doComp.getSurfaceGroupCollection());
		// clear the existing collection
		doComp.getSurfaceGroupCollection().clear();
		for (SurfaceGroupBean surfaceGroupBean : getSurfaceGroups()) {
			SurfaceGroup doSurfaceGroup = null;
			// if no id, add new domain object
			if (surfaceGroupBean.getId() == null) {
				doSurfaceGroup = new SurfaceGroup();
			} else {
				// find domain object with the same ID and add the updated
				// domain object
				for (SurfaceGroup surfaceGroup : doSurfaceGroupList) {
					if (surfaceGroup.getId().equals(
							new Long(surfaceGroupBean.getId()))) {
						doSurfaceGroup = surfaceGroup;
						break;
					}
				}
			}
			surfaceGroupBean.updateDomainObj(doSurfaceGroup);
			doComp.getSurfaceGroupCollection().add(doSurfaceGroup);
		}
	}
}

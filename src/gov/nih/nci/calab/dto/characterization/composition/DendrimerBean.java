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

	private String repeatUnit;

	private String generation;

	private String numberOfSurfaceGroups;

	private String molecularFormula;

	private List<SurfaceGroupBean> surfaceGroups = new ArrayList<SurfaceGroupBean>();;

	private ComposingElementBean core = new ComposingElementBean();

	public DendrimerBean() {

		this.surfaceGroups = new ArrayList<SurfaceGroupBean>();
		this.core.setElementType(CaNanoLabConstants.CORE);
		getComposingElements().add(this.core);
	}

	public DendrimerBean(DendrimerComposition dendrimer) {
		super(dendrimer);
		this.branch = dendrimer.getBranch();
		this.generation = (dendrimer.getGeneration() == null) ? "" : dendrimer
				.getGeneration().toString();
		this.molecularFormula = dendrimer.getMolecularFormula();
		this.repeatUnit = dendrimer.getRepeatUnit();

		for (ComposingElementBean element : getComposingElements()) {
			this.core = element;
		}
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

	public ComposingElementBean getCore() {
		return this.core;
	}

	public void setCore(ComposingElementBean core) {
		this.core = core;
	}

	public DendrimerComposition getDomainObj() {
		DendrimerComposition doComp = new DendrimerComposition();
		super.updateDomainObj(doComp);

		if (this.generation.length() > 0) {
			doComp.setGeneration(new Float(this.generation));
		}
		doComp.setBranch(this.branch);
		doComp.setRepeatUnit(this.repeatUnit);
		doComp.setMolecularFormula(this.molecularFormula);
		for (SurfaceGroupBean surfaceGroup : this.surfaceGroups) {
			doComp.getSurfaceGroupCollection().add(surfaceGroup.getDomainObj());
		}
		return doComp;
	}

}

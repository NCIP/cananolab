package gov.nih.nci.calab.dto.characterization.composition;

import gov.nih.nci.calab.dto.characterization.CharacterizationBean;

import java.util.ArrayList;
import java.util.List;

public class DendrimerBean extends CharacterizationBean {
	private String core;

	private String branch;

	private String repeatUnit;

	private String generation;
	
	private String numberOfSurfaceGroups;
	
	private String molecularFormula;

	private List<SurfaceGroupBean> surfaceGroups;	
	
	public DendrimerBean() {
		surfaceGroups=new ArrayList<SurfaceGroupBean>();
	}
	
	public String getBranch() {
		return branch;
	}

	public String getCore() {
		return core;
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

	public void setCore(String core) {
		this.core = core;
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
	
	public SurfaceGroupBean getSurfaceGroup(int ind){
		return surfaceGroups.get(ind);
	}
	
	public void setSurfaceGroup(int ind, SurfaceGroupBean surfaceGroup) {
		surfaceGroups.set(ind, surfaceGroup);
	}
}

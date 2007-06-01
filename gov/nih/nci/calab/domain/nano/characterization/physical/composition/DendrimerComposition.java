package gov.nih.nci.calab.domain.nano.characterization.physical.composition;

import java.util.ArrayList;
import java.util.Collection;

public class DendrimerComposition extends ParticleComposition {
	private static final long serialVersionUID = 1234567890L;

	private String branch;

	private Float generation;

	private String molecularFormula;

	private String repeatUnit;

	private Collection<SurfaceGroup> surfaceGroupCollection = new ArrayList<SurfaceGroup>();

	public DendrimerComposition() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public Float getGeneration() {
		return generation;
	}

	public void setGeneration(Float generation) {
		this.generation = generation;
	}

	public String getMolecularFormula() {
		return molecularFormula;
	}

	public void setMolecularFormula(String molecularFormula) {
		this.molecularFormula = molecularFormula;
	}

	public String getRepeatUnit() {
		return repeatUnit;
	}

	public void setRepeatUnit(String repeatUnit) {
		this.repeatUnit = repeatUnit;
	}

	public Collection<SurfaceGroup> getSurfaceGroupCollection() {
		return surfaceGroupCollection;
	}

	public void setSurfaceGroupCollection(
			Collection<SurfaceGroup> surfaceGroupCollection) {
		this.surfaceGroupCollection = surfaceGroupCollection;
	}

	public String getClassification() {
		return PHYSICAL_CHARACTERIZATION;
	}

	public String getName() {
		return PHYSICAL_COMPOSITION;
	}
}

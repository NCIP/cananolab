/**
 * 
 */
package gov.nih.nci.calab.dto.characterization.physical;

import gov.nih.nci.calab.domain.nano.characterization.physical.SurfaceChemistry;

/**
 * @author zengje
 *
 */
public class SurfaceChemistryBean {
	private String id;
	private String moleculeName;
	private String numberOfMolecules;

	/**
	 * 
	 */
	public SurfaceChemistryBean(){
	}
	
	public SurfaceChemistryBean(SurfaceChemistry surfaceChemistry) {
		this.id = surfaceChemistry.getId().toString();
		this.moleculeName = surfaceChemistry.getMoleculeName();
		this.numberOfMolecules = surfaceChemistry.getNumberOfMolecule().toString();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
	public String getMoleculeName() {
		return moleculeName;
	}
	public void setMoleculeName(String molecule) {
		this.moleculeName = molecule;
	}
	
	public SurfaceChemistry getDomainObj() {
		SurfaceChemistry surfaceChemistry = new SurfaceChemistry();
		surfaceChemistry.setNumberOfMolecule((getNumberOfMolecules()!= null)?Integer.parseInt(getNumberOfMolecules()):null);
		surfaceChemistry.setMoleculeName(getMoleculeName());
		if (getId()!=null&&getId().length() > 0) {
			surfaceChemistry.setId(new Long(getId()));
		}
		return surfaceChemistry;
	}

	public String getNumberOfMolecules() {
		return numberOfMolecules;
	}

	public void setNumberOfMolecules(String numberOfMolecule) {
		this.numberOfMolecules = numberOfMolecule;
	}

}

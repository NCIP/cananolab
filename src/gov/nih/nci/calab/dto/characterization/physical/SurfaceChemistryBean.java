/**
 * 
 */
package gov.nih.nci.calab.dto.characterization.physical;

import gov.nih.nci.calab.domain.Measurement;
import gov.nih.nci.calab.domain.nano.characterization.physical.SurfaceChemistry;

/**
 * @author zengje
 *
 */
public class SurfaceChemistryBean {
	private String id;
	private String moleculeName;
	private String numberOfMolecule;

	/**
	 * 
	 */
	public SurfaceChemistryBean(){
		super();
	}
	
	public SurfaceChemistryBean(SurfaceChemistry surfaceChemistry) {
		this.id = surfaceChemistry.getId().toString();
		this.moleculeName = surfaceChemistry.getMoleculeName();
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
		surfaceChemistry.setNumberOfMolecule(new Integer(getNumberOfMolecule()));
		surfaceChemistry.setMoleculeName(getMoleculeName());
		if (getId()!=null&&getId().length() > 0) {
			surfaceChemistry.setId(new Long(getId()));
		}
		return surfaceChemistry;
	}

	public String getNumberOfMolecule() {
		return numberOfMolecule;
	}

	public void setNumberOfMolecule(String numberOfMolecule) {
		this.numberOfMolecule = numberOfMolecule;
	}

}

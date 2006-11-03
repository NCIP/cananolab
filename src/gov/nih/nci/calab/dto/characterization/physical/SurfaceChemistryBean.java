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
	private String molecule;
	private String density;
	private String densityUnit;
	/**
	 * 
	 */
	public SurfaceChemistryBean(){
		super();
	}
	
	public SurfaceChemistryBean(SurfaceChemistry surfaceChemistry) {
		this.id = surfaceChemistry.getId().toString();
		this.molecule = surfaceChemistry.getMolecule();
		this.density = surfaceChemistry.getDensity().getValue().toString();
		this.densityUnit = surfaceChemistry.getDensity().getUnitOfMeasurement().toString();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDensity() {
		return density;
	}
	public void setDensity(String density) {
		this.density = density;
	}
	public String getDensityUnit() {
		return densityUnit;
	}
	public void setDensityUnit(String densityUnit) {
		this.densityUnit = densityUnit;
	}
	public String getMolecule() {
		return molecule;
	}
	public void setMolecule(String molecule) {
		this.molecule = molecule;
	}
	
	public SurfaceChemistry getDomainObj() {
		SurfaceChemistry surfaceChemistry = new SurfaceChemistry();
		surfaceChemistry.setDensity(new Measurement(getDensity(),getDensityUnit()));
		surfaceChemistry.setMolecule(getMolecule());
		if (getId()!=null&&getId().length() > 0) {
			surfaceChemistry.setId(new Long(getId()));
		}
		return surfaceChemistry;
	}

}

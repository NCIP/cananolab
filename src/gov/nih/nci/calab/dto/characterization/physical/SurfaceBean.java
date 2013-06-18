/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.calab.dto.characterization.physical;

import gov.nih.nci.calab.domain.Measurement;
import gov.nih.nci.calab.domain.nano.characterization.physical.Surface;
import gov.nih.nci.calab.domain.nano.characterization.physical.SurfaceChemistry;
import gov.nih.nci.calab.dto.characterization.CharacterizationBean;
import gov.nih.nci.calab.dto.characterization.DerivedBioAssayDataBean;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.service.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the size characterization information to be shown in
 * the view page.
 * 
 * @author pansu
 * 
 */
public class SurfaceBean extends CharacterizationBean {

	private String surfaceArea;
	
	private String surfaceAreaUnit;

	private String zetaPotential;

	private String charge;

	private String chargeUnit;

	private String isHydrophobic;
	
	private String numberOfSurfaceChemistries;

	private List<SurfaceChemistryBean> surfaceChemistries = new ArrayList<SurfaceChemistryBean>();

	public SurfaceBean() {
		super();
		surfaceChemistries = new ArrayList<SurfaceChemistryBean>();
		initSetup();
	}

	public SurfaceBean(Surface aChar) {
		super(aChar);
		
		this.charge = (aChar.getCharge() != null)?StringUtils.convertToString(aChar.getCharge().getValue()):"";
		this.chargeUnit = (aChar.getCharge() != null)?aChar.getCharge().getUnitOfMeasurement():"";
		this.isHydrophobic = (aChar.getIsHydrophobic()!=null)?aChar.getIsHydrophobic().toString():"";
		this.surfaceArea = (aChar.getSurfaceArea() != null)?aChar.getSurfaceArea().getValue().toString():"";
		this.surfaceAreaUnit = (aChar.getSurfaceArea() != null)?aChar.getSurfaceArea().getUnitOfMeasurement():"";
		//this.zetaPotential = (aChar.getZetaPotential() != null)?aChar.getZetaPotential().getValue():"";
		this.zetaPotential = (aChar.getZetaPotential() != null) ? aChar.getZetaPotential().toString() : "";
		
		for (SurfaceChemistry surfaceChemistry : aChar.getSurfaceChemistryCollection()) {
			SurfaceChemistryBean surfaceChemistryBean = new SurfaceChemistryBean(surfaceChemistry);
			surfaceChemistries.add(surfaceChemistryBean);
		}
		this.numberOfSurfaceChemistries = surfaceChemistries.size()+"";
		
	}

	public String getCharge() {
		return charge;
	}

	public void setCharge(String charge) {
		this.charge = charge;
	}

	public String getIsHydrophobic() {
		return isHydrophobic;
	}

	public void setIsHydrophobic(String isHydrophobic) {
		this.isHydrophobic = isHydrophobic;
	}

	public String getSurfaceArea() {
		return surfaceArea;
	}

	public void setSurfaceArea(String surfaceArea) {
		this.surfaceArea = surfaceArea;
	}

	public String getZetaPotential() {
		return zetaPotential;
	}

	public void setZetaPotential(String zetaPotential) {
		this.zetaPotential = zetaPotential;
	}

	public void setDerivedBioAssayDataList(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayDataList(derivedBioAssayData);
		initSetup();
	}

	public String getChargeUnit() {
		return chargeUnit;
	}

	public void setChargeUnit(String chargeUnit) {
		this.chargeUnit = chargeUnit;
	}

	public List<SurfaceChemistryBean> getSurfaceChemistries() {
		return surfaceChemistries;
	}

	public void setSurfaceChemistries(List<SurfaceChemistryBean> surfaceGroups) {
		this.surfaceChemistries = surfaceGroups;
	}

	public String getSurfaceAreaUnit() {
		return surfaceAreaUnit;
	}

	public void setSurfaceAreaUnit(String surfaceAreaUnit) {
		this.surfaceAreaUnit = surfaceAreaUnit;
	}

	public void initSetup() {
//		for (DerivedBioAssayDataBean table : getDerivedBioAssayData()) {
//			DatumBean average = new DatumBean();
//			average.setType("Average");
//			average.setValueUnit("nm");
//			DatumBean zaverage = new DatumBean();
//			zaverage.setType("Z-Average");
//			zaverage.setValueUnit("nm");
//			DatumBean pdi = new DatumBean();
//			pdi.setType("PDI");
//			table.getDatumList().add(average);
//			table.getDatumList().add(zaverage);
//			table.getDatumList().add(pdi);
//		}
	}

	public Surface getDomainObj() {
		Surface surface = new Surface();
		super.updateDomainObj(surface);
		boolean hycrophobicStatus = (isHydrophobic.equalsIgnoreCase(CaNanoLabConstants.BOOLEAN_YES)) ? true
				: false;
		surface.setIsHydrophobic(hycrophobicStatus);
		surface.setCharge(new Measurement(new Float(charge),chargeUnit));
		//surface.setZetaPotential(new Measurement(zetaPotential, "mV"));
		surface.setZetaPotential((zetaPotential.length()==0)?null:new Float(zetaPotential));
		
		surface.setSurfaceArea(new Measurement(new Float(surfaceArea), surfaceAreaUnit));
		
		for (SurfaceChemistryBean surfaceChemistry : surfaceChemistries) {
			surface.getSurfaceChemistryCollection().add(surfaceChemistry.getDomainObj());
		}
		return surface;
	}


	public String getNumberOfSurfaceChemistries() {
		return numberOfSurfaceChemistries;
	}

	public void setNumberOfSurfaceChemistries(String numberOfSurfaceChemistry) {
		this.numberOfSurfaceChemistries = numberOfSurfaceChemistry;
	}
}

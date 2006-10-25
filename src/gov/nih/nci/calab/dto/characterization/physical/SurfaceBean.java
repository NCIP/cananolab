package gov.nih.nci.calab.dto.characterization.physical;

import gov.nih.nci.calab.domain.Measurement;
import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.DerivedBioAssayData;
import gov.nih.nci.calab.domain.nano.characterization.physical.Surface;
import gov.nih.nci.calab.dto.characterization.CharacterizationBean;
import gov.nih.nci.calab.dto.characterization.DatumBean;
import gov.nih.nci.calab.dto.characterization.DerivedBioAssayDataBean;
import gov.nih.nci.calab.dto.characterization.composition.SurfaceGroupBean;
import gov.nih.nci.calab.service.util.CananoConstants;

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

	private String type;
	
	private String otherSurfaceType;

	private String surfaceArea;
	
	private String surfaceAreaUnit;

	private String zetaPotential;

	private String charge;

	private String chargeUnit;

	private String isHydrophobic;

	private List<SurfaceChemistryBean> surfaceChemistries = new ArrayList<SurfaceChemistryBean>();;

	public SurfaceBean() {
		super();
		initSetup();
	}

	public SurfaceBean(Characterization aChar) {
		super();
		this.setCharacterizationSource(aChar.getSource());
		this.setViewTitle(aChar.getIdentificationName());
		this.setDescription(aChar.getDescription());
		if (aChar.getInstrument() != null) {
			this.getInstrument().setType(
					aChar.getInstrument().getInstrumentType().getName());
			this.getInstrument().setDescription(
					aChar.getInstrument().getDescription());
			this.getInstrument().setManufacturer(
					aChar.getInstrument().getManufacturer().getName());
		}
		this.setNumberOfDerivedBioAssayData(Integer.valueOf(
				aChar.getDerivedBioAssayDataCollection().size()).toString());
		for (DerivedBioAssayData table : aChar
				.getDerivedBioAssayDataCollection()) {
			DerivedBioAssayDataBean ctBean = new DerivedBioAssayDataBean(table);
			this.getDerivedBioAssayData().add(ctBean);
		}
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getZetaPotential() {
		return zetaPotential;
	}

	public void setZetaPotential(String zetaPotential) {
		this.zetaPotential = zetaPotential;
	}

	public void setDerivedBioAssayData(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayData(derivedBioAssayData);
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
		boolean hycrophobicStatus = (isHydrophobic.equalsIgnoreCase(CananoConstants.BOOLEAN_YES)) ? true
				: false;
		surface.setIsHydrophobic(hycrophobicStatus);
		surface.setCharge(new Measurement(charge,chargeUnit));
		surface.setZetaPotential(new Measurement(zetaPotential, "mV"));
		
		surface.setType((this.type == null || this.type.length()<=0)?this.otherSurfaceType:this.type);
		surface.setSurfaceArea(new Measurement(surfaceArea, surfaceAreaUnit));
		
		for (SurfaceChemistryBean surfaceChemistry : surfaceChemistries) {
			surface.getSurfaceChemistryCollection().add(surfaceChemistry.getDomainObj());
		}
		return surface;
	}

	public String getOtherSurfaceType() {
		return otherSurfaceType;
	}

	public void setOtherSurfaceType(String otherSurfaceType) {
		this.otherSurfaceType = otherSurfaceType;
	}
}

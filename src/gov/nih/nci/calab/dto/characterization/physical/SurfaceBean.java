package gov.nih.nci.calab.dto.characterization.physical;

import gov.nih.nci.calab.domain.Measurement;
import gov.nih.nci.calab.domain.nano.characterization.physical.Surface;
import gov.nih.nci.calab.domain.nano.characterization.physical.SurfaceChemistry;
import gov.nih.nci.calab.dto.characterization.CharacterizationBean;
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

	private String zetaPotentialUnit;

	private String charge;

	private String chargeUnit;

	private String isHydrophobic;

	private List<SurfaceChemistryBean> surfaceChemistries = new ArrayList<SurfaceChemistryBean>();

	public SurfaceBean() {
		super();
		surfaceChemistries = new ArrayList<SurfaceChemistryBean>();
	}

	public SurfaceBean(SurfaceBean propBean, CharacterizationBean charBean) {
		super(charBean);
		this.charge = propBean.getCharge();
		this.chargeUnit = propBean.getChargeUnit();
		this.isHydrophobic = propBean.getIsHydrophobic();
		this.surfaceArea = propBean.getSurfaceArea();
		this.surfaceAreaUnit = propBean.getSurfaceAreaUnit();
		this.surfaceChemistries = propBean.getSurfaceChemistries();
		this.zetaPotential = propBean.getZetaPotential();
		this.zetaPotentialUnit = propBean.getZetaPotentialUnit();
	}

	public SurfaceBean(Surface aChar) {
		super(aChar);

		this.charge = (aChar.getCharge() != null) ? StringUtils
				.convertToString(aChar.getCharge().getValue()) : "";
		this.chargeUnit = (aChar.getCharge() != null) ? aChar.getCharge()
				.getUnitOfMeasurement() : "";
		this.isHydrophobic = aChar.getIsHydrophobic() ? CaNanoLabConstants.BOOLEAN_YES
				: CaNanoLabConstants.BOOLEAN_NO;
		this.surfaceArea = (aChar.getSurfaceArea() != null) ? aChar
				.getSurfaceArea().getValue().toString() : "";
		this.surfaceAreaUnit = (aChar.getSurfaceArea() != null) ? aChar
				.getSurfaceArea().getUnitOfMeasurement() : "";
		// this.zetaPotential = (aChar.getZetaPotential() !=
		// null)?aChar.getZetaPotential().getValue():"";
		this.zetaPotential = (aChar.getZetaPotential() != null) ? aChar
				.getZetaPotential().getValue().toString() : "";
		this.zetaPotentialUnit = (aChar.getZetaPotential() != null) ? aChar
				.getZetaPotential().getUnitOfMeasurement() : "";

		for (SurfaceChemistry surfaceChemistry : aChar
				.getSurfaceChemistryCollection()) {
			SurfaceChemistryBean surfaceChemistryBean = new SurfaceChemistryBean(
					surfaceChemistry);
			surfaceChemistries.add(surfaceChemistryBean);
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

	public String getZetaPotential() {
		return zetaPotential;
	}

	public void setZetaPotential(String zetaPotential) {
		this.zetaPotential = zetaPotential;
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

	public void updateDomainObj(Surface surface) {
		super.updateDomainObj(surface);

		boolean hycrophobicStatus = (isHydrophobic
				.equalsIgnoreCase(CaNanoLabConstants.BOOLEAN_YES)) ? true
				: false;
		surface.setIsHydrophobic(hycrophobicStatus);
		surface.setCharge(new Measurement(new Float(charge), chargeUnit));
		// surface.setZetaPotential(new Measurement(zetaPotential, "mV"));
		surface.setZetaPotential(new Measurement(new Float(zetaPotential),
				zetaPotentialUnit));

		surface.setSurfaceArea(new Measurement(new Float(surfaceArea),
				surfaceAreaUnit));

		for (SurfaceChemistryBean surfaceChemistry : surfaceChemistries) {
			surface.getSurfaceChemistryCollection().add(
					surfaceChemistry.getDomainObj());
		}
	}

	public String getZetaPotentialUnit() {
		return zetaPotentialUnit;
	}

	public void setZetaPotentialUnit(String zetaPotentialUnit) {
		this.zetaPotentialUnit = zetaPotentialUnit;
	}
}

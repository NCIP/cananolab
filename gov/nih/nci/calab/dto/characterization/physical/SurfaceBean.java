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

	private String surfaceAreaUnit = "nm^2";

	private String zetaPotential;

	private String zetaPotentialUnit = "mV";

	private String charge;

	private String chargeUnit;

	private String isHydrophobic;

	private List<SurfaceChemistryBean> surfaceChemistries = new ArrayList<SurfaceChemistryBean>();

	public SurfaceBean() {

		this.surfaceChemistries = new ArrayList<SurfaceChemistryBean>();
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
		if (aChar.getIsHydrophobic() == null) {
			this.isHydrophobic = "";
		} else {
			this.isHydrophobic = (aChar.getIsHydrophobic()) ? CaNanoLabConstants.BOOLEAN_YES
					: CaNanoLabConstants.BOOLEAN_NO;
		}
		this.surfaceArea = (aChar.getSurfaceArea() != null) ? aChar
				.getSurfaceArea().getValue().toString() : "";
		this.zetaPotential = (aChar.getZetaPotential() != null) ? aChar
				.getZetaPotential().getValue().toString() : "";

		for (SurfaceChemistry surfaceChemistry : aChar
				.getSurfaceChemistryCollection()) {
			SurfaceChemistryBean surfaceChemistryBean = new SurfaceChemistryBean(
					surfaceChemistry);
			this.surfaceChemistries.add(surfaceChemistryBean);
		}
	}

	public String getCharge() {
		return this.charge;
	}

	public void setCharge(String charge) {
		this.charge = charge;
	}

	public String getIsHydrophobic() {
		return this.isHydrophobic;
	}

	public void setIsHydrophobic(String isHydrophobic) {
		this.isHydrophobic = isHydrophobic;
	}

	public String getSurfaceArea() {
		return this.surfaceArea;
	}

	public void setSurfaceArea(String surfaceArea) {
		this.surfaceArea = surfaceArea;
	}

	public String getZetaPotential() {
		return this.zetaPotential;
	}

	public void setZetaPotential(String zetaPotential) {
		this.zetaPotential = zetaPotential;
	}

	public String getChargeUnit() {
		return this.chargeUnit;
	}

	public void setChargeUnit(String chargeUnit) {
		this.chargeUnit = chargeUnit;
	}

	public List<SurfaceChemistryBean> getSurfaceChemistries() {
		return this.surfaceChemistries;
	}

	public void setSurfaceChemistries(List<SurfaceChemistryBean> surfaceGroups) {
		this.surfaceChemistries = surfaceGroups;
	}

	public String getSurfaceAreaUnit() {
		return this.surfaceAreaUnit;
	}

	public void setSurfaceAreaUnit(String surfaceAreaUnit) {
		this.surfaceAreaUnit = surfaceAreaUnit;
	}

	public void updateDomainObj(Surface surface) {
		super.updateDomainObj(surface);
		if (this.isHydrophobic == null || this.isHydrophobic.length() == 0) {
			surface.setIsHydrophobic(null);
		} else {
			boolean hycrophobicStatus = (this.isHydrophobic
					.equalsIgnoreCase(CaNanoLabConstants.BOOLEAN_YES)) ? true
					: false;
			surface.setIsHydrophobic(hycrophobicStatus);
		}
		if ((this.charge == null) || (this.charge.length() == 0)) {
			surface.setCharge(null);
		} else {
			surface.setCharge(new Measurement(new Float(this.charge), this.chargeUnit));
		}
		// surface.setZetaPotential(new Measurement(zetaPotential, "mV"));
		if ((this.zetaPotential == null) || (this.zetaPotential.length() == 0)) {
			surface.setZetaPotential(null);
		} else {
			surface.setZetaPotential(new Measurement(new Float(this.zetaPotential),
					this.zetaPotentialUnit));
		}

		if ((this.surfaceArea == null) || (this.surfaceArea.length() == 0)) {
			surface.setSurfaceArea(null);
		} else {
			surface.setSurfaceArea(new Measurement(new Float(this.surfaceArea),
					this.surfaceAreaUnit));
		}

		// copy collection
		List<SurfaceChemistry> doChemList = new ArrayList<SurfaceChemistry>(
				surface.getSurfaceChemistryCollection());
		// clear collection
		surface.getSurfaceChemistryCollection().clear();
		for (SurfaceChemistryBean surfaceChemistry : this.surfaceChemistries) {
			SurfaceChemistry doSurfaceChemistry = null;
			if (surfaceChemistry.getId() == null) {
				doSurfaceChemistry = new SurfaceChemistry();
			} else {
				// find domain object with the same ID and add the updated
				// domain object
				for (SurfaceChemistry doChem : doChemList) {
					if (doChem.getId().equals(
							new Long(surfaceChemistry.getId()))) {
						doSurfaceChemistry = doChem;
						break;
					}
				}
			}
			surfaceChemistry.updateDomainObj(doSurfaceChemistry);
			surface.getSurfaceChemistryCollection().add(doSurfaceChemistry);
		}
	}

	public String getZetaPotentialUnit() {
		return this.zetaPotentialUnit;
	}

	public void setZetaPotentialUnit(String zetaPotentialUnit) {
		this.zetaPotentialUnit = zetaPotentialUnit;
	}
}

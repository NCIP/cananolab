package gov.nih.nci.calab.dto.characterization.physical;

import gov.nih.nci.calab.domain.Measurement;
import gov.nih.nci.calab.domain.nano.characterization.physical.Solubility;
import gov.nih.nci.calab.dto.characterization.CharacterizationBean;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.service.util.StringUtils;

/**
 * This class represents the Solubility characterization information to be shown
 * in the view page.
 * 
 * @author chande
 * 
 */
public class SolubilityBean extends CharacterizationBean {
	private String solvent;

	private String criticalConcentration;

	private String criticalConcentrationUnit;

	private String isSoluble;

	public SolubilityBean() {
		super();
	}

	/**
	 * User to create a new solubilityBean from partial solubilityBean and a
	 * characterizationBean
	 * 
	 * @param solubilityBean
	 * @param charBean
	 */
	public SolubilityBean(SolubilityBean solubilityPropBean,
			CharacterizationBean charBean) {
		super(charBean);
		this.solvent=solubilityPropBean.getSolvent();
		this.criticalConcentration=solubilityPropBean.getCriticalConcentration();
		this.criticalConcentrationUnit=solubilityPropBean.getCriticalConcentrationUnit();
		this.isSoluble=solubilityPropBean.getIsSoluble();		
	}

	public SolubilityBean(Solubility aChar) {
		super(aChar);

		this.solvent = aChar.getSolvent();
		if (aChar.getCriticalConcentration() != null) {
			this.criticalConcentration = StringUtils.convertToString(aChar
					.getCriticalConcentration().getValue());
			this.criticalConcentrationUnit = aChar.getCriticalConcentration()
					.getUnitOfMeasurement();
		}
		this.isSoluble = aChar.getIsSoluble() ? "true" : "false";
	}

	public void updateDomainObj(Solubility solubility) {
		super.updateDomainObj(solubility);

		solubility.setSolvent(this.solvent);
		solubility.setCriticalConcentration(new Measurement(new Float(
				this.criticalConcentration), this.criticalConcentrationUnit));
		solubility.setIsSoluble((this.isSoluble
				.equalsIgnoreCase(CaNanoLabConstants.BOOLEAN_YES)) ? true
				: false);
	}

	public String getCriticalConcentration() {
		return criticalConcentration;
	}

	public void setCriticalConcentration(String criticalConcentration) {
		this.criticalConcentration = criticalConcentration;
	}

	public String getIsSoluble() {
		return isSoluble;
	}

	public void setIsSoluble(String isSoluble) {
		this.isSoluble = isSoluble;
	}

	public String getSolvent() {
		return solvent;
	}

	public void setSolvent(String solvent) {
		this.solvent = solvent;
	}

	public String getCriticalConcentrationUnit() {
		return criticalConcentrationUnit;
	}

	public void setCriticalConcentrationUnit(String criticalConcentrationUnit) {
		this.criticalConcentrationUnit = criticalConcentrationUnit;
	}
}

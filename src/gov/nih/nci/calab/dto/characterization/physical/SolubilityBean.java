package gov.nih.nci.calab.dto.characterization.physical;

import gov.nih.nci.calab.domain.Measurement;
import gov.nih.nci.calab.domain.nano.characterization.physical.Solubility;
import gov.nih.nci.calab.dto.characterization.CharacterizationBean;
import gov.nih.nci.calab.dto.characterization.DerivedBioAssayDataBean;
import gov.nih.nci.calab.service.util.CananoConstants;

import java.util.List;


/**
 * This class represents the Solubility characterization information to be shown in
 * the view page.
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
		initSetup();
	}
	
	public SolubilityBean(Solubility aChar) {
		super(aChar);

		this.solvent = aChar.getSolvent();
		if (aChar.getCriticalConcentration() != null) {
			this.criticalConcentration = aChar.getCriticalConcentration().getValue();
			this.criticalConcentrationUnit = aChar.getCriticalConcentration().getUnitOfMeasurement();
		}
		this.isSoluble = aChar.getIsSoluble() ? "true" : "false";
	}
	
	public void setDerivedBioAssayDataList(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayDataList(derivedBioAssayData);
		initSetup();
	}
	
	public void initSetup() {
		/*
		for (DerivedBioAssayDataBean table:getDerivedBioAssayData()) {
			DatumBean average=new DatumBean();
			average.setType("Average");
			average.setValueUnit("nm");
			DatumBean zaverage=new DatumBean();
			zaverage.setType("Z-Average");
			zaverage.setValueUnit("nm");
			DatumBean pdi=new DatumBean();
			pdi.setType("PDI");
			table.getDatumList().add(average);
			table.getDatumList().add(zaverage);
			table.getDatumList().add(pdi);
		}
		*/
	}
	
	public Solubility getDomainObj() {
		Solubility solubility = new Solubility();
		super.updateDomainObj(solubility);
		
		solubility.setSolvent(this.criticalConcentration);
		solubility.setCriticalConcentration(new Measurement(this.criticalConcentration, this.criticalConcentrationUnit));
		solubility.setIsSoluble((this.isSoluble.equalsIgnoreCase(CananoConstants.BOOLEAN_YES)) ? true : false);
		
		return solubility;
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

package gov.nih.nci.calab.dto.characterization.invitro;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.invitro.CytokineInduction;

import gov.nih.nci.calab.dto.characterization.*;

import java.util.List;


/**
 * This class represents the cytokineInduction characterization information to be shown in
 * the view page.
 * 
 * @author beasleyj
 * 
 */
public class CytokineInductionBean extends CharacterizationBean {
	public CytokineInductionBean() {
		super();
		initSetup();
	}
	
	public CytokineInductionBean(Characterization aChar) {
		super(aChar);
	}
	
	public void initSetup() {
		for (DerivedBioAssayDataBean table: getDerivedBioAssayData()) {
			DatumBean cytokineConcentration=new DatumBean();
			cytokineConcentration.setType("Cytokine Concentration");
			table.getDatumList().add(cytokineConcentration);
		}
	}
	
	public void setDerivedBioAssayData(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayData(derivedBioAssayData);
		
		for (DerivedBioAssayDataBean table:getDerivedBioAssayData()) {
			DatumBean cytokineConcentration=new DatumBean();
			cytokineConcentration.setType("Cytokine Concentration");
			cytokineConcentration.setValueUnit("%");
			table.getDatumList().add(cytokineConcentration);
		}
	}
	
	public CytokineInduction getDomainObj() {
		CytokineInduction cytokineInduction = new CytokineInduction();
		super.updateDomainObj(cytokineInduction);
		return cytokineInduction;
	}
}

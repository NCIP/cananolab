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
	}
	
	public CytokineInductionBean(Characterization aChar) {
		super(aChar);
	}
	
	public void setDerivedBioAssayDataList(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayDataList(derivedBioAssayData);

		for (DerivedBioAssayDataBean table : getDerivedBioAssayDataList()) {
			for (DatumBean datum : table.getDatumList()) {
				datum.setType("Cytokine Concentration");
				datum.setValueUnit("g/ml");
			}
		}
	}
	
	public CytokineInduction getDomainObj() {
		CytokineInduction cytokineInduction = new CytokineInduction();
		super.updateDomainObj(cytokineInduction);
		return cytokineInduction;
	}
}

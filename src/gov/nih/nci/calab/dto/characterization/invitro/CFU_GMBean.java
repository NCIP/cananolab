package gov.nih.nci.calab.dto.characterization.invitro;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.invitro.CFU_GM;

import gov.nih.nci.calab.dto.characterization.*;

import java.util.List;


/**
 * This class represents the cfu_gm characterization information to be shown in
 * the view page.
 * 
 * @author beasleyj
 * 
 */
public class CFU_GMBean extends CharacterizationBean {
	public CFU_GMBean() {
		super();		
	}
	
	public CFU_GMBean(Characterization aChar) {
		super(aChar);
	}
	
	public void setDerivedBioAssayDataList(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayDataList(derivedBioAssayData);
				
		for (DerivedBioAssayDataBean table:getDerivedBioAssayDataList()) {
			for (DatumBean datum : table.getDatumList()) {
				datum.setType("CFU_GM");
				datum.setValueUnit("CFU");
			}			
		}
	}
	
	public CFU_GM getDomainObj() {
		CFU_GM cfu_gm = new CFU_GM();
		super.updateDomainObj(cfu_gm);
		return cfu_gm;
	}
}

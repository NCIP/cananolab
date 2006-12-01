package gov.nih.nci.calab.dto.characterization.invitro;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.invitro.OxidativeBurst;

import gov.nih.nci.calab.dto.characterization.*;

import java.util.List;


/**
 * This class represents the oxidativeBurst characterization information to be shown in
 * the view page.
 * 
 * @author beasleyj
 * 
 */
public class OxidativeBurstBean extends CharacterizationBean {
	public OxidativeBurstBean() {
		super();
	}
	
	public OxidativeBurstBean(Characterization aChar) {
		super(aChar);
	}
	
	public void setDerivedBioAssayDataList(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayDataList(derivedBioAssayData);

		for (DerivedBioAssayDataBean table : getDerivedBioAssayDataList()) {
			for (DatumBean datum : table.getDatumList()) {
				datum.setType("Percent Oxidative Burst");
				datum.setValueUnit("%");
			}
		}
	}
	
	public OxidativeBurst getDomainObj() {
		OxidativeBurst oxidativeBurst = new OxidativeBurst();
		super.updateDomainObj(oxidativeBurst);
		return oxidativeBurst;
	}
}

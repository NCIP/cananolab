package gov.nih.nci.calab.dto.characterization.invitro;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.invitro.OxidativeStress;

import gov.nih.nci.calab.dto.characterization.*;

import java.util.List;


/**
 * This class represents the oxidativeStress characterization information to be shown in
 * the view page.
 * 
 * @author beasleyj
 * 
 */
public class OxidativeStressBean extends CharacterizationBean {
	public OxidativeStressBean() {
		super();
	}
	
	public OxidativeStressBean(Characterization aChar) {
		super(aChar);
	}
	
	public void setDerivedBioAssayDataList(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayDataList(derivedBioAssayData);

		for (DerivedBioAssayDataBean table : getDerivedBioAssayDataList()) {
			for (DatumBean datum : table.getDatumList()) {
				datum.setType("Percent Oxidative Stress");
				datum.setValueUnit("%");
			}
		}
	}
	
	public OxidativeStress getDomainObj() {
		OxidativeStress oxidativeStress = new OxidativeStress();
		super.updateDomainObj(oxidativeStress);
		return oxidativeStress;
	}
}

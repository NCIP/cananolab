package gov.nih.nci.calab.dto.characterization.invitro;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.invitro.LeukocyteProliferation;

import gov.nih.nci.calab.dto.characterization.*;

import java.util.List;


/**
 * This class represents the leukocyteProliferation characterization information to be shown in
 * the view page.
 * 
 * @author beasleyj
 * 
 */
public class LeukocyteProliferationBean extends CharacterizationBean {
	public LeukocyteProliferationBean() {
		super();
	}
	
	public LeukocyteProliferationBean(Characterization aChar) {
		super(aChar);
	}
	
	public void setDerivedBioAssayDataList(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayDataList(derivedBioAssayData);

		for (DerivedBioAssayDataBean table : getDerivedBioAssayDataList()) {
			for (DatumBean datum : table.getDatumList()) {
				datum.setType("Percent Proliferation");
				datum.setValueUnit("%");
			}
		}
	}
	
	public LeukocyteProliferation getDomainObj() {
		LeukocyteProliferation leukocyteProliferation = new LeukocyteProliferation();
		super.updateDomainObj(leukocyteProliferation);
		return leukocyteProliferation;
	}
}

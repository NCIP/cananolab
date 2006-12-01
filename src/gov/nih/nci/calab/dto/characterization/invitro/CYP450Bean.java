package gov.nih.nci.calab.dto.characterization.invitro;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.invitro.CYP450;

import gov.nih.nci.calab.dto.characterization.*;

import java.util.List;


/**
 * This class represents the cyp450 characterization information to be shown in
 * the view page.
 * 
 * @author beasleyj
 * 
 */
public class CYP450Bean extends CharacterizationBean {
	public CYP450Bean() {
		super();
	}
	
	public CYP450Bean(Characterization aChar) {
		super(aChar);
	}
	
	public void setDerivedBioAssayDataList(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayDataList(derivedBioAssayData);

		for (DerivedBioAssayDataBean table : getDerivedBioAssayDataList()) {
			for (DatumBean datum : table.getDatumList()) {
				datum.setType("Percent CYP450");
				datum.setValueUnit("%");
			}
		}
	}
	
	public CYP450 getDomainObj() {
		CYP450 cyp450 = new CYP450();
		super.updateDomainObj(cyp450);
		return cyp450;
	}
}

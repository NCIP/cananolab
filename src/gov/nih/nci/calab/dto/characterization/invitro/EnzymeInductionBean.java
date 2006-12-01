package gov.nih.nci.calab.dto.characterization.invitro;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.invitro.EnzymeInduction;

import gov.nih.nci.calab.dto.characterization.*;

import java.util.List;


/**
 * This class represents the enzymeInduction characterization information to be shown in
 * the view page.
 * 
 * @author beasleyj
 * 
 */
public class EnzymeInductionBean extends CharacterizationBean {
	public EnzymeInductionBean() {
		super();
	}
	
	public EnzymeInductionBean(Characterization aChar) {
		super(aChar);
	}
	
	public void setDerivedBioAssayDataList(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayDataList(derivedBioAssayData);

		for (DerivedBioAssayDataBean table : getDerivedBioAssayDataList()) {
			for (DatumBean datum : table.getDatumList()) {
				datum.setType("Percent Enzyme Induction");
				datum.setValueUnit("%");
			}
		}
	}
	
	public EnzymeInduction getDomainObj() {
		EnzymeInduction enzymeInduction = new EnzymeInduction();
		super.updateDomainObj(enzymeInduction);
		return enzymeInduction;
	}
}

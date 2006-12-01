package gov.nih.nci.calab.dto.characterization.invitro;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.invitro.Coagulation;

import gov.nih.nci.calab.dto.characterization.*;

import java.util.List;


/**
 * This class represents the coagulation characterization information to be shown in
 * the view page.
 * 
 * @author beasleyj
 * 
 */
public class CoagulationBean extends CharacterizationBean {
	public CoagulationBean() {
		super();
	}
	
	public CoagulationBean(Characterization aChar) {
		super(aChar);
	}
	
	public void setDerivedBioAssayDataList(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayDataList(derivedBioAssayData);
		
		for (DerivedBioAssayDataBean table:getDerivedBioAssayDataList()) {
			for (DatumBean datum : table.getDatumList()) {
				datum.setType("Coagulation Time");
				datum.setValueUnit("seconds");
			}			
		}
	}
	
	public Coagulation getDomainObj() {
		Coagulation coagulation = new Coagulation();
		super.updateDomainObj(coagulation);
		return coagulation;
	}
}

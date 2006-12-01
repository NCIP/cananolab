package gov.nih.nci.calab.dto.characterization.invitro;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.invitro.Chemotaxis;

import gov.nih.nci.calab.dto.characterization.*;

import java.util.List;


/**
 * This class represents the chemotaxis characterization information to be shown in
 * the view page.
 * 
 * @author beasleyj
 * 
 */
public class ChemotaxisBean extends CharacterizationBean {
	public ChemotaxisBean() {
		super();
	}
	
	public ChemotaxisBean(Characterization aChar) {
		super(aChar);
	}
	
	public void setDerivedBioAssayDataList(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayDataList(derivedBioAssayData);
		
		for (DerivedBioAssayDataBean table:getDerivedBioAssayDataList()) {
			for (DatumBean datum : table.getDatumList()) {
				datum.setType("Relative Fluorescent Values");
				datum.setValueUnit("RFU");
			}			
		}
	}
	
	public Chemotaxis getDomainObj() {
		Chemotaxis chemotaxis = new Chemotaxis();
		super.updateDomainObj(chemotaxis);
		return chemotaxis;
	}
}

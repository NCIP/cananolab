package gov.nih.nci.calab.dto.characterization.invitro;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.invitro.PlasmaProteinBinding;

import gov.nih.nci.calab.dto.characterization.*;

import java.util.List;


/**
 * This class represents the plasmaProteinBinding characterization information to be shown in
 * the view page.
 * 
 * @author beasleyj
 * 
 */
public class PlasmaProteinBindingBean extends CharacterizationBean {
	public PlasmaProteinBindingBean() {
		super();
	}
	
	public PlasmaProteinBindingBean(Characterization aChar) {
		super(aChar);
	}
	
	public void setDerivedBioAssayDataList(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayDataList(derivedBioAssayData);

		for (DerivedBioAssayDataBean table : getDerivedBioAssayDataList()) {
			for (DatumBean datum : table.getDatumList()) {
				datum.setType("Percent Plasma Protein Binding");
				datum.setValueUnit("%");
			}
		}
	}
	
	public PlasmaProteinBinding getDomainObj() {
		PlasmaProteinBinding plasmaProteinBinding = new PlasmaProteinBinding();
		super.updateDomainObj(plasmaProteinBinding);
		return plasmaProteinBinding;
	}
}

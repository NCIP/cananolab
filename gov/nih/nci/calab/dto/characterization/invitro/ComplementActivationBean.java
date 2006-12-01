package gov.nih.nci.calab.dto.characterization.invitro;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.invitro.ComplementActivation;

import gov.nih.nci.calab.dto.characterization.*;

import java.util.List;


/**
 * This class represents the complementActivation characterization information to be shown in
 * the view page.
 * 
 * @author beasleyj
 * 
 */
public class ComplementActivationBean extends CharacterizationBean {
	public ComplementActivationBean() {
		super();
	}
	
	public ComplementActivationBean(Characterization aChar) {
		super(aChar);
	}
	
	public void setDerivedBioAssayDataList(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayDataList(derivedBioAssayData);

		for (DerivedBioAssayDataBean table : getDerivedBioAssayDataList()) {
			for (DatumBean datum : table.getDatumList()) {
				datum.setType("Percent Complement Activation");
				datum.setValueUnit("%");
			}
		}
	}
	
	public ComplementActivation getDomainObj() {
		ComplementActivation complementActivation = new ComplementActivation();
		super.updateDomainObj(complementActivation);
		return complementActivation;
	}
}

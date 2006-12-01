package gov.nih.nci.calab.dto.characterization.invitro;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.invitro.GlucuronidationSulphation;

import gov.nih.nci.calab.dto.characterization.*;

import java.util.List;


/**
 * This class represents the glucuronidationSulphation characterization information to be shown in
 * the view page.
 * 
 * @author beasleyj
 * 
 */
public class GlucuronidationSulphationBean extends CharacterizationBean {
	public GlucuronidationSulphationBean() {
		super();
	}
	
	public GlucuronidationSulphationBean(Characterization aChar) {
		super(aChar);
	}
	
	public void setDerivedBioAssayDataList(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayDataList(derivedBioAssayData);

		for (DerivedBioAssayDataBean table : getDerivedBioAssayDataList()) {
			for (DatumBean datum : table.getDatumList()) {
				datum.setType("Percent Glucuronidation Sulphation");
				datum.setValueUnit("%");
			}
		}
	}
	
	public GlucuronidationSulphation getDomainObj() {
		GlucuronidationSulphation glucuronidationSulphation = new GlucuronidationSulphation();
		super.updateDomainObj(glucuronidationSulphation);
		return glucuronidationSulphation;
	}
}

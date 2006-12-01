package gov.nih.nci.calab.dto.characterization.invitro;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.invitro.NKCellCytotoxicActivity;

import gov.nih.nci.calab.dto.characterization.*;

import java.util.List;


/**
 * This class represents the nkCellCytotoxicActivity characterization information to be shown in
 * the view page.
 * 
 * @author beasleyj
 * 
 */
public class NKCellCytotoxicActivityBean extends CharacterizationBean {
	public NKCellCytotoxicActivityBean() {
		super();
	}
	
	public NKCellCytotoxicActivityBean(Characterization aChar) {
		super(aChar);
	}
	
	public void setDerivedBioAssayDataList(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayDataList(derivedBioAssayData);

		for (DerivedBioAssayDataBean table : getDerivedBioAssayDataList()) {
			for (DatumBean datum : table.getDatumList()) {
				datum.setType("Percent NKCellCytotoxicActivity");
				datum.setValueUnit("%");
			}
		}
	}
	
	public NKCellCytotoxicActivity getDomainObj() {
		NKCellCytotoxicActivity nkCellCytotoxicActivity = new NKCellCytotoxicActivity();
		super.updateDomainObj(nkCellCytotoxicActivity);
		return nkCellCytotoxicActivity;
	}
}

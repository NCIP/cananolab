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
		initSetup();
	}
	
	public NKCellCytotoxicActivityBean(Characterization aChar) {
		super(aChar);
	}
	
	public void initSetup() {
		for (DerivedBioAssayDataBean table: getDerivedBioAssayData()) {
			DatumBean percentNKCellCytotoxicActivity=new DatumBean();
			percentNKCellCytotoxicActivity.setType("Percent NKCellCytotoxicActivity");
			percentNKCellCytotoxicActivity.setValueUnit("%");
			table.getDatumList().add(percentNKCellCytotoxicActivity);
		}
	}
	
	public void setDerivedBioAssayData(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayData(derivedBioAssayData);
		
		for (DerivedBioAssayDataBean table:getDerivedBioAssayData()) {
			DatumBean percentNKCellCytotoxicActivity=new DatumBean();
			percentNKCellCytotoxicActivity.setType("Percent NKCellCytotoxicActivity");
			percentNKCellCytotoxicActivity.setValueUnit("%");
			table.getDatumList().add(percentNKCellCytotoxicActivity);
		}
	}
	
	public NKCellCytotoxicActivity getDomainObj() {
		NKCellCytotoxicActivity nkCellCytotoxicActivity = new NKCellCytotoxicActivity();
		super.updateDomainObj(nkCellCytotoxicActivity);
		return nkCellCytotoxicActivity;
	}
}

package gov.nih.nci.calab.dto.characterization.invitro;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.invitro.CytokineInduction;

import gov.nih.nci.calab.dto.characterization.*;

import java.util.List;


/**
 * This class represents the cytokineInduction characterization information to be shown in
 * the view page.
 * 
 * @author beasleyj
 * 
 */
public class CytokineInductionBean extends CharacterizationBean {
	public CytokineInductionBean() {
		super();
		initSetup();
	}
	
	public CytokineInductionBean(Characterization aChar) {
		super(aChar);
	}
	
	public void initSetup() {
		for (DerivedBioAssayDataBean table: getDerivedBioAssayData()) {
			DatumBean percentCytokineInduction=new DatumBean();
			percentCytokineInduction.setType("Percent Cytokine Induction");
			percentCytokineInduction.setValueUnit("%");
			table.getDatumList().add(percentCytokineInduction);
		}
	}
	
	public void setDerivedBioAssayData(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayData(derivedBioAssayData);
		
		for (DerivedBioAssayDataBean table:getDerivedBioAssayData()) {
			DatumBean percentCytokineInduction=new DatumBean();
			percentCytokineInduction.setType("Percent CytokineInduction");
			percentCytokineInduction.setValueUnit("%");
			table.getDatumList().add(percentCytokineInduction);
		}
	}
	
	public CytokineInduction getDomainObj() {
		CytokineInduction cytokineInduction = new CytokineInduction();
		super.updateDomainObj(cytokineInduction);
		return cytokineInduction;
	}
}

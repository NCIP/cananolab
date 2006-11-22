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
		initSetup();
	}
	
	public EnzymeInductionBean(Characterization aChar) {
		super(aChar);
	}
	
	public void initSetup() {
		for (DerivedBioAssayDataBean table: getDerivedBioAssayDataList()) {
			DatumBean percentEnzymeInduction=new DatumBean();
			percentEnzymeInduction.setType("Percent EnzymeInduction");
			percentEnzymeInduction.setValueUnit("%");
			table.getDatumList().add(percentEnzymeInduction);
		}
	}
	
	public void setDerivedBioAssayDataList(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayDataList(derivedBioAssayData);
		
		for (DerivedBioAssayDataBean table:getDerivedBioAssayDataList()) {
			DatumBean percentEnzymeInduction=new DatumBean();
			percentEnzymeInduction.setType("Percent EnzymeInduction");
			percentEnzymeInduction.setValueUnit("%");
			table.getDatumList().add(percentEnzymeInduction);
		}
	}
	
	public EnzymeInduction getDomainObj() {
		EnzymeInduction enzymeInduction = new EnzymeInduction();
		super.updateDomainObj(enzymeInduction);
		return enzymeInduction;
	}
}

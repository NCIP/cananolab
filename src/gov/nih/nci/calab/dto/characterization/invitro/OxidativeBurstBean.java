package gov.nih.nci.calab.dto.characterization.invitro;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.invitro.OxidativeBurst;

import gov.nih.nci.calab.dto.characterization.*;

import java.util.List;


/**
 * This class represents the oxidativeBurst characterization information to be shown in
 * the view page.
 * 
 * @author beasleyj
 * 
 */
public class OxidativeBurstBean extends CharacterizationBean {
	public OxidativeBurstBean() {
		super();
		initSetup();
	}
	
	public OxidativeBurstBean(Characterization aChar) {
		super(aChar);
	}
	
	public void initSetup() {
		for (DerivedBioAssayDataBean table: getDerivedBioAssayData()) {
			DatumBean percentOxidativeBurst=new DatumBean();
			percentOxidativeBurst.setType("Percent Oxidative Burst");
			percentOxidativeBurst.setValueUnit("%");
			table.getDatumList().add(percentOxidativeBurst);
		}
	}
	
	public void setDerivedBioAssayData(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayData(derivedBioAssayData);
		
		for (DerivedBioAssayDataBean table:getDerivedBioAssayData()) {
			DatumBean percentOxidativeBurst=new DatumBean();
			percentOxidativeBurst.setType("Percent Oxidative Burst");
			percentOxidativeBurst.setValueUnit("%");
			table.getDatumList().add(percentOxidativeBurst);
		}
	}
	
	public OxidativeBurst getDomainObj() {
		OxidativeBurst oxidativeBurst = new OxidativeBurst();
		super.updateDomainObj(oxidativeBurst);
		return oxidativeBurst;
	}
}

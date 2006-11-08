package gov.nih.nci.calab.dto.characterization.invitro;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.invitro.OxidativeStress;

import gov.nih.nci.calab.dto.characterization.*;

import java.util.List;


/**
 * This class represents the oxidativeStress characterization information to be shown in
 * the view page.
 * 
 * @author beasleyj
 * 
 */
public class OxidativeStressBean extends CharacterizationBean {
	public OxidativeStressBean() {
		super();
		initSetup();
	}
	
	public OxidativeStressBean(Characterization aChar) {
		super(aChar);
	}
	
	public void initSetup() {
		for (DerivedBioAssayDataBean table: getDerivedBioAssayData()) {
			DatumBean percentOxidativeStress=new DatumBean();
			percentOxidativeStress.setType("Percent OxidativeStress");
			percentOxidativeStress.setValueUnit("%");
			table.getDatumList().add(percentOxidativeStress);
		}
	}
	
	public void setDerivedBioAssayData(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayData(derivedBioAssayData);
		
		for (DerivedBioAssayDataBean table:getDerivedBioAssayData()) {
			DatumBean percentOxidativeStress=new DatumBean();
			percentOxidativeStress.setType("Percent OxidativeStress");
			percentOxidativeStress.setValueUnit("%");
			table.getDatumList().add(percentOxidativeStress);
		}
	}
	
	public OxidativeStress getDomainObj() {
		OxidativeStress oxidativeStress = new OxidativeStress();
		super.updateDomainObj(oxidativeStress);
		return oxidativeStress;
	}
}

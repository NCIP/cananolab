package gov.nih.nci.calab.dto.characterization.invitro;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.invitro.CFU_GM;

import gov.nih.nci.calab.dto.characterization.*;

import java.util.List;


/**
 * This class represents the cfu_gm characterization information to be shown in
 * the view page.
 * 
 * @author beasleyj
 * 
 */
public class CFU_GMBean extends CharacterizationBean {
	public CFU_GMBean() {
		super();
		initSetup();
	}
	
	public CFU_GMBean(Characterization aChar) {
		super(aChar);
	}
	
	public void initSetup() {
		for (DerivedBioAssayDataBean table: getDerivedBioAssayData()) {
			DatumBean percentCFU_GM=new DatumBean();
			percentCFU_GM.setType("Percent CFU_GM");
			percentCFU_GM.setValueUnit("%");
			table.getDatumList().add(percentCFU_GM);
		}
	}
	
	public void setDerivedBioAssayData(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayData(derivedBioAssayData);
		
		for (DerivedBioAssayDataBean table:getDerivedBioAssayData()) {
			DatumBean percentCFU_GM=new DatumBean();
			percentCFU_GM.setType("Percent CFU_GM");
			percentCFU_GM.setValueUnit("%");
			table.getDatumList().add(percentCFU_GM);
		}
	}
	
	public CFU_GM getDomainObj() {
		CFU_GM cfu_gm = new CFU_GM();
		super.updateDomainObj(cfu_gm);
		return cfu_gm;
	}
}

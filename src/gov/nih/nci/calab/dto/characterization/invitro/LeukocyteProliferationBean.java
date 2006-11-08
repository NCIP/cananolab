package gov.nih.nci.calab.dto.characterization.invitro;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.invitro.LeukocyteProliferation;

import gov.nih.nci.calab.dto.characterization.*;

import java.util.List;


/**
 * This class represents the leukocyteProliferation characterization information to be shown in
 * the view page.
 * 
 * @author beasleyj
 * 
 */
public class LeukocyteProliferationBean extends CharacterizationBean {
	public LeukocyteProliferationBean() {
		super();
		initSetup();
	}
	
	public LeukocyteProliferationBean(Characterization aChar) {
		super(aChar);
	}
	
	public void initSetup() {
		for (DerivedBioAssayDataBean table: getDerivedBioAssayData()) {
			DatumBean percentLeukocyteProliferation=new DatumBean();
			percentLeukocyteProliferation.setType("Percent LeukocyteProliferation");
			percentLeukocyteProliferation.setValueUnit("%");
			table.getDatumList().add(percentLeukocyteProliferation);
		}
	}
	
	public void setDerivedBioAssayData(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayData(derivedBioAssayData);
		
		for (DerivedBioAssayDataBean table:getDerivedBioAssayData()) {
			DatumBean percentLeukocyteProliferation=new DatumBean();
			percentLeukocyteProliferation.setType("Percent LeukocyteProliferation");
			percentLeukocyteProliferation.setValueUnit("%");
			table.getDatumList().add(percentLeukocyteProliferation);
		}
	}
	
	public LeukocyteProliferation getDomainObj() {
		LeukocyteProliferation leukocyteProliferation = new LeukocyteProliferation();
		super.updateDomainObj(leukocyteProliferation);
		return leukocyteProliferation;
	}
}

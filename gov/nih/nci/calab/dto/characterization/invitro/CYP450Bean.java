package gov.nih.nci.calab.dto.characterization.invitro;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.invitro.CYP450;

import gov.nih.nci.calab.dto.characterization.*;

import java.util.List;


/**
 * This class represents the cyp450 characterization information to be shown in
 * the view page.
 * 
 * @author beasleyj
 * 
 */
public class CYP450Bean extends CharacterizationBean {
	public CYP450Bean() {
		super();
		initSetup();
	}
	
	public CYP450Bean(Characterization aChar) {
		super(aChar);
	}
	
	public void initSetup() {
		for (DerivedBioAssayDataBean table: getDerivedBioAssayData()) {
			DatumBean percentCYP450=new DatumBean();
			percentCYP450.setType("Percent CYP450");
			percentCYP450.setValueUnit("%");
			table.getDatumList().add(percentCYP450);
		}
	}
	
	public void setDerivedBioAssayData(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayData(derivedBioAssayData);
		
		for (DerivedBioAssayDataBean table:getDerivedBioAssayData()) {
			DatumBean percentCYP450=new DatumBean();
			percentCYP450.setType("Percent CYP450");
			percentCYP450.setValueUnit("%");
			table.getDatumList().add(percentCYP450);
		}
	}
	
	public CYP450 getDomainObj() {
		CYP450 cyp450 = new CYP450();
		super.updateDomainObj(cyp450);
		return cyp450;
	}
}

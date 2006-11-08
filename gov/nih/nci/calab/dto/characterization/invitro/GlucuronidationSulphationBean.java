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
		initSetup();
	}
	
	public GlucuronidationSulphationBean(Characterization aChar) {
		super(aChar);
	}
	
	public void initSetup() {
		for (DerivedBioAssayDataBean table: getDerivedBioAssayData()) {
			DatumBean percentGlucuronidationSulphation=new DatumBean();
			percentGlucuronidationSulphation.setType("Percent GlucuronidationSulphation");
			percentGlucuronidationSulphation.setValueUnit("%");
			table.getDatumList().add(percentGlucuronidationSulphation);
		}
	}
	
	public void setDerivedBioAssayData(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayData(derivedBioAssayData);
		
		for (DerivedBioAssayDataBean table:getDerivedBioAssayData()) {
			DatumBean percentGlucuronidationSulphation=new DatumBean();
			percentGlucuronidationSulphation.setType("Percent GlucuronidationSulphation");
			percentGlucuronidationSulphation.setValueUnit("%");
			table.getDatumList().add(percentGlucuronidationSulphation);
		}
	}
	
	public GlucuronidationSulphation getDomainObj() {
		GlucuronidationSulphation glucuronidationSulphation = new GlucuronidationSulphation();
		super.updateDomainObj(glucuronidationSulphation);
		return glucuronidationSulphation;
	}
}

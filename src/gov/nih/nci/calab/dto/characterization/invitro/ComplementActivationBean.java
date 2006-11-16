package gov.nih.nci.calab.dto.characterization.invitro;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.invitro.ComplementActivation;

import gov.nih.nci.calab.dto.characterization.*;

import java.util.List;


/**
 * This class represents the complementActivation characterization information to be shown in
 * the view page.
 * 
 * @author beasleyj
 * 
 */
public class ComplementActivationBean extends CharacterizationBean {
	public ComplementActivationBean() {
		super();
		initSetup();
	}
	
	public ComplementActivationBean(Characterization aChar) {
		super(aChar);
	}
	
	public void initSetup() {
		for (DerivedBioAssayDataBean table: getDerivedBioAssayData()) {
			DatumBean percentComplementActivation=new DatumBean();
			percentComplementActivation.setType("Percent Complement Activation");
			percentComplementActivation.setValueUnit("%");
			table.getDatumList().add(percentComplementActivation);
		}
	}
	
	public void setDerivedBioAssayData(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayData(derivedBioAssayData);
		
		for (DerivedBioAssayDataBean table:getDerivedBioAssayData()) {
			DatumBean percentComplementActivation=new DatumBean();
			percentComplementActivation.setType("Percent Complement Activation");
			percentComplementActivation.setValueUnit("%");
			table.getDatumList().add(percentComplementActivation);
		}
	}
	
	public ComplementActivation getDomainObj() {
		ComplementActivation complementActivation = new ComplementActivation();
		super.updateDomainObj(complementActivation);
		return complementActivation;
	}
}

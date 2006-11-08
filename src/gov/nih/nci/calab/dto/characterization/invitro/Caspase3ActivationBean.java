package gov.nih.nci.calab.dto.characterization.invitro;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.invitro.Caspase3Activation;

import gov.nih.nci.calab.dto.characterization.*;

import java.util.List;


/**
 * This class represents the caspase3Activation characterization information to be shown in
 * the view page.
 * 
 * @author beasleyj
 * 
 */
public class Caspase3ActivationBean extends CharacterizationBean {
	public Caspase3ActivationBean() {
		super();
		initSetup();
	}
	
	public Caspase3ActivationBean(Characterization aChar) {
		super(aChar);
	}
	
	public void initSetup() {
		for (DerivedBioAssayDataBean table: getDerivedBioAssayData()) {
			DatumBean percentCaspase3Activation=new DatumBean();
			percentCaspase3Activation.setType("Percent Caspase 3 Activation");
			percentCaspase3Activation.setValueUnit("%");
			table.getDatumList().add(percentCaspase3Activation);
		}
	}
	
	public void setDerivedBioAssayData(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayData(derivedBioAssayData);
		
		for (DerivedBioAssayDataBean table:getDerivedBioAssayData()) {
			DatumBean percentCaspase3Activation=new DatumBean();
			percentCaspase3Activation.setType("Percent Caspase 3 Activation");
			percentCaspase3Activation.setValueUnit("%");
			table.getDatumList().add(percentCaspase3Activation);
		}
	}
	
	public Caspase3Activation getDomainObj() {
		Caspase3Activation caspase3Activation = new Caspase3Activation();
		super.updateDomainObj(caspase3Activation);
		return caspase3Activation;
	}
}

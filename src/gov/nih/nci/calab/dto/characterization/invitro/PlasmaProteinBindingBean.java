package gov.nih.nci.calab.dto.characterization.invitro;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.invitro.PlasmaProteinBinding;

import gov.nih.nci.calab.dto.characterization.*;

import java.util.List;


/**
 * This class represents the ProteinBinding characterization information to be shown in
 * the view page.
 * 
 * @author beasleyj
 * 
 */
public class PlasmaProteinBindingBean extends CharacterizationBean {
	public PlasmaProteinBindingBean() {
		super();
		initSetup();
	}
	
	public PlasmaProteinBindingBean(Characterization aChar) {
		super(aChar);
	}

	public void initSetup() {
		for (DerivedBioAssayDataBean table: getDerivedBioAssayData()) {
			DatumBean proteinBinding=new DatumBean();
			proteinBinding.setType("Percent Protein Binding");
			proteinBinding.setValueUnit("%");
			table.getDatumList().add(proteinBinding);
		}
	}
	
	public void setDerivedBioAssayData(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayData(derivedBioAssayData);
		
		for (DerivedBioAssayDataBean table: getDerivedBioAssayData()) {
			DatumBean proteinBinding=new DatumBean();
			proteinBinding.setType("Percent Protein Binding");
			proteinBinding.setValueUnit("%");
			table.getDatumList().add(proteinBinding);
		}
	}
	
	public PlasmaProteinBinding getDomainObj() {
		PlasmaProteinBinding proteinBinding = new PlasmaProteinBinding();
		super.updateDomainObj(proteinBinding);
		return proteinBinding;
	}
}

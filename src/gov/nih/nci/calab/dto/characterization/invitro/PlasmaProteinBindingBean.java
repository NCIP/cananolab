package gov.nih.nci.calab.dto.characterization.invitro;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.invitro.PlasmaProteinBinding;

import gov.nih.nci.calab.dto.characterization.*;

import java.util.List;


/**
 * This class represents the plasmaProteinBinding characterization information to be shown in
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
		for (DerivedBioAssayDataBean table: getDerivedBioAssayDataList()) {
			DatumBean percentPlasmaProteinBinding=new DatumBean();
			percentPlasmaProteinBinding.setType("Percent Plasma Protein Binding");
			percentPlasmaProteinBinding.setValueUnit("%");
			table.getDatumList().add(percentPlasmaProteinBinding);
		}
	}
	
	public void setDerivedBioAssayDataList(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayDataList(derivedBioAssayData);
		
		for (DerivedBioAssayDataBean table:getDerivedBioAssayDataList()) {
			DatumBean percentPlasmaProteinBinding=new DatumBean();
			percentPlasmaProteinBinding.setType("Percent Plasma Protein Binding");
			percentPlasmaProteinBinding.setValueUnit("%");
			table.getDatumList().add(percentPlasmaProteinBinding);
		}
	}
	
	public PlasmaProteinBinding getDomainObj() {
		PlasmaProteinBinding plasmaProteinBinding = new PlasmaProteinBinding();
		super.updateDomainObj(plasmaProteinBinding);
		return plasmaProteinBinding;
	}
}

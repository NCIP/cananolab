package gov.nih.nci.calab.dto.characterization.invitro;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.Datum;
import gov.nih.nci.calab.domain.nano.characterization.DerivedBioAssayData;
import gov.nih.nci.calab.domain.nano.characterization.invitro.PlasmaProteinBinding;

import gov.nih.nci.calab.dto.characterization.*;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;

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
	}
	
	public PlasmaProteinBindingBean(Characterization aChar) {
		super(aChar);
	}
	
	public void setDerivedBioAssayDataList(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayDataList(derivedBioAssayData);

		for (DerivedBioAssayDataBean table : getDerivedBioAssayDataList()) {
			for (DatumBean datum : table.getDatumList()) {
				datum.setType(CaNanoLabConstants.BLOODCONTACTTOX_PLASMA_PROTEIN_BINDING_DATA_TYPE);
				datum.setValueUnit(CaNanoLabConstants.UNIT_PERCENT);
			}
		}
	}
	
	public PlasmaProteinBinding getDomainObj() {
		PlasmaProteinBinding plasmaProteinBinding = new PlasmaProteinBinding();
		super.updateDomainObj(plasmaProteinBinding);
		for (DerivedBioAssayData chart: plasmaProteinBinding.getDerivedBioAssayDataCollection()){
			for (Datum data: chart.getDatumCollection()){
				data.setType(CaNanoLabConstants.BLOODCONTACTTOX_PLASMA_PROTEIN_BINDING_DATA_TYPE);
				if (data.getValue() != null) {
					data.getValue().setUnitOfMeasurement(CaNanoLabConstants.UNIT_PERCENT);
				}			
			}
		}
	return plasmaProteinBinding;
	}
}

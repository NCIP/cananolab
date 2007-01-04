package gov.nih.nci.calab.dto.characterization.invitro;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.Datum;
import gov.nih.nci.calab.domain.nano.characterization.DerivedBioAssayData;
import gov.nih.nci.calab.domain.nano.characterization.invitro.EnzymeInduction;

import gov.nih.nci.calab.dto.characterization.*;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;

import java.util.List;


/**
 * This class represents the enzymeInduction characterization information to be shown in
 * the view page.
 * 
 * @author beasleyj
 * 
 */
public class EnzymeInductionBean extends CharacterizationBean {
	public EnzymeInductionBean() {
		super();
	}
	
	public EnzymeInductionBean(Characterization aChar) {
		super(aChar);
	}
	
	public void setDerivedBioAssayDataList(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayDataList(derivedBioAssayData);

		for (DerivedBioAssayDataBean table : getDerivedBioAssayDataList()) {
			for (DatumBean datum : table.getDatumList()) {
				datum.setType(CaNanoLabConstants.TOXICITY_ENZYME_FUNCTION_DATA_TYPE);
				datum.setValueUnit(CaNanoLabConstants.UNIT_PERCENT);
			}
		}
	}
	
	public EnzymeInduction getDomainObj() {
		EnzymeInduction enzymeInduction = new EnzymeInduction();
		super.updateDomainObj(enzymeInduction);
		for (DerivedBioAssayData chart: enzymeInduction.getDerivedBioAssayDataCollection()){
			for (Datum data: chart.getDatumCollection()){
				data.setType(CaNanoLabConstants.TOXICITY_ENZYME_FUNCTION_DATA_TYPE);
				if (data.getValue() != null) {
					data.getValue().setUnitOfMeasurement(CaNanoLabConstants.UNIT_PERCENT);
				}			
			}
		}
		return enzymeInduction;
	}
}

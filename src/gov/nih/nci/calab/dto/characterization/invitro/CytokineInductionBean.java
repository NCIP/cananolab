package gov.nih.nci.calab.dto.characterization.invitro;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.Datum;
import gov.nih.nci.calab.domain.nano.characterization.DerivedBioAssayData;
import gov.nih.nci.calab.domain.nano.characterization.invitro.CytokineInduction;

import gov.nih.nci.calab.dto.characterization.*;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;

import java.util.List;


/**
 * This class represents the cytokineInduction characterization information to be shown in
 * the view page.
 * 
 * @author beasleyj
 * 
 */
public class CytokineInductionBean extends CharacterizationBean {
	public CytokineInductionBean() {
		super();
	}
	
	public CytokineInductionBean(Characterization aChar) {
		super(aChar);
	}
	
	public void setDerivedBioAssayDataList(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayDataList(derivedBioAssayData);

		for (DerivedBioAssayDataBean table : getDerivedBioAssayDataList()) {
			for (DatumBean datum : table.getDatumList()) {
				datum.setType("Cytokine Concentration");
//				datum.setValueUnit("g/ml");
			}
		}
	}
	
	public CytokineInduction getDomainObj() {
		CytokineInduction cytokineInduction = new CytokineInduction();
		super.updateDomainObj(cytokineInduction);
		for (DerivedBioAssayData chart: cytokineInduction.getDerivedBioAssayDataCollection()){
			for (Datum data: chart.getDatumCollection()){
				data.setType(CaNanoLabConstants.IMMUNOCELLFUNCTOX_CYTOKINE_INDUCTION_DATA_TYPE);
				if (data.getValue() != null) {
					data.getValue().setUnitOfMeasurement(CaNanoLabConstants.UNIT_MG_ML);
				}			
			}
		}
		return cytokineInduction;
	}
}

package gov.nih.nci.calab.dto.characterization.invitro;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.Datum;
import gov.nih.nci.calab.domain.nano.characterization.DerivedBioAssayData;
import gov.nih.nci.calab.domain.nano.characterization.invitro.LeukocyteProliferation;

import gov.nih.nci.calab.dto.characterization.*;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;

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
	}
	
	public LeukocyteProliferationBean(Characterization aChar) {
		super(aChar);
	}
	
	public void setDerivedBioAssayDataList(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayDataList(derivedBioAssayData);

		for (DerivedBioAssayDataBean table : getDerivedBioAssayDataList()) {
			for (DatumBean datum : table.getDatumList()) {
				datum.setType(CaNanoLabConstants.IMMUNOCELLFUNCTOX_LEUKOCYTE_PROLIFERATION_DATA_TYPE);
				datum.setValueUnit(CaNanoLabConstants.UNIT_PERCENT);
			}
		}
	}
	
	public LeukocyteProliferation getDomainObj() {
		LeukocyteProliferation leukocyteProliferation = new LeukocyteProliferation();
		super.updateDomainObj(leukocyteProliferation);
		for (DerivedBioAssayData chart: leukocyteProliferation.getDerivedBioAssayDataCollection()){
			for (Datum data: chart.getDatumCollection()){
				data.setType(CaNanoLabConstants.IMMUNOCELLFUNCTOX_LEUKOCYTE_PROLIFERATION_DATA_TYPE);
				if (data.getValue() != null) {
					data.getValue().setUnitOfMeasurement(CaNanoLabConstants.UNIT_PERCENT);
				}			
			}
		}
		return leukocyteProliferation;
	}
}

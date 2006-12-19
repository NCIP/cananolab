package gov.nih.nci.calab.dto.characterization.invitro;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.Datum;
import gov.nih.nci.calab.domain.nano.characterization.DerivedBioAssayData;
import gov.nih.nci.calab.domain.nano.characterization.invitro.OxidativeBurst;

import gov.nih.nci.calab.dto.characterization.*;
import gov.nih.nci.calab.service.util.CananoConstants;

import java.util.List;


/**
 * This class represents the oxidativeBurst characterization information to be shown in
 * the view page.
 * 
 * @author beasleyj
 * 
 */
public class OxidativeBurstBean extends CharacterizationBean {
	public OxidativeBurstBean() {
		super();
	}
	
	public OxidativeBurstBean(Characterization aChar) {
		super(aChar);
	}
	
	public void setDerivedBioAssayDataList(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayDataList(derivedBioAssayData);

		for (DerivedBioAssayDataBean table : getDerivedBioAssayDataList()) {
			for (DatumBean datum : table.getDatumList()) {
				datum.setType(CananoConstants.IMMUNOCELLFUNCTOX_OXIDATIVE_BURST_DATA_TYPE);
				datum.setValueUnit(CananoConstants.UNIT_PERCENT);
			}
		}
	}
	
	public OxidativeBurst getDomainObj() {
		OxidativeBurst oxidativeBurst = new OxidativeBurst();
		super.updateDomainObj(oxidativeBurst);
		for (DerivedBioAssayData chart: oxidativeBurst.getDerivedBioAssayDataCollection()){
			for (Datum data: chart.getDatumCollection()){
				data.setType(CananoConstants.IMMUNOCELLFUNCTOX_OXIDATIVE_BURST_DATA_TYPE);
				if (data.getValue() != null) {
					data.getValue().setUnitOfMeasurement(CananoConstants.UNIT_PERCENT);
				}			
			}
		}
		return oxidativeBurst;
	}
}

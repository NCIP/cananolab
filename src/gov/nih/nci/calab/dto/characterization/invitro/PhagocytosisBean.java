package gov.nih.nci.calab.dto.characterization.invitro;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.Datum;
import gov.nih.nci.calab.domain.nano.characterization.DerivedBioAssayData;
import gov.nih.nci.calab.domain.nano.characterization.invitro.Phagocytosis;

import gov.nih.nci.calab.dto.characterization.*;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;

import java.util.List;


/**
 * This class represents the phagocytosis characterization information to be shown in
 * the view page.
 * 
 * @author beasleyj
 * 
 */
public class PhagocytosisBean extends CharacterizationBean {
	public PhagocytosisBean() {
		super();
	}
	
	public PhagocytosisBean(Characterization aChar) {
		super(aChar);
	}
	
	public void setDerivedBioAssayDataList(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayDataList(derivedBioAssayData);

		for (DerivedBioAssayDataBean table : getDerivedBioAssayDataList()) {
			for (DatumBean datum : table.getDatumList()) {
				datum.setType(CaNanoLabConstants.IMMUNOCELLFUNCTOX_PHAGOCYTOSIS_DATA_TYPE);
				datum.setValueUnit(CaNanoLabConstants.UNIT_FOLD);
			}
		}
	}
	
	public Phagocytosis getDomainObj() {
		Phagocytosis phagocytosis = new Phagocytosis();
		super.updateDomainObj(phagocytosis);
		for (DerivedBioAssayData chart: phagocytosis.getDerivedBioAssayDataCollection()){
			for (Datum data: chart.getDatumCollection()){
				data.setType(CaNanoLabConstants.IMMUNOCELLFUNCTOX_PHAGOCYTOSIS_DATA_TYPE);
				if (data.getValue() != null) {
					data.getValue().setUnitOfMeasurement(CaNanoLabConstants.UNIT_FOLD);
				}			
			}
		}
		return phagocytosis;
	}
}

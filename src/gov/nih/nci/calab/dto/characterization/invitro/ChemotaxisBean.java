package gov.nih.nci.calab.dto.characterization.invitro;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.Datum;
import gov.nih.nci.calab.domain.nano.characterization.DerivedBioAssayData;
import gov.nih.nci.calab.domain.nano.characterization.invitro.Chemotaxis;

import gov.nih.nci.calab.dto.characterization.*;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;

import java.util.List;


/**
 * This class represents the chemotaxis characterization information to be shown in
 * the view page.
 * 
 * @author beasleyj
 * 
 */
public class ChemotaxisBean extends CharacterizationBean {
	public ChemotaxisBean() {
		super();
	}
	
	public ChemotaxisBean(Characterization aChar) {
		super(aChar);
	}
	
	public void setDerivedBioAssayDataList(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayDataList(derivedBioAssayData);
		
		for (DerivedBioAssayDataBean table:getDerivedBioAssayDataList()) {
			for (DatumBean datum : table.getDatumList()) {
				datum.setType("Relative Fluorescent Values");
				datum.setValueUnit("RFU");
			}			
		}
	}
	
	public Chemotaxis getDomainObj() {
		Chemotaxis chemotaxis = new Chemotaxis();
		super.updateDomainObj(chemotaxis);
		for (DerivedBioAssayData chart: chemotaxis.getDerivedBioAssayDataCollection()){
			for (Datum data: chart.getDatumCollection()){
				data.setType(CaNanoLabConstants.IMMUNOCELLFUNCTOX_CHEMOTAXIS_DATA_TYPE);
				if (data.getValue() != null) {
					data.getValue().setUnitOfMeasurement(CaNanoLabConstants.UNIT_RFU);
				}			
			}
		}
		return chemotaxis;
	}
}

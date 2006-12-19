package gov.nih.nci.calab.dto.characterization.invitro;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.Datum;
import gov.nih.nci.calab.domain.nano.characterization.DerivedBioAssayData;
import gov.nih.nci.calab.domain.nano.characterization.invitro.PlateAggregation;

import gov.nih.nci.calab.dto.characterization.*;
import gov.nih.nci.calab.service.util.CananoConstants;

import java.util.List;


/**
 * This class represents the plateAggregation characterization information to be shown in
 * the view page.
 * 
 * @author beasleyj
 * 
 */
public class PlateAggregationBean extends CharacterizationBean {
	public PlateAggregationBean() {
		super();
	}
	
	public PlateAggregationBean(Characterization aChar) {
		super(aChar);
	}
	
	public void setDerivedBioAssayDataList(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayDataList(derivedBioAssayData);

		for (DerivedBioAssayDataBean table : getDerivedBioAssayDataList()) {
			for (DatumBean datum : table.getDatumList()) {
				datum.setType(CananoConstants.BLOODCONTACTTOX_PLATE_AGGREGATION_DATA_TYPE);
				datum.setValueUnit(CananoConstants.UNIT_PERCENT);
			}
		}
	}
	
	public PlateAggregation getDomainObj() {
		PlateAggregation plateAggregation = new PlateAggregation();
		super.updateDomainObj(plateAggregation);
		for (DerivedBioAssayData chart: plateAggregation.getDerivedBioAssayDataCollection()){
			for (Datum data: chart.getDatumCollection()){
				data.setType(CananoConstants.BLOODCONTACTTOX_PLATE_AGGREGATION_DATA_TYPE);
				if (data.getValue() != null) {
					data.getValue().setUnitOfMeasurement(CananoConstants.UNIT_PERCENT);
				}			
			}
		}
	return plateAggregation;
	}
}

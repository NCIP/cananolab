package gov.nih.nci.calab.dto.characterization.invitro;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.Datum;
import gov.nih.nci.calab.domain.nano.characterization.DerivedBioAssayData;
import gov.nih.nci.calab.domain.nano.characterization.invitro.PlateletAggregation;

import gov.nih.nci.calab.dto.characterization.*;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;

import java.util.List;


/**
 * This class represents the plateletAggregation characterization information to be shown in
 * the view page.
 * 
 * @author beasleyj
 * 
 */
public class PlateletAggregationBean extends CharacterizationBean {
	public PlateletAggregationBean() {
		super();
	}
	
	public PlateletAggregationBean(Characterization aChar) {
		super(aChar);
	}
	
	public void setDerivedBioAssayDataList(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayDataList(derivedBioAssayData);

		for (DerivedBioAssayDataBean table : getDerivedBioAssayDataList()) {
			for (DatumBean datum : table.getDatumList()) {
				datum.setType(CaNanoLabConstants.BLOODCONTACTTOX_PLATELET_AGGREGATION_DATA_TYPE);
				datum.setValueUnit(CaNanoLabConstants.UNIT_PERCENT);
			}
		}
	}
	
	public PlateletAggregation getDomainObj() {
		PlateletAggregation plateletAggregation = new PlateletAggregation();
		super.updateDomainObj(plateletAggregation);
		for (DerivedBioAssayData chart: plateletAggregation.getDerivedBioAssayDataCollection()){
			for (Datum data: chart.getDatumCollection()){
				data.setType(CaNanoLabConstants.BLOODCONTACTTOX_PLATELET_AGGREGATION_DATA_TYPE);
				if (data.getValue() != null) {
					data.getValue().setUnitOfMeasurement(CaNanoLabConstants.UNIT_PERCENT);
				}			
			}
		}
	return plateletAggregation;
	}
}

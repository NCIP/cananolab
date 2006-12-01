package gov.nih.nci.calab.dto.characterization.invitro;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.invitro.PlateAggregation;

import gov.nih.nci.calab.dto.characterization.*;

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
				datum.setType("Percent Plate Aggregation");
				datum.setValueUnit("%");
			}
		}
	}
	
	public PlateAggregation getDomainObj() {
		PlateAggregation plateAggregation = new PlateAggregation();
		super.updateDomainObj(plateAggregation);
		return plateAggregation;
	}
}

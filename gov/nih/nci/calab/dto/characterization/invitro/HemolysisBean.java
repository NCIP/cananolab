package gov.nih.nci.calab.dto.characterization.invitro;

import gov.nih.nci.calab.domain.nano.characterization.Datum;
import gov.nih.nci.calab.domain.nano.characterization.DerivedBioAssayData;
import gov.nih.nci.calab.domain.nano.characterization.invitro.Hemolysis;
import gov.nih.nci.calab.dto.characterization.CharacterizationBean;
import gov.nih.nci.calab.dto.characterization.DatumBean;
import gov.nih.nci.calab.dto.characterization.DerivedBioAssayDataBean;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;

import java.util.List;

/**
 * This class represents the hemolysis characterization information to be shown
 * in the view page.
 * 
 * @author beasleyj
 * 
 */
public class HemolysisBean extends CharacterizationBean {

	public HemolysisBean() {
		super();
	}

	public HemolysisBean(Hemolysis aChar) {
		super(aChar);
	}

	public void setDerivedBioAssayDataList(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayDataList(derivedBioAssayData);

		for (DerivedBioAssayDataBean table : getDerivedBioAssayDataList()) {
			for (DatumBean datum : table.getDatumList()) {
				datum.setType(CaNanoLabConstants.BLOODCONTACTTOX_HEMOLYSIS_DATA_TYPE);
				datum.setValueUnit(CaNanoLabConstants.UNIT_PERCENT);
			}
		}
	}

	public Hemolysis getDomainObj() {
		Hemolysis hemolysis = new Hemolysis();
		super.updateDomainObj(hemolysis);
		for (DerivedBioAssayData chart: hemolysis.getDerivedBioAssayDataCollection()){
			for (Datum data: chart.getDatumCollection()){
				data.setType(CaNanoLabConstants.BLOODCONTACTTOX_HEMOLYSIS_DATA_TYPE);
				if (data.getValue() != null) {
					data.getValue().setUnitOfMeasurement(CaNanoLabConstants.UNIT_PERCENT);
				}			
			}
		}
		return hemolysis;
	}
}

/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.calab.dto.characterization.invitro;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.Datum;
import gov.nih.nci.calab.domain.nano.characterization.DerivedBioAssayData;
import gov.nih.nci.calab.domain.nano.characterization.invitro.NKCellCytotoxicActivity;

import gov.nih.nci.calab.dto.characterization.*;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;

import java.util.List;


/**
 * This class represents the nkCellCytotoxicActivity characterization information to be shown in
 * the view page.
 * 
 * @author beasleyj
 * 
 */
public class NKCellCytotoxicActivityBean extends CharacterizationBean {
	public NKCellCytotoxicActivityBean() {
		super();
	}
	
	public NKCellCytotoxicActivityBean(Characterization aChar) {
		super(aChar);
	}
	
	public void setDerivedBioAssayDataList(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayDataList(derivedBioAssayData);

		for (DerivedBioAssayDataBean table : getDerivedBioAssayDataList()) {
			for (DatumBean datum : table.getDatumList()) {
				datum.setType(CaNanoLabConstants.IMMUNOCELLFUNCTOX_NKCELL_CYTOTOXIC_ACTIVITY_DATA_TYPE);
				datum.setValueUnit(CaNanoLabConstants.UNIT_PERCENT);
			}
		}
	}
	
	public NKCellCytotoxicActivity getDomainObj() {
		NKCellCytotoxicActivity nkCellCytotoxicActivity = new NKCellCytotoxicActivity();
		super.updateDomainObj(nkCellCytotoxicActivity);
		for (DerivedBioAssayData chart: nkCellCytotoxicActivity.getDerivedBioAssayDataCollection()){
			for (Datum data: chart.getDatumCollection()){
				data.setType(CaNanoLabConstants.IMMUNOCELLFUNCTOX_NKCELL_CYTOTOXIC_ACTIVITY_DATA_TYPE);
				if (data.getValue() != null) {
					data.getValue().setUnitOfMeasurement(CaNanoLabConstants.UNIT_PERCENT);
				}			
			}
		}
		return nkCellCytotoxicActivity;
	}
}

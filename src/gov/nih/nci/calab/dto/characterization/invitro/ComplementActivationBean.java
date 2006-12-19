package gov.nih.nci.calab.dto.characterization.invitro;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.Datum;
import gov.nih.nci.calab.domain.nano.characterization.DerivedBioAssayData;
import gov.nih.nci.calab.domain.nano.characterization.invitro.ComplementActivation;

import gov.nih.nci.calab.dto.characterization.*;
import gov.nih.nci.calab.service.util.CananoConstants;

import java.util.List;

/**
 * This class represents the complementActivation characterization information
 * to be shown in the view page.
 * 
 * @author beasleyj
 * 
 */
public class ComplementActivationBean extends CharacterizationBean {
	public ComplementActivationBean() {
		super();
	}

	public ComplementActivationBean(Characterization aChar) {
		super(aChar);
	}

	public void setDerivedBioAssayDataList(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayDataList(derivedBioAssayData);

		for (DerivedBioAssayDataBean table : getDerivedBioAssayDataList()) {
			for (DatumBean datum : table.getDatumList()) {
				datum.setType("Percent Complement Activation");
				datum.setValueUnit("%");
			}
		}
	}

	public ComplementActivation getDomainObj() {
		ComplementActivation complementActivation = new ComplementActivation();
		super.updateDomainObj(complementActivation);
		for (DerivedBioAssayData chart : complementActivation
				.getDerivedBioAssayDataCollection()) {
			for (Datum data : chart.getDatumCollection()) {
				data.setType(CananoConstants.IMMUNOCELLFUNCTOX_COMPLEMENT_ACTIVATION_DATA_TYPE);
				if (data.getValue() != null) {
					data.getValue().setUnitOfMeasurement(CananoConstants.UNIT_PERCENT);
				}
			}
		}
		return complementActivation;
	}
}

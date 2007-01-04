package gov.nih.nci.calab.dto.characterization.physical;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.physical.MolecularWeight;
import gov.nih.nci.calab.dto.characterization.CharacterizationBean;
import gov.nih.nci.calab.dto.characterization.DatumBean;
import gov.nih.nci.calab.dto.characterization.DerivedBioAssayDataBean;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;

import java.util.List;

/**
 * This class represents the size characterization information to be shown in
 * the view page.
 * 
 * @author pansu
 * 
 */
public class MolecularWeightBean extends CharacterizationBean {
	public MolecularWeightBean() {
		super();
		initSetup();
	}

	public MolecularWeightBean(Characterization aChar) {
		super(aChar);
	}

	public void setDerivedBioAssayDataList(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayDataList(derivedBioAssayData);
		initSetup();
	}

	public void initSetup() {
		for (DerivedBioAssayDataBean table : getDerivedBioAssayDataList()) {
			if (table.getDatumList().isEmpty()) {
				DatumBean mw = new DatumBean();
				mw.setType(CaNanoLabConstants.PHYSICAL_MOLECULAR_WEIGHT);
				mw.setValueUnit("kDa");

				table.getDatumList().add(mw);
			}
		}
	}

	public MolecularWeight getDomainObj() {
		MolecularWeight molecularWeight = new MolecularWeight();
		super.updateDomainObj(molecularWeight);
		return molecularWeight;
	}
}

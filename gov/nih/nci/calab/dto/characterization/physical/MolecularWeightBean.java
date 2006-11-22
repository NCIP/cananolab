package gov.nih.nci.calab.dto.characterization.physical;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.physical.MolecularWeight;
import gov.nih.nci.calab.dto.characterization.CharacterizationBean;
import gov.nih.nci.calab.dto.characterization.DatumBean;
import gov.nih.nci.calab.dto.characterization.DerivedBioAssayDataBean;

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
		/*
		 * this.setCharacterizationSource(aChar.getSource());
		 * this.setViewTitle(aChar.getIdentificationName());
		 * this.setDescription(aChar.getDescription()); if
		 * (aChar.getInstrument() != null) {
		 * this.getInstrument().setType(aChar.getInstrument().getInstrumentType().getName());
		 * this.getInstrument().setDescription(aChar.getInstrument().getDescription());
		 * this.getInstrument().setManufacturer(aChar.getInstrument().getManufacturer().getName()); }
		 * this.setNumberOfDerivedBioAssayData(Integer.valueOf(aChar.getDerivedBioAssayDataCollection().size()).toString());
		 * for (DerivedBioAssayData table :
		 * aChar.getDerivedBioAssayDataCollection()) { DerivedBioAssayDataBean
		 * ctBean = new DerivedBioAssayDataBean(table);
		 * this.getDerivedBioAssayData().add(ctBean); } if
		 * (aChar.getCharacterizationProtocol() != null) {
		 * this.getCharacterizationProtocol().setId(aChar.getCharacterizationProtocol().getId());
		 * this.getCharacterizationProtocol().setName(aChar.getCharacterizationProtocol().getName());
		 * this.getCharacterizationProtocol().setVersion(aChar.getCharacterizationProtocol().getVersion()); }
		 */
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
				mw.setType("Molecular Weight");
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

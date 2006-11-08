package gov.nih.nci.calab.dto.characterization.invitro;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.invitro.Phagocytosis;

import gov.nih.nci.calab.dto.characterization.*;

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
		initSetup();
	}
	
	public PhagocytosisBean(Characterization aChar) {
		super(aChar);
	}
	
	public void initSetup() {
		for (DerivedBioAssayDataBean table: getDerivedBioAssayData()) {
			DatumBean percentPhagocytosis=new DatumBean();
			percentPhagocytosis.setType("Percent Phagocytosis");
			percentPhagocytosis.setValueUnit("%");
			table.getDatumList().add(percentPhagocytosis);
		}
	}
	
	public void setDerivedBioAssayData(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayData(derivedBioAssayData);
		
		for (DerivedBioAssayDataBean table:getDerivedBioAssayData()) {
			DatumBean percentPhagocytosis=new DatumBean();
			percentPhagocytosis.setType("Percent Phagocytosis");
			percentPhagocytosis.setValueUnit("%");
			table.getDatumList().add(percentPhagocytosis);
		}
	}
	
	public Phagocytosis getDomainObj() {
		Phagocytosis phagocytosis = new Phagocytosis();
		super.updateDomainObj(phagocytosis);
		return phagocytosis;
	}
}

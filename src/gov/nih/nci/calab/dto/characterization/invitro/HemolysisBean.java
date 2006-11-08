package gov.nih.nci.calab.dto.characterization.invitro;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.invitro.Hemolysis;

import gov.nih.nci.calab.dto.characterization.*;

import java.util.List;


/**
 * This class represents the hemolysis characterization information to be shown in
 * the view page.
 * 
 * @author beasleyj
 * 
 */
public class HemolysisBean extends CharacterizationBean {
	public HemolysisBean() {
		super();
		initSetup();
	}
	
	public HemolysisBean(Characterization aChar) {
		super(aChar);
	}
	
	public void initSetup() {
		for (DerivedBioAssayDataBean table: getDerivedBioAssayData()) {
			DatumBean percentHemolysis=new DatumBean();
			percentHemolysis.setType("Percent Hemolysis");
			percentHemolysis.setValueUnit("%");
			table.getDatumList().add(percentHemolysis);
		}
	}
	
	public void setDerivedBioAssayData(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayData(derivedBioAssayData);
		
		for (DerivedBioAssayDataBean table:getDerivedBioAssayData()) {
			DatumBean percentHemolysis=new DatumBean();
			percentHemolysis.setType("Percent Hemolysis");
			percentHemolysis.setValueUnit("%");
			table.getDatumList().add(percentHemolysis);
		}
	}
	
	public Hemolysis getDomainObj() {
		Hemolysis hemolysis = new Hemolysis();
		super.updateDomainObj(hemolysis);
		return hemolysis;
	}
}

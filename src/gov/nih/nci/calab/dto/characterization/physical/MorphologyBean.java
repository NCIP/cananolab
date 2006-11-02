package gov.nih.nci.calab.dto.characterization.physical;

import gov.nih.nci.calab.domain.nano.characterization.physical.Morphology;
import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.DerivedBioAssayData;

import gov.nih.nci.calab.dto.characterization.CharacterizationBean;
import gov.nih.nci.calab.dto.characterization.DerivedBioAssayDataBean;
import gov.nih.nci.calab.dto.characterization.DatumBean;
import java.util.List;


/**
 * This class represents the Morphology characterization information to be shown in
 * the view page.
 * 
 * @author pansu
 * 
 */
public class MorphologyBean extends CharacterizationBean {
	private String type;
	private String otherType;
	
	public String getOtherType() {
		return otherType;
	}

	public void setOtherType(String otherType) {
		this.otherType = otherType;
	}

	public MorphologyBean() {
		super();
		initSetup();
	}
	
	public MorphologyBean(Morphology aChar) {
		super(aChar);
		/*
		this.setCharacterizationSource(aChar.getSource());
		this.setViewTitle(aChar.getIdentificationName());
		this.setDescription(aChar.getDescription());
		if (aChar.getInstrument() != null) {
			this.getInstrument().setType(aChar.getInstrument().getInstrumentType().getName());
			this.getInstrument().setDescription(aChar.getInstrument().getDescription());
			this.getInstrument().setManufacturer(aChar.getInstrument().getManufacturer().getName());
		}
		this.setNumberOfDerivedBioAssayData(Integer.valueOf(aChar.getDerivedBioAssayDataCollection().size()).toString());
		for (DerivedBioAssayData table : aChar.getDerivedBioAssayDataCollection()) {
			DerivedBioAssayDataBean ctBean = new DerivedBioAssayDataBean(table);
			this.getDerivedBioAssayData().add(ctBean);
		}
		if (aChar.getCharacterizationProtocol() != null) {
			this.getCharacterizationProtocol().setId(aChar.getCharacterizationProtocol().getId());
			this.getCharacterizationProtocol().setName(aChar.getCharacterizationProtocol().getName());
			this.getCharacterizationProtocol().setVersion(aChar.getCharacterizationProtocol().getVersion());
		}
		*/
		this.type = aChar.getType();
	}
	
	public void setDerivedBioAssayData(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayData(derivedBioAssayData);
		initSetup();
	}
	
	public void initSetup() {
		/*
		for (DerivedBioAssayDataBean table:getDerivedBioAssayData()) {
			DatumBean average=new DatumBean();
			average.setType("Average");
			average.setValueUnit("nm");
			DatumBean zaverage=new DatumBean();
			zaverage.setType("Z-Average");
			zaverage.setValueUnit("nm");
			DatumBean pdi=new DatumBean();
			pdi.setType("PDI");
			table.getDatumList().add(average);
			table.getDatumList().add(zaverage);
			table.getDatumList().add(pdi);
		}
		*/
	}
	
	public Morphology getDomainObj() {
		Morphology morphology = new Morphology();
		super.updateDomainObj(morphology);
		
		if (this.type.equalsIgnoreCase("other") && !this.otherType.equalsIgnoreCase(""))
			morphology.setType(this.otherType);
		else
			morphology.setType(this.type);
		
		return morphology;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}

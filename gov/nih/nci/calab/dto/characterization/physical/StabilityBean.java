package gov.nih.nci.calab.dto.characterization.physical;

import gov.nih.nci.calab.domain.Measurement;
import gov.nih.nci.calab.domain.nano.characterization.physical.Stability;
import gov.nih.nci.calab.dto.characterization.CharacterizationBean;
import gov.nih.nci.calab.dto.characterization.DerivedBioAssayDataBean;

import java.util.List;


/**
 * This class represents the Stability characterization information to be shown in
 * the view page.
 * 
 * @author chande
 * 
 */
public class StabilityBean extends CharacterizationBean {
	private String id;
	private String longTermStorage;
	private String longTermStorageUnit;
	private String shortTermStorage;
	private String shortTermStorageUnit;
	private String stressResult;
	private String releaseKineticsDescription;
	private String measurementType;
	private StressorBean stressor = new StressorBean();
	
	public StabilityBean() {
		super();
		initSetup();
	}
	
	public StabilityBean(Stability aChar) {
		super(aChar);

		this.id = aChar.getId().toString();
		if (aChar.getLongTermStorage() != null) {
			this.longTermStorage = aChar.getLongTermStorage().getValue();
			this.longTermStorageUnit = aChar.getLongTermStorage().getUnitOfMeasurement();
		}
		if (aChar.getShortTermStorage() != null) {
			this.shortTermStorage = aChar.getShortTermStorage().getValue();
			this.shortTermStorageUnit = aChar.getShortTermStorage().getUnitOfMeasurement();
		}
		this.stressResult = aChar.getStressResult();
		this.releaseKineticsDescription = aChar.getReleaseKineticsDescription();
		this.measurementType = aChar.getMeasurementType();
		if (aChar.getStressor() != null) {
			this.stressor = new StressorBean(aChar.getStressor());
		}
		
	}
	
	public void setDerivedBioAssayDataList(
			List<DerivedBioAssayDataBean> derivedBioAssayData) {
		super.setDerivedBioAssayDataList(derivedBioAssayData);
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
	
	public Stability getDomainObj() {
		Stability stability = new Stability();
		super.updateDomainObj(stability);
		
	    if (this.id != null && !this.id.equals(""))
	    	stability.setId(new Long(id));
		stability.setLongTermStorage(new Measurement(this.longTermStorage, this.longTermStorageUnit));
		stability.setShortTermStorage(new Measurement(this.shortTermStorage, this.shortTermStorageUnit));
		stability.setStressResult(this.stressResult);
		stability.setReleaseKineticsDescription(this.releaseKineticsDescription);
		stability.setMeasurementType(this.measurementType);
		if (this.stressor != null)
			stability.setStressor(this.stressor.getDomainObj());
		
		return stability;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLongTermStorage() {
		return longTermStorage;
	}

	public void setLongTermStorage(String longTermStorage) {
		this.longTermStorage = longTermStorage;
	}

	public String getLongTermStorageUnit() {
		return longTermStorageUnit;
	}

	public void setLongTermStorageUnit(String longTermStorageUnit) {
		this.longTermStorageUnit = longTermStorageUnit;
	}

	public String getMeasurementType() {
		return measurementType;
	}

	public void setMeasurementType(String measurementType) {
		this.measurementType = measurementType;
	}

	public String getReleaseKineticsDescription() {
		return releaseKineticsDescription;
	}

	public void setReleaseKineticsDescription(String releaseKineticsDescription) {
		this.releaseKineticsDescription = releaseKineticsDescription;
	}

	public String getShortTermStorage() {
		return shortTermStorage;
	}

	public void setShortTermStorage(String shortTermStorage) {
		this.shortTermStorage = shortTermStorage;
	}

	public String getShortTermStorageUnit() {
		return shortTermStorageUnit;
	}

	public void setShortTermStorageUnit(String shortTermStorageUnit) {
		this.shortTermStorageUnit = shortTermStorageUnit;
	}

	public StressorBean getStressor() {
		return stressor;
	}

	public void setStressor(StressorBean stressor) {
		this.stressor = stressor;
	}

	public String getStressResult() {
		return stressResult;
	}

	public void setStressResult(String stressResult) {
		this.stressResult = stressResult;
	}

}

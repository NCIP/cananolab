/**
 * 
 */
package gov.nih.nci.calab.domain.nano.characterization.physical;

import gov.nih.nci.calab.domain.Measurement;
import gov.nih.nci.calab.domain.nano.characterization.Characterization;

/**
 * @author zengje
 * 
 */
public class Stability extends Characterization {

	private static final long serialVersionUID = 1234567890L;

	private Measurement longTermStorage;

	private Measurement shortTermStorage;

	private String stressResult;

	private String releaseKineticsDescription;

	private String measurementType;

	private Stressor stressor;

	/**
	 * 
	 */
	public Stability() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Measurement getLongTermStorage() {
		return longTermStorage;
	}

	public void setLongTermStorage(Measurement longTermStorage) {
		this.longTermStorage = longTermStorage;
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

	public Measurement getShortTermStorage() {
		return shortTermStorage;
	}

	public void setShortTermStorage(Measurement shortTermStorage) {
		this.shortTermStorage = shortTermStorage;
	}

	public Stressor getStressor() {
		return stressor;
	}

	public void setStressor(Stressor stressor) {
		this.stressor = stressor;
	}

	public String getStressResult() {
		return stressResult;
	}

	public void setStressResult(String stressResult) {
		this.stressResult = stressResult;
	}

	public String getClassification() {
		return PHYSICAL_CHARACTERIZATION;
	}

	public String getName() {
		return PHYSICAL_STABILITY;
	}
}

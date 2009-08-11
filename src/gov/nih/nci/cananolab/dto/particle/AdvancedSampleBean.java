package gov.nih.nci.cananolab.dto.particle;

/**
 * This class represents shared properties of samples resulted from advanced
 * sample search.
 * 
 * @author pansu
 * 
 */
public class AdvancedSampleBean {
	private String sampleName;
	private String location; // e.g. NCICB, NCL, WUSTL, etc.

	public AdvancedSampleBean() {
	}

	public AdvancedSampleBean(String sampleName, String location) {
		super();
		this.sampleName = sampleName;
		this.location = location;
	}

	public String getSampleName() {
		return sampleName;
	}

	public void setSampleName(String sampleName) {
		this.sampleName = sampleName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}

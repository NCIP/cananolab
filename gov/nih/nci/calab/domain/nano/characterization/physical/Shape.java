package gov.nih.nci.calab.domain.nano.characterization.physical;

import gov.nih.nci.calab.domain.Measurement;
import gov.nih.nci.calab.domain.nano.characterization.Characterization;

public class Shape extends Characterization {

	private static final long serialVersionUID = 1234567890L;
	
	private String type;

	private Measurement maxDimension;

	private Measurement minDimension;

	public Shape() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Measurement getMaxDimension() {
		return maxDimension;
	}

	public void setMaxDimension(Measurement maxDimension) {
		this.maxDimension = maxDimension;
	}

	public Measurement getMinDimension() {
		return minDimension;
	}

	public void setMinDimension(Measurement minDimension) {
		this.minDimension = minDimension;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getClassification() {
		return PHYSICAL_CHARACTERIZATION;
	}

	public String getName() {
		return PHYSICAL_SHAPE;
	}
}

package gov.nih.nci.calab.domain.nano.characterization.physical.composition;


public class CarbonNanotubeComposition extends ParticleComposition {

	private static final long serialVersionUID = 1234567890L;

	private String chirality;

	private Float growthDiameter;

	private Float averageLength;

	private String wallType;

	public CarbonNanotubeComposition() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getClassification() {
		return PHYSICAL_CHARACTERIZATION;
	}

	public String getName() {
		return PHYSICAL_COMPOSITION;
	}

	public Float getAverageLength() {
		return averageLength;
	}

	public void setAverageLength(Float averageLength) {
		this.averageLength = averageLength;
	}

	public String getChirality() {
		return chirality;
	}

	public void setChirality(String chirality) {
		this.chirality = chirality;
	}

	public Float getGrowthDiameter() {
		return growthDiameter;
	}

	public String getWallType() {
		return wallType;
	}

	public void setWallType(String wallType) {
		this.wallType = wallType;
	}

	public void setGrowthDiameter(Float growthDiameter) {
		this.growthDiameter = growthDiameter;
	}
}

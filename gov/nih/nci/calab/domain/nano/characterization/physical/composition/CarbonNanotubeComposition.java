package gov.nih.nci.calab.domain.nano.characterization.physical.composition;

public class CarbonNanotubeComposition extends ParticleComposition {

	private static final long serialVersionUID = 1234567890L;

	private String chirality;

	private Float growthDiameter;

	private Float averageLength;

	private String wallType;

	public CarbonNanotubeComposition() {
	}

	public String getClassification() {
		return PHYSICAL_CHARACTERIZATION;
	}

	public String getName() {
		return PHYSICAL_COMPOSITION;
	}

	public Float getAverageLength() {
		return this.averageLength;
	}

	public void setAverageLength(Float averageLength) {
		this.averageLength = averageLength;
	}

	public String getChirality() {
		return this.chirality;
	}

	public void setChirality(String chirality) {
		this.chirality = chirality;
	}

	public Float getGrowthDiameter() {
		return this.growthDiameter;
	}

	public String getWallType() {
		return this.wallType;
	}

	public void setWallType(String wallType) {
		this.wallType = wallType;
	}

	public void setGrowthDiameter(Float growthDiameter) {
		this.growthDiameter = growthDiameter;
	}
}

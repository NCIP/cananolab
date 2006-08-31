/**
 * 
 */
package gov.nih.nci.calab.dto.characterization.composition;


/**
 * This class represents properties of a Carbon Nanotube composition to be shown in
 * the view page.
 * @author zeng, pansu
 *
 */
public class CarbonNanotubeBean extends CompositionBean {
	private String chirality;
	
	private String growthDiameter;
	
	private String averageLength;
	
	private String wallType;
	
	public CarbonNanotubeBean(){
		super();
	}

	public String getAverageLength() {
		return averageLength;
	}

	public void setAverageLength(String averageLength) {
		this.averageLength = averageLength;
	}

	public String getGrowthDiameter() {
		return growthDiameter;
	}

	public void setGrowthDiameter(String growthDiameter) {
		this.growthDiameter = growthDiameter;
	}

	public String getChirality() {
		return chirality;
	}

	public void setChirality(String chirality) {
		this.chirality = chirality;
	}

	public String getWallType() {
		return wallType;
	}

	public void setWallType(String wallType) {
		this.wallType = wallType;
	}
	
	
}

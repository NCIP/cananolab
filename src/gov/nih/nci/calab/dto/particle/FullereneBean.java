/**
 * 
 */
package gov.nih.nci.calab.dto.particle;

/**
 * @author Zeng
 *
 */
public class FullereneBean extends ParticleBean {
	private String chirality;
	
	private String growthDiameter;
	
	private String averageLength;
	
	public FullereneBean(){
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
	
	
}

/**
 * 
 */
package gov.nih.nci.calab.dto.particle;

/**
 * @author Zeng
 *
 */
public class FullereneBean extends ParticleBean {
	private String carnality;
	
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

	public String getCarnality() {
		return carnality;
	}

	public void setCarnality(String carnality) {
		this.carnality = carnality;
	}

	public String getGrowthDiameter() {
		return growthDiameter;
	}

	public void setGrowthDiameter(String growthDiameter) {
		this.growthDiameter = growthDiameter;
	}
	
	
}

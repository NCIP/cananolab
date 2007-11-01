/**
 * 
 */
package gov.nih.nci.calab.dto.characterization.composition;

import gov.nih.nci.calab.domain.nano.characterization.physical.composition.CarbonNanotubeComposition;

/**
 * This class represents properties of a Carbon Nanotube composition to be shown
 * in the view page.
 * 
 * @author zeng, pansu
 * 
 */
public class CarbonNanotubeBean extends CompositionBean {
	private String chirality;

	private String growthDiameter;

	private String averageLength;

	private String wallType;

	public CarbonNanotubeBean() {
		
	}

	public CarbonNanotubeBean(CarbonNanotubeComposition carbonNanotube) {
		super(carbonNanotube);
		this.setId(carbonNanotube.getId().toString());
		this.averageLength = (carbonNanotube.getAverageLength() == null) ? ""
				: carbonNanotube.getAverageLength().toString();
		this.chirality = (carbonNanotube.getChirality() == null) ? ""
				: carbonNanotube.getChirality().toString();
		this.growthDiameter = (carbonNanotube.getGrowthDiameter() == null) ? ""
				: carbonNanotube.getGrowthDiameter().toString();
		this.wallType = carbonNanotube.getWallType();
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

	public CarbonNanotubeComposition getDomainObj() {
		CarbonNanotubeComposition doComp = new CarbonNanotubeComposition();
		super.updateDomainObj(doComp);

		if (averageLength.length() > 0) {
			doComp.setAverageLength(new Float(averageLength));
		}
		doComp.setChirality(chirality);

		if (growthDiameter.length() > 0) {
			doComp.setGrowthDiameter(new Float(growthDiameter));
		}
		doComp.setWallType(wallType);

		return doComp;
	}
}

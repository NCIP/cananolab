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
		return this.averageLength;
	}

	public void setAverageLength(String averageLength) {
		this.averageLength = averageLength;
	}

	public String getGrowthDiameter() {
		return this.growthDiameter;
	}

	public void setGrowthDiameter(String growthDiameter) {
		this.growthDiameter = growthDiameter;
	}

	public String getChirality() {
		return this.chirality;
	}

	public void setChirality(String chirality) {
		this.chirality = chirality;
	}

	public String getWallType() {
		return this.wallType;
	}

	public void setWallType(String wallType) {
		this.wallType = wallType;
	}

	public CarbonNanotubeComposition getDomainObj() {
		CarbonNanotubeComposition doComp = new CarbonNanotubeComposition();
		super.updateDomainObj(doComp);

		if (this.averageLength.length() > 0) {
			doComp.setAverageLength(new Float(this.averageLength));
		}
		doComp.setChirality(this.chirality);

		if (this.growthDiameter.length() > 0) {
			doComp.setGrowthDiameter(new Float(this.growthDiameter));
		}
		doComp.setWallType(this.wallType);

		return doComp;
	}
}

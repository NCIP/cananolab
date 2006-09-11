/**
 * 
 */
package gov.nih.nci.calab.dto.characterization.composition;

import java.util.ArrayList;
import java.util.List;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.CarbonNanotubeComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.ComposingElement;

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
		this.setId(carbonNanotube.getId().toString());
		this.averageLength = (carbonNanotube.getAverageLength() == null) ? ""
				: carbonNanotube.getAverageLength().toString();
		this.chirality = (carbonNanotube.getChirality() == null) ? ""
				: carbonNanotube.getChirality().toString();
		this.growthDiameter = (carbonNanotube.getGrowthDiameter() == null) ? ""
				: carbonNanotube.getGrowthDiameter().toString();
		this.wallType = carbonNanotube.getWallType();
		List<ComposingElementBean> elementBeans = new ArrayList<ComposingElementBean>();
		for (ComposingElement element : carbonNanotube
				.getComposingElementCollection()) {
			ComposingElementBean elementBean = new ComposingElementBean(element);
			elementBeans.add(elementBean);
		}
		this.setComposingElements(elementBeans);
		this.setNumberOfElements(elementBeans.size() + "");
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

	public Characterization getDomainObj() {

		CarbonNanotubeComposition doComp = new CarbonNanotubeComposition();
		if (averageLength.length() > 0) {
			doComp.setAverageLength(new Float(averageLength));
		}		
		doComp.setChirality(chirality);	

		if (growthDiameter.length() > 0) {
			doComp.setGrowthDiameter(new Float(growthDiameter));
		}
		if (getId()!=null&&getId().length() > 0) {
			doComp.setId(new Long(getId()));
		}
		doComp.setWallType(wallType);
		doComp.setSource(getCharacterizationSource());
		doComp.setIdentificationName(getViewTitle());
		doComp.setDescription(getDescription());
		for (ComposingElementBean element : getComposingElements()) {
			doComp.getComposingElementCollection().add(element.getDomainObj());
		}
		return doComp;
	}
}

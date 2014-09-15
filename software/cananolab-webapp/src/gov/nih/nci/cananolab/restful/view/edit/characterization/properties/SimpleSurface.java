package gov.nih.nci.cananolab.restful.view.edit.characterization.properties;

import gov.nih.nci.cananolab.domain.characterization.physical.Surface;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;

import javax.servlet.http.HttpServletRequest;

public class SimpleSurface extends SimpleCharacterizationProperty {
	String isHydrophobic;  //user doesn't have to choose yes/no. could be empty

	
	@Override
	public void setLookups(HttpServletRequest request) {
		// No lookup 
		
	}

	@Override
	public void transferFromPropertyBean(HttpServletRequest request, CharacterizationBean charBean)
			throws Exception {
		super.transferFromPropertyBean(request, charBean);
		Surface surface = charBean.getSurface();
		if (surface.getIsHydrophobic() == null)
			isHydrophobic = "";
		else 
			isHydrophobic = String.valueOf(surface.getIsHydrophobic());
	}


	@Override
	public void transferToPropertyBean(CharacterizationBean charBean)
			throws Exception {
		
		Surface surface = charBean.getSurface();
		if (this.isHydrophobic != null && this.isHydrophobic.length() > 0) 
			surface.setIsHydrophobic(Boolean.valueOf(this.isHydrophobic));
	}



	public String getIsHydrophobic() {
		return isHydrophobic;
	}

	public void setIsHydrophobic(String isHydrophobic) {
		this.isHydrophobic = isHydrophobic;
	}
	
	
}

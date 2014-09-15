package gov.nih.nci.cananolab.restful.view.edit.characterization.properties;

import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;

import javax.servlet.http.HttpServletRequest;

public class SimpleSurface extends SimpleCharacterizationProperty {
	String isHydrophobic;  //user doesn't have to choose yes/no. could be empty

	
	@Override
	public void setLookups(HttpServletRequest request) {
		// TODO Auto-generated method stub
		
	}
	
	

	@Override
	public void transferFromPropertyBean(HttpServletRequest request, CharacterizationBean charBean)
			throws Exception {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void transferToPropertyBean(CharacterizationBean charBean)
			throws Exception {
		// TODO Auto-generated method stub
		
	}



	public String getIsHydrophobic() {
		return isHydrophobic;
	}

	public void setIsHydrophobic(String isHydrophobic) {
		this.isHydrophobic = isHydrophobic;
	}
	
	
}

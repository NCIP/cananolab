package gov.nih.nci.cananolab.restful.view.characterization.properties;

import gov.nih.nci.cananolab.domain.characterization.physical.Surface;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.annotate.JsonTypeName;

@JsonTypeName("SimpleSurface")
public class SimpleSurface extends SimpleCharacterizationProperty {
	String isHydrophobic;  //user doesn't have to choose yes/no. could be empty
	
	List<String> isHydrophobicOptions = new ArrayList<String>();

	
	@Override
	public void setLookups(HttpServletRequest request) {
		isHydrophobicOptions.add("yes");
		isHydrophobicOptions.add("no");
	}

	@Override
	public void transferFromPropertyBean(HttpServletRequest request, CharacterizationBean charBean, boolean needOptions)
			throws Exception {
		super.transferFromPropertyBean(request, charBean, needOptions);
		Surface surface = charBean.getSurface();
		if (surface.getIsHydrophobic() == null)
			isHydrophobic = "";
		else 
			isHydrophobic = String.valueOf(surface.getIsHydrophobic());
		
		if (needOptions)
			setLookups(request);
	}


	@Override
	public void transferToPropertyBean(CharacterizationBean charBean)
			throws Exception {
		
		Surface surface = charBean.getSurface();
		if (this.isHydrophobic != null && this.isHydrophobic.length() > 0) 
			surface.setIsHydrophobic(Boolean.valueOf(this.isHydrophobic));
	}



	@Override
	public List<String> getPropertyViewTitles() {
		List<String> vals = new ArrayList<String>();
		vals.add("Is Hydrophobic?");
		return vals;
	}

	@Override
	public List<String> getPropertyViewValues() {
		List<String> vals = new ArrayList<String>();
		vals.add(this.isHydrophobic);
		return vals;
	}

	public String getIsHydrophobic() {
		return isHydrophobic;
	}

	public void setIsHydrophobic(String isHydrophobic) {
		this.isHydrophobic = isHydrophobic;
	}

	public List<String> getIsHydrophobicOptions() {
		return isHydrophobicOptions;
	}

	public void setIsHydrophobicOptions(List<String> isHydrophobicOptions) {
		this.isHydrophobicOptions = isHydrophobicOptions;
	}
	
	
}

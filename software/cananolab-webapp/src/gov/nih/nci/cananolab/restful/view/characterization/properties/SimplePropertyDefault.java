package gov.nih.nci.cananolab.restful.view.characterization.properties;

import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;

import java.util.List;

import org.codehaus.jackson.annotate.JsonTypeName;

@JsonTypeName("SimplePropertyDefault")
public class SimplePropertyDefault extends SimpleCharacterizationProperty {
	
	public SimplePropertyDefault() {
		super();
	}
	public SimplePropertyDefault(String input) {
		super();
	}
	@Override
	public void transferToPropertyBean(CharacterizationBean charBean)
			throws Exception {
		// TODO Auto-generated method stub
		
	}
	@Override
	public List<String> getPropertyViewTitles() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<String> getPropertyViewValues() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}

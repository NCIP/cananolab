package gov.nih.nci.cananolab.restful.view.edit.characterization.properties;

import gov.nih.nci.cananolab.domain.characterization.invitro.EnzymeInduction;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.restful.core.InitSetup;
import gov.nih.nci.cananolab.restful.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;

public class SimpleEnzymeInduction extends SimpleCharacterizationProperty{
	String enzymeName = "";
	
	List<String> enzymeNameOptions = new ArrayList<String>(); 

	@Override
	public void setLookups(HttpServletRequest request) 
	throws Exception {
		SortedSet<String> types = InitSetup.getInstance().getDefaultAndOtherTypesByLookup(request,
				"enzymeNames", "enzyme induction", "enzyme", "otherEnzyme",
				true);
		
		if (types != null)
			enzymeNameOptions.addAll(types);
		CommonUtil.addOtherToList(enzymeNameOptions);
		
	}

	@Override
	public void transferFromPropertyBean(HttpServletRequest request, CharacterizationBean charBean)
			throws Exception {
		super.transferFromPropertyBean(request, charBean);
		
		EnzymeInduction enzyme = charBean.getEnzymeInduction();
		
		if (enzyme.getEnzyme() != null)
			this.enzymeName = enzyme.getEnzyme();
		
	}



	@Override
	public void transferToPropertyBean(CharacterizationBean charBean)
			throws Exception {
		// TODO Auto-generated method stub
		
	}



	public String getEnzymeName() {
		return enzymeName;
	}

	public void setEnzymeName(String enzymeName) {
		this.enzymeName = enzymeName;
	}

	public List<String> getEnzymeNameOptions() {
		return enzymeNameOptions;
	}

	public void setEnzymeNameOptions(List<String> enzymeNameOptions) {
		this.enzymeNameOptions = enzymeNameOptions;
	}

	
}

package gov.nih.nci.cananolab.restful.view.characterization.properties;

import gov.nih.nci.cananolab.domain.characterization.invitro.EnzymeInduction;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.restful.core.InitSetup;
import gov.nih.nci.cananolab.restful.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.annotate.JsonTypeName;

@JsonTypeName("SimpleEnzymeInduction")
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
	public void transferFromPropertyBean(HttpServletRequest request, CharacterizationBean charBean, boolean needOptions)
			throws Exception {
		super.transferFromPropertyBean(request, charBean, needOptions);
		
		EnzymeInduction enzyme = charBean.getEnzymeInduction();
		
		if (enzyme.getEnzyme() != null)
			this.enzymeName = enzyme.getEnzyme();
		
		if (needOptions)
			this.setLookups(request);
	}



	@Override
	public void transferToPropertyBean(CharacterizationBean charBean)
			throws Exception {
		EnzymeInduction enzyme = charBean.getEnzymeInduction();
		String name = (this.enzymeName == null) ? "" : this.enzymeName;
		enzyme.setEnzyme(name);
	}


	@Override
	public List<String> getPropertyViewTitles() {
		List<String> titles = new ArrayList<String>();
		titles.add("Enzyme Name");
		return titles;
	}

	@Override
	public List<String> getPropertyViewValues() {
		List<String> vals = new ArrayList<String>();
		vals.add(this.enzymeName);
		return vals;
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

package gov.nih.nci.cananolab.restful.view.edit.characterization.properties;

import gov.nih.nci.cananolab.restful.core.InitSetup;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;

public class SimpleEnzymeInduction extends SimpleCharacterizationProperty{
	String enzymeName;
	
	List<String> enzymeNameOptions = new ArrayList<String>(); //

	@Override
	public void setLookups(HttpServletRequest request) 
	throws Exception {
		SortedSet<String> types = InitSetup.getInstance().getDefaultAndOtherTypesByLookup(request,
				"enzymeNames", "enzyme induction", "enzyme", "otherEnzyme",
				true);
		
		if (types != null)
			enzymeNameOptions.addAll(types);
		
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

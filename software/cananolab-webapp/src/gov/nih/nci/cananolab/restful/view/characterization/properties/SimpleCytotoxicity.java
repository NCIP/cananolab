package gov.nih.nci.cananolab.restful.view.characterization.properties;

import gov.nih.nci.cananolab.domain.characterization.invitro.Cytotoxicity;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.annotate.JsonTypeName;

@JsonTypeName("SimpleCytotoxicity")
public class SimpleCytotoxicity extends SimpleCharacterizationProperty {
	String cellLine;

	public String getCellLine() {
		return cellLine;
	}

	public void setCellLine(String cellLine) {
		this.cellLine = cellLine;
	}

	@Override
	public void setLookups(HttpServletRequest request) 
	throws Exception {
		
		//no options needed
	}

	@Override
	public void transferFromPropertyBean(HttpServletRequest request, CharacterizationBean charBean, boolean needOptions)
			throws Exception {
		super.transferFromPropertyBean(request, charBean, false);
		
		Cytotoxicity cyto = charBean.getCytotoxicity();
		if (cyto.getCellLine() != null)
			this.cellLine = cyto.getCellLine();
			
	}

	@Override
	public void transferToPropertyBean(CharacterizationBean charBean)
			throws Exception {
		Cytotoxicity cyto = charBean.getCytotoxicity();
		
		if (this.cellLine != null)
			cyto.setCellLine(this.cellLine);
	}

	@Override
	public List<String> getPropertyViewTitles() {
		List<String> titles = new ArrayList<String>();
		titles.add("Cell Line");
		
		return titles;
	}

	@Override
	public List<String> getPropertyViewValues() {
		List<String> vals = new ArrayList<String>();
		vals.add(this.cellLine);
		
		return vals;
	}
	
	
}

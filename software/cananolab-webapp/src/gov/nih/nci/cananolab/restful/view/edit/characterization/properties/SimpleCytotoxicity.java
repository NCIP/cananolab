package gov.nih.nci.cananolab.restful.view.edit.characterization.properties;

import gov.nih.nci.cananolab.domain.characterization.invitro.Cytotoxicity;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;

import javax.servlet.http.HttpServletRequest;

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
	public void transferFromPropertyBean(HttpServletRequest request, CharacterizationBean charBean)
			throws Exception {
		super.transferFromPropertyBean(request, charBean);
		
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
	
	
}

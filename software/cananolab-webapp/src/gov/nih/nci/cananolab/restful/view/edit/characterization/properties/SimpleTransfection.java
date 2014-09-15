package gov.nih.nci.cananolab.restful.view.edit.characterization.properties;

import javax.servlet.http.HttpServletRequest;

public class SimpleTransfection extends SimpleCharacterizationProperty {
	String cellLine;
	
	

	@Override
	public void setLookups(HttpServletRequest request) {
		// TODO Auto-generated method stub
		
	}

	public String getCellLine() {
		return cellLine;
	}

	public void setCellLine(String cellLine) {
		this.cellLine = cellLine;
	}
	
	
}

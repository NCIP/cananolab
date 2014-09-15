package gov.nih.nci.cananolab.restful.view.edit.characterization.properties;

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
		// TODO Auto-generated method stub
		
		//no options needed
	}
	
	
}

package gov.nih.nci.cananolab.restful.view;

import java.util.SortedMap;
import java.util.TreeMap;

public class SimpleCharacterizationsByTypeBean {
	
	String type;
	SortedMap<String, Object> charsByAssayType = new TreeMap<String, Object>();
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public SortedMap<String, Object> getCharsByAssayType() {
		return charsByAssayType;
	}
	public void setCharsByAssayType(SortedMap<String, Object> charsByAssayType) {
		this.charsByAssayType = charsByAssayType;
	}
	
	

}

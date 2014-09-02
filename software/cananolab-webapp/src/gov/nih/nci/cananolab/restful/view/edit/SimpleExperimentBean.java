package gov.nih.nci.cananolab.restful.view.edit;

import gov.nih.nci.cananolab.restful.core.InitSetup;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;

public class SimpleExperimentBean {
	
	long id;
	String displayName;
	String abbreviation;
	String description;
	
	List<SimpleInstrumentBean> instruments;

	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getAbbreviation() {
		return abbreviation;
	}
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<SimpleInstrumentBean> getInstruments() {
		return instruments;
	}
	public void setInstruments(List<SimpleInstrumentBean> instruments) {
		this.instruments = instruments;
	}
	
}

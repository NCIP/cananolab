package gov.nih.nci.cananolab.restful.view.edit;

import gov.nih.nci.cananolab.restful.validator.PatternMatchIfNotNullNotEmpty;

public class SimpleOrganizationBean {
	
	long id;
	
	@PatternMatchIfNotNullNotEmpty(regexpName="textFieldWhiteList", messageSource="sample", messageKey="organization.name.invalid")
	String name = "";
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	

}

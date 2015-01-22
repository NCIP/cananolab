package gov.nih.nci.cananolab.restful.view.edit;

import gov.nih.nci.cananolab.restful.validator.PatternMatchIfNotNullNotEmpty;

public class SimpleAddressBean {
	
	@PatternMatchIfNotNullNotEmpty(regexpName="textFieldWhiteList", messageSource="sample", messageKey="organization.address1.invalid")
	String line1 = "";
	
	@PatternMatchIfNotNullNotEmpty(regexpName="textFieldWhiteList", messageSource="sample", messageKey="organization.address2.invalid")
	String line2 = "";
	
	@PatternMatchIfNotNullNotEmpty(regexpName="relaxedAlphabetic", messageSource="sample", messageKey="organization.city.invalid")
	String city = "";
	
	@PatternMatchIfNotNullNotEmpty(regexpName="relaxedAlphabetic", messageSource="sample", messageKey="organization.state.invalid")
	String stateProvince = "";
	
	@PatternMatchIfNotNullNotEmpty(regexpName="zip", messageSource="sample", messageKey="postalCode.invalid")
	String zip = "";
	
	@PatternMatchIfNotNullNotEmpty(regexpName="relaxedAlphabetic", messageSource="sample", messageKey="organization.country.invalid")
	String country = "";
	
	public String getLine1() {
		return line1;
	}
	public void setLine1(String line1) {
		this.line1 = line1;
	}
	public String getLine2() {
		return line2;
	}
	public void setLine2(String line2) {
		this.line2 = line2;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getStateProvince() {
		return stateProvince;
	}
	public void setStateProvince(String stateProvince) {
		this.stateProvince = stateProvince;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	
	

}

package gov.nih.nci.cananolab.restful.view.edit;

import gov.nih.nci.cananolab.restful.validator.CustomPattern;

public class SimpleAddressBean {
	
	String line1 = "";
	String line2 = "";
	String city = "";
	String stateProvince = "";
	
	@CustomPattern(regexp="^(\\d{5}(-\\d{4})?)|([a-zA-Z0-9\\s])$")
	String zip = "";
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

package gov.nih.nci.cananolab.restful.view.edit;

import gov.nih.nci.cananolab.domain.common.Organization;
import gov.nih.nci.cananolab.restful.validator.PatternMatchIfNotNullNotEmpty;

public class SimpleOrganizationBean {
	
	long id;
	
	@PatternMatchIfNotNullNotEmpty(regexpName="textFieldWhiteList", messageSource="sample", messageKey="organization.name.invalid")
	String name = "";
	
	SimpleAddressBean address;
	
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
	public SimpleAddressBean getAddress() {
		return address;
	}
	public void setAddress(SimpleAddressBean address) {
		this.address = address;
	}
	
	public void transferOrganizationData(Organization domainOrg) {
		
		if (domainOrg == null)
			return;
		
		setName(domainOrg.getName());
		setId(domainOrg.getId());
		
		SimpleAddressBean simpleAddress = new SimpleAddressBean();
		
		simpleAddress.setLine1(domainOrg.getStreetAddress1());
		simpleAddress.setLine2(domainOrg.getStreetAddress2());
		simpleAddress.setCity(domainOrg.getCity());
		simpleAddress.setStateProvince(domainOrg.getState());
		simpleAddress.setCountry(domainOrg.getCountry());
		simpleAddress.setZip(domainOrg.getPostalCode());
		
		this.setAddress(simpleAddress);
	}
}

package gov.nih.nci.cananolab.restful.view.edit;

public class SimplePointOfContactBean {
	
	long id;
	boolean isPrimaryContact;
	String contactPerson = "";
	
	SimpleOrganizationBean organization;
	
	String role = "";
	
	SimpleAddressBean address;
	
	String firstName = "";
	String lastName = "";
	String middleInitial = "";
	
	String phoneNumber = "";
	String email = "";
	
	long sampleId;
	
	public boolean isPrimaryContact() {
		return isPrimaryContact;
	}
	public void setPrimaryContact(boolean isPrimaryContact) {
		this.isPrimaryContact = isPrimaryContact;
	}
	public String getContactPerson() {
		return contactPerson;
	}
	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}
	
	public SimpleOrganizationBean getOrganization() {
		return organization;
	}
	public void setOrganization(SimpleOrganizationBean organization) {
		this.organization = organization;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public SimpleAddressBean getAddress() {
		return address;
	}
	public void setAddress(SimpleAddressBean address) {
		this.address = address;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getMiddleInitial() {
		return middleInitial;
	}
	public void setMiddleInitial(String middleInitial) {
		this.middleInitial = middleInitial;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getSampleId() {
		return sampleId;
	}
	public void setSampleId(long sampleId) {
		this.sampleId = sampleId;
	}
	
	
	//available org name objects?


}

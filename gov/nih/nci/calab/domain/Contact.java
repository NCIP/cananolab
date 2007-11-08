package gov.nih.nci.calab.domain;

/**
 * <!-- LICENSE_TEXT_START -->
 * <!-- LICENSE_TEXT_END -->
 */

/**
 * 
 */

public class Contact implements java.io.Serializable {
	private static final long serialVersionUID = 1234567890L;

	private java.lang.Long id;

	public java.lang.Long getId() {
		return this.id;
	}

	public void setId(java.lang.Long id) {
		this.id = id;
	}

	private java.lang.String firstName;

	public java.lang.String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(java.lang.String firstName) {
		this.firstName = firstName;
	}

	private java.lang.String lastName;

	public java.lang.String getLastName() {
		return this.lastName;
	}

	public void setLastName(java.lang.String lastName) {
		this.lastName = lastName;
	}

	private java.lang.String title;

	public java.lang.String getTitle() {
		return this.title;
	}

	public void setTitle(java.lang.String title) {
		this.title = title;
	}

	private java.lang.String phoneNumber;

	public java.lang.String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(java.lang.String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	private java.lang.String email;

	public java.lang.String getEmail() {
		return this.email;
	}

	public void setEmail(java.lang.String email) {
		this.email = email;
	}

	private java.util.Date updateDate;

	public java.util.Date getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(java.util.Date updateDate) {
		this.updateDate = updateDate;
	}

	private java.lang.String middleName;

	public java.lang.String getMiddleName() {
		return this.middleName;
	}

	public void setMiddleName(java.lang.String middleName) {
		this.middleName = middleName;
	}

	private java.lang.String fax;

	public java.lang.String getFax() {
		return this.fax;
	}

	public void setFax(java.lang.String fax) {
		this.fax = fax;
	}

	private java.lang.String address;

	public java.lang.String getAddress() {
		return this.address;
	}

	public void setAddress(java.lang.String address) {
		this.address = address;
	}

	private java.lang.String city;

	public java.lang.String getCity() {
		return this.city;
	}

	public void setCity(java.lang.String city) {
		this.city = city;
	}

	private java.lang.String state;

	public java.lang.String getState() {
		return this.state;
	}

	public void setState(java.lang.String state) {
		this.state = state;
	}

	private java.lang.String country;

	public java.lang.String getCountry() {
		return this.country;
	}

	public void setCountry(java.lang.String country) {
		this.country = country;
	}

	private java.lang.String postalCode;

	public java.lang.String getPostalCode() {
		return this.postalCode;
	}

	public void setPostalCode(java.lang.String postalCode) {
		this.postalCode = postalCode;
	}

	private java.lang.String piName;

	public java.lang.String getPiName() {
		return this.piName;
	}

	public void setPiName(java.lang.String piName) {
		this.piName = piName;
	}

	private java.util.Collection sourceCollection = new java.util.HashSet();

	public java.util.Collection getSourceCollection() {
		// try {
		// if (sourceCollection.size() == 0) {
		// }
		// } catch (Exception e) {
		// ApplicationService applicationService = ApplicationServiceProvider
		// .getApplicationService();
		// try {
		//
		// gov.nih.nci.calab.domain.Contact thisIdSet = new
		// gov.nih.nci.calab.domain.Contact();
		// thisIdSet.setId(this.getId());
		// java.util.Collection resultList = applicationService.search(
		// "gov.nih.nci.calab.domain.Source", thisIdSet);
		// sourceCollection = resultList;
		// return resultList;
		//
		// } catch (Exception ex) {
		// System.out
		// .println("Contact:getSourceCollection throws exception ... ...");
		// ex.printStackTrace();
		// }
		// }
		return this.sourceCollection;
	}

	public void setSourceCollection(java.util.Collection sourceCollection) {
		this.sourceCollection = sourceCollection;
	}

	public boolean equals(Object obj) {
		boolean eq = false;
		if (obj instanceof Contact) {
			Contact c = (Contact) obj;
			Long thisId = getId();

			if (thisId != null && thisId.equals(c.getId())) {
				eq = true;
			}

		}
		return eq;
	}

	public int hashCode() {
		int h = 0;

		if (getId() != null) {
			h += getId().hashCode();
		}

		return h;
	}

}
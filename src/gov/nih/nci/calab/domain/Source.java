package gov.nih.nci.calab.domain;

/**
 * <!-- LICENSE_TEXT_START -->
 * <!-- LICENSE_TEXT_END -->
 */

/**
 * 
 */

public class Source implements java.io.Serializable {
	private static final long serialVersionUID = 1234567890L;

	private java.lang.Long id;

	public java.lang.Long getId() {
		return this.id;
	}

	public void setId(java.lang.Long id) {
		this.id = id;
	}

	private java.lang.String organizationName;

	public java.lang.String getOrganizationName() {
		return this.organizationName;
	}

	public void setOrganizationName(java.lang.String organizationName) {
		this.organizationName = organizationName;
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

	private java.util.Collection contactCollection = new java.util.HashSet();

	public java.util.Collection getContactCollection() {
		// try {
		// if (contactCollection.size() == 0) {
		// }
		// } catch (Exception e) {
		// ApplicationService applicationService = ApplicationServiceProvider
		// .getApplicationService();
		// try {
		//
		// gov.nih.nci.calab.domain.Source thisIdSet = new
		// gov.nih.nci.calab.domain.Source();
		// thisIdSet.setId(this.getId());
		// java.util.Collection resultList = applicationService.search(
		// "gov.nih.nci.calab.domain.Contact", thisIdSet);
		// contactCollection = resultList;
		// return resultList;
		//
		// } catch (Exception ex) {
		// System.out
		// .println("Source:getContactCollection throws exception ... ...");
		// ex.printStackTrace();
		// }
		// }
		return this.contactCollection;
	}

	public void setContactCollection(java.util.Collection contactCollection) {
		this.contactCollection = contactCollection;
	}

	private java.util.Collection sampleCollection = new java.util.HashSet();

	public java.util.Collection getSampleCollection() {
		// try {
		// if (sampleCollection.size() == 0) {
		// }
		// } catch (Exception e) {
		// ApplicationService applicationService = ApplicationServiceProvider
		// .getApplicationService();
		// try {
		//
		// gov.nih.nci.calab.domain.Source thisIdSet = new
		// gov.nih.nci.calab.domain.Source();
		// thisIdSet.setId(this.getId());
		// java.util.Collection resultList = applicationService.search(
		// "gov.nih.nci.calab.domain.Sample", thisIdSet);
		// sampleCollection = resultList;
		// return resultList;
		//
		// } catch (Exception ex) {
		// System.out
		// .println("Source:getSampleCollection throws exception ... ...");
		// ex.printStackTrace();
		// }
		// }
		return this.sampleCollection;
	}

	public void setSampleCollection(java.util.Collection sampleCollection) {
		this.sampleCollection = sampleCollection;
	}

	public boolean equals(Object obj) {
		boolean eq = false;
		if (obj instanceof Source) {
			Source c = (Source) obj;
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
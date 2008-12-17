/**
 * 
 */
package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.Organization;
import gov.nih.nci.cananolab.domain.common.PointOfContact;

/**
 * PointOfContact view bean
 * 
 * @author tanq, cais
 * 
 */

// TODO: need to revise PointOfContactBean, copy from OrganizationBean
public class PointOfContactBean {
	private PointOfContact domain;
	private String displayName;
	private Organization organization;
	private String[] visibilityGroups = new String[0];
	private boolean hidden = false;
	private String pocId;
	private String address;

	// TODO: need info for nanoparticleSample??

	public PointOfContactBean() {
		super();
		domain = new PointOfContact();
		organization = new Organization();
		domain.setOrganization(organization);
		displayName = "";
	}

	public PointOfContactBean(PointOfContact pointOfContact) {
		domain = pointOfContact;
		organization = pointOfContact.getOrganization();
	}

	/**
	 * @return the domain
	 */
	public PointOfContact getDomain() {
		return domain;
	}

	/**
	 * @param domain
	 *            the domain to set
	 */
	public void setDomain(PointOfContact domain) {
		this.domain = domain;
	}

	/**
	 * @return the pocId
	 */
	public String getPocId() {
		return pocId;
	}

	/**
	 * @param pocId
	 *            the pocId to set
	 */
	public void setPocId(String pocId) {
		this.pocId = pocId;
	}

	/**
	 * @return the pocs
	 */
	public Organization getOrganization() {
		return organization;
	}

	/**
	 * @param pocs
	 *            the pocs to set
	 */
	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	/**
	 * @return the visibilityGroups
	 */
	public String[] getVisibilityGroups() {
		return visibilityGroups;
	}

	/**
	 * @param visibilityGroups
	 *            the visibilityGroups to set
	 */
	public void setVisibilityGroups(String[] visibilityGroups) {
		this.visibilityGroups = visibilityGroups;
	}

	/**
	 * @return the hidden
	 */
	public boolean isHidden() {
		return hidden;
	}

	/**
	 * @param hidden
	 *            the hidden to set
	 */
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		if (hidden) {
			return "private";
		}
		String firstName = domain.getFirstName();
		displayName = "";
		if (firstName != null) {
			displayName = firstName + " ";
		}
		String lastName = domain.getLastName();
		if (lastName != null) {
			displayName += lastName;
		}
		if (domain.getOrganization() != null) {
			String orgName = domain.getOrganization().getName();
			if (orgName!=null && displayName.trim().length()>0) {
				displayName += " (" + orgName + ")";
			}
			else {
				displayName=orgName;
			}
		}
		return displayName;
	}

	/**
	 * @param name
	 *            the displayName to set
	 */
	public void setDisplayName(String name) {
		displayName = name;
	}

}

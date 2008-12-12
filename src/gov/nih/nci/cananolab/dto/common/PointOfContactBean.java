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

//TODO: need to revise PointOfContactBean, copy from OrganizationBean

public class PointOfContactBean{
	private PointOfContact domain;
	private String POCName;
	private Organization organization;
	private String[] orgVisibilityGroups = new String[0];
	private String[] pocVisibilityGroups = new String[0];
	private String[] emailVisibilityGroups = new String[0];
	private String[] phoneVisibilityGroups = new String[0];
	private boolean hidden = false;
	private String address;
	//TODO: need info for nanoparticleSample??
	
	
	public PointOfContactBean() {
		super();
		domain = new PointOfContact();
		organization = new Organization();
		domain.setOrganization(organization);
		POCName = "";
	}

	public PointOfContactBean(PointOfContact pointOfContact) {
		domain = pointOfContact;
		organization =
			pointOfContact.getOrganization();
		//TODO:: the following not needed??
		String firstName = domain.getFirstName();
		POCName = "";
		if (firstName!=null) {
			POCName = firstName +" ";
		}
		String lastName = domain.getLastName();
		if (lastName!=null) {
			POCName+=lastName;
		}
		if (domain.getOrganization()!=null) {
			String orgName = domain.getOrganization().getName();
			if (orgName!=null) {
				POCName+=" ("+orgName+")";
			}
		}
	}
	
	/**
	 * @return the domain
	 */
	public PointOfContact getDomain() {
		return domain;
	}

	/**
	 * @param domain the domain to set
	 */
	public void setDomain(PointOfContact domain) {
		this.domain = domain;
	}

	/**
	 * @return the pocs
	 */
	public Organization getOrganization() {
		return organization;
	}

	/**
	 * @param pocs the pocs to set
	 */
	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	/**
	 * @return the orgVisibilityGroups
	 */
	public String[] getOrgVisibilityGroups() {
		return orgVisibilityGroups;
	}

	/**
	 * @param orgVisibilityGroups the orgVisibilityGroups to set
	 */
	public void setOrgVisibilityGroups(String[] orgVisibilityGroups) {
		this.orgVisibilityGroups = orgVisibilityGroups;
	}

	/**
	 * @return the pocVisibilityGroups
	 */
	public String[] getPocVisibilityGroups() {
		return pocVisibilityGroups;
	}

	/**
	 * @param pocVisibilityGroups the pocVisibilityGroups to set
	 */
	public void setPocVisibilityGroups(String[] pocVisibilityGroups) {
		this.pocVisibilityGroups = pocVisibilityGroups;
	}

	/**
	 * @return the emailVisibilityGroups
	 */
	public String[] getEmailVisibilityGroups() {
		return emailVisibilityGroups;
	}

	/**
	 * @param emailVisibilityGroups the emailVisibilityGroups to set
	 */
	public void setEmailVisibilityGroups(String[] emailVisibilityGroups) {
		this.emailVisibilityGroups = emailVisibilityGroups;
	}

	/**
	 * @return the phoneVisibilityGroups
	 */
	public String[] getPhoneVisibilityGroups() {
		return phoneVisibilityGroups;
	}

	/**
	 * @param phoneVisibilityGroups the phoneVisibilityGroups to set
	 */
	public void setPhoneVisibilityGroups(String[] phoneVisibilityGroups) {
		this.phoneVisibilityGroups = phoneVisibilityGroups;
	}

	/**
	 * @return the hidden
	 */
	public boolean isHidden() {
		return hidden;
	}

	/**
	 * @param hidden the hidden to set
	 */
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	/**
	 * @return the pOCName
	 */
	public String getPOCName() {
		String firstName = domain.getFirstName();
		POCName = "";
		if (firstName!=null) {
			POCName = firstName +" ";
		}
		String lastName = domain.getLastName();
		if (lastName!=null) {
			POCName+=lastName;
		}
		if (domain.getOrganization()!=null) {
			String orgName = domain.getOrganization().getName();
			if (orgName!=null) {
				POCName+=" ("+orgName+")";
			}
		}
		return POCName;
	}

	/**
	 * @param name the pOCName to set
	 */
	public void setPOCName(String name) {
		POCName = name;
	}	
	
	
	
	
	
}

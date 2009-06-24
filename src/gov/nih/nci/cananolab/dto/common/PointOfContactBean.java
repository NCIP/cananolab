/**
 *
 */
package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.Organization;
import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * PointOfContact view bean
 *
 * @author tanq, cais
 *
 */

public class PointOfContactBean {
	private PointOfContact domain = new PointOfContact();
	private String displayName = "";
	private String[] visibilityGroups = new String[0];
	private String pocId;
	private Boolean primaryStatus = false;

	public PointOfContactBean() {
		domain.setOrganization(new Organization());
	}

	public PointOfContactBean(PointOfContact pointOfContact) {
		domain = pointOfContact;
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
	 * @return the displayName
	 */
	public String getDisplayName() {
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
			if (orgName != null && displayName.trim().length() > 0) {
				displayName += " (" + orgName + ")";
			} else {
				displayName = orgName;
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

	public void setupDomain(String createdBy) {
		// always update createdBy and createdDate
		if (domain.getId() == null) {
			domain.setCreatedBy(createdBy);
			domain.setCreatedDate(new Date());
		}
		if (domain.getOrganization().getId() == null) {
			domain.getOrganization().setCreatedBy(createdBy);
			domain.setCreatedDate(new Date());
		}
	}

	public Boolean getPrimaryStatus() {
		return primaryStatus;
	}

	public void setPrimaryStatus(Boolean primaryStatus) {
		this.primaryStatus = primaryStatus;
	}

	public boolean equals(Object obj) {
		boolean eq = false;
		if (obj instanceof PointOfContactBean) {
			PointOfContactBean p = (PointOfContactBean) obj;
			Long thisId = this.getDomain().getId();
			if (thisId != null && thisId.equals(p.getDomain().getId())) {
				eq = true;
			}
		}
		return eq;
	}

	public String getPersonDisplayName() {
		List<String> nameStrs = new ArrayList<String>();
		nameStrs.add(domain.getFirstName());
		nameStrs.add(domain.getMiddleInitial());
		nameStrs.add(domain.getLastName());
		String name = StringUtils.join(nameStrs, " ");
		nameStrs = new ArrayList<String>();
		nameStrs.add(name);
		nameStrs.add(domain.getEmail());
		nameStrs.add(domain.getPhone());
		return StringUtils.join(nameStrs, "<br>");
	}

	public String getOrganizationDisplayName() {
		List<String> orgStrs = new ArrayList<String>();
		if (domain.getOrganization() != null) {
			orgStrs.add(domain.getOrganization().getName());
			orgStrs.add(domain.getOrganization().getStreetAddress1());
			orgStrs.add(domain.getOrganization().getStreetAddress2());

			List<String> addressStrs = new ArrayList<String>();
			addressStrs.add(domain.getOrganization().getCity());
			addressStrs.add(domain.getOrganization().getState());
			addressStrs.add(domain.getOrganization().getPostalCode());

			orgStrs.add(StringUtils.join(addressStrs, " "));
			return StringUtils.join(orgStrs, "<br>");
		} else {
			return "";
		}
	}
}

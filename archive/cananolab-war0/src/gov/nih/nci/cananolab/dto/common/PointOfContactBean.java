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
 * @author tanq, cais, pansu
 *
 */

public class PointOfContactBean {
	private PointOfContact domain = new PointOfContact();
	private String displayName = "";
	private String[] visibilityGroups = new String[0];
	private Boolean primaryStatus = true;

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
		if (domain == null) {
			return "";
		}
		String orgName = "";
		if (domain.getOrganization() != null) {
			orgName = domain.getOrganization().getAbbreviatedName();
		}
		List<String> nameStrs = new ArrayList<String>();
		nameStrs.add(domain.getFirstName());
		nameStrs.add(domain.getMiddleInitial());
		nameStrs.add(domain.getLastName());
		String personName = StringUtils.join(nameStrs, " ");
		if (personName.length() > 0) {
			displayName = orgName + " (" + personName + ")";
		} else {
			displayName = orgName;
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
		if (domain.getId()!=null && domain.getId() == 0) {
			domain.setId(null);
		}
		if (domain.getId() == null) {
			domain.setCreatedBy(createdBy);
			domain.setCreatedDate(new Date());
		}
		if (domain.getOrganization().getId() == null) {
			domain.getOrganization().setCreatedBy(createdBy);
			domain.getOrganization().setCreatedDate(new Date());
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
		if (domain == null) {
			return "";
		}
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
		if (domain == null) {
			return "";
		}
		List<String> orgStrs = new ArrayList<String>();
		if (domain.getOrganization() != null) {
			orgStrs.add(domain.getOrganization().getAbbreviatedName());
			orgStrs.add(domain.getOrganization().getStreetAddress1());
			orgStrs.add(domain.getOrganization().getStreetAddress2());

			List<String> addressStrs = new ArrayList<String>();
			addressStrs.add(domain.getOrganization().getCity());
			addressStrs.add(domain.getOrganization().getState());
			addressStrs.add(domain.getOrganization().getPostalCode());
			addressStrs.add(domain.getOrganization().getCountry());

			orgStrs.add(StringUtils.join(addressStrs, " "));
			return StringUtils.join(orgStrs, "<br>");
		} else {
			return "";
		}
	}
}

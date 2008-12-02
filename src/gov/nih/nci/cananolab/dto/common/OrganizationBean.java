/**
 * 
 */
package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.Organization;
import gov.nih.nci.cananolab.domain.common.PointOfContact;

import java.util.ArrayList;
import java.util.List;


/**
 * Organization view bean
 * 
 * @author tanq
 * 
 */
public class OrganizationBean{
	Organization domain;
	private List<PointOfContact> pocs = new ArrayList<PointOfContact>(20);
	private String[] visibilityGroups = new String[0];

	/**
	 * @return the domain
	 */
	public Organization getDomain() {
		return domain;
	}

	/**
	 * @param domain the domain to set
	 */
	public void setDomain(Organization domain) {
		this.domain = domain;
	}

	/**
	 * @return the pocs
	 */
	public List<PointOfContact> getPocs() {
		return pocs;
	}

	/**
	 * @param pocs the pocs to set
	 */
	public void setPocs(List<PointOfContact> pocs) {
		this.pocs = pocs;
	}

	/**
	 * @return the visibilityGroups
	 */
	public String[] getVisibilityGroups() {
		return visibilityGroups;
	}

	/**
	 * @param visibilityGroups the visibilityGroups to set
	 */
	public void setVisibilityGroups(String[] visibilityGroups) {
		this.visibilityGroups = visibilityGroups;
	}
	
}

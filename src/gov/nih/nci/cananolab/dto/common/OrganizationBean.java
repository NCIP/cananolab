/**
 * 
 */
package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.Organization;
import gov.nih.nci.cananolab.domain.common.PointOfContact;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Organization view bean
 * 
 * @author tanq
 * 
 */
public class OrganizationBean{
	private Organization domain;
	private List<PointOfContact> pocs = new ArrayList<PointOfContact>(20);
	private String[] visibilityGroups = new String[0];
	private boolean hidden = false;
	private String address;	
	
	public OrganizationBean() {
		super();
		domain = new Organization();
//		pocs.add(new PointOfContact());
	}

	public OrganizationBean(Organization organization) {
		domain = organization;
		Collection<PointOfContact> pointOfContactCollection =
			organization.getPointOfContactCollection();
		if (pointOfContactCollection!=null &&
				pointOfContactCollection.size()>0) {
			List<PointOfContact> pointOfContactList = new ArrayList<PointOfContact>(pointOfContactCollection);
			Collections.sort(pointOfContactList, 
					new Comparator<PointOfContact>() {
			    public int compare(PointOfContact o1, PointOfContact o2) {
			        return (int)(o1.getCreatedDate().compareTo(o2.getCreatedDate()));
			    }});
			pocs = pointOfContactList;
		}
	}
	
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
	
	private String getOrgAddress(Organization orga) {
		StringBuffer addressBuf = new StringBuffer();
		addressBuf.append(orga.getStreetAddress1() + " ");
		String street2 = orga.getStreetAddress2();
		if(street2 != null && street2.length() > 0 )
			addressBuf.append(orga.getStreetAddress2() + " ");
		
		addressBuf.append(orga.getCity() + " ");
		addressBuf.append(orga.getState() + " ");
		addressBuf.append(orga.getPostalCode());
		return addressBuf.toString();
	}
	
	public String getAddress() {
		return getOrgAddress(domain);
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void addPointOfContact() {
		pocs.add(new PointOfContact());
	}
	
	public void removePointOfContact(int ind) {
		pocs.remove(ind);
	}
}

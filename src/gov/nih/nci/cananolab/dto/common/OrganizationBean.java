/**
 * 
 */
package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.Author;
import gov.nih.nci.cananolab.domain.common.Organization;
import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.domain.common.Publication;

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
	Organization domain;
	private List<PointOfContact> pocs = new ArrayList<PointOfContact>(20);
	private String[] visibilityGroups = new String[0];
	private boolean hidden = false;
	public OrganizationBean() {
		super();
		domain = new Organization();
		pocs.add(new PointOfContact());
	}

	public OrganizationBean(Organization organization) {
		domain = organization;
		//TODO: POCs
//		Collection<Author> authorCollection =
//			publication.getAuthorCollection();
//		if (authorCollection!=null &&
//				authorCollection.size()>0) {
//			List<Author> authorslist = new ArrayList<Author>(authorCollection);
//			Collections.sort(authorslist, 
//					new Comparator<Author>() {
//			    public int compare(Author o1, Author o2) {
//			        return (int)(o1.getCreatedDate().compareTo(o2.getCreatedDate()));
//			    }});
//			authors = authorslist;
//		}
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
	
}

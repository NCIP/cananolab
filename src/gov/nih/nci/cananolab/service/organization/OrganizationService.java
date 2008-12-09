package gov.nih.nci.cananolab.service.organization;

import gov.nih.nci.cananolab.domain.common.Organization;
import gov.nih.nci.cananolab.dto.common.OrganizationBean;
import gov.nih.nci.cananolab.exception.PointOfContactException;

import java.util.Collection;
import java.util.List;

/**
 * Interface defining methods invovled in submiting and searching organizations.
 * 
 * @author tanq
 * 
 */
public interface OrganizationService {

	/**
	 * Persist a new organization or update an existing organizations
	 * @param particleId
	 * @param primaryOrganization
	 * @param otherOrganizationCollection
	 * 
	 * @throws PointOfContactException
	 */	
	public void saveOrganization(Organization primaryOrganization, 
			Collection<Organization> otherOrganizationCollection) 
		throws PointOfContactException;
	
	public OrganizationBean findPrimaryOrganization(String particleId)
		throws PointOfContactException;	
	
	public List<OrganizationBean> findOtherOrganizationCollection(String particleId)
		throws PointOfContactException;	
	
	
}

package gov.nih.nci.cananolab.service.common;

import gov.nih.nci.cananolab.domain.common.Organization;
import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.DuplicateEntriesException;
import gov.nih.nci.cananolab.exception.PointOfContactException;

import java.util.Collection;
import java.util.List;

/**
 * Interface defining methods invovled in submiting and searching organizations.
 * 
 * @author tanq
 * 
 */
public interface PointOfContactService {

	/**
	 * Persist a new organization or update an existing organizations
	 * 
	 * @param particleId
	 * @param primaryPointOfContact
	 * @param otherPointOfContactCollection
	 * 
	 * @throws PointOfContactException
	 */
	public void savePointOfContact(PointOfContact primaryPointOfContact,
			Collection<PointOfContact> otherPointOfContactCollection)
			throws PointOfContactException, DuplicateEntriesException;

	public PointOfContactBean findPrimaryPointOfContact(String particleId)
			throws PointOfContactException;

	public List<PointOfContactBean> findOtherPointOfContactCollection(
			String particleId) throws PointOfContactException;

	public PointOfContactBean findPointOfContactById(String pocId)
			throws PointOfContactException;

	public void retrieveAccessibility(PointOfContactBean pocBean, UserBean user)
			throws PointOfContactException;
	
	public PointOfContact loadPOCNanoparticleSample(PointOfContact poc, String nanoparticleSampleCollection) 
		throws PointOfContactException;
	public Organization findOrganizationByName(String orgName) throws PointOfContactException;
}

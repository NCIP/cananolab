package gov.nih.nci.cananolab.service.sample;

import gov.nih.nci.cananolab.domain.common.Organization;
import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.DuplicateEntriesException;
import gov.nih.nci.cananolab.exception.PointOfContactException;

import java.util.Collection;
import java.util.List;
import java.util.SortedSet;

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
	 * @param sampleId
	 * @param primaryPointOfContact
	 * @param otherPointOfContactCollection
	 *
	 * @throws PointOfContactException
	 */
	public void savePointOfContact(PointOfContact primaryPointOfContact,
			Collection<PointOfContact> otherPointOfContactCollection)
			throws PointOfContactException, DuplicateEntriesException;

	public List<PointOfContactBean> findOtherPointOfContactCollection(
			String sampleId) throws PointOfContactException;

	public PointOfContactBean findPointOfContactById(String pocId)
			throws PointOfContactException;

	public void setVisibility(PointOfContactBean pocBean, UserBean user)
			throws PointOfContactException;

	public PointOfContact loadPOCSample(PointOfContact poc,
			String sampleCollection) throws PointOfContactException;

	public Organization findOrganizationByName(String orgName)
			throws PointOfContactException;

	public SortedSet<PointOfContact> findAllPointOfContacts()
			throws PointOfContactException;

	public List<PointOfContactBean> findPointOfContactsBySampleId(String sampleId)
			throws PointOfContactException;

	public void saveOtherPOCs(Sample sample) throws PointOfContactException;
}

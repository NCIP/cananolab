package gov.nih.nci.cananolab.service.common.impl;

import gov.nih.nci.cagrid.cananolab.client.CaNanoLabServiceClient;
import gov.nih.nci.cananolab.domain.common.Organization;
import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.PointOfContactException;
import gov.nih.nci.cananolab.service.common.PointOfContactService;

import java.util.Collection;
import java.util.List;
import java.util.SortedSet;

import org.apache.log4j.Logger;

/**
 * Remote implementation of PointOfContactService
 *
 * @author tanq
 *
 */
public class PointOfContactServiceRemoteImpl implements PointOfContactService {
	private static Logger logger = Logger
			.getLogger(PointOfContactServiceRemoteImpl.class);
	private CaNanoLabServiceClient gridClient;

	public PointOfContactServiceRemoteImpl(String serviceUrl) throws Exception {
		gridClient = new CaNanoLabServiceClient(serviceUrl);
	}

	/**
	 * Persist a new organization or update an existing organizations
	 *
	 * @param primaryPointOfContact
	 * @param otherPointOfContactCollection
	 *
	 * @throws PointOfContactException
	 */
	public void savePointOfContact(PointOfContact primaryPointOfContact,
			Collection<PointOfContact> otherPointOfContactCollection)
			throws PointOfContactException {
		throw new PointOfContactException("not implemented for grid service.");
	}

	public List<PointOfContactBean> findOtherPointOfContactCollection(
			String sampleId) {
		// TODO: grid findOrganizationsBySampleId
		return null;
	}

	public PointOfContactBean findPointOfContactById(String pocId) {
		// TODO: grid findOrganizationsBySampleId
		return null;
	}

	public void retrieveAccessibility(PointOfContactBean pocBean, UserBean user)
			throws PointOfContactException {
		throw new PointOfContactException("not implemented for grid service.");
	}

	public PointOfContact loadPOCSample(PointOfContact poc,
			String sampleCollection) throws PointOfContactException {
		throw new PointOfContactException("not implemented for grid service.");
	}

	public Organization findOrganizationByName(String orgName)
			throws PointOfContactException {
		throw new PointOfContactException("not implemented for grid service.");
	}

	public SortedSet<PointOfContact> findAllPointOfContacts()
			throws PointOfContactException {
		throw new PointOfContactException("not implemented for grid service.");
	}

	public List<PointOfContactBean> findPointOfContactsBySampleId(String sampleId)
			throws PointOfContactException {
		throw new PointOfContactException("not implemented for grid service.");
	}
}

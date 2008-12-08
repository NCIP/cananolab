package gov.nih.nci.cananolab.service.organization.impl;

import gov.nih.nci.cagrid.cananolab.client.CaNanoLabServiceClient;
import gov.nih.nci.cananolab.domain.common.Organization;
import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.dto.common.OrganizationBean;
import gov.nih.nci.cananolab.exception.OrganizationException;
import gov.nih.nci.cananolab.service.organization.OrganizationService;

import java.util.List;

import org.apache.log4j.Logger;

/**
 * Remote implementation of OrganizationService
 * 
 * @author tanq
 * 
 */
public class OrganizationServiceRemoteImpl implements OrganizationService {
	private static Logger logger = Logger
			.getLogger(OrganizationServiceRemoteImpl.class);
	private CaNanoLabServiceClient gridClient;

	
	public OrganizationServiceRemoteImpl(String serviceUrl) throws Exception {
		gridClient = new CaNanoLabServiceClient(serviceUrl);
	}

	/**
	 * Persist a new organization or update an existing organizations
	 * 
	 * @param primaryOrganization
	 * @param otherOrganizationCollection
	 * 
	 * @throws OrganizationException
	 */
	public void saveOrganization(OrganizationBean primaryOrganization, 
			List<OrganizationBean> otherOrganizationCollection)
		throws OrganizationException{
		throw new OrganizationException("not implemented for grid service.");
	}

	public OrganizationBean findPrimaryOrganization(String particleId){
		//TODO: grid findPrimaryOrganization
		return null;
	}
	
	public List<OrganizationBean> findOtherOrganizationCollection(String particleId){
		//TODO: grid findOrganizationsByParticleSampleId
		return null;
	}	
	
}

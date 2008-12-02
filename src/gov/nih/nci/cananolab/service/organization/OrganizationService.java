package gov.nih.nci.cananolab.service.organization;

import gov.nih.nci.cananolab.domain.common.Organization;
import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.dto.common.OrganizationBean;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.exception.OrganizationException;

import java.io.IOException;
import java.io.OutputStream;
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
	 * 
	 * @param primaryOrganization
	 * @param otherOrganizationCollection
	 * 
	 * @throws OrganizationException
	 */	
	public void saveOrganization(OrganizationBean primaryOrganization, 
			List<OrganizationBean> otherOrganizationCollection) 
		throws OrganizationException;
	
	public OrganizationBean findPrimaryOrganization(String particleId)
		throws OrganizationException;	
	
	public List<OrganizationBean> findOtherOrganizationCollection(String particleId)
		throws OrganizationException;	
	
	
}

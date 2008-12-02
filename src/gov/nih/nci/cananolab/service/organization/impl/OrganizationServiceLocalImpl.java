package gov.nih.nci.cananolab.service.organization.impl;

import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.dto.common.OrganizationBean;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.exception.OrganizationException;
import gov.nih.nci.cananolab.service.organization.OrganizationService;
import gov.nih.nci.cananolab.service.organization.helper.OrganizationServiceHelper;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

/**
 * Local implementation of SourceService
 * 
 * @author tanq
 * 
 */
public class OrganizationServiceLocalImpl implements OrganizationService {
	private static Logger logger = Logger
			.getLogger(OrganizationServiceLocalImpl.class);
	private OrganizationServiceHelper helper = new OrganizationServiceHelper();

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
//		try {
//			FileService fileService = new FileServiceLocalImpl();
//			fileService.prepareSaveFile(publication);
//			NanoparticleSampleService sampleService = new NanoparticleSampleServiceLocalImpl();
//			Set<NanoparticleSample> particleSamples = new HashSet<NanoparticleSample>();
//			if (particleNames != null && particleNames.length > 0) {
//				for (String name : particleNames) {
//					NanoparticleSample sample = sampleService
//							.findNanoparticleSampleByName(name);
//					particleSamples.add(sample);
//				}
//			}
//
//			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
//					.getApplicationService();
//
//			if (publication.getNanoparticleSampleCollection() == null) {
//				publication
//						.setNanoparticleSampleCollection(new HashSet<NanoparticleSample>());
//			} else {
//				publication.getNanoparticleSampleCollection().clear();
//			}
//			for (NanoparticleSample sample : particleSamples) {
//				publication.getNanoparticleSampleCollection().add(sample);
//				sample.getPublicationCollection().add(publication);
//			}
//
//			AuthorizationService authService = new AuthorizationService(
//					CaNanoLabConstants.CSM_APP_NAME);
//
//			if (publication.getAuthorCollection() == null) {
//				publication.setAuthorCollection(new HashSet<Author>());
//			} else {
//				for (Author author : publication.getAuthorCollection()) {
//					if (author.getId() != null)
//						authService
//								.removePublicGroup(author.getId().toString());
//				}
//				publication.getAuthorCollection().clear();
//			}
//			if (authors != null) {
//				Calendar myCal = Calendar.getInstance();
//				for (Author author : authors) {
//					if (!StringUtils.isBlank(author.getFirstName())
//							|| !StringUtils.isBlank(author.getLastName())
//							|| !StringUtils.isBlank(author.getInitial())) {
//						if (author.getCreatedDate() == null) {
//							myCal.add(Calendar.SECOND, 1);
//							author.setCreatedDate(myCal.getTime());
//						}
//						publication.getAuthorCollection().add(author);
//					}
//				}
//			}
//			appService.saveOrUpdate(publication);
//			fileService.writeFile(publication, fileData);
//
//		} catch (Exception e) {
//			String err = "Error in saving the publication.";
//			logger.error(err, e);
//			throw new PublicationException(err, e);
//		}
	}

	public List<OrganizationBean> findOtherOrganizationCollection(String particleId)
		throws OrganizationException{
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria
					.forClass(Publication.class);
			crit.createAlias("nanoparticleSampleCollection", "sample",
					CriteriaSpecification.LEFT_JOIN);
			crit.add(Restrictions.eq("sample.id", new Long(particleId)));
			
			crit
					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

			List results = appService.query(crit);
			List<PublicationBean> publicationCollection = new ArrayList<PublicationBean>();
			for (Object obj : results) {
				Publication publication = (Publication) obj;
//				if (loadAuthor) {
//					publicationCollection.add(new PublicationBean(publication,
//							false, true));
//				} else {
//					publicationCollection.add(new PublicationBean(publication));
//				}
			}
			//TODO, Qina
			return null;
		} catch (Exception e) {
			String err = "Problem finding publication collections with the given particle ID.";
			logger.error(err, e);
			throw new OrganizationException(err, e);
		}
	}
	
	public OrganizationBean findPrimaryOrganization(String particleId)
		throws OrganizationException{
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria
					.forClass(Publication.class);
			crit.createAlias("nanoparticleSampleCollection", "sample",
					CriteriaSpecification.LEFT_JOIN);
			crit.add(Restrictions.eq("sample.id", new Long(particleId)));
			
			crit
					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

			List results = appService.query(crit);
			List<PublicationBean> publicationCollection = new ArrayList<PublicationBean>();
			for (Object obj : results) {
				Publication publication = (Publication) obj;
//				if (loadAuthor) {
//					publicationCollection.add(new PublicationBean(publication,
//							false, true));
//				} else {
//					publicationCollection.add(new PublicationBean(publication));
//				}
			}
			//TODO, Qina
			return null;
		} catch (Exception e) {
			String err = "Problem finding publication collections with the given particle ID.";
			logger.error(err, e);
			throw new OrganizationException(err, e);
		}
	}

}

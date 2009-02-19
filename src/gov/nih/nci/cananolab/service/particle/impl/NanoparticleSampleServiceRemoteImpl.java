package gov.nih.nci.cananolab.service.particle.impl;

import gov.nih.nci.cagrid.cananolab.client.CaNanoLabServiceClient;
import gov.nih.nci.cagrid.cqlquery.Association;
import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.cagrid.cqlquery.QueryModifier;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;
import gov.nih.nci.cananolab.domain.common.Keyword;
import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.dto.particle.composition.CompositionBean;
import gov.nih.nci.cananolab.exception.DuplicateEntriesException;
import gov.nih.nci.cananolab.exception.ParticleException;
import gov.nih.nci.cananolab.service.particle.NanoparticleCharacterizationService;
import gov.nih.nci.cananolab.service.particle.NanoparticleCompositionService;
import gov.nih.nci.cananolab.service.particle.NanoparticleSampleService;
import gov.nih.nci.cananolab.service.publication.PublicationService;
import gov.nih.nci.cananolab.service.publication.impl.PublicationServiceRemoteImpl;
import gov.nih.nci.cananolab.util.CaNanoLabComparators;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;
import gov.nih.nci.cananolab.util.SortableName;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.SortedSet;

import org.apache.log4j.Logger;

/**
 * Remote grid client implementation of NanoparticleSampleSearchInterface
 *
 * @author pansu
 *
 */
public class NanoparticleSampleServiceRemoteImpl implements
		NanoparticleSampleService {
	private static Logger logger = Logger
			.getLogger(NanoparticleSampleServiceRemoteImpl.class);
	private CaNanoLabServiceClient gridClient;
	private String serviceUrl;

	public NanoparticleSampleServiceRemoteImpl(String serviceUrl)
			throws Exception {
		gridClient = new CaNanoLabServiceClient(serviceUrl);
		this.serviceUrl = serviceUrl;
	}

	/**
	 *
	 * @param particlePointOfContact
	 * @param nanoparticleEntityClassNames
	 * @param otherNanoparticleTypes
	 * @param functionalizingEntityClassNames
	 * @param otherFunctionalizingEntityTypes
	 * @param functionClassNames
	 * @param otherFunctionTypes
	 * @param characterizationClassNames
	 * @param wordList
	 * @return
	 * @throws ParticleException
	 */
	// public List<ParticleBean> findNanoparticleSamplesBy(String
	// particlePointOfContact,
	// String[] nanoparticleEntityClassNames,
	// String[] otherNanoparticleTypes,
	// String[] functionalizingEntityClassNames,
	// String[] otherFunctionalizingEntityTypes,
	// String[] functionClassNames, String[] otherFunctionTypes,
	// String[] characterizationClassNames, String[] wordList)
	// throws ParticleException {
	// List<ParticleBean> particles = new ArrayList<ParticleBean>();
	// try {
	// NanoparticleSample[] particleSamples = gridClient
	// .getNanoparticleSamplesBy(particlePointOfContact,
	// nanoparticleEntityClassNames,
	// functionalizingEntityClassNames,
	// functionClassNames, characterizationClassNames,
	// wordList);
	// if (particleSamples != null) {
	// for (NanoparticleSample particleSample : particleSamples) {
	// // manually fetch the associated source
	// loadSourceForParticleSample(particleSample);
	// ParticleBean particleBean = new ParticleBean(particleSample);
	// loadParticleBeanAssociationClassNames(particleBean);
	// particles.add(particleBean);
	// }
	// Collections.sort(particles,
	// new CaNanoLabComparators.ParticleBeanComparator());
	// }
	// return particles;
	// } catch (RemoteException e) {
	// logger.error(CaNanoLabConstants.NODE_UNAVAILABLE, e);
	// throw new ParticleException(CaNanoLabConstants.NODE_UNAVAILABLE, e);
	// } catch (Exception e) {
	// String err = "Problem finding particles with the given search
	// parameters.";
	// logger.error(err, e);
	// throw new ParticleException(err, e);
	// }
	// }
	public List<ParticleBean> findNanoparticleSamplesBy(
			String particlePointOfContact,
			String[] nanoparticleEntityClassNames,
			String[] otherNanoparticleTypes,
			String[] functionalizingEntityClassNames,
			String[] otherFunctionalizingEntityTypes,
			String[] functionClassNames, String[] otherFunctionTypes,
			String[] characterizationClassNames, String[] wordList)
			throws ParticleException {
		List<ParticleBean> particles = new ArrayList<ParticleBean>();
		try {
			String[] particleSampleStrs = gridClient
					.getNanoparticleSampleViewStrs(particlePointOfContact,
							nanoparticleEntityClassNames,
							functionalizingEntityClassNames,
							functionClassNames, characterizationClassNames,
							wordList);
			if (particleSampleStrs != null) {
				String[] columns = null;
				for (String particleSampleStr : particleSampleStrs) {
					columns = particleSampleStr
							.split(CaNanoLabConstants.VIEW_COL_DELIMITER);
					NanoparticleSample particleSample = new NanoparticleSample();
					// id
					particleSample.setId(new Long(columns[0]));
					// sample name
					particleSample.setName(columns[1]);
					// primary poc
					PointOfContact poc = new PointOfContact();
					poc.setFirstName(columns[2]);
					poc.setLastName(columns[3]);
					poc.getOrganization().setName(columns[4]);
					particleSample.setPrimaryPointOfContact(poc);
					ParticleBean particleBean = new ParticleBean(particleSample);
					// composition, set all compositions as NanoparticleEntity
					// for now
					if (columns.length > 5 && columns[5] != null
							&& columns[5].length() > 0) {
						String[] compositionsClazzNames = columns[5]
								.split(CaNanoLabConstants.VIEW_CLASSNAME_DELIMITER);
						if (compositionsClazzNames != null) {
							particleBean
									.setNanoparticleEntityClassNames(compositionsClazzNames);
						}
					}
					// functionClassNames
					if (columns.length > 6 && columns[6] != null
							&& columns[6].length() > 0) {
						String[] functionClazzNames = columns[6]
								.split(CaNanoLabConstants.VIEW_CLASSNAME_DELIMITER);
						if (functionClazzNames != null) {
							particleBean
									.setFunctionClassNames(functionClazzNames);
						}
					}

					// characterizationClassNames
					if (columns.length > 7 && columns[7] != null
							&& columns[7].length() > 0) {
						String[] characterizationClazzNames = columns[7]
								.split(CaNanoLabConstants.VIEW_CLASSNAME_DELIMITER);
						if (characterizationClazzNames != null) {
							particleBean
									.setCharacterizationClassNames(characterizationClazzNames);
						}
					}
					particles.add(particleBean);
				}
				Collections.sort(particles,
						new CaNanoLabComparators.ParticleBeanComparator());
			}
			return particles;
		} catch (RemoteException e) {
			logger.error(CaNanoLabConstants.NODE_UNAVAILABLE, e);
			throw new ParticleException(CaNanoLabConstants.NODE_UNAVAILABLE, e);
		} catch (Exception e) {
			String err = "Problem finding particles with the given search parameters.";
			logger.error(err, e);
			throw new ParticleException(err, e);
		}
	}

	private void loadParticleBeanAssociationClassNames(ParticleBean particleBean)
			throws Exception {

		String particleId = particleBean.getDomainParticleSample().getId()
				.toString();
		String[] functionClassNames = gridClient
				.getFunctionClassNamesByParticleId(particleId);
		particleBean.setFunctionClassNames(functionClassNames);
		String[] characterizationClassNames = gridClient
				.getCharacterizationClassNamesByParticleId(particleId);
		particleBean.setCharacterizationClassNames(characterizationClassNames);
		String[] nanoparticleEntityClassNames = gridClient
				.getNanoparticleEntityClassNamesByParticleId(particleId);
		particleBean
				.setNanoparticleEntityClassNames(nanoparticleEntityClassNames);
		String[] functionalizingEntityClassNames = gridClient
				.getFunctionalizingEntityClassNamesByParticleId(particleId);
		particleBean
				.setFunctionalizingEntityClassNames(functionalizingEntityClassNames);
	}

	/**
	 * Get all the associated data of a nanoparticle sample
	 *
	 * @param particleSample
	 * @throws Exception
	 */
	private void loadParticleSamplesAssociations(
			NanoparticleSample particleSample) throws Exception {
		String particleId = particleSample.getId().toString();
		// source
		loadPointOfContactForParticleSample(particleSample);
		// keyword
		loadKeywordsForParticleSample(particleSample);

		// characterization
		NanoparticleCharacterizationService charService = new NanoparticleCharacterizationServiceRemoteImpl(
				serviceUrl);
		List<CharacterizationBean> characterizationCollection = charService
				.findCharsByParticleSampleId(particleId);
		// TODO fix remote method
		// particleSample
		// .setCharacterizationCollection(new HashSet<Characterization>(
		// characterizationCollection));
		NanoparticleCompositionService compService = new NanoparticleCompositionServiceRemoteImpl(
				serviceUrl);
		CompositionBean compBean = compService
				.findCompositionByParticleSampleId(particleId);
		if (compBean != null) {
			particleSample.setSampleComposition(compBean.getDomain());
		}
		loadPublicationsForParticleSample(particleSample);
	}

	public ParticleBean findNanoparticleSampleById(String particleId)
			throws ParticleException {
		try {
			CQLQuery query = new CQLQuery();
			gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
			target
					.setName("gov.nih.nci.cananolab.domain.particle.NanoparticleSample");
			Attribute attribute = new Attribute();
			attribute.setName("id");
			attribute.setPredicate(Predicate.EQUAL_TO);
			attribute.setValue(particleId);
			target.setAttribute(attribute);
			query.setTarget(target);
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.particle.NanoparticleSample");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			NanoparticleSample particleSample = null;
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				particleSample = (NanoparticleSample) obj;
				loadParticleSamplesAssociations(particleSample);
			}
			ParticleBean particleBean = new ParticleBean(particleSample);
			return particleBean;
		} catch (RemoteException e) {
			logger.error(CaNanoLabConstants.NODE_UNAVAILABLE, e);
			throw new ParticleException(CaNanoLabConstants.NODE_UNAVAILABLE, e);
		} catch (Exception e) {
			String err = "Problem finding the remote particle by id: "
					+ particleId;
			logger.error(err, e);
			throw new ParticleException(err, e);
		}
	}

	public ParticleBean findFullNanoparticleSampleById(String particleId)
			throws ParticleException {
		throw new ParticleException("Not implemented for grid service");
	}

	public NanoparticleSample findNanoparticleSampleByName(String particleName)
			throws ParticleException {
		try {
			CQLQuery query = new CQLQuery();
			gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
			target
					.setName("gov.nih.nci.cananolab.domain.particle.NanoparticleSample");
			Attribute attribute = new Attribute();
			attribute.setName("name");
			attribute.setPredicate(Predicate.EQUAL_TO);
			attribute.setValue(particleName);
			target.setAttribute(attribute);
			query.setTarget(target);
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.particle.NanoparticleSample");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			NanoparticleSample particleSample = null;
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				particleSample = (NanoparticleSample) obj;
				loadParticleSamplesAssociations(particleSample);
			}
			return particleSample;
		} catch (RemoteException e) {
			logger.error(CaNanoLabConstants.NODE_UNAVAILABLE, e);
			throw new ParticleException(CaNanoLabConstants.NODE_UNAVAILABLE, e);
		} catch (Exception e) {
			String err = "Problem finding the particle by name: "
					+ particleName;
			logger.error(err, e);
			throw new ParticleException(err, e);
		}
	}

	public void retrieveVisibility(ParticleBean particleBean, UserBean user)
			throws ParticleException {
		throw new ParticleException("Not implemented for grid service");
	}

	public void saveNanoparticleSample(NanoparticleSample particleSample)
			throws ParticleException, DuplicateEntriesException {
		throw new ParticleException("Not implemented for grid service");
	}

	public void deleteAnnotationById(String className, Long dataId)
			throws ParticleException {
		throw new ParticleException("Not implemented for grid service");
	}

	public SortedSet<PointOfContact> findPointOfContacts()
			throws ParticleException {
		throw new ParticleException("Not implemented for grid service");
	}

	public SortedSet<SortableName> findOtherParticles(
			String particleOrganization, String particleName, UserBean user)
			throws ParticleException {
		throw new ParticleException("Not implemented for grid service");
	}

	public SortedSet<String> findAllNanoparticleSampleNames(UserBean user)
			throws ParticleException {
		throw new ParticleException("Not implemented for grid service");
	}

	/**
	 * load the source for an associated NanoparticleSample
	 *
	 * @param particleId
	 * @return
	 * @throws ParticleException
	 *
	 */
	private void loadPointOfContactForParticleSample(
			NanoparticleSample particleSample) throws Exception {
		CQLQuery query = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target.setName("gov.nih.nci.cananolab.domain.common.PointOfContact");
		Association association = new Association();
		// TODO: verify if primaryNanoparticleSampleCollection need??
		association
				.setName("gov.nih.nci.cananolab.domain.particle.NanoparticleSample");
		association.setRoleName("nanoparticleSampleCollection");

		Attribute attribute = new Attribute();
		attribute.setName("id");
		attribute.setPredicate(Predicate.EQUAL_TO);
		attribute.setValue(particleSample.getId().toString());
		association.setAttribute(attribute);

		target.setAssociation(association);
		query.setTarget(target);
		CQLQueryResults results = gridClient.query(query);
		results
				.setTargetClassname("gov.nih.nci.cananolab.domain.common.PointOfContact");
		CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
		PointOfContact poc = null;
		while (iter.hasNext()) {
			java.lang.Object obj = iter.next();
			poc = (PointOfContact) obj;
		}
		particleSample.setPrimaryPointOfContact(poc);
	}

	/**
	 * load all keywords for an associated NanoparticleSample equal to
	 * particleId
	 *
	 */
	private void loadKeywordsForParticleSample(NanoparticleSample particleSample)
			throws Exception {
		Keyword[] keywords = gridClient
				.getKeywordsByParticleSampleId(particleSample.getId()
						.toString());
		if (keywords != null && keywords.length > 0) {
			particleSample.setKeywordCollection(new HashSet<Keyword>());
			for (Keyword keyword : keywords) {
				particleSample.getKeywordCollection().add(keyword);
			}
		}
	}

	/**
	 * load all publications for an associated NanoparticleSample equal to
	 * particleId
	 *
	 */
	private void loadPublicationsForParticleSample(
			NanoparticleSample particleSample) throws Exception {
		PublicationService publicationService = new PublicationServiceRemoteImpl(
				serviceUrl);
		// publication does not include particle nor author
		List<PublicationBean> publications = publicationService
				.findPublicationsByParticleSampleId(particleSample.getId()
						.toString(), false, false);
		if (publications != null && publications.size() > 0) {
			particleSample.setPublicationCollection(new HashSet<Publication>());
			for (PublicationBean publication : publications) {
				particleSample.getPublicationCollection().add(
						(Publication) publication.getDomainFile());
			}
		}
	}

	public int getNumberOfPublicNanoparticleSamples() throws ParticleException {
		try {
			CQLQuery query = new CQLQuery();
			gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
			target
					.setName("gov.nih.nci.cananolab.domain.particle.NanoparticleSample");
			query.setTarget(target);
			QueryModifier modifier = new QueryModifier();
			modifier.setCountOnly(true);
			query.setQueryModifier(modifier);

			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.particle.NanoparticleSample");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			int count = 0;
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				count = ((Long) obj).intValue();
			}
			return count;
		} catch (Exception e) {
			String err = "Error finding counts of remote public nanoparticle samples.";
			logger.error(err, e);
			throw new ParticleException(err, e);
		}
	}

	public void assignVisibility(ParticleBean particleSampleBean)
			throws ParticleException {
		throw new ParticleException("Not implemented for grid service");
	}

	public List<ParticleBean> getUserAccessibleParticles(
			List<ParticleBean> particles, UserBean user)
			throws ParticleException {
		throw new ParticleException("Not implemented for grid service");
	}

	public SortedSet<String> findParticleNamesByPublicationId(String publicationId)
			throws ParticleException {
		// TODO add implementation detail
		throw new ParticleException("Not implemented for grid service");
	}

	public SortedSet<String> findAllParticleNames() throws ParticleException {
		throw new ParticleException("Not implemented for grid service");
	}
}

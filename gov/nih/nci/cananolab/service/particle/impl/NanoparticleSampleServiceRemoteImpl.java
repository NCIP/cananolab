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
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.domain.common.Report;
import gov.nih.nci.cananolab.domain.common.Source;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.domain.particle.characterization.Characterization;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.SampleComposition;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.exception.DuplicateEntriesException;
import gov.nih.nci.cananolab.exception.ParticleException;
import gov.nih.nci.cananolab.service.particle.NanoparticleCharacterizationService;
import gov.nih.nci.cananolab.service.particle.NanoparticleCompositionService;
import gov.nih.nci.cananolab.service.particle.NanoparticleSampleService;
import gov.nih.nci.cananolab.service.publication.PublicationService;
import gov.nih.nci.cananolab.service.publication.impl.PublicationServiceRemoteImpl;
import gov.nih.nci.cananolab.service.report.ReportService;
import gov.nih.nci.cananolab.service.report.impl.ReportServiceRemoteImpl;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
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
	 * @param particleSource
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
	// particleSource,
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
	// .getNanoparticleSamplesBy(particleSource,
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
	public List<ParticleBean> findNanoparticleSamplesBy(String particleSource,
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
					.getNanoparticleSampleViewStrs(particleSource,
							nanoparticleEntityClassNames,
							functionalizingEntityClassNames,
							functionClassNames, characterizationClassNames,
							wordList);
			// String[] particleSampleStrs = {
			// "35444457~~~NCICB-6~~~DNT~~~Carbon nanotube!!!small
			// molecule~~~therapeutic~~~Enzyme Induction:Molecular
			// Weight:Oxidative Stress",
			// "35445457~~~NCICB-60~~~DNT~~~Carbon nanotube!!!small
			// molecule~~~therapeutic~~~Enzyme Induction:Molecular
			// Weight:Oxidative Stress",
			// };
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
					// source
					Source source = new Source();
					source.setOrganizationName(columns[2]);
					particleSample.setSource(source);
					ParticleBean particleBean = new ParticleBean(particleSample);
					// composition, set all compositions as NanoparticleEntity
					// for now
					if (columns.length>3 && columns[3] != null && columns[3].length() > 0) {
						String[] compositionsClazzNames = columns[3]
								.split(CaNanoLabConstants.VIEW_CLASSNAME_DELIMITER);
						if (compositionsClazzNames != null) {
							particleBean
									.setNanoparticleEntityClassNames(compositionsClazzNames);
						}
					}
					// functionClassNames
					if (columns.length>4 && columns[4] != null && columns[4].length() > 0) {
						String[] functionClazzNames = columns[4]
								.split(CaNanoLabConstants.VIEW_CLASSNAME_DELIMITER);
						if (functionClazzNames != null) {
							particleBean
									.setFunctionClassNames(functionClazzNames);
						}
					}

					// characterizationClassNames
					if (columns.length>5 && columns[5] != null && columns[5].length() > 0) {
						String[] characterizationClazzNames = columns[5]
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
		loadSourceForParticleSample(particleSample);
		// keyword
		loadKeywordsForParticleSample(particleSample);

		// characterization
		NanoparticleCharacterizationService charService = new NanoparticleCharacterizationServiceRemoteImpl(
				serviceUrl);
		List<Characterization> characterizationCollection = charService
				.findCharsByParticleSampleId(particleId);
		particleSample
				.setCharacterizationCollection(new HashSet<Characterization>(
						characterizationCollection));
		NanoparticleCompositionService compService = new NanoparticleCompositionServiceRemoteImpl(
				serviceUrl);
		SampleComposition sampleComposition = compService
				.findCompositionByParticleSampleId(particleId);
		if (sampleComposition != null) {
			particleSample.setSampleComposition(sampleComposition);
		}
		loadReportsForParticleSample(particleSample);
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

	public SortedSet<Source> findAllParticleSources() throws ParticleException {
		throw new ParticleException("Not implemented for grid service");
	}

	public SortedSet<Source> findAllParticleSources(UserBean user)
			throws ParticleException {
		return findAllParticleSources();
	}

	public SortedSet<SortableName> findOtherParticles(String particleSource,
			String particleName, UserBean user) throws ParticleException {
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
	private void loadSourceForParticleSample(NanoparticleSample particleSample)
			throws Exception {
		CQLQuery query = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target.setName("gov.nih.nci.cananolab.domain.common.Source");
		Association association = new Association();
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
				.setTargetClassname("gov.nih.nci.cananolab.domain.common.Source");
		CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
		Source source = null;
		while (iter.hasNext()) {
			java.lang.Object obj = iter.next();
			source = (Source) obj;
		}
		particleSample.setSource(source);
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
	 * load all reports for an associated NanoparticleSample equal to
	 * particleId
	 * 
	 */
	private void loadReportsForParticleSample(NanoparticleSample particleSample)
			throws Exception {
		ReportService reportService = new ReportServiceRemoteImpl(serviceUrl);
		Report[] reports = reportService
				.findReportsByParticleSampleId(particleSample.getId()
						.toString());
		if (reports != null && reports.length > 0) {
			particleSample.setReportCollection(new HashSet<Report>());
			for (Report report : reports) {
				particleSample.getReportCollection().add(report);
			}
		}
	}
	
	/**
	 * load all publications for an associated NanoparticleSample equal to
	 * particleId
	 * 
	 */
	private void loadPublicationsForParticleSample(NanoparticleSample particleSample)
			throws Exception {
		PublicationService publicationService = new PublicationServiceRemoteImpl(serviceUrl);
		Publication[] publications = publicationService
				.findPublicationsByParticleSampleId(particleSample.getId()
						.toString());
		if (publications != null && publications.length > 0) {
			particleSample.setPublicationCollection(new HashSet<Publication>());
			for (Publication publication : publications) {
				particleSample.getPublicationCollection().add(publication);
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

	public void assignAssociatedPublicVisibility(
			AuthorizationService authService, ParticleBean particleSampleBean,
			String[] visibleGroups) throws ParticleException {
		throw new ParticleException("Not implemented for grid service");
	}
}

package gov.nih.nci.cananolab.service.particle.impl;

import gov.nih.nci.cagrid.cananolab.client.CaNanoLabSvcClient;
import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;
import gov.nih.nci.cananolab.domain.common.Source;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.exception.DuplicateEntriesException;
import gov.nih.nci.cananolab.exception.ParticleException;
import gov.nih.nci.cananolab.service.particle.NanoparticleSampleService;
import gov.nih.nci.cananolab.util.SortableName;

import java.util.ArrayList;
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
	private CaNanoLabSvcClient gridClient;

	public NanoparticleSampleServiceRemoteImpl(String serviceUrl)
			throws Exception {
		gridClient = new CaNanoLabSvcClient(serviceUrl);
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
			NanoparticleSample[] particleSamples = gridClient
					.getNanoparticleSamplesBy(particleSource,
							nanoparticleEntityClassNames,
							functionalizingEntityClassNames,
							functionClassNames, characterizationClassNames,
							wordList);
			for (NanoparticleSample particleSample : particleSamples) {
				particles.add(new ParticleBean(particleSample));
			}
			return particles;
		} catch (Exception e) {
			String err = "Problem finding particles with the given search parameters.";
			logger.error(err, e);
			throw new ParticleException(err, e);
		}
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
			}
			ParticleBean particleBean = new ParticleBean(particleSample);
			return particleBean;
		} catch (Exception e) {
			String err = "Problem finding the remote particle by id: "
					+ particleId;
			logger.error(err, e);
			throw new ParticleException(err, e);
		}
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
			}
			return particleSample;
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
}

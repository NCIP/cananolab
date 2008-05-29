package gov.nih.nci.cananolab.service.particle.impl;

import gov.nih.nci.cagrid.cananolab.client.CaNanoLabServiceClient;
import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;
import gov.nih.nci.cananolab.domain.common.DerivedDatum;
import gov.nih.nci.cananolab.domain.common.Instrument;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.domain.particle.characterization.Characterization;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationSummaryBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationSummaryRowBean;
import gov.nih.nci.cananolab.dto.particle.characterization.DerivedBioAssayDataBean;
import gov.nih.nci.cananolab.exception.ParticleCharacterizationException;
import gov.nih.nci.cananolab.exception.ParticleException;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.particle.NanoparticleCharacterizationService;
import gov.nih.nci.cananolab.service.particle.helper.NanoparticleCharacterizationServiceHelper;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.CaNanoLabComparators;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.hibernate.FetchMode;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

/**
 * Service methods involving remote characterizations
 * 
 * @author tanq, pansu
 * 
 */
public class NanoparticleCharacterizationServiceRemoteImpl extends
		NanoparticleCharacterizationServiceBaseImpl implements
		NanoparticleCharacterizationService {
	private static Logger logger = Logger
			.getLogger(NanoparticleCharacterizationServiceRemoteImpl.class);
	private CaNanoLabServiceClient gridClient;

	public NanoparticleCharacterizationServiceRemoteImpl(String serviceUrl)
			throws Exception {
		gridClient = new CaNanoLabServiceClient(serviceUrl);
	}

	public Characterization findCharacterizationById(String charId)
			throws ParticleCharacterizationException {
		try {
			CQLQuery query = new CQLQuery();
			gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
			target
					.setName("gov.nih.nci.cananolab.domain.particle.characterization.Characterization");
			Attribute attribute = new Attribute();
			attribute.setName("id");
			attribute.setPredicate(Predicate.EQUAL_TO);
			attribute.setValue(charId);
			target.setAttribute(attribute);
			query.setTarget(target);
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.particle.characterization.Characterization");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			Characterization achar = null;
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				achar = (Characterization) obj;
				loadCharacterizationAssociations(achar);
			}
			return achar;
		} catch (Exception e) {
			logger.error("Problem finding the characterization by id: "
					+ charId, e);
			throw new ParticleCharacterizationException();
		}
	}

	public void saveCharacterization(NanoparticleSample particleSample,
			Characterization achar) throws Exception {
		throw new ParticleException("Not implemented for grid service");
	}

	public SortedSet<String> findAllCharacterizationSources()
			throws ParticleCharacterizationException {
		throw new ParticleCharacterizationException(
				"Not implemented for grid service");
	}

	public List<Instrument> findAllInstruments()
			throws ParticleCharacterizationException {
		throw new ParticleCharacterizationException(
				"Not implemented for grid service");
	}

	public Instrument findInstrumentBy(String instrumentType,
			String manufacturer) throws ParticleCharacterizationException {
		throw new ParticleCharacterizationException(
				"Not implemented for grid service");
	}

	protected SortedSet<Characterization> findParticleCharacterizationsByClass(
			String particleName, String className)
			throws ParticleCharacterizationException {
		try {
			// TODO implement in grid service
			SortedSet<Characterization> charas = new TreeSet<Characterization>();
			// charas = gridClient.getParticleCharacterizationsByClass(
			// particleName, className);
			return charas;
		} catch (Exception e) {
			String err = "Error getting " + particleName
					+ " characterizations of type " + className;
			logger.error(err, e);
			throw new ParticleCharacterizationException(err, e);
		}
	}

	// set lab file visibility of a characterization
	public void retrieveVisiblity(CharacterizationBean charBean, UserBean user)
			throws ParticleCharacterizationException {
		throw new ParticleCharacterizationException(
				"Not implemented for grid service");
	}

	public void deleteCharacterization(Characterization chara)
			throws ParticleCharacterizationException {
		throw new ParticleCharacterizationException(
				"Not implemented for grid service");
	}

	/**
	 * Get all the associated data of a Characterization
	 * 
	 * @param particleSample
	 * @throws Exception
	 */
	private void loadCharacterizationAssociations(Characterization achar)
			throws Exception {
		String charId = achar.getId().toString();
		// TODO Qina

		/*
		 * CustomizedApplicationService appService =
		 * (CustomizedApplicationService) ApplicationServiceProvider
		 * .getApplicationService();
		 * 
		 * DetachedCriteria crit = DetachedCriteria.forClass(
		 * Characterization.class).add( Property.forName("id").eq(new
		 * Long(charId))); crit.createAlias("derivedBioAssayDataCollection",
		 * "bioassay", CriteriaSpecification.LEFT_JOIN);
		 * crit.createAlias("bioassay.labFile", "file",
		 * CriteriaSpecification.LEFT_JOIN); crit.setFetchMode("protocolFile",
		 * FetchMode.JOIN); crit.setFetchMode("derivedBioAssayDataCollection",
		 * FetchMode.JOIN); crit.setFetchMode("instrumentConfiguration",
		 * FetchMode.JOIN); crit
		 * .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		 * 
		 * List result = appService.query(crit); if (!result.isEmpty()) { achar =
		 * (Characterization) result.get(0); } return achar;
		 */
	}
}
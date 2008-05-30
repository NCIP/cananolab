package gov.nih.nci.cananolab.service.particle.impl;

import gov.nih.nci.cagrid.cananolab.client.CaNanoLabServiceClient;
import gov.nih.nci.cagrid.cqlquery.Association;
import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;
import gov.nih.nci.cananolab.domain.common.DerivedBioAssayData;
import gov.nih.nci.cananolab.domain.common.Instrument;
import gov.nih.nci.cananolab.domain.common.InstrumentConfiguration;
import gov.nih.nci.cananolab.domain.common.ProtocolFile;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.domain.particle.characterization.Characterization;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.exception.ParticleCharacterizationException;
import gov.nih.nci.cananolab.exception.ParticleException;
import gov.nih.nci.cananolab.service.common.impl.FileServiceRemoteImpl;
import gov.nih.nci.cananolab.service.particle.NanoparticleCharacterizationService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;

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
		fileService = new FileServiceRemoteImpl(serviceUrl);
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
			Characterization[] chars = gridClient.getCharacterizationsBy(
					particleName, className);
			SortedSet<Characterization> charSet = new TreeSet<Characterization>();
			for (Characterization achar : chars) {
				loadCharacterizationAssociations(achar);
				charSet.add(achar);
			}
			return charSet;
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
		ProtocolFile protocolFile = gridClient
				.getProtocolFileByCharacterizationId(charId);
		loadProtocolForProtocolFile(protocolFile);
		achar.setProtocolFile(protocolFile);
		loadDerivedBioAssayDataForCharacterization(achar);
		InstrumentConfiguration instrumentConfig = gridClient
				.getInstrumentConfigurationByCharacterizationId(charId);
		Instrument instrument = gridClient
				.getInstrumentByInstrumentConfigurationId(instrumentConfig
						.getId().toString());
		instrumentConfig.setInstrument(instrument);
		achar.setInstrumentConfiguration(instrumentConfig);
	}

	private void loadProtocolForProtocolFile(ProtocolFile protocolFile)
			throws Exception {
		// TODO implement cql
	}

	private void loadDerivedBioAssayDataForCharacterization(
			Characterization achar) throws Exception {
		achar
				.setDerivedBioAssayDataCollection(new HashSet<DerivedBioAssayData>());
		DerivedBioAssayData[] bioassayArray = gridClient
				.getDerivedBioAssayDataByCharacterizationId(achar.getId()
						.toString());
		if (bioassayArray != null) {
			for (DerivedBioAssayData bioassay : bioassayArray) {
				achar.getDerivedBioAssayDataCollection().add(bioassay);
			}
		}
	}

	/**
	 * return all characterization with an associated NanoparticleSample whose
	 * id is equal to particleId
	 * 
	 * @param particleId
	 * @return
	 * @throws ParticleException
	 */
	public Collection<Characterization> findCharsByParticleSampleId(
			String particleId) throws ParticleCharacterizationException {
		try {
			CQLQuery query = new CQLQuery();
			gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
			target
					.setName("gov.nih.nci.cananolab.domain.particle.characterization.Characterization");
			Association association = new Association();
			association
					.setName("gov.nih.nci.cananolab.domain.particle.NanoparticleSample");
			association.setRoleName("nanoparticleSample");

			Attribute attribute = new Attribute();
			attribute.setName("id");
			attribute.setPredicate(Predicate.EQUAL_TO);
			attribute.setValue(particleId);

			association.setAttribute(attribute);

			target.setAssociation(association);
			query.setTarget(target);
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.particle.characterization.Characterization");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			Characterization chars = null;
			Collection<Characterization> characterizationCollection = new ArrayList<Characterization>();
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				chars = (Characterization) obj;
				// todo (may not needed for this search level)
				// findDerivedBioAssayDataByCharId is added in helper file
				// Collection<DerivedBioAssayData> derivedBioAssayDataCollection
				// =
				// gridClient.findDerivedBioAssayDataByCharId(chars.getId().toString());
				// chars.setDerivedBioAssayDataCollection(derivedBioAssayDataCollection);
				characterizationCollection.add(chars);
			}
			return characterizationCollection;
		} catch (Exception e) {
			String err = "Problem finding the characterizationCollection by particle id: "
					+ particleId;
			logger.error(err, e);
			throw new ParticleCharacterizationException(err, e);
		}
	}
}
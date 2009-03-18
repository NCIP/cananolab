package gov.nih.nci.cananolab.service.particle.impl;

import gov.nih.nci.cagrid.cananolab.client.CaNanoLabServiceClient;
import gov.nih.nci.cagrid.cqlquery.Association;
import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;
import gov.nih.nci.cananolab.domain.characterization.physical.Surface;
import gov.nih.nci.cananolab.domain.common.Protocol;
import gov.nih.nci.cananolab.domain.common.ProtocolFile;
import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.exception.ParticleCharacterizationException;
import gov.nih.nci.cananolab.exception.ParticleCompositionException;
import gov.nih.nci.cananolab.exception.ParticleException;
import gov.nih.nci.cananolab.service.common.impl.FileServiceRemoteImpl;
import gov.nih.nci.cananolab.service.particle.NanoparticleCharacterizationService;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.cananolab.util.Comparators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;

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

	public Characterization findCharacterizationById(String charId,
			String className) throws ParticleCharacterizationException {
		try {
			String fullClassName = ClassUtils.getFullClass(className)
					.getCanonicalName();
			CQLQuery query = new CQLQuery();
			gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
			target.setName(fullClassName);
			Attribute attribute = new Attribute();
			attribute.setName("id");
			attribute.setPredicate(Predicate.EQUAL_TO);
			attribute.setValue(charId);
			target.setAttribute(attribute);
			query.setTarget(target);
			CQLQueryResults results = gridClient.query(query);
			results.setTargetClassname(fullClassName);
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

	protected List<Characterization> findParticleCharacterizationsByClass(
			String particleName, String className)
			throws ParticleCharacterizationException {
		try {
			CQLQuery query = new CQLQuery();
			gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
			String fullClassName = ClassUtils.getFullClass(className)
					.getCanonicalName();
			target.setName(fullClassName);
			Association association = new Association();
			association
					.setName("gov.nih.nci.cananolab.domain.particle.NanoparticleSample");
			association.setRoleName("nanoparticleSample");

			Attribute attribute = new Attribute();
			attribute.setName("name");
			attribute.setPredicate(Predicate.EQUAL_TO);
			attribute.setValue(particleName);

			association.setAttribute(attribute);
			target.setAssociation(association);
			query.setTarget(target);
			CQLQueryResults results = gridClient.query(query);
			results.setTargetClassname(fullClassName);
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			Characterization chara = null;
			List<Characterization> charList = new ArrayList<Characterization>();
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				chara = (Characterization) obj;
				loadCharacterizationAssociations(chara);
				charList.add(chara);
			}
			Collections.sort(charList,
					new Comparators.CharacterizationDateComparator());
			return charList;
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
		if (protocolFile != null) {
			loadProtocolForProtocolFile(protocolFile);
			achar.setProtocolFile(protocolFile);
		}
		// TODO:: temporarily commented until grid service is updated
		// loadDerivedBioAssayDataForCharacterization(achar);
		if (achar instanceof Surface) {
			loadSurfaceChemistriesForSurface((Surface) achar);
		}
		// TODO temporarily commented until grid service is updated
		// InstrumentConfiguration instrumentConfig = gridClient
		// .getInstrumentConfigurationByCharacterizationId(charId);
		// if (instrumentConfig != null) {
		// Instrument instrument = gridClient
		// .getInstrumentByInstrumentConfigurationId(instrumentConfig
		// .getId().toString());
		// if (instrument != null)
		// instrumentConfig.setInstrument(instrument);
		// achar.setInstrumentConfiguration(instrumentConfig);
		// }
	}

	private void loadProtocolForProtocolFile(ProtocolFile protocolFile)
			throws Exception {
		try {
			CQLQuery query = new CQLQuery();
			gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
			target.setName("gov.nih.nci.cananolab.domain.common.Protocol");
			Association association = new Association();
			association
					.setName("gov.nih.nci.cananolab.domain.common.ProtocolFile");
			association.setRoleName("protocolFileCollection");
			Attribute attribute = new Attribute();
			attribute.setName("id");
			attribute.setPredicate(Predicate.EQUAL_TO);
			attribute.setValue(protocolFile.getId().toString());
			association.setAttribute(attribute);

			target.setAssociation(association);
			query.setTarget(target);
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.common.Protocol");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			Protocol protocol = null;

			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				protocol = (Protocol) obj;
			}
			protocolFile.setProtocol(protocol);
		} catch (Exception e) {
			String err = "Problem finding the protocol for protocolFile id: "
					+ protocolFile.getId();
			logger.error(err, e);
			throw new ParticleCompositionException(err, e);
		}
	}

	// private void loadDerivedBioAssayDataForCharacterization(
	// Characterization achar) throws Exception {
	// DerivedBioAssayData[] bioassayArray = gridClient
	// .getDerivedBioAssayDatasByCharacterizationId(achar.getId()
	// .toString());
	// if (bioassayArray != null && bioassayArray.length > 0) {
	// achar
	// .setDerivedBioAssayDataCollection(new HashSet<DerivedBioAssayData>());
	// for (DerivedBioAssayData bioassay : bioassayArray) {
	// /**
	// * TODO temporarily commented File file = gridClient
	// * .getFileByDerivedBioAssayDataId(bioassay.getId()
	// * .toString()); if (file != null) { bioassay.setFile(file); }
	// */
	// DerivedDatum[] datums = gridClient
	// .getDerivedDatumsByDerivedBioAssayDataId(bioassay
	// .getId().toString());
	// if (datums != null && datums.length > 0) {
	// bioassay
	// .setDerivedDatumCollection(new HashSet<DerivedDatum>());
	// for (DerivedDatum datum : datums) {
	// bioassay.getDerivedDatumCollection().add(datum);
	// }
	// }
	// achar.getDerivedBioAssayDataCollection().add(bioassay);
	// }
	// }
	// }

	private void loadSurfaceChemistriesForSurface(Surface surface)
			throws Exception {
		// TODO fix for grid client
		/*
		 * SurfaceChemistry[] chemistries = gridClient
		 * .getSurfaceChemistriesBySurfaceId(surface.getId().toString()); if
		 * (chemistries != null && chemistries.length > 0) { surface
		 * .setSurfaceChemistryCollection(new HashSet<SurfaceChemistry>()); for
		 * (SurfaceChemistry chem : chemistries) {
		 * surface.getSurfaceChemistryCollection().add(chem); } }
		 */
	}

	/**
	 * return all characterization with an associated NanoparticleSample whose
	 * id is equal to particleId
	 *
	 * @param particleId
	 * @return
	 * @throws ParticleException
	 */
	public List<CharacterizationBean> findCharsByParticleSampleId(
			String particleId) throws ParticleCharacterizationException {
		try {
			String[] charNames = gridClient
					.getCharacterizationClassNamesByParticleId(particleId);
			List<CharacterizationBean> characterizationCollection = new ArrayList<CharacterizationBean>();
			if (charNames != null) {
				for (String name : charNames) {
					CQLQuery query = new CQLQuery();
					gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
					String fullClassName = ClassUtils.getFullClass(name)
							.getCanonicalName();
					target.setName(fullClassName);
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
					results.setTargetClassname(fullClassName);
					CQLQueryResultsIterator iter = new CQLQueryResultsIterator(
							results);
					Characterization chara = null;

					while (iter.hasNext()) {
						java.lang.Object obj = iter.next();
						chara = (Characterization) obj;
						// loadCharacterizationAssociations(chara);
						characterizationCollection
								.add(new CharacterizationBean(chara));
					}
				}
			}
			return characterizationCollection;
		} catch (Exception e) {
			String err = "Problem finding the characterizationCollection by particle id: "
					+ particleId;
			logger.error(err, e);
			throw new ParticleCharacterizationException(err, e);
		}
	}

	public void removePublicVisibility(AuthorizationService authService,
			Characterization aChar) throws ParticleCharacterizationException {
		throw new ParticleCharacterizationException(
				"Not implemented for grid service");
	}

	public void assignPublicVisibility(AuthorizationService authService,
			Characterization aChar) throws ParticleCharacterizationException {
		throw new ParticleCharacterizationException(
				"Not implemented for grid service");
	}
}
package gov.nih.nci.cananolab.service.sample.impl;

import gov.nih.nci.cagrid.cananolab.client.CaNanoLabServiceClient;
import gov.nih.nci.cagrid.cqlquery.Association;
import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;
import gov.nih.nci.cananolab.domain.characterization.Characterization;
import gov.nih.nci.cananolab.domain.characterization.OtherCharacterization;
import gov.nih.nci.cananolab.domain.common.Condition;
import gov.nih.nci.cananolab.domain.common.Datum;
import gov.nih.nci.cananolab.domain.common.ExperimentConfig;
import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.common.Instrument;
import gov.nih.nci.cananolab.domain.common.Protocol;
import gov.nih.nci.cananolab.domain.common.Technique;
import gov.nih.nci.cananolab.dto.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.dto.common.DataConditionMatrixBean;
import gov.nih.nci.cananolab.dto.common.ExperimentConfigBean;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.common.SampleBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.CharacterizationException;
import gov.nih.nci.cananolab.exception.ExperimentConfigException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.service.common.impl.FileServiceRemoteImpl;
import gov.nih.nci.cananolab.service.sample.CharacterizationService;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Service methods involving local characterizations
 * 
 * @author tanq, pansu
 * 
 */
public class CharacterizationServiceRemoteImpl implements
		CharacterizationService {
	private static Logger logger = Logger
			.getLogger(CharacterizationServiceRemoteImpl.class);
	private CaNanoLabServiceClient gridClient;
	private String serviceUrl;

	public CharacterizationServiceRemoteImpl(String serviceUrl)
			throws CharacterizationException {
		try {
			this.serviceUrl = serviceUrl;
			gridClient = new CaNanoLabServiceClient(serviceUrl);
		} catch (Exception e) {
			throw new CharacterizationException(
					"Can't create grid client succesfully.");
		}
	}

	public void saveCharacterization(SampleBean sample,
			CharacterizationBean charBean, UserBean user)
			throws CharacterizationException, NoAccessException {
		throw new CharacterizationException("Not implemented for grid service");
	}

	public CharacterizationBean findCharacterizationById(String charId,
			UserBean user) throws CharacterizationException {
		throw new CharacterizationException("Not implemented for grid service");
	}

	public void deleteCharacterization(Characterization chara, UserBean user)
			throws CharacterizationException, NoAccessException {
		throw new CharacterizationException("Not implemented for grid service");
	}

	public List<CharacterizationBean> findCharacterizationsBySampleId(
			String sampleId, UserBean user) throws CharacterizationException {
		List<CharacterizationBean> charBeans = new ArrayList<CharacterizationBean>();
		try {
			// find all characterization class names first
			String[] sampleViewStrs = gridClient.getSampleViewStrs(sampleId);
			// column 8 contains characterization short class names
			String[] charClassNames = null;
			if (sampleViewStrs.length > 9
					&& !StringUtils.isEmpty(sampleViewStrs[9])) {
				charClassNames = sampleViewStrs[9]
						.split(Constants.VIEW_CLASSNAME_DELIMITER);
			}
			if (charClassNames != null) {
				for (String name : charClassNames) {
					CQLQuery query = new CQLQuery();
					gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
					String fullClassName = null;
					if (ClassUtils.getFullClass(name) != null) {
						fullClassName = ClassUtils.getFullClass(name)
								.getCanonicalName();
					} else {
						fullClassName = ClassUtils.getFullClass(
								OtherCharacterization.class.getCanonicalName())
								.getCanonicalName();
					}					
					target.setName(fullClassName);
					Association association = new Association();
					association
							.setName("gov.nih.nci.cananolab.domain.particle.Sample");
					association.setRoleName("sample");

					Attribute attribute = new Attribute();
					attribute.setName("id");
					attribute.setPredicate(Predicate.EQUAL_TO);
					attribute.setValue(sampleId);

					association.setAttribute(attribute);

					target.setAssociation(association);
					query.setTarget(target);
					CQLQueryResults results = gridClient.query(query);
					results.setTargetClassname(fullClassName);
					CQLQueryResultsIterator iter = new CQLQueryResultsIterator(
							results);
					while (iter.hasNext()) {
						java.lang.Object obj = iter.next();
						Characterization achar = (Characterization) obj;
						loadCharacterizationAssociations(achar);
						CharacterizationBean charBean = new CharacterizationBean(
								achar);
						charBeans.add(charBean);
					}
				}
			}
		} catch (Exception e) {
			String err = "Problem finding the remote characterizations by sample id: "
					+ sampleId;
			logger.error(err, e);
			throw new CharacterizationException(err, e);
		}
		return charBeans;
	}

	/**
	 * Get all the associated data of a Characterization
	 * 
	 * @param particleSample
	 * @throws Exception
	 */
	private void loadCharacterizationAssociations(Characterization achar)
			throws Exception {
		Protocol protocol = gridClient.getProtocolByCharacterizationId(achar
				.getId().toString());
		if (protocol != null) {
			File file = gridClient.getFileByProtocolId(protocol.getId()
					.toString());
			if (file != null) {
				protocol.setFile(file);
			}
			achar.setProtocol(protocol);
		}
		loadExperimentConfigsForCharacterization(achar);
//		loadFindingsForCharacterization(achar);
	}

	private void loadExperimentConfigsForCharacterization(Characterization achar)
			throws Exception {
		ExperimentConfig[] configs = gridClient
				.getExperimentConfigsByCharacterizationId(achar.getId()
						.toString());
		if (configs != null) {
			for (ExperimentConfig config : configs) {
				loadTechniqueForConfig(config);
				loadInstrumentsForConfig(config);
			}
		}
	}

	private void loadTechniqueForConfig(ExperimentConfig config)
			throws Exception {
		CQLQuery query = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target.setName("gov.nih.nci.cananolab.domain.common.Technique");
		Association association = new Association();
		association
				.setName("gov.nih.nci.cananolab.domain.common.ExperimentConfig");
		association.setRoleName("experimentConfigCollection");
		Attribute attribute = new Attribute();
		attribute.setName("id");
		attribute.setPredicate(Predicate.EQUAL_TO);
		attribute.setValue(config.getId().toString());
		association.setAttribute(attribute);

		target.setAssociation(association);
		query.setTarget(target);
		CQLQueryResults results = gridClient.query(query);
		results
				.setTargetClassname("gov.nih.nci.cananolab.domain.common.Technique");
		CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
		Technique technique = null;
		while (iter.hasNext()) {
			java.lang.Object obj = iter.next();
			technique = (Technique) obj;
		}
		config.setTechnique(technique);
	}

	private void loadInstrumentsForConfig(ExperimentConfig config)
			throws Exception {
		CQLQuery query = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target.setName("gov.nih.nci.cananolab.domain.common.Instrument");
		Association association = new Association();
		association
				.setName("gov.nih.nci.cananolab.domain.common.ExperimentConfig");
		association.setRoleName("experimentConfigCollection");
		Attribute attribute = new Attribute();
		attribute.setName("id");
		attribute.setPredicate(Predicate.EQUAL_TO);
		attribute.setValue(config.getId().toString());
		association.setAttribute(attribute);

		target.setAssociation(association);
		query.setTarget(target);
		CQLQueryResults results = gridClient.query(query);
		results
				.setTargetClassname("gov.nih.nci.cananolab.domain.common.Instrument");
		CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
		config.setInstrumentCollection(new HashSet<Instrument>());
		while (iter.hasNext()) {
			java.lang.Object obj = iter.next();
			Instrument instrument = (Instrument) obj;
			config.getInstrumentCollection().add(instrument);
		}
	}

//	private void loadFindingsForCharacterization(Characterization achar)
//			throws Exception {
//		Finding[] findings = gridClient.getFindingsByCharacterizationId(achar
//				.getId().toString());
//		if (findings != null && findings.length > 0) {
//			achar.setFindingCollection(new HashSet<Finding>());
//			for (Finding finding : findings) {
//				loadFindingAssociations(finding);
//				achar.getFindingCollection().add(finding);
//			}
//		}
//	}
//
//	private void loadFindingAssociations(Finding finding) throws Exception {
//		loadFilesForFinding(finding);
//		loadDataForFinding(finding);
//	}
//
//	private void loadFilesForFinding(Finding finding) throws Exception {
//		CQLQuery query = new CQLQuery();
//		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
//		target.setName("gov.nih.nci.cananolab.domain.common.File");
//		Association association = new Association();
//		association.setName("gov.nih.nci.cananolab.domain.common.Finding");
//		association.setRoleName("findingCollection");
//		Attribute attribute = new Attribute();
//		attribute.setName("id");
//		attribute.setPredicate(Predicate.EQUAL_TO);
//		attribute.setValue(finding.getId().toString());
//		association.setAttribute(attribute);
//
//		target.setAssociation(association);
//		query.setTarget(target);
//		CQLQueryResults results = gridClient.query(query);
//		results.setTargetClassname("gov.nih.nci.cananolab.domain.common.File");
//		CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
//		finding.setFileCollection(new HashSet<File>());
//		FileServiceRemoteImpl fileService = new FileServiceRemoteImpl(
//				serviceUrl);
//		while (iter.hasNext()) {
//			java.lang.Object obj = iter.next();
//			File file = (File) obj;
//			fileService.loadKeywordsForFile(file);
//			finding.getFileCollection().add(file);
//		}
//	}

	private void loadConditionsForDatum(Datum datum) throws Exception {
		CQLQuery query = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target.setName("gov.nih.nci.cananolab.domain.common.Condition");
		Association association = new Association();
		association.setName("gov.nih.nci.cananolab.domain.common.Datum");
		association.setRoleName("datumCollection");
		Attribute attribute = new Attribute();
		attribute.setName("id");
		attribute.setPredicate(Predicate.EQUAL_TO);
		attribute.setValue(datum.getId().toString());
		association.setAttribute(attribute);

		target.setAssociation(association);
		query.setTarget(target);
		CQLQueryResults results = gridClient.query(query);
		results
				.setTargetClassname("gov.nih.nci.cananolab.domain.common.Condition");
		CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
		datum.setConditionCollection(new HashSet<Condition>());
		while (iter.hasNext()) {
			java.lang.Object obj = iter.next();
			Condition condition = (Condition) obj;
			datum.getConditionCollection().add(condition);
		}
	}

	public void saveExperimentConfig(ExperimentConfigBean configBean,
			UserBean user) throws ExperimentConfigException, NoAccessException {
		throw new ExperimentConfigException("Not implemented for grid service");
	}

	public void deleteExperimentConfig(ExperimentConfig config, UserBean user)
			throws ExperimentConfigException, NoAccessException {
		throw new ExperimentConfigException("Not implemented for grid service");
	}

	public void copyAndSaveCharacterization(CharacterizationBean charBean,
			SampleBean oldSampleBean, SampleBean[] newSampleBeans,
			boolean copyData, UserBean user) throws CharacterizationException,
			NoAccessException {
		throw new CharacterizationException("Not implemented for grid service");
	}

	public void deleteFile(FileBean fileBean, UserBean user)
			throws CharacterizationException, NoAccessException {
		// TODO Auto-generated method stub
		
	}

	public void saveDataConditionMatrix(DataConditionMatrixBean matrix,
			UserBean user) throws CharacterizationException, NoAccessException {
		// TODO Auto-generated method stub
		
	}

	public void saveFile(FileBean fileBean, UserBean user)
			throws CharacterizationException, NoAccessException {
		// TODO Auto-generated method stub
		
	}
}
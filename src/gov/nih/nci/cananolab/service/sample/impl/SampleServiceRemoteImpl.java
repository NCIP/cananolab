package gov.nih.nci.cananolab.service.sample.impl;

import gov.nih.nci.cagrid.cananolab.client.CaNanoLabServiceClient;
import gov.nih.nci.cagrid.cqlquery.Association;
import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.cagrid.cqlquery.QueryModifier;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;
import gov.nih.nci.cananolab.domain.common.Keyword;
import gov.nih.nci.cananolab.domain.common.Organization;
import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.exception.DuplicateEntriesException;
import gov.nih.nci.cananolab.exception.SampleException;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.helper.SampleServiceHelper;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.Comparators;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.SortableName;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.query.hibernate.HQLCriteria;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.hibernate.FetchMode;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;

/**
 * Service methods involving samples
 *
 * @author pansu
 *
 */
public class SampleServiceRemoteImpl implements SampleService {
	private static Logger logger = Logger
			.getLogger(SampleServiceRemoteImpl.class);
	private CaNanoLabServiceClient gridClient;
	private String serviceUrl;
	private SampleServiceHelper helper = new SampleServiceHelper();

	public SampleServiceRemoteImpl(String serviceUrl) throws Exception {
		this.serviceUrl = serviceUrl;
		gridClient = new CaNanoLabServiceClient(serviceUrl);
	}

	/**
	 * Persist a new sample or update an existing canano sample
	 *
	 * @param sample
	 * @throws SampleException,
	 *             DuplicateEntriesException
	 */
	public void saveSample(Sample sample) throws SampleException,
			DuplicateEntriesException {
		throw new SampleException("Not implemented for grid service");
	}

	/**
	 *
	 * @param samplePointOfContacts
	 * @param nanomaterialEntityClassNames
	 * @param otherNanoparticleTypes
	 * @param functionalizingEntityClassNames
	 * @param otherFunctionalizingEntityTypes
	 * @param functionClassNames
	 * @param otherFunctionTypes
	 * @param characterizationClassNames
	 * @param wordList
	 * @return
	 * @throws SampleException
	 */
	public List<SampleBean> findSamplesBy(String samplePointOfContact,
			String[] nanomaterialEntityClassNames,
			String[] otherNanoparticleTypes,
			String[] functionalizingEntityClassNames,
			String[] otherFunctionalizingEntityTypes,
			String[] functionClassNames, String[] otherFunctionTypes,
			String[] characterizationClassNames,
			String[] otherCharacterizationTypes, String[] wordList)
			throws SampleException {
		try {
			String[] sampleViewStrs = gridClient.getSampleViewStrs(
					samplePointOfContact, nanomaterialEntityClassNames,
					functionalizingEntityClassNames, functionClassNames,
					characterizationClassNames, wordList);
			// String[] sampleViewStrs = {
			// "35444457~~~NCICB-6~~~DNT~~~Carbon nanotube!!!small
			// molecule~~~therapeutic~~~Enzyme Induction:Molecular
			// Weight:Oxidative Stress",
			// "35445457~~~NCICB-60~~~DNT~~~Carbon nanotube!!!small
			// molecule~~~therapeutic~~~Enzyme Induction:Molecular
			// Weight:Oxidative Stress",
			// };
			List<SampleBean> samples = new ArrayList<SampleBean>();
			if (sampleViewStrs != null) {
				String[] columns = null;
				for (String sampleStr : sampleViewStrs) {
					columns = sampleStr.split(Constants.VIEW_COL_DELIMITER);
					Sample sample = new Sample();
					// id
					sample.setId(new Long(columns[0]));
					// sample name
					sample.setName(columns[1]);
					// source
					PointOfContact primaryPOC = new PointOfContact();
					Organization org = new Organization();
					primaryPOC.setFirstName(columns[2]);
					primaryPOC.setLastName(columns[3]);
					org.setName(columns[4]);
					primaryPOC.setOrganization(org);

					SampleBean sampleBean = new SampleBean(sample);
					// composition, set all compositions as NanoparticleEntity
					// for now
					if (columns.length > 5 && columns[5] != null
							&& columns[5].length() > 0) {
						String[] compositionsClazzNames = columns[5]
								.split(Constants.VIEW_CLASSNAME_DELIMITER);
						if (compositionsClazzNames != null) {
							sampleBean
									.setNanomaterialEntityClassNames(compositionsClazzNames);
						}
					}
					// functionClassNames
					if (columns.length > 6 && columns[6] != null
							&& columns[6].length() > 0) {
						String[] functionClazzNames = columns[6]
								.split(Constants.VIEW_CLASSNAME_DELIMITER);
						if (functionClazzNames != null) {
							sampleBean
									.setFunctionClassNames(functionClazzNames);
						}
					}

					// characterizationClassNames
					if (columns.length > 7 && columns[7] != null
							&& columns[7].length() > 0) {
						String[] characterizationClazzNames = columns[7]
								.split(Constants.VIEW_CLASSNAME_DELIMITER);
						if (characterizationClazzNames != null) {
							sampleBean
									.setCharacterizationClassNames(characterizationClazzNames);
						}
					}
					samples.add(sampleBean);
				}
				Collections.sort(samples,
						new Comparators.SampleBeanComparator());
			}
			return samples;
		} catch (RemoteException e) {
			logger.error(Constants.NODE_UNAVAILABLE, e);
			throw new SampleException(Constants.NODE_UNAVAILABLE, e);
		} catch (Exception e) {
			String err = "Problem finding samples with the given search parameters.";
			logger.error(err, e);
			throw new SampleException(err, e);
		}
	}

	public SampleBean findSampleById(String sampleId) throws SampleException {
		try {
			CQLQuery query = new CQLQuery();
			gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
			target.setName("gov.nih.nci.cananolab.domain.particle.Sample");
			Attribute attribute = new Attribute();
			attribute.setName("id");
			attribute.setPredicate(Predicate.EQUAL_TO);
			attribute.setValue(sampleId);
			target.setAttribute(attribute);
			query.setTarget(target);
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.particle.Sample");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			Sample sample = null;
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				sample = (Sample) obj;
				loadSamplesAssociations(sample);
			}
			SampleBean sampleBean = new SampleBean(sample);
			return sampleBean;
		} catch (RemoteException e) {
			logger.error(Constants.NODE_UNAVAILABLE, e);
			throw new SampleException(Constants.NODE_UNAVAILABLE, e);
		} catch (Exception e) {
			String err = "Problem finding the remote sample by id: " + sampleId;
			logger.error(err, e);
			throw new SampleException(err, e);
		}
	}

	/**
	 * Get all the associated data of a sample
	 *
	 * @param particleSample
	 * @throws Exception
	 */
	private void loadSamplesAssociations(Sample sample) throws Exception {
		String particleId = sample.getId().toString();
		// source
		loadPointOfContactsForSample(sample);
		// keyword
		loadKeywordsForSample(sample);
	}

	/**
	 * load the source for an associated NanoparticleSample
	 *
	 * @param particleId
	 * @return
	 * @throws ParticleException
	 *
	 */
	private void loadPointOfContactsForSample(Sample sample) throws Exception {
		PointOfContact primaryPOC = gridClient
				.getPrimaryPointOfContactBySampleId(sample.getId().toString());
		PointOfContact[] otherPOCs = gridClient
				.getOtherPointOfContactsBySampleId(sample.getId().toString());
		if (primaryPOC != null) {
			sample.setPrimaryPointOfContact(primaryPOC);
		}
		if (otherPOCs != null && otherPOCs.length > 0) {
			Collection<PointOfContact> otherPOCCollection = new HashSet<PointOfContact>(
					Arrays.asList(otherPOCs));
			sample.setOtherPointOfContactCollection((otherPOCCollection));
		}
	}

	/**
	 * load all keywords for an associated NanoparticleSample equal to
	 * particleId
	 *
	 */
	private void loadKeywordsForSample(Sample sample) throws Exception {
		Keyword[] keywords = gridClient.getKeywordsBySampleId(sample.getId()
				.toString());
		if (keywords != null && keywords.length > 0) {
			sample.setKeywordCollection(new HashSet<Keyword>(Arrays
					.asList(keywords)));
		}
	}

	public int getNumberOfPublicSamples() throws SampleException {
		try {
			CQLQuery query = new CQLQuery();
			gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
			target.setName("gov.nih.nci.cananolab.domain.particle.Sample");
			query.setTarget(target);
			QueryModifier modifier = new QueryModifier();
			modifier.setCountOnly(true);
			query.setQueryModifier(modifier);

			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.particle.Sample");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			int count = 0;
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				count = ((Long) obj).intValue();
			}
			return count;
		} catch (Exception e) {
			String err = "Error finding counts of remote public samples.";
			logger.error(err, e);
			throw new SampleException(err, e);
		}
	}

	public SampleBean findFullSampleById(String sampleId) throws Exception {
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();

		DetachedCriteria crit = DetachedCriteria.forClass(Sample.class).add(
				Property.forName("id").eq(new Long(sampleId)));
		// characterization
		crit.setFetchMode("characterizationCollection", FetchMode.JOIN);
		crit.setFetchMode(
				"characterizationCollection.derivedBioAssayDataCollection",
				FetchMode.JOIN);
		crit
				.setFetchMode(
						"characterizationCollection.derivedBioAssayDataCollection.derivedDatumCollection",
						FetchMode.JOIN);
		crit.setFetchMode(
				"characterizationCollection.experimentConfigCollection",
				FetchMode.JOIN);
		// sampleComposition
		crit.setFetchMode("sampleComposition", FetchMode.JOIN);
		crit.setFetchMode("sampleComposition.nanomaterialEntityCollection",
				FetchMode.JOIN);
		crit
				.setFetchMode(
						"sampleComposition.nanomaterialEntityCollection.composingElementCollection",
						FetchMode.JOIN);
		crit.setFetchMode("sampleComposition.nanomaterialEntityCollection."
				+ "composingElementCollection.inherentFunctionCollection",
				FetchMode.JOIN);
		crit.setFetchMode("sampleComposition.fileCollection", FetchMode.JOIN);
		crit.setFetchMode("sampleComposition.chemicalAssociationCollection",
				FetchMode.JOIN);
		crit
				.setFetchMode(
						"sampleComposition.chemicalAssociationCollection.associatedElementA",
						FetchMode.JOIN);
		crit
				.setFetchMode(
						"sampleComposition.chemicalAssociationCollection.associatedElementB",
						FetchMode.JOIN);
		crit.setFetchMode("sampleComposition.functionalizingEntityCollection",
				FetchMode.JOIN);
		crit
				.setFetchMode(
						"sampleComposition.functionalizingEntityCollection.functionCollection",
						FetchMode.JOIN);
		crit.setFetchMode("keywordCollection", FetchMode.JOIN);
		crit.setFetchMode("publicationCollection", FetchMode.JOIN);
		crit.setFetchMode("primaryPointOfContact", FetchMode.JOIN);
		crit.createAlias("otherPointOfContactCollection", "otherPoc",
				CriteriaSpecification.LEFT_JOIN);
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

		List result = appService.query(crit);
		Sample sample = null;
		SampleBean sampleBean = null;
		if (!result.isEmpty()) {
			sample = (Sample) result.get(0);
			sampleBean = new SampleBean(sample);
		}
		return sampleBean;
	}

	public Sample findSampleByName(String sampleName) throws SampleException {
		try {
			return helper.findSampleByName(sampleName);
		} catch (Exception e) {
			String err = "Problem finding the particle by name: " + sampleName;
			logger.error(err, e);
			throw new SampleException(err, e);
		}
	}

	public void retrieveVisibility(SampleBean sampleBean, UserBean user)
			throws SampleException {
		throw new SampleException("Not implemented for grid service");
	}

	public void deleteAnnotationById(String className, Long dataId)
			throws SampleException {
		throw new SampleException("Not implemented for grid service");
	}

	public SortedSet<String> findAllSampleNames(UserBean user)
			throws SampleException {
		try {
			AuthorizationService auth = new AuthorizationService(
					Constants.CSM_APP_NAME);

			SortedSet<String> names = new TreeSet<String>(
					new Comparators.SortableNameComparator());
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			HQLCriteria crit = new HQLCriteria(
					"select sample.name from gov.nih.nci.cananolab.domain.particle.Sample sample");
			List results = appService.query(crit);
			for (Object obj : results) {
				String name = ((String) obj).trim();
				if (auth.isUserAllowed(name, user)) {
					names.add(name);
				}
			}
			return names;
		} catch (Exception e) {
			String err = "Error finding samples for " + user.getLoginName();
			logger.error(err, e);
			throw new SampleException(err, e);
		}
	}

	public void assignVisibility(SampleBean sampleBean) throws Exception {
		throw new SampleException("Not implemented for grid service");
	}

	public List<SampleBean> getUserAccessibleSamples(
			List<SampleBean> particles, UserBean user) throws SampleException {
		throw new SampleException("Not implemented for grid service");
	}

	public SortedSet<String> findAllSampleNames() throws SampleException {
		throw new SampleException("Not implemented for grid service");
	}

	public SortedSet<SortableName> findOtherSamplesFromSamePointOfContact(
			String sampleId) throws SampleException {
		throw new SampleException("Not implemented for grid service");
	}
}

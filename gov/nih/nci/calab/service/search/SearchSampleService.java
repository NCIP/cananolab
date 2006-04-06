package gov.nih.nci.calab.service.search;

import gov.nih.nci.calab.db.DataAccessProxy;
import gov.nih.nci.calab.db.IDataAccess;
import gov.nih.nci.calab.domain.Sample;
import gov.nih.nci.calab.domain.Source;
import gov.nih.nci.calab.dto.administration.AliquotBean;
import gov.nih.nci.calab.dto.administration.ContainerBean;
import gov.nih.nci.calab.dto.administration.SampleBean;
import gov.nih.nci.calab.dto.administration.StorageLocation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * 
 * @author pansu
 * 
 */
/* CVS $Id: SearchSampleService.java,v 1.4 2006-04-06 20:26:07 pansu Exp $ */

public class SearchSampleService {
	private static Logger logger = Logger.getLogger(SearchSampleService.class);

	/**
	 * 
	 * @return all sample sources
	 */
	public List<String> getAllSampleSources() {
		List<String> sampleSources = new ArrayList<String>();

		try {
			IDataAccess ida = (new DataAccessProxy())
					.getInstance(IDataAccess.TOOLKITAPI);
			ida.open();
			String hqlString = "select source.organizationName from Source source";
			List results = ida.query(hqlString, Source.class.getName());
			for (Object obj : results) {
				sampleSources.add((String) obj);
			}
			ida.close();
		} catch (Exception e) {
			logger.error("Error in retrieving all sample sources", e);
			throw new RuntimeException("Error in retrieving all sample sources");
		}

		return sampleSources;
	}

	/**
	 * 
	 * @return all source sample IDs
	 */
	public List<String> getAllSourceSampleIds() {
		List<String> sourceSampleIds = new ArrayList<String>();

		try {
			IDataAccess ida = (new DataAccessProxy())
					.getInstance(IDataAccess.TOOLKITAPI);
			ida.open();
			String hqlString = "select distinct sample.sourceSampleId from Sample sample";
			List results = ida.query(hqlString, Sample.class.getName());
			for (Object obj : results) {
				sourceSampleIds.add((String) obj);
			}
			ida.close();
		} catch (Exception e) {
			logger.error("Error in retrieving all source sample IDs", e);
			throw new RuntimeException("Error in retrieving all source sample IDs");
		}

		return sourceSampleIds;
	}

	/**
	 * 
	 * @return all sample submitters
	 */
	public List<String> getAllSampleSubmitters() {
		List<String> sampleSubmitters = new ArrayList<String>();
		try {
			IDataAccess ida = (new DataAccessProxy())
					.getInstance(IDataAccess.TOOLKITAPI);
			ida.open();
			String hqlString = "select distinct sample.createdBy from Sample sample";
			List results = ida.query(hqlString, Sample.class.getName());
			for (Object obj : results) {
				sampleSubmitters.add((String) obj);
			}
			ida.close();
		} catch (Exception e) {
			logger.error("Error in retrieving all sample submitters", e);
			throw new RuntimeException("Error in retrieving all sample submitters");
		}

		return sampleSubmitters;
	}

	/**
	 * Search database for sample container information based on sample ID
	 * 
	 * @param sampleId
	 * @param sampleType
	 * @param sampleSource
	 * @param sourceSampleId
	 * @param dateAccessionedBegin
	 * @param dateAccessionedEnd
	 * @param sampleSubmitter
	 * @param storageLocation
	 * @return a list of SampleBean
	 */
	public List<SampleBean> searchSamplesBySampleId(String sampleId,
			String sampleType, String sampleSource, String sourceSampleId,
			Date dateAccessionedBegin, Date dateAccessionedEnd,
			String sampleSubmitter, StorageLocation storageLocation) {
		List<SampleBean> samples = new ArrayList<SampleBean>();

		SampleBean sample1 = new SampleBean(sampleId, sampleType, "SOP",
				"description", sampleSource, sourceSampleId, "10/21/2005",
				"solubility", "1", "lot description", "1", "comments",
				"Jane Doe", "10/21/2005", new ContainerBean[] {
						new ContainerBean("Tube", "", "18", "mg", "1.8",
								"mg/ml", "10", "ml", "solvent",
								"safety precautions", "storageCondition",
								new StorageLocation(null, "205", "1", "1",
										null, "A"), "comments"),
						new ContainerBean("Vial", "", "18", "mg", "1.8",
								"mg/ml", "10", "ml", "solvent",
								"safety precautions", "storageCondition",
								new StorageLocation(null, "205", "1", "1",
										null, "A"), "comments") });
		samples.add(sample1);

		return samples;
	}

	/**
	 * Search database for sample container information based on aliquot ID
	 * 
	 * @param aliquotId
	 * @param sampleType
	 * @param sampleSource
	 * @param sourceSampleId
	 * @param dateAccessionedBegin
	 * @param dateAccessionedEnd
	 * @param sampleSubmitter
	 * @param storageLocation
	 * @return
	 */
	public List<SampleBean> searchSamplesByAliquotId(String aliquotId,
			String sampleType, String sampleSource, String sourceSampleId,
			Date dateAccessionedBegin, Date dateAccessionedEnd,
			String sampleSubmitter, StorageLocation storageLocation) {
		List<SampleBean> samples = new ArrayList<SampleBean>();
		String sampleId = aliquotId;
		SampleBean sample1 = new SampleBean(sampleId, sampleType, "SOP",
				"description", sampleSource, sourceSampleId, "10/21/2005",
				"solubility", "1", "lot description", "1", "comments",
				"Jane Doe", "10/21/2005", new ContainerBean[] {
						new ContainerBean("Tube", "", "18", "mg", "1.8",
								"mg/ml", "10", "ml", "solvent",
								"safety precautions", "storageCondition",
								new StorageLocation(null, "205", "1", "1",
										null, "A"), "comments"),
						new ContainerBean("Vial", "", "18", "mg", "1.8",
								"mg/ml", "10", "ml", "solvent",
								"safety precautions", "storageCondition",
								new StorageLocation(null, "205", "1", "1",
										null, "A"), "comments") });

		samples.add(sample1);

		return samples;
	}

	/**
	 * 
	 * @param aliquotId
	 * @param sampleType
	 * @param sampleSource
	 * @param sourceSampleId
	 * @param dateAccessionedBegin
	 * @param dateAccessionedEnd
	 * @param sampleSubmitter
	 * @param storageLocation
	 * @return
	 */
	public List<AliquotBean> searchAliquotsByAliquotId(String aliquotId,
			String sampleType, String sampleSource, String sourceSampleId,
			Date dateAccessionedBegin, Date dateAccessionedEnd,
			String sampleSubmitter, StorageLocation storageLocation) {
		List<AliquotBean> aliquots = new ArrayList<AliquotBean>();

		AliquotBean aliquot = new AliquotBean(aliquotId, new ContainerBean(
				"Tube", "", "18", "mg", "1.8", "mg/ml", "10", "ml", "solvent",
				"safety precautions", "storageCondition", new StorageLocation(
						null, "205", "1", "1", null, "A"), "comments"),
				"solubilized", "Jane Doe", "10/21/2005", new SampleBean(
						"NCL-6", sampleType, "SOP", "description",
						sampleSource, sourceSampleId, "10/21/2005",
						"solubility", "1", "lot description", "1", "comments",
						"Jane Doe", "10/21/2005"));

		aliquots.add(aliquot);

		return aliquots;
	}

	/**
	 * Search database for sample container information based on other
	 * parameters
	 * 
	 * @param sampleType
	 * @param sampleSource
	 * @param sourceSampleId
	 * @param dateAccessionedBegin
	 * @param dateAccessionedEnd
	 * @param sampleSubmitter
	 * @param storageLocation
	 * @return
	 */
	public List<SampleBean> searchSamples(String sampleType,
			String sampleSource, String sourceSampleId,
			Date dateAccessionedBegin, Date dateAccessionedEnd,
			String sampleSubmitter, StorageLocation storageLocation) {
		List<SampleBean> samples = new ArrayList<SampleBean>();
		SampleBean sample1 = new SampleBean("NCL-6", sampleType, "SOP",
				"description", sampleSource, sourceSampleId, "10/21/2005",
				"solubility", "1", "lot description", "1", "comments",
				"Jane Doe", "10/21/2005", new ContainerBean[] {
						new ContainerBean("Tube", "", "18", "mg", "1.8",
								"mg/ml", "10", "ml", "solvent",
								"safety precautions", "storageCondition",
								new StorageLocation(null, "205", "1", "1",
										null, "A"), "comments"),
						new ContainerBean("Vial", "", "18", "mg", "1.8",
								"mg/ml", "10", "ml", "solvent",
								"safety precautions", "storageCondition",
								new StorageLocation(null, "205", "1", "1",
										null, "A"), "comments") });

		SampleBean sample2 = new SampleBean("NCL-7", sampleType, "SOP",
				"description", sampleSource, sourceSampleId, "10/21/2005",
				"solubility", "1", "lot description", "1", "comments",
				"Jane Doe", "10/21/2005",
				new ContainerBean[] { new ContainerBean("Tube", "", "18", "mg",
						"1.8", "mg/ml", "10", "ml", "solvent",
						"safety precautions", "storageCondition",
						new StorageLocation(null, "205", "1", "1", null, "A"),
						"comments") });

		samples.add(sample1);
		samples.add(sample2);

		return samples;
	}
}

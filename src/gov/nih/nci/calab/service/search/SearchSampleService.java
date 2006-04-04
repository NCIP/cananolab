package gov.nih.nci.calab.service.search;

import gov.nih.nci.calab.dto.administration.AliquotBean;
import gov.nih.nci.calab.dto.administration.ContainerBean;
import gov.nih.nci.calab.dto.administration.SampleBean;
import gov.nih.nci.calab.dto.administration.StorageLocation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author pansu
 * 
 */
/* CVS $Id: SearchSampleService.java,v 1.3 2006-04-04 15:33:11 pansu Exp $ */

public class SearchSampleService {
	/**
	 * 
	 * @return all sample sources
	 */
	public List<String> getAllSampleSources() {
		List<String> sources = new ArrayList<String>();
		sources.add("Vendor X");
		sources.add("Institute Y");
		return sources;
	}

	/**
	 * 
	 * @return all source sample IDs
	 */
	public List<String> getAllSourceSampleIds() {
		List<String> sourceSampleIds = new ArrayList<String>();
		sourceSampleIds.add("X123");
		sourceSampleIds.add("Y-345");
		return sourceSampleIds;
	}

	/**
	 * 
	 * @return all sample submitters
	 */
	public List<String> getAllSampleSubmitters() {
		List<String> sampleSubmitters = new ArrayList<String>();
		sampleSubmitters.add("Jane Doe");
		sampleSubmitters.add("John Doe");
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

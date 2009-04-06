package gov.nih.nci.cananolab.service.sample.helper;

import gov.nih.nci.cananolab.domain.characterization.OtherCharacterization;
import gov.nih.nci.cananolab.domain.common.Datum;
import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.common.Protocol;
import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.service.common.LookupService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.query.hibernate.HQLCriteria;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import org.apache.log4j.Logger;
import org.hibernate.FetchMode;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

/**
 * Service methods involving characterizations
 *
 * @author tanq, pansu
 *
 */

public class CharacterizationServiceHelper {
	private static Logger logger = Logger
			.getLogger(CharacterizationServiceHelper.class);

	public CharacterizationServiceHelper() {
	}

	public Characterization findCharacterizationById(String charId)
			throws Exception {
		Characterization achar = null;
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		DetachedCriteria crit = DetachedCriteria.forClass(
				Characterization.class).add(
				Property.forName("id").eq(new Long(charId)));
		// fully load characterization
		crit.setFetchMode("pointOfContact", FetchMode.JOIN);
		crit.setFetchMode("pointOfContact.organization", FetchMode.JOIN);
		crit.setFetchMode("protocolFile", FetchMode.JOIN);
		crit.setFetchMode("protocolFile.protocol", FetchMode.JOIN);
		crit.setFetchMode("experimentConfigCollection", FetchMode.JOIN);
		crit.setFetchMode("experimentConfigCollection.technique",
				FetchMode.JOIN);
		crit.setFetchMode("experimentConfigCollection.instrumentCollection",
				FetchMode.JOIN);
		crit.setFetchMode("datumCollection", FetchMode.JOIN);
		crit
				.setFetchMode("datumCollection.conditionCollection",
						FetchMode.JOIN);
		crit.setFetchMode("datumCollection.dataSet", FetchMode.JOIN);
		crit.setFetchMode("datumCollection.dataSet.file", FetchMode.JOIN);
		crit.setFetchMode("datumCollection.dataSet.file.keywordCollection",
				FetchMode.JOIN);
		crit.setFetchMode("datumCollection.dataRow", FetchMode.JOIN);
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

		List result = appService.query(crit);
		if (!result.isEmpty()) {
			achar = (Characterization) result.get(0);
		}
		return achar;
	}

	// use in dwr ajax
	public String[] getDerivedDatumValueUnits(String derivedDatumName) {
		try {
			SortedSet<String> units = LookupService.findLookupValues(
					derivedDatumName, "unit");
			SortedSet<String> otherUnits = LookupService.findLookupValues(
					derivedDatumName, "otherUnit");
			units.addAll(otherUnits);
			return units.toArray(new String[0]);
		} catch (Exception e) {
			String err = "Error getting value unit for " + derivedDatumName;
			logger.error(err, e);
			return null;
		}
	}

	public List<Characterization> findSampleCharacterizationsByClass(
			String sampleName, String className) throws Exception {
		List<Characterization> charas = new ArrayList<Characterization>();

		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		DetachedCriteria crit = DetachedCriteria.forClass(Class
				.forName(className));
		crit.add(Restrictions.eq("sample.name", sampleName));
		crit.setFetchMode("pointOfContact", FetchMode.JOIN);
		crit.setFetchMode("pointOfContact.organization", FetchMode.JOIN);
		crit.setFetchMode("protocolFile", FetchMode.JOIN);
		crit.setFetchMode("experimentConfigCollection", FetchMode.JOIN);
		crit.setFetchMode("experimentConfigCollection.technique",
				FetchMode.JOIN);
		crit.setFetchMode("datumCollection", FetchMode.JOIN);
		crit.setFetchMode("fileCollection", FetchMode.JOIN);
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

		List result = appService.query(crit);
		for (Object obj : result) {
			charas.add((Characterization) obj);
		}
		return charas;
	}

	public List<String> findOtherCharacterizationByAssayCategory(
			String assayCategory) throws Exception {
		List<String> charNames = new ArrayList<String>();
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		DetachedCriteria crit = DetachedCriteria.forClass(
				OtherCharacterization.class).add(
				Property.forName("assayCategory").eq(assayCategory));
		List result = appService.query(crit);
		for (Object obj : result) {
			charNames.add(((OtherCharacterization) obj).getName());
		}
		return charNames;
	}

	public Protocol findProtocolByCharacterizationId(
			java.lang.String characterizationId) throws Exception {
		Protocol protocol = null;

		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		String hql = "select aChar.protocol from gov.nih.nci.cananolab.domain.particle.characterization.Characterization aChar where aChar.id="
				+ characterizationId;
		HQLCriteria crit = new HQLCriteria(hql);
		List results = appService.query(crit);
		for (Object obj : results) {
			protocol = (Protocol) obj;
		}
		return protocol;
	}

	public List<Datum> findDataByCharacterizationId(String charId)
			throws Exception {
		List<Datum> datumCollection = new ArrayList<Datum>();

		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		HQLCriteria crit = new HQLCriteria(
				"select achar.datumCollection from gov.nih.nci.cananolab.domain.particle.Characterization achar where achar.id = "
						+ charId);
		List results = appService.query(crit);
		for (Object obj : results) {
			Datum datum = (Datum) obj;
			datumCollection.add(datum);
		}
		return datumCollection;
	}

	public List<File> findFilesByCharacterizationId(String charId)
			throws Exception {
		List<File> fileCollection = new ArrayList<File>();

		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		HQLCriteria crit = new HQLCriteria(
				"select achar.fileCollection from gov.nih.nci.cananolab.domain.particle.Characterization achar where achar.id = "
						+ charId);
		List results = appService.query(crit);
		for (Object obj : results) {
			File file = (File) obj;
			fileCollection.add(file);
		}
		return fileCollection;
	}

	// public List<DerivedDatum> findDerivedDatumListByDerivedBioAssayDataId(
	// String derivedBioAssayDataId) throws Exception {
	// List<DerivedDatum> datumList = new ArrayList<DerivedDatum>();
	//
	// CustomizedApplicationService appService = (CustomizedApplicationService)
	// ApplicationServiceProvider
	// .getApplicationService();
	// HQLCriteria crit = new HQLCriteria(
	// "select bioassay.derivedDatumCollection from
	// gov.nih.nci.cananolab.domain.common.DerivedBioAssayData bioassay where
	// bioassay.id = "
	// + derivedBioAssayDataId);
	// List results = appService.query(crit);
	// for (Object obj : results) {
	// datumList.add((DerivedDatum) obj);
	// }
	// return datumList;
	// }
}
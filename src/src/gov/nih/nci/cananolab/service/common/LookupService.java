package gov.nih.nci.cananolab.service.common;

import gov.nih.nci.cananolab.domain.common.CommonLookup;
import gov.nih.nci.cananolab.exception.CaNanoLabException;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;

/**
 * The service returns prepopulated lookup data that are shared across different
 * views.
 * 
 * @author pansu
 * 
 */
/* CVS $Id: LookupService.java,v 1.9 2008-09-16 15:54:50 tanq Exp $ */

public class LookupService {
	private static Logger logger = Logger.getLogger(LookupService.class);

	/**
	 * get all lookup name, attribute, values and stored it in a map of map of
	 * ordered list of strings.
	 * 
	 * @return
	 * @throws CaNanoLabException
	 */
	public static Map<String, Map<String, SortedSet<String>>> findAllLookups()
			throws CaNanoLabException {
		Map<String, Map<String, SortedSet<String>>> lookupMap = new HashMap<String, Map<String, SortedSet<String>>>();
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria
					.forClass(CommonLookup.class);
			List results = appService.query(crit);
			for (Object obj : results) {
				CommonLookup lookup = (CommonLookup) obj;
				String name = lookup.getName();
				String attribute = lookup.getAttribute();
				String value = lookup.getValue();
				Map<String, SortedSet<String>> nameLookup = null;
				if (lookupMap.get(name) != null) {
					nameLookup = lookupMap.get(name);
				} else {
					nameLookup = new HashMap<String, SortedSet<String>>();
					lookupMap.put(lookup.getName(), nameLookup);
				}
				SortedSet<String> values = null;
				if (nameLookup.get(attribute) != null) {
					values = nameLookup.get(attribute);
				} else {
					values = new TreeSet<String>();
					nameLookup.put(attribute, values);
				}
				values.add(value);
			}
			return lookupMap;
		} catch (Exception e) {
			String err = "Error in retrieving all common lookup values .";
			logger.error(err, e);
			throw new CaNanoLabException(err, e);
		}
	}

	public static SortedSet<String> findLookupValues(String name,
			String attribute) throws CaNanoLabException {
		SortedSet<String> lookupValues = new TreeSet<String>(
				new Comparator<String>() {
		    public int compare(String s1, String s2) {
		        return s1.toLowerCase () .compareTo ( s2.toLowerCase());
		    }}
		);
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria
					.forClass(CommonLookup.class);
			crit.add(Property.forName("name").eq(name));
			crit.add(Property.forName("attribute").eq(attribute));
			Collection results = appService.query(crit);
			for (Object obj : results) {
				lookupValues.add(((CommonLookup) obj).getValue());
			}
		} catch (Exception e) {
			logger.error("Error in retrieving common lookup values for name "
					+ name + " and attribute " + attribute, e);
			throw new CaNanoLabException();
		}
		return lookupValues;
	}

	public static Map<String, String> findSingleAttributeLookupMap(
			String attribute) throws CaNanoLabException {
		Map<String, String> lookup = new HashMap<String, String>();
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria
					.forClass(CommonLookup.class);
			crit.add(Property.forName("attribute").eq(attribute));
			Collection results = appService.query(crit);
			for (Object obj : results) {
				lookup.put(((CommonLookup) obj).getName(), ((CommonLookup) obj)
						.getValue());
			}
		} catch (Exception e) {
			logger.error("Error in retrieving " + attribute
					+ " from CommonLookup", e);
			throw new CaNanoLabException();
		}
		return lookup;
	}

	/**
	 * Retrieve lookup values from database based type and otherType
	 * 
	 * @param lookupName
	 * @param lookupAttribute
	 * @param otherTypeAttribute
	 * @return
	 * @throws CaNanoLabException
	 */
	public static SortedSet<String> getDefaultAndOtherLookupTypes(
			String lookupName, String lookupAttribute, String otherTypeAttribute)
			throws CaNanoLabException {
		SortedSet<String> types = LookupService.findLookupValues(lookupName,
				lookupAttribute);
		SortedSet<String> otherTypes = LookupService.findLookupValues(
				lookupName, otherTypeAttribute);
		types.addAll(otherTypes);
		return types;
	}

	public static void saveOtherType(String lookupName, String otherAttribute,
			String otherAttributeValue) throws CaNanoLabException {
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria
					.forClass(CommonLookup.class);
			crit.add(Property.forName("name").eq(lookupName)).add(
					Property.forName("attribute").eq(otherAttribute)).add(
					Property.forName("value").eq(otherAttributeValue));
			Integer number = appService.getQueryRowCount(crit,
					CommonLookup.class.getCanonicalName());
			if (number == 0) {
				CommonLookup lookup = new CommonLookup();
				lookup.setName(lookupName);
				lookup.setAttribute(otherAttribute);
				lookup.setValue(otherAttributeValue);
				appService.saveOrUpdate(lookup);
			}

		} catch (Exception e) {
			String err = "Error in saving other attribute types for "
					+ lookupName;
			logger.error(err, e);
			throw new CaNanoLabException(err, e);
		}
	}
}
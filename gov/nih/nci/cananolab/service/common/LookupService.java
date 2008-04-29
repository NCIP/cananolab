package gov.nih.nci.cananolab.service.common;

import gov.nih.nci.cananolab.domain.common.CommonLookup;
import gov.nih.nci.cananolab.exception.CaNanoLabException;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.util.Collection;
import java.util.HashMap;
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
/* CVS $Id: LookupService.java,v 1.6 2008-04-29 23:13:23 pansu Exp $ */

public class LookupService {
	private static Logger logger = Logger.getLogger(LookupService.class);

	public static SortedSet<String> findLookupValues(String name,
			String attribute) throws CaNanoLabException {
		SortedSet<String> lookupValues = new TreeSet<String>();
		try {
			ApplicationService appService = ApplicationServiceProvider
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
			ApplicationService appService = ApplicationServiceProvider
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
}
package gov.nih.nci.calab.service.common;

import gov.nih.nci.calab.db.HibernateUtil;
import gov.nih.nci.calab.domain.LookupType;
import gov.nih.nci.calab.domain.MeasureUnit;
import gov.nih.nci.calab.exception.CaNanoLabException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.hibernate.Session;

/**
 * The service to return prepopulated data that are shared across different
 * views.
 * 
 * @author zengje
 * 
 */
/* CVS $Id: LookupService.java,v 1.139 2007-12-06 09:01:44 pansu Exp $ */

public class LookupService {
	private static Logger logger = Logger.getLogger(LookupService.class);

	public static Map<String, SortedSet<String>> getAllMeasureUnits()
			throws CaNanoLabException {
		Map<String, SortedSet<String>> unitMap = new HashMap<String, SortedSet<String>>();

		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			String hqlString = "from MeasureUnit unit order by unit.name";
			List results = session.createQuery(hqlString).list();
			HibernateUtil.commitTransaction();
			SortedSet<String> units = null;
			for (Object obj : results) {
				MeasureUnit unit = (MeasureUnit) obj;
				String type = unit.getType();
				if (unitMap.get(type) != null) {
					units = unitMap.get(type);
				} else {
					units = new TreeSet<String>();
					unitMap.put(type, units);
				}
				units.add(unit.getName());
			}

		} catch (Exception e) {
			logger.error("Error in retrieving all measure units", e);
			throw new CaNanoLabException("Error in retrieving all measure units.");
		} finally {
			HibernateUtil.closeSession();
		}
		return unitMap;
	}

	public static SortedSet<String> getAllLookupTypes(String lookupType)
			throws CaNanoLabException {
		SortedSet<String> types = new TreeSet<String>();

		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			String hqlString = "select distinct name from " + lookupType;
			List results = session.createQuery(hqlString).list();
			HibernateUtil.commitTransaction();
			for (Object obj : results) {
				types.add((String) obj);
			}

		} catch (Exception e) {
			logger
					.error("Problem to retrieve all " + lookupType + " types.",
							e);
			throw new CaNanoLabException("Problem to retrieve all " + lookupType
					+ " types.");
		} finally {
			HibernateUtil.closeSession();
		}
		return types;
	}

	public static void addLookupType(Session session, LookupType lookupType,
			String type) {
		String className = lookupType.getClass().getSimpleName();
		if (type != null && type.length() > 0) {
			List results = session.createQuery(
					"select count(distinct name) from " + className
							+ " type where name='" + type + "'").list();
			lookupType.setName(type);
			int count = -1;
			for (Object obj : results) {
				count = ((Integer) (obj)).intValue();
			}
			if (count == 0) {
				session.save(lookupType);
			}
		}
	}

}
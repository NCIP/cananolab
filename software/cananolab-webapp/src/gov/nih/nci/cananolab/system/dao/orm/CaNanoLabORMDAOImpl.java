package gov.nih.nci.cananolab.system.dao.orm;

import gov.nih.nci.cananolab.system.dao.CaNanoLabORMDAO;
import gov.nih.nci.system.dao.DAOException;
import gov.nih.nci.system.dao.orm.WritableORMDAOImpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.JDBCException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.hibernate.type.NullableType;

/**
 * Modified the original ORMDAOImpl to contain generic CRUD operations. Removed
 * CSM portion.
 *
 * modified by Sue Pan
 *
 * @author Satish Patel, Dan Dumitru
 */
public class CaNanoLabORMDAOImpl extends WritableORMDAOImpl implements
		CaNanoLabORMDAO {
	private static Logger log = Logger.getLogger(CaNanoLabORMDAOImpl.class
			.getName());


	public Object load(Class domainClass, Serializable id) {
		return getFlushAutoHibernateTemplate().load(domainClass, id);
	}

	public Object get(Class domainClass, Serializable id) {
		return getFlushAutoHibernateTemplate().get(domainClass, id);
	}

	public void saveOrUpdate(Object t) {
		getFlushAutoHibernateTemplate().saveOrUpdate(t);
	}

	public void deleteById(Class domainClass, Serializable id) {
		Object obj = getFlushAutoHibernateTemplate().load(domainClass, id);
		delete(obj);
	}

	public List getAll(Class domainClass) {
		return getFlushAutoHibernateTemplate().loadAll(domainClass);
	}

	public Object getObject(Class domainClass, String uniqueKeyName,
			Object uniqueKeyValue) {

		DetachedCriteria crit = DetachedCriteria.forClass(domainClass).add(
				Property.forName(uniqueKeyName).eq(uniqueKeyValue));
		List results = getFlushAutoHibernateTemplate().findByCriteria(crit);

		if (results != null && !results.isEmpty()) {
			return results.get(0);
		} else {
			return null;
		}
	}

	public List directSQL(String directSQL, String[] columns,
			Object[] columnTypes) throws DAOException {
		Session session = getSession();
		try {
			SQLQuery query = session.createSQLQuery(directSQL);
			for (int i = 0; i < columns.length; i++) {
				query.addScalar(columns[i], (NullableType) columnTypes[i]);
			}
			List results = query.list();
			return results;
		} catch (JDBCException ex) {
			log.error("JDBC Exception in CustomORMDAOImpl ", ex);
			throw new DAOException("JDBC Exception in CustomORMDAOImpl ", ex);
		} catch (org.hibernate.HibernateException hbmEx) {
			log.error(hbmEx.getMessage());
			throw new DAOException("Hibernate problem ", hbmEx);
		} catch (Exception e) {
			log.error("Exception ", e);
			throw new DAOException("Exception in CustomORMDAOImpl ", e);
		}
	}
}

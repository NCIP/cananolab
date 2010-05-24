package gov.nih.nci.cananolab.system.dao.orm;

import gov.nih.nci.cananolab.system.dao.CustomizedORMDAO;
import gov.nih.nci.system.dao.DAOException;
import gov.nih.nci.system.dao.Request;
import gov.nih.nci.system.dao.Response;
import gov.nih.nci.system.dao.orm.translator.CQL2HQL;
import gov.nih.nci.system.dao.orm.translator.NestedCriteria2HQL;
import gov.nih.nci.system.dao.orm.translator.Path2NestedCriteria;
import gov.nih.nci.system.query.cql.CQLQuery;
import gov.nih.nci.system.query.hibernate.HQLCriteria;
import gov.nih.nci.system.query.nestedcriteria.NestedCriteria;
import gov.nih.nci.system.query.nestedcriteria.NestedCriteriaPath;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.JDBCException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.type.NullableType;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Modified the original ORMDAOImpl to contain generic CRUD operations. Removed
 * CSM portion.
 *
 * modified by Sue Pan
 *
 * @author Satish Patel, Dan Dumitru
 */
public class CustomizedORMDAOImpl extends HibernateDaoSupport implements
		CustomizedORMDAO {
	private static Logger log = Logger.getLogger(CustomizedORMDAOImpl.class
			.getName());

	private boolean caseSensitive;

	private int resultCountPerQuery;

	private Configuration config;

	public CustomizedORMDAOImpl(SessionFactory sessionFactory,
			Configuration config, boolean caseSensitive, int resultCountPerQuery) {
		this.config = config;
		this.setSessionFactory(sessionFactory);
		this.caseSensitive = caseSensitive;
		this.resultCountPerQuery = resultCountPerQuery;
	}

	public Response query(Request request) throws DAOException {
		Session session = getSession();

		Object obj = request.getRequest();

		try {
			log.debug("****** obj: " + obj.getClass());

			if (obj instanceof DetachedCriteria) {
				return query(request, session, (DetachedCriteria) obj);
			} else if (obj instanceof NestedCriteriaPath) {
				return query(request, session, (NestedCriteriaPath) obj);
			} else if (obj instanceof HQLCriteria) {
				return query(request, session, (HQLCriteria) obj);
			} else if (obj instanceof CQLQuery) {
				return query(request, session, (CQLQuery) obj);
			} else
				throw new DAOException("Can not determine type of the query");

		} catch (JDBCException ex) {
			log.error("JDBC Exception in ORMDAOImpl ", ex);
			throw new DAOException("JDBC Exception in ORMDAOImpl ", ex);
		} catch (org.hibernate.HibernateException hbmEx) {
			log.error(hbmEx.getMessage());
			throw new DAOException("Hibernate problem ", hbmEx);
		} catch (Exception e) {
			log.error("Exception ", e);
			throw new DAOException("Exception in ORMDAOImpl ", e);
		}
		/*  not needed after introduced transaction manager in Spring
		finally {
			try {
				session.clear();
				session.close();
			} catch (Exception eSession) {
				log.error("Could not close the session - "
						+ eSession.getMessage());
				throw new DAOException("Could not close the session  "
						+ eSession);
			}
		}*/
	}

	public List<String> getAllClassNames() {

		List<String> allClassNames = new ArrayList<String>();
		Map allClassMetadata = getSessionFactory().getAllClassMetadata();

		for (Iterator iter = allClassMetadata.keySet().iterator(); iter
				.hasNext();) {
			allClassNames.add((String) iter.next());
		}

		return allClassNames;
	}

	private Response query(Request request, Session session,
			DetachedCriteria obj) throws Exception {
		List rs = null;
		Criteria hCriteria = null;
		Integer rowCount = null;

		Integer firstRow = request.getFirstRow();
		Boolean isCount = request.getIsCount();

		Response rsp = new Response();

		hCriteria = ((org.hibernate.criterion.DetachedCriteria) request
				.getRequest()).getExecutableCriteria(session);
		log.info("Detached Criteria Query :" + hCriteria.toString());

		if (hCriteria != null) {
			if (isCount != null && isCount.booleanValue()) {
				rowCount = (Integer) hCriteria.setProjection(
						Projections.rowCount()).uniqueResult();
				log.debug("DetachedCriteria ORMDAOImpl ===== count = "
						+ rowCount);
				rsp.setRowCount(rowCount);
			} else if ((isCount != null && !isCount.booleanValue())
					|| isCount == null) {
				if (firstRow != null) {
					hCriteria.setFirstResult(firstRow.intValue());

				}

				hCriteria.setMaxResults(resultCountPerQuery);

				rs = hCriteria.list();
				rsp.setRowCount(rs.size());
				rsp.setResponse(rs);
			}
		}

		return rsp;
	}

	private Response query(Request request, Session session,
			NestedCriteriaPath obj) throws Exception {
		List rs = null;
		Integer rowCount = null;

		Integer firstRow = request.getFirstRow();
		Boolean isCount = request.getIsCount();
		Query query = null;

		Response rsp = new Response();

		log.debug("ORMDAOImpl.query: it is a NestedCriteriaPath Object ....");
		NestedCriteria nc = Path2NestedCriteria.createNestedCriteria(obj
				.getpathString(), obj.getParameters(), request.getClassCache());
		NestedCriteria2HQL converter = new NestedCriteria2HQL(nc, config,
				session, caseSensitive);
		query = converter.translate();
		log.info("HQL Query :" + query.getQueryString());
		if (query != null) {
			if (isCount != null && isCount.booleanValue()) {
				converter.getCountQuery().setMaxResults(1);
				log
						.debug("ORMDAOImpl.  isCount .... .... | converter.getCountQuery() = "
								+ converter.getCountQuery().getQueryString());
				rowCount = Integer.parseInt(converter.getCountQuery()
						.uniqueResult()
						+ "");
				log.debug("ORMDAOImpl HQL ===== count = " + rowCount);
				rsp.setRowCount(rowCount);
			} else if ((isCount != null && !isCount.booleanValue())
					|| isCount == null) {
				if (firstRow != null) {
					log.debug("Setting First Row to " + firstRow);
					query.setFirstResult(firstRow.intValue());
				}

				query.setMaxResults(resultCountPerQuery);

				rs = query.list();

				rsp.setRowCount(rs.size());
				rsp.setResponse(rs);
			}
		}

		return rsp;
	}

	// if (obj instanceof HQLCriteria)
	private Response query(Request request, Session session, HQLCriteria obj)
			throws Exception {

		List rs = null;
		Integer rowCount = null;

		Integer firstRow = request.getFirstRow();
		Boolean isCount = request.getIsCount();

		Response rsp = new Response();

		if (isCount != null && isCount.booleanValue()) {
			Query hqlQuery = session
					.createQuery(getCountQuery(((HQLCriteria) obj)
							.getHqlString()));
			if (obj.getParameters() != null && obj.getParameters().size() > 0) {
				int count = 0;
				for (Object param : obj.getParameters())
					hqlQuery.setParameter(count++, param);
			}
			hqlQuery.setMaxResults(1);
			log.debug("HQL Criteria Query : isCount = "
					+ hqlQuery.getQueryString());
			rowCount = Integer.parseInt(hqlQuery.uniqueResult() + "");
			log.debug("HQL Criteria Query : count = " + rowCount);
			rsp.setRowCount(rowCount);

		} else if ((isCount != null && !isCount.booleanValue())
				|| isCount == null) {
			Query hqlQuery = session.createQuery(((HQLCriteria) obj)
					.getHqlString());
			if (obj.getParameters() != null && obj.getParameters().size() > 0) {
				int count = 0;
				for (Object param : obj.getParameters())
					hqlQuery.setParameter(count++, param);
			}
			log.info("HQL Criteria Query :" + hqlQuery.getQueryString());

			if (firstRow != null) {
				hqlQuery.setFirstResult(firstRow.intValue());
			}

			hqlQuery.setMaxResults(resultCountPerQuery);

			rs = hqlQuery.list();
			rsp.setRowCount(rs.size());
			rsp.setResponse(rs);
		}

		return rsp;
	}

	// if (obj instanceof CQLQuery)
	private Response query(Request request, Session session, CQLQuery obj)
			throws Exception {
		List rs = null;
		Integer rowCount = null;

		Integer firstRow = request.getFirstRow();
		Boolean isCount = request.getIsCount();

		Response rsp = new Response();

		CQL2HQL converter = new CQL2HQL(request.getClassCache());
		if (isCount != null && isCount.booleanValue()) {
			HQLCriteria hqlCriteria = converter.translate((CQLQuery) obj,
					false, caseSensitive);
			String hql = hqlCriteria.getHqlString();
			List params = hqlCriteria.getParameters();
			log.info("CQL Query :" + hql);
			Query hqlQuery = session.createQuery(getCountQuery(hql));

			for (int i = 0; i < params.size(); i++)
				hqlQuery.setParameter(i, params.get(i));

			hqlQuery.setMaxResults(1);
			log.debug("CQL Criteria Query : isCount = "
					+ hqlQuery.getQueryString());
			rowCount = Integer.parseInt(hqlQuery.uniqueResult() + "");
			log.debug("CQL Criteria Query : count = " + rowCount);
			rsp.setRowCount(rowCount);
		} else if ((isCount != null && !isCount.booleanValue())
				|| isCount == null) {
			HQLCriteria hqlCriteria = converter.translate((CQLQuery) obj,
					false, caseSensitive);
			String hql = hqlCriteria.getHqlString();
			List params = hqlCriteria.getParameters();
			log.info("CQL Query :" + hql);
			Query hqlQuery = session.createQuery(hql);

			for (int i = 0; i < params.size(); i++)
				hqlQuery.setParameter(i, params.get(i));

			hqlQuery.setMaxResults(100000);
			if (firstRow != null) {
				hqlQuery.setFirstResult(firstRow.intValue());
			}

			hqlQuery.setMaxResults(resultCountPerQuery);

			rs = hqlQuery.list();
			rsp.setRowCount(rs.size());
			rsp.setResponse(rs);
		}

		return rsp;
	}

	private String getCountQuery(String hql) {
		return NestedCriteria2HQL.getCountQuery(hql);
	}

	public Object load(Class domainClass, Serializable id) {
		return getHibernateTemplate().load(domainClass, id);
	}

	public Object get(Class domainClass, Serializable id) {
		return getHibernateTemplate().get(domainClass, id);
	}

	public void saveOrUpdate(Object t) {
		getHibernateTemplate().saveOrUpdate(t);
	}

	public void delete(Object t) {
		getHibernateTemplate().delete(t);
	}

	public void deleteById(Class domainClass, Serializable id) {
		Object obj = getHibernateTemplate().load(domainClass, id);
		getHibernateTemplate().delete(obj);
	}

	public List getAll(Class domainClass) {
		return getHibernateTemplate().loadAll(domainClass);
	}

	public Object getObject(Class domainClass, String uniqueKeyName,
			Object uniqueKeyValue) {

		DetachedCriteria crit = DetachedCriteria.forClass(domainClass).add(
				Property.forName(uniqueKeyName).eq(uniqueKeyValue));
		List results = getHibernateTemplate().findByCriteria(crit);

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
		/* not needed after introduced transaction manager in Spring
		finally {
			try {
				session.clear();
				session.close();
			} catch (Exception eSession) {
				log.error("Could not close the session - "
						+ eSession.getMessage());
				throw new DAOException("Could not close the session  "
						+ eSession);
			}
		}*/
	}
}

package gov.nih.nci.calab.db;

import java.io.Serializable;
import java.sql.Connection;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class HibernateDataAccess implements IDataAccess {

	final static Logger logger = Logger.getLogger(HibernateDataAccess.class);

	public SessionFactory sessionFactory;

	public final ThreadLocal<Session> thread = new ThreadLocal<Session>();

	/**
	 * Location of hibernate.cfg.xml file. NOTICE: Location should be on the
	 * classpath as Hibernate uses #resourceAsStream style lookup for its
	 * configuration file. That is place the config file in a Java package - the
	 * default location is the default Java package.<br>
	 * <br>
	 * Examples: <br>
	 * <code>CONFIG_FILE_LOCATION = "/hibernate.conf.xml". 
	 * CONFIG_FILE_LOCATION = "/com/foo/bar/myhiberstuff.conf.xml".</code>
	 */
	private static String CONFIG_FILE_LOCATION = "/orm1.cfg.xml";

	private static HibernateDataAccess instance = null;

	public Session session;

	public Transaction tx;

	private HibernateDataAccess() {
		sessionFactory = null;
		Configuration cfg = new Configuration();
		try {
			cfg.configure(CONFIG_FILE_LOCATION);
			sessionFactory = cfg.buildSessionFactory();
		} catch (Exception e) {
			logger.error("%%%% Error Creating SessionFactory %%%%", e);
			e.printStackTrace();
		}
	}

	public static HibernateDataAccess getInstance() {
		if (instance == null)
			instance = new HibernateDataAccess();
		return instance;
	}

	public void open() throws Exception {
		session = (Session) thread.get();
		if (session == null) {
			session = sessionFactory.openSession();
			thread.set(session);
		}
		tx = session.beginTransaction();
	}

	public void close() throws Exception {
		try {
			if (session.isOpen()) {
				if (!tx.wasRolledBack()) {
					tx.commit();
				}
			}
		} catch (org.hibernate.TransactionException txException) {
			logger.error("%%% Error to commit transaction %%%", txException);
		}

		Session s = (Session) thread.get();
		if (s != null) {
			s.close();
		}
		thread.set(null);
	}

	public void rollback() {
		if (session.isOpen()) {
			tx.rollback();
		}
	}

	public List search(String hql) throws Exception {
		List retList = session.createQuery(hql).list();
		return retList;
	}

	public List search(Class className, Object o) throws Exception {

		throw new Exception("Not supported yet");
	}

	public List search(Class className, List l) throws Exception {

		throw new Exception("Not supported yet");
	}

	public List search(Class className, Object o, String url) throws Exception {

		throw new Exception("Not supported yet");
	}

	public List query(Object criteria, String targetClassName) throws Exception {

		throw new Exception("Not supported yet");
	}

	public Object createObject(Object obj) throws Exception {
		// check if the obj exist in database, if it is, return the serializable
		// id
		// tx = session.beginTransaction();
		Object returnObj = session.save(obj);
		// tx.commit();
		return returnObj;
		// throw new Exception ("Not supported yet");
	}

	public Object updateObject(Object obj) throws Exception {
		throw new Exception("Not supported yet");
	}

	public void removeObject(Object obj) throws Exception {
		session.delete(obj);
		// throw new Exception ("Not supported yet");
	}

	public void store(Object o) throws Exception {
		// tx = session.beginTransaction();
		session.saveOrUpdate(o);
		// tx.commit();

	}

	public void update(Object o) {
		session.update(o);
	}

	public Object load(Class klass, long id) {
		return session.get(klass, id);
	}

	public Criteria createCriteria(Class klass) {
		return session.createCriteria(klass);
	}

	public void delete(Object o) {
		session.delete(o);
	}

	public Query createNamedQuery(String name) {
		return session.getNamedQuery(name);
	}

	public Object load(Class klass, Serializable id) throws Exception {
		return session.load(klass, id);
	}

	public List searchByParam(String hqlString, List paramList) {
		Query query = session.createQuery(hqlString);
		int i = 0;
		for (Object valueObj : paramList) {
			// log.debug("NestCriteria2HQL. param list i = " + i + " | value = "
			// + valueObj);
			if (valueObj instanceof String) {
				query.setString(i, (String) valueObj);
			} else if (valueObj instanceof Long) {
				query.setLong(i, ((Long) valueObj).longValue());
			} else if (valueObj instanceof Date) {
				query.setDate(i, (Date) valueObj);
			} else if (valueObj instanceof Boolean) {
				query.setBoolean(i, ((Boolean) valueObj).booleanValue());
			} else if (valueObj instanceof Double) {
				query.setDouble(i, ((Double) valueObj).doubleValue());
			} else if (valueObj instanceof Float) {
				query.setFloat(i, ((Float) valueObj).floatValue());
			} else if (valueObj instanceof Integer) {
				query.setInteger(i, ((Integer) valueObj).intValue());
			} else {
				query.setString(i, valueObj.toString());
			}
			i++;
		}
		return query.list();
	}

	/**
	 * Get a SQLQuery from a native SQL query
	 * 
	 * @param sqlQuery
	 * @return
	 */
	public SQLQuery getNativeQuery(String sqlQuery) {
		SQLQuery query = session.createSQLQuery(sqlQuery);
		return query;
	}

	public Connection getConnection() {
		return session.connection();
	}
}

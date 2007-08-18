package gov.nih.nci.calab.db;

import java.sql.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 * 
 * @author pansu
 * 
 */
public class HibernateUtil {
	private static Logger logger = Logger.getLogger(HibernateUtil.class);

	private static final SessionFactory sessionFactory;

	public static final ThreadLocal<Session> threadSession = new ThreadLocal<Session>();

	public static final ThreadLocal<Transaction> threadTransaction = new ThreadLocal<Transaction>();

	private static String CONFIG_FILE_LOCATION = "/orm1.cfg.xml";

	static {
		try {
			// Create the SessionFactory
			sessionFactory = new Configuration()
					.configure(CONFIG_FILE_LOCATION).buildSessionFactory();
		} catch (Throwable ex) {
			// Make sure you log the exception, as it might be swallowed
			logger.error("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static Session currentSession() throws HibernateException {
		Session s = threadSession.get();
		// Open a new Session, if this Thread has none yet
		if (s == null) {
			s = sessionFactory.openSession();
			threadSession.set(s);
		}
		return s;
	}

	public static void closeSession() throws HibernateException {
		commitTransaction();
		Session s = threadSession.get();
		threadSession.set(null);		
		if (s != null)
			s.close();
	}

	public static Transaction beginTransaction() {
		Transaction tx = threadTransaction.get();
		if (tx == null) {
			tx = currentSession().beginTransaction();
			threadTransaction.set(tx);
		}

		return tx;
	}

	public static void commitTransaction() {
		Transaction tx = threadTransaction.get();
		try {
			if (tx != null && !tx.wasCommitted() && !tx.wasRolledBack())
				tx.commit();
			threadTransaction.set(null);
		} catch (HibernateException ex) {
			rollbackTransaction();
			throw ex;
		}
	}

	public static void rollbackTransaction() {
		Transaction tx = threadTransaction.get();
		threadTransaction.set(null);
		try {
			if (tx != null && !tx.wasCommitted() && !tx.wasRolledBack()) {
				tx.rollback();
			}
		} finally {
			closeSession();
		}
	}

	public static Query createQueryByParam(String hqlString, List paramList) {
		Query query = currentSession().createQuery(hqlString);
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
		return query;
	}
}

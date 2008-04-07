package gov.nih.nci.cananolab.system.applicationservice.impl;

import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.system.dao.CustomizedORMDAO;
import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.applicationservice.impl.ApplicationServiceImpl;
import gov.nih.nci.system.util.ClassCache;

import java.io.Serializable;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.hibernate.Session;

/**
 * Customized to contain more CRUD operations.
 * 
 * @author pansu
 * 
 */
public class CustomizedApplicationServiceImpl extends ApplicationServiceImpl
		implements CustomizedApplicationService {
	private ClassCache classCache;

	private static Logger logger = Logger
			.getLogger(CustomizedApplicationServiceImpl.class.getName());

	public CustomizedApplicationServiceImpl(ClassCache classCache,
			Properties systemProperties) {
		super(classCache, systemProperties);
		this.classCache = classCache;
		// TODO Auto-generated constructor stub
	}

	public Object load(Class domainClass, Serializable id)
			throws ApplicationException {
		CustomizedORMDAO dao = (CustomizedORMDAO) classCache
				.getDAOForClass(domainClass.getCanonicalName());
		return dao.load(domainClass, id);
	}

	public void saveOrUpdate(Object object) throws ApplicationException {
		CustomizedORMDAO dao = (CustomizedORMDAO) classCache
				.getDAOForClass(object.getClass().getCanonicalName());
		try {
			dao.saveOrUpdate(object);
		} catch (Exception e) {
			String err = "Could not save or update for class "
					+ object.getClass().getCanonicalName();
			logger.error(err);
			throw new ApplicationException(err, e);
		}
	}

	public void delete(Object object) throws ApplicationException {
		CustomizedORMDAO dao = (CustomizedORMDAO) classCache
				.getDAOForClass(object.getClass().getCanonicalName());
		try {
			dao.delete(object);
		} catch (Exception e) {
			String err = "Could not delete for class "
					+ object.getClass().getCanonicalName();
			logger.error(err);
			throw new ApplicationException(err, e);
		}
	}

	public void deleteById(Class domainClass, Serializable id)
			throws ApplicationException {
		CustomizedORMDAO dao = (CustomizedORMDAO) classCache
				.getDAOForClass(domainClass.getCanonicalName());
		try {
			dao.deleteById(domainClass, id);
		} catch (Exception e) {
			String err = "Could not delete by Id for class "
					+ domainClass.getCanonicalName() + " with Id: " + id;
			logger.error(err);
			throw new ApplicationException(err, e);
		}
	}

	public List getAll(Class domainClass) throws ApplicationException {
		CustomizedORMDAO dao = (CustomizedORMDAO) classCache
				.getDAOForClass(domainClass.getCanonicalName());
		try {
			return dao.getAll(domainClass);
		} catch (Exception e) {
			String err = "Could not get all for class "
					+ domainClass.getCanonicalName();
			logger.error(err);
			throw new ApplicationException(err, e);
		}
	}

	public Object getObject(Class domainClass, String uniqueKeyName,
			Object uniqueKeyValue) throws ApplicationException {
		CustomizedORMDAO dao = (CustomizedORMDAO) classCache
				.getDAOForClass(domainClass.getCanonicalName());
		try {
			return dao.getObject(domainClass, uniqueKeyName, uniqueKeyValue);
		} catch (Exception e) {
			String err = "Could not get unique object for class "
					+ domainClass.getCanonicalName();
			logger.error(err);
			throw new ApplicationException(err, e);
		}
	}

	public Session getCurrentSession() throws ApplicationException {
		try {
			CustomizedORMDAO dao = (CustomizedORMDAO) classCache
					.getDAOForClass(NanoparticleSample.class.getCanonicalName());
			return dao.getCurrentSession();
		} catch (Exception e) {
			String err = "Could not get current session.";
			logger.error(err);
			throw new ApplicationException(err, e);
		}
	}
}

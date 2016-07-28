/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.system.applicationservice.impl;

import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.system.applicationservice.CaNanoLabApplicationService;
import gov.nih.nci.cananolab.system.dao.CaNanoLabORMDAO;
import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.applicationservice.impl.WritableApplicationServiceImpl;
import gov.nih.nci.system.util.ClassCache;

//import org.hibernate.type.StandardBasicTypes;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.aop.framework.Advised;
import org.springframework.stereotype.Component;

/**
 * Customized to contain more CRUD operations.
 *
 * @author pansu
 *
 */
public class CaNanoLabApplicationServiceImpl extends WritableApplicationServiceImpl implements CaNanoLabApplicationService
{
	private ClassCache classCache;

	private static Logger logger = Logger.getLogger(CaNanoLabApplicationServiceImpl.class.getName());

	public CaNanoLabApplicationServiceImpl(ClassCache classCache) {
		super(classCache);
		this.classCache = classCache;
	}

	public Object load(Class domainClass, Serializable id) throws ApplicationException {
		CaNanoLabORMDAO dao = (CaNanoLabORMDAO) classCache.getDAOForClass(domainClass.getCanonicalName());
		return dao.load(domainClass, id);
	}

	public Object get(Class domainClass, Serializable id) throws ApplicationException {
		CaNanoLabORMDAO dao = (CaNanoLabORMDAO) classCache.getDAOForClass(domainClass.getCanonicalName());
		return dao.get(domainClass, id);
	}

	public void saveOrUpdate(Object object) throws ApplicationException {
		try {
			CaNanoLabORMDAO dao = (CaNanoLabORMDAO) classCache
					.getDAOForClass(object.getClass().getCanonicalName());
			dao.saveOrUpdate(object);
		} catch (Exception e) {
			String err = "Could not save or update for class "
					+ object.getClass().getCanonicalName();
			logger.error(err);
			throw new ApplicationException(err, e);
		}
	}

	public void delete(Object object) throws ApplicationException {
		try {
			CaNanoLabORMDAO dao = (CaNanoLabORMDAO) classCache
					.getDAOForClass(object.getClass().getCanonicalName());
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
		CaNanoLabORMDAO dao = (CaNanoLabORMDAO) classCache
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
		CaNanoLabORMDAO dao = (CaNanoLabORMDAO) classCache
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
		CaNanoLabORMDAO dao = (CaNanoLabORMDAO) classCache
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

	public List directSQL(String directSQL, String[] columns,
			Object[] columnTypes) throws ApplicationException {
		CaNanoLabORMDAO dao = (CaNanoLabORMDAO) classCache
				.getDAOForClass(Sample.class.getCanonicalName());
		try {
			return dao.directSQL(directSQL, columns, columnTypes);
		} catch (Exception e) {
			String err = "Could not execute direct sql query ";
			logger.error(err);
			throw new ApplicationException(err, e);
		}
	}

	private Object unwrap(Object proxy) throws Exception {
		try {
			Object interceptor = null;
			int i = 0;
			while (true) {
				Field field = proxy.getClass().getDeclaredField(
						"CGLIB$CALLBACK_" + i);
				field.setAccessible(true);
				Object value = field.get(proxy);
				if (value.getClass().getName().contains("EqualsInterceptor")) {
					interceptor = value;
					break;
				}
				i++;
			}
			Field field = interceptor.getClass().getDeclaredField("advised");
			field.setAccessible(true);
			Advised advised = (Advised) field.get(interceptor);
			Object realObject = advised.getTargetSource().getTarget();
			return realObject;
		} catch (Exception e) {
			return proxy;
		}
	}

	/**
	 * Return a list of data (csm protected_group_name) marked public in the
	 * database
	 *
	 * @return
	 * @throws Exception
	 */
	public List<String> getAllPublicData() throws ApplicationException {
		List<String> publicData = new ArrayList<String>();
		try {
			String query = "select a.protection_group_name protection_group_name from csm_protection_group a, csm_role b, csm_user_group_role_pg c, csm_group d	"
					+ "where a.protection_group_id=c.protection_group_id and b.role_id=c.role_id and c.group_id=d.group_id and "
					+ "d.group_name='"
					+ AccessibilityBean.CSM_PUBLIC_GROUP
					+ "' and b.role_name='" + AccessibilityBean.CSM_READ_ROLE + "'";

			String[] columns = new String[] { "protection_group_name" };
			Object[] columnTypes = new Object[] { Hibernate.STRING };
			List results = directSQL(query, columns, columnTypes);
			for (int i = 0; i < results.size(); i++) {
				if (results.get(i) != null) {
					publicData.add(((String) results.get(i)));
				}
			}
		} catch (Exception e) {
			String err = "Could not execute direct sql query to find all public data";
			logger.error(err);
			throw new ApplicationException(err, e);
		}
		return publicData;
	}
}

package gov.nih.nci.cananolab.system.applicationservice;

import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.applicationservice.ApplicationService;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;

/**
 * Customized to contain more CRUD operations.
 * 
 * @author pansu
 * 
 */
public interface CustomizedApplicationService extends ApplicationService {

	public void saveOrUpdate(Object object) throws ApplicationException;

	public Object load(Class domainClass, Serializable id)
			throws ApplicationException;

	public void delete(Object object) throws ApplicationException;

	public void deleteById(Class domainClass, Serializable id)
			throws ApplicationException;

	public List getAll(Class domainClass) throws ApplicationException;

	public Object getObject(Class domainClass, String uniqueKeyName,
			Object uniqueKeyValue) throws ApplicationException;

	//used to execute native SQL queries for CSM, up for removal in next release
	public Session getCurrentSession() throws ApplicationException;
}

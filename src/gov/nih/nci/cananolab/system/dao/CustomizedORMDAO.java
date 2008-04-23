package gov.nih.nci.cananolab.system.dao;

import gov.nih.nci.system.dao.DAO;

import java.io.Serializable;
import java.util.List;

/**
 * Customized to contain generic CRUD operations.
 * 
 * @author pansu
 * 
 */
public interface CustomizedORMDAO extends DAO {

	public void saveOrUpdate(Object object);

	public Object load(Class domainClass, Serializable id);

	public Object get(Class domainClass, Serializable id);

	public void delete(Object object);

	public void deleteById(Class domainClass, Serializable id);

	public List getAll(Class domainClass);

	public Object getObject(Class domainClass, String uniqueKeyName,
			Object uniqueKeyValue);

	public List directQuery(String directSQL, String[] columns,
			Object[] columnTypes);
}

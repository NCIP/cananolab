/*
 * Created on Jul 12, 2005
 *
 *
 *
 */
package gov.nih.nci.calab.db;

import java.io.Serializable;
import java.util.List;

/**
 * @author chenjin
 *
 *
 *
 */
public class DataAccessProxy implements IDataAccess {

	private IDataAccess access = null;
	
	/* (non-Javadoc)
	 * @see gov.nih.nci.ncia.db.IDataAccess#open()
	 */
	public void open() throws Exception {
		
		access.open();
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.ncia.db.IDataAccess#close()
	 */
	public void close() throws Exception {
		
		access.close();
	}

	public void rollback()
	{
		access.rollback();
	}
	/* (non-Javadoc)
	 * @see gov.nih.nci.ncia.db.IDataAccess#retrieve(java.lang.String)
	 */
	public List search(String hql) throws Exception {
		
		return access.search(hql);
	}
	
	/* (non-Javadoc)
	 * @see gov.nih.nci.ncia.db.IDataAccess#retrieve(Class,java.lang.Object)
	 */
	public List search(Class className, Object o) throws Exception {
		
		return access.search(className, o);
	}
	
	public List search(Class className, List l ) throws Exception {
		return access.search(className, l);
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.ncia.db.IDataAccess#retrieve(Class,java.lang.Object,java.lang.String)
	 */
	public List search(Class className, Object o, String url) throws Exception {
		
		return access.search(className, o, url );
	}

	public List query(Object criteria, String targetClassName) throws Exception {
		
		return access.query( criteria, targetClassName );		
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.ncia.db.IDataAccess#store(java.lang.Object)
	 */
	public void store(Object o) throws Exception {
		
		access.store(o);
	}
	
	public Object createObject(Object obj) throws Exception
	{
		return access.createObject(obj);
	}
	
	public Object updateObject(Object obj) throws Exception
	{
		return access.updateObject(obj);
	}
	
	public void removeObject(Object obj) throws Exception
	{
		access.removeObject(obj);
	}
	
//	public List getObjects(Object obj) throws Exception
//	{
//		return access.getObjects(obj);
//	}
	
	public Object load(Class klass, Serializable id) throws Exception
	{
		return access.load(klass, id);
	}
	
	public IDataAccess getInstance(int type) throws Exception {
		switch (type) {
		case IDataAccess.HIBERNATE:
			access = HibernateDataAccess.getInstance();
			break;
		case IDataAccess.TOOLKITAPI:
			access = new ToolkitAPIDataAccess();
			break;		
 		default:
			throw new Exception( "type: " + type + " is not supported " );

		}
		return access;
	}

	public List searchByParam (String hqlstring, List paramList) throws Exception
	{
		return access.searchByParam(hqlstring, paramList);
	}

}

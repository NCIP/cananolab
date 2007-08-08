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
public interface IDataAccess {
	public static final int HIBERNATE = 0;
	public static final int TOOLKITAPI = 1;
//    public static final int HIBERNATE3 = 2;
//    public static final int REMOTETOOLKITAPI = 3;
//    public static final int LOCALTOOLKITAPI = 4;
    
	public abstract void open() throws Exception;
	public abstract void close() throws Exception;
	public abstract void rollback();
	public abstract List search( String HQL ) throws Exception;
	public abstract List search(  Class className, Object o ) throws Exception;
	public abstract List search(  Class className, List l ) throws Exception;
	public abstract List search(  Class className, Object o, String url ) throws Exception;
	public abstract List query(Object criteria, String targetClassName) throws Exception;
	/**
	 * SaveOrUpdate for Hibernate Access
	 * @param o The object to persist or update
	 */
	public abstract void store( Object o ) throws Exception;
	/**
	 * Save for either Hibernate access or Toolkit access
	 * @param obj The object instance to persist
	 * @return
	 * @throws Exception
	 */
	public abstract Object createObject(Object obj) throws Exception;
	
	public abstract Object updateObject(Object obj) throws Exception;
	public abstract void removeObject(Object obj) throws Exception;
//	public abstract List getObjects(Object obj) throws Exception;
	public abstract Object load(Class klass, Serializable id) throws Exception;
	public abstract Object get(Class klass, Serializable id) throws Exception;
	public abstract List searchByParam (String hqlstring, List paramList) throws Exception;
}

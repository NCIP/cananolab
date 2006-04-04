/*
 * Created on Dec 21, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gov.nih.nci.system.dao.impl.orm;

import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.apache.log4j.*;
import gov.nih.nci.system.servicelocator.*;
/**
 * @author 
 *
 * ORMConnection is a singleton object that is used to create Hibernate SessionFactory object.
 */
public class ORMConnection {

	private static Logger log = Logger.getLogger(ORMConnection.class.getName());
	static private ORMConnection orm_instance;
    private static final ThreadLocal threadLocal = new ThreadLocal();

	
	private SessionFactory[] sessionFactories;
	private Configuration[] configurations;

    /**
     * Private Constructor, Hibernate SessionFactory is instantiated here.
     *
     */
    private ORMConnection() 
    {
    	// Get ServiceLocator
    	ServiceLocator serviceLocator = new ServiceLocator();
    	// Get the total number of ORM datasource from ServiceLocator
    	 int count = serviceLocator.getORMCount();
    	//int count = 2;
    	sessionFactories = new SessionFactory[count];
    	configurations = new Configuration[count];
 	   try {
 	   	   for (int i=0; i<count; i++)
 	   	   {
 	   	   	   String cfgFileName = "orm"+(i+1)+".cfg.xml";
 	   	   	   configurations[i] = new Configuration().configure(cfgFileName);
 	   	   	   sessionFactories[i] = configurations[i].buildSessionFactory();
//	   sessionFactories[i] = new Configuration().configure(cfgFileName).buildSessionFactory();

 	   	   }
 	   }
	   catch (HibernateException e)
	   {
	   	log.error(e.getMessage());
	   	e.printStackTrace();
	   	throw e;
	   }
    }

    /**
     * Return an ORMConnection object.  This method ganrantees there are only
     * one object of ORMConnection in the server.
     *  
     * @return an object of ORMConnection
     */
    static public ORMConnection getInstance()
    {
        if ( orm_instance == null )
        {
        	return getNewInstance();        	
        }
        else
        {
        	return orm_instance;        	
        }
    }
    
    /**
     * Return an new ORMConnection object.  
     *  
     * @return an object of ORMConnection
     */
    static public ORMConnection getNewInstance()
    {
    	orm_instance = new ORMConnection();
    	return orm_instance;
    }
	
    /**
     * Return a Hibernate Session object
     * 
     * @return an object of Hibernate Session
     * 
     * @throws HibernateException
     */
    public Session openSession(int counter) throws HibernateException
    {
    	if (sessionFactories != null)
    	{
    		return sessionFactories[counter-1].openSession();
    	}
    	else 
    	{
    		log.error("No Hibernate Session Factory ... ...");
    		throw new HibernateException("No Hibernate Session Factory ... ...");
    	}
    }
    
    /**
     * Return a Hibernate Session object
     * 
     * @return an object of Hibernate Session
     * 
     * @throws HibernateException
     */
    public Configuration getConfiguration(int counter) throws HibernateException
    {
    	if (configurations != null)
    	{
    		return configurations[counter-1];
    	}
    	else 
    	{
    		log.error("No Hibernate Configurations ... ...");
    		throw new HibernateException("No Hibernate Configurations ... ...");
    	}
    }  
    
    public SessionFactory getSessionFactory(int counter) throws HibernateException
    {
    	if (sessionFactories != null)
    	{
    		return sessionFactories[counter-1];
    	}
    	else 
    	{
    		log.error("No Hibernate Configurations ... ...");
    		throw new HibernateException("No Hibernate Configurations ... ...");
    	}
    }    
    
}

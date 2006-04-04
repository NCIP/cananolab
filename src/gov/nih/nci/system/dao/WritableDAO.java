/*
 * Created on Jul 17, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gov.nih.nci.system.dao;

import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.dao.impl.orm.ORMConnection;
import gov.nih.nci.system.servicelocator.ServiceLocator;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Example;

/**
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 *
 * @author Ekagra Software Technologies Ltd.
 */
public class WritableDAO
{
	static final Logger log = Logger.getLogger(WritableDAO.class.getName());

	public Object createObject(Object object) throws ApplicationException
	{
		Session session = null;
		Transaction transaction = null;
		try
		{
			session = this.getSession(object);
		}
		catch (Exception ex)
		{
			throw new ApplicationException("Could not obtain session for this entity! Could not create object");
		}
		if (session == null)
		{
			throw new ApplicationException("Could not obtain session for this entity! Could not create object");
		}
		try
		{
			transaction = session.beginTransaction();
			session.save(object);
			transaction.commit();
		}
		catch (Exception ex)
		{
			log.error(ex);
			try
			{
				transaction.rollback();
			}
			catch (Exception ex3)
			{
				if (log.isDebugEnabled())
					log.debug("createObject|Failure|Error in Rolling Back Transaction|" + ex3.getMessage());
			}

			log.error("createObject|Failure|Error in creating the " + object.getClass().getName() + ex.getMessage());

			throw new ApplicationException("An error occured in creating the " + object.getClass().getName() + "\n" + ex.getMessage());
		}
		finally
		{
			try
			{
				session.close();
			}
			catch (Exception ex2)
			{
				if (log.isDebugEnabled())
					log.debug("createObject|Failure|Error in Closing Session |" + ex2.getMessage());
			}
		}
		if (log.isDebugEnabled())
			log.debug("createObject|Success|Successful in creating the " + object.getClass().getName());
		
		return object;

	}

	public Object updateObject(Object object) throws ApplicationException
	{
		Session session = null;
		Transaction transaction = null;
		try
		{
			session = this.getSession(object);
		}
		catch (Exception ex)
		{
			throw new ApplicationException("Could not obtain session for this entity! Could not update the object");
		}
		if (session == null)
		{
			throw new ApplicationException("Could not obtain session for this entity! Could not update the object");
		}
		try
		{
			transaction = session.beginTransaction();
			session.update(object);
			transaction.commit();
		}
		catch (Exception ex)
		{
			log.error(ex);
			try
			{
				transaction.rollback();
			}
			catch (Exception ex3)
			{
				if (log.isDebugEnabled())
					log.debug("updateObject|Failure|Error in Rolling Back Transaction|" + ex3.getMessage());
			}

			log.error("updateObject|Failure|Error in updating the " + object.getClass().getName() + ex.getMessage());

			throw new ApplicationException("An error occured in updating the " + object.getClass().getName() + "\n" + ex.getMessage());
		}
		finally
		{
			try
			{
				session.close();
			}
			catch (Exception ex2)
			{
				if (log.isDebugEnabled())
					log.debug("updateObject|Failure|Error in Closing Session |" + ex2.getMessage());
			}
		}
		if (log.isDebugEnabled())
			log.debug("updateObject|Success|Successful in updating the " + object.getClass().getName());
		return object;
	}

	public void removeObject(Object obj) throws ApplicationException
	{
		Session session = null;
		Transaction transaction = null;
		try
		{
			session = this.getSession(obj);
		}
		catch (Exception ex)
		{
			throw new ApplicationException("Could not obtain session for this entity! Could not remove the object");
		}

		if (session == null)
		{
			throw new ApplicationException("Could not obtain session for this entity! Could not remove the object");
		}
		try
		{
			transaction = session.beginTransaction();
			session.delete(obj);
			transaction.commit();
		}
		catch (Exception ex)
		{
			log.error(ex);
			try
			{
				transaction.rollback();
			}
			catch (Exception ex3)
			{
				if (log.isDebugEnabled())
					log.debug("deleteObject|Failure|Error in Rolling Back Transaction|" + ex3.getMessage());
			}

			log.error("deleteObject|Failure|Error in removing the " + obj.getClass().getName() + ex.getMessage());

			throw new ApplicationException("An error occured in removing the " + obj.getClass().getName() + "\n" + ex.getMessage());
		}
		finally
		{
			try
			{
				session.close();
			}
			catch (Exception ex2)
			{
				if (log.isDebugEnabled())
					log.debug("deleteObject|Failure|Error in Closing Session |" + ex2.getMessage());
			}
		}
		if (log.isDebugEnabled())
			log.debug("deleteObject|Success|Successful in deleting the " + obj.getClass().getName());
		
	}

	public List getObjects(Object object) throws ApplicationException
	{
		Session session = null;

		try
		{
			session = this.getSession(object);
		}
		catch (Exception ex)
		{
			throw new ApplicationException("Could not obtain session for this entity! Could not get objects");
		}
		if (session == null)
		{
			throw new ApplicationException("Could not obtain session for this entity! Could not get the objects");
		}
		List list = null;
		try
		{
			Criteria criteria = session.createCriteria(object.getClass());
			criteria.add(Example.create(object));
			list = criteria.list();
		}
		catch (Exception ex)
		{
			log.error("getObjects|Failure|Error in getting objects" + ex.getMessage());

			throw new ApplicationException("An error occured in getting objects" + "\n" + ex.getMessage());
		}
		finally
		{
			try
			{
				session.close();
			}
			catch (Exception ex2)
			{
				if (log.isDebugEnabled())
					log.debug("getObjects|Failure|Error in Closing Session |" + ex2.getMessage());
			}
		}
		if (log.isDebugEnabled())
			log.debug("getObjects|Success|Successful in deleting the " + object.getClass().getName());
		
		return list;
	}

	private Session getSession(Object object) throws Exception
	{
		ServiceLocator serviceLocator = new ServiceLocator();
		int oRMCounter = serviceLocator.getORMCounter(object.getClass().getName());
		ORMConnection oRMConnection = ORMConnection.getInstance();
		Session session = oRMConnection.openSession(oRMCounter);
		return session;
	}
}

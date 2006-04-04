package gov.nih.nci.system.applicationservice.impl;

import gov.nih.nci.common.util.HQLCriteria;
import gov.nih.nci.evs.query.EVSQuery;
import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.applicationservice.ApplicationServiceProvider;
import gov.nih.nci.system.dao.WritableDAO;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

/**
 * @author Kunal Modi (Ekagra Software Technologies Ltd.)
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ApplicationServiceImpl extends ApplicationService
{

	private ApplicationServiceBusinessImpl applicationServiceBusinessImpl = null;
	private WritableDAO writableDAO = null;

	/**
	 * Default Constructor. It obtains appropriate implementation of the
	 * {@link ApplicationService}interface and caches it. It also instantiates
	 * the instance of writableDAO and caches it.
	 */
	public ApplicationServiceImpl()
	{
		this.applicationServiceBusinessImpl = ApplicationServiceBusinessImpl.getLocalInstance();
		this.writableDAO = new WritableDAO();
	}


	/* (non-Javadoc)
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#getBeanInstance()
	 */
	@Override
	protected ApplicationService getBeanInstance()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#getBeanInstance(java.lang.String)
	 */
	@Override
	protected ApplicationService getBeanInstance(String URL)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#setRecordsCount(int)
	 */
	@Override
	public void setRecordsCount(int recordsCount) throws ApplicationException
	{
		try
		{
			this.applicationServiceBusinessImpl.setRecordsCount(recordsCount);
		}
		catch (Exception e)
		{
			throw new ApplicationException(e.getMessage());
		}
	}
	
	/* (non-Javadoc)
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#setSearchCaseSensitivity(boolean)
	 */
	public void setSearchCaseSensitivity(boolean caseSensitivity)
	{
		this.applicationServiceBusinessImpl.setSearchCaseSensitivity(caseSensitivity);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#getQueryRowCount(java.lang.Object,
	 *      java.lang.String)
	 */
	public int getQueryRowCount(Object criteria, String targetClassName) throws ApplicationException
	{
		try
		{
			return this.applicationServiceBusinessImpl.getQueryRowCount(criteria, targetClassName);
		}
		catch (Exception e)
		{
			throw new ApplicationException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#query(java.lang.Object,
	 *      java.lang.String)
	 */
	public List query(DetachedCriteria detachedcriteria, String targetClassName) throws ApplicationException
	{
		try
		{
			return this.applicationServiceBusinessImpl.query(detachedcriteria, targetClassName);
		}
		catch (Exception e)
		{
			throw new ApplicationException(e.getMessage());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#query(java.lang.Object,
	 *      java.lang.String)
	 */
	public List query(HQLCriteria hqlcriteria, String targetClassName) throws ApplicationException
	{
		try
		{
			return this.applicationServiceBusinessImpl.query(hqlcriteria, targetClassName);
		}
		catch (Exception e)
		{
			throw new ApplicationException(e.getMessage());
		}
	}	

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#query(java.lang.Object,
	 *      int, int, java.lang.String)
	 */
	public List query(Object criteria, int firstRow, int resultsPerQuery, String targetClassName) throws ApplicationException
	{
		try
		{
			return this.applicationServiceBusinessImpl.query(criteria, firstRow, resultsPerQuery, targetClassName);
		}
		catch (Exception e)
		{
			throw new ApplicationException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#evsSearch(gov.nih.nci.evs.query.EVSQuery)
	 */
	public List evsSearch(EVSQuery evsCriterion) throws ApplicationException
	{
		try
		{
			return this.applicationServiceBusinessImpl.evsSearch(evsCriterion);
		}
		catch (Exception e)
		{
			throw new ApplicationException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#search(java.lang.Class,
	 *      java.lang.Object)
	 */
	public List search(Class targetClass, Object obj) throws ApplicationException
	{
		try
		{
			return this.applicationServiceBusinessImpl.search(targetClass, obj);
		}
		catch (Exception e)
		{
			throw new ApplicationException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#search(java.lang.Class,
	 *      java.util.List)
	 */
	public List search(Class targetClass, List objList) throws ApplicationException
	{
		try
		{
			return this.applicationServiceBusinessImpl.search(targetClass, objList);
		}
		catch (Exception e)
		{
			throw new ApplicationException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#search(java.lang.String,
	 *      java.lang.Object)
	 */
	public List search(String path, Object obj) throws ApplicationException
	{
		try
		{
			return this.applicationServiceBusinessImpl.search(path, obj);
		}
		catch (Exception e)
		{
			throw new ApplicationException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#search(java.lang.String,
	 *      java.util.List)
	 */
	public List search(String path, List objList) throws ApplicationException
	{
		try
		{
			return this.applicationServiceBusinessImpl.search(path, objList);
		}
		catch (Exception e)
		{
			throw new ApplicationException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#createObject(java.lang.Object)
	 */
	/*@WRITABLE_API_START@*/
	// NOTE: Use only "//" for comments in the following method
	public Object createObject(Object obj) throws ApplicationException
	{
		return writableDAO.createObject(obj);
	}
	/*@WRITABLE_API_END@*/

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#updateObject(java.lang.Object)
	 */
	/*@WRITABLE_API_START@*/
	// NOTE: Use only "//" for comments in the following method
	public Object updateObject(Object obj) throws ApplicationException
	{
		return writableDAO.updateObject(obj);
	}
	/*@WRITABLE_API_END@*/

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#removeObject(java.lang.Object)
	 */
	/*@WRITABLE_API_START@*/
	// NOTE: Use only "//" for comments in the following method
	public void removeObject(Object obj) throws ApplicationException
	{
		writableDAO.removeObject(obj);
	}
	/*@WRITABLE_API_END@*/

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#getObjects(java.lang.Object)
	 */
	/*@WRITABLE_API_START@*/
	// NOTE: Use only "//" for comments in the following method
	public List getObjects(Object obj) throws ApplicationException
	{
		return writableDAO.getObjects(obj);
	}
	/*@WRITABLE_API_END@*/

}

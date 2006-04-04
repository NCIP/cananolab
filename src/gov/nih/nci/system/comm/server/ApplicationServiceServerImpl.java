package gov.nih.nci.system.comm.server;

import gov.nih.nci.common.util.ClientInfo;
import gov.nih.nci.common.util.ClientInfoThreadVariable;
import gov.nih.nci.common.util.Constant;
import gov.nih.nci.common.util.HQLCriteria;
import gov.nih.nci.evs.query.EVSQuery;
import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.comm.common.ApplicationServiceProxy;
import gov.nih.nci.system.server.mgmt.SecurityEnabler;

import java.util.List;
import java.util.StringTokenizer;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ApplicationServiceServerImpl implements ApplicationServiceProxy
{

	private ApplicationService applicationService;
	private SecurityEnabler securityEnabler;

	/**
	 * Default Constructor it takes in 
	 */
	public ApplicationServiceServerImpl()
	{
		securityEnabler = new SecurityEnabler(Constant.APPLICATION_NAME);
		ApplicationContext ctx = new ClassPathXmlApplicationContext(Constant.APPLICATION_SERVICE_FILE_NAME);
		applicationService = (ApplicationService) ctx.getBean(Constant.APPLICATION_SERVICE);
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.comm.common.ApplicationServiceProxy#authenticate(java.lang.String, java.lang.String)
	 */
	public String authenticate(String userId, String password) throws ApplicationException
	{
		return securityEnabler.authenticate(userId, password);
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.comm.common.ApplicationServiceProxy#logOut(java.lang.String)
	 */
	public void logOut(String sessionKey)
	{
		securityEnabler.logOut(sessionKey);
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.comm.common.ApplicationServiceProxy#setSearchCaseSensitivity(java.lang.String, boolean)
	 */
	public void setSearchCaseSensitivity(ClientInfo clientInfo)
	{
		// do nothing
	}
	
	/* (non-Javadoc)
	 * @see gov.nih.nci.system.comm.common.ApplicationServiceProxy#setSearchCaseSensitivity(java.lang.String, boolean)
	 */
	public void setRecordsCount(ClientInfo clientInfo)
	{
		// do nothing
	}	

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.comm.common.ApplicationServiceProxy#search(gov.nih.nci.common.util.ClientInfo, java.lang.String, java.util.List)
	 */
	public List search(ClientInfo clientInfo, String path, List objList) throws ApplicationException
	{
		ClientInfoThreadVariable.setClientInfo(clientInfo);
		
		if (securityEnabler.getSecurityLevel() > 0)
		{
			String newPath = new String(path);
			if (objList.size() != 0)
				newPath = newPath.concat("," + objList.get(0).getClass().getName());
			newPath = newPath.replaceAll("Impl","");
			newPath = newPath.replaceAll("impl.","");
			StringTokenizer tokenPath = new StringTokenizer(newPath, ",");
			while (tokenPath.hasMoreTokens())
			{
				String domainObjectName =  tokenPath.nextToken().trim();
				if (!securityEnabler.hasAuthorization(clientInfo.getSessionKey(),domainObjectName, "READ"))
					throw new ApplicationException("User does not have privilege to perform a READ on " + domainObjectName+ " object");
			}
		}
		
		return applicationService.search(path, objList);

	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.comm.common.ApplicationServiceProxy#search(gov.nih.nci.common.util.ClientInfo, java.lang.String, java.lang.Object)
	 */
	public List search(ClientInfo clientInfo, String path, Object obj) throws ApplicationException
	{
		ClientInfoThreadVariable.setClientInfo(clientInfo);
		
		if (securityEnabler.getSecurityLevel() > 0)
		{
			String newPath = new String(path);
			if (obj != null)
				newPath = newPath.concat("," + obj.getClass().getName());
			newPath = newPath.replaceAll("Impl","");
			newPath = newPath.replaceAll("impl.","");
			StringTokenizer tokenPath = new StringTokenizer(newPath, ",");
			while (tokenPath.hasMoreTokens())
			{
				String domainObjectName =  tokenPath.nextToken().trim();
				if (!securityEnabler.hasAuthorization(clientInfo.getSessionKey(),domainObjectName, "READ"))
					throw new ApplicationException("User does not have privilege to perform a READ on " + domainObjectName+ " object");
			}
		}
		return applicationService.search(path, obj);

	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.comm.common.ApplicationServiceProxy#search(gov.nih.nci.common.util.ClientInfo, java.lang.Class, java.util.List)
	 */
	public List search(ClientInfo clientInfo, Class targetClass, List objList) throws ApplicationException
	{
		ClientInfoThreadVariable.setClientInfo(clientInfo);
		
		if (securityEnabler.getSecurityLevel() > 0)
		{
			String newPath = new String(targetClass.getName());
			if (objList.size() != 0)
				newPath = newPath.concat("," + objList.get(0).getClass().getName());
			newPath = newPath.replaceAll("Impl","");
			newPath = newPath.replaceAll("impl.","");
			StringTokenizer tokenPath = new StringTokenizer(newPath, ",");
			while (tokenPath.hasMoreTokens())
			{
				String domainObjectName =  tokenPath.nextToken().trim();
				if (!securityEnabler.hasAuthorization(clientInfo.getSessionKey(),domainObjectName, "READ"))
					throw new ApplicationException("User does not have privilege to perform a READ on " + domainObjectName+ " object");
			}
		}

		return applicationService.search(targetClass, objList);

	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.comm.common.ApplicationServiceProxy#search(gov.nih.nci.common.util.ClientInfo, java.lang.Class, java.lang.Object)
	 */
	public List search(ClientInfo clientInfo, Class targetClass, Object obj) throws ApplicationException
	{
		ClientInfoThreadVariable.setClientInfo(clientInfo);
		
		if (securityEnabler.getSecurityLevel() > 0)
		{
			String newPath = new String(targetClass.getName());
			if (obj != null)
				newPath = newPath.concat("," + obj.getClass().getName());
			newPath = newPath.replaceAll("Impl","");
			newPath = newPath.replaceAll("impl.","");
			StringTokenizer tokenPath = new StringTokenizer(newPath, ",");
			while (tokenPath.hasMoreTokens())
			{
				String domainObjectName =  tokenPath.nextToken().trim();
				if (!securityEnabler.hasAuthorization(clientInfo.getSessionKey(),domainObjectName, "READ"))
					throw new ApplicationException("User does not have privilege to perform a READ on " + domainObjectName+ " object");
			}
		}

		return applicationService.search(targetClass, obj);

	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.comm.common.ApplicationServiceProxy#query(gov.nih.nci.common.util.ClientInfo, java.lang.Object, int, int, java.lang.String)
	 */
	public List query(ClientInfo clientInfo, Object criteria, int firstRow, int resultsPerQuery, String targetClassName) throws ApplicationException
	{
		ClientInfoThreadVariable.setClientInfo(clientInfo);
		
		List list = applicationService.query(criteria, firstRow, resultsPerQuery, targetClassName);

		if (securityEnabler.getSecurityLevel() > 0)
		{
			if (list.size() != 0)
				targetClassName.concat("," + list.get(0).getClass().getName());
			StringTokenizer tokenPath = new StringTokenizer(targetClassName, ",");
			while (tokenPath.hasMoreTokens())
			{
				String domainObjectName =  tokenPath.nextToken().trim();
				if (!securityEnabler.hasAuthorization(clientInfo.getSessionKey(),domainObjectName, "READ"))
					throw new ApplicationException("User does not have privilege to perform a READ on " + domainObjectName+ " object");
			}
		}

		return list;

	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.comm.common.ApplicationServiceProxy#query(gov.nih.nci.common.util.ClientInfo, org.hibernate.criterion.DetachedCriteria, java.lang.String)
	 */
	public List query(ClientInfo clientInfo, DetachedCriteria detachedCriteria, String targetClassName) throws ApplicationException
	{
		ClientInfoThreadVariable.setClientInfo(clientInfo);
		
		List list = applicationService.query(detachedCriteria, targetClassName);
		
		if (securityEnabler.getSecurityLevel() > 0)
		{
			if (list.size() != 0)
				targetClassName.concat("," + list.get(0).getClass().getName());
			StringTokenizer tokenPath = new StringTokenizer(targetClassName, ",");
			while (tokenPath.hasMoreTokens())
			{
				String domainObjectName =  tokenPath.nextToken().trim();
				if (!securityEnabler.hasAuthorization(clientInfo.getSessionKey(),domainObjectName, "READ"))
					throw new ApplicationException("User does not have privilege to perform a READ on " + domainObjectName+ " object");
			}
		}

		return list;

	}
	
	/* (non-Javadoc)
	 * @see gov.nih.nci.system.comm.common.ApplicationServiceProxy#query(gov.nih.nci.common.util.ClientInfo, gov.nih.nci.common.util.HQLCriteria, java.lang.String)
	 */
	public List query(ClientInfo clientInfo, HQLCriteria hqlCriteria, String targetClassName) throws ApplicationException
	{
		ClientInfoThreadVariable.setClientInfo(clientInfo);
		
		List list = applicationService.query(hqlCriteria, targetClassName);
		
		if (securityEnabler.getSecurityLevel() > 0)
		{
			if (list.size() != 0)
				targetClassName.concat("," + list.get(0).getClass().getName());
			StringTokenizer tokenPath = new StringTokenizer(targetClassName, ",");
			while (tokenPath.hasMoreTokens())
			{
				String domainObjectName =  tokenPath.nextToken().trim();
				if (!securityEnabler.hasAuthorization(clientInfo.getSessionKey(),domainObjectName, "READ"))
					throw new ApplicationException("User does not have privilege to perform a READ on " + domainObjectName+ " object");
			}
		}

		return list;

	}
	
	/* (non-Javadoc)
	 * @see gov.nih.nci.system.comm.common.ApplicationServiceProxy#getQueryRowCount(gov.nih.nci.common.util.ClientInfo, java.lang.Object, java.lang.String)
	 */
	public int getQueryRowCount(ClientInfo clientInfo, Object criteria, String targetClassName) throws ApplicationException
	{
		ClientInfoThreadVariable.setClientInfo(clientInfo);

		if (securityEnabler.getSecurityLevel() > 0)
		{
			if (!securityEnabler.hasAuthorization(clientInfo.getSessionKey(),targetClassName, "READ"))
				throw new ApplicationException("User does not have privilege to perform a READ on " + targetClassName+ " object");
		}
		
		return applicationService.getQueryRowCount(criteria, targetClassName);

	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.comm.common.ApplicationServiceProxy#evsSearch(gov.nih.nci.common.util.ClientInfo, gov.nih.nci.evs.query.EVSQuery)
	 */
	public List evsSearch(ClientInfo clientInfo, EVSQuery evsCriterion) throws ApplicationException
	{
		ClientInfoThreadVariable.setClientInfo(clientInfo);
		
		List list = applicationService.evsSearch(evsCriterion);
		if (securityEnabler.getSecurityLevel() > 0)
		{
			String returnObjectName = "";
			if (list.size() != 0)
				returnObjectName = list.get(0).getClass().getName();
			if (!securityEnabler.hasAuthorization(clientInfo.getSessionKey(),returnObjectName, "READ"))
				throw new ApplicationException("User does not have privilege to perform a READ on " + returnObjectName+ " object");
		}

		return list;

	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.comm.common.ApplicationServiceProxy#createObject(gov.nih.nci.common.util.ClientInfo, java.lang.Object)
	 */	
	/*@WRITABLE_API_START@*/
	// NOTE: Use only "//" for comments in the following method
	public Object createObject(ClientInfo clientInfo, Object domainobject) throws ApplicationException
	{

		if (securityEnabler.getSecurityLevel() > 0)
		{

			String domainObjectName = domainobject.getClass().getName();
			try
			{
				if (!securityEnabler.hasAuthorization(clientInfo.getSessionKey(), domainObjectName, "CREATE"))
				{
					throw new ApplicationException("User does not have privilege to CREATE " + domainObjectName + " object");
				}
			}
			catch (ApplicationException e)
			{
				throw new ApplicationException(e.getMessage());
			}
		}

		return applicationService.createObject(domainobject);

	}
	/*@WRITABLE_API_END@*/

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.comm.common.ApplicationServiceProxy#updateObject(gov.nih.nci.common.util.ClientInfo, java.lang.Object)
	 */
	/*@WRITABLE_API_START@*/
	// NOTE: Use only "//" for comments in the following method
	public Object updateObject(ClientInfo clientInfo, Object domainobject) throws ApplicationException
	{

		if (securityEnabler.getSecurityLevel() > 0)
		{
			try
			{
				String domainObjectName = domainobject.getClass().getName();
				if (!securityEnabler.hasAuthorization(clientInfo.getSessionKey(), domainObjectName, "UPDATE"))
				{
					throw new ApplicationException("User does not have privilege to CREATE " + domainObjectName + " object");
				}
			}
			catch (ApplicationException e)
			{
				throw new ApplicationException(e.getMessage());
			}
		}

		return applicationService.updateObject(domainobject);

	}
	/*@WRITABLE_API_END@*/

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.comm.common.ApplicationServiceProxy#removeObject(gov.nih.nci.common.util.ClientInfo, java.lang.Object)
	 */
	/*@WRITABLE_API_START@*/
	// NOTE: Use only "//" for comments in the following method
	public void removeObject(ClientInfo clientInfo, Object domainobject) throws ApplicationException
	{

		if (securityEnabler.getSecurityLevel() > 0)
		{
			try
			{
				String domainObjectName = domainobject.getClass().getName();
				if (!securityEnabler.hasAuthorization(clientInfo.getSessionKey(), domainObjectName, "DELETE"))
				{
					throw new ApplicationException("User does not have privilege to DELETE " + domainObjectName + " object");
				}
			}
			catch (ApplicationException e)
			{
				throw new ApplicationException(e.getMessage());
			}
		}

		applicationService.removeObject(domainobject);

	}
	/*@WRITABLE_API_END@*/

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.comm.common.ApplicationServiceProxy#getObjects(gov.nih.nci.common.util.ClientInfo, java.lang.Object)
	 */
	/*@WRITABLE_API_START@*/
	// NOTE: Use only "//" for comments in the following method
	public List getObjects(ClientInfo clientInfo, Object domainobject) throws ApplicationException
	{

		if (securityEnabler.getSecurityLevel() > 0)
		{
			try
			{
				String domainObjectName = domainobject.getClass().getName();
				if (!securityEnabler.hasAuthorization(clientInfo.getSessionKey(), domainObjectName, "READ"))
				{
					throw new ApplicationException("User does not have privilege to CREATE " + domainObjectName + " object");
				}
			}
			catch (ApplicationException e)
			{
				throw new ApplicationException(e.getMessage());
			}
		}

		return applicationService.getObjects(domainobject);

	}
	/*@WRITABLE_API_END@*/

}
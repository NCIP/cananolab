package gov.nih.nci.system.applicationservice.impl;

import gov.nih.nci.common.net.Request;
import gov.nih.nci.common.net.Response;
import gov.nih.nci.common.util.ClientInfoThreadVariable;
import gov.nih.nci.common.util.Constant;
import gov.nih.nci.common.util.HQLCriteria;
import gov.nih.nci.common.util.ListProxy;
import gov.nih.nci.common.util.NestedCriteria;
import gov.nih.nci.common.util.PrintUtils;
import gov.nih.nci.common.util.SearchUtils;
import gov.nih.nci.evs.query.EVSQuery;
import gov.nih.nci.system.applicationservice.HTTPClient;
import gov.nih.nci.system.proxy.InterfaceProxy;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;

/**
 * <!-- LICENSE_TEXT_START -->
 * Copyright 2001-2004 SAIC. Copyright 2001-2003 SAIC. This software was developed in conjunction with the National Cancer Institute,
 * and so to the extent government employees are co-authors, any rights in such works shall be subject to Title 17 of the United States Code, section 105.
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the disclaimer of Article 3, below. Redistributions
 * in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other
 * materials provided with the distribution.
 * 2. The end-user documentation included with the redistribution, if any, must include the following acknowledgment:
 * "This product includes software developed by the SAIC and the National Cancer Institute."
 * If no such end-user documentation is to be included, this acknowledgment shall appear in the software itself,
 * wherever such third-party acknowledgments normally appear.
 * 3. The names "The National Cancer Institute", "NCI" and "SAIC" must not be used to endorse or promote products derived from this software.
 * 4. This license does not authorize the incorporation of this software into any third party proprietary programs. This license does not authorize
 * the recipient to use any trademarks owned by either NCI or SAIC-Frederick.
 * 5. THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES, (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED. IN NO EVENT SHALL THE NATIONAL CANCER INSTITUTE,
 * SAIC, OR THEIR AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * <!-- LICENSE_TEXT_END -->
 */

/**
 * ApplicationService presents various methods to query a HTTP server.
 * 
 * @author caBIO Team
 * @version 1.0
 */

public class ApplicationServiceBusinessImpl
{

	private static String httpAddress = null;
	private static boolean isLocal = true;
	private static ApplicationServiceBusinessImpl applicationService = null;
	private static int firstRow = 0;
	private static int maxRecordsCount = 0;
	private static int recordsCount = 0;
	private boolean inputImplFlag = false;
	private static Logger log = Logger.getLogger(ApplicationServiceBusinessImpl.class.getName());

	private boolean caseSensitivityFlag = false; // by default it is case
													// insensitive

	/**
	 * Creates a new ApplicationService instance with the HTTP server address
	 * 
	 * @param httpURL -
	 *            Specifies the http address for the server
	 * @return application service instance
	 */
	public static ApplicationServiceBusinessImpl getRemoteInstance(String httpURL)
	{
		httpAddress = httpURL;
		isLocal = false;
		loadProperties();
		applicationService = getInstance();

		return applicationService;

	}

	/**
	 * 
	 * @return application service local instance
	 */
	public static ApplicationServiceBusinessImpl getLocalInstance()
	{
		httpAddress = null;
		isLocal = true;
		loadProperties();
		applicationService = getInstance();
		return applicationService;

	}

	static
	{
		try
		{
			if (applicationService == null)
				applicationService = new ApplicationServiceBusinessImpl();

		}
		catch (Throwable ex)
		{
			ex.printStackTrace(System.out);

		}
	}

	/**
	 * @return ApplicationService instance
	 */
	public static ApplicationServiceBusinessImpl getApplicationService()
	{
		return applicationService;

	}

	/**
	 * @return ApplicationService instance
	 */
	public static ApplicationServiceBusinessImpl getInstance()
	{
		try
		{
			if (applicationService == null)
			{
				applicationService = new ApplicationServiceBusinessImpl();
			}

		}
		catch (Throwable ex)
		{
			log.error("Cannot get ApplicationService instance" + ex.getMessage());
			ex.printStackTrace(System.out);
		}
		return applicationService;
	}

	/**
	 * @param i
	 * @throws Exception 
	 */
	public void setRecordsCount(int i) throws Exception
	{
		if (i > maxRecordsCount)
		{
			log.error("Illegal Value for RecordsCount: RECORDSPERQUERY cannot be greater than MAXRECORDSPERQUERY. RECORDSPERQUERY = " + i + " MAXRECORDSPERQUERY = " + maxRecordsCount);

			throw new Exception("Illegal Value for RecordsCount: RECORDSPERQUERY cannot be greater than MAXRECORDSPERQUERY. RECORDSPERQUERY = " + i + " MAXRECORDSPERQUERY = " + maxRecordsCount);

		}
		else
			recordsCount = i;
	}

	private static void loadProperties()
	{

		try
		{
			Properties _properties = new Properties();

			_properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("CORESystem.properties"));

			String rsPerQuery = (String) _properties.getProperty("RECORDSPERQUERY");
			String maxRsPerQuery = (String) _properties.getProperty("MAXRECORDSPERQUERY");
			if (rsPerQuery != null)
			{
				recordsCount = new Integer(rsPerQuery).intValue();
			}
			else
			{
				recordsCount = Constant.RESULT_COUNT_PER_QUERY;
			}

			if (maxRsPerQuery != null)
			{
				maxRecordsCount = new Integer(maxRsPerQuery).intValue();

			}
			else
			{
				maxRecordsCount = Constant.MAX_RESULT_COUNT_PER_QUERY;
			}

		}
		catch (IOException e)
		{
			log.error("IOException: " + e.getMessage());
			System.out.println("IOException occured: " + e.getMessage());
		}
		catch (Exception ex)
		{
			log.error("Exception: " + ex.getMessage());
			System.out.println("Exception - " + ex.getMessage());
		}
	}

	/**
	 * @param criteria
	 * @return total count for the query
	 * @throws Exception
	 * 
	 */
	public int getQueryRowCount(Object criteria, String targetClassName) throws Exception
	{
		Integer count = null;
		Response response = new Response();
		Request request = new Request(criteria);
		request.setIsCount(new Boolean(true));
		request.setDomainObjectName(targetClassName);

		try
		{
			if (isLocal)
			{
				try
				{
					InterfaceProxy client = (InterfaceProxy) Class.forName(gov.nih.nci.common.util.Constant.DELEGATE_NAME).newInstance();
					response = (Response) client.query(request);
					count = (Integer) response.getRowCount();

				}
				catch (LinkageError le)
				{
					le.printStackTrace();
					log.error("LinkageError: " + le.getMessage());
					throw new Exception("Having problem in instantiating Delegate as LOCAL TYPE \n" + le.getMessage());
				}
				catch (ClassNotFoundException cnfe)
				{
					log.error("ClassNotFoundException: " + cnfe.getMessage());
					throw new Exception("*************Having problem in instantiating Delegate as LOCAL TYPE\n" + cnfe.getMessage());
				}

			}
			else
			// Remote
			{
				try
				{
					HTTPClient client = new HTTPClient(httpAddress);
					response = (Response) client.query(request);
					count = (Integer) response.getRowCount();

				}
				catch (Exception e)
				{
					log.error("Exception: " + e.getMessage());
					throw new Exception("Having problem When used as REMOTE TYPE \n" + e.getMessage());
				}
			}
		}
		catch (Exception ex)
		{
			log.error("Exception in query: " + ex.getMessage());
			System.out.println("Exception in query(): " + ex.getMessage());
		}
		if (count != null)
			return count.intValue();
		else
			return 0;
	}

	public List query(DetachedCriteria detachedCriteria, String targetClassName) throws Exception
	{
		return privateQuery((Object) detachedCriteria, targetClassName);
	}

	private List query(NestedCriteria nestedCriteria, String targetClassName) throws Exception
	{
		return privateQuery((Object) nestedCriteria, targetClassName);
	}

	public List query(HQLCriteria hqlCriteria, String targetClassName) throws Exception
	{
		return privateQuery((Object) hqlCriteria, targetClassName);
	}

	/**
	 * Gets the result list for the specified Hibernate Criteria from the
	 * HTTPClient.
	 * 
	 * @param criteria
	 *            Specified Hibernate criteria
	 * @return gets the result list
	 * @throws Exception
	 */

	// public List query(Object criteria, String targetClassName) throws
	// Exception{
	private List privateQuery(Object criteria, String targetClassName) throws Exception
	{

		List results = null;
		List resultList = new ListProxy();
		Response response = new Response();
		Request request = new Request(criteria);
		request.setIsCount(new Boolean(false));
		request.setFirstRow(new Integer(firstRow));

		// if(maxRecordsCount > Constant.MAX_RESULT_COUNT_PER_QUERY)
		// {
		// log.error("Illegal value for MAXRECORDSPERQUERY: Max Records Per
		// Query cannot be greater than the default value." +
		// " MAXRECORDSPERQUERY = " + maxRecordsCount + ", Default
		// MAXRECORDSPERQUERY = " + Constant.MAX_RESULT_COUNT_PER_QUERY);
		// throw new Exception("Illegal value for MAXRECORDSPERQUERY: Max
		// Records Per Query cannot be greater than the default value." +
		// " MAXRECORDSPERQUERY = " + maxRecordsCount + ", Default
		// MAXRECORDSPERQUERY = " + Constant.MAX_RESULT_COUNT_PER_QUERY);
		//	   
		// }
		int localRecordsCount = recordsCount;
		if (ClientInfoThreadVariable.isClientRequest())
			localRecordsCount = ClientInfoThreadVariable.getRecordsCount();
		
		if ((maxRecordsCount > 0) && (localRecordsCount > maxRecordsCount))
		{
			log.error("Illegal Value for RecordsCount: RECORDSPERQUERY cannot be greater than MAXRECORDSPERQUERY. RECORDSPERQUERY = " + localRecordsCount + " MAXRECORDSPERQUERY = " + maxRecordsCount);
			throw new Exception("Illegal Value for RecordsCount: RECORDSPERQUERY cannot be greater than MAXRECORDSPERQUERY. RECORDSPERQUERY = " + localRecordsCount + " MAXRECORDSPERQUERY = " + maxRecordsCount);
		}
		else if (localRecordsCount <= 0)
		{
			request.setRecordsCount(new Integer(Constant.RESULT_COUNT_PER_QUERY));
			recordsCount = Constant.RESULT_COUNT_PER_QUERY;
		}
		else if (localRecordsCount > 0)
		{
			request.setRecordsCount(new Integer(localRecordsCount));
		}
		request.setDomainObjectName(targetClassName);
		try
		{
			if (isLocal)
			{
				try
				{
					InterfaceProxy client = (InterfaceProxy) Class.forName(gov.nih.nci.common.util.Constant.DELEGATE_NAME).newInstance();
					// BaseDelegate client =
					// (BaseDelegate)Class.forName(gov.nih.nci.common.util.Constant.DELEGATE_NAME).newInstance();
					response = (Response) client.query(request);
					results = (List) response.getResponse();

				}
				catch (LinkageError le)
				{
					log.error("LinkageError: Having problem in instantiating Delegate as LOCAL TYPE \n" + le.getMessage());
					throw new Exception("Having problem in instantiating Delegate as LOCAL TYPE \n" + le.getMessage());
				}
				catch (ClassNotFoundException cnfe)
				{
					log.error("ClassNotFoundException: Having problem in instantiating Delegate as LOCAL TYPE\n" + cnfe.getMessage());
					throw new Exception("Having problem in instantiating Delegate as LOCAL TYPE\n" + cnfe.getMessage());
				}
			}
			else
			// Remote
			{
				try
				{
					HTTPClient client = new HTTPClient(httpAddress);
					response = (Response) client.query(request);
					results = (List) response.getResponse();

				}
				catch (Exception e)
				{
					log.error("ApplicationService.query throws exception: " + e.getMessage());
					throw new Exception(e.getMessage());
				}
			}

		}
		catch (Exception ex)
		{
			log.error("Exception \n" + ex.getMessage());
			throw new Exception("Exception " + ex.getMessage());
		}

		resultList.clear();
		// Set the value for ListProxy
		if (results != null)
		{
			// System.out.println("results not null, add to listProxy.addAll() "
			// + results.size());
			resultList.addAll(results);
		}
		ListProxy myProxy = (ListProxy) resultList;
		myProxy.setOriginalStart(firstRow);
		myProxy.setMaxRecordsPerQuery(localRecordsCount);
		myProxy.setOriginalCriteria(criteria);
		myProxy.setServerAddress(httpAddress);
		myProxy.setTargetClassName(targetClassName);

		return resultList;

	}

	/**
	 * @param criteria
	 * @param firstRow
	 * @param resultsPerQuery
	 * @param targetClassName
	 * @return List
	 * @throws Exception
	 */
	public List query(Object criteria, int firstRow, int resultsPerQuery, String targetClassName) throws Exception
	{
		// List myList = new ListProxy();
		List results = null;
		Response response = new Response();
		Request request = new Request(criteria);
		request.setIsCount(new Boolean(false));
		request.setFirstRow(new Integer(firstRow));
		/*
		 * if((resultsPerQuery != recordsCount) && (recordsCount > 0 &&
		 * recordsCount < maxRecordsCount)) { request.setRecordsCount(new
		 * Integer(recordsCount)); } else
		 *///
		if (resultsPerQuery > 0 && resultsPerQuery < maxRecordsCount)
		{
			request.setRecordsCount(new Integer(resultsPerQuery));
		}
		if ((maxRecordsCount > 0) && (resultsPerQuery > maxRecordsCount))
		{
			log.error("Illegal Value for RecordsCount: RECORDSPERQUERY cannot be greater than MAXRECORDSPERQUERY. RECORDSPERQUERY = " + resultsPerQuery + " MAXRECORDSPERQUERY = " + maxRecordsCount);
			throw new Exception("Illegal Value for RecordsCount: RECORDSPERQUERY cannot be greater than MAXRECORDSPERQUERY. RECORDSPERQUERY = " + resultsPerQuery + " MAXRECORDSPERQUERY = " + maxRecordsCount);
		}
		request.setDomainObjectName(targetClassName);
		try
		{
			if (isLocal)
			{
				try
				{
					InterfaceProxy client = (InterfaceProxy) Class.forName(gov.nih.nci.common.util.Constant.DELEGATE_NAME).newInstance();
					response = (Response) client.query(request);
					results = (List) response.getResponse();

				}
				catch (LinkageError le)
				{
					log.error("LinkageError: Having problem in instantiating Delegate as LOCAL TYPE \n" + le.getMessage());
					throw new Exception("Having problem in instantiating Delegate as LOCAL TYPE \n" + le.getMessage());
				}
				catch (ClassNotFoundException cnfe)
				{
					log.error("Having problem in instantiating Delegate as LOCAL TYPE\n" + cnfe.getMessage());
					throw new Exception("Having problem in instantiating Delegate as LOCAL TYPE\n" + cnfe.getMessage());
				}

			}
			else
			// Remote
			{
				try
				{
					HTTPClient client = new HTTPClient(httpAddress);
					response = (Response) client.query(request);
					results = (List) response.getResponse();

				}
				catch (Exception e)
				{
					log.error("ApplicationService.query throws exception: " + e.getMessage());
					throw new Exception(e.getMessage());
				}
			}
		}
		catch (Exception ex)
		{
			log.error("Exception in query(Object, int, int, String): " + ex.getMessage());
		}
		return results;
	}

	/**
	 * Returns a list of objects from the EVS datasource
	 * 
	 * @param evsCriterion
	 *            Specifies the evs criteria object
	 * @return gets a list of objects
	 * @throws Exception
	 */
	public List evsSearch(EVSQuery evsCriterion) throws Exception
	{
		List results = null;
		Response response;
		Request request = new Request(evsCriterion);
		request.setDomainObjectName(evsCriterion.getClass().getName());
		try
		{
			if (isLocal)
			{
				try
				{
					InterfaceProxy client = (InterfaceProxy) Class.forName(gov.nih.nci.common.util.Constant.DELEGATE_NAME).newInstance();
					response = (Response) client.query(request);
					results = (List) response.getResponse();

				}
				catch (LinkageError le)
				{
					log.error("LinkageError: Having problem in instantiating Delegate as LOCAL TYPE \n" + le.getMessage());
					throw new Exception("Having problem in instantiating Delegate as LOCAL TYPE \n" + le.getMessage());
				}
				catch (ClassNotFoundException cnfe)
				{
					log.error("Having problem in instantiating Delegate as LOCAL TYPE\n" + cnfe.getMessage());
					throw new Exception("Having problem in instantiating Delegate as LOCAL TYPE\n" + cnfe.getMessage());
				}
			}
			else
			// Remote
			{
				try
				{
					HTTPClient client = new HTTPClient(httpAddress);
					response = (Response) client.query(request);
					results = (List) response.getResponse();

				}
				catch (Exception e)
				{
					log.error("ApplicationService.evsSearch throws exception: " + e.getMessage());
					throw new Exception(e.getMessage());
				}
			}
		}
		catch (Exception ex)
		{
			log.error("Exception evsSearch: " + ex.getMessage());
			throw new Exception(ex.getMessage());
		}
		return results;
	}

	/**
	 * Prints a list of objects on the Standard Output Device
	 * 
	 * @param resultList -
	 *            Specifies the List of objects that needs to be printed
	 */

	public void printResults(List resultList)
	{
		if (resultList.size() < 1)
		{
			System.out.println("No records found");
		}
		else
		{
			PrintUtils printer = new PrintUtils();
			printer.printResults(resultList);
		}
	}

	public void printEVSResults(List resultList)
	{
		if (resultList.size() < 1)
		{
			System.out.println("No records found");
		}
		else
		{
			PrintUtils printer = new PrintUtils();
			printer.printEVSResults(resultList);
		}
	}

	/**
	 * Prints a list of objects in a tree like structure to the Standard Output
	 * Device
	 * 
	 * @param resultList -
	 *            Specifies the List of objects that needs to be printed
	 */
	public void printTree(List resultList)
	{
		if (resultList.size() < 1)
		{
		}
		else
		{
			PrintUtils printer = new PrintUtils();
			printer.printTree(resultList);
		}

	}

	/**
	 * Returns the class in the specified package.
	 * 
	 * @param packageName
	 *            Specifies the package name for the class
	 * @param className
	 *            Specifies the class name
	 * @return Returns a class
	 * @throws Exception
	 *             Throws ClassNotFoundException
	 */

	private Collection getAssociation(Object criterionClassObj, String searchClassName) throws Exception
	{
		// Use reflection to find the method in critionClassObject and then get
		// the result
		Class objKlass = criterionClassObj.getClass();
		// Method[] objMethods = objKlass.getDeclaredMethods();
		Method[] objMethods = objKlass.getMethods();

		String searchBeanName = searchClassName.substring(searchClassName.lastIndexOf(".") + 1, searchClassName.indexOf("Impl"));
		for (int i = 0; i < objMethods.length; i++)
		{
			String methodName = objMethods[i].getName();

			// if (methodName.indexOf(searchBeanName) != -1)
			String associationName = methodName.substring(3);
			if (associationName.equals(searchBeanName) || associationName.equals(searchBeanName + "Collection"))
			{
				// if the methodName matches the searchBeanName, the method
				// definitely return Collection or Object type
				Class returnType = objMethods[i].getReturnType();
				Object returnObject = objMethods[i].invoke(criterionClassObj, new Object[] {});
				if (returnObject == null)
				{
					return null;
				}
				else
				{
					if (returnObject instanceof java.util.Collection)
					{
						return (java.util.Collection) returnObject;
					}
					else
					{
						java.util.Collection result = new HashSet();
						result.add(returnObject);
						return result;
					}
				}

			}
		}
		return null;
	}

	/**
	 * ************************************************** HQL Search
	 * ************************************************
	 */

	public List search(Class targetClass, Object obj) throws Exception
	{
		return search(targetClass.getName(), obj);
	}

	public List search(Class targetClass, List objList) throws Exception
	{
		return search(targetClass.getName(), objList);
	}

	public List search(String path, Object obj) throws Exception
	{
		// check if it is a nested query
		List pathList = new ArrayList();
		// parse path -> arraylist
		StringTokenizer tokens = new StringTokenizer(path, ",");
		// Check if the target name is with Impl, and set the flag then add the
		// target name in the pathlist
		if (tokens.hasMoreTokens())
		{
			String target = tokens.nextToken();
			if (target.indexOf(".impl.") > 0)
			{
				inputImplFlag = true;
			}
			else
			{
				inputImplFlag = false;
			}
			pathList.add(convertPathName(target.trim()));
		}
		// add the rest of the path in the pathlist if there is any
		while (tokens.hasMoreTokens())
		{
			// pathList.add(tokens.nextToken().trim());
			pathList.add(convertPathName(tokens.nextToken().trim()));
		}
		NestedCriteria crit = createNestedCriteria(pathList, obj);
		List results = query(crit, crit.getTargetObjectName());
		log.debug("ApplicationService.search(): inputImplFlag = " + inputImplFlag);
		if (inputImplFlag)
		{
			return convertToImpl(results);
		}
		return results;
	}

	public List search(String path, List objList) throws Exception
	{
		// check if it is a nested query
		List pathList = new ArrayList();
		// parse path -> arraylist
		StringTokenizer tokens = new StringTokenizer(path, ",");
		// Check if the target name is with Impl, and set the flag then add the
		// target name in the pathlist
		if (tokens.hasMoreTokens())
		{
			String target = tokens.nextToken();
			if (target.indexOf(".impl.") > 0)
			{
				inputImplFlag = true;
			}
			else
			{
				inputImplFlag = false;
			}
			pathList.add(convertPathName(target.trim()));
		}
		// add the rest of the path in the pathlist if there is any
		while (tokens.hasMoreTokens())
		{
			// pathList.add(tokens.nextToken());
			pathList.add(convertPathName(tokens.nextToken().trim()));
		}
		NestedCriteria crit = createNestedCriteria(pathList, objList);
		List results = query(crit, crit.getTargetObjectName());
		if (inputImplFlag)
		{
			return convertToImpl(results);
		}
		return results;
	}

	private NestedCriteria createNestedCriteria(List pathList, Object obj) throws Exception
	{
		List objList = new ArrayList();
		objList.add(obj);
		return createNestedCriteria(pathList, objList);
	}

	private NestedCriteria createNestedCriteria(List pathList, List objList) throws Exception
	{
		SearchUtils searchUtil = new SearchUtils();
		String target, source;

		List newObjList = new ArrayList();
		log.debug("ApplicationService.createNestedCriteria(): objList class name = " + objList.get(0).getClass().getName());
		if ((objList.get(0)).getClass().getName().indexOf(".impl.") > 0)
		{
			for (Iterator iter = objList.iterator(); iter.hasNext();)
			{
				Object obj = iter.next();
				newObjList.add(convertImpl(obj));
			}
		}
		else
		{
			newObjList = objList;
		}

		String sourceName = (newObjList.get(0)).getClass().getName();
		// String sourceName =
		// getFullQName((objList.get(0)).getClass().getName());
		String targetName = "";

		NestedCriteria criteria = null;
		NestedCriteria internalCriteria = null;
		for (int i = pathList.size() - 1; i >= 0; i--)
		{
			// targetName = getFullQName((String)pathList.get(i));
			targetName = (String) pathList.get(i);
			log.debug("ApplicationService.createNestedCriteria(): new targetName = " + targetName);
			// if the target and the source are the same class, ignore the
			// association
			criteria = new NestedCriteria();
			criteria.setSourceObjectName(sourceName);
			criteria.setTargetObjectName(targetName);
			log.debug("ApplicationService.createNestedCriteria(): sourceName = " + sourceName + " | targetName = " + targetName);
			if (!targetName.equals(sourceName) && !noInheritent(sourceName, targetName))
			{
				String roleName = searchUtil.getRoleName(Class.forName(sourceName), Class.forName(targetName).newInstance());
				if (roleName == null)
				{
					log.error("No association found from " + sourceName + " to " + targetName + ", please double check your query path.");
					throw new Exception("No association found from " + sourceName + " to " + targetName + ", please double check your query path.");
				}
				criteria.setRoleName(roleName);
			}
			// if the obj is the same type of source(that means it the first
			// criterion), add Map, otherwise, skip
			// if (sourceName.equals((objList.get(0)).getClass().getName()))
			if (sourceName.equals((newObjList.get(0)).getClass().getName()))
			{
				// criteria.setSourceObject(obj);
				criteria.setSourceObjectList(newObjList);
				criteria.setInternalNestedCriteria(internalCriteria);
			}
			else
			// it is not the
			{
				criteria.setInternalNestedCriteria(internalCriteria);
			}
			internalCriteria = criteria;
			sourceName = targetName;
		}
		if (criteria != null)
		{
			if (ClientInfoThreadVariable.isClientRequest())
				criteria.setSearchCaseSensitivity(ClientInfoThreadVariable.getSearchCaseSensitivity());
			else
				criteria.setSearchCaseSensitivity(caseSensitivityFlag);
		}
		return criteria;
	}

	// Assume the passing name is either Interface or Impl class's name
	public static String getFullQName(String name) throws Exception
	{
		try
		{
			Class.forName(name);
		}
		catch (ClassNotFoundException e)
		{
			log.error("ERROR: Class " + name + " does not exist.  Please check the package and class name.");
			throw new Exception("ERROR: Class " + name + " does not exist.  Please check the package and class name.");
		}

		// assume it is already a full qualified name if the name contains
		// ".impl." and "Impl"
		if ((name.indexOf(".impl.") > 0) && (name.indexOf("Impl") > 0))
		{
			return name;
		}
		else
		{
			String full = name.substring(0, name.lastIndexOf(".")) + ".impl." + name.substring(name.lastIndexOf(".") + 1) + "Impl";
			return full;
		}
	}

	public void setSearchCaseSensitivity(boolean caseSensitivity)
	{
		this.caseSensitivityFlag = caseSensitivity;
	}

	private boolean noInheritent(String sourceName, String targetName)
	{
		try
		{
			if (Class.forName(sourceName).getSuperclass().getName().equals(targetName) || Class.forName(targetName).getSuperclass().getName().equals(sourceName))
				return true;
			return false;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	private Object convertImpl(Object implObject)
	{
		try
		{
			Class objKlass = implObject.getClass();

			log.debug("ApplicationService.copyValue: objKlass.getName = " + objKlass.getName());
			Class newObjClass = objKlass.getSuperclass();
			log.debug("ApplicationService.copyValue(): new object name = " + newObjClass.getName());

			Object newObject = newObjClass.newInstance();

			// the concrete class 's superclass -- noimpl class
			copyValue(newObject, implObject, objKlass.getSuperclass());
			return newObject;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}

	}

	private List convertImpl(List objList)
	{
		List newList = new ArrayList();
		for (Iterator iter = objList.iterator(); iter.hasNext();)
		{
			newList.add(convertImpl(iter.next()));
		}
		return newList;
	}

	private Object copyValue(Object newObject, Object obj, Class objKlass)
	{
		try
		{
			Field[] newObjFields = objKlass.getDeclaredFields();
			for (int i = 0; i < newObjFields.length; i++)
			{
				Object fieldValue = null;
				Object newFieldValue = null;
				Field field = newObjFields[i];
				field.setAccessible(true);
				String fieldName = field.getName();
				if (fieldName.equals("serialVersionUID"))
					continue;

				String getterMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				String setterMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				// orig's getter method
				Method getterMethod = objKlass.getMethod(getterMethodName);
				// new object setter method
				Method setterMethod = newObject.getClass().getMethod(setterMethodName, new Class[] { getterMethod.getReturnType() });

//				if (field.getType().getName().indexOf("gov.nih.nci") > -1)
				if (!field.getType().isPrimitive() && !field.getType().getName().startsWith("java."))
				{
					fieldValue = field.get(obj);
					if (fieldValue != null)
					{
						if (fieldValue.getClass().getName().indexOf(".impl.") > -1)
						{
							fieldValue = convertImpl(fieldValue);
						}
					}
				}
				else
				{
					fieldValue = getterMethod.invoke(obj);
					if (fieldValue instanceof Collection)
					{
						Collection oldValue = (Collection) fieldValue;
						Collection newValue = new ArrayList();
						for (Iterator iter = oldValue.iterator(); iter.hasNext();)
						{
							Object element = iter.next();
							if (obj.getClass().getName().indexOf(".impl.") > -1)
							{
								newValue.add(convertImpl(element));
							}
							else
							{
								newValue.add(element);
							}
						}
						fieldValue = newValue;
					}
				}
				// System.out.println("ApplicationService.copyValue(): field
				// name = " + fieldName + " | fieldValue = " + fieldValue);

				setterMethod.invoke(newObject, new Object[] { fieldValue });
			}
			// the superclass
			objKlass = objKlass.getSuperclass();
			while (objKlass != null && !objKlass.equals(Object.class) && !objKlass.isInterface())
			{
				copyValue(newObject, obj, objKlass);
				objKlass = objKlass.getSuperclass();
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
			log.error("ApplicationService.copyValue: ApplicationService Error" + e.getMessage());
			return null;
		}
		return newObject;
	}

	private List convertToImpl(List results)
	{
		List newList = new ArrayList();
		for (Iterator iter = results.iterator(); iter.hasNext();)
		{
			Object resultObj = iter.next();
			try
			{
				Class objKlass = resultObj.getClass();

				log.debug("ApplicationService.copyValue: objKlass.getName = " + objKlass.getName());
				String resultObjName = objKlass.getName();
				log.debug("ApplicationService.convertToImpl(): resultObjName = " + resultObjName);
				String newImplObjName = resultObjName.substring(0, resultObjName.lastIndexOf(".")) + ".impl." + resultObjName.substring(resultObjName.lastIndexOf(".") + 1) + "Impl";
				Class newObjClass = Class.forName(newImplObjName);
				log.debug("ApplicationService.copyValue(): new object name = " + newObjClass.getName());

				Object newObject = newObjClass.newInstance();

				// the concrete class 's superclass -- noimpl class
				copyValueToImpl(newObject, resultObj, objKlass);
				newList.add(newObject);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return newList;
	}

	private Object copyValueToImpl(Object newObject, Object obj, Class objKlass)
	{
		try
		{
			Field[] newObjFields = objKlass.getDeclaredFields();
			for (int i = 0; i < newObjFields.length; i++)
			{
				Object fieldValue = null;
				Object newFieldValue = null;
				Field field = newObjFields[i];
				field.setAccessible(true);
				String fieldName = field.getName();
				if (fieldName.equals("serialVersionUID"))
					continue;

				String getterMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				String setterMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				// orig's getter method
				Method getterMethod = objKlass.getMethod(getterMethodName);
				// new object setter method
				Method setterMethod = newObject.getClass().getMethod(setterMethodName, new Class[] { getterMethod.getReturnType() });

				fieldValue = field.get(obj);

				setterMethod.invoke(newObject, new Object[] { fieldValue });
			}
			// the superclass
			objKlass = objKlass.getSuperclass();
			while (objKlass != null && !objKlass.equals(Object.class) && !objKlass.isInterface())
			{
				copyValue(newObject, obj, objKlass);
				objKlass = objKlass.getSuperclass();
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
			log.error("ApplicationService.copyValue: ApplicationService Error" + e.getMessage());
			return null;
		}
		return newObject;
	}

	private String convertPathName(String oldName)
	{
		log.debug("ApplicationService.convertPathName(): oldName = " + oldName);
		String temp = oldName.replace(".impl.", ".");
		if (temp.endsWith("Impl"))
		{
			temp = temp.substring(0, temp.length() - 4);
		}
		log.debug("ApplicationService.convertPathName(): temp = " + temp);
		return temp;

	}

	/**
	 * ***************************** End of HQL Query
	 * **********************************
	 */
}

// $Log: not supported by cvs2svn $
// Revision 1.4  2006/02/06 21:18:42  modik
// Modified Code to
// 1. Read the client side CORESystem.Properties File
// 2. Able to set the values from client to server for Records Count and Case Sensitivity
//
// Revision 1.3  2006/01/11 21:23:13  zengje
// Changed the hard code searching for "gov.nih.nci" to eliminate the primitive type and type starts with "java."
//
// Revision 1.2  2005/12/30 20:47:07  modik
// Commiting after merge
//
// Revision 1.48 2005/12/13 22:11:46 zengje
// Added the convertToImpl() method to avoid possible classcasting error for
// current user.
//
// Revision 1.47 2005/12/13 14:35:28 masondo
// Fixed the ApplicationService intial block. The code was not initializing the
// ApplicationService only in the local block. The reason it worked was the code
// is always executed in the static block
//
// Revision 1.46 2005/12/12 20:32:12 zengje
// 1. Changed query(NestedCriteria, String) to private.
// 2. Remove the hard code upper limit for max record per query
// 3. Convert target name and path to non-impl
// 4. Convert criterion objects to non-impl.
//
// Use non-impl object to search for public search method.
//
// If user used DetachedCriteria and Impl class, it will use the Impl set to do
// the query.
//
// Revision 1.45 2005/11/14 22:11:23 zengje
// Added pulic methods query(DetachedCriteria, String); query(NestedCriteria,
// String), and query(HQLCriteria, String). Changed query(Object, String) to
// private method.
//
// Revision 1.44 2005/10/26 17:15:31 zengje
// Added interface setSearchCaseSensitivity(boolean)
//
// Revision 1.43 2005/10/25 19:47:51 muhsins
// Modified code to compile with jdk 1.5_04
//
// Revision 1.42 2005/07/15 21:05:32 zengje
// Modified the logging/Exception message
//
// Revision 1.41 2005/07/12 17:29:57 lethai
// added logging messages
//
// Revision 1.40 2005/07/08 20:26:38 zengje
// removed unused import statment. Switch ServerSearchutil to SearchUtil
//
// Revision 1.39 2005/07/08 19:46:31 lethai
// modified setRecordsCount to throw exception when the value is greater than
// 5000.
//
// Revision 1.38 2005/07/08 18:55:35 zengje
// Changed hqlSearch to search, and removed the DetachedCriteria stuff.
//
// Revision 1.37 2005/07/07 18:33:15 lethai
// Modified query method to fix bug# 802 (ListProxy.subList)
//
// Revision 1.36 2005/07/05 18:48:52 lethai
// added code to check for maxrecordsperquery cannot be more than 5000
//
// Revision 1.35 2005/06/29 14:23:05 lethai
// added fix for nullpointerexception on rowcount value.
//
// Revision 1.34 2005/06/28 18:00:18 lethai
// comment out debug message
//
// Revision 1.33 2005/06/23 18:23:43 lethai
// removed codes that checks and removes duplicate records. Do not need it
// anymore because Server side code takes care of duplicate records by filtering
// uisng Set.
//
// Revision 1.32 2005/06/17 13:17:45 zengje
// removed some comments
//
// Revision 1.31 2005/06/09 15:46:22 zengje
// Modified to work with List of objects.
//
// Revision 1.30 2005/06/02 15:09:00 zengje
// Added hqlSearch(String, Object) for Nested Criteria/HQL search.
// Replaced DetachedCriteria in getRowCount() with Object.
// There are a lot of debug statement in this version, which will be removed
// later.
//
// Revision 1.29 2005/05/31 18:20:35 lethai
// Removed setHasAllRecords for ListProxy.
//
// Revision 1.28 2005/05/26 15:44:26 muhsins
// Criteria have been changed to Hibernate3 DetachedCriteria
//
// Revision 1.27 2005/05/25 16:44:52 lethai
// Modified to solve large result set problem, bug # 740, 739
//
// Revision 1.25 2005/05/06 21:33:31 shanbhak
// Replaced InterfaceProxy with BaseDelegate class for Local instance to avoid
// classCastException.
//
package gov.nih.nci.system.webservice;

import gov.nih.nci.common.net.Request;
import gov.nih.nci.common.net.Response;
import gov.nih.nci.common.util.NestedCriteria;
import gov.nih.nci.common.util.SearchUtils;
import gov.nih.nci.system.delegator.BaseDelegate;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.*;


/**
 * class to list headers sent in request as a string array
 */
public class WSApplicationService {

	private static Logger log = Logger.getLogger(WSApplicationService.class);
    private static String httpAddress = null;
    private static boolean isLocal = true;
    private static WSApplicationService wsApplicationService = null;
    private int firstRow=0;
    private int recordsCount=0;

	static {
		try {
			if(wsApplicationService==null)
				wsApplicationService = new WSApplicationService();
	   	} catch (Throwable ex) {
	   		log.error(ex.getMessage());
	    	ex.printStackTrace(System.out);
    	}
	}

	public static WSApplicationService getInstance()
	{
		try {
			if(wsApplicationService==null)
			{
				WSApplicationService wsApplicationService = new WSApplicationService();
			}
    	} catch (Throwable ex) {
    		log.error(ex.getMessage());
			ex.printStackTrace(System.out);
		}
		return wsApplicationService;
	}



/*********************************** public Methods starts here ***************************************************************/
	    
	
//	public List query(DetachedCriteria criteria, String targetObjectName) throws Exception{
	private List query(Object criteria, String targetObjectName) throws Exception{	    	//System.out.println("WSApplicationService.query...............");
	    	//Set Max record limit.
	    	//criteria.setMaxResults(1000);
	    	List results = null;
	    	Request request = new Request(criteria);
	    	if(recordsCount > 0)
	    	{
	    	    request.setFirstRow(new Integer(firstRow));
	    	    request.setRecordsCount(new Integer(recordsCount));
	    	}
	    	request.setDomainObjectName(targetObjectName);
 	  	    try
	    	{
 	  	    	BaseDelegate delegate = new BaseDelegate(); 	  	    	
	    		results = (List)((Response)delegate.query(request)).getResponse();
	    	}catch(Exception ex)
			{
	    		log.error("WSAppService.query caught an exception......");
	    		//System.out.println("WSAppService.query caught an exception......");
	    		ex.printStackTrace();
	    		throw ex;
	    	}
	    	return results;
	    }
	    
	   
    	// return a fully qualified Impl name
//    	public String getFullyQualifiedObjectName(String targetName)
//    	{
//    		if (targetName.indexOf("impl") < 0)
//    		{
//    			String packageName = targetName.substring(0, targetName.lastIndexOf("."))+".impl.";
//    			String beanName = targetName.substring(targetName.lastIndexOf(".")+1)+"Impl";
//    			String newName = packageName + beanName;
//    			return newName;
//    		}
//    		else
//    		{
//    			return targetName;
//    		}
//    	}

    	public List resetAssociations(List results)
    	{
    		if (results == null)
    		{
    			//System.out.println("WebAppService.resetAssociation: input list is null ... ...");
    			return null;
    		}
    		for (int i = 0; i<results.size(); i++)
    		{
    			Object obj = results.get(i);
    			Class objKlass = obj.getClass();
    			// the concrete class itself
    			resetAssociation(obj, objKlass);
    			// the superclass
    			objKlass = objKlass.getSuperclass();
    			while (objKlass != null && !objKlass.equals(Object.class) && !objKlass.isInterface())
    			{
        			resetAssociation(obj, objKlass);
        			objKlass = objKlass.getSuperclass();
    			}

    		}
    		return results;
    	}

    	private void resetAssociation(Object obj, Class objKlass)
    	{
			Method[] objMethods = objKlass.getDeclaredMethods();
			for (int index=0;index<objMethods.length; index++)
			{
				String methodName = objMethods[index].getName();
				if (methodName.startsWith("set") && methodName.endsWith("Collection"))
				{
					Object[] inputParams = new Object[]{new ArrayList()};
					try{
    					objMethods[index].invoke(obj,inputParams);
					}
					catch(Exception e)
					{
						log.error(e.getMessage());
						e.printStackTrace();
					}
				}
				else if (methodName.startsWith("set"))
				{
					// Assume there is only one input param for setter
					Class[] inputType = objMethods[index].getParameterTypes();
//					if (inputType[0].getName().indexOf("gov.nih.nci") > -1)
					if (!inputType[0].isPrimitive() && !inputType[0].getName().startsWith("java."))
					{
						try{
						objMethods[index].invoke(obj,new Object[]{null});
						}
						catch(Exception e)
						{
							log.error(e.getMessage());
							e.printStackTrace();
						}						
					}										
				}
			}

    	}




    	private Collection getAssociation(Object criterionClassObj, String searchClassName) throws Exception
		{
			// Use reflection to find the method in critionClassObject and then get the result
			Class objKlass = criterionClassObj.getClass();
		//	System.out.println("J.Z. AppService.getAssociation: objKlass.getName() = " + objKlass.getName());
			Method[] objMethods = objKlass.getDeclaredMethods();

//			String searchBeanName = searchClassName.substring(searchClassName.lastIndexOf(".")+1, searchClassName.indexOf("Impl"));
			String searchBeanName = searchClassName.substring(searchClassName.lastIndexOf(".")+1);
			for(int i=0; i<objMethods.length; i++)
			{
				String methodName = objMethods[i].getName();
				String associationName = methodName.substring(3);
				if (associationName.equals(searchBeanName) || associationName.equals(searchBeanName + "Collection"))
				{
					//if the methodName matches the searchBeanName, the method definitely return Collection or Object type
					Class returnType = objMethods[i].getReturnType();
					Object returnObject = objMethods[i].invoke(criterionClassObj, new Object[]{null});
					//System.out.println("J.Z. AppService.getAssociation: returnObject class name = " + returnObject.getClass().getName());
					if (returnObject == null)
					{
						return null;
					}
					else
					{
						if (returnObject instanceof java.util.Collection)
						{
							return (java.util.Collection)returnObject;
						}
						else
						{
							//System.out.println("J.Z. AppService.getAssociation: return object is a object ....");
							java.util.Collection result = new HashSet();
							result.add(returnObject);
							return result;
						}
					}
				}
			}
			return null;
		}

//    	public List search(Class targetClass, Object domainObject) throws Exception
//		{
//	     	DetachedCriteria criteria = null;
//			List results = new ArrayList();
//			SearchUtils searchUtils = new SearchUtils();
//
//			try{
//				switch(searchUtils.getDirection(targetClass, domainObject))
//				{
//					case Constant.BIDIRECTIONAL:
//						//System.out.println("WSApplicationService it is BI-directional ....");
//						criteria = searchUtils.buildCriteria(targetClass, domainObject);
//						break;
//					case Constant.UNIDIRECTIONAL:
//						// get criterion object(s)
//						List intrimList = query(searchUtils.buildCriteria(domainObject.getClass(), domainObject, targetClass), domainObject.getClass().getName());
//					//System.out.println("WSApplicationService.search(Class, object): uni-directional intrimList size = " + intrimList.size());
//					    // dupCheckVec contains non-duplicate object returned from first query
//						Vector dupCheckVec = new Vector();
//						for (int i = 0; i<intrimList.size(); i++)
//						{
//							// Check if the intrimList.get(i) has duplicate id
//							Object object = intrimList.get(i);
//							if (!dupCheckVec.contains(object))
//							{
//								// get association from criterion object(s)
//								Collection intrimData = getAssociation(object, targetClass.getName());
//								if ((intrimData != null) && (intrimData.size() > 0))
//								{
//									results.addAll(intrimData);
//								}
//								dupCheckVec.add(object);
//							}
//						}
//						return results;
//					case Constant.NO_ASSOCIATION:
//						throw new Exception("No direct association found between " + targetClass.getName() + " & "+ domainObject.getClass().getName());
//				}
//				results = query(criteria, targetClass.getName());
//			}catch(Exception e)
//			{
//	    		System.out.println("WSAppService.search caught an exception......");
//				e.printStackTrace();
//				throw e;
//			}
//			return results;
//		}
//
//    	public List search(String targetName, List doCriterionList) throws Exception
//		{
//			List searchList = new ArrayList();
//			List results = new ArrayList();
//			try
//			{
//				StringTokenizer st = new StringTokenizer(targetName,",");
//				int itemCount = st.countTokens();
//				if (itemCount <= 1)
//				{
//					Class targetClass = Class.forName(getFullyQualifiedObjectName(targetName));
//	 				results = search(targetClass, doCriterionList);
//				}
//				else
//				{
//					// Create a list, call search(List, object)
//					for(int i= 0; i< itemCount; i++)
//					{
//					    String className = st.nextToken();
//					    Class searchClass = Class.forName(getFullyQualifiedObjectName(className));
//						searchList.add(searchClass);
//					}
//					results = search(searchList, doCriterionList);
//				}
//			}
//			catch (Exception e)
//			{
//	    		System.out.println("WSAppService.search caught an exception......");
//				e.printStackTrace();
//				throw e;
//			}
//			return results;
//		}
//
//		public List search(Class targetClass, List doCriterionList) throws Exception
//		{
//	     	DetachedCriteria criteria = null;
//			List results = new ArrayList();
//			List searchList = new ArrayList();
//			List criterionList = new ArrayList();
//			SearchUtils searchUtils = new SearchUtils();
//
//			// Get Criterion class type
//			Class criterionClass = ((Object)doCriterionList.get(0)).getClass();
//
//			switch(searchUtils.getDirection(targetClass, criterionClass.newInstance()))
//			{
//				case Constant.BIDIRECTIONAL:
//					criteria = searchUtils.buildCriteria(targetClass, doCriterionList);
//					break;
//				case Constant.UNIDIRECTIONAL:
//					// get criterion object(s)
//					List intrimList = query(searchUtils.buildCriteria(criterionClass, doCriterionList, targetClass), criterionClass.getName());
//					// Somewhere need to set eager fetch for searchClass role
//					// get association from criterion object(s)
//					for (int i = 0; i<intrimList.size(); i++)
//					{
//						Collection intrimData = getAssociation(intrimList.get(i), targetClass.getName());
//						if ((intrimData != null) && (intrimData.size() > 0))
//						{
//							results.addAll(intrimData);
//						}
//					}
//					return results;
//				case Constant.NO_ASSOCIATION:
//					throw new Exception("No direct association found between " + targetClass.getName() + " & "+ criterionClass.getName());
//			}
//			results = query(criteria, targetClass.getName());
//
//			return results;
//		}
//
//		// a list of class in the opposite order of searching path
//		public List search(List searchPathList, Object criterion) throws Exception
//		{
//			int size = searchPathList.size();
//			List intrimResults = search((Class)searchPathList.get(size-1), criterion);
//			for(int i=size-2; i>=0; i--)
//			{
//				if (intrimResults != null)
//				{
//					intrimResults = search((Class)searchPathList.get(i), intrimResults);
//				}
//
//			}
//			return intrimResults;
//		}
//
//		// a list of Class in the opposite order of searching path
//		public List search(List searchPathList, List doCriterionList) throws Exception
//		{
//			int size = searchPathList.size();
//			List intrimResults = doCriterionList;
//			for(int i=size-1; i>=0; i--)
//			{
//				intrimResults = search((Class)searchPathList.get(i), intrimResults);
//			}
//			return intrimResults;
//		}
			
		public void setFirstRow(int firstResult)
		{
		    firstRow = firstResult;
		}
		
		public void setRecordsCount(int rc)
		{
		    recordsCount = rc;
		}
		
/*********************************** HQL Search *******************************/
		
		public List search(String path, Object obj) throws Exception
		{
//			System.out.println("WSAppService.search:.......");		
			// check if it is a nested query
			List pathList = new ArrayList();
			// parse path -> arraylist
			StringTokenizer tokens = new StringTokenizer(path, ",");
			while(tokens.hasMoreTokens())
			{
				pathList.add(tokens.nextToken().trim());
			}
			NestedCriteria crit = createNestedCriteria(pathList, obj);
			List results = query(crit,crit.getTargetObjectName());
			return results;
		}
		
		public List search(String path, List objList) throws Exception
		{
//			System.out.println("WSAppService.search(String ,list):.......");		
			// check if it is a nested query
			List pathList = new ArrayList();
			// parse path -> arraylist
			StringTokenizer tokens = new StringTokenizer(path, ",");
			while(tokens.hasMoreTokens())
			{
				pathList.add(tokens.nextToken());
			}
			NestedCriteria crit = createNestedCriteria(pathList, objList);
			List results = query(crit,crit.getTargetObjectName());
			return results;
		}
		
		private NestedCriteria createNestedCriteria(List pathList, Object obj)
			throws Exception
		{
			List objList = new ArrayList();
			objList.add(obj);
			return createNestedCriteria(pathList, objList);
		}
		
		
		private NestedCriteria createNestedCriteria(List pathList, List objList)
			throws Exception
		{
			SearchUtils searchUtil = new SearchUtils();
			String target, source;
//			String sourceName = obj.getClass().getName();
			String sourceName = (objList.get(0)).getClass().getName();
			String targetName = "";

			NestedCriteria criteria = null;
			NestedCriteria internalCriteria = null;
			for (int i=pathList.size()-1; i>=0; i--)
			{
//				targetName = getFullQName((String)pathList.get(i));
				targetName = (String)pathList.get(i);
				// if the target and the source are the same class, ignore the association
				criteria = new NestedCriteria();
				criteria.setSourceObjectName(sourceName);
				criteria.setTargetObjectName(targetName);
				if (!targetName.equals(sourceName))
				{
					String roleName = searchUtil.getRoleName(Class.forName(sourceName), Class.forName(targetName).newInstance());
					if (roleName == null)
					{
						log.error("No association found from " + sourceName + " to " + targetName + ", please double check your query path.");
						throw new Exception("No association found from " + sourceName + " to " + targetName + ", please double check your query path.");
					}
					criteria.setRoleName(roleName);				
				}
				// if the obj is the same type of source(that means it the first criterion), add Map, otherwise, skip
				if (sourceName.equals((objList.get(0)).getClass().getName()))
				{
					criteria.setSourceObjectList(objList);
					criteria.setInternalNestedCriteria(internalCriteria);
				}
				else // it is not the 
				{
					criteria.setInternalNestedCriteria(internalCriteria);
				}
				internalCriteria = criteria;
				sourceName = targetName;
			}
			return criteria;
		}
}
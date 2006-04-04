package gov.nih.nci.system.webservice;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;



/**
 * class to list headers sent in request as a string array
 */
public class WSQuery {
	
	private static Logger log = Logger.getLogger(WSQuery.class);
	
	// the flag used to store if input object is Impl object or not.  Will be removed for 4.0 version.
	private boolean inputImplFlag = false;

		public String getVersion() throws Exception
		{
			try
			{
				return (String)"caCORE 3.1";
			}
			catch(Exception e)
			{
				log.error(e.getMessage());
				e.printStackTrace();
				throw e;
			}
		}  // getVersion()

    	private List copyValue(List results) throws Exception
    	{
    		ArrayList alteredResults = new ArrayList();
    		if (results == null)
    		{
    			//System.out.println("WebAppService.resetAssociation: input list isnull ... ...");
    			return null;
    		}
    		for (int i = 0; i<results.size(); i++)
    		{
    			Object obj = results.get(i);
    			Class objKlass = obj.getClass();

        		log.debug( "WSQuery.copyValue: objKlass.getName = " + objKlass.getName());
        		String objFullName = objKlass.getName();
//        		String newObjectName = objFullName.replaceAll(".impl.", ".ws.");
        		int index = objFullName.lastIndexOf(".");
        		
        		String newObjectName = objFullName.substring(0,index)+ ".ws." + objFullName.substring(index+1);
        		if (inputImplFlag)
        		{
        			newObjectName = newObjectName + "Impl";
        		}
        		
        		log.debug("WSQuery.copyValue(): new object name = " + newObjectName);

        		Class newObjClass = Class.forName(newObjectName);
        		Object newObject= newObjClass.newInstance();

    			// the concrete class itself
    			alteredResults.add(copyValue(newObject, obj, objKlass));
    		}
    		return alteredResults;
    	}

    	// newObject  -- the target .ws.* object
    	// obj        -- the original .impl.* object
    	// objKlass   -- impl object class
    	private Object copyValue(Object newObject, Object obj, Class objKlass)
    	{
    		try {
    		Field[] newObjFields = objKlass.getDeclaredFields();
    		for(int i = 0; i<newObjFields.length;i++)
    		{
    			Field field = newObjFields[i];
    			field.setAccessible(true);
    			String fieldName = field.getName();
    			if (fieldName.equals("serialVersionUID")) continue;

//				if (field.getType().getName().indexOf("gov.nih.nci") > -1) continue;
    			if (field.getType().isPrimitive() || field.getType().getName().startsWith("java."))
    			{
        			String getterMethodName = "get"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
        			String setterMethodName = "set"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
        			log.debug("WSQuery.copyValue:  methodName = " + getterMethodName);
        			Method getterMethod = objKlass.getMethod(getterMethodName);
        			Object value = getterMethod.invoke(obj);
        			Method setterMethod = newObject.getClass().getMethod(setterMethodName, new Class[]{getterMethod.getReturnType()});
        			setterMethod.invoke(newObject, new Object[]{value});    				
    			}
    		}
				// the superclass
				objKlass = objKlass.getSuperclass();
				while (objKlass != null && !objKlass.equals(Object.class) && !objKlass.isInterface())
				{
	    			copyValue(newObject, obj, objKlass);
	    			objKlass = objKlass.getSuperclass();
				}

    		}catch (Exception e) {
    			e.printStackTrace();
    			log.error("WSQuery.copyValue: WS Error"+ e.getMessage());
    			//System.out.println("WSQuery.copyValue: WS Error"+ e);
    			return null;
    		}
    		return newObject;
    	}


    	private Object copyValueToImpl(Object obj)
    	{
    		Class objKlass;
			Object newObject;
    		try {
	    		objKlass = obj.getClass();
	    		log.debug( "WSQuery.copyValue: objKlass.getName = " + objKlass.getName());
	    		String objFullName = objKlass.getName();
	    		String newObjectName = objFullName.replaceAll(".ws.", ".");
	    		if (newObjectName.endsWith("Impl"))
	    		{
	    			newObjectName = newObjectName.substring(0, newObjectName.length()-4);
	    		}
	    		Class newObjClass = Class.forName(newObjectName); 
	    		newObject= newObjClass.newInstance();

	    		log.debug("copying properties from ws object to impl object......");
	    		PropertyUtils.copyProperties(newObject, obj);
    		}catch (Exception e) {
    			log.error("WS Error"+ e.getMessage());
    			//System.out.println("WS Error"+ e);
    			return null;
    		}
    		return newObject;
    	}

/************************************* HQL Query ***************************************/
    	/**
    	 * @param orgTargetObjectName  
    	 * 			It can be a fully qualified object name or a navigation path contains all fully qualified object name
    	 * 			The first object is the Target Object name
    	 * @param orgCriteriaObj
    	 * 			The domain object which contains the search criteria for the target object
    	 * @param firstResult
    	 * 			The starting row number
    	 * @param maxResultsCount
    	 * 			The maximun number of records
    	 * 
    	 * @return A list of the target objects whose row number starts from firstReuslut, and total of maxResultsCount records
    	 * 
    	 */
        public List query(String orgTargetObjectName, Object orgCriteriaObj, int firstResult, int maxResultsCount) throws Exception
        
        {
            
            List results = new ArrayList();
            List alteredResults = new ArrayList();
            if(orgCriteriaObj.getClass().getPackage().getName().indexOf(".nci.evs.")>0){
                EVSWebService service = new EVSWebService();
                alteredResults = service.evsSearch(orgTargetObjectName, orgCriteriaObj, firstResult, maxResultsCount);                                             
            }
            else{
                String targetObjectName = orgTargetObjectName.replaceAll(".ws.",".");
                if (targetObjectName.endsWith("Impl"))
                {
                    targetObjectName = targetObjectName.substring(0, targetObjectName.length()-4);
                    inputImplFlag = true;
                }
                else
                {
                    inputImplFlag = false;
                }
                Object criteriaObj = copyValueToImpl(orgCriteriaObj);
                WSApplicationService appService = WSApplicationService.getInstance();

//              Class targetClass = null;
//              List searchList = new ArrayList();
                            
                appService.setFirstRow(firstResult);
                appService.setRecordsCount(maxResultsCount);

                // Get target Class object and call search(Class, Object);
                try
                {
                    results = appService.search(targetObjectName, criteriaObj);

                }
                catch(Exception e)
                {
                    // Need to change later
                    //System.out.println("WSQuery caught an exception: ");
                    log.error("WSQuery caught an exception: ");
                    e.printStackTrace();
                    throw e;
                }
                // reset all association to null or empty
                results = appService.resetAssociations(results);
                alteredResults = copyValue(results);

            }

        	return alteredResults;
        }

        /**
    	 * @param orgTargetObjectName  
    	 * 			It can be a fully qualified object name or a navigation path contains all fully qualified object name
    	 * 			The first object is the Target Object name
    	 * @param orgCriteriaObj
    	 * 			The domain object which contains the search criteria for the target object
    	 * @return A list of the target objects that are the first # of the record.  (# is set in the property file)
         * @throws Exception
         */
        public List queryObject(String orgTargetObjectName, Object orgCriteriaObj) throws Exception
        {
            
            List results = new ArrayList();
            log.debug("Class name = " + orgCriteriaObj.getClass().getName());
            log.debug("Package name = " + orgCriteriaObj.getClass().getPackage().getName());
            if(orgCriteriaObj.getClass().getPackage().getName().indexOf(".nci.evs.")>0){
                EVSWebService service = new EVSWebService();
                results = query(orgTargetObjectName, orgCriteriaObj, 0, 0);
                //results = service.evsSearch(orgTargetObjectName, orgCriteriaObj);
            }
            else{
        	// To pass 0 for rowcount is to reset the value in WSApplicationService.
                results = query(orgTargetObjectName,orgCriteriaObj,0,0);
            }
            return results;
        }     	
}
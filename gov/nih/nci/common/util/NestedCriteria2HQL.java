/*
 * Created on May 27, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gov.nih.nci.common.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;

/**
 * @author zengje
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class NestedCriteria2HQL {

	private NestedCriteria crit;
	private Configuration cfg;
	private Session session;
	private Query query;
	private Query countQuery;
	
	private final String AND = "AND ";
	private final String OR  = " OR "; // to have an extra space so that it has the same length as AND

	private static Logger log = Logger.getLogger(NestedCriteria.class);

	/**
	 * 
	 */
	public NestedCriteria2HQL(NestedCriteria crit, Configuration cfg, Session session) {
		this.crit = crit;
		this.cfg = cfg;
		this.session = session;
	}
	
	// On the server side, a List is needed to store all the object value in order
	private List paramList = new ArrayList();
	
	public Query translate()
	{
		
		StringBuffer hql = new StringBuffer();
		
		NestedCriteria temp = crit;
		int count = 0;
		
		hql.append("from ");
		hql.append(temp.getTargetObjectName()+ " " + getAlias(temp.getTargetObjectName()) + " ");
		hql.append("where ");
		hql.append(getAlias(temp.getTargetObjectName())+".id in ");
		while(temp != null)
		{
			// if it is the subqueries, need a "("
			hql.append("(");
			hql.append("select ");
			String sourceAlias = getAlias(temp.getSourceName());
			// tricky part, append ".id" for subqueries not the main one
			// if the search is like search("Gene", gene), there is no rolename
			hql.append((temp.getRoleName() == null)?sourceAlias:sourceAlias+"."+ temp.getRoleName());
			
			hql.append(".id ");
			
			hql.append("from ");
			hql.append(temp.getSourceName() + " " + sourceAlias + " ");
			hql.append("where ");
			// if innerNestedCriteria is null, use HashMap value to construct criterion
			// else use source id to ...
			NestedCriteria internal = temp.getInternalNestedCriteria();
			if (internal == null)
			{
				//Since only the obj is passed, parse the obj
				List objList = temp.getSourceObjectList();
				Iterator iterator = objList.iterator();
				while(iterator.hasNext())
				{
					String objCriterion = getObjectCriterion(sourceAlias, iterator.next(), cfg);
					if ((objCriterion != null)&&(objCriterion.trim().length()!= 0))
					{
						hql.append("( ");
						hql.append(objCriterion);
						hql.append(") ");
						hql.append(OR);						
					}					
				}
				if (hql.toString().endsWith(OR))
				{
					// remove last OR					
					hql.setLength(hql.length()-4);
				}
				if (hql.toString().endsWith("where "))
				{
					// remove last where					
					hql.setLength(hql.length()-6);
				}
				// Make up the ")"
				for(int i=0;i<count;i++)
				{
					hql.append(")");
				}
			}
			else
			{
				hql.append(sourceAlias+".id ");
				hql.append("in ");
			}
			temp = internal;
			count++;
		}
		hql.append(")");
		log.debug("NestedCriteria:translate: final hql = " + hql.toString());
		query = session.createQuery(hql.toString());
		countQuery = session.createQuery("select count(*) " + hql.toString());
		// add all parameters
		for (int i=0;i<paramList.size();i++)
		{
			Object valueObj = paramList.get(i);
//			log.debug("NestCriteria2HQL. param list i = " + i + " | value = " + valueObj);				
			if (valueObj instanceof String)
			{
				query.setString(i, (String)valueObj);
				countQuery.setString(i, (String)valueObj);
			}
			else if (valueObj instanceof Long)
			{
				query.setLong(i, ((Long)valueObj).longValue());
				countQuery.setLong(i, ((Long)valueObj).longValue());
			}
			else if (valueObj instanceof Date)
			{
				query.setDate(i,(Date)valueObj);
				countQuery.setDate(i,(Date)valueObj);
			}
			else if (valueObj instanceof Boolean)
			{
				query.setBoolean(i, ((Boolean)valueObj).booleanValue());
				countQuery.setBoolean(i, ((Boolean)valueObj).booleanValue());				
			}
			else if (valueObj instanceof Double)
			{
				query.setDouble(i, ((Double)valueObj).doubleValue());
				countQuery.setDouble(i, ((Double)valueObj).doubleValue());
			}
			else if (valueObj instanceof Float)
			{
				query.setFloat(i, ((Float)valueObj).floatValue());
				countQuery.setFloat(i, ((Float)valueObj).floatValue());
			}
			else if (valueObj instanceof Integer)
			{
				query.setInteger(i, ((Integer)valueObj).intValue());
				countQuery.setInteger(i, ((Integer)valueObj).intValue());
			}
			else
			{
				query.setString(i, valueObj.toString());
				countQuery.setString(i, valueObj.toString());
			}
		}
		return query;
	}
	
	public Query getCountQuery()
	{
		return countQuery;
	}
	
	private void setAttrCriterion(Object obj, PersistentClass pclass, HashMap criterions) throws Exception
	{
		Iterator properties = pclass.getPropertyIterator();
		while(properties.hasNext())
		{
			Property prop = (Property)properties.next();
			// Only get attribute that is not association type
			if (!prop.getType().isAssociationType())
			{
				String fieldName = prop.getName();
				
				// get the field
				Field field = pclass.getMappedClass().getDeclaredField(fieldName);
				field.setAccessible(true);

				// Only take the non-null name/value pair
				if (field.get(obj) != null)
				{
					if (prop.getType().getName().equals("gov.nih.nci.common.util.StringClobType"))
					{
						// Add "*" at the end, so the operator will use "like" 
						criterions.put(fieldName, field.get(obj)+"*");
					}
					else
					{
						criterions.put(fieldName, field.get(obj));
					}					
				}				
			}
		}

	}

	// TODO: Exception handle
	public HashMap getObjAttrCriterion(Object obj, Configuration cfg)
	  		throws Exception
	{
		HashMap criterions = new HashMap();
		List propertyList = new ArrayList();
		String objClassName = obj.getClass().getName();
		// get all the property from the source object
		PersistentClass pclass = cfg.getClassMapping(objClassName);
		
		setAttrCriterion(obj, pclass, criterions);

		// check for super class
//		while (pclass.isJoinedSubclass())
		while(pclass.getSuperclass() != null)
		{
			// get super class
			pclass = pclass.getSuperclass();
			setAttrCriterion(obj,pclass, criterions);
		}
		// Need to add the "id" in it since we use id as our identifier
		String identifier = pclass.getIdentifierProperty().getName();
		Field idField = pclass.getMappedClass().getDeclaredField(identifier);
		idField.setAccessible(true);
		// TODO:  Right now, convert everything to String, that might NOT be right.
		if (idField.get(obj) != null)
		{
			criterions.put(identifier, idField.get(obj));			
		}

		return criterions;
	}
	
	private void setAssoCriterion(Object obj, PersistentClass pclass, HashMap criterions) throws Exception
	{
		Iterator properties = pclass.getPropertyIterator();
		while(properties.hasNext())
		{
			Property prop = (Property)properties.next();
			// Only get association type
			if (prop.getType().isAssociationType())
			{
				String fieldName = prop.getName();
				// get the Declaredfield, since all the association variables are defined as private
				Field field = pclass.getMappedClass().getDeclaredField(fieldName);
				field.setAccessible(true);
				// Only take the non-null name/value pair
				Object value = field.get(obj); 
				if ( value!= null)
				{
					if ( value instanceof Collection)
					{
						if(((Collection)value).size() > 0)
						{
							criterions.put(fieldName, field.get(obj));												
						}
					}
					else 
					{
						criterions.put(fieldName, field.get(obj));												
					}
				}
			}
		}		
	}

	// TODO: Exception handling
	public HashMap getObjAssocCriterion(Object obj, Configuration cfg)
		throws Exception
	{
		HashMap criterions = new HashMap();
		List propertyList = new ArrayList();
		String objClassName = obj.getClass().getName();
		
		// get all the property from the source object
		PersistentClass pclass = cfg.getClassMapping(objClassName);
		
		setAssoCriterion(obj, pclass, criterions);
		while (pclass.isJoinedSubclass())
		{
			pclass = pclass.getSuperclass();
			setAssoCriterion(obj, pclass, criterions);			
		}			
		return criterions;
	}
	
	public String getAlias(String sourceName)
	{
		String alias = sourceName.substring(sourceName.lastIndexOf(".")+1);
		alias = alias.substring(0,1).toLowerCase() + alias.substring(1);
		return alias;
	}
	
	public String getObjectCriterion(String sourceAlias, Object obj, Configuration cfg)
	{
		boolean operatorAtEndFlag = false;
		StringBuffer whereClause = new StringBuffer();
		// TODO exception handling
		try
		{
			//	get field value from hashmap 
			HashMap criterionMap = getObjAttrCriterion(obj, cfg); 
			if(criterionMap != null)
			{
				Iterator keys = criterionMap.keySet().iterator();
				while(keys.hasNext())
				{
					String key = (String)keys.next();
					Object value = criterionMap.get(key);
					// TODO: need to base on the object type using different operator 
					if (!key.equals("id") && (value instanceof String))
					{
						if (crit.caseSensitivityFlag)
						{
							whereClause.append(sourceAlias+"."+ key + getOperator(value)+"? ");	
							paramList.add(((String)value).replaceAll("\\*", "\\%"));
						}
						else
						{
							whereClause.append("lower(" + sourceAlias+"."+ key + ") "+ getOperator(value)+"? ");
							paramList.add(((String)value).toLowerCase().replaceAll("\\*", "\\%"));
						}
					}
					else
					{
						whereClause.append(sourceAlias+"."+ key + getOperator(value)+"? ");					
						paramList.add(value);
					}
					whereClause.append(AND);
					operatorAtEndFlag = true;
				}	
			}
			
			// get association value
			HashMap associationCritMap = getObjAssocCriterion(obj, cfg);
			if (associationCritMap != null)
			{
				if((criterionMap.size() > 0) && (associationCritMap.size() == 0))
				{
					whereClause.setLength(whereClause.length()-4);
					operatorAtEndFlag = false;
				}
				
				Iterator associationKeys = associationCritMap.keySet().iterator();
				while (associationKeys.hasNext())
				{
					String roleName = (String)associationKeys.next();
					Object roleValue = associationCritMap.get(roleName);
					
					// for object association ,the source Alias is changed 
					String assoAlias = sourceAlias + "." + roleName;

					if (roleValue instanceof Collection )
					{
						// parse collection
						Object[] objs = ((Collection)roleValue).toArray();
						whereClause.append("( ");
						for (int i = 0; i<objs.length; i++)
						{
							whereClause.append("( ");
							whereClause.append(getObjectCriterion(assoAlias, objs[i], cfg));
							whereClause.append(") ");
							whereClause.append(AND);
							operatorAtEndFlag = true;
						}
						// remove the last "AND "
						whereClause.setLength(whereClause.length()-4);
						operatorAtEndFlag = false;
						whereClause.append(") ");
					}
					else
					{
						String sub = getObjectCriterion(assoAlias, roleValue, cfg);
						if (sub.length() != 0)
						{
							operatorAtEndFlag = false; // some thing is appended after the logical operator
							whereClause.append(sub);							
						}						
					}
					whereClause.append(AND);
					operatorAtEndFlag = true;					
				} 
				// remove the last "AND "
				if (operatorAtEndFlag)
				{
					whereClause.setLength(whereClause.length()-4);
					operatorAtEndFlag = false;
					whereClause.append(") ");								
				}
			}			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
				if (operatorAtEndFlag == true)
		{
			whereClause.setLength(whereClause.length()-4);
		}
		return whereClause.toString();
	}
	
	public String getOperator(Object valueObj)
	{
		if (valueObj instanceof java.lang.String)
		{
			String value = (String)valueObj;
			if (value.indexOf("*")>= 0)
			{
				return " like ";
			}
		}
		return "=";
	}
}

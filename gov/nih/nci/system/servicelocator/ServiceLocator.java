package gov.nih.nci.system.servicelocator;


import gov.nih.nci.common.util.*;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;
import org.jdom.xpath.XPath.*;
import org.jdom.*;
import org.xml.sax.SAXException;
import org.apache.log4j.*;


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
 * ServiceLocator provides methods to retrieve information from an XML configuration file
 * @author caBIO Team
 * @version 1.0
 */

public class ServiceLocator {
	
	private static Logger log = Logger.getLogger(ServiceLocator.class.getName());
	public Document document;
	public Parser parser;
	public Vector domainObjects;
	public Hashtable dataSource;
	List list;
/**
 * Creates a ServiceLocator instance
 */
	public ServiceLocator()
	{
 
		parser = new Parser("../conf/DAOConfig.xml");
		list = parser.getList("/DAOConfiguration/domainObjects/@name");
		domainObjects = parser.listAttributes(list);
	}

/**
	 * Returns the configuration document
	 * return	Returns a document
 */
public Document getDocument()
	{
		return document;
	}

/**
 * Returns the datasource value
 * @param dataSource  Specified dataSource information
 * @param key		  Specified key of the hashtable
 */
public String getDataSourceCollectionValue(Hashtable dataSource, String key)
{
	return dataSource.get(key).toString();
}

/**
 * Gets the datasource configuration values
 * @param objectName
 * @return
 * @throws ServiceLocatorException
 */
public Hashtable getDataSourceCollection(String objectName)throws ServiceLocatorException
{
	Hashtable dataSource = new Hashtable();
	try{
	for(int i =0; i < domainObjects.size();i++){
		String dObject = (domainObjects.get(i).toString());

			if (dObject.indexOf(objectName)>= 0){

				String xPathExp = "/DAOConfiguration/domainObjects[@name='" + dObject +"']";
				dataSource= parser.listElements(parser.getList(xPathExp));
			}
		}
	}
	catch(Exception ex){
		log.error(ex.getMessage());
		throw new ServiceLocatorException(ex.getMessage());
	}
	return dataSource;
}

/**
 * Updates the data source configuration values sent by the user
 * @param domainObj
 * @param updateTable
 */
public void updateDataSourceCollection(String domainObj, String[][] updateTable)
{
	int indexOfDomainObject = 0;
		for (int i=0; i<domainObjects.size(); i++){
		String dObject = (domainObjects.get(i).toString());
			if(dObject == domainObj){
				indexOfDomainObject = i;
			}
		}
	parser.updateElements( indexOfDomainObject,updateTable );
 }

	public int getORMCount()
	{
		Iterator allDataSource = parser.getElements("DataSource");
		int count = 0;

		while(allDataSource.hasNext())
		{
			Element temp = (Element)allDataSource.next();
			if(temp.getText().indexOf("ORM") != -1)
			{
				count++;
			}
		}
		return count;

	}

	public int getORMCounter(String domainObjectName) throws Exception
	{
		int counter = 0;
		String dsName;
		try
		{
			dsName =getDataSourceCollectionValue(getDataSourceCollection(domainObjectName), "DataSource");
			String index = dsName.substring(3);
			return Integer.parseInt(index);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
			throw new Exception(e.getMessage());
		}
	}
}


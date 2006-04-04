package gov.nih.nci.system.delegator;

import gov.nih.nci.system.dao.*;
import gov.nih.nci.system.servicelocator.*;
import gov.nih.nci.system.proxy.InterfaceProxy;
import gov.nih.nci.common.net.*;
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
  * BaseDelegate encapsulates the query for different datasources  
  * @author caBIO Team 
  * @version 1.0
  */
public class BaseDelegate implements InterfaceProxy
{

	private static Logger log = Logger.getLogger(BaseDelegate.class.getName());
	public ServiceLocator locator;

	/**
	 * Return the resultset of the query embedded in an object of gov.nih.nci.common.net.Response
	 * @param request - a gov.nih.nci.common.net.Request object passed from client 
	 * @return an object of gov.nih.nci.common.net.Response that contains the query resultset
	 * @throws DelegateException
	 */    
	 
	public Response query(gov.nih.nci.common.net.Request request) throws DelegateException, Exception
	{
		
		DAOFactory factory = null;

		String dataSource ;
		String domainObjectName = request.getDomainObjectName();
		locator = new ServiceLocator();
		Response response;
		try{
			request.setConfig(locator.getDataSourceCollection(domainObjectName));
			dataSource =locator.getDataSourceCollectionValue(locator.getDataSourceCollection(domainObjectName), "DataSource");
		
		}
		catch(ServiceLocatorException slEx)
		{
			log.error("No data source found");
			throw new DelegateException(" No data source was found " + slEx.getMessage());
		}
		catch(Exception exception)
		{
			log.error("Exception while getting datasource information "+ exception.getMessage());
			throw new Exception("Exception in Base Delegate while getting datasource information: " + exception);
		}

		if (dataSource == null)
		{
			log.error("No Data Source could be found for the specified domain object");
			throw new DelegateException("No Data Source could be found for the specified domain object");
		}
		try
		{
		
			if (dataSource.indexOf("ORM") != -1)
			{
				factory =DAOFactory.getFactory(gov.nih.nci.common.util.Constant.ORM_DAO);
			}
			else if (dataSource.equalsIgnoreCase("EVS"))
			{
				factory = DAOFactory.getFactory(gov.nih.nci.common.util.Constant.EVS_DAO);		
			}
			else {
				factory = DAOFactory.getFactory(gov.nih.nci.common.util.Constant.EXTERNAL_DAO);
				}
			
			response = factory.query(request);
		}
		catch(DAOException daoException)
		{
			log.error(daoException.getMessage());
			throw new Exception(daoException.getMessage());
		}
		catch(Exception exception)
		{
			log.error(exception.getMessage());
			throw new Exception("Exception in the Base Delegate:  "+exception.getMessage());
		}

		return response;
	}
}

// $Log: not supported by cvs2svn $
// Revision 1.11  2005/07/15 16:27:58  muhsins
// logging information added
//
// Revision 1.10  2005/07/15 15:22:36  muhsins
// logging information added
//
// Revision 1.9  2005/07/12 15:53:53  muhsins
// Loggin statements added
//
// Revision 1.8  2005/05/11 20:30:11  muhsins
// Added InterfaceProxy interface
//
// Revision 1.7  2005/05/06 21:35:28  shanbhak
// Added cvs log at the end
//
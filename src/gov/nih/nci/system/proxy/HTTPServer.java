package gov.nih.nci.system.proxy;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import gov.nih.nci.system.delegator.*;
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
  * HTTP Tunneling Servlet
  * @author caBIO Team
  * @version 1.0
  */
public class HTTPServer extends HttpServlet
{
	private static Logger log = Logger.getLogger(HTTPServer.class.getName());
	/**
	 * Called by the server (via the service method) to allow a servlet to handle a GET request.
	 * @param request - an HttpServletRequest object that contains the request the client has made of the servlet
	 * @param response - an HttpServletResponse object that contains the response the servlet sends to the client
	 * @throws java.io.IOException - if an input or output error is detected when the servlet handles the GET request
	 *         ServletException - if the request for the GET could not be handled
	 */

	public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        PrintWriter out = response.getWriter();
        out.println("<HTML><HEAD><TITLE>Welcome</TITLE></HEAD><BODY>");
        out.println("<H1>Welcome to the HTTP Proxy. The Server has been deployed. call doPost to send request!</H1>");
        out.println("</body></html>");
    }

	/**
	 * Called by the server (via the service method) to allow a servlet to handle a POST request.
	 * @param request - an HttpServletRequest object that contains the request the client has made of the servlet
	 * @param response - an HttpServletResponse object that contains the response the servlet sends to the client
	 * @throws java.io.IOException - if an input or output error is detected when the servlet handles the POST request
	 *         ServletException - if the request for the POST could not be handled
	 */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        //Read Client Request
        gov.nih.nci.common.net.Request clientRequest = null;
        try
        {
            java.io.InputStream is = request.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            clientRequest = (gov.nih.nci.common.net.Request)ois.readObject();
        }
        catch( OptionalDataException ex)
        {
        	log.error(ex.getMessage());
        }
        catch(ClassNotFoundException ex)
        {
        	log.error(ex.getMessage());
        }
        catch(IOException ex)
        {
        	log.error(ex.getMessage());
        }
        catch(Exception ex)
        {
        	log.error("Exception in HTTPServer reading the response \n" + ex.getMessage());
			throw new ServletException("Exception in HTTPServer reading the response "+ex);
        }

	    //Set content type of the Stream as we are sending Objects
        response.setContentType("binary/x-java-serialized");
        java.io.OutputStream os = response.getOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(os);

	   //Write it back to the stream
        try
        {
 		    oos.writeObject(query(clientRequest));
        }
        catch( InvalidClassException ex)
        {
        	log.error(ex.getMessage());
        }
        catch(NotSerializableException  ex)
        {
        	log.error(ex.getMessage());
        }
        catch(IOException ex)
        {
        	log.error(ex.getMessage());
        }
        catch(Exception ex)
        {
        	log.error(ex.getMessage());
        }
        finally
		{
        	oos.flush();
        	oos.close();
		}
    }

    /**
     * Return the resultset of the query embedded in an object of gov.nih.nci.common.net.Response
     * @param clientRequest - a gov.nih.nci.common.net.Request object passed from client
     * @return an object of gov.nih.nci.common.net.Response that contains the query resultset
     */

    public gov.nih.nci.common.net.Response query(gov.nih.nci.common.net.Request clientRequest)
    {
    	//Response
    	gov.nih.nci.common.net.Response clientResponse = new gov.nih.nci.common.net.Response();;
        //Delegate
     	BaseDelegate delegate = null;

        try
        {
        	delegate = new BaseDelegate();
        	clientResponse = delegate.query(clientRequest);
        }

        catch(DelegateException de)
        {
	       	de.printStackTrace();
  			clientResponse.setResponse(de);
  			log.error(de.getMessage());
        }
        catch(Exception ex)
        {
  			ex.printStackTrace();
  			clientResponse.setResponse(ex);
  			log.error(ex.getMessage());
        }
        return clientResponse;
    }

    /**
     * Return Servlet Information
     * @return servlet name
     */
    public String getServletInfo()
    {
        return "HTTPServlet";
    }
}

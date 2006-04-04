package gov.nih.nci.system.applicationservice;

import gov.nih.nci.common.net.Response;

import gov.nih.nci.system.proxy.*;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;
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
 * HTTPClient provides components to connect and query a HTTP server.
 * @author caBIO Team 
 * @version 1.0
 */
public class HTTPClient {
	
	private static Logger log= Logger.getLogger(HTTPClient.class.getName());
    private String httpAddress;
    private HttpURLConnection httpConnection;

/**
	* Creates a HTTPClient instance.
*/
    public HTTPClient(){
        httpAddress = null;
        httpConnection = null;
      }

/**
	* Creates a HTTPClient instance that establishes a connection to the specified HTTP server.
	* @param serverURL - Specifies the HTTP server address
	* @throws Exception
*/
   
    public HTTPClient(String httpAddress) throws Exception{
        init(httpAddress);
        }

/**
	* Sets the specified httpAddress for this HTTPClient
	* @param serverURL - Specifies the HTTP server address
*/    
    public void setHttpAddress(String httpAddress){
           this.httpAddress = httpAddress;
           }
/**
	* Returns the httpAddress for this HTTPClient
	* @return - Returns the HTTP server address
*/
    public String getHttpAddress(){
           return httpAddress;
           }

/**
	* Creates a http connection for the specified httpAddress
	* @param serverURL - Specifies the HTTP server address
	* @throws Exception
*/
    public void init(String httpAddress) throws Exception{
       try{
       		
			this.httpAddress 	= httpAddress;
            URL url 		= new URL(httpAddress);
            httpConnection 	= (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod("POST");
            httpConnection.setAllowUserInteraction(false);
            httpConnection.setDoOutput(true);
            httpConnection.setRequestProperty("Content-type","binary/x-java-serialized");

        }
        catch( java.net.ConnectException ex){
        	log.error("ConnectionException - "+ ex.getMessage());
        	throw new Exception(ex.getMessage());}
        catch( java.io.IOException ex){
        	log.error("IOException - "+ ex.getMessage());
        	throw new Exception(ex.getMessage());}
        catch(Exception ex){
        	String msg = null;
        	if(ex.getMessage()==null)
        		msg = "Specified http server cannot be found";
        	else
        		msg = ex.getMessage();
        	log.error("Exception - "+ msg);
        	throw new Exception("Exception " + msg);
        	}
     }

    public HttpURLConnection getConnection(){
        return httpConnection;
        }


/**
   * Sends a request to the server and receives a response
   * @param request Specifies the query that needs to be executed in a request object. 
   * @return Returns the resultset of a query in a respone object
   * @throws Exception
*/
   public gov.nih.nci.common.net.Response query(gov.nih.nci.common.net.Request request) throws Exception{
 		
   	   String exMessage = null;
	   ObjectOutputStream oos = null;
	   gov.nih.nci.common.net.Response response = new Response();
	   
        try{
            OutputStream out = httpConnection.getOutputStream();
            oos = new ObjectOutputStream(out);
            oos.writeObject(request);
        }
        catch( java.net.ConnectException ex){
        	log.error("ConnectionException - " + ex.getMessage());
        	throw new Exception(ex.getMessage());}
        catch(java.io.InvalidClassException ex){ 
        	log.error("InvalidClassException - "+ex.getMessage());
        	throw new Exception(ex.getMessage());}
        catch(Exception ex){
        	log.error("Exception - "+ ex.getMessage());
        	throw new Exception(ex.getMessage());}
        finally
		{
        	oos.flush();
            oos.close();  
		}

		try{
		      InputStream in = httpConnection.getInputStream();
		      ObjectInputStream ois = new ObjectInputStream(in);

		      response = (gov.nih.nci.common.net.Response) ois.readObject();
		      if(response.getResponse() instanceof Exception){
		          Exception e = (Exception) response.getResponse();
		          log.error(e.getMessage());
		          throw new Exception(e.getMessage());
		          }
			}
		catch(ClassNotFoundException ex){ 
			log.error("ClassNotFoundException - "+ ex.getMessage());
			throw new Exception("ClassNotFoundException: - " + ex.getMessage());}
		catch(java.io.InvalidClassException ex){ 
			log.error("InvalidClassException - "+ ex.getMessage());
			throw new Exception("InvalidClassException - "+ ex.getMessage());}
		catch(java.io.StreamCorruptedException ex){ 
			log.error("StreamCorruptedException - "+ ex.getMessage());
			throw new Exception("StreamCorruptedException - " + ex.getMessage());}
		catch(java.io.OptionalDataException  ex){ 
			log.error("OptionalDataException - "+ ex.getMessage());
			throw new Exception("OptionalDataException - " +  ex.getMessage());}
		catch(java.io.IOException  ex){ 
			log.error("IOException - "+ ex.getMessage());
			throw new Exception("IOException - " + ex.getMessage());}
		catch(Exception ex){ 
			String msg = "query failed";
			if(ex.getMessage()!=null){
				msg = msg + " - "+ex.getMessage();
			}
			log.error("Exception - "+ msg);
			throw new Exception(msg);
			}

		return response;
        }

}

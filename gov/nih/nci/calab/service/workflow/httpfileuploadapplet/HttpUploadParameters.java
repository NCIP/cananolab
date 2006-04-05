/*
The caArray Software License, Version 1.2

Copyright 2004 SAIC. This software was developed in conjunction with the National 
Cancer Institute, and so to the extent government employees are co-authors, any 
rights in such works shall be subject to Title 17 of the United States Code, 
section 105.

Redistribution and use in source and binary forms, with or without modification, 
are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this 
list of conditions and the disclaimer of Article 3, below. Redistributions in 
binary form must reproduce the above copyright notice, this list of conditions 
and the following disclaimer in the documentation and/or other materials 
provided with the distribution.

2. Affymetrix Pure Java run time library needs to be downloaded from  
(http://www.affymetrix.com/support/developer/runtime_libraries/index.affx) 
after agreeing to the licensing terms from the Affymetrix. 

3. The end-user documentation included with the redistribution, if any, must 
include the following acknowledgment:

"This product includes software developed by the Science Applications International 
Corporation (SAIC) and the National Cancer Institute (NCI).”

If no such end-user documentation is to be included, this acknowledgment shall 
appear in the software itself, wherever such third-party acknowledgments 
normally appear.

4. The names "The National Cancer Institute", "NCI", 
“Science Applications International Corporation”, and "SAIC" must not be used to 
endorse or promote products derived from this software.

5. This license does not authorize the incorporation of this software into any 
proprietary programs. This license does not authorize the recipient to use any 
trademarks owned by either NCI or SAIC.

6. THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES, 
(INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND 
FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED. IN NO EVENT SHALL THE NATIONAL 
CANCER INSTITUTE, SAIC, OR THEIR AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, 
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY
  OF SUCH DAMAGE.
*/

package gov.nih.nci.calab.service.workflow.httpfileuploadapplet;

/**
 * This Java bean class encapsulates all the parameters received from the server
 * that will be used to configure the upload process.
 */
   
public class HttpUploadParameters
{
    private final String sidString = ";jsessionid="; 
    /**
     * The destination url to which the uploading files will be
     * sent and saved.
     */ 
    private String uploadURL = null;
    
    /**
     * The destination url to which server is notified for the next step
     * process.
     */ 
    private String notifyURL = null;
    
    /**
     * The default url to go when user presses the done button to
     * end the uploading process.
     */ 
    private String defaultURL = null;
    
    /**
     * The String variable that holds the key to the hybridization annotation 
     * data for this upload operation,that is stored in a static cache
     * on the server side.
     */ 
    private String id = null;
    
    /**
     * The port number on the server that listens for incoming files. 
     * To be used by file upload through the Socket object. 
     */ 
    private int portNumber = 8080;

    /**
     * The session id for session identification.
     */
    private String sid = null;
    
    /**
     * The default constructor
     */
    public HttpUploadParameters()
    {}
    
    /**
     * @return Returns the id.
     */
    public String getId() 
    {
        return id;
    }
    /**
     * @param id The id to set.
     */
    public void setId(String id) 
    {
        this.id = id;
    }
    /**
     * @return Returns the portNumber.
     */
    public int getPortNumber() 
    {
        return portNumber;
    }
    /**
     * @param portNumber The portNumber to set.
     */
    public void setPortNumber(int portNumber) 
    {
        this.portNumber = portNumber;
    }

    /**
     * @return Returns the uploadURL.
     */
    public String getUploadURL() 
    {
        return uploadURL;
    }
    /**
     * @param uploadURL The uploadURL to set.
     */
    public void setUploadURL(String uploadURL) 
    {
        this.uploadURL = uploadURL;
    }
    
    /**
     * @return Returns the notifyURL.
     */
    public String getNotifyURL() 
    {
        return notifyURL;
    }
    /**
     * @param notifyURL The notifyURL to set.
     */
    public void setNotifyURL(String notifyURL) 
    {
        this.notifyURL = notifyURL;
    }
    
    /**
     * @return Returns the defaultURL.
     */
    public String getDefaultURL() {
        return defaultURL;
    }
    /**
     * @param defaultURL The defaultURL to set.
     */
    public void setDefaultURL(String defaultURL) {
        this.defaultURL = defaultURL;
    }
    
    /**
     * This method returns a long type as an id 
     * from a string id which has a format of 123456789990_1
     * generated by the server as a key to store hybridization
     * annotation data.
     *
     * @param long The id without suffix as "-x"
     */
    public long getIdasLong()
    {   
        return Long.parseLong(id);
    }
    /**
     * @return Returns the sid.
     */
    public String getSid() {
        return sid;
    }
    /**
     * @param sid The sid to set.
     */
    public void setSid(String sid) {
        this.sid = sid;
    }
    
    /**
     * This method will return a uploadURL string with
     * a session id (jsessionid) appended at the end for
     * session identification by the server.
     *
     * @return String the upload url with a session id.
     */
    public String getUploadURLWithSid()
    {
        return uploadURL + sidString + sid;
    }
    
    /**
     * This method will return a notifyURL string with
     * a session id (jsessionid) appended at the end for
     * session identification by the server.
     *
     * @return String the notify url with a session id.
     */
    public String getNotifyURLWithSid()
    {
        int index = notifyURL.indexOf("?");
        String str = sidString + sid;
        StringBuffer sb = new StringBuffer(notifyURL);
        sb.insert(index, str);

        return sb.toString();
    }
    
    /**
     * This method will return a defaultURL string with
     * a session id (jsessionid) appended at the end for
     * session identification by the server.
     *
     * @return String the default url with a session id.
     */
    public String getDefaultURLWithSid()
    {
        return defaultURL + sidString + sid;
    }
}
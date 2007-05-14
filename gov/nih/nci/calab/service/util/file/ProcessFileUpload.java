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
package gov.nih.nci.calab.service.util.file;

import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.service.util.PropertyReader;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This servlet is called up by the HttpFileUploadApplet to process files
 * that are sent from the client machines. It does several things.  First
 * it extracts meta data about the incoming file, and converts them into
 * a HttpUploadedFileData object. It then reads file and writes them to 
 * the designated directory on the file server.  Thirdly, depending on 
 * a boolean setting of the HttpFileUploadCache object, it deletes the files
 * it just wrote if isStopped flag is set to true, otherwise do nothing. Lastly
 * if the isStopped flag is set to false, it adds HttpUploadedFileData object
 * to the HttpFileUploadCache object to be used by the subsequent file
 * parsing service.
 *
 * @author zhoujim 
 */
public class ProcessFileUpload extends HttpServlet
{
    private Log log_ = LogFactory.getLog(this.getClass());

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String query = request.getQueryString();
        log_.debug("doPost..." + query);

        //Retrieve the cache object for this upload session.
        HttpFileUploadSessionData sessionData 
            = (HttpFileUploadSessionData)request.getSession().getAttribute("httpFileUploadSessionData");
        
        String fileName = (String)request.getParameter("fileName");
        fileName = sessionData.getTimeStamp()+"_"+fileName;
        
        HttpUploadedFileData data = new HttpUploadedFileData();
        data.setFileName(fileName);
        data.setType((String)request.getParameter("menuType"));
        data.setId((String)request.getParameter("id"));
        data.setValidatorCode((String)request.getParameter("crc32"));
        String module = (String)request.getParameter("module");
        
        String token = (String)request.getParameter("mode");
        log_.info("Mode is " + token);
        
        
        //This should not happen since it must be first created just before 
        //launching the applet. But it will happen if the session is expired.
        if (sessionData == null)
        {
            log_.debug("sessionData is null, exit by sending an expiration message to applet");
            try
            {
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                //response.sendRedirect()
                //It is important to have "expired" in this message.
                //This word is an indicator of session expiration for
                //the applet.
                out.println("Your session is expired.  Please login again to complete your file upload.");
            }
            catch (IOException ie)
            {
                log_.error("IOException occured: " + ie.getMessage());
            }
            return;
        }       
        //It is important to check that the incoming file is the first in a roll 
        //for reason: the upload process could have been stopped by the user
        //previously, so HttpFileUploadCache object's isStopped flag is true.
        //If it is a new upload process, we must set isStopped to false
        //to make sure that incoming file will not be deleted upon writing.
        else
        {
            if ("first".equals(token))
            {
            	sessionData.setIsStopped(false);
            }
        }
        
        //If this comes after user presses the stop button, then delete it.
        //It seems to be case when the HttpFileUploadThread is stopped upon 
        //user's click, the ongoing uploading process performed by the 
        //HttpFileUploader is continuing. As a result, the files coming in
        //after stop action should not be saved. 
        if (sessionData.getIsStopped())
        {
            log_.debug("Module " + module + ": File " + data.getFileName() + " is not saved");
            return;
        }
        String errorMessage = null;
        DiskFileUpload fu = new DiskFileUpload();
        // If file size exceeds, a FileUploadException will be thrown
        fu.setSizeMax(2100000000);
        File fNew = null;
        boolean isSaved = false;
        try
        {
            List fileItems = fu.parseRequest(request);
            Iterator itr = fileItems.iterator();
            log_.debug("itr count " + itr.toString());    
            while (itr.hasNext())
            {
                FileItem fi = (FileItem)itr.next();
                
                String fullPathName = null;
                
                //TODO: set path here
                String path = PropertyReader.getProperty(CaNanoLabConstants.FILEUPLOAD_PROPERTY, "fileRepositoryDir");
                
               
                fullPathName = path +  CaNanoLabConstants.FOLDER_WORKFLOW_DATA + File.separator
                					+ sessionData.getAssayType() + File.separator 
                                    + sessionData.getAssay() + File.separator
                                    + sessionData.getRun()   + File.separator
                                    + sessionData.getInout();

                //check directory is existing or not.
                File fPath = new File(fullPathName);
                if (!fPath.exists())
                {
                    fPath.mkdirs();
                }

                //If failed to get the 
                if (fullPathName == null || fullPathName.length() == 0)
                {
                	log_.debug("Failed to get the file path to the file system.");
                	break;
                }
                log_.debug("The file Path is " + fullPathName);
                
                String uploadFileName = sessionData.getTimeStamp()+"_"+fi.getName();
                fNew = new File(fullPathName, uploadFileName);
                fi.write(fNew);
                isSaved = true;
                log_.debug("Module " + module + ": File " + fNew.getName() + " saved");
                
                //uncompress file here
                String unzipFilePath = fullPathName + File.separator
                                       + CaNanoLabConstants.UNCOMPRESSED_FILE_DIRECTORY;
                File unzipFile = new File(unzipFilePath);
                if (!unzipFile.exists())
                {
                    unzipFile.mkdirs();
                }
                
                FileUnzipper fUnzipper = new FileUnzipper();
                fUnzipper.unzip(fullPathName + File.separator+ uploadFileName,
                                unzipFilePath);
                
            }
        }    
        catch (Exception e)
        {
        	errorMessage = "Cannot write files into File System. sending a message to applet";
            log_.error(errorMessage);
            throw new IOException("Error in writing files into File system, " + e.getMessage());
        }
        //Get zip file's original name, which is embedded
        //in the zip file.
        if (isSaved)
        {
            ReadZipFileContent zipReader = new ReadZipFileContent();
            try
            {
                String original = zipReader.getFileName(fNew);
                log_.debug("The original file name is " + original);
                data.setOriginalFileName(original);
                sessionData.addToList(data);
            }
            catch (Exception e)
            {
            	errorMessage = "cannot get original file name from the zip file";
        	    log_.error(errorMessage);
        	    
        	    //If failed to get the original file name, we treat
        	    //it like file upload failure.
        	    isSaved = false;
        	    //Also delete the file fNew
        	    try
        	    {
        	        fNew.delete();
        	    }
        	    catch (Exception ee)
        	    {
        	    	log_.error("Failed to delete the file just saved: " + ee.getMessage());
        	    }
            }
        }
        
        //If the file is not saved, send a message to the applet.
        //if (!isSaved)
        if (!isSaved)
        {
            try
            {
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();

                out.println("Your file was uploaded successfully, but the system failed to save it.  You may try again.");
            }
            catch (IOException ie)
            {
                log_.error("IOException occured: " + ie.getMessage());
            }
        	//write a messge to db for record.
       
            if (data.getOriginalFileName() == null)
            {
            	data.setOriginalFileName("lost");
            }
        }
        
    }
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        doPost(request, response);
    }
}
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
 * This class extends Thread class to run the file upload process.  The thread 
 * is started by the applet, and it in turn uses HttpFileUploader class to carry
 * out the file upload operation.  
 */


import java.util.Vector;

public class HttpFileUploadThread extends Thread 
{
     /**
      * the HttpFileUploader to handle the transmittal of files. 
      */
    private HttpFileUploader loader = null;
    
    /**
     * A boolean value to indicate if all uploading files have been zipped. 
     */
    private volatile boolean isZipped = false;

    /**
     * A boolean value to indicate if fatal error happens before zipping is started. 
     */ 
    private volatile boolean isFailed = false;

    /**
     * A boolean value to indicate if the upload process is completed. 
     */
    private volatile boolean isUploadComplete = false;
    
    /** 
     * CONSTRUCTOR 
     *
     * @param files The container containing all the files to be uploaded.
     * @oaram up the HttpUploadParameters object holding parameters passed 
     *        from the server.
     */
    public HttpFileUploadThread(Vector files, HttpUploadParameters up)
    {
        loader = new HttpFileUploader(files, up); 
    }

    /**
     * The run method to control the upload process.
     */
    public void run()
    {    
        //We will zip all files before starting upload.
        loader.zipFiles();
        isFailed = loader.isFailed();
        if (!isFailed)
        {
        	isZipped = loader.isZipped();
        }
        
        boolean done = false;
        //If failed to zip files, then there is no point to
        //to start upload.
        while (!isFailed)
        {    
            //turn off this flag once in upload loop, so
            //Applet can diplay other meaningful message to the user.
            //isZipped = false;   
             
            done = loader.uploadFilesViaHttpClient();
            //DO NOT REMOVE THE FOLLOWING LINES.
            //done = loader.uploadFilesViaURLConnection();
            //done = loader.uploadFilesViaSocket();
            
            if (done)
            {
            	isUploadComplete = true;
            	break;
            }
        }
    }
    
    /**
     * To stop the run method of this thread. To stop the
     * run method, loader sets isStopped value to true, which
     * will cause the exit of file upload loop when the file
     * being uploaded finished.  
     */
    public boolean stopUpload()
    {
        loader.setStopped(true);
        
        while (!isFailed && !isUploadComplete)
        {
        	try
        	{
        	    Thread.sleep(50);
        	}
        	catch (InterruptedException e)
        	{
        		;
        	}
        }

        return true;
    }
    /** 
     * Delete temp zip files
     */
    public void cleanUp()
    {
        loader.cleanUp();
    }
    
    public boolean isUploadPreparationDone()
    {
        return isZipped;
    }

    public String getCurrentFileName()
    {
        return loader.getCurrentFileName();
    }
    
    public int getCompletionPercent()
    {
        return loader.getPercentage();
    }
    
    public boolean isFailed()
    {
        return loader.isFailed();
    }
    
    public String getFailureMessage()
    {
        return loader.getFailureMessage();
    }
    
    public boolean isUploadComplete()
    {
        return isUploadComplete;    
    }
    
    public void startUpload()
    {
        this.start();
    }
    
    public boolean notifyServer(String mode)
    {
        loader.notifyServer(mode);
        
        //If the notify process failed.
        if (loader.isFailed())
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    
    public String getNotifyReply()
    {
        return loader.getNotifyReply();
    }
    
    public Object[] getOkFiles()
    {
        return loader.getOkFiles();
    }
    public Object[] getFailFiles()
    {
        return loader.getFailFiles();
    }

    public String getDurationString()
    {
        return loader.getDurationString().toString();
    }
    public long getBytesSent()
    {
        return loader.getBytesSent();
    }
    
    public String getTimeBytes()
    {
        StringBuffer sb = loader.getDurationString();
        sb.append(" and " );
        sb.append(loader.getBytesSent());
        sb.append(" bytes uploaded");
        
        return sb.toString();
    }
}
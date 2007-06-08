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


import gov.nih.nci.calab.service.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class serves as a container for hybridization file annotation data 
 * gathered before the file upload operation. An instance of this class 
 * will be stored in HttpFileProperties map for each file upload applet session.
 */

public class HttpFileUploadSessionData implements Serializable
{
    /**
     * The data object that gathers all the hybridization annotation
     * data through many jsp pages.
     */
    private String timeStamp = null;
    private String assayType = null;
    private String assay = null;
    private String run = null;
    private String inout = null;
    private String runId= null;
    private String fromType = null;
    

    /**
     * The container object that is the place for all meta data for 
     * the uploaded files.  Valid data type: HttpUploadedFileData.
     */
    private List fileList = new ArrayList();
    
    /**
     * The boolean flag.  Set to true when user requests a stop action.
     * Otherwise it is false.  Default value is false.
     */
    private boolean isStopped = false;
    
    /**
     * The default constructor.
     */
    public HttpFileUploadSessionData()
    {}

    public List getFileList() 
    {
        return fileList;
    }
    /**
     * @param fileList The fileList to set.
     */
    public void setFileList(List fileList) 
    {
        this.fileList = fileList;
    }
    
    /**
     * @return Returns the isStopped.
     */
    public boolean getIsStopped() 
    {
        return isStopped;
    }
    /**
     * @param isStopped The isStopped to set.
     */
    public void setIsStopped(boolean isStopped) 
    {
        this.isStopped = isStopped;
    }
    
    /**
     * Add a meta data object HttpUploadedFileData into the fileList
     * container.
     * 
     * @param obj The HttpUploadedFileData object constructed using data
     *         passed from the applet through http query header string.
     */
    public void addToList(Object obj)
    {
        fileList.add(obj);
    }
    
    /**
     * Upon calling file parsing service, the content of the fileList
     * is passed to the parsing object, and this list is cleared for
     * accepting next batch of file upload.
     */
    public void clearList()
    {
        fileList.clear();
        timeStamp = null;
    }
    
    public String getTimeStamp()
    {
        
        if(timeStamp == null)
        {
            timeStamp = StringUtils.getTimeAsString();
        }
        
        return timeStamp;
    }
      
    /**
     * 
     * To return the size of the fileList.
     */
    public int getFileListSize()
    {
        return fileList.size();
    }

    public String getAssay()
    {
        return assay;
    }

    public void setAssay(String assay)
    {
        this.assay = assay;
    }

    public String getAssayType()
    {
        return assayType;
    }

    public void setAssayType(String assayType)
    {
        this.assayType = assayType;
    }

    public String getInout()
    {
        return inout;
    }

    public void setInout(String inout)
    {
        this.inout = inout;
    }

    public String getRun()
    {
        return run;
    }

    public void setRun(String run)
    {
        this.run = run;
    }

    public String getRunId()
    {
        return runId;
    }

    public void setRunId(String runId)
    {
        this.runId = runId;
    }

	public String getFromType() {
		return fromType;
	}

	public void setFromType(String fromType) {
		this.fromType = fromType;
	}
    
}
    


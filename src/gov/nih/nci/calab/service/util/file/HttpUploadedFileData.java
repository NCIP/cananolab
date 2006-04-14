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

import java.io.Serializable;

/**
 * A simple java bean class to store meta data about the uploaded file received 
 * from the applet.
 */

public class HttpUploadedFileData implements Serializable
{
    /**
     * The type means Data or Other user set at the time files are selected.
     */
    private String type = null;
    
    /**
     * The absolute file path of the uploaded file on the 
     * client machine, before it was zipped.
     */
    private String filePath = null;
    
    /**
     * The zip file name for the uploaded file, based on zip file naming convention
     * for the application such as hybData_gpr.zip from
     * hybData.gpr.
     */
    private String fileName = null;

	/**
     * The sumbitters userId for the uploaded file,. 
     *
     */
    private String userId = null;

	/**
     * The original file name for the uploaded file, 
     * hybData.gpr.
     */
    private String originalFileName = null;

    /**
     * The validatorCode validation for the for the uploaded file.
     */
    private String validatorCode = null;
    
    
    /**
     * The original file's extension that is preserved in the zipped file
     * file name.  
     */
    private String fileExtension = null;
    
    /**
     * It is actually the key to access cached data about this upload operation.
     */
    private String id= null;
    
    
    /**
     * Default constructor.
     */
    public HttpUploadedFileData()
    {}

	/**
     * @return Returns the userId.
     */
    public String getUserId() 
    {
        return userId;
    }

	/**
     * @param userId The userId to set.
     */
    public void setUserId(String userId) 
    {
        this.userId = userId;
    }

	/**
     * @return Returns the fileName.
     */
    public String getFileName() 
    {
        return fileName;
    }

	/**
     * @param fileName The fileName to set.
     */
    public void setFileName(String fileName) 
    {
        this.fileName = fileName;
    }

	/**
     * @return Returns the filePath.
     */
    public String getFilePath() 
    {
        return filePath;
    }
    
	/**
     * @param filePath The filePath to set.
     */
    public void setFilePath(String filePath) 
    {
        this.filePath = filePath;
    }
    
	/**
     * @return Returns the type.
     */
    public String getType() 
    {
        return type;
    }
    
	/**
     * @param type The type to set.
     */
    public void setType(String type) 
    {
        this.type = type;
    }
    
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
     * @param filename The filename from which to extract file extension.
     */
    public void setFileExtension(String filename)
    {
        /* A file name should look like hybData_gpr_2892890239390093.zip.
         * It is derived from the original file hybData.gpr.
        */
        if (filename == null || filename.length() == 0)
        {
            return;
        }
        int index = filename.indexOf("_");
        int end = index;
        int start = -1;
        while (index != -1)
        {   
            index = filename.indexOf("_", index + 1);
             
            if (index != -1)
            {
             	start = end + 1;
                end = index;
            }
        }
         
        if (start != -1 && end != -1 && end > start)
        {
            fileExtension = filename.substring(start, end);
        }
    }

    /**
     * @return Returns the fileExtension.
     */
    public String getFileExtension()
    {
        return fileExtension;
    }

	/**
     * Returns the original file name for the uploaded file
     * 
     * @return String the original file name for the
     *         uploaded file.
     */
    public String getOriginalFileName() 
    {
        return originalFileName;
    }

	/**
     * Sets the original file name for the uplaoded file
     * 
     * @param originalFileName String representing the 
     *                         original file name for
     *                         the uplaoded file.
     */
    public void setOriginalFileName(String originalFileName) 
    {
        this.originalFileName = originalFileName;
    }
	
	/**
     * Returns the CRC32 validatorCode for the uploaded file
     * 
     * @return String the CRC32 for the uploaded file.
     */
    public String getValidatorCode() 
    {
        return validatorCode;
    }

	/**
     * Sets the CRC32 validatorCode for the uplaoded file
     * 
     * @param validatorCode String representing the CRC32 
     *                      for the uplaoded file.
     */
    public void setValidatorCode(String validatorCode) 
    {
        this.validatorCode = validatorCode;
    }
	
}
    


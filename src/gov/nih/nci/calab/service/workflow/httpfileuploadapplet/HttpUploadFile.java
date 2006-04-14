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

import java.io.File;


/**
 * This Java bean class is used to hold file information user selected
 * for upload.
 */

public class HttpUploadFile
{
    /**
     * The absolute path of the selected file on the client machine.
     */
    private String absoluteFilePath;

    /**
     * The file Type user selected.  Valid types: DATA and OTHER.
     */
    private String fileType;
    
    /**
     * Zip file name generated after the selected file is zipped.  Name
     * convention is:  original file name with dot changed to a underscore,
     * plus underscore, plus experiment id, then zip extension.  Example:
     * myhibridization_one.gpr --> myhibridization_one_gpr_378398239494990.zip.
     */
    private String zipFileName = null;
    
    /**
     * Zip file was created as a temporary file on client machine, this is the 
     * absolute path of this temporary zip file.
     */
    private String zipFilePath = null;
    
    /**
     * The selected file name without path part. 
     * This file name has been changed if it 
     * contains illegal characters.
     */
    private String fileName = null;
    
    /**
     * The CRC32 value in string for the zipped file.
     */
    private String crc32 = null;
     
    private static String ZIPEXTENSION = ".zip";
    /**
     * Modify the file name to include its data type for display purpose only.
     *
     * @return String the modified file name.
     */
    public String toString()
    {
       //return null; //
       return new File(absoluteFilePath).getName() + " (" + fileType + ")";
    }

    public String getAbsoluteFilePath()
    {
        return absoluteFilePath;
    }

    public void setAbsoluteFilePath(String path)
    {
        absoluteFilePath = path;
    }

    public String getFileType()
    {
        return fileType;
    }

    public void setFileType(String type)
    {
        fileType = type;
    }
    
    public String getZipFilePath()
    {
        return zipFilePath;
    }

    public void setZipFilePath(String zipFilePath)
    {
        this.zipFilePath = zipFilePath;
    }
    
    /**
     * Generate a zip file name for this selected file.
     *
     * @return String such generated zip file name.
     */
    public String getZipFileName()
    {
    	if (zipFileName != null)
    		return zipFileName;
        zipFileName = getFileName();
        zipFileName = zipFileName.replace('.', '_');
        zipFileName = zipFileName + ".zip";
        return zipFileName;
    }
    
    /**
     * Generate a zip file name for this selected file based on
     *  the file name passed in.
     *
     * @param fileName the file name based on which, the 
     *        zip file name will be derived.
     * @return String such generated zip file name.
     */
    public String getZipFileName(String fileName)
    {
        zipFileName = fileName;
        zipFileName = zipFileName.replace('.', '_');
        zipFileName = zipFileName + ".zip";
        return zipFileName;
    }
    
    /*public String getZipFileName()
    {
        return zipFileName;
    }*/
    
    public void setZipFileName(String zipFileName)
    {
        this.zipFileName = zipFileName;
    }
    
    
    /** 
     * To retrieve the file name from file's absolute path.
     * 
     * @return String the selected file name with path depleted. 
     */
    public String getFileName()
    {
        if (fileName != null)
        {
            return fileName;
        }
           
        int index = -1;
        String mark = "\\";
        
        index = absoluteFilePath.indexOf(mark);
        if (index < 0)
        {
            mark = "/";
            index = absoluteFilePath.indexOf(mark);
        }
        
        if (index < 0)
        {
            return absoluteFilePath;
        }   
        int position = index;
            
        while (index > 0)
        {
            index = absoluteFilePath.indexOf(mark, index + 1);
            
            if (index > 0)
            {
                position = index;
            }
        }
        
        fileName = absoluteFilePath.substring(position+1);
        return fileName;
    }
    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }
    
    public void setCrc32(String crc32)
    {
        this.crc32 = crc32;
    }
    
    public String getCrc32()
    {
        return crc32;
    }
    public boolean isZipFile()
    {
    	String tempPath = absoluteFilePath.toLowerCase();
    	
    	int index = tempPath.lastIndexOf(ZIPEXTENSION);
    	if (index == -1)
    		return false;
    		
    	if ((index + ZIPEXTENSION.length() ) == tempPath.length())
    	{
    		return true;
    	} 
    	return false;
    }
}
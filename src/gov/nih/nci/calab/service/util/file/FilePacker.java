/*
The caArray Software License, Version 1.0

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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author zhoujim
 * 
 * This class is for Experiment hybridization batch download. It will search the directory 
 * that programmer specified and pack all same type file that programmer specified into one
 * zip file and let user download.
 *  
 * It will take two parameters as filepath and file type and pack all same type file into 
 * one file.
 */
public class FilePacker
{
    
    private String path;
    
	private Log log_ = LogFactory.getLog(this.getClass());

    /**
     * @param path
     * @param fileType
     */
    public FilePacker(String path)
    {
        this.path = path;
    }
    
    public FilePacker()
    {
        //doing nothing
    }
    
    public boolean packFiles(String[] needToPackFiles,String path)
    {
        boolean isDone = false;
        try
        {
            createPackedFile(needToPackFiles, path);
            isDone = true;
        }
        catch (Exception e)
        {
            isDone = false;
            log_.error("Exception e: " + e.toString());
        }
        
        return isDone;
    }
    
    /**
     * @return true is file packing is done, otherwise false
     */
    public boolean packFiles()
    {
        boolean isDone = false;
        Vector fileNameHolder = new Vector();
        
        removeOldZipFile(path);

        File  f = new File(path);
        String[] fileLists = f.list();
        for(int i = 0; i < fileLists.length; i++ )
        {
             fileNameHolder.addElement(fileLists[i]);
        }
        String[] needToPackFiles = new String[fileNameHolder.size()];
        for(int i = 0; i < needToPackFiles.length; i++)
        {
            needToPackFiles[i] = (String)fileNameHolder.elementAt(i);
        }
        try
        {
            createPackedFile(needToPackFiles, path);
            isDone = true;
        }
        catch (Exception e)
        {
            isDone = false;
            log_.error("Exception e: " + e.toString());
        }
        
        return isDone;
    }

    /**
     * @param path
     * @param fileType
     */
    public void removeOldZipFile(String path)
    {
        File f = new File(path + File.separator + CaNanoLabConstants.ALL_FILES + ".zip");
        if(f.exists())
        {
	        boolean isDeleted = f.delete();
	        if(isDeleted)
	        {
	            log_.info("File " + CaNanoLabConstants.ALL_FILES + ".zip has been deleted");
	        }
	        else
	        {
	            log_.debug("File " + CaNanoLabConstants.ALL_FILES + ".zip cannot be deleted");
	        }
        }
    }

    /**
     * @param needToPackFiles
     * @param path
     * @param fileType
     */
    private void createPackedFile(String[] needToPackFiles, String path)
    throws Exception
    {
        byte[] buf = new byte[1024];
        String outFileName = path + File.separator + CaNanoLabConstants.ALL_FILES + ".zip";
        ZipOutputStream out = null;        
        
        List existingFiles = new ArrayList(); 
        for(int i = 0; i < needToPackFiles.length; i++)
        {
            File file = new File(path + File.separator + needToPackFiles[i]);
            if (!file.exists())
            {
                continue;
            }
            existingFiles.add(file);
        }
        
        if (existingFiles.size() == 0)
        {
            log_.info("File " + outFileName + " could not be created because none of the file entries exist!");
            return;
        }
        try
        {
	        out = new ZipOutputStream(new FileOutputStream(outFileName));
	        out.setLevel(1);
	        for (Iterator iter = existingFiles.iterator(); iter.hasNext();)
            {
	            File file = (File)iter.next();
	            FileInputStream in = new FileInputStream(file);
	            out.putNextEntry(new ZipEntry(file.getName()));
	            int len = in.read(buf);
	            while(len > 0)
	            {
	                out.write(buf,0,len);
	                len = in.read(buf);
	            }
	            out.closeEntry();
	            in.close();
	        }
            out.close();
        }
        catch(Exception e)
        {            
            throw e;
        }
        finally
        {
            	 // nothing
        }
    }

public static void main(String[] args)
{
    boolean isDelete = false;
    File zipFile = new File("C:\\share\\content\\caarray\\caarrayftp\\microarrayfiles\\experimentinput\\1015897540523024\\ALL_CEL.zip");
    if(zipFile.length() == 0)
    {
        //isDelete = zipFile.delete();
    }
    System.out.println("isDelete --- " + zipFile.getName());
}
   

}

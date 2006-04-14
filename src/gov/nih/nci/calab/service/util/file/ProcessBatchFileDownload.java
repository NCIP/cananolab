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

import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author zhoujim
 *
 * JIMTODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ProcessBatchFileDownload extends HttpServlet
{
	private Log log_ = LogFactory.getLog(this.getClass());
    public void doGet(HttpServletRequest request, HttpServletResponse response)
    {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
    {
        
        Vector fileNameHolder = new Vector();
        try
        {
            ObjectInputStream ois = new ObjectInputStream(request.getInputStream());
            String dataTypeAndExperimentId = (String)ois.readObject();
          	ois.close();
        
            StringTokenizer stoken = new StringTokenizer(dataTypeAndExperimentId, "|");
            
            String fileType = stoken.nextToken();
            long expId = Long.parseLong(stoken.nextToken());
        	
            String experimentInputFilePath = FileLocatorUtils.getExperimentInputFileDirectory(new Long(expId));
            
            File  f = new File(experimentInputFilePath);
            String[] fileLists = f.list();
            for(int i = 0; i < fileLists.length; i++ )
            {
                if((fileLists[i].toUpperCase()).indexOf(fileType) >= 0 && !fileLists[i].equalsIgnoreCase("ALL_" + fileType + ".zip"))
                {
                    fileNameHolder.addElement(fileLists[i]);
                }
            }
            String[] needToSendFiles = new String[fileNameHolder.size()];

            for(int i = 0; i < needToSendFiles.length; i++)
            {
                needToSendFiles[i] = (String)fileNameHolder.elementAt(i);
            }
	
            ObjectOutputStream oos = new ObjectOutputStream(response.getOutputStream());
            //send back array of String -- needed files' name
            oos.writeObject(needToSendFiles);
          	oos.flush();
          	oos.close();
        }
        catch (Exception e)
        {
            log_.error("Cannot find files from the file system " + e.toString());
        }
        
    }
    
}

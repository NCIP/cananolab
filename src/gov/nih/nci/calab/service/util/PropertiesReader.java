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

package gov.nih.nci.calab.service.util;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

/**
 * @author doswellj
 */
public class PropertiesReader
{
    /**
     * Logger used by this class.
     */
    protected org.apache.log4j.Logger logger_ =
        org.apache.log4j.Logger.getLogger(this.getClass());
    
    protected String filePath_;
    protected InputStream propInStream_;
    protected Hashtable propertyHash_ = new Hashtable();
    protected boolean loaded_ = false;    
    
    /**
     * 
     * @param fileClasspath - The file path which can be located by Java ClassLoader. 
     */
    public PropertiesReader(String filePath)
    {         
        this.propInStream_ = 
            Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);        
        if (propInStream_ == null)
        {
            logger_.error("Unable to read properties file " + filePath);
        }
        else
        {
            filePath_ = filePath;
        }
    }

    public PropertiesReader(InputStream propInStream)
    {         
        this.propInStream_ = propInStream;
    }


    public void loadProperties()
    {
        Properties props = new Properties();
        try
        {
            props.load(propInStream_);
            loaded_ = true;
            propInStream_.close();
        }
        catch (Exception e)
        {
            logger_.error("Failed to read file " + filePath_, e);
        }

        // Set system properties based on each property just loaded.
        for (Enumeration e = props.propertyNames(); e.hasMoreElements();)
        {
            String propName = (String)e.nextElement();
            String propValue = props.getProperty(propName);
            propertyHash_.put(propName, propValue);
        }        
    }


    public Hashtable getProperties()
    {
        return propertyHash_;
    }


    public String getProperty(String propertyName)
    {
        return (String)getProperties().get(propertyName);
    }
     
    /**
     * @return
     */
    public boolean isLoaded()
    {
        return loaded_;
    }

}

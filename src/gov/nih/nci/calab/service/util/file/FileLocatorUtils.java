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
import gov.nih.nci.calab.service.util.PropertyReader;

import java.io.File;

/**
 * Looks up file locations
 * 
 * @author gustafsons
 * @author tranp
 */
public final class FileLocatorUtils
{
    private static final String ARRAY_DESIGN_MAGEML_FILE_DIRECTORY = "arrayDesignMAGEMLFileDirectory";
    private static final String AFFY_LIBRARY_DIRECTORY = "affyLibraryDirectory";
    private static final String EXPERIMENT_DATA_FILES_DIRECTORY = "experimentDataFilesDirectory";
    private static final String EXPERIMENT_MAGEML_FILE_DIRECTORY = "experimentMAGEMLFileDirectory";
    private static final String GAL_FILE_INPUT_DIRECTORY = "galFileInputDirectory";
    private static final String ARRAY_DESIGN_FILE_INPUT_DIRECTORY = "arrayDesignFileInputDirectory";
    private static final String QUANTITATION_TYPES_FILE_DIRECTORY = "quantitationTypesFileDirectory";
    private static final String DESIGN_ELEMENTS_FILE_DIRECTORY = "designElementsFileDirectory";
    private static final String NETCDF_FILE_OUTPUT_DIRECTORY = "netCDFOutputFileDirectory";
    private static final String EXPERIMENT_INPUT_FILE_DIRECTORY = "experimentInputFileDirectory";
    private static final String ARRAY_DESIGN_FILE_DIRECTORY = "arrayDesignFileDirectory";

    /**
     * Logger used by this class.
     */
    private static org.apache.log4j.Logger logger_ =
        org.apache.log4j.Logger.getLogger(FileLocatorUtils.class);
        

    /**
     * Finds the final directory for placing the arrayDesign's uploaded files 
     * including MAGE-ML and GAL. If not there it makes it.
     */
    public static String getArrayDesignFileDirectory()
    {
        return getOrCreateDirectory(ARRAY_DESIGN_FILE_DIRECTORY);
    }

    /**
     * Finds the logical directory for the arrayDesign files, if not there it 
     * makes it.
     */
    public static String getArrayDesignMAGEMLFileDirectory()
    {
        return getOrCreateDirectory(ARRAY_DESIGN_MAGEML_FILE_DIRECTORY);

    }       
    
    /**
     * Finds the logical directory for the input files, if the directory doesn't  
     * exist, it is created.
     */
    public static String getExperimentInputFileDirectory(Long experimentId)
    {
        return getOrCreateDirectory(EXPERIMENT_INPUT_FILE_DIRECTORY, experimentId + "");
    }


    /**
     * Input directory for Experiment MAGEML upload
     * @return
     */
    public static String getExperimentInputFileDirectory()
    {
        return getOrCreateDirectory(EXPERIMENT_INPUT_FILE_DIRECTORY);
    }
    
    
    public static String getNETCDFOutputFileDirectory(Long experimentId)
    {
        return getOrCreateDirectory(NETCDF_FILE_OUTPUT_DIRECTORY, experimentId + "");
    }
    
    
    /**
     * Locates the file path name for the .nc (NETCDF) file that stores the 
     * the parsed quantification data or BioDataCube for the specified
     * BioAssay contained in the specified Experiment.
     * @param experimentId - The Experiment that contains the specified BioAssay
     * @param bioAssayId - The BioAssay that references the returned NETCDF file
     * contained quantification data.
     */
    public static String getNETCDFFileLocation(Long experimentId, Long bioAssayId)
    {
        if (experimentId == null || bioAssayId == null)
        {
            return null;
        }   
        String netCDFDirectory = getNETCDFOutputFileDirectory(experimentId);
        return getNETCDFFileLocation(netCDFDirectory, bioAssayId);
    }
    
    
    /**
     * Locates the file path name for the .nc (NETCDF) file that stores the 
     * the parsed quantification data or BioDataCube for the specified
     * BioAssay. The NETCDF file is known to be located at the specified
     * directory.
     * 
     * @param directory - The directory at which the NETCDF file is located.
     * @param bioAssayId - The BioAssay that references the returned NETCDF file
     * contained quantification data.
     * @return
     */
    public static String getNETCDFFileLocation(String directory, Long bioAssayId)
    {
        if (directory == null || bioAssayId == null)
        {
            return null;
        }   
        String netCDFFileName =
            directory + File.separator + "bioassay_" + bioAssayId.longValue() + ".nc";
        return netCDFFileName;
    }
    
    

    
 
    /**
     * This method is for release 1.3.1.  When the gal file is uploaded 
     * after an array design is created, the file will be placed in the
     * directory created by this method. There will be only one gal file 
     * in this directory, so file name crash is not an issue.
     * 
     * @param arrayDesignId the array design id, which will be used to name
     *        this subdirectory, under which the gal file is place
     * 
     * @return The full directory path which is created if it doesn't exist.
     */
    public static String getArrayDesignFileInputDirectory(Long arrayDesignId)
    {
        return getOrCreateDirectory(ARRAY_DESIGN_FILE_INPUT_DIRECTORY, arrayDesignId + "");
    }
    
    /**
     * Finds the logical directory for the experimentMAGEML files, if the directory 
     * doesn't exists, creates it.
     *
     * @return The directory path for uploaded experiment MAGEML files.
     */
    public static String getExperimentMAGEMLFileDirectory()
    {
        return getOrCreateDirectory(EXPERIMENT_MAGEML_FILE_DIRECTORY);
    }      
     
     
        
    /**
     * Finds the logical directory for the experimentData files, if not there it 
     * makes it.
     */
    public static String getExperimentDataFilesDirectory(Long experimentId)
    {
        return getOrCreateDirectory(EXPERIMENT_DATA_FILES_DIRECTORY, experimentId + "");
    }
      
      
    public static String getAffyLibraryDirectory()
    {
        return getOrCreateDirectory(AFFY_LIBRARY_DIRECTORY);
    }    


    public static File getLatestModifiedFile(String directory)
    {
        return getLatestModifiedFile(directory, null);
    }
    
    
    public static boolean deleteDirectory(String directory)
    {
        if (directory == null || directory.length() == 0)
        {
            return false;
        }
        File directoryFile = new File(directory);
        if (!directoryFile.exists())
        {
            return false;
        }
        File[] containedFiles = directoryFile.listFiles();
        for (int i = 0; i < containedFiles.length; i++)
        {
            containedFiles[i].delete();
        }
        return directoryFile.delete();
    }
    
    
    /**
     * Retrieves the latest created file in the specified directory.
     * 
     * @param directory - The directory in which the latest created file is retrieved.
     * @param extension - The file extension, either prefixed with dot or not, e.g. .zip, zip.
     * @return The latest modified file or NULL if the directory doesn't exist
     * or is empty.
     */
    public static File getLatestModifiedFile(String directory, String extension)
    {
        if (directory == null)
        {
            return null;
        }
        // check for extension not having dot since we accept both .ext or ext        
        if (extension != null && !extension.startsWith("."))
        {
            extension = "." + extension; 
        }
        File file = null;
        File fileDir = new File(directory);
        if (fileDir.exists())
        {
            File[] files = fileDir.listFiles();
            long greatestLastModified = -1;
            for (int i = 0; i < files.length; i++)
            {
                if (files[i].isFile())
                {
                    if (extension != null 
                        && !files[i].getName().toUpperCase().endsWith(extension.toUpperCase()))
                    {
                        continue;
                    }
                    if (files[i].lastModified() > greatestLastModified)
                    {
                        greatestLastModified = files[i].lastModified();
                        file = files[i];
                    }
                }
            }
        }
        return file;
    }


    private static String getOrCreateDirectory(String propertyName, String lastDirectoryName)
    {
        String path = getOrCreateDirectory(propertyName);
        if (path != null 
            && lastDirectoryName != null && lastDirectoryName.length() > 0)
        {
            path += File.separator + lastDirectoryName;
            checkAndCreateDirs(path);
        }
        else
        {
            logger_.error(propertyName + " not set in caarray.properties");
        }
        return path;
    }     
        
        
    private static String getOrCreateDirectory(String propertyName)
    {
        String propertyFileName = "";
        String path = PropertyReader.getProperty(propertyFileName,propertyName);
        if (path != null)
        {
            checkAndCreateDirs(path);
        }
        else
        {
            logger_.error(propertyName + " not set in caarray.properties");
        }
        return path;
    }     
            

    private static void checkAndCreateDirs(String fileOrDirectoryPathName)
    {
        File file = new File(fileOrDirectoryPathName);
        if (!file.exists())
        {
            file.mkdirs();
        }
    }
    
    
    public final static void main(String[] args)
    {
        //String dir = FileLocatorUtils.getNETCDFOutputFileDirectory(new Long(123)); 
        //FileLocatorUtils.deleteDirectory(dir);
        String netCDFName = FileLocatorUtils.getNETCDFFileLocation(new Long(123), new Long(345));
        System.out.println(netCDFName);
    }
    
}

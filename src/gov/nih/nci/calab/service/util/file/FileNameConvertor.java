package gov.nih.nci.calab.service.util.file;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class is for changing orginal file name to the zipped file name without zip 
 * extension for calab project. Time Stamp will be added in front of file name to avoid
 * the file overwritten.
 *  
 * @author zhoujim
 *
 */
public class FileNameConvertor
{
    private Log log_ = LogFactory.getLog(this.getClass());
    
    public String getConvertedFileName(String orgFileName) throws IOException
    {
        if (orgFileName == null && orgFileName.length() < 1)
        {
            log_.error("Please provide the orginal file name.");
            throw new IOException("Please provide the orginal file name. ");
        }
        String convertedFileName = null;
        int lastIndexOfDot = orgFileName.lastIndexOf(".");
        String nameWithoutZipExt = orgFileName.substring(0, lastIndexOfDot);
        int lastIndexOfUnderScore = nameWithoutZipExt.lastIndexOf("_");
        convertedFileName = nameWithoutZipExt.substring(0, lastIndexOfUnderScore)
                           + "." + nameWithoutZipExt.substring(lastIndexOfUnderScore+1);
        
        return convertedFileName;
    }
    
    /**
     *  For testing only
     * @param args
     */
    public static void main(String[] args)
    {
        try
        {
            FileNameConvertor fileNameConvertor = new FileNameConvertor();
            String name = fileNameConvertor.getConvertedFileName("");
            System.out.println("Converted File Name : " + name);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

package gov.nih.nci.calab.service.util.file;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class is only for uncompressing a zipped file
 * 
 * @author zhoujim
 *
 */
public class FileUnzipper
{
    private Log log_ = LogFactory.getLog(this.getClass());

      public void copyInputStream(InputStream in, OutputStream out)
      throws IOException
      {
        byte[] buffer = new byte[1024];
        int len;

        while((len = in.read(buffer)) >= 0)
          out.write(buffer, 0, len);

        in.close();
        out.close();
      }

      /**
       * This is method perforems unzip a file action. It will take a zip file name, and unzip 
       * to the user specified directory.
       * 
       * NOTE: zip file name must be specified with full path.
       *   
       * @param zippedFileName
       * @param unzippedFilePath
       * @throws IOException
       */
      public void unzip(String zippedFileName, String unzippedFilePath) throws IOException {
        Enumeration entries;
        ZipFile zipFile;

        if (zippedFileName.length() == 1 && zippedFileName.length() < 1 )
        {
            log_.error("Please provide zip file name.");
            throw new IOException("Please provide zip file name.");
        }

          zipFile = new ZipFile(zippedFileName);

          entries = zipFile.entries();

          while(entries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry)entries.nextElement();

            log_.info("Extracting file: " + entry.getName());
            //create a new file name associated with the zipped file name
            
            copyInputStream(zipFile.getInputStream(entry),
               new BufferedOutputStream(new FileOutputStream(new File(unzippedFilePath, entry.getName()))));
          }

          zipFile.close();
      }
}

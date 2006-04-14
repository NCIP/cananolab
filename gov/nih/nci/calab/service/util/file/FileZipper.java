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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * This object creates a zip file containing the set of files within a
 * SelectedFiles object. One can then get an InputStream for the zip file
 * to use for transfering via FTP. This object also contains an unzip 
 * method to extract the files from the zip file. There is also support
 * for determining how much of the zipping has been done (ie number of bytes
 * read from the source file)
 * 
 *  Typical use would be:
 *   FileZipper myZipper = new FileZipper();
 *   myZipper.setFiles(mySelectedFiles);  //this method blocks until the zip file is created
 *   sendToServer(myZipper.getInputStream());
 * 
 * on server:
 *   Zipper.unZip(unpackDirectory, fullyQualifiedZipFileName) 
 * 
 * @author L. Schuler 5/5/2004
 * @author TranP
 *
 */
public class FileZipper
{
    /**
     * Logger used by this class.
     */
    private static org.apache.log4j.Logger logger_ =
        org.apache.log4j.Logger.getLogger(FileZipper.class);

    /** boolean T/F if this object is ready to send data via
     * the inputStream	 */
    private boolean iAmReady = false;

    /** The zip file created within this class **/
    private File zipFile;

    /** The files to be placed into the zip file **/
    private File[] mySelectedFiles = null;

    /** The number of bytes read from the source file sent to the 
     * zipping process */
    private long bytesCompressed = 0;

    /** flag T/F if zipping is currently in progress */
    private boolean isCompressing = false;

    /** an internal copy buffer size */
    private static final int BUFFER_SIZE = 1024;

    /** 
     *  
     */
    public FileZipper()
    {
    }

    /** 
     * Method to get the name of the zip file. Since it is a 
     * java temp file, the exact name is not known unless this is called.
     * It shouldn't really be needed, but just in case...
     * 
     * @return The name of the zip file
     */
    public String getName()
    {
        return zipFile.getName();
    }

    /**
     * This method establishes the set of files to be put into the
     * zip file, and then actually places them in there. Be warned, this
     * method will block until the temporary zip file has been 
     * filled with the files-which could take a while if there are large
     * data files to zip.
     * 
     * @param files the files to place into the zip file
     * @param sessIdent the session Identifier. used to construct the path in the zip file.
     * @throws Exception if an error occurs during zip building
     */
    public void setFiles(String sessIdent, File[] files) throws Exception
    {
        mySelectedFiles = files;
        bytesCompressed = 0;
        isCompressing = true;
        // create a temp zip file
        try
        {
            zipFile = File.createTempFile("sub", ".zip");
        }
        catch (Exception eee)
        {
            throw new Exception("Unable to create temporary zip file " + eee.getMessage());
        }
        try
        {
            //open the zip file
            FileOutputStream fos = new FileOutputStream(zipFile);
            ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(fos));
            System.out.println("temp zip file=" + zipFile.getName());

            // go through all the files

            for (int i = 0; i < mySelectedFiles.length; i++)
            {
                String theFileName = mySelectedFiles[i].getAbsolutePath();
                System.out.print("File " + theFileName);
                FileInputStream fileIn = new FileInputStream(mySelectedFiles[i]);
                ZipEntry entry = new ZipEntry(theFileName);
                zipOut.setMethod(ZipOutputStream.DEFLATED);
                zipOut.putNextEntry(entry);
                // why re-invent the wheel :)
                copyStream(fileIn, zipOut, this);
                fileIn.close();

            }
            zipOut.flush();
            zipOut.close();
        }
        catch (Exception e)
        {
            throw new Exception("Unable to compress files " + e.getMessage());
        }
        isCompressing = false;
        iAmReady = true;
    }

    /**
     * Get the size of the zip file created by this class
     * 
     * @return the size of the temp zip file, 0 if not created yet
     */
    public long getZipFileSize()
    {
        if (zipFile != null)
            return (long)zipFile.length();
        return 0;
    }

    /**
     * callback method for copy util. called when a new chunk of data has been moved
     * 
     * @param total the total number of bytes transfered in this copy call
     * @param lastchunk the number of bytes transferred since the last callback
     */
    private void bytesTransferred(long total, int lastchunk)
    {
        bytesCompressed += lastchunk;
    }

    /**
     * return the number of bytes of source dtata files that have been sent to the
     * zip compression function.
     * 
     * @return number of bytes from the source files that have been compressed
     */
    public long getBytesCompressed()
    {
        return bytesCompressed;
    }

    /** 
     * Is this object currently compressing data files ?
     * 
     * @return T/F if compression is currently in preogress.
     */
    public boolean isCompressing()
    {
        return isCompressing;
    }

    /**
     * unzip a file relative to the specified directory
     * 
     * @param targetDirectory the directory to unzip the files into
     * @param zipFilePath the zip file to unzip
     * @return An array of file names, without parent path, contained in the zip file.
     */
    public final static String[] unZip(String targetDirectory, String zipFilePath)
        throws IOException
    {
        ArrayList containedFileNames = new ArrayList();
        try
        {
            BufferedOutputStream dest = null;
            BufferedInputStream is = null;
            ZipEntry entry = null;
            ZipFile zipfile = new ZipFile(zipFilePath);
            Enumeration e = zipfile.entries();
            while (e.hasMoreElements())
            {
                entry = (ZipEntry)e.nextElement();
                logger_.debug("Extracting from zip file " + zipFilePath + " entry " + entry);

                String fileName = targetDirectory + "/" + entry.getName();
                if (!"/".equals(File.separator))
                {
                    fileName.replace('/', File.separatorChar);
                }
                File outFile = new File(fileName);
                outFile.getParentFile().mkdirs();
                is = new BufferedInputStream(zipfile.getInputStream(entry));
                dest = new BufferedOutputStream(new FileOutputStream(outFile));
                FileZipper.copyStream(is, dest, null);

                containedFileNames.add(entry.getName());
                
                dest.flush();
                dest.close();
                is.close();
                logger_.debug("Done extracting zip file " + zipFilePath + ".");
            }
        }
        catch (IOException e)
        {
            String message = "Unable to unzip file " + zipFilePath + " to directory " +
                targetDirectory + ". The file may be being written: " + e;
            logger_.warn(message);
            throw e;
        }
        return (String[])containedFileNames.toArray(new String[containedFileNames.size()]);
    }

    /**
     * Creates a zip file and adds the specified <code>fileNames</code> to it. tranp
     * 
     * @param zipFileName
     * @param fileNames
     * @param noPath - True to indicate all files are put at the root of the zip files.
     * @throws IOException
     */
    public static void createZip(String zipFilePathName, String[] filePathNames, boolean noPath)
        throws IOException
    {
        // Create a buffer for reading the files
        byte[] buf = new byte[1024];
        // Create the ZIP file
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFilePathName));
        // Compress the files
        for (int i = 0; i < filePathNames.length; i++)
        {
           
            File file =  new File(filePathNames[i]) ;
            if(file.exists()){
                FileInputStream in = new FileInputStream(filePathNames[i]);
                String entryName = filePathNames[i];
                if (noPath)
                {
                    int separatorPos = entryName.lastIndexOf(File.separator);
                    if (separatorPos > 0)
                    {
                        entryName = entryName.substring(separatorPos + 1);
                    }
                }
            
                // Add ZIP entry to output stream.
                out.putNextEntry(new ZipEntry(entryName));
                // Transfer bytes from the file to the ZIP file
                int len;
                while ((len = in.read(buf)) > 0)
                {
                    out.write(buf, 0, len);
                }

                // Complete the entry
                out.closeEntry();
                in.close();
            }
        }
        // Complete the ZIP file
        out.close();
    }

    /**
     * Checks if the specified file is a compressed zip file. The file name
     * must end with case-insensitive .zip or .jar.
     * 
     * @param pathName
     * @return
     */
    public final static boolean isZipFile(String pathName)
    {
        if (pathName == null 
            || !(pathName.endsWith(".zip") || pathName.endsWith(".jar")
                 || pathName.endsWith(".ZIP") || pathName.endsWith(".JAR")))
        {
            return false;
        }
        
        
        boolean isZip = false;
        ZipFile zipFile = null;
        try
        {
            zipFile = new ZipFile(pathName);
            isZip = true;
        }
        catch (Exception e)
        {
            logger_.warn("File " + pathName + " is NOT a zip file or can't be read.");
            isZip = false;
        }
        finally
        {
            try
            {
                if (zipFile != null)
                    zipFile.close();
            }
            catch (Exception ignored)
            {
            }
        }
        return isZip;
    }

    /**
     * Shamelessly pilferred from the apache.common.net suite. It wasn't trivial to just use
     * it in place, so I ripped the code out and modified it for my purposes. Thanks guys.
     * 
     * This method copys data from the input source to the output source until EOF. After 
     * a chunk of data is moved, it calls back the Zipper class "bytesTransferred" method 
     * to keep count of the progress.
     * 
     * @param source The datainput stream
     * @param dest the data output stream
     * @param listener the callback object to inform of data progress
     * @return the total number o bytes moved
     * @throws Exception if there is an IOEception while copying
     */
    private static final long copyStream(InputStream source,
                                         OutputStream dest,
                                         FileZipper listener)
        throws IOException
    {
        int bytes;
        long total;
        byte[] buffer;
        buffer = new byte[BUFFER_SIZE];
        total = 0;
        try
        {
            while ((bytes = source.read(buffer)) != -1)
            {
                // Technically, some read(byte[]) methods may return 0 and we cannot
                // accept that as an indication of EOF.
                if (bytes == 0)
                {
                    bytes = source.read();
                    if (bytes < 0)
                        break;
                    dest.write(bytes);
                    dest.flush();
                    ++total;
                    if (listener != null)
                        listener.bytesTransferred(total, 1);
                    continue;
                }

                dest.write(buffer, 0, bytes);
                dest.flush();
                total += bytes;
                if (listener != null)
                    listener.bytesTransferred(total, bytes);
            }
        }
        catch (IOException e)
        {
            throw new IOException("IOException caught while copying " + total + ": " + e);
        }

        return total;
    }

    /**
     * Use this for testing.
     * 
     * @param args
     */
    public static void main(String[] args)
    {
    }
}

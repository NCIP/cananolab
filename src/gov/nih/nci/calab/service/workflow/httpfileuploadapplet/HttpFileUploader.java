/**
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


import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.MultipartPostMethod;


/**
 * This object is the central part of the file upload implementation.  It
 * zips all files to be uploaded in one to one relationship before the upload
 * process is started.  It contains three ways to do the actual uploading job:
 * using java.net.Socket, java.net.URLConnection and Apatche Jakatar's HttpClient.
 * For the current release HttpClient is used as the way for file upload, since
 * it can upload file as large as up to 300 mk.
 */

public class HttpFileUploader
{
    /**
     * The String constant for the line breakers.
     */
	private static final String NEWLINE = "\r\n";

    /**
     * The number as 60 second in a minute.
     */
    private static final int MINUTE = 60;

    /**
     * The number as 3600 second in an hour.
     */
    private static final int HOUR = 3600;

    /**
     * The number as 86400 second in a day.
     */
    private static final int DAY = 86400;

    /**
     * The conversion factor from millisecond to second.
     */
    private static final int MILLIFACT = 1000;

    /**
     * The container for files user selected for uploading.
     * Valid data type is HttpUploadFile.
     */
    private Vector files = null;

    /**
     * The bean holds all parameters passed from the server.
     */
    private HttpUploadParameters up = null;

    /**
     * The boolean variable used as a flag for zipping. True for
     * zipping completed, otherwise not completed.
     */
    private volatile boolean isZipped = false;

    /**
     * The boolean value used as a flag to indicate the uploading
     * process is failed completed, but not partially.  The situations
     * that lead to its true value are the following: zipping fails completely,
     * and fails to notify server upon upload completion leading to no file
     * parsing senario.
     */
    private volatile boolean isFailed = false;

    /**
     * The boolean value used as a flag to indicate that the
     * selected file is legal when input file is a zip file. 
     */ 
    private volatile boolean isIllegalInputFile = false;

    /**
     * The boolean value used as a flag to indicate that user
     * requested a stop action. 
     */   
    private volatile boolean isStopped = false;
    /**
     * The boolean value used as a flag to indicate the uploading process is
     * completed or not.  it seems this variable is not needed.
     */
    //private boolean isComplete = false;

    /**
     * The String value holds the exception error message.  This value changes
     * all the times, as long as error occurs all the times.  Howerver, not all
     * of its values will be displayed by the applet. Only those caught by the
     * Applet's timer action event will be dilivered to the applet for display.
     */
    private String failureMessage = null;

    /**
     *an internal copy buffer size.
     */
    private static final int BUFFER_SIZE = 1024; //4096;

    /**
     * The String value holds the file name being uploaded.  This value changes
     * all the times, as many times as the total number of files to be uploaded.
     * Howerver, not all of its values will be displayed by the applet.
     * Only those caught by the Applet's timer action event will be dilivered
     * to the applet for display.
     */
    private String currentFileName = "";

    /**
     * The String value holds the reply from the server upon notify.
     */
    private String notifyReply = null;

    /**
     * The total bytes sent to server. This variable increases every time
     * a file is uploaded to the server. It is used to calculate upload
     * percetage used by the progress bar of the applet, it is also part of
     * the upload summary data.
     */
    private long bytesSent = (long)0;

    /**
     * The file length for the file being uploaded.
     */
    private long fileLength = (long)0;

    /**
     * The sum of all file lengths used for percentage calculation used by the
     * progress bar of the applet.
     */
    private long totalSize = (long)0;

    /**
     * The NumberFormat used to format a float number into a fixed string
     * representation.
     */
    NumberFormat formater = null;

    /**
     * The container holds files that have been uploaded successfully. Valid
     * data type for it: HttpUploadFile.
     */
    Vector okFiles = new Vector();

    /**
     * The container holds files that failed to be uploaded. Valid
     * data type for it: HttpUploadFile.
     */
    Vector failFiles = new Vector();

    /**
     * The long as a time stamp taken at the start of upload process.
     */
    long startTime = (long)0;

    /**
     * The long as a time stamp taken at the end of upload process.
     */
    long endTime = (long)0;

    /**
     * The String that is the temporary file path used for zipping. If
     * this value fails to be set, then the upload process will be set as
     * isFailed true.
     */
    String tempFilePath = null;

    /**
     * The array of chars represents the illegal characters
     * for the Unix file naming system.
     */
    char[] pattern = { ' ', '/', '\\', '|', ';', ',', '!', '@',
            '(', ')', '<', '>',   '\"', '#', '$',
            '\'', '~', '{',  '}', '[', ']', '=',
            '+',  '&', '^',   '\t'};

    /**
     * The array of String represents the permissible file extensions
     * derived from HttpUploadParameters's permissibleFileExtension field.
     */
    String[] fileExtensions = null;
    
    /**
     * constructor
     *
     * @param files The Vector containing a batch of file names to be uploaded.
     * @param up the HttpUploadParameters object encapsulating all the parameters
     *        passed from the server to the applet.
     */
    public HttpFileUploader(Vector files, HttpUploadParameters up)
    {
        this.files = files;
        this.up = up;
        formater = NumberFormat.getNumberInstance();
        formater.setMaximumFractionDigits(0);
        if ("arrayDesign".equalsIgnoreCase(up.getModule()))
        {
        	fileExtensions = up.getPermissibleFileExtension().split("_");
		}
    }

    public boolean isZipped()
    {
        return isZipped;
    }


    /**
     * Zip files into zip file one at a time.
     */
    public void zipFiles()
    {
        if (files == null)
        {
            return;
        }

        //We need this, because the tempfile name created in this way will
        //be used by HttpClient, but it is not the name we desire to use.
        if (tempFilePath == null)
        {
            try
            {
                File tempFile = File.createTempFile("dummy", ".zip");

                tempFilePath = getFilePath(tempFile.getAbsolutePath());
                tempFile.delete();
            }
            catch (Exception e)
            {
                //Only failed to obtain a temp file path, we set isFailed to true.
                isFailed = true;
                failureMessage = e.getMessage();
            }
        }


        //If no tempfile path obtained, then there is no point to continue.
        if (isFailed)
        {
            return;
        }

        int i = 0;
        for (Iterator it = files.iterator(); it.hasNext();)
        {
        	//We will stop zipping process if users requested
        	//a stop action.
        	if (isStopped)
        	{
        		isFailed = true;
        		return;
        	}
        	if (isIllegalInputFile)
        	{
        		isFailed = true;
        		return;
        	}
            HttpUploadFile uf = (HttpUploadFile)it.next();
            //Let's check if this is a zip file
            if (uf.isZipFile())
            {
            	/*
            	String newName = replaceIllegalCharacter(uf.getFileName());
            	//The file name is legal
            	if (newName == null)
            	{
            		uf.setZipFileName(uf.getFileName());
            	}
            	//The file name changed to make it legal
            	else
            	{
            		uf.setZipFileName(newName);
            	}
            	
            	uf.setZipFilePath(uf.getAbsoluteFilePath());
            	*/
            	boolean illegalFileInZip = false;
            	boolean illegalFileNumInZip = false;
            	
            	//Check the zip file contents.
            	try
            	{
            		File f = new File(uf.getAbsoluteFilePath());
            		ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(f));
            		String zipEntryName = null;
            		ZipEntry zEntry = zipInputStream.getNextEntry();
            		int count = 0;
            		while (zEntry != null)
            		{
            			zipEntryName = zEntry.getName();
            			
            			illegalFileInZip = checkFileType(zipEntryName);
            			
            			if (illegalFileInZip)
            				break;
            			else
            				zEntry = zipInputStream.getNextEntry();
            			count++;
            		}
					
					if (!illegalFileInZip)
            			illegalFileNumInZip = checkFileNumber(count);
            		
                    //long timeS = System.currentTimeMillis();
                    if (!illegalFileInZip && !illegalFileNumInZip)
                    {
                    	String crc32 = AppletValidatorCodeGenerator.getValidatorCode(f);
                    	if (crc32 != null)
                    	{
                	    	uf.setCrc32(crc32.toString());
                    	}
                    	i++;
                    }
                    else
                    {
                    	isIllegalInputFile = true;
                		failFiles.add(uf.getAbsoluteFilePath());
                		if (illegalFileInZip)
                			failureMessage = "The zip file contains illegal file types. ";
                			
                		if (illegalFileNumInZip)
                		{
                			if (failureMessage == null)
                				failureMessage = "The zip file contains illegal number of files.";
                			else
                			    failureMessage += " The zip file contains illegal number of files.";
                		}
                		isFailed = true;
                		return;
                    }
            	}
            	catch (Exception e)
            	{
                	failFiles.add(uf.getAbsoluteFilePath());
                	failureMessage = e.getMessage();
            	}
            }
            //else
            //{
            //No matter the file is zip file or not, we zip it anyway to
            //preserved original file name.
                try
                {
                    zipFile(uf);
                
                    //Now generate CRC32 value.
                    File justZipped = new File(uf.getZipFilePath());
                
                    //long timeS = System.currentTimeMillis();
                    String crc32 = AppletValidatorCodeGenerator.getValidatorCode(justZipped);
                    //long timeE = System.currentTimeMillis();

                    if (crc32 != null)
                    {
                	    uf.setCrc32(crc32);
                    }
                    else
                    {
                	    uf.setCrc32("0");
                    }
                
                    i++;
                }
                catch (Exception e)
                {
                    failFiles.add(uf.getAbsoluteFilePath());
                    failureMessage = e.getMessage();
                }
            }
        //}

        //at least one file is zipped.
        if (i > 0)
        {
            isZipped = true;
        }
        else
        {
            isFailed = true;
        }
    }


    /**
     * This method puts a single file into a zip format. The zip file is created in a
     * temporary director.
     *
     * @param uf The HttpUploadFile object containing all the information about the
     *          file to be uploaded.
     * @throws Exception if an error occurs during zip building
     */
    public void zipFile(HttpUploadFile uf)
        throws Exception
    {
        // create a temp zip file
        File tempFile = null;
        String newName = null;
        try
        {
            newName = replaceIllegalCharacter(uf.getFileName());

            String path = null;
            if (newName == null)
                path = tempFilePath + "\\" + uf.getZipFileName();
            else
            {
                path = tempFilePath + "\\" + uf.getZipFileName(newName);
            }
            tempFile = new File(path);
            tempFile.createNewFile();
            File parent = tempFile.getParentFile();
            if (!parent.exists())
            {
                tempFile.mkdirs();
            }
        }
        catch (Exception eee)
        {
            throw new Exception(
                "Unable to create temporary zip file " + eee.getMessage());
        }
        try
        {
            //open the zip file
            FileOutputStream fos = new FileOutputStream(tempFile);
            ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(fos));

            String fileName = uf.getAbsoluteFilePath();

            FileInputStream fis = new FileInputStream(fileName);

            ZipEntry entry = null;
            
            //Put the original file name into the zip file.
            entry = new ZipEntry(uf.getFileName());
            
            if (newName == null)
            {
               uf.setFileName(newName);
            }

            zipOut.setMethod(ZipOutputStream.DEFLATED);
            zipOut.setLevel(1);
            zipOut.putNextEntry(entry);

            byte[] byteBuff = null;

            int numBytes = 0;
            byteBuff = new byte[BUFFER_SIZE];

            while((numBytes = fis.read(byteBuff)) != -1)
            {
                zipOut.write(byteBuff, 0, numBytes);
            }
            uf.setZipFilePath(tempFile.getAbsolutePath());

            fis.close();
            zipOut.flush();
            zipOut.closeEntry();

            zipOut.close();
        }
        catch (Exception eee)
        {
            throw new Exception("Unable to compress files " + eee.getMessage());
        }
    }
    /*
     * Retreive the parent directory path for a file from this file'
     * absolute path. return null if no path available.
     */
    /*
    private String getParentPath(String path)
    {
        int index = path.lastIndexOf("\\");
        if (index != -1)
        {
            return path.substring(0, index);
        }
        return null;
    }
    */

    /**
     * Replace any characters illegal for the Unix file naming
     * convention with an underscore.
     * @param fileName which may contain illegal characters.
     * @return String file name which illegal characters being
     *         replaced.
     */
    private String replaceIllegalCharacter(String fileName)
    {
        int index = -1;
        boolean isChanged = false;
        for (int i = 0; i < pattern.length; i++)
        {
            index = fileName.indexOf(pattern[i]);

            if (index != -1)
            {
                fileName = fileName.replace(pattern[i], '_');
                isChanged = true;
            }
        }
        if (isChanged)
        {
            return fileName;
        }
        else
        {
            return null;
        }
    }

    /**
     * This method is called to removed all temporary zip files after they
     * have been uploaded from the temp directory.
     *
     * @param files The Vector contains all files to be cleaned.
     */
    public void cleanUp(Vector files)
    {
        this.files = files;
        this.cleanUp();
    }

    /**
     * This method is the actual implementation of the cleanup process.
     *
     */
    public void cleanUp()
    {
        if (files == null)
        {
            return;
        }
        for (Iterator it = files.iterator(); it.hasNext();)
        {
            HttpUploadFile uf = (HttpUploadFile)it.next();

            if (uf.getZipFilePath() == null || uf.getZipFilePath().equals(uf.getAbsoluteFilePath()))
            {
                continue;
            }

            File file = new File(uf.getZipFilePath());
            if (file.exists())
            {
                file.delete();
            }
        }

        okFiles.clear();
        failFiles.clear();

        startTime = (long)0;
        endTime = (long)0;
        bytesSent = (long)0;
    }

    /**
     * Returns error message derived from the Exception thrown during the uploading
     * process at the time of this method is called.
     *
     * @return String the error message currently assigned to this variable.
     */
    public String getFailureMessage()
    {
        return failureMessage;
    }

    /**
     * Returns the value isFailed set dynamically during the cause of file upload.
     *
     * @return boolean True if upload is so far so good, otherwise false to indicate
     *         error occurance.
     */
    public boolean isFailed()
    {
        return isFailed;
    }

    /**
     * Simply returns a string value received from server as part of response.
     * The value will be displayed by the applet.
     *
     * @return an String value returned by the server as part of the response.
     */
    public String getNotifyReply()
    {
        return notifyReply;
    }

    /**
     * Returns all uploaded files to applet so applet can display them
     * as part of the summary popup window.
     *
     * @return an Object array that encompasses all uploaded file names.
     */
    public Object[] getOkFiles()
    {
        return okFiles.toArray();
    }

    /** returns all failed files to applet so applet can display them
     * as part of the summary popup window.
     *
     * @return an Object array that encompasses all failed file names.
     */
    public Object[] getFailFiles()
    {
        return failFiles.toArray();
    }

    /** Simply return the current bytsSent value used as part of upload summary.
     *
     * @return long the bytes sent to the server.
     */
    public long getBytesSent()
    {
        return bytesSent;
    }

    /**
     * Calculate the day, hour, minute and second used for uploading
     * this batch of files, and compose a StringBuffer to be used by
     * the applet as part of the summary content.
     *
     * @return StringBuffer the composed time duration string buffer.
     */
    public StringBuffer getDurationString()
    {
        long diff = endTime - startTime;

        int timeDiff = (int)(diff/MILLIFACT);

        int day = -1;
        int hour = -1;
        int minute = -1;
        int second = -1;

        day = timeDiff/DAY;

        if (day == 0)
        {
            hour = timeDiff/HOUR;
        }
        else
        {
            timeDiff = timeDiff%DAY;
            hour = timeDiff/HOUR;
        }

        if (hour == 0)
        {
            minute = timeDiff/MINUTE;
        }
        else
        {
            timeDiff = timeDiff%HOUR;
            minute = timeDiff/MINUTE;
        }

        if (minute == 0)
        {
            second = timeDiff;
        }
        else
        {
            second = timeDiff%MINUTE;
        }

        StringBuffer sb = new StringBuffer();

        sb = appendValue(sb, day, "day");
        sb = appendValue(sb, hour, "hour");
        sb = appendValue(sb, minute, "minute");
        sb = appendValue(sb, second, "second");

        //If less than a second, a generic time string
        if (sb.length() == 0)
        {
            sb.append("less than a second");
        }

        return sb;
    }

    /**
     * This utility method is used to append a time value and its unit onto
     * a StringBuffer to compose a string statement, so the final statement
     * conforms to English grammar.
     *
     * @param sb StringBuffer, to which the time value and unit name will be
     *     appended.
     * @param time The integer value of a time interval.
     * @param token The string token as the unit name of this time, as days,
     *     hours, minutes and seconds.
     * @return StringBuffer for the statement.
     */
    private StringBuffer appendValue(StringBuffer sb, int time, String token)
    {
        if (time > 1)
        {
            sb.append(time);
            sb.append(" ");
            sb.append(token);
            sb.append("s ");
        }
        else
        {
            if (sb.length() > 0)
            {
                sb.append(time);
                sb.append(" ");
                sb.append(token);
                sb.append(" ");
            }
            //Otherwise do not say like 0 minute.
            else
            {
            	if (time == 1)
            	{
                    sb.append("1 ");
                    sb.append(token);
                    sb.append(" ");
                }
            }
        }

    	return sb;
    }

    /**
     * To calculate the percentage of upload that is completed. Due to the nature
     * of upload through Http protocol, it is not possible to obtain the actual
     * bytes of data being sent at a particular time point, So this calculation
     * is based on the files that have been uploaded divided by the sum
     * of the sizes of all files.
     *
     * @return the percentage number that is completed.
     */
    public int getPercentage()
    {
        if (totalSize == 0)
        {
            return 0;
        }

        float percent = (float)((double)bytesSent/(double)totalSize);

        percent = percent*100;

        return Integer.parseInt(formater.format(percent));
    }

    /**
     * This method is called by the thread to pass the current uploading file
     * name to the applet for display, so the user knows what file is being uploaded.
     *
     * @return String the file name whose bytes are being transfered to the server.
     */
    public String getCurrentFileName()
    {
        return currentFileName;
    }


    /**
     * This method is called to calculate the total bytes of all zipped files
     * to be uploaded to be used by the applet's progress bar.
     */
    private void countFileTotalSize()
    {
        totalSize = (long)0;

        for (Iterator it = files.iterator(); it.hasNext();)
        {
            HttpUploadFile uf = (HttpUploadFile)it.next();
            File f = new File(uf.getZipFilePath());
            if (f.exists())
            {
                totalSize += f.length();
            }
        }
    }

    //------------- THE HEART OF THE PROGRAM ------------------------------
    /**
     * The method using URLConnection to upload files.  The largest files
     * this method can handle is about 32 mb. The method always returns
     * true after all files are uploaded regardless of success or failure.
     * Failed ones are kept in the Vector failFiles.
     * NOTE: DO NOT REMOVE THIS METHOD IN CASE OF THE FAILURE OF OTHER METHODS.
     *
     * @return boolean true. Always true in this case when the file loop is over.
     */
    public boolean uploadFilesViaURLConnection()
    {
        URL url = null;
        URLConnection urlConn = null;
        DataOutputStream out = null;
        BufferedReader br = null;
        String query = up.getUploadURLWithSid();
        String boundary = "-----------------------------" + getRandomString();

        //Count the total size of the files to be uploaded.
        countFileTotalSize();
        bytesSent = (long)0;
        fileLength = (long)0;
        okFiles.clear();
        failFiles.clear();
        startTime = System.currentTimeMillis();

        //A flag to tell server that this is a fresh upload start, so server
        //will reset isStopped flag to false if the there is a uploaded process
        //that is stopped by the user.
        boolean isFirst = true;

        ////Upload will go on if some of them fail.
        for (Iterator it = files.iterator(); it.hasNext();)
        {
            HttpUploadFile uf = (HttpUploadFile)it.next();
            try
            {
                File f = new File(uf.getZipFilePath());
                fileLength = f.length();
                currentFileName = uf.getAbsoluteFilePath();
                url = new URL(query + this.getUploadQueryString(uf, isFirst));

                urlConn = url.openConnection();
                setConnectionProperty(urlConn, boundary, true);

                // Retrieve OutputStream For upload (Post).
                out = new DataOutputStream(urlConn.getOutputStream());

                //Create header string for the request.
                out.writeBytes(this.getHttpConnHeaderString(uf, boundary));
                this.httpUploadFileStream(new FileInputStream(f), out);

                //Create end string for the request
                out.writeBytes(this.getTailString(boundary).toString());
                out.flush();

                //Read in the response string from the server.
                br = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

                bytesSent += fileLength;
                out.close();
                urlConn = null;
                okFiles.add(currentFileName);

                if (isFirst)
                {
                    isFirst = false;
                }
            }
            catch (Exception e)
            {
                failFiles.add(currentFileName);
                failureMessage = e.getMessage();
            }
        } //END OF FOR LOOP

        endTime = System.currentTimeMillis();

        return true;
    }


    /**
     * Implementation of the file upload using Apatche's HttpClient framework.  This
     * can handle file up to 200 mb.  The overhead is that the applet jar file must include
     * HttpClient jar file, logging jar file and encryption jar file. The method always returns
     * true after all files are uploaded regardless of success or failure. Failed ones are
     * kept in the Vector failFiles.
     *
     * @return boolean true.
     */
    public boolean uploadFilesViaHttpClient()
    {
        //Count the total size of the files to be uploaded.
        countFileTotalSize();
        bytesSent = (long)0;
        fileLength = (long)0;
        okFiles.clear();
        failFiles.clear();
        startTime = System.currentTimeMillis();

        HttpClient client = null;

        //A flag to tell server that this is a fresh upload start, so server
        //will reset isStopped flag to false if the there is a uploaded process
        //that is stopped by the user.
        boolean isFirst = true;

        //Upload will go on if some of them fail.
        for (Iterator it = files.iterator(); it.hasNext();)
        {
        	//The loop continues only no stop action is 
        	//requested by the user.
        	if (isStopped)
        	{
        		break;
        	}
            HttpUploadFile uf = (HttpUploadFile)it.next();

            try
            {
                File f = new File(uf.getZipFilePath());
                fileLength = f.length();

                currentFileName = uf.getAbsoluteFilePath();

                MultipartPostMethod filePost = new MultipartPostMethod(up.getUploadURLWithSid()); //getUploadURL());

                filePost.addParameter(f.getName(), f);
                filePost.setQueryString(getUploadQueryString(uf, isFirst));
                client = new HttpClient();
                //client.setConnectionTimeout(5000);

                /* //The following commented code is valid for HttpClient 3.0.
                PostMethod filePost = new PostMethod(up.getUploadURL());
                filePost.setQueryString(getUploadQueryString(uf));
                Part[] parts = {new FilePart(f.getName(), f)};
                filePost.setRequestEntity(new MultipartRequestEntity(parts, filePost.getParams()));
                */
                int status = client.executeMethod(filePost);
                
                String res = filePost.getResponseBodyAsString();

                filePost.releaseConnection();
                
                //Now to turn off this flag
                if (isFirst)
                {
                    isFirst = false;
                }
                //Check to see the session is expired on the server side.
                //If server side is ok, the response string is only two bytes
                //long.  So do checking only the res is longer than 5 bytes to be safe.
                if (res != null && res.length() > 5)
                {
                    //As long as response body contains message, we check
                    //for expiration information since the servlet may 
                    //throw an IOException when writing file to the file system.

                    int index = res.indexOf("expired");

                	if (index != -1)
                	{
                		isFailed = true;
                		failureMessage = res;
                		break;
                	}
                	//File is not saved by the system, but
                	//the loop goes on.
                	index = res.indexOf("system failed");
                	if (index != -1)
                	{
                		failureMessage = res;
                		failFiles.add(currentFileName + ": \n      " + res);
                        failFiles.add(currentFileName);
                		continue;
                	}
                }
                bytesSent += fileLength;

                okFiles.add(currentFileName);
            }
            catch (Exception e)
            {
                failFiles.add(currentFileName);
                failureMessage = e.getMessage();
            }
        } //END OF FOR LOOP

        endTime = System.currentTimeMillis();

        return true;
    }

    /**
     * The imploemetation of file upload using java.net.Socket. This method can
     * upload file up to 120 mb. The method always returns true after all files
     * are uploaded regardless of success or failure. Failed ones are
     * kept in the Vector failFiles. EXCEPTION: if the first try block fails, then
     * it returns false, and no file shall be uploaded at all.
     * NOTE: DO NOT REMOVE THIS METHOD IN CASE OF THE FAILURE OF OTHER METHODS.
     *
     * @return boolean true if upload successful, otherwise false.
     */
    public boolean uploadFilesViaSocket()
    {
        Socket sock = null;
        DataOutputStream out = null;
        BufferedReader br = null;
        URL url = null;

        //Count the total size of the files to be uploaded.
        countFileTotalSize();
        bytesSent = (long)0;
        fileLength = (long)0;
        okFiles.clear();
        failFiles.clear();
        startTime = System.currentTimeMillis();

        try
        {
            url = new URL(up.getUploadURLWithSid());
            sock = new Socket(url.getHost(), 8080);
            out = new DataOutputStream(new BufferedOutputStream(sock.getOutputStream()));
            br  = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        }
        catch(Exception e)
        {
            isFailed = true;
            failureMessage = e.getMessage();
        }
        //When these things fail, return immediately.
        if (isFailed)
        {
            return false;
        }

        String boundary = "-----------------------------" + getRandomString();

        StringBuffer[] heads = this.getSocketHead(files, boundary);
        StringBuffer tail = this.getTailString(boundary);
        StringBuffer headTemplate = this.getSocketHeadTemplate(boundary, url);

        //A flag to tell server that this is a fresh upload start, so server
        //will reset isStopped flag to false if the there is a uploaded process
        //that is stopped by the user.
        boolean isFirst = true;

        //Upload will go on if some of them fail.
        for (int i = 0; i < files.size(); i++)
        {
            HttpUploadFile uf = (HttpUploadFile)files.get(i);

            try
            {
                StringBuffer query = new StringBuffer(headTemplate.toString());

                query = replaceTemplate(query, "####", this.getUploadQueryString(uf, isFirst));

                File f = new File(uf.getZipFilePath());
                fileLength = f.length();
                currentFileName = uf.getAbsoluteFilePath();

                long contentLength = f.length() + tail.length() + heads[i].length();

                query = replaceTemplate(query, "****", Long.toString(contentLength));

                out = new DataOutputStream(new BufferedOutputStream(sock.getOutputStream()));
                out.writeBytes(query.toString());
                out.writeBytes(heads[i].toString());
                this.httpUploadFileStream(new FileInputStream(f), out);

                out.writeBytes(tail.toString());
                out.flush ();

                bytesSent += fileLength;
                okFiles.add(currentFileName);
                if (isFirst)
                {
                    isFirst = false;
                }
            }
            catch(Exception e)
            {
                failFiles.add(currentFileName);
                failureMessage = e.getMessage();
            }
        }//END OF FOR LOOP

        endTime = System.currentTimeMillis();

        return true;
    }

    /**
     * After a upload, notify the server to start process uploaded files, or
     * clean up the cache if failed.
     *
     * @param mode The string value to indicate what to do for the server.
     *        mode=stop delete uploaded files and block incoming files to be
     *        saved to file system;  mode=success, start parsing service;
     *        mode=done, moving onto different page (close applet).
     */
    public void notifyServer(String mode)
    {
        URL url = null;
        URLConnection urlConn = null;
        //DataOutputStream out = null;
        BufferedReader br = null;
        String query = up.getNotifyURLWithSid(); //getNotifyURL();
		isFailed = false;
        try
        {
            url = new URL(query + this.getNotifyQueryString(mode));

            urlConn = url.openConnection();
            setConnectionProperty(urlConn, null, false);

            //Read in the response string from the server.
            br = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

            String line;
            StringBuffer sb = new StringBuffer();
            while ((line = br.readLine()) != null)
            {
                sb.append(line);
            }
            br.close();
            urlConn = null;

            notifyReply = sb.toString();
        }
        catch(Exception e)
        {
            isFailed = true;
            failureMessage = e.getMessage();
        }
    }

    /**
     * To construct the head string used by Socket mediated http upload.
     *
     * @param files the Vector contains a list of files to be uploaded.
     * @param boundary The boundary string used by the http head to demarcate
     *         the head string from the content and between contents.
     * @return an array of StringBuffer representing head string for each file.
     */
    private StringBuffer[] getSocketHead(Vector files, String boundary)
    {
        StringBuffer[] sbArray = new StringBuffer[files.size()];
        StringBuffer sb;
        for (int i = 0; i < files.size(); i++)
        {
            HttpUploadFile uf = (HttpUploadFile)files.get(i);
            sbArray[i] = new StringBuffer();
            sb = sbArray[i];
            // Line 1.
            sb.append(boundary);
            sb.append(NEWLINE);
            // Line 2.
            sb.append("Content-Disposition: form-data; name=\"File");
            sb.append(i + 1);
            sb.append("\"; filename=\"");
            sb.append(uf.getZipFileName());
            sb.append("\"");
            sb.append(NEWLINE);
            // Line 3 & Empty Line 4.
            sb.append("Content-Type: application/octet-stream");
            sb.append(NEWLINE);
            sb.append(NEWLINE);
        }
        return sbArray;
    }

    /**
     * To construct the tail string to mark the end of the http content.
     *
     * @param boundary The boundary string used by the http head to demarcate
     *         the head string from the content and between contents.
     * @return a StringBuffer representing tail string generic for every file.
     */
    private StringBuffer getTailString(String boundary)
    {
        StringBuffer sb = new StringBuffer();
        // Telling the Server we have Finished.
        sb.append(NEWLINE);
        sb.append(boundary);
        sb.append("--");
        sb.append(NEWLINE);
        return sb;
    }

    /**
     * To construct the head information used by Socket mediated http upload.
     *
     * @param boundary The boundary string used by the http head to demarcate
     *         the head string from the content and between contents.
     * @param url The URL to which the upload process is targetting.
     * @return StringBuffer representing general information about this request.
     */
      private StringBuffer getSocketHeadTemplate(String boundary, URL url)
      {
        StringBuffer sb = new StringBuffer();
        // Header: Request line
        sb.append("POST ");
        sb.append(url.getPath());

        sb.append("####");

        sb.append(" HTTP/1.1\r\n");
        // Header: General
        sb.append("Host: ");
        sb.append(url.getHost());
        sb.append(NEWLINE);
        // Header: Entity
        sb.append("Content-type: multipart/form-data; boundary=");
        sb.append(boundary.substring(2, boundary.length()) +"\r\n");
        sb.append("Content-length: ");
        sb.append("****");
        sb.append(NEWLINE);
        // Blank line
        sb.append(NEWLINE);
        return sb;
      }

      /**
       * To do a string replacement operation with a new string token for
       * an existing one.
       *
       * @param template the StringBuffer part of which is to be replaced.
       * @param token The string token found in the template, and will be replaced.
       * @param newToken The string token that will be used to replace the existing one.
       * @return StringBuffer whose token has be replaced if the token is present.
       */
    private StringBuffer replaceTemplate(StringBuffer template,
        String token, String newToken)
    {
        int index = template.indexOf(token);
        return template.replace(index, index + token.length(), newToken);
    }

    /**
     * To set the property of an URLConnection to be used for file upload.
     *
     * @param urlConn the URLConnection class object whose property will be set.
     * @param boundary The string value used as a boundary string for this
     *         urlConn.
     * @param isOut boolean value to indicate if the urlConn's do output property
     *        be set to true or false.
     */
    private void setConnectionProperty(URLConnection urlConn,
        String boundary, boolean isOut)
    {
        if (isOut)
        {
            urlConn.setDoOutput(true);
        }
        else
        {
            urlConn.setDoOutput(false);
        }

        urlConn.setDoInput(true);
        urlConn.setUseCaches(false);

        if (boundary != null)
        {
            urlConn.setRequestProperty("Content-Type",
            "multipart/form-data; boundary=" + boundary.substring(2, boundary.length()));
        }
    }

    /**
     * To compose a header string used by the URLConnection for file upload.
     *
     * @param uf the HttpUploadFile object containing information about the file
     *         to be uploaded.
     * @param boundary the String value for the boundary string.
     * @return String the header string such composed.
     */
    private String getHttpConnHeaderString(HttpUploadFile uf, String boundary)
    {
        StringBuffer sb = new StringBuffer();
        // Line 1.
        sb.append(boundary);
        sb.append(NEWLINE);
        // Line 2.
        sb.append("Content-Disposition: form-data; name=\"file");
        sb.append("\"; filename=\"");
        sb.append(uf.getZipFileName());
        sb.append("\"");
        sb.append(NEWLINE);
        // Line 3 & Empty Line 4.
        sb.append("Content-Type: application/octet-stream");
        sb.append(NEWLINE);
        sb.append(NEWLINE);

        return sb.toString();
    }

    /**
     * Compose the http upload query string that passes all the information
     * about this file to the server.
     *
     * @param uf the HttpUploadFile object containing all the information about
     *         the file to be uploaded.
     * @param isFirst the boolean value to tell the server that this is the first
     *         file from this batch of uploading files, so server will set isStopped
     *        to false.
     * @return String the completed query string.
     */
    private String getUploadQueryString(HttpUploadFile uf, boolean isFirst)
    {
        StringBuffer query = new StringBuffer();
        query.append("?type=" + uf.getFileType());
        //query.append("&filePath="+uf.getAbsoluteFilePath());
        query.append("&fileName=" + uf.getZipFileName());
        query.append("&id=" + up.getId());
        query.append("&crc32=" + uf.getCrc32());
        query.append("&module=" + up.getModule());

        if (isFirst)
        {
            query.append("&mode=first");
        }

        return query.toString();
    }

    /**
     * Compose the http notify query string that passes all the information
     * about the uploaded files to the server for parsing.
     *
     * @param mode The string value as part of the query string sent to
     *         server for server to process uploaded files accordingly.
     * @param String the composed query string.
     */
    private String getNotifyQueryString(String mode)
    {
        StringBuffer query = new StringBuffer();
        query.append("&id=" + up.getId());
        query.append("&mode=" + mode);
        query.append("&module=" + up.getModule());
        return query.toString();
    }

    /**
     * Write a file into an output stream.
     *
     * @param infis the fileinputstream as a source of bytes to write.
     * @param dOut the dataoutput stream to which the bytes will be written.
     * @throw Exception if error occurs.
     */
    private void httpUploadFileStream(FileInputStream infis, DataOutputStream dOut)
        throws Exception
    {
        if (infis == null)
        {
            return;
        }

        byte[] byteBuff = null;
        FileInputStream fis = infis;
        try
        {
            int numBytes = 0;
            byteBuff = new byte[BUFFER_SIZE];

            while ((numBytes = fis.read(byteBuff)) != -1) // && !stop)
            {
                dOut.write(byteBuff, 0, numBytes);
                dOut.flush();
            }
        }
        catch (IOException ioe)
        {
            throw new Exception(ioe.getMessage());
        }
        finally
        {
              try
              {
                fis.close();
              }
              catch(Exception e)
              {}
              byteBuff = null;
        }
      }

      private String getFilePath(String path)
      {
          int index = path.lastIndexOf("\\");

          if (index != -1)
          {
              return path = path.substring(0, index);
          }

          return null;
      }

      /**
       * To build a randon string used for http header boundry.
       *
       * @return String the random string.
       */
    private String getRandomString()
    {
        String alphaNum="1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuffer sbRan = new StringBuffer(11);
        int num;
        for(int i = 0; i < 11; i++)
        {
            num = (int)(Math.random()* (alphaNum.length() - 1));
            sbRan.append(alphaNum.substring(num, num+1));
        }
        return sbRan.toString();
    }

	public boolean isStopped() {
		return isStopped;
	}

	public void setStopped(boolean isStopped) {
		this.isStopped = isStopped;
	}
	private boolean checkFileType(String zipEntryName)
	{
		int index = -1;
		zipEntryName = zipEntryName.toUpperCase();
		for (int i = 0; i < fileExtensions.length; i++)
		{
			if ("ZIP".equals(fileExtensions[i]))
				continue;
			else
			{
				index = zipEntryName.lastIndexOf(fileExtensions[i]);
				if (index == -1)
					return true;
				//somehow the checked extesion not at the right end of the file name.
				else if ((index + fileExtensions[i].length() != zipEntryName.length()))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean checkFileNumber(int count)
	{
		for (int i = 0; i < fileExtensions.length; i++)
		{
			if ("GAL".equals(fileExtensions[i]) && count > 1)
				return true;
		}
		return false;
	}
}

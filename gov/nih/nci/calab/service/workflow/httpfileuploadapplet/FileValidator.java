package gov.nih.nci.calab.service.workflow.httpfileuploadapplet;

/*
 * <!-- LICENSE_TEXT_START -->
 *  The MAGE Software License, Version 1.0
 *
 *  Copyright 2001 SAIC. This software was developed in conjunction with the National Cancer
 *  Institute, and so to the extent government employees are co-authors, any rights in such works
 *  shall be subject to Title 17 of the United States Code, section 105.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are permitted
 *  provided that the following conditions are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice, this list of conditions
 *  and the disclaimer of Article 3, below.  Redistributions in binary form must reproduce the above
 *  copyright notice, this list of conditions and the following disclaimer in the documentation and/or
 *  other materials provided with the distribution.
 *
 *  2.  The end-user documentation included with the redistribution, if any, must include the
 *  following acknowledgment:
 *
 *  "This product includes software developed by the SAIC and the National Cancer
 *  Institute."
 *
 *  If no such end-user documentation is to be included, this acknowledgment shall appear in the
 *  software itself, wherever such third-party acknowledgments normally appear.
 *
 *  3. The names "The National Cancer Institute", "NCI" and "SAIC" must not be used to endorse or
 *  promote products derived from this software.
 *
 *  4. This license does not authorize the incorporation of this software into any proprietary
 *  programs.  This license does not authorize the recipient to use any trademarks owned by either
 *  NCI or SAIC-Frederick.
 *
 *
 *  5. THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 *  MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE) ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE NATIONAL CANCER INSTITUTE, SAIC, OR
 *  THEIR AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 *  EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 *  PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 *  PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
 *  OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * <!-- LICENSE_TEXT_END -->
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.CRC32;

import org.apache.log4j.Logger;

/**
 * File validator for testing integrity of files
 * using CRC32s
 *
 * Title: caArray
 * Description: MicroArray data repository and application
 * Copyright: (c) 2005 NCICB.  All rights reserved.
 * Company: NIH/NCICB
 * @author $,
 * @version $Revision: 1.1 $, $Date: 2006-04-14 13:04:27 $
 */
public class FileValidator 
{
    /**
     * Logger used by this class.
     */
    private static Logger logger_ =
        Logger.getLogger(FileValidator.class);
 	/**
	 * Default Constructor
	 */
	public FileValidator()
	{}
	
	/**
	 * Retrieves the CRC32 as vaidatorCode for the submitted file
	 * 
	 * @param fileToValidate File to validate
	 * @return	String validatorCode for submitted file
	 * @throws Exception
	 */
	public static String getValidatorCode(File fileToValidate)throws Exception
	{
		String value = null;
		FileInputStream fis = null;
		CRC32 crc32 = new CRC32();

		// Always check arguments 
		if (fileToValidate.exists() == false ||
				fileToValidate.length() == 0 ||
				fileToValidate == null)
		{
			logger_.error(
					"File to validate does not exist " +
					"or has a lenght of 0 bytes " +
					"or is null");
			throw new IllegalArgumentException(
					"File to validate does not exist " +
					"or has a lenght of 0 bytes " +
					"or is null");
		}

		try
		{
			byte[] stream = new byte[4096];
			fis = new FileInputStream(fileToValidate);
			int bytesRead = 0;
			while ((bytesRead = fis.read(stream)) != -1)
			{
				crc32.update(stream,0,bytesRead);
			}
			value = new Long(crc32.getValue()).toString();
		}
		catch (IOException e)
		{
			logger_.error(
					"File: " +
					fileToValidate.getName() +
					" could not be read. " +
					e.getMessage());
			throw new IOException(
					"File: " +
					fileToValidate.getName() +
					" could not be read");
		}
		catch (Exception e)
		{
			logger_.error(
					"Exception occurred during CRC32 check: " +
					e.getMessage());
			throw e;
		}
		finally
		{
			if (fis != null)
			{
				try
				{
					fis.close();
					fis = null;
				}
				catch (Exception e)
				{
					logger_.error("Couldn't close input stream");
				}
			}
		}
		return value;
	}

	public static boolean validateFile(
			File fileToValidate, 
			String validatorToValidate) throws Exception
	{
		// Always check arugments
		if (fileToValidate.exists() == false ||
				fileToValidate.length() == 0 ||
				fileToValidate == null ||
				validatorToValidate == null)
		{
			logger_.error(
					"File to validate does not exist " +
					"or has a length of 0 bytes " +
					"or is null " +
					"or validatorToValidate is null");
			throw new IllegalArgumentException(
					"File to validate does not exist " +
					"or has a length of 0 bytes " +
					"or is null " +
					"or validatorToValidate is null");
		}
		boolean isValid = false;
		try
		{
			if (getValidatorCode(fileToValidate).compareTo(validatorToValidate) == 0)
			{
				isValid = true;
			}
			else
			{
				isValid = false;
			}
				
		}
		catch (Exception e)
		{
			logger_.error(
					"Exception occurred during CRC32 check: " +
					e.getMessage());
			throw e;
			
		}
		return isValid;
	}
}

package gov.nih.nci.calab.service.workflow.httpfileuploadapplet;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.CRC32;

public class AppletValidatorCodeGenerator
{
		
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
			throw new IOException(
					"File: " +
					fileToValidate.getName() +
					" could not be read");
		}
		catch (Exception e)
		{
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
				catch (Exception ignore)
				{
				}
			}
		}
		return value;
	}
}

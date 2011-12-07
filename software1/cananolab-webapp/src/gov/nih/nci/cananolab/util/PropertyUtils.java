package gov.nih.nci.cananolab.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * This class is caNanoLab Util class. It will read user property file, and fetch
 * property value by property name.
 * 
 * @author houyh
 * 
 */
public class PropertyUtils {
	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(PropertyUtils.class);

	private static Properties properties = null;

	/**
	 * This method is for users to get property value. It requires users to
	 * provide propert file name, and property name.
	 * 
	 * @param propertyFileName
	 * @param propertyName
	 * @return property value
	 */
	public static String getProperty(String propertyFileName,
			String propertyName) {
		if (properties == null) {
			loadProperty(propertyFileName, false);
		} else if (properties.get(propertyName) == null) {
			loadProperty(propertyFileName, true);
		}
		return (String) properties.get(propertyName);
	}
	
	/**
	 * This method is for users to get property value. It requires users to
	 * provide propert file name, and property name.
	 * 
	 * @param propertyFileName
	 * @param propertyName
	 * @return property value
	 */
	public static boolean setProperty(String propertyFileName, String name, String value) {
		boolean result = true;
		
		// Need to load property first, otherwise will lose other settings. 
		loadProperty(propertyFileName, true);
		
		OutputStream out = null;
		try {
			out = new BufferedOutputStream(
				new FileOutputStream(getPropertyFileName(propertyFileName)));
			
			if (value == null){
				properties.remove(name);
			} else {
				properties.setProperty(name, value);
			}
			properties.store(out, null);
		} catch (Exception e) {
			logger.error("Property file cannot be updated: " + e.toString(), e);
			result = false;
		} finally {
			if (out != null) {
				try {
					out.flush();
					out.close();
				} catch (Exception e) {
				}
			}
		}
		return result;
	}
	
	/**
	 * This method reads property file, and load all properties into properties
	 * Properties class by using name/value pair. All property files must reside
	 * in gov/nih/nci/calab/resource directory.
	 * 
	 * @param propertyFileName
	 * @param reload
	 */
	protected static void loadProperty(String propertyFileName, boolean reload) {
		InputStream istream = null;
		properties = new Properties();
		try {
			if (reload) {
				// Directly load from property file to avoid caching issue.
				istream = new BufferedInputStream(
						new FileInputStream(getPropertyFileName(propertyFileName)));
			} else {
				istream = Thread.currentThread().getContextClassLoader().
						getResourceAsStream(propertyFileName);
			}
			properties.load(istream);
		} catch (Exception exp) {
			logger.error("Property file cannot be read: " + exp.toString(), exp);
		} finally {
			if (istream != null) {
				try {
					istream.close();
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 * Get the file name of caNanolab property file.
	 *
	 * @param propertyFileName
	 * @return file name of caNanolab property file.
	 */
	private static String getPropertyFileName(String propertyFileName) {
		String fileName = Thread.currentThread().getContextClassLoader().
					getResource(propertyFileName).getPath();
		
		return fileName;
	}
}
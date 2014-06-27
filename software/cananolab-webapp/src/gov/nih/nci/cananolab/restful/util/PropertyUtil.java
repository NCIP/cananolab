package gov.nih.nci.cananolab.restful.util;

import gov.nih.nci.cananolab.restful.SecurityServices;

import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * 
 * @author yangs8
 *
 */
public class PropertyUtil {
	
	private static Logger logger = Logger.getLogger(PropertyUtil.class);
	
	private static final String APPLICATION_PROPERTY_FILE = "application.properties";
	private static final String COMMUNITY_PROPERTY_FILE = "community.properties";
	private static final String PROTOCOL_PROPERTY_FILE = "protocol.properties";
	private static final String PUBLICATION_PROPERTY_FILE = "publication.properties";
	private static final String SAMPLE_PROPERTY_FILE = "sample.properties";
	
	
	private static Properties application_properties = null;
	private static Properties community_properties = null;
	private static Properties protocol_properties = null;
	private static Properties publication_properties = null;
	private static Properties sample_properties = null;
	
	public static String getProperty(String type, String key) {
		
		
		return "";
	}
	
	/**
	 * Reads property file, and load all properties into properties
	 * Properties class by using name/value pair. All property files must reside
	 * in classpath.
	 * 
	 * @param propertyFileName
	 * @return
	 */
	protected static Properties loadProperty(String propertyFileName) {
		InputStream istream = null;
		Properties properties = new Properties();
		try {
			istream = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream(propertyFileName);
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
		
		return properties;
	}

}

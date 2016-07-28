package gov.nih.nci.cananolab.restful.util;

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
	
	/**
	 * Get a property value based on type and key.
	 * 
	 * Valid types are: application, sample, protocol, publication and community.
	 * 
	 * @param type
	 * @param key
	 * @return
	 */
	public static String getProperty(String type, String key) {
		if (type == null) {
			logger.error("Unable to get property value for [" + key + "] without a valid type");
			return "";
		}
		
		Properties props = getPropertiesByType(type);
		return props.getProperty(key);
	}
	
	public static String getPropertyReplacingToken(String type, String key, String tokenIndex, String replacement) {
		String propertyVal = getProperty(type, key);
		String token = "{" + tokenIndex + "}";
		
		return propertyVal.replace(token, replacement);
	}
	
	protected static Properties getPropertiesByType(String type) {
		type = type.toLowerCase();
		if (type.equals("sample"))
			return (sample_properties == null) ? loadProperty("sample.properties") : sample_properties;
		else if (type.equals("application"))
			return (application_properties == null) ? loadProperty("application.properties") : application_properties;
		else if	(type.equals("protocol")) 
			return (protocol_properties == null) ? loadProperty("protocol.properties") : protocol_properties;
		else if (type.equals("publication"))
			return (publication_properties == null) ? loadProperty("publication.properties") : publication_properties;
		else if (type.equals("community"))
			return (community_properties == null) ? loadProperty("community.properties") : community_properties;
		else
			return new Properties();			
			
	}
	
	/**
	 * Reads property file, and load all properties into properties
	 * Properties class by using name/value pair. All property files must reside
	 * in classpath.
	 * 
	 * @param propertyFileName
	 * @return
	 */
	public static Properties loadProperty(String propertyFileName) {
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

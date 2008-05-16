package gov.nih.nci.cananolab.util;

import java.io.InputStream;
import java.util.Properties;

/**
 * This class is caLab Util class. It will read user property file, and fetch
 * property value by property name.
 * 
 * @author zhoujim
 * 
 */
public class PropertyReader {
	protected static org.apache.log4j.Logger logger_ = org.apache.log4j.Logger
			.getLogger(PropertyReader.class);

	private static Properties myProps = null;

	/**
	 * This method reads property file, and load all properties into myProps
	 * Properties class by using name/value pair. All property files must reside
	 * in gov/nih/nci/calab/resource directory.
	 * 
	 * @param propertyFileName
	 */
	public static void loadProperty(String propertyFileName) {
		try {
			myProps = new Properties();
			// URL url = ClassLoader.getSystemResource(propertyFileName);
			// InputStream istream = url.openStream();
			InputStream istream = Thread.currentThread()
					.getContextClassLoader().getResourceAsStream(
							propertyFileName);
			myProps.load(istream);
		} catch (Exception exp) {
			logger_.error("Property file cannot be read: " + exp.toString());
		}
	}

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
		if (myProps == null
				|| (myProps != null && myProps.get(propertyName) == null)) {
			loadProperty(propertyFileName);
		}
		return (String) myProps.get(propertyName);
	}
}
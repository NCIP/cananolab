/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.calab.service.remote;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * Customize Spring Framework's PropertyPlaceholderConfigurer so that serviceUrl
 * codebase can be dynamically configured from system property.
 * 
 * @author pansu
 * 
 */
public class RemoteQuerySystemPropertyConfigurer extends
		PropertyPlaceholderConfigurer {
	protected void loadProperties(Properties props) throws IOException {
		props.setProperty("remote.codebase", System
				.getProperty("remote.codebase"));
	}

	public static String getRemoteServiceUrlCodebase(String appServiceURL)
			throws Exception {
		URL url = new URL(appServiceURL);
		String codeBase = url.getProtocol() + "://" + url.getHost() + ":"
				+ url.getPort();
		return codeBase;
	}
}

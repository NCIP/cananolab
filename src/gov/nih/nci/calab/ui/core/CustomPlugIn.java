package gov.nih.nci.calab.ui.core;

import gov.nih.nci.calab.ui.sample.InitSampleSetup;
import gov.nih.nci.calab.ui.security.InitSecuritySetup;

import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;

/**
 * Creates default CSM groups and sample types and initialize Hibernate
 * configurations as soon as server starts up.
 * 
 * @author pansu
 * 
 */
public class CustomPlugIn implements PlugIn {
	Logger logger=Logger.getLogger(CustomPlugIn.class);
	// This method will be called at application startup time
	public void init(ActionServlet actionServlet, ModuleConfig config)
			throws ServletException {
		System.out.println("Entering CustomPlugIn.init()");
		// set all sample types and create default visible groups in CSM
		try {
			InitSampleSetup.getInstance().setAllSampleTypes(
					actionServlet.getServletContext());
			InitSecuritySetup.getInstance().createDefaultCSMGroups();
		} catch (Exception e) {
			logger.error("Servlet initialization error", e);
		}
		System.out.println("Exiting CustomPlugIn.init()");
	}

	// This method will be called at application shutdown time
	public void destroy() {
		System.out.println("Entering CustomPlugIn.destroy()");
		System.out.println("Exiting CustomPlugIn.destroy()");
	}
}

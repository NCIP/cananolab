package gov.nih.nci.calab.ui.core;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;

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
	Logger logger=Logger.getLogger(CustomPlugIn.class.getName());
	// This method will be called at application startup time
	public void init(ActionServlet actionServlet, ModuleConfig config)
			throws ServletException {
		System.out.println("Entering CustomPlugIn.init()");
		System.out.println("Creating default visible groups...");
		// create default visible groups in CSM
		try {
			InitSessionSetup.getInstance().setAllSampleTypes(
					actionServlet.getServletContext());
			InitSessionSetup.getInstance().createDefaultCSMGroups();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Servlet initialization error", e);
		}
		System.out.println("Exiting CustomPlugIn.init()");
	}

	// This method will be called at application shutdown time
	public void destroy() {
		System.out.println("Entering CustomPlugIn.destroy()");
		System.out.println("Exiting CustomPlugIn.destroy()");
	}
}

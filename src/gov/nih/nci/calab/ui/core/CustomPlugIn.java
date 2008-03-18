package gov.nih.nci.calab.ui.core;

import java.util.Map;

import gov.nih.nci.calab.dto.remote.GridNodeBean;
import gov.nih.nci.calab.service.remote.GridService;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.ui.particle.InitParticleSetup;
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
	Logger logger = Logger.getLogger(CustomPlugIn.class);

	// This method will be called at application startup time
	public void init(ActionServlet actionServlet, ModuleConfig config)
			throws ServletException {
		System.out.println("Entering CustomPlugIn.init()");
		// set all sample types and create default visible groups in CSM
		try {
			InitSampleSetup.getInstance().setAllSampleTypes(
					actionServlet.getServletContext());
			InitParticleSetup.getInstance().setCharNameToCategory(
					actionServlet.getServletContext());
			InitSecuritySetup.getInstance().createDefaultCSMGroups();
			
			//set grid node hosts for public browse on bodyLogin.jsp
			Map<String, GridNodeBean> gridNodes = GridService.discoverServices(
					CaNanoLabConstants.GRID_INDEX_SERVICE_URL,
					CaNanoLabConstants.DOMAIN_MODEL_NAME);
			actionServlet.getServletContext().setAttribute("allGridNodes", gridNodes);
		} catch (Exception e) {
			this.logger.error("Servlet initialization error", e);
		}
		System.out.println("Exiting CustomPlugIn.init()");
	}

	// This method will be called at application shutdown time
	public void destroy() {
		System.out.println("Entering CustomPlugIn.destroy()");
		System.out.println("Exiting CustomPlugIn.destroy()");
	}
}

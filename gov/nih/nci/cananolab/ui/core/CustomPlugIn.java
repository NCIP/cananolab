package gov.nih.nci.cananolab.ui.core;

import gov.nih.nci.cananolab.ui.particle.InitCompositionSetup;
import gov.nih.nci.cananolab.ui.particle.InitNanoparticleSetup;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;

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
		try {
			// set servlet context variables
			InitSetup.getInstance().getDisplayNameLookup(
					actionServlet.getServletContext());
			InitCompositionSetup.getInstance().getDefaultFunctionTypes(
					actionServlet.getServletContext());
			InitCompositionSetup.getInstance()
					.getDefaultNanoparticleEntityTypes(
							actionServlet.getServletContext());
			InitCompositionSetup.getInstance()
					.getDefaultFunctionalizingEntityTypes(
							actionServlet.getServletContext());
			actionServlet.getServletContext().setAttribute("applicationOwner",
					CaNanoLabConstants.APP_OWNER);
			
			InitNanoparticleSetup.getInstance()
				.getDefaultCharacterizationTypes(
						actionServlet.getServletContext());
			
			InitNanoparticleSetup.getInstance()
				.getDefaultCompositionTypes(
					actionServlet.getServletContext());
			
			InitCompositionSetup.getInstance().getAntigenSpecies(
					actionServlet.getServletContext());
			
			InitCompositionSetup.getInstance().getAntibodySpecies(
					actionServlet.getServletContext());
			
			InitCompositionSetup.getInstance().getMolecularFormulaTypes(
					actionServlet.getServletContext());
			
			InitSecuritySetup.getInstance().createDefaultCSMGroups();
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

package gov.nih.nci.cananolab.ui.core;

import gov.nih.nci.cananolab.dto.common.GridNodeBean;
import gov.nih.nci.cananolab.exception.GridAutoDiscoveryException;
import gov.nih.nci.cananolab.service.common.GridDiscoveryServiceJob;
import gov.nih.nci.cananolab.ui.particle.InitNanoparticleSetup;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;

import java.util.Map;

import javax.servlet.ServletContext;
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
			// set default lookup valules in the servlet context
			ServletContext appContext = actionServlet.getServletContext();
			InitSetup.getInstance().getDefaultLookupTable(appContext);
			InitSetup.getInstance().getClassNameToDisplayNameLookup(appContext);
			InitSetup
					.getInstance()
					.getServletContextDefaultTypesByReflection(
							appContext,
							"defaultFunctionalizingEntityTypes",
							"gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.FunctionalizingEntity");
			InitSetup
					.getInstance()
					.getServletContextDefaultTypesByReflection(
							appContext,
							"defaultNanoparticleEntityTypes",
							"gov.nih.nci.cananolab.domain.particle.samplecomposition.base.NanoparticleEntity");
			InitSetup
					.getInstance()
					.getServletContextDefaultTypesByReflection(appContext,
							"defaultFunctionTypes",
							"gov.nih.nci.cananolab.domain.particle.samplecomposition.Function");
			InitSetup
					.getInstance()
					.getServletContextDefaultTypesByReflection(appContext,
							"defaultTargetTypes",
							"gov.nih.nci.cananolab.domain.particle.samplecomposition.Target");

			actionServlet.getServletContext().setAttribute("applicationOwner",
					CaNanoLabConstants.APP_OWNER);

			InitNanoparticleSetup.getInstance()
					.getDefaultCharacterizationTypes(
							actionServlet.getServletContext());

			InitNanoparticleSetup.getInstance().getDefaultCompositionTypes(
					actionServlet.getServletContext());

			InitSetup.getInstance().getServletContextDefaultLookupTypes(
					appContext, "antigenSpecies", "Antigen", "species");
			InitSetup.getInstance().getServletContextDefaultLookupTypes(
					appContext, "antibodySpecies", "Antibody", "species");
			InitSetup.getInstance().getServletContextDefaultLookupTypes(
					appContext, "molecularFormulaTypes", "ComposingElement",
					"molecularFormulaType");
			InitSetup.getInstance().getServletContextDefaultLookupTypes(
					appContext, "wallTypes", "CarbonNanotube", "wallType");

			InitSecuritySetup.getInstance().createDefaultCSMGroups();

			setupInitialGridNodes(appContext);
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

	// discover grid nodes during start-up time and saves the grid nodes in
	// application context
	private void setupInitialGridNodes(ServletContext appContext) {
		try {
			GridDiscoveryServiceJob gridDiscoveryJob = new GridDiscoveryServiceJob();
			Map<String, GridNodeBean> gridNodeMap = gridDiscoveryJob
					.getAllGridNodes();
			if (gridNodeMap == null) {
				throw new GridAutoDiscoveryException();
			}
			appContext.setAttribute("allGridNodes", gridNodeMap);
		} catch (Exception e) {
			logger.error(e);
		}
	}
}

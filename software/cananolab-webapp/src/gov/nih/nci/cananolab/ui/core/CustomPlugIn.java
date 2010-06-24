package gov.nih.nci.cananolab.ui.core;

import gov.nih.nci.cananolab.ui.sample.InitCharacterizationSetup;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;
import gov.nih.nci.cananolab.util.SampleConstants;

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
			InitSetup
					.getInstance()
					.getDefaultTypesByReflection(appContext,
							"defaultFunctionalizingEntityTypes",
							"gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity");
			InitSetup.getInstance().getDefaultTypesByReflection(appContext,
					"defaultNanomaterialEntityTypes",
					"gov.nih.nci.cananolab.domain.particle.NanomaterialEntity");
			InitSetup.getInstance().getDefaultTypesByReflection(appContext,
					"defaultFunctionTypes",
					"gov.nih.nci.cananolab.domain.particle.Function");
			InitSetup.getInstance().getDefaultTypesByReflection(appContext,
					"defaultTargetTypes",
					"gov.nih.nci.cananolab.domain.function.Target");
			InitSetup
					.getInstance()
					.getDefaultTypesByReflection(appContext,
							"defaultChemicalAssociationTypes",
							"gov.nih.nci.cananolab.domain.particle.ChemicalAssociation");

			InitSetup.getInstance().getDefaultTypesByLookup(appContext,
					"speciesTypes", "species", "type");
			InitSetup.getInstance().getDefaultTypesByLookup(appContext,
					"wallTypes", "carbon nanotube", "wallType");

			InitCharacterizationSetup.getInstance()
					.getDefaultCharacterizationTypes(appContext);
			// For PubChem data sources drop-down list.
			appContext.setAttribute("pubChemDataSources",
					SampleConstants.PUBCHEM_DS_LIST);

			InitSetup.getInstance().setStaticOptions(appContext);

			InitSecuritySetup.getInstance().setupDefaultCSM();

//			setupInitialGridNodes(appContext);
		} catch (Exception e) {
			this.logger.error("Servlet initialization error", e);
		}
		System.out.println("Exiting CustomPlugIn.init()");
	}

	// This method will be called at application shutdown time
	public void destroy() {
		System.out.println("Entering CustomPlugIn.destroy()");
		System.out.println("Exiting CustomPlugIn.destroy()");
		// be sure to clean up CSM entries that need to be removed before
		// shutting down the server.
		try {
			cleanUpCSM();
		} catch (Exception e) {
			logger.error("Error removing obsoleted CSM entries.");
		}
	}

	private void cleanUpCSM() throws Exception {
		CSMCleanupJob csmCleanJob = new CSMCleanupJob();
		csmCleanJob.cleanUpCSM();
	}
}

package gov.nih.nci.cananolab.ui.core;

import gov.nih.nci.cananolab.ui.sample.InitCharacterizationSetup;
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
		logger.info("Entering CustomPlugIn.init()");
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

			// InitSecuritySetup.getInstance().setupDefaultCSM();

			// load mapping for data availability
			InitSetup.getInstance().getDefaultTypesByLookup(appContext,
					"MINChar", "MINChar", "entity");
			InitSetup.getInstance().getLookupByName(appContext,
					"caNano2MINChar", "caNano2MINChar");
			InitSetup
					.getInstance()
					.getDefaultTypesByReflection(
							appContext,
							"physicoChars",
							"gov.nih.nci.cananolab.domain.characterization.physical.PhysicoChemicalCharacterization");
			InitSetup
					.getInstance()
					.getDefaultTypesByReflection(appContext, "invitroChars",
							"gov.nih.nci.cananolab.domain.characterization.invitro.InvitroCharacterization");

			InitSetup
					.getInstance()
					.getDefaultTypesByReflection(appContext, "invivoChars",
							"gov.nih.nci.cananolab.domain.characterization.invivo.InvivoCharacterization");

			InitSetup
					.getInstance()
					.getDefaultTypesByReflection(appContext, "chemicalAssocs",
							"gov.nih.nci.cananolab.domain.particle.ChemicalAssociation");
		} catch (Exception e) {
			this.logger.error("Servlet initialization error", e);
		}
		logger.info("Exiting CustomPlugIn.init()");
	}

	// This method will be called at application shutdown time
	public void destroy() {
		logger.info("Entering CustomPlugIn.destroy()");
		logger.info("Exiting CustomPlugIn.destroy()");
		// be sure to clean up CSM entries that need to be removed before
		// shutting down the server.
	}
}

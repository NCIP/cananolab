package gov.nih.nci.cananolab.restful;

import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationSummaryViewBean;
import gov.nih.nci.cananolab.restful.sample.CharacterizationBO;
import gov.nih.nci.cananolab.restful.sample.CharacterizationManager;
import gov.nih.nci.cananolab.restful.sample.ExperimentConfigManager;
import gov.nih.nci.cananolab.restful.util.CommonUtil;
import gov.nih.nci.cananolab.restful.util.SecurityUtil;
import gov.nih.nci.cananolab.restful.view.SimpleCharacterizationsByTypeBean;
import gov.nih.nci.cananolab.restful.view.edit.SimpleCharacterizationEditBean;
import gov.nih.nci.cananolab.restful.view.edit.SimpleCharacterizationSummaryEditBean;
import gov.nih.nci.cananolab.restful.view.edit.SimpleExperimentBean;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

@Path("/characterization")
public class CharacterizationServices {

	private Logger logger = Logger.getLogger(CharacterizationServices.class);
	
	@Inject
	ApplicationContext applicationContext;
	
	@GET
	@Path("/setupEdit")
	@Produces ("application/json")
    public Response setupEdit(@Context HttpServletRequest httpRequest, 
    		@DefaultValue("") @QueryParam("sampleId") String sampleId) {
		logger.debug("In setupEdit");		
		
		if (! SecurityUtil.isUserLoggedIn(httpRequest))
			return Response.status(Response.Status.UNAUTHORIZED)
					.entity(SecurityUtil.MSG_SESSION_INVALID).build();
		
		try {
		CharacterizationBO characterizationBO = 
				(CharacterizationBO) applicationContext.getBean("characterizationBO");

		CharacterizationSummaryViewBean charView = characterizationBO.summaryEdit(sampleId, httpRequest);
		SimpleCharacterizationSummaryEditBean editBean = new SimpleCharacterizationSummaryEditBean();
		
		List<SimpleCharacterizationsByTypeBean> finalBean = editBean.transferData(httpRequest, charView, sampleId);
		
		logger.debug("Set up " + finalBean.size() + " characterization types for sample: " + sampleId);
		
		return Response.ok(finalBean).header("Access-Control-Allow-Credentials", "true")
						.header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
						.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(CommonUtil.wrapErrorMessageInList(e.getMessage())).build();
		}
	}

	@GET
	@Path("/setupAdd")
	@Produces ("application/json")
    public Response setupAdd(@Context HttpServletRequest httpRequest, 
    		@DefaultValue("") @QueryParam("sampleId") String sampleId, 
    		@DefaultValue("") @QueryParam("charType") String charType) {
		logger.debug("In setupAdd");		
		
		if (! SecurityUtil.isUserLoggedIn(httpRequest))
			return Response.status(Response.Status.UNAUTHORIZED)
					.entity(SecurityUtil.MSG_SESSION_INVALID).build();
		
		try {
		CharacterizationBO characterizationBO = 
				(CharacterizationBO) applicationContext.getBean("characterizationBO");

		SimpleCharacterizationEditBean charView = characterizationBO.setupNew(httpRequest, sampleId, charType);
//		SimpleCharacterizationSummaryEditBean editBean = new SimpleCharacterizationSummaryEditBean();
//		
//		List<SimpleCharacterizationsByTypeBean> finalBean = editBean.transferData(httpRequest, charView, sampleId);
//		
//		logger.debug("Set up " + finalBean.size() + " characterization types for sample: " + sampleId);
		
		return Response.ok(charView).header("Access-Control-Allow-Credentials", "true")
						.header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
						.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(CommonUtil.wrapErrorMessageInList(e.getMessage())).build();
		}
	}
	
	@GET
	@Path("/setupUpdate")
	@Produces ("application/json")
    public Response setupUpdate(@Context HttpServletRequest httpRequest, 
    		@DefaultValue("") @QueryParam("sampleId") String sampleId, @DefaultValue("") @QueryParam("charId") String charId,
    		@DefaultValue("") @QueryParam("charClassName") String charClassName,
    		@DefaultValue("") @QueryParam("charType") String charType) {
		logger.debug("In setupAdd");	
		
		if (! SecurityUtil.isUserLoggedIn(httpRequest))
			return Response.status(Response.Status.UNAUTHORIZED)
					.entity(SecurityUtil.MSG_SESSION_INVALID).build();
		
		try {
		CharacterizationBO characterizationBO = 
				(CharacterizationBO) applicationContext.getBean("characterizationBO");

		SimpleCharacterizationEditBean charView = characterizationBO.setupUpdate(httpRequest, sampleId, charId, charClassName, charType);

		return Response.ok(charView).header("Access-Control-Allow-Credentials", "true")
						.header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
						.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(CommonUtil.wrapErrorMessageInList(e.getMessage())).build();
		}
	}
	
	
	@GET
	@Path("/getCharNamesByCharType")
	@Produces ("application/json")
    public Response getCharNamesByCharType(@Context HttpServletRequest httpRequest, 
    		@DefaultValue("") @QueryParam("charType") String charType) {
		logger.debug("In getCharNamesByCharType");		
		
		try {
			CharacterizationManager characterizationMgr = 
				(CharacterizationManager) applicationContext.getBean("characterizationManager");

		List<String> charNames = characterizationMgr.getCharacterizationNames(httpRequest, charType);

		return Response.ok(charNames).header("Access-Control-Allow-Credentials", "true")
						.header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
						.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(CommonUtil.wrapErrorMessageInList(e.getMessage())).build();
		}
	}
	
	@GET
	@Path("/getAssayTypesByCharName")
	@Produces ("application/json")
    public Response getAssayTypesByCharName(@Context HttpServletRequest httpRequest, 
    		@DefaultValue("") @QueryParam("charName") String charName) {
		logger.debug("In getAssayTypesByCharName");		
		
		try {
			CharacterizationManager characterizationMgr = 
				(CharacterizationManager) applicationContext.getBean("characterizationManager");

		List<String> assayTypes = characterizationMgr.getAssayTypes(httpRequest, charName);

		return Response.ok(assayTypes).header("Access-Control-Allow-Credentials", "true")
						.header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
						.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(CommonUtil.wrapErrorMessageInList(e.getMessage())).build();
		}
	}
	
	@GET
	@Path("/setupAddTechnique")
	@Produces ("application/json")
    public Response setupAddTechnique(@Context HttpServletRequest httpRequest, 
    		@DefaultValue("") @QueryParam("charName") String charName) {
		logger.debug("In setupAddTechnique");	
		
		if (! SecurityUtil.isUserLoggedIn(httpRequest))
			return Response.status(Response.Status.UNAUTHORIZED)
					.entity(SecurityUtil.MSG_SESSION_INVALID).build();
		
		try {
			CharacterizationManager characterizationMgr = 
				(CharacterizationManager) applicationContext.getBean("characterizationManager");

		List<String> assayTypes = characterizationMgr.getAssayTypes(httpRequest, charName);

		return Response.ok(assayTypes).header("Access-Control-Allow-Credentials", "true")
						.header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
						.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(CommonUtil.wrapErrorMessageInList(e.getMessage())).build();
		}
	}
	
	@GET
	@Path("/getAbbreviationByTechnique")
	@Produces ("application/json")
    public Response getAbbreviationByTechnique(@Context HttpServletRequest httpRequest, 
    		@DefaultValue("") @QueryParam("techniqueType") String techniqueType) {
		logger.debug("In getAbbreviationByTechnique");		
		
		try {
			ExperimentConfigManager experimentMgr = 
				(ExperimentConfigManager) applicationContext.getBean("experimentConfigManager");

		String abbr = experimentMgr.getTechniqueAbbreviation(httpRequest, techniqueType);

		return Response.ok(abbr).header("Access-Control-Allow-Credentials", "true")
						.header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
						.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(CommonUtil.wrapErrorMessageInList(e.getMessage())).build();
		}
	}
	
	@POST
	@Path("/saveExperimentConfig")
	@Produces ("application/json")
    public Response saveExperimentConfig(@Context HttpServletRequest httpRequest, 
    		SimpleExperimentBean simpleExpConfig) {
		logger.debug("In getAbbreviationByTechnique");	
		
		if (! SecurityUtil.isUserLoggedIn(httpRequest))
			return Response.status(Response.Status.UNAUTHORIZED)
					.entity(SecurityUtil.MSG_SESSION_INVALID).build();
		
		try {
			CharacterizationBO characterizationBO = 
					(CharacterizationBO) applicationContext.getBean("characterizationBO");
			
			SimpleCharacterizationEditBean editBean = characterizationBO.saveExperimentConfig(httpRequest, simpleExpConfig);

		return Response.ok(editBean).header("Access-Control-Allow-Credentials", "true")
						.header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
						.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(CommonUtil.wrapErrorMessageInList(e.getMessage())).build();
		}
	}
}

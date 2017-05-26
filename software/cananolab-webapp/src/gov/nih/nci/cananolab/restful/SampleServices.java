package gov.nih.nci.cananolab.restful;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import gov.nih.nci.cananolab.dto.common.DataReviewStatusBean;
import gov.nih.nci.cananolab.dto.particle.AdvancedSampleSearchBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationSummaryViewBean;
import gov.nih.nci.cananolab.restful.bean.LabelValueBean;
import gov.nih.nci.cananolab.restful.sample.AdvancedSampleSearchBO;
import gov.nih.nci.cananolab.restful.sample.CharacterizationBO;
import gov.nih.nci.cananolab.restful.sample.CharacterizationManager;
import gov.nih.nci.cananolab.restful.sample.CharacterizationResultManager;
import gov.nih.nci.cananolab.restful.sample.SampleBO;
import gov.nih.nci.cananolab.restful.sample.SearchSampleBO;
import gov.nih.nci.cananolab.restful.util.CommonUtil;
import gov.nih.nci.cananolab.restful.view.SimpleAdvancedSearchResultView;
import gov.nih.nci.cananolab.restful.view.SimpleCharacterizationSummaryViewBean;
import gov.nih.nci.cananolab.restful.view.SimpleCharacterizationsByTypeBean;
import gov.nih.nci.cananolab.restful.view.SimpleSampleBean;
import gov.nih.nci.cananolab.restful.view.edit.SampleEditGeneralBean;
import gov.nih.nci.cananolab.restful.view.edit.SimplePointOfContactBean;
import gov.nih.nci.cananolab.security.utils.SpringSecurityUtil;
import gov.nih.nci.cananolab.ui.form.SearchSampleForm;
import gov.nih.nci.cananolab.util.Constants;

@Path("/sample")
public class SampleServices {
	private static final Logger logger = Logger.getLogger(SampleServices.class);
	
	@GET
	@Path("/setup")
	@Produces ("application/json")
    public Response setup(@Context HttpServletRequest httpRequest)
	{
		logger.debug("In initSetup");		
		
		try { 
			SearchSampleBO searchSampleBO = (SearchSampleBO) SpringApplicationContext.getBean(httpRequest, "searchSampleBO");
			Map<String, List<String>> dropdownTypeLists = searchSampleBO.setup(httpRequest);

			return Response.ok(dropdownTypeLists).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(CommonUtil.wrapErrorMessageInList("Error while setting up drop down lists")).build();
		}
	}
	
	@GET
	@Path("/getCharacterizationByType")
	@Produces ("application/json")
    public Response getCharacterizationByType(@Context HttpServletRequest httpRequest, 
    		@DefaultValue("") @QueryParam("type") String type)
	{	
		SearchSampleBO searchSampleBO = (SearchSampleBO) SpringApplicationContext.getBean(httpRequest, "searchSampleBO");
		
		try {
			List<String> characterizations = searchSampleBO.getCharacterizationByType(httpRequest, type);
			return Response.ok(characterizations).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(CommonUtil.wrapErrorMessageInList("Error while getting characterization by type")).build();
		}
	}
	
	@POST
	@Path("/searchSample")
	@Produces ("application/json")
	public Response searchSample(@Context HttpServletRequest httpRequest, SearchSampleForm searchForm )
	{	
		try
		{
			SearchSampleBO searchSampleBO = (SearchSampleBO) SpringApplicationContext.getBean(httpRequest, "searchSampleBO");
			
			List results = searchSampleBO.search(searchForm, httpRequest);
			
			Object result = results.get(0);
			if (result instanceof String) {
				logger.debug("Search sampel has error: " + results.get(0));
				return Response.status(Response.Status.NOT_FOUND).entity(result).build();
			} else {
				logger.debug("Sample search successful");
				return Response.ok(results).build();
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(CommonUtil.wrapErrorMessageInList("Error while searching for samples: " + e.getMessage())).build();
		}
	}

	@GET
	@Path("/view")
	@Produces ("application/json")
	 public Response view(@Context HttpServletRequest httpRequest, 
	    		@DefaultValue("") @QueryParam("sampleId") String sampleId)
	{	
		try
		{ 
			SampleBO sampleBO = (SampleBO) SpringApplicationContext.getBean(httpRequest, "sampleBO");

			SimpleSampleBean sampleBean = sampleBO.summaryView(sampleId,httpRequest);
			
			return (sampleBean.getErrors().size() == 0) ?
					Response.ok(sampleBean).header("Access-Control-Allow-Credentials", "true")
					.header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
					.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build()
					:Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(sampleBean.getErrors()).build();
			
		} catch (Exception e) {
			//return Response.ok("Error while viewing the search results").build();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(CommonUtil.wrapErrorMessageInList("Error while viewing the search results")).build();
		}
	}
	
	
	@GET
	@Path("/viewDataAvailability")
	@Produces ("application/json")
	 public Response viewDataAvailability(@Context HttpServletRequest httpRequest, 
	    		@DefaultValue("") @QueryParam("sampleId") String sampleId)
	{
		try { 

			SampleBO sampleBO = (SampleBO) SpringApplicationContext.getBean(httpRequest, "sampleBO");

			SimpleSampleBean sampleBean = sampleBO.dataAvailabilityView(sampleId,httpRequest);
			
			//SimpleSampleBean view = new SimpleSampleBean();
			//view.transferSampleBeanForSummaryView(sampleBean);
			
			return Response.ok(sampleBean).build();
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(CommonUtil.wrapErrorMessageInList("Error while getting data availability data")).build();
		}
	}
	
	@GET
	@Path("/characterizationView")
	@Produces ("application/json")
	 public Response characterizationView(@Context HttpServletRequest httpRequest, 
	    		@DefaultValue("") @QueryParam("sampleId") String sampleId)
	{
		try { 
			CharacterizationBO characterizationBO = (CharacterizationBO) SpringApplicationContext.getBean(httpRequest, "characterizationBO");

			CharacterizationSummaryViewBean charView = characterizationBO.summaryView(sampleId,httpRequest);
			SimpleCharacterizationSummaryViewBean viewBean = new SimpleCharacterizationSummaryViewBean();
			
			List<SimpleCharacterizationsByTypeBean> finalBean = viewBean.transferData(httpRequest, charView, sampleId);
			
			logger.debug("Found " + finalBean.size() + " characterizations for sample: " + sampleId);
			
			return (finalBean.size() == 0) ? Response.status(Response.Status.NOT_FOUND)
					.entity(CommonUtil.wrapErrorMessageInList("There is no characterization with your sample.")).build()
							: Response.ok(finalBean).header("Access-Control-Allow-Credentials", "true")
							.header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
							.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();
			
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(CommonUtil.wrapErrorMessageInList(e.getMessage())).build();
		}
		
	}
	
	
	@GET
	@Path("/downloadImage")
	@Produces({"image/png", "application/json"})
	 public Response downloadImage(@Context HttpServletRequest httpRequest, @Context HttpServletResponse httpResponse,
	    		@DefaultValue("") @QueryParam("fileId") String fileId){
		try {
			CharacterizationBO characterizationBO = 
					(CharacterizationBO) SpringApplicationContext.getBean(httpRequest, "characterizationBO");
			
			java.io.File file = characterizationBO.download(fileId, httpRequest);
			
			return Response.ok((Object) file).build();
			
		} catch (Exception ioe) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(CommonUtil.wrapErrorMessageInList(ioe.getMessage())).build();
		}
	}
	
	@GET
	@Path("/download")
	@Produces({"image/png", "application/json"})
	 public Response download(@Context HttpServletRequest httpRequest, @Context HttpServletResponse httpResponse,
	    		@DefaultValue("") @QueryParam("fileId") String fileId){
		try {
			CharacterizationBO characterizationBO = 
					(CharacterizationBO) SpringApplicationContext.getBean(httpRequest, "characterizationBO");
			
			String result = characterizationBO.download(fileId, httpRequest, httpResponse);
			return Response.ok(result).build();
		} 
		
		catch (Exception ioe) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(CommonUtil.wrapErrorMessageInList(ioe.getMessage())).build();
		}
	}
	
	@GET
	@Path("/exportCharacterizationView")
	@Produces("application/json")
	 public Response exportCharacterizationView(@Context HttpServletRequest httpRequest, @Context HttpServletResponse httpResponse,
	    		@DefaultValue("") @QueryParam("sampleId") String sampleId, @QueryParam("type") String type){
	
		try {
			CharacterizationBO characterizationBO = 
					(CharacterizationBO) SpringApplicationContext.getBean(httpRequest, "characterizationBO");
			
			String result = characterizationBO.summaryExport(sampleId, type, httpRequest, httpResponse);
			return Response.ok(result).build();
		}
		catch (Exception ioe) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(CommonUtil.wrapErrorMessageInList(ioe.getMessage())).build();
		}
	}
	
	@GET
	@Path("/edit")
	@Produces("application/json")
	 public Response edit(@Context HttpServletRequest httpRequest, 
	    		@DefaultValue("") @QueryParam("sampleId") String sampleId)
	{
		logger.debug("In edit Sample");
		
		if (!SpringSecurityUtil.isUserLoggedIn()) {
			logger.info("User not logged in");
			return Response.status(Response.Status.UNAUTHORIZED).entity(Constants.MSG_SESSION_INVALID).build();
		}
		SampleBO sampleBO = (SampleBO) SpringApplicationContext.getBean(httpRequest, "sampleBO");

		try {
			SampleEditGeneralBean sampleBean = sampleBO.summaryEdit(sampleId,httpRequest);
			return (sampleBean.getErrors().size() == 0) ?
					Response.ok(sampleBean).build() :
						Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(sampleBean.getErrors()).build();
		} 

		catch (Exception ioe) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(CommonUtil.wrapErrorMessageInList(ioe.getMessage())).build();
		}
	}	
	
	@POST
	@Path("/savePOC")
	@Produces ("application/json")
	public Response savePOC(@Context HttpServletRequest httpRequest, SampleEditGeneralBean simpleEditBean) {
		logger.debug("In savePOC");
		
		SampleEditGeneralBean editBean = null;
		try {
			SampleBO sampleBO = (SampleBO) SpringApplicationContext.getBean(httpRequest, "sampleBO");
			
			if (!SpringSecurityUtil.isUserLoggedIn())
				return Response.status(Response.Status.UNAUTHORIZED)
						.entity(Constants.MSG_SESSION_INVALID).build();
			
			editBean = sampleBO.savePointOfContactList(simpleEditBean, httpRequest);
			List<String> errors = editBean.getErrors();
			logger.debug("SavePOC completed with " + errors.size() + " errors");
			
			//return Response.ok(editBean).build();
			return (errors == null || errors.size() == 0 || 
					(errors.size() > 0 && !editBean.getErrorType().equals("POC"))) ?
					Response.ok(editBean).build() :
						Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errors).build();

		} catch (Exception e) {
			logger.error(e.getMessage());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(CommonUtil.wrapErrorMessageInList("Error while saving Point of Contact: " + e.getMessage())).build();
		}
	}
	
	@POST
	@Path("/saveAccess")
	@Produces ("application/json")
	public Response saveAccess(@Context HttpServletRequest httpRequest, SampleEditGeneralBean simpleEdit) {
		logger.debug("In saveAccess");
		try {
			SampleBO sampleBO = 
					(SampleBO) SpringApplicationContext.getBean(httpRequest, "sampleBO");
			
			if (!SpringSecurityUtil.isUserLoggedIn())
				return Response.status(Response.Status.UNAUTHORIZED)
						.entity(Constants.MSG_SESSION_INVALID).build();
			
			SampleEditGeneralBean editBean = sampleBO.saveAccess(simpleEdit, httpRequest);
			List<String> errors = editBean.getErrors();
			return (errors == null || errors.size() == 0) ?
					Response.ok(editBean).build() :
						Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errors).build();

		} catch (Exception e) {
			logger.error(e.getMessage());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(CommonUtil.wrapErrorMessageInList("Error while saving Access: " + e.getMessage())).build();
		}
	}
	
	@GET
	@Path("/regenerateDataAvailability")
	@Produces("application/json")
	 public Response regenerateDataAvailability(@Context HttpServletRequest httpRequest, 
	    		@DefaultValue("") @QueryParam("sampleId") String sampleId){
		
		SampleBO sampleBO = 
				(SampleBO) SpringApplicationContext.getBean(httpRequest, "sampleBO");
		
		if (!SpringSecurityUtil.isUserLoggedIn())
			return Response.status(Response.Status.UNAUTHORIZED)
					.entity(Constants.MSG_SESSION_INVALID).build();

		try {
			SampleEditGeneralBean simpleBean = sampleBO.updateDataAvailability(sampleId.trim(), httpRequest);
			return (simpleBean.getErrors().size() == 0) ?
					Response.ok(simpleBean).build()
					:
					Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(simpleBean.getErrors()).build();
		} catch (Exception ioe) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(CommonUtil.wrapErrorMessageInList(ioe.getMessage())).build();
		}
	}	
	
	@POST
	@Path("/updateSample")
	@Produces ("application/json")
	public Response updateSample(@Context HttpServletRequest httpRequest, SampleEditGeneralBean simpleEdit) {
		logger.info("In updateSample");
		try {
			SampleBO sampleBO = (SampleBO) SpringApplicationContext.getBean(httpRequest, "sampleBO");
			
			if (!SpringSecurityUtil.isUserLoggedIn())
				return Response.status(Response.Status.UNAUTHORIZED).entity(Constants.MSG_SESSION_INVALID).build();
			
			SampleEditGeneralBean editBean = sampleBO.update(simpleEdit, httpRequest);
			List<String> errors = editBean.getErrors();
			return (errors == null || errors.size() == 0) ?
					Response.ok(editBean).build() :
						Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errors).build();

		} catch (Exception e) {
			logger.error(e.getMessage());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(CommonUtil.wrapErrorMessageInList("Error while updating sample: " + e.getMessage())).build();
		}
	}
	
	@POST
	@Path("/submitSample")
	@Produces ("application/json")
	public Response submitSample(@Context HttpServletRequest httpRequest, SampleEditGeneralBean simpleEdit) {
		logger.debug("In submitSample");
		try {
			SampleBO sampleBO = 
					(SampleBO) SpringApplicationContext.getBean(httpRequest, "sampleBO");
			
			if (!SpringSecurityUtil.isUserLoggedIn())
				return Response.status(Response.Status.UNAUTHORIZED)
						.entity(Constants.MSG_SESSION_INVALID).build();
			
			SampleEditGeneralBean editBean = sampleBO.submit(simpleEdit, httpRequest);
			List<String> errors = editBean.getErrors();
			
			logger.debug("Sumbit sample completed with " + errors.size() + " errors");
			logger.debug("Submitted sample id: " + editBean.getSampleId());
			return (errors == null || errors.size() == 0) ?
					Response.ok(editBean).build() :
						Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errors).build();

		} catch (Exception e) {
			logger.error(e.getMessage());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(CommonUtil.wrapErrorMessageInList("Error while submitting sample: " + e.getMessage())).build();
		}
	}
	
	@POST
	@Path("/copySample")
	@Produces ("application/json")
	public Response copySample(@Context HttpServletRequest httpRequest, SampleEditGeneralBean simpleEdit) {
		logger.debug("In copySample");
		try {
			SampleBO sampleBO = 
					(SampleBO) SpringApplicationContext.getBean(httpRequest, "sampleBO");
			
			if (!SpringSecurityUtil.isUserLoggedIn())
				return Response.status(Response.Status.UNAUTHORIZED)
						.entity(Constants.MSG_SESSION_INVALID).build();
			
			if (simpleEdit.getNewSampleName() == null || simpleEdit.getNewSampleName().length() == 0)
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
						.entity("No new sample name is set for cloning").build();
			
			SampleEditGeneralBean editBean = sampleBO.clone(simpleEdit, httpRequest);
			List<String> errors = editBean.getErrors();
			return (errors == null || errors.size() == 0) ?
					Response.ok(editBean).build() :
						Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errors).build();

		} catch (Exception e) {
			logger.error(e.getMessage());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(CommonUtil.wrapErrorMessageInList("Error while copying sample: " + e.getMessage())).build();
		}
	}
	
	@GET
	@Path("/deleteSample")
	@Produces("application/json")
	 public Response deleteSample(@Context HttpServletRequest httpRequest, 
	    		@DefaultValue("") @QueryParam("sampleId") String sampleId){
		logger.debug("In deleteSample");
		try {
			SampleBO sampleBO = 
					(SampleBO) SpringApplicationContext.getBean(httpRequest, "sampleBO");
			
			if (!SpringSecurityUtil.isUserLoggedIn())
				return Response.status(Response.Status.UNAUTHORIZED)
						.entity(Constants.MSG_SESSION_INVALID).build();
			
			String msg = sampleBO.delete(sampleId, httpRequest);
			logger.debug("Delete sample complete: " + msg);
			return (msg == null || msg.startsWith("Error")) ?
					Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(msg).build()
					:
					Response.ok(msg).build();
						

		} catch (Exception e) {
			logger.error(e.getMessage());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(CommonUtil.wrapErrorMessageInList("Error while deleting sample: " + e.getMessage())).build();
		}
	}
	
	@GET
	@Path("/deleteSampleFromWorkspace")
	@Produces("application/json")
	 public Response deleteSampleFromWorkspace(@Context HttpServletRequest httpRequest, 
	    		@DefaultValue("") @QueryParam("sampleId") String sampleId){
		logger.debug("In deleteSampleFromWorkspace");
		try {
			SampleBO sampleBO = 
					(SampleBO) SpringApplicationContext.getBean(httpRequest, "sampleBO");
			
			if (!SpringSecurityUtil.isUserLoggedIn())
				return Response.status(Response.Status.UNAUTHORIZED)
						.entity(Constants.MSG_SESSION_INVALID).build();
			
			String msg = sampleBO.deleteSampleById(sampleId, httpRequest);
			logger.debug("Delete sample complete: " + msg);
			return (msg == null || msg.startsWith("Error")) ?
					Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(msg).build()
					:
					Response.ok(msg).build();
						

		} catch (Exception e) {
			logger.error(e.getMessage());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(CommonUtil.wrapErrorMessageInList("Error while deleting sample: " + e.getMessage())).build();
		}
	}
	
	
	@POST
	@Path("/deletePOC")
	@Produces("application/json")
	 public Response deletePOC(@Context HttpServletRequest httpRequest, SimplePointOfContactBean simplePOCBean){
		logger.debug("In deleteSample");
		try {
			SampleBO sampleBO = 
					(SampleBO) SpringApplicationContext.getBean(httpRequest, "sampleBO");
			
			if (!SpringSecurityUtil.isUserLoggedIn())
				return Response.status(Response.Status.UNAUTHORIZED)
						.entity(Constants.MSG_SESSION_INVALID).build();
			
			SampleEditGeneralBean editBean = sampleBO.deletePointOfContact(simplePOCBean, httpRequest);
			List<String> errors = editBean.getErrors();
			
			return (errors == null || errors.size() == 0) ?
					Response.ok(editBean).build() :
						Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errors).build();

		} catch (Exception e) {
			logger.error(e.getMessage());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(CommonUtil.wrapErrorMessageInList("Error while deleting POC from sample: " + e.getMessage())).build();
		}
	}
	
	@POST
	@Path("/deleteDataAvailability")
	@Produces("application/json")
	 public Response deleteDataAvailability(@Context HttpServletRequest httpRequest, SampleEditGeneralBean simpleSampleBean){
		logger.debug("In deleteDataAvailability");
		try {
			SampleBO sampleBO = 
					(SampleBO) SpringApplicationContext.getBean(httpRequest, "sampleBO");
			
			if (!SpringSecurityUtil.isUserLoggedIn())
				return Response.status(Response.Status.UNAUTHORIZED)
						.entity(Constants.MSG_SESSION_INVALID).build();
			
			SampleEditGeneralBean editBean = sampleBO.deleteDataAvailability(simpleSampleBean, httpRequest);
			List<String> errors = editBean.getErrors();
			
			return (errors == null || errors.size() == 0) ?
					Response.ok(editBean).build() :
						Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errors).build();

		} catch (Exception e) {
			logger.error(e.getMessage());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(CommonUtil.wrapErrorMessageInList("Error while deleting data availability from sample: " + e.getMessage())).build();
		}
	}
	
	@GET
	@Path("/submissionSetup")
	@Produces("application/json")
	 public Response submissionSetup(@Context HttpServletRequest httpRequest){
		logger.debug("In edit Sample");
		
		if (!SpringSecurityUtil.isUserLoggedIn()) {
			logger.info("User not logged in");
			return Response.status(Response.Status.UNAUTHORIZED)
					.entity(Constants.MSG_SESSION_INVALID).build();
		}
		SampleBO sampleBO = (SampleBO) SpringApplicationContext.getBean(httpRequest, "sampleBO");

		try {
			SampleEditGeneralBean sampleBean = sampleBO.setupNew(httpRequest);
			return (sampleBean.getErrors().size() == 0) ?
					Response.ok(sampleBean).build()
					:
						Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(sampleBean.getErrors()).build();
		} 

		catch (Exception ioe) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(CommonUtil.wrapErrorMessageInList("Error while setting up to submit sample." + ioe.getMessage())).build();
		
		}
	}	
	
	@GET
	@Path("/getSampleNames")
	@Produces("application/json")
	 public Response getSampleNames(@Context HttpServletRequest httpRequest){
		logger.debug("In getSortedSampleNames");
		try {

			SampleBO sampleBO = (SampleBO) SpringApplicationContext.getBean(httpRequest, "sampleBO");
			
			if (!SpringSecurityUtil.isUserLoggedIn())
				return Response.status(Response.Status.UNAUTHORIZED).entity(Constants.MSG_SESSION_INVALID).build();
			
			List<String> names = sampleBO.getMatchedSampleNames(httpRequest, "");
			
			return 	Response.ok(names).build();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(CommonUtil.wrapErrorMessageInList("Error while deleting sample: " + e.getMessage())).build();
		}
	}
	
	@POST
	@Path("/deleteAccess")
	@Produces("application/json")
	 public Response deleteAccess(@Context HttpServletRequest httpRequest, SampleEditGeneralBean simpleSampleBean){
		logger.debug("In deleteAccess");
		try {

			SampleBO sampleBO = (SampleBO) SpringApplicationContext.getBean(httpRequest, "sampleBO");
			
			if (!SpringSecurityUtil.isUserLoggedIn())
				return Response.status(Response.Status.UNAUTHORIZED)
						.entity(Constants.MSG_SESSION_INVALID).build();
			
			SampleEditGeneralBean editBean = sampleBO.deleteAccess(simpleSampleBean, httpRequest);
			List<String> errors = editBean.getErrors();
			
			return (errors == null || errors.size() == 0) ?
					Response.ok(editBean).build() :
						Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errors).build();

		} catch (Exception e) {
			logger.error(e.getMessage());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(CommonUtil.wrapErrorMessageInList("Error while deleting Access from sample: " + e.getMessage())).build();
		}
	}
	
	@POST
	@Path("/submitForReview")
	@Produces("application/json")
	 public Response submitForReview(@Context HttpServletRequest httpRequest, DataReviewStatusBean reviewBean){
		logger.debug("In submitForReview");
		try {

			SampleBO sampleBO = (SampleBO) SpringApplicationContext.getBean(httpRequest, "sampleBO");
			
			if (!SpringSecurityUtil.isUserLoggedIn())
				return Response.status(Response.Status.UNAUTHORIZED).entity(Constants.MSG_SESSION_INVALID).build();
			
			String message = sampleBO.submitForReview(httpRequest, reviewBean);
			
			return Response.ok(message).build();

		} catch (Exception e) {
			logger.error(e.getMessage());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(CommonUtil.wrapErrorMessageInList("Error while submitting sample for review: " + e.getMessage())).build();
		}
	}
	
	@GET
	@Path("/getCurrentSampleName")
	@Produces ("application/json")
	 public Response getCurrentSampleName(@Context HttpServletRequest httpRequest, 
	    		@DefaultValue("") @QueryParam("sampleId") String sampleId){
		
		try { 
			SampleBO sampleBO = (SampleBO) SpringApplicationContext.getBean(httpRequest, "sampleBO");

			String sampelName = sampleBO.getCurrentSampleNameInSession(httpRequest, sampleId);
			
			return 
					Response.ok(sampelName)
/*					.header("Access-Control-Allow-Credentials", "true")
					.header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
					.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization")
*/					.build();
		} catch (Exception e) {
			//return Response.ok("Error while viewing the search results").build();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(CommonUtil.wrapErrorMessageInList(e.getMessage())).build();
		}
	}
	
	@GET
	@Path("/setupAdvancedSearch")
	@Produces ("application/json")
    public Response setupAdvancedSearch(@Context HttpServletRequest httpRequest)
	{
		logger.debug("In setupAdvancedSearch");		
		
		try { 
			AdvancedSampleSearchBO searchSampleBO = (AdvancedSampleSearchBO) SpringApplicationContext.getBean(httpRequest, "advancedSampleSearchBO");
			
			Map<String, Object> dropdownTypeLists = searchSampleBO.setup(httpRequest);
			//AdvancedSampleSearchBean searchBean = searchSampleBO.setup(httpRequest);

			return Response.ok(dropdownTypeLists).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(CommonUtil.wrapErrorMessageInList("Error while setting up drop down lists")).build();
		}
	}
	
	@GET
	@Path("/getDecoratedCharacterizationOptions")
	@Produces ("application/json")
    public Response getDecoratedCharacterizationOptions(@Context HttpServletRequest httpRequest, 
    		@DefaultValue("") @QueryParam("charType") String charType) {
		logger.debug("In getDecoratedCharacterizationOptions");		
		
		try { 
			CharacterizationManager characterizationMgr = 
					(CharacterizationManager) SpringApplicationContext.getBean(httpRequest, "characterizationManager");
			
			List<LabelValueBean> charOptions = characterizationMgr.getDecoratedCharacterizationOptions(httpRequest, charType);
					//searchSampleBO.setup(httpRequest);

			return Response.ok(charOptions).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(CommonUtil.wrapErrorMessageInList("Error while creating characterization options")).build();
		}
	}
	
	@GET
	@Path("/getDecoratedDatumOptions")
	@Produces ("application/json")
    public Response getDecoratedDatumOptions(@Context HttpServletRequest httpRequest, 
    		@DefaultValue("") @QueryParam("charType") String charType, 
    		@DefaultValue("") @QueryParam("charName") String charName) {
		logger.debug("In getDecoratedDatumOptions");		
		
		try { 
			CharacterizationResultManager characterizationMgr = 
					(CharacterizationResultManager) SpringApplicationContext.getBean(httpRequest, "characterizationResultManager");
			
			List<LabelValueBean> datumOptions = characterizationMgr.getDecoratedDatumNameOptions(
					httpRequest, charType, charName, null);

			return Response.ok(datumOptions).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(CommonUtil.wrapErrorMessageInList("Error while creating characterization datum options")).build();
		}
	}
	
	@GET
	@Path("/getDatumUnitOptions")
	@Produces ("application/json")
    public Response getDatumUnitOptions(@Context HttpServletRequest httpRequest, 
    		@DefaultValue("") @QueryParam("datumName") String datumName) {
		logger.debug("In getDatumUnitOptions");		
		
		try {
			CharacterizationResultManager characterizationResultManager = 
				(CharacterizationResultManager) SpringApplicationContext.getBean(httpRequest, "characterizationResultManager");
			
			List<String> names = characterizationResultManager
					.getColumnValueUnitOptions(httpRequest, datumName, "", false);
					
			return Response.ok(names).header("Access-Control-Allow-Credentials", "true")
						.header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
						.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(CommonUtil.wrapErrorMessageInList(e.getMessage())).build();
		}
	}
	
	@POST
	@Path("/searchSampleAdvanced")
	@Produces ("application/json")
	public Response searchSampleAdvanced(@Context HttpServletRequest httpRequest, AdvancedSampleSearchBean searchBean )
	{
		
		logger.debug("In searchSampleAdvanced");
		
		try {
			AdvancedSampleSearchBO searchSampleBO = (AdvancedSampleSearchBO) SpringApplicationContext.getBean(httpRequest, "advancedSampleSearchBO");
			
			SimpleAdvancedSearchResultView resultView = searchSampleBO.search(httpRequest, searchBean);
			
			
			if (resultView.getErrors().size() > 0) {
				logger.debug("Search sampel has error: " + resultView.getErrors().get(0));
				return Response.status(Response.Status.NOT_FOUND).entity(resultView.getErrors()).build();
			} else {
				logger.debug("Sample search successful");
				return Response.ok(resultView).build();
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(CommonUtil.wrapErrorMessageInList("Error while searching for samples: " + e.getMessage())).build();
		}
	}
	
	@GET
	@Path("/isSampleEditable")
	@Produces ("application/json")
	 public Response isSampleEditable(@Context HttpServletRequest httpRequest, @DefaultValue("") @QueryParam("sampleId") String sampleId){
		
		try { 
			SampleBO sampleBO =	(SampleBO) SpringApplicationContext.getBean(httpRequest, "sampleBO");

			boolean editable = sampleBO.isSampleEditableByCurrentUser(httpRequest, sampleId);
			
			return Response.ok(editable)
					.header("Access-Control-Allow-Credentials", "true")
					.header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
					.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();
			
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(CommonUtil.wrapErrorMessageInList("Error while evaluating if sample is editable by current user.")).build();
		}
	}
	
	@GET
	@Path("/summaryExport")
	@Produces ("application/vnd.ms-excel")
	 public Response summaryExport(@Context HttpServletRequest httpRequest, @Context HttpServletResponse httpResponse){
		
		try { 
			AdvancedSampleSearchBO searchSampleBO = (AdvancedSampleSearchBO) SpringApplicationContext.getBean(httpRequest, "advancedSampleSearchBO");
			
			 String result = searchSampleBO.export(httpRequest, httpResponse);
				
			return Response.ok("").build();
			
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while exporting the file" + e.getMessage())).build();

		}
	}
}

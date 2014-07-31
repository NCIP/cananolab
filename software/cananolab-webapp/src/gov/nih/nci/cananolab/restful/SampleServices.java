package gov.nih.nci.cananolab.restful;

import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationSummaryViewBean;
import gov.nih.nci.cananolab.restful.sample.CharacterizationBO;
import gov.nih.nci.cananolab.restful.sample.SampleBO;
import gov.nih.nci.cananolab.restful.sample.SearchSampleBO;
import gov.nih.nci.cananolab.restful.view.SimpleCharacterizationSummaryViewBean;
import gov.nih.nci.cananolab.restful.view.SimpleCharacterizationsByTypeBean;
import gov.nih.nci.cananolab.restful.view.SimpleSampleBean;
import gov.nih.nci.cananolab.restful.view.SimplePublicationWithSamplesBean;
import gov.nih.nci.cananolab.restful.view.edit.SampleEditGeneralBean;
import gov.nih.nci.cananolab.restful.view.edit.SimpleAccessBean;
import gov.nih.nci.cananolab.restful.view.edit.SimplePointOfContactBean;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.ui.form.SearchSampleForm;

import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

@Path("/sample")
public class SampleServices {
	private Logger logger = Logger.getLogger(SampleServices.class);
	
	@Inject
	ApplicationContext applicationContext;

	@GET
	@Path("/setup")
	@Produces ("application/json")
    public Response setup(@Context HttpServletRequest httpRequest) {
		System.out.println("In initSetup");		
		
		try { 
			SearchSampleBO searchSampleBO = 
					(SearchSampleBO) applicationContext.getBean("searchSampleBO");
			Map<String, List<String>> dropdownTypeLists = searchSampleBO.setup(httpRequest);

			return Response.ok(dropdownTypeLists).build();
		} catch (Exception e) {
			//return Response.ok("Error while setting up drop down lists").build();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error while setting up drop down lists").build();
		}
	}
	
	
	
	@GET
	@Path("/getCharacterizationByType")
	@Produces ("application/json")
    public Response getCharacterizationByType(@Context HttpServletRequest httpRequest, 
    		@DefaultValue("") @QueryParam("type") String type) {
		
		SearchSampleBO searchSampleBO = 
				(SearchSampleBO) applicationContext.getBean("searchSampleBO");
		
		try {
			List<String> characterizations = searchSampleBO.getCharacterizationByType(httpRequest, type);
			return Response.ok(characterizations).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error while getting characterization by type").build();
		}
	}
	
	@POST
	@Path("/searchSample")
	@Produces ("application/json")
	public Response searchSample(@Context HttpServletRequest httpRequest, SearchSampleForm searchForm ) {
		
		try {
			SearchSampleBO searchSampleBO = 
					(SearchSampleBO) applicationContext.getBean("searchSampleBO");
			
			List results = searchSampleBO.search(searchForm, httpRequest);
			
			Object result = results.get(0);
			if (result instanceof String) {
				return Response.status(Response.Status.NOT_FOUND).entity(result).build();
			} else {
				return Response.ok(results).build();
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error while searching for samples: " + e.getMessage()).build();
		}
	}
	
	
	
	@GET
	@Path("/view")
	@Produces ("application/json")
	 public Response view(@Context HttpServletRequest httpRequest, 
	    		@DefaultValue("") @QueryParam("sampleId") String sampleId){
		
		try { 

			SampleBO sampleBO = 
					(SampleBO) applicationContext.getBean("sampleBO");

			SampleBean sampleBean = sampleBO.summaryView(sampleId,httpRequest);
			SimpleSampleBean view = new SimpleSampleBean();
			view.transferSampleBeanForSummaryView(sampleBean);
			
			//return Response.ok(view).build();
			return Response.ok(view).header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();
			
		} catch (Exception e) {
			//return Response.ok("Error while viewing the search results").build();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error while viewing the search results").build();
		}
	}
	
	
	@GET
	@Path("/viewDataAvailability")
	@Produces ("application/json")
	 public Response viewDataAvailability(@Context HttpServletRequest httpRequest, 
	    		@DefaultValue("") @QueryParam("sampleId") String sampleId){
		
		try { 

			SampleBO sampleBO = 
					(SampleBO) applicationContext.getBean("sampleBO");

			SimpleSampleBean sampleBean = sampleBO.dataAvailabilityView(sampleId,httpRequest);
			
			//SimpleSampleBean view = new SimpleSampleBean();
			//view.transferSampleBeanForSummaryView(sampleBean);
			
			return Response.ok(sampleBean).build();
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error while getting data availability data").build();
		}
	}
	
	@GET
	@Path("/characterizationView")
	@Produces ("application/json")
	 public Response characterizationView(@Context HttpServletRequest httpRequest, 
	    		@DefaultValue("") @QueryParam("sampleId") String sampleId){
		try { 

			CharacterizationBO characterizationBO = 
					(CharacterizationBO) applicationContext.getBean("characterizationBO");

			CharacterizationSummaryViewBean charView = characterizationBO.summaryView(sampleId,httpRequest);
			SimpleCharacterizationSummaryViewBean viewBean = new SimpleCharacterizationSummaryViewBean();
			
			List<SimpleCharacterizationsByTypeBean> finalBean = viewBean.transferData(charView);
			
			return Response.ok(finalBean).header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();
			
		} catch (Exception e) {
			//return Response.ok(e.getMessage()).build();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
		
	}
	
	
	@GET
	@Path("/downloadImage")
	@Produces({"image/png", "application/json"})
	 public Response downloadImage(@Context HttpServletRequest httpRequest, @Context HttpServletResponse httpResponse,
	    		@DefaultValue("") @QueryParam("fileId") String fileId){
		try {
			CharacterizationBO characterizationBO = 
					(CharacterizationBO) applicationContext.getBean("characterizationBO");
			
			java.io.File file = characterizationBO.download(fileId, httpRequest);
			
			return Response.ok(new FileInputStream(file)).build();
			
		} catch (Exception ioe) {
			return Response.ok(ioe.getMessage()).build();
		}
	}
	
	@GET
	@Path("/download")
	@Produces({"image/png", "application/json"})
	 public Response download(@Context HttpServletRequest httpRequest, @Context HttpServletResponse httpResponse,
	    		@DefaultValue("") @QueryParam("fileId") String fileId){
		try {
			CharacterizationBO characterizationBO = 
					(CharacterizationBO) applicationContext.getBean("characterizationBO");
			
			String result = characterizationBO.download(fileId, httpRequest, httpResponse);
			return Response.ok(result).build();
		} 
		
		catch (Exception ioe) {
			return Response.ok(ioe.getMessage()).build();
		}
	}
	
	@GET
	@Path("/exportCharacterizationView")
	@Produces("application/json")
	 public Response exportCharacterizationView(@Context HttpServletRequest httpRequest, @Context HttpServletResponse httpResponse,
	    		@DefaultValue("") @QueryParam("sampleId") String sampleId, @QueryParam("type") String type){
	
		try {
			CharacterizationBO characterizationBO = 
					(CharacterizationBO) applicationContext.getBean("characterizationBO");
			
			String result = characterizationBO.summaryExport(sampleId, type, httpRequest, httpResponse);
			return Response.ok(result).build();
		} 
		
		catch (Exception ioe) {
			return Response.ok(ioe.getMessage()).build();
		}
	}
	
	@GET
	@Path("/edit")
	@Produces("application/json")
	 public Response edit(@Context HttpServletRequest httpRequest, 
	    		@DefaultValue("") @QueryParam("sampleId") String sampleId){
		
		SampleBO sampleBO = 
				(SampleBO) applicationContext.getBean("sampleBO");

		try {
			SampleEditGeneralBean sampleBean = sampleBO.summaryEdit(sampleId,httpRequest);
			return Response.ok(sampleBean).build();
		} 

		catch (Exception ioe) {
			return Response.status(Response.Status.BAD_REQUEST).entity(ioe.getMessage()).build();
		}
	}	
	
	@POST
	@Path("/savePOC")
	@Produces ("application/json")
	public Response savePOC(@Context HttpServletRequest httpRequest, SimplePointOfContactBean simplePOCBean) {
		logger.info("In savePOC");
		try {
			SampleBO sampleBO = 
					(SampleBO) applicationContext.getBean("sampleBO");
			
			UserBean user = (UserBean) (httpRequest.getSession().getAttribute("user"));
			if (user == null) 
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
						.entity("User session invalidate. Session may have been expired").build();
			
			SampleEditGeneralBean editBean = sampleBO.savePointOfContact(simplePOCBean, httpRequest);
			List<String> errors = editBean.getErrors();
			return (errors == null || errors.size() == 0) ?
					Response.ok(editBean).build() :
						Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errors).build();

		} catch (Exception e) {
			logger.error(e.getMessage());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error while saving Point of Contact: " + e.getMessage()).build();
		}
	}
	
	@POST
	@Path("/saveAccess")
	@Produces ("application/json")
	public Response saveAccess(@Context HttpServletRequest httpRequest, SimpleAccessBean simpleAccess) {
		logger.info("In savePOC");
		try {
			SampleBO sampleBO = 
					(SampleBO) applicationContext.getBean("sampleBO");
			
			UserBean user = (UserBean) (httpRequest.getSession().getAttribute("user"));
			if (user == null) 
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
						.entity("User session invalidate. Session may have been expired").build();
			
			SampleEditGeneralBean editBean = sampleBO.saveAccess(simpleAccess, httpRequest);
			List<String> errors = editBean.getErrors();
			return (errors == null || errors.size() == 0) ?
					Response.ok(editBean).build() :
						Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errors).build();

		} catch (Exception e) {
			logger.error(e.getMessage());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error while saving Access: " + e.getMessage()).build();
		}
	}
	
//	@GET
//	@Path("/searchById")
//	@Produces("application/json")
//	 public Response searchById(@Context HttpServletRequest httpRequest, 
//	    		@DefaultValue("") @QueryParam("id") String id, @QueryParam("type") String type){
//		
//		SampleBO sampleBO = 
//				(SampleBO) applicationContext.getBean("sampleBO");
//
//		try {
//			SimplePublicationWithSamplesBean result = sampleBO.searchSampleById(httpRequest, id, type);
//			
//			return (result.getErrors().size() > 0) ?
//					Response.status(Response.Status.INTERNAL_SERVER_ERROR)
//						.entity(result.getErrors()).build()
//						:
//						Response.ok(result).build();
//		} 
//
//		catch (Exception ioe) {
//			return Response.status(Response.Status.BAD_REQUEST).entity(ioe.getMessage()).build();
//		}
//	}	
}

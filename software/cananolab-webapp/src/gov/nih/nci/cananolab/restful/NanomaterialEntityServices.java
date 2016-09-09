package gov.nih.nci.cananolab.restful;

import gov.nih.nci.cananolab.dto.particle.composition.NanomaterialEntityBean;
import gov.nih.nci.cananolab.restful.SpringApplicationContext;
import gov.nih.nci.cananolab.restful.sample.NanomaterialEntityBO;
import gov.nih.nci.cananolab.restful.util.CommonUtil;
import gov.nih.nci.cananolab.restful.view.SimpleAdvacedSampleCompositionBean;
import gov.nih.nci.cananolab.restful.view.edit.SimpleNanomaterialEntityBean;
import gov.nih.nci.cananolab.security.utils.SpringSecurityUtil;

import java.util.List;
import java.util.Map;

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

@Path("/nanomaterialEntity")
public class NanomaterialEntityServices {
	
	private static final Logger logger = Logger.getLogger(NanomaterialEntityServices.class);
	
	@GET
	@Path("/setup")
	@Produces ("application/json")
    public Response setup(@Context HttpServletRequest httpRequest, @DefaultValue("") @QueryParam("sampleId") String sampleId) {
				
		try {
			NanomaterialEntityBO nanomaterialEntityBO = 
					(NanomaterialEntityBO) SpringApplicationContext.getBean(httpRequest, "nanomaterialEntityBO");
			Map<String, Object> dropdownMap = nanomaterialEntityBO.setupNew(sampleId, httpRequest);
			return Response.ok(dropdownMap).header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();

		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while setting up drop down lists" + e.getMessage())).build();

		}
	}
	
	@GET
	@Path("/edit")
	@Produces ("application/json")
    public Response edit(@Context HttpServletRequest httpRequest, @DefaultValue("") @QueryParam("sampleId") String sampleId, @DefaultValue("") @QueryParam("dataId") String dataId) {
				
		try { 
			NanomaterialEntityBO nanomaterialEntityBO = 
					(NanomaterialEntityBO) SpringApplicationContext.getBean(httpRequest, "nanomaterialEntityBO");
			if (!SpringSecurityUtil.isUserLoggedIn()) 
				return Response.status(Response.Status.UNAUTHORIZED)
						.entity("Session expired").build();
			
			SimpleNanomaterialEntityBean nano = nanomaterialEntityBO.setupUpdate(sampleId, dataId, httpRequest);
			
			List<String> errors = nano.getErrors();
			return (errors == null || errors.size() == 0) ?
					Response.ok(nano).build() :
						Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errors).build();
			
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while viewing the NanoMaterial Entity" + e.getMessage())).build();

		}
	}
	
	@POST
	@Path("/saveComposingElement")
	@Produces ("application/json")
    public Response saveComposingElement(@Context HttpServletRequest httpRequest, SimpleNanomaterialEntityBean nanoBean) {
				
		try { 
			NanomaterialEntityBO nanomaterialEntityBO = 
					(NanomaterialEntityBO) SpringApplicationContext.getBean(httpRequest, "nanomaterialEntityBO");
			if (!SpringSecurityUtil.isUserLoggedIn()) 
				return Response.status(Response.Status.UNAUTHORIZED)
						.entity("Session expired").build();
			
			SimpleNanomaterialEntityBean nano = nanomaterialEntityBO.saveComposingElement(nanoBean, httpRequest);
			
			List<String> errors = nano.getErrors();
			return (errors == null || errors.size() == 0) ?
					Response.ok(nano).build() :
						Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errors).build();
					
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while saving the composing element" + e.getMessage())).build();

		}
	}
	
	@POST
	@Path("/removeComposingElement")
	@Produces ("application/json")
    public Response removeComposingElement(@Context HttpServletRequest httpRequest, SimpleNanomaterialEntityBean nanoBean) {
				
		try { 
			NanomaterialEntityBO nanomaterialEntityBO = 
					(NanomaterialEntityBO) SpringApplicationContext.getBean(httpRequest, "nanomaterialEntityBO");
			if (!SpringSecurityUtil.isUserLoggedIn())
				return Response.status(Response.Status.UNAUTHORIZED)
						.entity("Session expired").build();
			
			SimpleNanomaterialEntityBean nano = nanomaterialEntityBO.removeComposingElement(nanoBean, httpRequest);
			List<String> errors = nano.getErrors();
			return (errors == null || errors.size() == 0) ?
					Response.ok(nano).build() :
						Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errors).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while removing the composing element" + e.getMessage())).build();

		}
	}
	
	@POST
	@Path("/saveFile")
	@Produces ("application/json")
    public Response saveFile(@Context HttpServletRequest httpRequest, SimpleNanomaterialEntityBean nanoBean) {
				
		try { 
			NanomaterialEntityBO nanomaterialEntityBO = 
					(NanomaterialEntityBO) SpringApplicationContext.getBean(httpRequest, "nanomaterialEntityBO");
			if (!SpringSecurityUtil.isUserLoggedIn())
				return Response.status(Response.Status.UNAUTHORIZED)
						.entity("Session expired").build();
			
			
			SimpleNanomaterialEntityBean nano = nanomaterialEntityBO.saveFile(nanoBean, httpRequest);
			
			
			List<String> errors = nano.getErrors();
			return (errors == null || errors.size() == 0) ?
					Response.ok(nano).build() :
						Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errors).build();
					
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while saving the file" + e.getMessage())).build();

		}
	}
	
	@POST
	@Path("/removeFile")
	@Produces ("application/json")
    public Response removeFile(@Context HttpServletRequest httpRequest, SimpleNanomaterialEntityBean nanoBean) {
				
		try { 
			NanomaterialEntityBO nanomaterialEntityBO = 
					(NanomaterialEntityBO) SpringApplicationContext.getBean(httpRequest, "nanomaterialEntityBO");
			if (!SpringSecurityUtil.isUserLoggedIn())
				return Response.status(Response.Status.UNAUTHORIZED)
						.entity("Session expired").build();
			
			
			SimpleNanomaterialEntityBean nano = nanomaterialEntityBO.removeFile(nanoBean, httpRequest);
			
			
			List<String> errors = nano.getErrors();
			return (errors == null || errors.size() == 0) ?
					Response.ok(nano).build() :
						Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errors).build();
					
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while removing the file" + e.getMessage())).build();

		}
	}
	
	@POST
	@Path("/submit")
	@Produces ("application/json")
    public Response submit(@Context HttpServletRequest httpRequest, SimpleNanomaterialEntityBean nanoBean) {
				
		try { 
			NanomaterialEntityBO nanomaterialEntityBO = (NanomaterialEntityBO) SpringApplicationContext.getBean(httpRequest, "nanomaterialEntityBO");
			if (!SpringSecurityUtil.isUserLoggedIn()) 
				return Response.status(Response.Status.UNAUTHORIZED)
						.entity("Session expired").build();
				
			List<String> msgs = nanomaterialEntityBO.create(nanoBean, httpRequest);
			
			return Response.ok(msgs).header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();
	
					
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while saving the nano material entity" + e.getMessage())).build();

		}
	}
	@POST
	@Path("/delete")
	@Produces ("application/json")
    public Response delete(@Context HttpServletRequest httpRequest, SimpleNanomaterialEntityBean nanoBean) {
				
		try { 
			NanomaterialEntityBO nanomaterialEntityBO = 
					(NanomaterialEntityBO) SpringApplicationContext.getBean(httpRequest, "nanomaterialEntityBO");
			if (!SpringSecurityUtil.isUserLoggedIn()) 
				return Response.status(Response.Status.UNAUTHORIZED)
						.entity("Session expired").build();
				
			List<String> msgs = nanomaterialEntityBO.delete(nanoBean, httpRequest);
			
			return Response.ok(msgs).header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();
	
					
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while deleting the nanomaterial entity " + e.getMessage())).build();

		}
	}
	@GET
	@Path("/viewDetails")
	@Produces ("application/json")
    public Response viewDetails(@Context HttpServletRequest httpRequest, @DefaultValue("") @QueryParam("sampleId") String sampleId, @DefaultValue("") @QueryParam("dataId") String dataId) {
				
		try { 
			NanomaterialEntityBO nanomaterialEntityBO = 
					(NanomaterialEntityBO) SpringApplicationContext.getBean(httpRequest, "nanomaterialEntityBO");
						
			NanomaterialEntityBean entityBean = nanomaterialEntityBO.setupNanoEntityForAdvSearch(sampleId, dataId, httpRequest);
			
			SimpleAdvacedSampleCompositionBean compBean = new SimpleAdvacedSampleCompositionBean();
			compBean.transferNanomaterialEntityForAdvancedSampleSearch(entityBean, httpRequest);
			
			return Response.ok(compBean).header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();

		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while viewing the NanoMaterial Entity" + e.getMessage())).build();

		}
	}
	
}


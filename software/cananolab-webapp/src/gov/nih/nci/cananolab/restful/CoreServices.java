package gov.nih.nci.cananolab.restful;

import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.common.Organization;
import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.restful.core.AccessibilityManager;
import gov.nih.nci.cananolab.restful.core.BaseAnnotationBO;
import gov.nih.nci.cananolab.restful.core.CustomPlugInBO;
import gov.nih.nci.cananolab.restful.core.InitSetup;
import gov.nih.nci.cananolab.restful.core.PointOfContactManager;
import gov.nih.nci.cananolab.restful.core.TabGenerationBO;
import gov.nih.nci.cananolab.restful.protocol.ProtocolBO;
import gov.nih.nci.cananolab.restful.util.CommonUtil;
import gov.nih.nci.cananolab.restful.util.SecurityUtil;
import gov.nih.nci.cananolab.restful.view.SimpleTabsBean;
import gov.nih.nci.cananolab.restful.view.SimpleWorkspaceBean;
import gov.nih.nci.cananolab.restful.view.edit.SimpleOrganizationBean;
import gov.nih.nci.cananolab.restful.view.edit.SimpleSubmitProtocolBean;
import gov.nih.nci.cananolab.restful.workspace.WorkspaceManager;
import gov.nih.nci.cananolab.service.security.UserBean;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.context.ApplicationContext;

@Path("/core")
public class CoreServices {
	
	@Inject
	ApplicationContext applicationContext;
	
	private Logger logger = Logger.getLogger(CoreServices.class);
	
	@GET
	@Path("/initSetup")
	@Produces ("application/json")
    public Response initSetup(@Context HttpServletRequest httpRequest) {
		System.out.println("In initSetup");		
		
//		CustomPlugInBO customPlugInBO = (CustomPlugInBO)applicationContext.getBean("customPlugInBO");
		ServletContext context = httpRequest.getSession(true).getServletContext();
//		try {
//			customPlugInBO.init(context);
//		} catch(Exception e) {
//			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
//		}
		
		InitSetup.getInstance().setPublicCountInContext(context);
		return Response.ok(context.getAttribute("publicCounts")).build();
	}
	
	@GET
	@Path("/getTabs")
	@Produces ("application/json")
    public Response getTabs(@Context HttpServletRequest httpRequest, 
    		@DefaultValue("") @QueryParam("homePage") String homePage) {
		
		logger.debug("In getTabs. SessionId: " + httpRequest.getSession().getId());
		
		
		TabGenerationBO tabGen = (TabGenerationBO)applicationContext.getBean("tabGenerationBO");
		SimpleTabsBean tabs = tabGen.getTabs(httpRequest, homePage);
		
		return Response.ok(tabs).build();
	}
	
	@GET
	@Path("/getCollaborationGroup")
	@Produces ("application/json")
    public Response getCollaborationGroup(@Context HttpServletRequest httpRequest,
    		@DefaultValue("") @QueryParam("searchStr") String searchStr) {
				
		try { 
			AccessibilityManager accManager = 
					 (AccessibilityManager) applicationContext.getBean("accessibilityManager");
			
			UserBean user = (UserBean) (httpRequest.getSession().getAttribute("user"));
			if (user == null) 
				return Response.status(Response.Status.UNAUTHORIZED)
						.entity("Session expired").build();
			
			String[] value = accManager.getMatchedGroupNames(searchStr, httpRequest);
			return Response.ok(value).header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();

		} catch (Exception e) {
			return Response.status(Response.Status.NOT_FOUND)
					.entity("Problem getting the Collaboration"+ e.getMessage()).build();
		}
	}

	@GET
	@Path("/getAllWorkspaceItems")
	@Produces ("application/json")
    public Response getAllWorkspaceItems(@Context HttpServletRequest httpRequest) {
				
		try { 
			WorkspaceManager manager = 
					 (WorkspaceManager) applicationContext.getBean("workspaceManager");
			
			if (! SecurityUtil.isUserLoggedIn(httpRequest))
				return Response.status(Response.Status.UNAUTHORIZED)
						.entity(SecurityUtil.MSG_SESSION_INVALID).build();
			
			SimpleWorkspaceBean value = manager.getWorkspaceItems(httpRequest);
			return (value.getErrors().size() == 0) ?
					Response.ok(value).build()
					:
						Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(value.getErrors()).build();
			//return Response.ok(value).header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();

		} catch (Exception e) {
			return Response.status(Response.Status.NOT_FOUND).entity("Problem getting the workspace items: "+ e.getMessage()).build();
		}
	}
	
	@GET
	@Path("/getWorkspaceItems")
	@Produces ("application/json")
    public Response getWorkspaceItems(@Context HttpServletRequest httpRequest, 
    		@DefaultValue("") @QueryParam("type") String type) {
				
		try { 
			WorkspaceManager manager = 
					 (WorkspaceManager) applicationContext.getBean("workspaceManager");
			
			if (! SecurityUtil.isUserLoggedIn(httpRequest))
				return Response.status(Response.Status.UNAUTHORIZED)
						.entity(SecurityUtil.MSG_SESSION_INVALID).build();
			
			SimpleWorkspaceBean value = manager.getWorkspaceItems(httpRequest, type);
			return (value.getErrors().size() == 0) ?
					Response.ok(value).build()
					:
						Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(value.getErrors()).build();
			//return Response.ok(value).header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();

		} catch (Exception e) {
			return Response.status(Response.Status.NOT_FOUND).entity("Problem getting the workspace items: "+ e.getMessage()).build();
		}
	}
	
	@GET
	@Path("/getUsers")
	@Produces ("application/json")
    public Response getUsers(@Context HttpServletRequest httpRequest,
    		@DefaultValue("") @QueryParam("searchStr") String searchStr, @DefaultValue("") @QueryParam("dataOwner") String dataOwner) {
				
		try { 
			AccessibilityManager accManager = 
					 (AccessibilityManager) applicationContext.getBean("accessibilityManager");
			
			UserBean user = (UserBean) (httpRequest.getSession().getAttribute("user"));
			if (user == null) 
				return Response.status(Response.Status.UNAUTHORIZED)
						.entity("Session expired").build();
			
			Map<String, String> value = accManager.getMatchedUsers(dataOwner, searchStr, httpRequest);
			return Response.ok(value).header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();

		} catch (Exception e) {
			return Response.status(Response.Status.NOT_FOUND).entity("Problem getting the users"+ e.getMessage()).build();
		}
	}
	
	@GET
	@Path("/getOrganizationByName")
	@Produces ("application/json")
    public Response getOrganizationByName(@Context HttpServletRequest httpRequest,
    		@DefaultValue("") @QueryParam("organizationName") String organizationName) {
		logger.info("In getOrganizationByName");
		try { 
			PointOfContactManager manager = 
					 (PointOfContactManager) applicationContext.getBean("pointOfContactManager");
			if (manager == null)
				logger.info("manager is null" );
			
			if (! SecurityUtil.isUserLoggedIn(httpRequest))
				return Response.status(Response.Status.UNAUTHORIZED).entity(SecurityUtil.MSG_SESSION_INVALID).build();
			
			logger.info("Calling getO");
			Organization domainOrg = manager.getOrganizationByName(httpRequest, organizationName.trim());
			SimpleOrganizationBean simpleOrg = new SimpleOrganizationBean();
			simpleOrg.transferOrganizationData(domainOrg);
			
			return Response.ok(simpleOrg).header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();

		} catch (Exception e) {
			return Response.status(Response.Status.NOT_FOUND)
					.entity("Error while getting data for organization: " + organizationName).build();
		}
	}
	
	@POST
	@Path("/uploadFile")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces ("application/json")
	public Response uploadFile(@Context HttpServletRequest httpRequest, @FormDataParam("myFile") InputStream fileInputStream,
            @FormDataParam("myFile") FormDataContentDisposition contentDispositionHeader) {
	
		try {
			ProtocolBO protocolBO = 
					(ProtocolBO) applicationContext.getBean("protocolBO");
			String fileName = contentDispositionHeader.getFileName();
			protocolBO.saveFile(fileInputStream,fileName,httpRequest);
			return Response.ok(fileName).header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();

					
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while submitting the protocol file " + e.getMessage())).build();
		}
		
	}

	
}

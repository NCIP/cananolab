package gov.nih.nci.cananolab.restful;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

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
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartInput;

import gov.nih.nci.cananolab.domain.common.Organization;
import gov.nih.nci.cananolab.dto.common.FavoriteBean;
import gov.nih.nci.cananolab.restful.core.AccessibilityManager;
import gov.nih.nci.cananolab.restful.core.InitSetup;
import gov.nih.nci.cananolab.restful.core.PointOfContactManager;
import gov.nih.nci.cananolab.restful.core.TabGenerationBO;
import gov.nih.nci.cananolab.restful.favorites.FavoritesBO;
import gov.nih.nci.cananolab.restful.protocol.ProtocolBO;
import gov.nih.nci.cananolab.restful.util.CommonUtil;
import gov.nih.nci.cananolab.restful.view.SimpleFavoriteBean;
import gov.nih.nci.cananolab.restful.view.SimpleTabsBean;
import gov.nih.nci.cananolab.restful.view.SimpleWorkspaceBean;
import gov.nih.nci.cananolab.restful.view.edit.SimpleOrganizationBean;
import gov.nih.nci.cananolab.restful.workspace.WorkspaceManager;
import gov.nih.nci.cananolab.security.CananoUserDetails;
import gov.nih.nci.cananolab.security.utils.SpringSecurityUtil;
import gov.nih.nci.cananolab.util.Constants;

@Path("/core")
public class CoreServices 
{
	private static final Logger logger = Logger.getLogger(CoreServices.class);
	
	@GET
	@Path("/initSetup")
	@Produces ("application/json")
	public Response initSetup(@Context HttpServletRequest httpRequest) 
	{
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
	public Response getTabs(@Context HttpServletRequest httpRequest, @DefaultValue("") @QueryParam("homePage") String homePage)
	{
		logger.info("In getTabs service");
		CananoUserDetails userDetails = SpringSecurityUtil.getPrincipal();

		TabGenerationBO tabGen = (TabGenerationBO) SpringApplicationContext.getBean(httpRequest, "tabGenerationBO");
		SimpleTabsBean tabs = tabGen.getTabs(httpRequest, userDetails, homePage);
		
		return Response.ok(tabs).build();
	}

	@GET
	@Path("/getCollaborationGroup")
	@Produces ("application/json")
	public Response getCollaborationGroup(@Context HttpServletRequest httpRequest,
			@DefaultValue("") @QueryParam("searchStr") String searchStr)
	{
		try { 
			AccessibilityManager accManager = (AccessibilityManager) SpringApplicationContext.getBean(httpRequest, "accessibilityManager");

			if (!SpringSecurityUtil.isUserLoggedIn()) 
				return Response.status(Response.Status.UNAUTHORIZED).entity(Constants.MSG_SESSION_INVALID).build();

			String[] value = accManager.getMatchedGroupNames(searchStr, httpRequest);
			return Response.ok(value).header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();

		} catch (Exception e) {
			return Response.status(Response.Status.NOT_FOUND).entity("Problem getting the Collaboration"+ e.getMessage()).build();
		}
	}

	@GET
	@Path("/getAllWorkspaceItems")
	@Produces ("application/json")
	public Response getAllWorkspaceItems(@Context HttpServletRequest httpRequest)
	{
		try { 
			WorkspaceManager manager = (WorkspaceManager) SpringApplicationContext.getBean(httpRequest, "workspaceManager");

			if (!SpringSecurityUtil.isUserLoggedIn())
				return Response.status(Response.Status.UNAUTHORIZED).entity(Constants.MSG_SESSION_INVALID).build();

			SimpleWorkspaceBean value = manager.getWorkspaceItems(httpRequest);
			return (value.getErrors().size() == 0) ? Response.ok(value).build() :
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
			@DefaultValue("") @QueryParam("type") String type) 
	{				
		try { 
			WorkspaceManager manager = (WorkspaceManager) SpringApplicationContext.getBean(httpRequest, "workspaceManager");

			if (!SpringSecurityUtil.isUserLoggedIn())
				return Response.status(Response.Status.UNAUTHORIZED).entity(Constants.MSG_SESSION_INVALID).build();

			SimpleWorkspaceBean value = manager.getWorkspaceItems(httpRequest, type);
			return (value.getErrors().size() == 0) ? Response.ok(value).build() :
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
			@DefaultValue("") @QueryParam("searchStr") String searchStr, @DefaultValue("") @QueryParam("dataOwner") String dataOwner)
	{
		try { 
			AccessibilityManager accManager = (AccessibilityManager) SpringApplicationContext.getBean(httpRequest, "accessibilityManager");

			if (!SpringSecurityUtil.isUserLoggedIn()) 
				return Response.status(Response.Status.UNAUTHORIZED).entity(Constants.MSG_SESSION_INVALID).build();

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
			@DefaultValue("") @QueryParam("organizationName") String organizationName)
	{
		logger.info("In getOrganizationByName");
		try { 
			PointOfContactManager manager = (PointOfContactManager) SpringApplicationContext.getBean(httpRequest, "pointOfContactManager");
			if (manager == null)
				logger.info("manager is null" );

			if (!SpringSecurityUtil.isUserLoggedIn())
				return Response.status(Response.Status.UNAUTHORIZED).entity(Constants.MSG_SESSION_INVALID).build();

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
	@Consumes("multipart/form-data")
	@Produces ("application/json")
	public Response uploadFile(@Context HttpServletRequest httpRequest, MultipartInput input)
	{
		try {
			ProtocolBO protocolBO = (ProtocolBO) SpringApplicationContext.getBean(httpRequest, "protocolBO");
			String fileName = null;
			InputStream fileInputStream = null;

			List<InputPart> parts = input.getParts();

			for (InputPart inputPart : parts) {
				MultivaluedMap<String, String> headers = inputPart.getHeaders();
				fileName = parseFileName(headers);
				fileInputStream = inputPart.getBody(InputStream.class,null);
			}

			protocolBO.saveFile(fileInputStream,fileName,httpRequest);
			return Response.ok(fileName).header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while submitting the protocol file " + e.getMessage())).build();
		}

	}

	private String parseFileName(MultivaluedMap<String, String> headers)
	{
		String[] contentDispositionHeader = headers.getFirst("Content-Disposition").split(";");
		String fileName = "";
		for (String name : contentDispositionHeader) {
			if ((name.trim().startsWith("filename"))) {
				String[] tmp = name.split("=");
				fileName = tmp[1].trim().replaceAll("\"","");	
			}
		}
		return fileName;
	}


	@GET
	@Path("/getFavorites")
	@Produces ("application/json")
	public Response getFavorites(@Context HttpServletRequest httpRequest, @QueryParam("id") Long id) {
		try
		{ 
			FavoritesBO favorite = (FavoritesBO) SpringApplicationContext.getBean(httpRequest, "favoritesBO");

			if (!SpringSecurityUtil.isUserLoggedIn())
				return Response.status(Response.Status.UNAUTHORIZED).entity(Constants.MSG_SESSION_INVALID).build();

			SimpleFavoriteBean simpleBean = favorite.findFavorites(httpRequest);

			return	Response.ok(simpleBean).header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();

		} catch (Exception e) {
			return Response.status(Response.Status.NOT_FOUND).entity("Problem getting the favorites : "+ e.getMessage()).build();
		}
	}

	@POST
	@Path("/addFavorite")
	@Produces ("application/json")
	public Response addFavorite(@Context HttpServletRequest httpRequest, FavoriteBean bean)
	{
		try
		{ 
			FavoritesBO favorite = (FavoritesBO) SpringApplicationContext.getBean(httpRequest, "favoritesBO");

			if (!SpringSecurityUtil.isUserLoggedIn())
				return Response.status(Response.Status.UNAUTHORIZED).entity(Constants.MSG_SESSION_INVALID).build();

			List<String> list = favorite.create(bean, httpRequest);
			return	Response.ok(list).header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();

		} catch (Exception e) {
			return Response.status(Response.Status.NOT_FOUND).entity("Problem adding the favorites : "+ e.getMessage()).build();
		}
	}
	
	@POST
	@Path("/deleteFavorite")
	@Produces ("application/json")
	public Response deleteFavorite(@Context HttpServletRequest httpRequest, FavoriteBean bean)
	{
		try
		{ 
			FavoritesBO favorite = (FavoritesBO) SpringApplicationContext.getBean(httpRequest, "favoritesBO");

			if (!SpringSecurityUtil.isUserLoggedIn())
				return Response.status(Response.Status.UNAUTHORIZED).entity(Constants.MSG_SESSION_INVALID).build();

			SimpleFavoriteBean simpleBean = favorite.delete(bean, httpRequest);
			return	Response.ok(simpleBean).header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();
		} catch (Exception e) {
			return Response.status(Response.Status.NOT_FOUND).entity("Problem while deleting the favorites from the list : "+ e.getMessage()).build();
		}
	}
}

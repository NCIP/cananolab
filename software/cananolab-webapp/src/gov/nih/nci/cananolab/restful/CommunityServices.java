package gov.nih.nci.cananolab.restful;

import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.dto.common.CollaborationGroupBean;
import gov.nih.nci.cananolab.dto.common.DataReviewStatusBean;
import gov.nih.nci.cananolab.restful.community.CollaborationGroupBO;
import gov.nih.nci.cananolab.restful.community.CollaborationGroupManager;
import gov.nih.nci.cananolab.restful.context.SpringApplicationContext;
import gov.nih.nci.cananolab.restful.util.SecurityUtil;
import gov.nih.nci.cananolab.ui.form.SearchProtocolForm;

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
import org.springframework.context.support.ClassPathXmlApplicationContext;

@Path("/community")
public class CommunityServices {
	
//	@Inject
//	ApplicationContext applicationContext;
	ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext-strutsless.xml");
	private Logger logger = Logger.getLogger(CommunityServices.class);
	
	@GET
	@Path("/getCollaborationGroups")
	@Produces ("application/json")
    public Response getCollaborationGroups(@Context HttpServletRequest httpRequest) {
		logger.info("In getCollaborationGroups");
				
		try { 
			CollaborationGroupBO collGroupBO = 
					 (CollaborationGroupBO) applicationContext.getBean("collaborationGroupBO");
			
			if (! SecurityUtil.isUserLoggedIn(httpRequest))
				return Response.status(Response.Status.UNAUTHORIZED)
						.entity(SecurityUtil.MSG_SESSION_INVALID).build();
			
			List<CollaborationGroupBean> existingGroups = collGroupBO.getExistingGroups(httpRequest);
			
			return Response.ok(existingGroups).header("Access-Control-Allow-Credentials", "true")
					.header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
					.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();

		} catch (Exception e) {
			return Response.status(Response.Status.NOT_FOUND)
					.entity("Problem getting the collaboration groups: "+ e.getMessage()).build();
		}
	}

	@POST
	@Path("/addCollaborationGroups")
	@Produces ("application/json")
    public Response addCollaborationGroups(@Context HttpServletRequest httpRequest, CollaborationGroupBean groupBean) {
		logger.info("In getCollaborationGroups");
				
		try { 
			CollaborationGroupBO collGroupBO = 
					 (CollaborationGroupBO) applicationContext.getBean("collaborationGroupBO");
			
			if (! SecurityUtil.isUserLoggedIn(httpRequest))
				return Response.status(Response.Status.UNAUTHORIZED)
						.entity(SecurityUtil.MSG_SESSION_INVALID).build();
			 
						
			List<CollaborationGroupBean> beans = collGroupBO.create(groupBean, httpRequest);
			
			return Response.ok(beans).header("Access-Control-Allow-Credentials", "true")
					.header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
					.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();

		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("Problem creating the collaboration groups: "+ e.getMessage()).build();
		}
	}
	
	@POST
	@Path("/deleteCollaborationGroups")
	@Produces ("application/json")
    public Response deleteCollaborationGroups(@Context HttpServletRequest httpRequest, CollaborationGroupBean groupBean) {
		logger.info("In getCollaborationGroups");
				
		try { 
			CollaborationGroupBO collGroupBO = 
					 (CollaborationGroupBO) applicationContext.getBean("collaborationGroupBO");
			
			if (! SecurityUtil.isUserLoggedIn(httpRequest))
				return Response.status(Response.Status.UNAUTHORIZED)
						.entity(SecurityUtil.MSG_SESSION_INVALID).build();
			 
						
			List<CollaborationGroupBean> beans = collGroupBO.delete(groupBean, httpRequest);
			
			return Response.ok(beans).header("Access-Control-Allow-Credentials", "true")
					.header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
					.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();

		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("Problem deleting the collaboration groups: "+ e.getMessage()).build();
		}
	}
	
	@GET
	@Path("/editCollaborationGroup")
	@Produces ("application/json")
    public Response editCollaborationGroup(@Context HttpServletRequest httpRequest, @DefaultValue("") @QueryParam("groupId") String groupId) {
		logger.info("In editCollaborationGroup");
				
		try { 
			CollaborationGroupManager collGroupManager = 
					 (CollaborationGroupManager) applicationContext.getBean("collaborationGroupManger");
			
			if (! SecurityUtil.isUserLoggedIn(httpRequest))
				return Response.status(Response.Status.UNAUTHORIZED)
						.entity(SecurityUtil.MSG_SESSION_INVALID).build();
			
			CollaborationGroupBean collaborationBean = collGroupManager.getCollaborationGroupById(httpRequest, groupId);
			
			return Response.ok(collaborationBean).header("Access-Control-Allow-Credentials", "true")
					.header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
					.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();

		} catch (Exception e) {
			return Response.status(Response.Status.NOT_FOUND)
					.entity("Problem getting the collaboration group by ID: "+ e.getMessage()).build();
		}
	}
	
	@POST
	@Path("/addUserAccess")
	@Produces ("application/json")
    public Response addUserAccess(@Context HttpServletRequest httpRequest, AccessibilityBean userAccess) {
		logger.info("In addUserAccess for Collaboration group");
				
		try { 
			CollaborationGroupManager collGroupManager = 
					 (CollaborationGroupManager) applicationContext.getBean("collaborationGroupManger");
			
			if (! SecurityUtil.isUserLoggedIn(httpRequest))
				return Response.status(Response.Status.UNAUTHORIZED)
						.entity(SecurityUtil.MSG_SESSION_INVALID).build();
			
			CollaborationGroupBean collaborationBean = collGroupManager.addUserAccess(httpRequest, userAccess);
			
			return Response.ok(collaborationBean).header("Access-Control-Allow-Credentials", "true")
					.header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
					.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();

		} catch (Exception e) {
			return Response.status(Response.Status.NOT_FOUND)
					.entity("Error while adding the user to collaboration group : "+ e.getMessage()).build();
		}
	}
	
	@POST
	@Path("/deleteUserAccess")
	@Produces ("application/json")
    public Response deleteUserAccess(@Context HttpServletRequest httpRequest, AccessibilityBean userAccess) {
		logger.info("In addUserAccess for Collaboration group");
				
		try { 
			CollaborationGroupManager collGroupManager = 
					 (CollaborationGroupManager) applicationContext.getBean("collaborationGroupManger");
			
			if (! SecurityUtil.isUserLoggedIn(httpRequest))
				return Response.status(Response.Status.UNAUTHORIZED)
						.entity(SecurityUtil.MSG_SESSION_INVALID).build();
			
			CollaborationGroupBean collaborationBean = collGroupManager.deleteUserAccess(httpRequest, userAccess);
			
			return Response.ok(collaborationBean).header("Access-Control-Allow-Credentials", "true")
					.header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
					.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();

		} catch (Exception e) {
			return Response.status(Response.Status.NOT_FOUND)
					.entity("Error while removing user from the collaboration group : "+ e.getMessage()).build();
		}
	}
	@GET
	@Path("/setupNew")
	@Produces ("application/json")
    public Response setupNew(@Context HttpServletRequest httpRequest) {
		logger.info("setup new CollaborationGroups");
				
		try { 
			CollaborationGroupBO collGroupBO = 
					 (CollaborationGroupBO) applicationContext.getBean("collaborationGroupBO");
			
			if (! SecurityUtil.isUserLoggedIn(httpRequest))
				return Response.status(Response.Status.UNAUTHORIZED)
						.entity(SecurityUtil.MSG_SESSION_INVALID).build();
			
			collGroupBO.setupNew(httpRequest);
			
			return Response.ok("").header("Access-Control-Allow-Credentials", "true")
					.header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
					.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();

		} catch (Exception e) {
			return Response.status(Response.Status.NOT_FOUND)
					.entity("Problem setting up the new collaboration groups: "+ e.getMessage()).build();
		}
	}

}

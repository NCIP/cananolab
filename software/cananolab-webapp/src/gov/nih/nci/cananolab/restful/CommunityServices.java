package gov.nih.nci.cananolab.restful;

import gov.nih.nci.cananolab.restful.bean.SimpleCollaborationGroup;
import gov.nih.nci.cananolab.restful.community.CollaborationGroupBO;
import gov.nih.nci.cananolab.restful.util.SecurityUtil;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

@Path("/community")
public class CommunityServices {
	
	@Inject
	ApplicationContext applicationContext;
	
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
			
			List<SimpleCollaborationGroup> existingGroups = collGroupBO.getExistingGroups(httpRequest);
			
			return Response.ok(existingGroups).header("Access-Control-Allow-Credentials", "true")
					.header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
					.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();

		} catch (Exception e) {
			return Response.status(Response.Status.NOT_FOUND)
					.entity("Problem getting the collaboration groups: "+ e.getMessage()).build();
		}
	}

}

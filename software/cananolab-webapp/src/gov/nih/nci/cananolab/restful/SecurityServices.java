package gov.nih.nci.cananolab.restful;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import gov.nih.nci.cananolab.security.CananoUserDetails;
import gov.nih.nci.cananolab.security.utils.SpringSecurityUtil;

@Path("/security")
public class SecurityServices
{
	private static final Logger logger = Logger.getLogger(SecurityServices.class);
		
	@GET
	@Path("/getUserGroups")
	@Produces ("application/json")
    public Response getUserGroups(@Context HttpServletRequest httpRequest)
	{
		logger.info("In getUserGroups service");
		CananoUserDetails userDetails = SpringSecurityUtil.getPrincipal();
		Map<String, List<String>> userGroups = new HashMap<String, List<String>>();
		
		if (userDetails != null)
		{
			userGroups.put(userDetails.getUsername(), userDetails.getGroups());
		}
		
		//return Response.status(Response.Status.NOT_FOUND).entity("Unable to get userGroup due to unknown reason.").build();
		return Response.ok(userGroups).build();
	}
	
}

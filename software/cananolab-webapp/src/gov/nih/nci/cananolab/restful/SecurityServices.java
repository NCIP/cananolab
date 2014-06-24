package gov.nih.nci.cananolab.restful;

import java.util.List;
import java.util.Set;

import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.restful.helper.SecurityHelper;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

@Path("/security")
public class SecurityServices {
	private Logger logger = Logger.getLogger(SecurityServices.class);
	
	SecurityHelper helper = new SecurityHelper();

	@GET
	@Path("/login")
	@Produces ("application/json")
    public Response login(@Context HttpServletRequest httpRequest, 
    		@DefaultValue("") @QueryParam("username") String username, 
    		@DefaultValue("") @QueryParam("password") String password) {
		
		logger.info("In login service");
		
		if (username.length() == 0 || password.length() == 0)
			return Response.serverError().entity("User name or password can't be blank").build();
		
		String result = helper.checkLogin(username, password, httpRequest);
		
		if (!result.equals(RestfulConstants.SUCCESS)) 
			return Response.status(Response.Status.NOT_FOUND).entity("Invalid user name and /or password").build();
		return Response.ok(httpRequest.getSession().getId()).build();
    }
	
	@GET
	@Path("/register")
	@Produces ("application/json")
    public Response register(@Context HttpServletRequest httpRequest,
    		@DefaultValue("") @QueryParam("title") String title, 
    		@DefaultValue("") @QueryParam("firstName") String firstName,
    		@DefaultValue("") @QueryParam("lastName") String lastName,
    		@DefaultValue("") @QueryParam("email") String email,
    		@DefaultValue("") @QueryParam("phone") String phone,
    		@DefaultValue("") @QueryParam("organization") String organization,
    		@DefaultValue("") @QueryParam("fax") String fax,
    		@DefaultValue("") @QueryParam("comment") String comment,
    		@DefaultValue("") @QueryParam("registerToUserList") String registerToUserList) {
        
		logger.info("In register service");
		
		String result = helper.register(title, firstName, lastName, email, phone, organization, fax, comment, registerToUserList);
		
		return Response.ok(result).build();
    }
	
	@GET
	@Path("/logout")
	@Produces ("application/json")
    public Response logout(@Context HttpServletRequest httpRequest) {
		logger.info("In logout service");
		
		HttpSession session = httpRequest.getSession(false);
		if (session == null) 
			return Response.ok("Null session. No need to log out").build();
		session.invalidate();
		return Response.ok("Logout successful").build();
	}
	
	@GET
	@Path("/getUserGroups")
	@Produces ("application/json")
    public Response getUserGroups(@Context HttpServletRequest httpRequest) {
		logger.info("In getUser service");
		
		HttpSession session = httpRequest.getSession(false);
		if (session == null) 
			return Response.ok(null).build();
		
		UserBean user = (UserBean)session.getAttribute("user");
		SecurityService service = (SecurityService) session.getAttribute("securityService");
		
		if (user != null && service != null) {
			try {
				List<String> groups = service.getGroupNamesForUser(user.getUserId());
				return Response.ok(groups).build();
			} catch (Exception e) {
				//logger.error("Erro while logging in user: " + username + "|" + password + ". " + e.getMessage());
				return Response.ok("Unable to get group for user").build();
			}
		}
		
		return Response.ok(null).build();
	}
}

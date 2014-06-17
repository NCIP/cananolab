package gov.nih.nci.cananolab.restful;

import gov.nih.nci.cananolab.restful.helper.SecurityHelper;

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

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
	@GET
	@Path("/login")
	@Produces ("application/json")
    public Response login(@Context HttpServletRequest httpRequest, 
    		@DefaultValue("") @QueryParam("username") String username, 
    		@DefaultValue("") @QueryParam("password") String password) {
		
		logger.info("In login service");
		
		if (username.length() == 0 || password.length() == 0)
			return Response.serverError().entity("User name or password can't be blank").build();
		
		String result = helper.checkLogin(username, password);
		
		if (!result.equals(RestfulConstants.SUCCESS)) 
			return Response.status(Response.Status.NOT_FOUND).entity("Invalid user name and /or password").build();
			//return Response.serverError().entity("Login failed").build();
		
		HttpSession session = httpRequest.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		session = httpRequest.getSession(true);
		return Response.ok(session.getId()).build();
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
	@Path("/updatePassword")
	@Produces ("application/json")
    public Response updatePassword(@Context HttpServletRequest httpRequest,
    		@DefaultValue("") @QueryParam("sessionId") String sessionId,
    		@DefaultValue("") @QueryParam("loginId") String loginId, 
    		@DefaultValue("") @QueryParam("password") String password,
    		@DefaultValue("") @QueryParam("newPassword") String newPassword) {
		logger.info("In updatePassword service");
		
		HttpSession session = httpRequest.getSession(false);
		if (session == null)
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("No existing session for this request").build();
		
		if (!sessionId.equals(session.getId()))
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Invalid session").build();
		
		String result = helper.updatePassword(loginId, password, newPassword);
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
}

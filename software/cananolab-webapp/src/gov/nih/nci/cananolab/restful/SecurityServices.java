package gov.nih.nci.cananolab.restful;

import gov.nih.nci.cananolab.restful.context.SpringApplicationContext;
import gov.nih.nci.cananolab.restful.security.LoginBO;
import gov.nih.nci.cananolab.restful.security.LogoutBO;
import gov.nih.nci.cananolab.restful.security.RegisterUserBO;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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

@Path("/security")
public class SecurityServices {
	private Logger logger = Logger.getLogger(SecurityServices.class);
	
	@Inject
	SpringApplicationContext applicationContext;
	
	/*
	 * TODO: Need input validation logic
	 * 
	 *    Ref. to validation.xml
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
		
		LoginBO loginBo = (LoginBO) SpringApplicationContext.getBean("loginBO");
		
		String result = loginBo.login(username, password, httpRequest);
		logger.info("login sessionid: " + httpRequest.getSession().getId());
		if (!result.equals(RestfulConstants.SUCCESS)) 
			return Response.status(Response.Status.NOT_FOUND).entity("Login ID or password is invalid").build();
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
		
		RegisterUserBO registerBo = (RegisterUserBO) SpringApplicationContext.getBean("registerUserBO");
		List<String> errors = registerBo.register(title, firstName, lastName, email, phone, organization, fax, comment, registerToUserList);
		
		return (errors == null || errors.size() == 0) ? Response.ok("success").build() : 
			Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errors).build();
    }
	
	@GET
	@Path("/logout")
	@Produces ("application/json")
    public Response logout(@Context HttpServletRequest httpRequest) {
		logger.info("In logout service");
		
		LogoutBO logoutBo = (LogoutBO) SpringApplicationContext.getBean("logoutBO");
		String result = logoutBo.logout(httpRequest);
		return Response.ok(result).build();
	}
	
	@GET
	@Path("/getUserGroups")
	@Produces ("application/json")
    public Response getUserGroups(@Context HttpServletRequest httpRequest) {
		logger.info("In getUser service");
		
		HttpSession session = httpRequest.getSession(false);
		if (session == null) 
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("No valid user session to complete request").build();
		
		UserBean user = (UserBean)session.getAttribute("user");
		SecurityService service = (SecurityService) session.getAttribute("securityService");
		
		if (user != null && service != null) {
			try {
				List<String> groups = service.getGroupNamesForUser(user.getUserId());
				
				Map<String, List<String>> userGroups = new HashMap<String, List<String>>();
				userGroups.put(user.getLoginName(), groups);
				return Response.ok(userGroups).build();
			} catch (Exception e) {
				//logger.error("Erro while logging in user: " + username + "|" + password + ". " + e.getMessage());
				return Response.status(Response.Status.NOT_FOUND)
						.entity("Unable to get group for user. Error: " + e.getMessage()).build();
			}
		}
		
		return Response.status(Response.Status.NOT_FOUND)
				.entity("Unable to get userGroup due to unknown reason.").build();
	}
	
	@POST
	@Path("/resetPassword")
	@Produces ("application/json")
    public Response resetPassword(@Context HttpServletRequest httpRequest, 
    		@DefaultValue("") @QueryParam("username") String username, 
    		@DefaultValue("") @QueryParam("oldPassword") String oldPassword,
    		@DefaultValue("") @QueryParam("newPassword") String newPassword) {
		
		logger.info("In password reset service");
		
		if (username.length() == 0 || newPassword.length() == 0)
			return Response.serverError().entity("username or password can't be blank").build();
		
		LoginBO loginBo = (LoginBO) SpringApplicationContext.getBean("loginBO");
		
		String result = loginBo.updatePassword(username, oldPassword, newPassword);
		logger.info("login sessionid: " + httpRequest.getSession().getId());
		if (!result.equals(RestfulConstants.SUCCESS)) 
			return Response.status(Response.Status.NOT_FOUND).entity("Password reset failed").build();
		return Response.ok(result).build();
    }
	
}

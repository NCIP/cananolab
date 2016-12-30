package gov.nih.nci.cananolab.restful;

import java.util.ArrayList;
import java.util.List;

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
import org.springframework.beans.factory.annotation.Autowired;

import gov.nih.nci.cananolab.restful.useraccount.UserAccountBO;
import gov.nih.nci.cananolab.restful.util.CommonUtil;
import gov.nih.nci.cananolab.security.CananoUserDetails;

@Path("/useraccount")
public class UserAccountServices 
{
	private static final Logger logger = Logger.getLogger(UserAccountServices.class);
	
	@Autowired
	private UserAccountBO userAccountBO;

	@GET
	@Path("/read")
	@Produces ("application/json")
	public Response readUserAccount(@Context HttpServletRequest httpRequest, @DefaultValue("") @QueryParam("username") String username)
	{
		try
		{ 
			CananoUserDetails userDetails = userAccountBO.readUserAccount(username);
			return Response.ok(userDetails).build();
		}
		catch (Exception e) {
			logger.error("Error in reading user account : ", e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(CommonUtil.wrapErrorMessageInList(e.getMessage())).build();
		}
	}

	@GET
	@Path("/search")
	@Produces ("application/json")
	public Response searchUserAccounts(@Context HttpServletRequest httpRequest, @DefaultValue("") @QueryParam("username") String username)
	{
		try
		{
			List<CananoUserDetails> userDetailsList = new ArrayList<CananoUserDetails>();
			userDetailsList = userAccountBO.searchByUsername(username);
			return Response.ok(userDetailsList).build();
		}
		catch (Exception e) {
			logger.error("Error in searching for user accounts : ", e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(CommonUtil.wrapErrorMessageInList(e.getMessage())).build();
		}
	}

	@POST
	@Path("/resetpwd")
	@Produces ("application/json")
	public Response resetPassword(@Context HttpServletRequest httpRequest, @DefaultValue("") @QueryParam("username") String username,
								  @DefaultValue("") @QueryParam("oldpassword") String oldpassword, @DefaultValue("") @QueryParam("newpassword") String newpassword)
	{
		try
		{
			userAccountBO.resetUserAccountPassword(oldpassword, newpassword, username);
			return Response.ok("success").build();
		}
		catch (Exception e) {
			logger.error("Error in searching for user accounts : ", e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(CommonUtil.wrapErrorMessageInList(e.getMessage())).build();
		}
	}
	
	@POST
	@Path("/create")
	@Produces ("application/json")
	public Response createUserAccount(@Context HttpServletRequest httpRequest, CananoUserDetails userDetails)
	{
		try
		{
			CananoUserDetails newUserDetails = userAccountBO.createUserAccount(userDetails);
			return Response.ok(newUserDetails).build();
		}
		catch (Exception e) {
			logger.error("Error in searching for user accounts : ", e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(CommonUtil.wrapErrorMessageInList(e.getMessage())).build();
		}
	}
	
	@POST
	@Path("/update")
	@Produces ("application/json")
	public Response updateUserAccount(@Context HttpServletRequest httpRequest, CananoUserDetails userDetails)
	{
		try
		{
			CananoUserDetails newUserDetails = userAccountBO.updateUserAccount(userDetails);
			
			return Response.ok(newUserDetails).build();
		}
		catch (Exception e) {
			logger.error("Error in searching for user accounts : ", e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(CommonUtil.wrapErrorMessageInList(e.getMessage())).build();
		}
	}

}

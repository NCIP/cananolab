package gov.nih.nci.cananolab.restful;

import gov.nih.nci.cananolab.restful.security.Login;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("/services")
public class SecurityServices {
	
	Login strutsbridge = new Login();

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
	@GET
	@Path("/login")
	@Produces ("application/json")
    public String login(@DefaultValue("") @QueryParam("username") String username, 
    		@DefaultValue("") @QueryParam("password") String password) {
        
		return strutsbridge.checkLogin(username, password);
    }
}

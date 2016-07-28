package gov.nih.nci.cananolab.restful;

import gov.nih.nci.cananolab.restful.sample.CompositionFileBO;
import gov.nih.nci.cananolab.restful.util.CommonUtil;
import gov.nih.nci.cananolab.restful.view.edit.SimpleFileBean;
import gov.nih.nci.cananolab.security.utils.SpringSecurityUtil;
import gov.nih.nci.cananolab.util.Constants;

import java.util.List;
import java.util.Map;

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

@Path("/compositionFile")
public class CompositionFileServices {

	private static final Logger logger = Logger.getLogger(CompositionFileServices.class);
	
	@GET
	@Path("/setup")
	@Produces ("application/json")
    public Response setup(@Context HttpServletRequest httpRequest)
	{		
		try { 
			CompositionFileBO compBO = (CompositionFileBO) SpringApplicationContext.getBean(httpRequest, "compositionFileBO");
			Map<String, Object> dropdownMap = compBO.setupNew(httpRequest);
			return Response.ok(dropdownMap).header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while setting up drop down lists" + e.getMessage())).build();

		}
	}
	
	@GET
	@Path("/edit")
	@Produces ("application/json")
    public Response edit(@Context HttpServletRequest httpRequest, @DefaultValue("") @QueryParam("sampleId") String sampleId, @DefaultValue("") @QueryParam("dataId") String dataId)
	{
				
		try { 
			CompositionFileBO compBO = (CompositionFileBO) SpringApplicationContext.getBean(httpRequest, "compositionFileBO");
			if (SpringSecurityUtil.getPrincipal() == null) 
				return Response.status(Response.Status.UNAUTHORIZED).entity(Constants.MSG_SESSION_INVALID).build();
			
			SimpleFileBean bean = compBO.setupUpdate(sampleId, dataId, httpRequest);
			
			List<String> errors = bean.getErrors();
			return (errors == null || errors.size() == 0) ?
					Response.ok(bean).build() :
						Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errors).build();
			
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while viewing the Composition File " + e.getMessage())).build();

		}
	}
	
	@POST
	@Path("/submit")
	@Produces ("application/json")
    public Response submit(@Context HttpServletRequest httpRequest, SimpleFileBean fileBean)
	{			
		try { 
			CompositionFileBO compBO = (CompositionFileBO) SpringApplicationContext.getBean(httpRequest, "compositionFileBO");
			if (SpringSecurityUtil.getPrincipal() == null) 
				return Response.status(Response.Status.UNAUTHORIZED).entity(Constants.MSG_SESSION_INVALID).build();
			
			List<String> msgs = compBO.create(fileBean, httpRequest);
			
			return Response.ok(msgs).header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();		
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while submitting the Composition File" + e.getMessage())).build();
		}
	}
	
	
	@POST
	@Path("/delete")
	@Produces ("application/json")
    public Response delete(@Context HttpServletRequest httpRequest, SimpleFileBean fileBean)
	{
		try { 
			CompositionFileBO compBO = (CompositionFileBO) SpringApplicationContext.getBean(httpRequest, "compositionFileBO");
			if (SpringSecurityUtil.getPrincipal() == null)
				return Response.status(Response.Status.UNAUTHORIZED).entity(Constants.MSG_SESSION_INVALID).build();
			
			List<String> msgs = compBO.delete(fileBean, httpRequest);
			
			return Response.ok(msgs).header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while deleting the Functionalizing Entity" + e.getMessage())).build();
		}
	}
	
}

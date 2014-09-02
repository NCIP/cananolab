package gov.nih.nci.cananolab.restful;

import gov.nih.nci.cananolab.restful.sample.FunctionalizingEntityBO;
import gov.nih.nci.cananolab.restful.sample.NanomaterialEntityBO;
import gov.nih.nci.cananolab.restful.util.CommonUtil;

import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

@Path("/functionalizingEntity")
public class FunctionalizingEntityServices {

private Logger logger = Logger.getLogger(FunctionalizingEntityServices.class);
	
	@Inject
	ApplicationContext applicationContext;
	
	@GET
	@Path("/setup")
	@Produces ("application/json")
    public Response setup(@Context HttpServletRequest httpRequest, @DefaultValue("") @QueryParam("sampleId") String sampleId) {
				
		try { 
			FunctionalizingEntityBO functionalizingEntity = 
					(FunctionalizingEntityBO) applicationContext.getBean("functionalizingEntityBO");
			Map<String, Object> dropdownMap = functionalizingEntity.setupNew(sampleId, httpRequest);
			return Response.ok(dropdownMap).header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();

		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while setting up drop down lists" + e.getMessage())).build();

		}
	}
}

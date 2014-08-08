package gov.nih.nci.cananolab.restful;

import java.util.Map;

import gov.nih.nci.cananolab.dto.common.PublicationSummaryViewBean;
import gov.nih.nci.cananolab.restful.protocol.SearchProtocolBO;
import gov.nih.nci.cananolab.restful.publication.PublicationBO;
import gov.nih.nci.cananolab.restful.publication.SearchPublicationBO;
import gov.nih.nci.cananolab.restful.util.CommonUtil;
import gov.nih.nci.cananolab.restful.view.SimplePublicationSummaryViewBean;

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

@Path("/protocol")
public class ProtocolServices {
private Logger logger = Logger.getLogger(ProtocolServices.class);
	
	@Inject
	ApplicationContext applicationContext;
	@GET
	@Path("/setup")
	@Produces ("application/json")
    public Response setup(@Context HttpServletRequest httpRequest) {
				
		try { 
			SearchProtocolBO searchProtocolBO = 
					(SearchProtocolBO) applicationContext.getBean("searchProtocolBO");
			Map<String, Object> dropdownMap = searchProtocolBO.setup(httpRequest);
			return Response.ok(dropdownMap).header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();

			// return Response.ok(dropdownMap).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while setting up drop down lists" + e.getMessage())).build();

		}
	}
}

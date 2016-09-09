package gov.nih.nci.cananolab.restful;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import gov.nih.nci.cananolab.dto.common.DataReviewStatusBean;
import gov.nih.nci.cananolab.restful.core.ManageResultBO;
import gov.nih.nci.cananolab.restful.curation.BatchDataAvailabilityBO;
import gov.nih.nci.cananolab.restful.curation.ReviewDataBO;
import gov.nih.nci.cananolab.restful.util.CommonUtil;
import gov.nih.nci.cananolab.security.utils.SpringSecurityUtil;
import gov.nih.nci.cananolab.service.common.LongRunningProcess;
import gov.nih.nci.cananolab.ui.form.GenerateBatchDataAvailabilityForm;
import gov.nih.nci.cananolab.util.Constants;

@Path("/curation")
public class CurationServices {
	private static final Logger logger = Logger.getLogger(CurationServices.class);

	@GET
	@Path("/reviewData")
	@Produces ("application/json")
    public Response setupNew(@Context HttpServletRequest httpRequest) {
				
		try { 
			ReviewDataBO reviewDataBO = (ReviewDataBO) SpringApplicationContext.getBean(httpRequest, "reviewDataBO");
			if (!SpringSecurityUtil.isUserLoggedIn())
				return Response.status(Response.Status.UNAUTHORIZED).entity(Constants.MSG_SESSION_INVALID).build();
			List<DataReviewStatusBean> list = reviewDataBO.setupNew(httpRequest);
			return Response.ok(list).header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();

			// return Response.ok(dropdownMap).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while setting up drop down lists" + e.getMessage())).build();

		}
	}
	
	@POST
	@Path("/generateBatchDataAvailability")
	@Produces ("application/json")
	public Response generateBatchDataAvailability(@Context HttpServletRequest httpRequest, GenerateBatchDataAvailabilityForm form ) {
	
		try {
			BatchDataAvailabilityBO batchDataAvailabilityBO = 
					(BatchDataAvailabilityBO) SpringApplicationContext.getBean(httpRequest, "batchDataAvailabilityBO");
			if (!SpringSecurityUtil.isUserLoggedIn())
				return Response.status(Response.Status.UNAUTHORIZED).entity(Constants.MSG_SESSION_INVALID).build();		
			List<String> results = batchDataAvailabilityBO.generate(form, httpRequest);
			
			return Response.ok(results).header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();

		} catch (Exception e) {
			logger.error(e.getMessage());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while searching for publication " + e.getMessage())).build();
		}
	}
	@GET
	@Path("/manageResult")
	@Produces ("application/json")
    public Response manageResult(@Context HttpServletRequest httpRequest) {
				
		try { 
			ManageResultBO manageResultBO = (ManageResultBO) SpringApplicationContext.getBean(httpRequest, "manageResultBO");
			
			if (!SpringSecurityUtil.isUserLoggedIn())
				return Response.status(Response.Status.UNAUTHORIZED).entity(Constants.MSG_SESSION_INVALID).build();	
			
			List<LongRunningProcess> list = manageResultBO.execute(httpRequest);
			return Response.ok(list).header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();

			// return Response.ok(dropdownMap).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while setting up drop down lists" + e.getMessage())).build();

		}
	}

}

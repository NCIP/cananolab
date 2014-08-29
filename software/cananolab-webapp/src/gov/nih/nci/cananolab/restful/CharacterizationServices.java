package gov.nih.nci.cananolab.restful;

import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationSummaryViewBean;
import gov.nih.nci.cananolab.restful.sample.CharacterizationBO;
import gov.nih.nci.cananolab.restful.sample.SearchSampleBO;
import gov.nih.nci.cananolab.restful.util.CommonUtil;
import gov.nih.nci.cananolab.restful.view.SimpleCharacterizationSummaryViewBean;
import gov.nih.nci.cananolab.restful.view.SimpleCharacterizationsByTypeBean;
import gov.nih.nci.cananolab.restful.view.edit.SimpleCharacterizationEditBean;
import gov.nih.nci.cananolab.restful.view.edit.SimpleCharacterizationSummaryEditBean;

import java.util.ArrayList;
import java.util.List;
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

@Path("/characterization")
public class CharacterizationServices {

	private Logger logger = Logger.getLogger(CharacterizationServices.class);
	
	@Inject
	ApplicationContext applicationContext;
	
	@GET
	@Path("/setupEdit")
	@Produces ("application/json")
    public Response setupEdit(@Context HttpServletRequest httpRequest, 
    		@DefaultValue("") @QueryParam("sampleId") String sampleId) {
		logger.debug("In setupEdit");		
		
		try {
		CharacterizationBO characterizationBO = 
				(CharacterizationBO) applicationContext.getBean("characterizationBO");

		CharacterizationSummaryViewBean charView = characterizationBO.summaryEdit(sampleId, httpRequest);
		SimpleCharacterizationSummaryEditBean editBean = new SimpleCharacterizationSummaryEditBean();
		
		List<SimpleCharacterizationsByTypeBean> finalBean = editBean.transferData(httpRequest, charView, sampleId);
		
		logger.debug("Set up " + finalBean.size() + " characterization types for sample: " + sampleId);
		
		return Response.ok(finalBean).header("Access-Control-Allow-Credentials", "true")
						.header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
						.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(CommonUtil.wrapErrorMessageInList(e.getMessage())).build();
		}
	}

	@GET
	@Path("/setupAdd")
	@Produces ("application/json")
    public Response setupAdd(@Context HttpServletRequest httpRequest, 
    		@DefaultValue("") @QueryParam("sampleId") String sampleId, 
    		@DefaultValue("") @QueryParam("charType") String charType) {
		logger.debug("In setupAdd");		
		
		try {
		CharacterizationBO characterizationBO = 
				(CharacterizationBO) applicationContext.getBean("characterizationBO");

		SimpleCharacterizationEditBean charView = characterizationBO.setupNew(httpRequest, sampleId, charType);
//		SimpleCharacterizationSummaryEditBean editBean = new SimpleCharacterizationSummaryEditBean();
//		
//		List<SimpleCharacterizationsByTypeBean> finalBean = editBean.transferData(httpRequest, charView, sampleId);
//		
//		logger.debug("Set up " + finalBean.size() + " characterization types for sample: " + sampleId);
		
		return Response.ok(charView).header("Access-Control-Allow-Credentials", "true")
						.header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
						.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(CommonUtil.wrapErrorMessageInList(e.getMessage())).build();
		}
	}
}
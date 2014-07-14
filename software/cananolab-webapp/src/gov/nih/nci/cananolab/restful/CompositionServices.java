package gov.nih.nci.cananolab.restful;

import gov.nih.nci.cananolab.dto.common.PublicationSummaryViewBean;
import gov.nih.nci.cananolab.dto.particle.composition.CompositionBean;
import gov.nih.nci.cananolab.restful.publication.PublicationBO;
import gov.nih.nci.cananolab.restful.sample.CompositionBO;
import gov.nih.nci.cananolab.restful.view.SimpleCompositionBean;
import gov.nih.nci.cananolab.restful.view.SimplePublicationSummaryViewBean;
import gov.nih.nci.cananolab.ui.form.CompositionForm;

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

@Path("/composition")
public class CompositionServices {

private Logger logger = Logger.getLogger(SampleServices.class);
	
	@Inject
	ApplicationContext applicationContext;
	@GET
	@Path("/summaryView")
	@Produces ("application/json")
	 public Response view(@Context HttpServletRequest httpRequest, 
	    		@DefaultValue("") @QueryParam("sampleId") String sampleId){
		
		try { 
			CompositionForm form = new CompositionForm();
			form.setSampleId(sampleId);

		 CompositionBO compositionBO = 
					(CompositionBO) applicationContext.getBean("compositionBO");

		 CompositionBean compBean = compositionBO.summaryView(form, httpRequest);
			SimpleCompositionBean view = new SimpleCompositionBean();
			view.transferCompositionBeanForSummaryView(compBean);
			
			return Response.ok(view).build();
			
		} catch (Exception e) {
			return Response.ok("Error while viewing the publication results").build();
		}
	}

}

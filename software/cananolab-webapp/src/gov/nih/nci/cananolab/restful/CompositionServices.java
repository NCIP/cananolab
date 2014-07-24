package gov.nih.nci.cananolab.restful;

import java.io.FileInputStream;

import gov.nih.nci.cananolab.dto.common.PublicationSummaryViewBean;
import gov.nih.nci.cananolab.dto.particle.composition.CompositionBean;
import gov.nih.nci.cananolab.restful.publication.PublicationBO;
import gov.nih.nci.cananolab.restful.sample.CharacterizationBO;
import gov.nih.nci.cananolab.restful.sample.CompositionBO;
import gov.nih.nci.cananolab.restful.view.SimpleCompositionBean;
import gov.nih.nci.cananolab.restful.view.SimplePublicationSummaryViewBean;
import gov.nih.nci.cananolab.ui.form.CompositionForm;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
			return Response.ok("Error while viewing the composition results").build();
		}
	}
	@GET
	@Path("/summaryPrint")
	@Produces ("application/json")
	 public Response summaryPrint(@Context HttpServletRequest httpRequest,  
	    		@DefaultValue("") @QueryParam("sampleId") String sampleId){
		
		try { 
			CompositionForm form = new CompositionForm();
			form.setSampleId(sampleId);

			CompositionBO compositionBO = 
					(CompositionBO) applicationContext.getBean("compositionBO");

			CompositionBean compBean = compositionBO.summaryPrint(form, httpRequest);
			SimpleCompositionBean view = new SimpleCompositionBean();
			view.transferCompositionBeanForSummaryView(compBean);
		
			
			return Response.ok(view).build();
			
		} catch (Exception e) {
			return Response.ok("Error while printing the file").build();
		}
	}
	
	@GET
	@Path("/summaryExport")
	@Produces ("application/vnd.ms-excel")
	 public Response summaryExport(@Context HttpServletRequest httpRequest, @Context HttpServletResponse httpResponse, 
	    		@DefaultValue("") @QueryParam("sampleId") String sampleId, @DefaultValue("") @QueryParam("type") String type){
		
		try { 
			CompositionForm form = new CompositionForm();
			form.setSampleId(sampleId);
			
			
			CompositionBO compositionBO = 
					(CompositionBO) applicationContext.getBean("compositionBO");

			 String result = compositionBO.summaryExport(form, type, httpRequest, httpResponse);
				
			return Response.ok("").build();
			
		} catch (Exception e) {
			return Response.ok("Error while exporting the file").build();
		}
		
	}
		@GET
		@Path("/downloadImage")
		@Produces("image/png")
		 public Response characterizationImage(@Context HttpServletRequest httpRequest, @Context HttpServletResponse httpResponse,
		    		@DefaultValue("") @QueryParam("fileId") String fileId){
			try {
				CompositionBO compositionBO = 
						(CompositionBO) applicationContext.getBean("compositionBO");
				java.io.File file = compositionBO.download(fileId, httpRequest);
				
				return Response.ok(new FileInputStream(file)).build();
				

			} catch (Exception e) {
				return Response.ok(e.getMessage()).build();
			}
		}
		
		@GET
		@Path("/download")
		@Produces({"image/png", "application/json"})
		 public Response download(@Context HttpServletRequest httpRequest, @Context HttpServletResponse httpResponse,
		    		@DefaultValue("") @QueryParam("fileId") String fileId){
			try {
				CompositionBO compositionBO = 
						(CompositionBO) applicationContext.getBean("compositionBO");
				
				String result = compositionBO.download(fileId, httpRequest, httpResponse);
				return Response.ok(result).build();
			} 
			
			catch (Exception e) {
				return Response.ok(e.getMessage()).build();
			}
		}
	
}
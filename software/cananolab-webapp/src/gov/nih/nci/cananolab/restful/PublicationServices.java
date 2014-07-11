package gov.nih.nci.cananolab.restful;

import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.dto.common.PublicationSummaryViewBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.restful.publication.PublicationBO;
import gov.nih.nci.cananolab.restful.sample.SampleBO;
import gov.nih.nci.cananolab.restful.view.SimplePublicationBean;
import gov.nih.nci.cananolab.restful.view.SimplePublicationSummaryViewBean;
import gov.nih.nci.cananolab.restful.view.SimpleSampleBean;

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
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

@Path("/publication")
public class PublicationServices {

private Logger logger = Logger.getLogger(SampleServices.class);
	
	@Inject
	ApplicationContext applicationContext;
	@GET
	@Path("/summaryView")
	@Produces ("application/json")
	 public Response view(@Context HttpServletRequest httpRequest, 
	    		@DefaultValue("") @QueryParam("sampleId") String sampleId){
		
		try { 

		 PublicationBO publicationBO = 
					(PublicationBO) applicationContext.getBean("publicationBO");

		 PublicationSummaryViewBean pubBean = publicationBO.summaryView(sampleId, httpRequest);
			SimplePublicationSummaryViewBean view = new SimplePublicationSummaryViewBean();
			view.transferPublicationBeanForSummaryView(pubBean);
			
			return Response.ok(view).build();
			
		} catch (Exception e) {
			return Response.ok("Error while viewing the publication results").build();
		}
	}

	@GET
	@Path("/download")
	@Produces ("application/pdf")
	 public Response download(@Context HttpServletRequest httpRequest, @Context HttpServletResponse httpResponse, 
	    		@DefaultValue("") @QueryParam("fileId") String fileId){
		
		try { 

			 PublicationBO publicationBO = 
						(PublicationBO) applicationContext.getBean("publicationBO");

			String result = publicationBO.download(fileId, httpRequest, httpResponse);
		
			
			return Response.ok(result).build();
			
		} catch (Exception e) {
			return Response.ok("Error while downloading the file").build();
		}
	}
	
	@GET
	@Path("/summaryPrint")
	@Produces ("application/json")
	 public Response summaryPrint(@Context HttpServletRequest httpRequest,  
	    		@DefaultValue("") @QueryParam("sampleId") String sampleId){
		
		try { 

			 PublicationBO publicationBO = 
						(PublicationBO) applicationContext.getBean("publicationBO");

			 PublicationSummaryViewBean summaryBean = publicationBO.summaryPrint(sampleId, httpRequest);
			 SimplePublicationSummaryViewBean view = new SimplePublicationSummaryViewBean();
				view.transferPublicationBeanForSummaryView(summaryBean);
		
			
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
			
				 PublicationBO publicationBO = 
						(PublicationBO) applicationContext.getBean("publicationBO");

			 String result = publicationBO.summaryExport(sampleId, type, httpRequest, httpResponse);
				
			return Response.ok("").build();
			
		} catch (Exception e) {
			return Response.ok("Error while exporting the file").build();
		}
	}
	

}

package gov.nih.nci.cananolab.restful;

import java.util.List;
import java.util.Map;

import gov.nih.nci.cananolab.domain.common.Author;
import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.dto.common.PublicationSummaryViewBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.restful.publication.PublicationBO;
import gov.nih.nci.cananolab.restful.publication.PublicationManager;
import gov.nih.nci.cananolab.restful.publication.SearchPublicationBO;
import gov.nih.nci.cananolab.restful.sample.SampleBO;
import gov.nih.nci.cananolab.restful.sample.SearchSampleBO;
import gov.nih.nci.cananolab.restful.view.SimplePublicationBean;
import gov.nih.nci.cananolab.restful.view.SimplePublicationSummaryViewBean;
import gov.nih.nci.cananolab.restful.view.SimpleSampleBean;
import gov.nih.nci.cananolab.restful.view.edit.SampleEditPublicationBean;
import gov.nih.nci.cananolab.ui.form.PublicationForm;
import gov.nih.nci.cananolab.ui.form.SearchPublicationForm;
import gov.nih.nci.cananolab.ui.form.SearchSampleForm;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
			
			// return Response.ok(view).build();
			return Response.ok(view).header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();
			
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

			 PublicationSummaryViewBean summaryBean = publicationBO.summaryView(sampleId, httpRequest);
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
	@GET
	@Path("/summaryEdit")
	@Produces ("application/json")
	 public Response summaryEdit(@Context HttpServletRequest httpRequest, 
	    		@DefaultValue("") @QueryParam("sampleId") String sampleId, @DefaultValue("") @QueryParam("dispatch") String dispatch){
		
		try { 

		 PublicationBO publicationBO = 
					(PublicationBO) applicationContext.getBean("publicationBO");

		 PublicationForm form = new PublicationForm();
		 form.setSampleId(sampleId);
		 form.setDispatch(dispatch);
		 PublicationSummaryViewBean pubBean = publicationBO.summaryEdit(form, httpRequest);
			SampleEditPublicationBean view = new SampleEditPublicationBean();
			view.transferPublicationBeanForSummaryEdit(httpRequest, pubBean);
			
			// return Response.ok(view).build();
			return Response.ok(view).header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();
			
		} catch (Exception e) {
			return Response.ok("Error while viewing the publication results").build();
		}
	}
	
	@GET
	@Path("/setup")
	@Produces ("application/json")
    public Response setup(@Context HttpServletRequest httpRequest) {
				
		try { 
			SearchPublicationBO searchPublicationBO = 
					(SearchPublicationBO) applicationContext.getBean("searchPublicationBO");
			Map<String, List<String>> dropdownMap = searchPublicationBO.setup(httpRequest);
			return Response.ok(dropdownMap).header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();

			// return Response.ok(dropdownMap).build();
		} catch (Exception e) {
			return Response.ok("Error while setting up drop down lists").build();
		}
	}

	@POST
	@Path("/searchPublication")
	@Produces ("application/json")
	public Response searchPublication(@Context HttpServletRequest httpRequest, SearchPublicationForm searchForm ) {
	
		try {
			SearchPublicationBO searchPublicationBO = 
					(SearchPublicationBO) applicationContext.getBean("searchPublicationBO");
			
						
			List results = searchPublicationBO.search(searchForm, httpRequest);
			
			Object result = results.get(0);
			if (result instanceof String) {
				return Response.status(Response.Status.NOT_FOUND).entity(result).build();
			} else
				return Response.ok(results).build();

		} catch (Exception e) {
			logger.error(e.getMessage());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error while searching for publication " + e.getMessage()).build();
		}
	}
	
	@POST
	@Path("/addAuthor")
	@Produces ("application/json")
	public Response addAuthor(@Context HttpServletRequest httpRequest, Author form ) {
	
		try {
			PublicationManager pubManager = 
					 (PublicationManager) applicationContext.getBean("publicationManager");
			
						
			PublicationBean pBean = pubManager.addAuthor(form, httpRequest);
			
			return Response.ok(pBean).build();

		} catch (Exception e) {
			logger.error(e.getMessage());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error while adding author for publication " + e.getMessage()).build();
		}
	}
}

package gov.nih.nci.cananolab.restful;

import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationSummaryViewBean;
import gov.nih.nci.cananolab.exception.FileException;
import gov.nih.nci.cananolab.restful.sample.CharacterizationBO;
import gov.nih.nci.cananolab.restful.sample.SampleBO;
import gov.nih.nci.cananolab.restful.sample.SearchSampleBO;
import gov.nih.nci.cananolab.restful.view.SimpleCharacterizationsByTypeBean;
import gov.nih.nci.cananolab.restful.view.SimpleCharacterizationSummaryViewBean;
import gov.nih.nci.cananolab.restful.view.SimpleSampleBean;
import gov.nih.nci.cananolab.ui.form.SearchSampleForm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

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

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

@Path("/sample")
public class SampleServices {
	private Logger logger = Logger.getLogger(SampleServices.class);
	
	@Inject
	ApplicationContext applicationContext;

	@GET
	@Path("/setup")
	@Produces ("application/json")
    public Response setup(@Context HttpServletRequest httpRequest) {
		System.out.println("In initSetup");		
		
		try { 
			SearchSampleBO searchSampleBO = 
					(SearchSampleBO) applicationContext.getBean("searchSampleBO");
			Map<String, List<String>> dropdownTypeLists = searchSampleBO.setup(httpRequest);

			return Response.ok(dropdownTypeLists).build();
		} catch (Exception e) {
			return Response.ok("Error while setting up drop down lists").build();
		}
	}
	
	
	
	@GET
	@Path("/getCharacterizationByType")
	@Produces ("application/json")
    public Response getCharacterizationByType(@Context HttpServletRequest httpRequest, 
    		@DefaultValue("") @QueryParam("type") String type) {
		
		SearchSampleBO searchSampleBO = 
				(SearchSampleBO) applicationContext.getBean("searchSampleBO");
		
		try {
			List<String> characterizations = searchSampleBO.getCharacterizationByType(httpRequest, type);
			return Response.ok(characterizations).build();
		} catch (Exception e) {
			return Response.ok("Error while getting characterization by type").build();
		}
	}
	
	@POST
	@Path("/searchSample")
	@Produces ("application/json")
	public Response searchSample(@Context HttpServletRequest httpRequest, SearchSampleForm searchForm ) {
		
		try {
			SearchSampleBO searchSampleBO = 
					(SearchSampleBO) applicationContext.getBean("searchSampleBO");
			
			List results = searchSampleBO.search(searchForm, httpRequest);
			
			//String json = ViewFilterUtil.getSampleSearchResultJSon(results);
			return Response.ok(results).build();

		} catch (Exception e) {
			logger.error(e.getMessage());
			return Response.ok("Error while searching for samples").build();
		}
	}
	
	
	
	@GET
	@Path("/view")
	@Produces ("application/json")
	 public Response view(@Context HttpServletRequest httpRequest, 
	    		@DefaultValue("") @QueryParam("sampleId") String sampleId){
		
		try { 

			SampleBO sampleBO = 
					(SampleBO) applicationContext.getBean("sampleBO");

			SampleBean sampleBean = sampleBO.summaryView(sampleId,httpRequest);
			SimpleSampleBean view = new SimpleSampleBean();
			view.transferSampleBeanForSummaryView(sampleBean);
			
			//return Response.ok(view).build();
			return Response.ok(view).header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();
			
		} catch (Exception e) {
			return Response.ok("Error while viewing the search results").build();
		}
	}
	
	
	@GET
	@Path("/viewDataAvailability")
	@Produces ("application/json")
	 public Response viewDataAvailability(@Context HttpServletRequest httpRequest, 
	    		@DefaultValue("") @QueryParam("sampleId") String sampleId){
		
		try { 

			SampleBO sampleBO = 
					(SampleBO) applicationContext.getBean("sampleBO");

			SimpleSampleBean sampleBean = sampleBO.dataAvailabilityView(sampleId,httpRequest);
			
			//SimpleSampleBean view = new SimpleSampleBean();
			//view.transferSampleBeanForSummaryView(sampleBean);
			
			return Response.ok(sampleBean).build();
			
		} catch (Exception e) {
			return Response.ok(e.getMessage()).build();
		}
	}
	
	@GET
	@Path("/characterizationView")
	@Produces ("application/json")
	 public Response characterizationView(@Context HttpServletRequest httpRequest, 
	    		@DefaultValue("") @QueryParam("sampleId") String sampleId){
		try { 

			CharacterizationBO characterizationBO = 
					(CharacterizationBO) applicationContext.getBean("characterizationBO");

			CharacterizationSummaryViewBean charView = characterizationBO.summaryView(sampleId,httpRequest);
			SimpleCharacterizationSummaryViewBean viewBean = new SimpleCharacterizationSummaryViewBean();
			
			List<SimpleCharacterizationsByTypeBean> finalBean = viewBean.transferData(charView);
			
			return Response.ok(finalBean).header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();
			
		} catch (Exception e) {
			return Response.ok(e.getMessage()).build();
		}
		
	}
	
	
	@GET
	@Path("/downloadImage")
	@Produces({"image/png", "application/json"})
	 public Response downloadImage(@Context HttpServletRequest httpRequest, @Context HttpServletResponse httpResponse,
	    		@DefaultValue("") @QueryParam("fileId") String fileId){
		try {
			CharacterizationBO characterizationBO = 
					(CharacterizationBO) applicationContext.getBean("characterizationBO");
			
			
			java.io.File file = characterizationBO.download(fileId, httpRequest);
			
			return Response.ok(new FileInputStream(file)).build();
			
		} catch (Exception ioe) {
			return Response.ok(ioe.getMessage()).build();
		}
	}
	
	@GET
	@Path("/download")
	@Produces({"image/png", "application/json"})
	 public Response download(@Context HttpServletRequest httpRequest, @Context HttpServletResponse httpResponse,
	    		@DefaultValue("") @QueryParam("fileId") String fileId){
		try {
			CharacterizationBO characterizationBO = 
					(CharacterizationBO) applicationContext.getBean("characterizationBO");
			
			String result = characterizationBO.download(fileId, httpRequest, httpResponse);
			return Response.ok(result).build();
		} 
		
		catch (Exception ioe) {
			return Response.ok(ioe.getMessage()).build();
		}
	}
	
	@GET
	@Path("/exportCharacterizationView")
	@Produces("application/json")
	 public Response exportCharacterizationView(@Context HttpServletRequest httpRequest, @Context HttpServletResponse httpResponse,
	    		@DefaultValue("") @QueryParam("sampleId") String sampleId, @QueryParam("type") String type){
	
		try {
			CharacterizationBO characterizationBO = 
					(CharacterizationBO) applicationContext.getBean("characterizationBO");
			
			String result = characterizationBO.summaryExport(sampleId, type, httpRequest, httpResponse);
			return Response.ok(result).build();
		} 
		
		catch (Exception ioe) {
			return Response.ok(ioe.getMessage()).build();
		}
	}
	
	@GET
	@Path("/printCharacterizationView")
	@Produces("application/json")
	 public Response printCharacterizationView(@Context HttpServletRequest httpRequest, @Context HttpServletResponse httpResponse,
	    		@DefaultValue("") @QueryParam("sampleId") String sampleId){
	
		try {
			CharacterizationBO characterizationBO = 
					(CharacterizationBO) applicationContext.getBean("characterizationBO");
			
			CharacterizationSummaryViewBean viewBean = characterizationBO.summaryPrint(sampleId, httpRequest, httpResponse);
			SimpleCharacterizationSummaryViewBean simpleViewBean = new SimpleCharacterizationSummaryViewBean();
			List<SimpleCharacterizationsByTypeBean> simpleBean = simpleViewBean.transferData(viewBean);
			
			return Response.ok(simpleBean).build();
		} 
		
		catch (Exception ioe) {
			return Response.ok(ioe.getMessage()).build();
		}
	}
	
}

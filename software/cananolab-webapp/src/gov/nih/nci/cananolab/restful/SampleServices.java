package gov.nih.nci.cananolab.restful;

import gov.nih.nci.cananolab.restful.sample.SearchSampleBO;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.springframework.context.ApplicationContext;

@Path("/sample")
public class SampleServices {
	
	@Inject
	ApplicationContext applicationContext;

	@GET
	@Path("/setup")
	@Produces ("application/json")
    public Response setup(@Context HttpServletRequest httpRequest) {
		System.out.println("In initSetup");		
		
		try { 
			SearchSampleBO searchActionProxy = 
					(SearchSampleBO) applicationContext.getBean("searchSampleBO");
			Map<String, List<String>> dropdownTypeLists = searchActionProxy.setup(httpRequest);

			return Response.ok(dropdownTypeLists).build();
		} catch (Exception e) {
			return Response.ok("Error while setting up drop down lists").build();
		}
	}
	
//	@POST
//	@Path("/searchSample")
//	@Produces ("application/json")
//	public Response searchSample(@Context HttpServletRequest httpRequest, @FormParam("form") TestForm form ) {
//		
//		
////		String f = form.getUser();
////		f += "aaba";
//			
//		return null;
//	}
}

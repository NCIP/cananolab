package gov.nih.nci.cananolab.restful;

import gov.nih.nci.cananolab.restful.context.SpringApplicationContext;
import gov.nih.nci.cananolab.restful.customsearch.CustomSearchBO;
import gov.nih.nci.cananolab.restful.customsearch.CustomSearchEngine;
import gov.nih.nci.cananolab.restful.customsearch.bean.CustomSearchBean;
import gov.nih.nci.cananolab.restful.customsearch.bean.ProtocolSearchableFieldsBean;
import gov.nih.nci.cananolab.restful.customsearch.bean.PublicationSearchableFieldsBean;
import gov.nih.nci.cananolab.restful.customsearch.bean.SampleSearchableFieldsBean;
import gov.nih.nci.cananolab.restful.protocol.SearchProtocolBO;
import gov.nih.nci.cananolab.restful.util.CommonUtil;

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
import org.springframework.context.support.ClassPathXmlApplicationContext;
@Path("/customsearch")
public class CustomSearchServices {

	private Logger logger = Logger.getLogger(CustomSearchServices.class);
		
//		@Inject
//		SpringApplicationContext applicationContext;
	ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext-strutsless.xml");
		@GET
		@Path("/search")
		@Produces ("application/json")
	    public Response setup(@Context HttpServletRequest httpRequest, @DefaultValue("") @QueryParam("keyword") String keyword) {
					
			try { 
				CustomSearchBO customSearchBO = 
						(CustomSearchBO) applicationContext.getBean("customSearchBO");
				
				List<CustomSearchBean> results = customSearchBO.search(httpRequest, keyword);
				return Response.ok(results).header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();

				// return Response.ok(dropdownMap).build();
			} catch (Exception e) {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while setting up drop down lists" + e.getMessage())).build();

			}
		}
}

package gov.nih.nci.cananolab.restful;

import java.util.ArrayList;
import java.util.List;

import gov.nih.nci.cananolab.dto.common.PublicDataCountBean;
import gov.nih.nci.cananolab.restful.core.CustomPlugInBO;
import gov.nih.nci.cananolab.restful.core.InitSetup;
import gov.nih.nci.cananolab.restful.core.TabGenerationBO;
import gov.nih.nci.cananolab.restful.util.InitSetupUtil;
import gov.nih.nci.cananolab.service.security.UserBean;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

@Path("/core")
public class CoreServices {
	
	@Inject
	ApplicationContext applicationContext;
	
	private Logger logger = Logger.getLogger(CoreServices.class);
	
	@GET
	@Path("/initSetup")
	@Produces ("application/json")
    public Response initSetup(@Context HttpServletRequest httpRequest) {
		System.out.println("In initSetup");		
		
		CustomPlugInBO customPlugInBO = (CustomPlugInBO)applicationContext.getBean("customPlugInBO");
		ServletContext context = httpRequest.getSession(true).getServletContext();
		try {
			customPlugInBO.init(context);
		} catch(Exception e) {
			return Response.ok(e.getMessage()).build();
		}
		
		InitSetup.getInstance().setPublicCountInContext(context);
		return Response.ok(context.getAttribute("publicCounts")).build();
	}
	
	@GET
	@Path("/getTabs")
	@Produces ("application/json")
    public Response getTabs(@Context HttpServletRequest httpRequest, 
    		@DefaultValue("") @QueryParam("homePage") String homePage) {
		//Mimick logic in cananoMainmenu.jsp
		
		//Help: https://wiki.nci.nih.gov/display/caNanoLab/caNanoLab+User%27s+Guide
		//Glossary: https://wiki.nci.nih.gov/display/caNanoLab//caNanoLab+Glossary
		//Home: https://cananolab.nci.nih.gov/caNanoLab/home.jsp
		
		//Protocols: https://cananolab.nci.nih.gov/caNanoLab/manageProtocol.do
		//Samples: https://cananolab.nci.nih.gov/caNanoLab/manageSample.do
		//Publications: https://cananolab.nci.nih.gov/caNanoLab/managePublication.do
		//login: https://cananolab.nci.nih.gov/caNanoLab/loginPage.do
		//logout: https://cananolab.nci.nih.gov/caNanoLab/logout.do
		
		System.out.println("In getTabs");
		
		TabGenerationBO tabGen = (TabGenerationBO)applicationContext.getBean("tabGenerationBO");
		List<String[]> tabs = tabGen.getTabs(httpRequest, homePage);
		
		return Response.ok(tabs).build();
	}

}

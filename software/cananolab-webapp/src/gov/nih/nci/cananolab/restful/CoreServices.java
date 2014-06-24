package gov.nih.nci.cananolab.restful;

import java.util.ArrayList;
import java.util.List;

import gov.nih.nci.cananolab.dto.common.PublicDataCountBean;
import gov.nih.nci.cananolab.restful.helper.InitSetupUtil;
import gov.nih.nci.cananolab.service.security.UserBean;

import javax.inject.Inject;
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
		InitSetupUtil setupUtil = (InitSetupUtil)applicationContext.getBean("initSetupUtil");
		PublicDataCountBean dataCountBean = setupUtil.getPublicCount();
		return Response.ok(dataCountBean).build();
	}
	
	@GET
	@Path("/getTabs")
	@Produces ("application/json")
    public Response getTabs(@Context HttpServletRequest httpRequest, 
    		@DefaultValue("") @QueryParam("homePage") String homePage) {
		//Mimick logic in cananoMainmenu.jsp
		
		System.out.println("In getTabs");
		List<String> tabs = new ArrayList<String>();
		HttpSession session = httpRequest.getSession();
		UserBean userBean = (session == null)? null : (UserBean)session.getAttribute("user");	
		String home = homePage.trim().toLowerCase();
		
		if (userBean == null) { //not logged in
			if (home.length() > 0 && home.startsWith("true")) {
				tabs.add("HOME");
				tabs.add("GLOSSARY");
			} else {
				tabs.add("HOME");
				tabs.add("PROTOCOLS");
				tabs.add("SAMPLES");
				tabs.add("PUBLICATIONS");
				tabs.add("HELP");
				tabs.add("GLOSSARY");
				tabs.add("LOGIN");
			}
		} else {
			tabs.add("HOME");
			tabs.add("PROTOCOLS");
			tabs.add("SAMPLES");
			tabs.add("PUBLICATIONS");
			tabs.add("COMMUNITY");
			
			if (userBean.isAdmin())
				tabs.add("ADMINISTRATION");
			
			if (userBean.isCurator())
				tabs.add("CURATION");
			
			//TODO: 
			//if (userBean.isCurator() && hasResultWaiting)
			//	tabs.add("RESULT");
			
			tabs.add("HELP");
			tabs.add("GLOSSARY");
			tabs.add("LOGOUT");
		}
		
		return Response.ok(tabs).build();
	}
	
	
}

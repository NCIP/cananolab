package gov.nih.nci.cananolab.restful;

import gov.nih.nci.cananolab.dto.common.PublicDataCountBean;
import gov.nih.nci.cananolab.restful.helper.InitSetupUtil;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@Path("/core")
public class CoreServices {
	
	ApplicationContext applicationContext = null;
	
	private Logger logger = Logger.getLogger(CoreServices.class);
	
	@GET
	@Path("/initSetup")
	@Produces ("application/json")
    public Response initSetup(@Context HttpServletRequest httpRequest) {
		
		applicationContext = new ClassPathXmlApplicationContext("applicationContext-strutsless.xml");
		InitSetupUtil setupUtil = (InitSetupUtil)applicationContext.getBean("initSetupUtil");
		PublicDataCountBean dataCountBean = setupUtil.getPublicCount();
		return Response.ok(dataCountBean).build();
	}
}

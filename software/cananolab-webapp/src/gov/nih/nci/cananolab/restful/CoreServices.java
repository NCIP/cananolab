package gov.nih.nci.cananolab.restful;

import gov.nih.nci.cananolab.dto.common.PublicDataCountBean;
import gov.nih.nci.cananolab.restful.helper.InitSetupUtil;

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
	
	@GET
	@Path("/getTabs")
	@Produces ("application/json")
    public Response getTabs(@Context HttpServletRequest httpRequest, 
    		@DefaultValue("") @QueryParam("sessionId") String sessionId) {
	
		//Mimick logic in cananoMainmenu.jsp
		/*  
		<c:when
							test="${item.value eq 'LOGOUT' && sessionScope.user == null ||
							        item.value eq 'ADMINISTRATION' && (user==null || !user.admin) ||
							        item.value eq 'CURATION' && (user==null || !user.curator)||
							        item.value eq 'COMMUNITY' && user==null ||
							        item.value eq 'LOGIN' && sessionScope.user !=null||
							        item.value eq 'LOGIN' && pageContext.request.requestURI eq '/caNanoLab/login.jsp' ||
							        item.value eq 'RESULTS' && !(user.curator && hasResultsWaiting)}">
							<td></td>
						</c:when> 
						
		full list: HELP, GLOSSARY - always there
		
					HOME - always there except in welcome page
		
			LOGIN, LOGOUT, ADMINISTRATION, CURATION, COMMUNITY, RESULTS - depending on login
			
			PROTOCOLS, SAMPLES, PUBLICATIONS,  - in search pages (like HOME?)
		 * */
	
		
		
		return null;
	}
}

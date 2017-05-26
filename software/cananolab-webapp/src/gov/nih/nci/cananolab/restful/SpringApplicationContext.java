package gov.nih.nci.cananolab.restful;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * This class is to return BO beans in application context.
 * 
 **/
public class SpringApplicationContext
{
	public static Object getBean(HttpServletRequest request, String beanName) {
		ApplicationContext context =  WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
		return context.getBean(beanName);
	}

}
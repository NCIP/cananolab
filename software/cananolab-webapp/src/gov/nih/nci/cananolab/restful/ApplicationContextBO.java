package gov.nih.nci.cananolab.restful;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
/**
 * This class is to return BO beans in application context.
 * 
 * @author asafievan
 *
 */
public class ApplicationContextBO {
	private static final ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext-strutsless.xml");
	
	public static ApplicationContext getApplicationContextBO() {
		return applicationContext;
	}

}

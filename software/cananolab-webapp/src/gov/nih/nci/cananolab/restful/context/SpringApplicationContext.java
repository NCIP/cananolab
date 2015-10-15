package gov.nih.nci.cananolab.restful.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringApplicationContext implements ApplicationContextAware {
	 
	private ApplicationContext appContext;
 
	// Private constructor prevents instantiation from other classes
    	private SpringApplicationContext() {}
 
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		appContext = applicationContext; 

	}
 
	public Object getBean(String beanName) {
		return appContext.getBean(beanName);
	}
	
	public ApplicationContext getContext(){
		return appContext;
	}
}

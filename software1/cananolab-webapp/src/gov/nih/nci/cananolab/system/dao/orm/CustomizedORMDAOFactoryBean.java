package gov.nih.nci.cananolab.system.dao.orm;

import gov.nih.nci.cananolab.system.dao.SessionFactoryHolder;

import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;

/**
 * Modified SDK's ORMDAOFactoryBean to work with customized ORMDAOImpl
 * 
 * modified by Sue Pan
 */
public class CustomizedORMDAOFactoryBean extends LocalSessionFactoryBean {

	private static Logger log = Logger
			.getLogger(CustomizedORMDAOFactoryBean.class.getName());

	private CustomizedORMDAOImpl ormDAO;

	boolean caseSensitive;

	int resultCountPerQuery;
	
	SessionFactoryHolder holder;

	public CustomizedORMDAOFactoryBean(String configLocation,
			Properties systemProperties, Map systemPropertiesMap, SessionFactoryHolder holder)
			throws Exception {

		this.holder= holder;
		
		Resource resource = new ClassPathResource(configLocation);
		this.setConfigLocation(resource);

		try {
			this.caseSensitive = Boolean.parseBoolean(systemProperties
					.getProperty("caseSensitive"));
			this.resultCountPerQuery = Integer.parseInt(systemProperties
					.getProperty("resultCountPerQuery"));
		} catch (Exception e) {
			log.error(e);
			throw e;
		}
	}

	public Object getObject() {
		return ormDAO;
	}

	public Class getObjectType() {
		return ormDAO.getClass();
	}

	public boolean isSingleton() {
		return true;
	}

	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		ormDAO = new CustomizedORMDAOImpl((SessionFactory) this
				.getSessionFactory(), (Configuration) this.getConfiguration(),
				caseSensitive, resultCountPerQuery);
		
		holder.setInstance(getSessionFactory());
	}
}

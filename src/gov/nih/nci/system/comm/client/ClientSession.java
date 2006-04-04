package gov.nih.nci.system.comm.client;

import gov.nih.nci.common.util.Constant;
import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.comm.common.ApplicationServiceProxy;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ClientSession
{

	private static ClientSession instance;

	private String sessionKey;

	private String sessionInitializedFrom;

	private ApplicationServiceProxy applicationServiceProxy;

	public static synchronized ClientSession getInstance()
	{
		if (instance == null)
		{
			instance = new ClientSession();
		}

		return instance;
	}
	
	public static synchronized ClientSession getInstance(ApplicationServiceProxy applicationServiceProxy)
	{
		if (instance == null)
		{
			instance = new ClientSession(applicationServiceProxy);
		}

		return instance;
	}

	private ClientSession()
	{
		if (null == applicationServiceProxy)
		{
			applicationServiceProxy = getApplicationServiceProxyFromClassPath();
		}
	}
	
	private ClientSession(ApplicationServiceProxy applicationServiceProxy)
	{
		if (null == this.applicationServiceProxy)
		{
			this.applicationServiceProxy = applicationServiceProxy;
		}
	}
	
	public boolean startSession(String userId, String password) throws ApplicationException
	{
		boolean authenticated = false;
		String sessionKey_from_server = null;
		try
		{
			sessionKey_from_server = applicationServiceProxy.authenticate(userId, password);
			if (sessionKey_from_server != null)
			{
				authenticated = true;
				sessionKey = sessionKey_from_server;

			}
		}
		catch (Exception ex)
		{
			authenticated = false;
			throw new ApplicationException("Error in authenticating user credentials");
		}
		return authenticated;
	}

	public void terminateSession()
	{

		applicationServiceProxy.logOut(sessionKey);
	}

	public String getSessionKey()
	{
		return sessionKey;
	}

	private ApplicationServiceProxy getApplicationServiceProxyFromClassPath()
	{
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(Constant.REMOTE_SERVICE_FILE_NAME);
		ApplicationServiceProxy applicationServiceProxy = (ApplicationServiceProxy) applicationContext.getBean(Constant.REMOTE_APPLICATION_SERVICE);
		return applicationServiceProxy;
	}
}
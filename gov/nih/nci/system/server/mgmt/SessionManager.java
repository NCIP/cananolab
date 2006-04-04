/*
 * Created on May 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gov.nih.nci.system.server.mgmt;

import gov.nih.nci.common.util.Constant;

import java.util.Hashtable;
import java.util.Set;

import org.jboss.ejb.plugins.keygenerator.KeyGenerator;
import org.jboss.ejb.plugins.keygenerator.uuid.UUIDKeyGenerator;

/**
 * This class acts as a Session Manager. It instantiates the
 * user sessions and caches them. It manages the life cycle of these
 * user sessions by keeping only the active sessions in its cache and 
 * expiring the sessions which have been inactive for the pre-determined 
 * period. It also provides methods to terminate the user session. This
 * class is a <code>Singleton</code> and only single instance is maintained.
 * 
 * @author Ekagra Software Technologies Ltd.
 */
public class SessionManager
{
	private static SessionManager sessionManager = null;
	private Hashtable sessions;
	private KeyGenerator keyGenerator;
	private long timeOut = Constant.DEFAULT_SESSION_TIMEOUT;

	/**
	 * This method returns the cachced instance of the {@link SessionManager} 
	 * if it is not instantiated else it instantiates the same and returns it.
	 *  
	 * @return The singleton instance of SessionManager
	 */
	public static synchronized SessionManager getInstance()
	{
		if (sessionManager == null)
		{
			sessionManager = new SessionManager();
		}
		return sessionManager;
	}

	/**
	 * Private Constructor. It instantiates the has internal cache to store
	 * the user sessions. It also initalizes the key generator. It then reads
	 * the default timeout from the session property <code>CSM.Security.Session.Timeout</code>
	 */
	private SessionManager()
	{
		sessions = new Hashtable();
		SessionMonitor SessionMonitor = new SessionMonitor();
		try
		{
			keyGenerator = new UUIDKeyGenerator();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		try
		{
			String str = System.getProperty(Constant.CSM_SECURITY_SESSION_TIMEOUT);
			Long l = new Long(str);
			timeOut = l.longValue();
		}
		catch (Exception ex)
		{
			timeOut = Constant.DEFAULT_SESSION_TIMEOUT;
		}
	}

	/**
	 * This method instantiates a {@link UserSession} object for the passes userid.
	 * It also generates a unique key and caches the user object using the key as name
	 * and the {@link UserSession} as the value 
	 * @param userId The login id of the user trying to log in 
	 * @return The session key associated with the user session representing the user
	 */
	public String initSession(String userId)
	{
		cleanUp();
		UserSession userSession = new UserSession(userId);
		Object object = keyGenerator.generateKey();
		String sessionKey = object.toString();
		sessions.put(sessionKey, userSession);
		return sessionKey;
	}
	
	/**
	 * This methods kills the user session associated with the passed session key
	 * and removes the user session object from its cache
	 * @param sessionKey The key associated with the user session
	 */
	public void killSession(String sessionKey)
	{
		if (this.isBlank(sessionKey))
		{
			return;
		}
		if (sessions.containsKey(sessionKey))
		{
			sessions.remove(sessionKey);
		}
	}
	
	/**
	 * This methods looks up in the internal cache of the SessionManager and returns
	 * the {@link UserSession} object associated with the passed session key. If the 
	 * user is in session then the last accessed property is set to current, thereby 
	 * extending the session.
	 * 
	 * @param sessionKey The session key associated with the user session
	 * @return The (@link UserSession) object for the session key passed
	 */
	public UserSession getSession(String sessionKey)
	{
		UserSession userSession = (UserSession) sessions.get(sessionKey);
		if (userSession != null)
		{
			userSession.setLastAccessedTime(System.currentTimeMillis());
		}
		return userSession;
	}

	/**
	 * This method returns the List of the all the session keys from the internal cache
	 * 
	 * @return The set of all the session keys from the internal cache
	 */
	protected Set getSessionKeySet()
	{
		return sessions.keySet();
	}

	protected long getTimeOut()
	{
		return this.timeOut;
	}	
	
	/**
	 * This method returns the {@link UserSession} ojbect just for the purpose of 
	 * monitoring. It doesnt update the last accessed time like the 
	 * {@link SessionManager#getSession(String)} method
	 * 
	 * @param sessionKey The session key associated with the user session
	 * @return The (@link UserSession) object for the session key passed
	 */
	protected UserSession getSessionForMonitoring(String sessionKey)
	{
		UserSession userSession = (UserSession) sessions.get(sessionKey);
		return userSession;
	}

	/**
	 * This methods checks whether the user session associated with the session key
	 * passed is active or not. It returns a <code>true</code> if the user is 
	 * in session or returns a <code>false</code>. If the user is in session then 
	 * the last accessed property is set to current, thereby extending the session.
	 * 
	 * @param sessionKey the session key associated with the user session
	 * @return true if user session is active else returns a false
	 */
	public boolean isUserInSession(String sessionKey)
	{
		boolean inSession = false;
		if (sessions.containsKey(sessionKey))
		{
			UserSession userSession = (UserSession) sessions.get(sessionKey);
			if (System.currentTimeMillis() - userSession.getLastAccessedTime() > timeOut)
			{
				this.killSession(sessionKey);
			}
			else
			{
				inSession = true;
			}
		}
		return inSession;
	}

	private void cleanUp()
	{
		if (sessions.size() > 200)
		{
			new SessionMonitor();
		}
	}

	private boolean isBlank(String str)
	{
		boolean test = false;

		if (str == null)
		{
			test = true;
		}
		else
		{
			String str1 = str.trim();
			if (str1.equals(""))
			{
				test = true;
			}
		}
		return test;
	}

}

package gov.nih.nci.cananolab.security.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import gov.nih.nci.cananolab.security.CananoUserDetails;

public class SpringSecurityUtil
{
	private static Logger logger = Logger.getLogger(SpringSecurityUtil.class);
	
	public static boolean isUserLoggedIn()
	{
		logger.debug("Checking is there is a logged in user in the security context.");
		boolean isLoggedIn = false;
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof CananoUserDetails)
	        isLoggedIn = true;
	    
	    return isLoggedIn;
	}
	
	public static Authentication getAuthentication()
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		return auth;
	}
	
	public static CananoUserDetails getPrincipal()
	{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CananoUserDetails userDetails = null;
		if (authentication != null)
		{
				Object principal = authentication.getPrincipal();
				if (principal instanceof CananoUserDetails)
					userDetails = (CananoUserDetails) authentication.getPrincipal();
				else
				{
					userDetails = new CananoUserDetails();
					userDetails.setUsername(principal.toString());
					Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
					if (authorities != null)
					{
						List<String> roleList= new ArrayList<String>();
						for (GrantedAuthority auth : authorities)
						{
							roleList.add(auth.getAuthority());
						}
						if (roleList.size() > 0)
							userDetails.setRoles(roleList);
					}
				}
		}
	    
		
		return userDetails;
	}
	
	public static String getLoggedInUserName()
	{
		logger.debug("Checking is there is a logged in user in the security context.");
		String loggedInUserName = "";
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    if (authentication != null && authentication.getPrincipal() instanceof CananoUserDetails)
	    	loggedInUserName = ((CananoUserDetails) authentication.getPrincipal()).getUsername();
	    
	    return loggedInUserName;
	}

}

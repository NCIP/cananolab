package gov.nih.nci.cananolab.security.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import gov.nih.nci.cananolab.security.dao.UserDao;
import gov.nih.nci.cananolab.util.StringUtils;
import gov.nih.nci.cananolab.security.CananoUserDetails;

@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
@Component("cananoUserDetailsService")
public class CananoUserDetailsServiceImpl implements UserDetailsService
{
	protected Logger logger = Logger.getLogger(CananoUserDetailsServiceImpl.class);
	
	@Autowired
	private UserDao userDao;
	
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{
		logger.debug("Loading user details: " + username);

		CananoUserDetails userDetails = null;
		
		if (!StringUtils.isEmpty(username))
		{
			userDetails = userDao.getUserByName(username);
			
			if (userDetails != null)
			{
				userDetails.setGroups(userDao.getUserGroups(username));
				userDetails.setRoles(userDao.getUserRoles(username));
			}
			else
				throw new UsernameNotFoundException("User: " + username + " not found.");
		}
		return userDetails;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

}

package gov.nih.nci.cananolab.restful.useraccount;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.security.CananoUserDetails;
import gov.nih.nci.cananolab.security.service.UserService;
import gov.nih.nci.cananolab.security.utils.SpringSecurityUtil;

@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
@Component("userAccountBO")
public class UserAccountBO
{
	protected Logger logger = Logger.getLogger(UserAccountBO.class);
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private UserService userService;
	
	public CananoUserDetails readUserAccount(String username) throws NoAccessException
	{
		CananoUserDetails principal = SpringSecurityUtil.getPrincipal();
		if (principal == null || !principal.isAdmin()) {
			throw new NoAccessException();
		}
		return (CananoUserDetails) userDetailsService.loadUserByUsername(username);
	}
	
	public CananoUserDetails createUserAccount(CananoUserDetails userDetails) throws NoAccessException
	{
		CananoUserDetails principal = SpringSecurityUtil.getPrincipal();
		if (principal == null || !principal.isAdmin()) {
			throw new NoAccessException();
		}
		
		userService.createUserAccount(userDetails);
		CananoUserDetails newUserDetails = (CananoUserDetails) userDetailsService.loadUserByUsername(userDetails.getUsername());
		return newUserDetails;
	}
	
	public void resetUserAccountPassword(String oldPassword, String newPassword, String userName) throws Exception
	{
		CananoUserDetails principal = SpringSecurityUtil.getPrincipal();
		if (principal == null || !principal.isAdmin()) {
			throw new NoAccessException();
		}
		
		userService.resetPasswordForUser(oldPassword, newPassword, userName);
	}
	
	public CananoUserDetails updateUserAccount(CananoUserDetails userDetails) throws NoAccessException
	{
		CananoUserDetails principal = SpringSecurityUtil.getPrincipal();
		if (principal == null || !principal.isAdmin()) {
			throw new NoAccessException();
		}
		
		userService.updateUserAccount(userDetails);
		CananoUserDetails newUserDetails = (CananoUserDetails) userDetailsService.loadUserByUsername(userDetails.getUsername());
		return newUserDetails;
	}
	
	public List<CananoUserDetails> searchByUsername(String searchStr) throws NoAccessException
	{
		CananoUserDetails principal = SpringSecurityUtil.getPrincipal();
		if (principal == null || !principal.isAdmin()) {
			throw new NoAccessException();
		}

		List<CananoUserDetails> userList = userService.loadUsers(searchStr);
		return userList;
	}
	
}

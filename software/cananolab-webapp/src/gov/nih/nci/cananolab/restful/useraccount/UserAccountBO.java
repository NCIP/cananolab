package gov.nih.nci.cananolab.restful.useraccount;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import gov.nih.nci.cananolab.security.CananoUserDetails;
import gov.nih.nci.cananolab.security.service.UserService;

@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
@Component("userAccountBO")
public class UserAccountBO
{
	protected Logger logger = Logger.getLogger(UserAccountBO.class);
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private UserService userService;
	
	public CananoUserDetails readUserAccount(String username)
	{
		return (CananoUserDetails) userDetailsService.loadUserByUsername(username);
	}
	
	public CananoUserDetails createUserAccount(CananoUserDetails userDetails)
	{
		userService.createUserAccount(userDetails);
		CananoUserDetails newUserDetails = (CananoUserDetails) userDetailsService.loadUserByUsername(userDetails.getUsername());
		return newUserDetails;
	}
	
	public void resetUserAccountPassword(String oldPassword, String newPassword, String userName) throws Exception
	{
		userService.resetPasswordForUser(oldPassword, newPassword, userName);
	}
	
	public CananoUserDetails updateUserAccount(CananoUserDetails userDetails)
	{
		userService.updateUserAccount(userDetails);
		CananoUserDetails newUserDetails = (CananoUserDetails) userDetailsService.loadUserByUsername(userDetails.getUsername());
		return newUserDetails;
	}
	
	public List<CananoUserDetails> searchByUsername(String searchStr)
	{
		List<CananoUserDetails> userList = userService.loadUsers(searchStr);
		return userList;
	}
	
}

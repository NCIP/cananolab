package gov.nih.nci.cananolab.security.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import gov.nih.nci.cananolab.security.CananoUserDetails;
import gov.nih.nci.cananolab.security.dao.UserDao;
import gov.nih.nci.cananolab.security.enums.CaNanoRoleEnum;
import gov.nih.nci.cananolab.security.utils.SpringSecurityUtil;
import gov.nih.nci.cananolab.util.StringUtils;

//@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
@Component("userService")
public class UserServiceImpl implements UserService
{
	protected Logger logger = Logger.getLogger(UserServiceImpl.class);
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Override
	public List<CananoUserDetails> loadUsers(String matchStr)
	{
		List<CananoUserDetails> userList = userDao.getUsers(matchStr);
		for (CananoUserDetails userDetails: userList)
		{
			if (userDetails != null)
			{
				userDetails.setGroups(userDao.getUserGroups(userDetails.getUsername()));
				userDetails.setRoles(userDao.getUserRoles(userDetails.getUsername()));
			}
		}
		Collections.sort(userList, new Comparator<CananoUserDetails>() {
			public int compare(CananoUserDetails u1, CananoUserDetails u2) {
				return u1.compareTo(u2);
			}
		});
		return userList;
	}
	
	@Override
	public List<String> getGroupsAccessibleToUser(String matchStr)
	{
		CananoUserDetails userDetails = SpringSecurityUtil.getPrincipal();
		List<String> groups = new ArrayList<String>();
		if (userDetails != null)
		{
			if (!StringUtils.isEmpty(matchStr))
			{
				for (String group : userDetails.getGroups())
					if (group.contains(matchStr))
						groups.add(group);
			}
			else
				groups.addAll(userDetails.getGroups());
		}
		
		return groups;
	}
	
	@Override
	public void createUserAccount(CananoUserDetails userDetails)
	{
		String username = userDetails.getUsername();
		if (userDetails != null && !StringUtils.isEmpty(username))
		{
			String encryptedString = passwordEncoder.encode(username);
			userDetails.setPassword(encryptedString);
			int status = userDao.insertUser(userDetails);
			userDao.insertUserAuthority(username, CaNanoRoleEnum.ROLE_ANONYMOUS.toString());
			for (String role : userDetails.getRoles())
			{
				if (!role.equals(CaNanoRoleEnum.ROLE_ANONYMOUS))
					userDao.insertUserAuthority(username, role);
			}
		}
	}
	
	@Override
	public int resetPasswordForUser(String oldPassword, String newPassword, String userName) throws Exception
	{
		int status = 0;
		if (!StringUtils.isEmpty(userName) && !StringUtils.isEmpty(newPassword))
		{
			String encodedPassword = userDao.readPassword(userName);
			//verify oldPassword, else throw exception
			boolean match = passwordEncoder.matches(oldPassword, encodedPassword);
			
			//update to new Password
			if (match)
			{
				String encryptedPassword = passwordEncoder.encode(newPassword);
				status = userDao.resetPassword(userName, encryptedPassword);
			}
			else
				throw new Exception("Incorrect old password.");
		}
		return status;
	}

	@Override
	public void updateUserAccount(CananoUserDetails userDetails)
	{
		String username = userDetails.getUsername();
		if (userDetails != null && !StringUtils.isEmpty(username))
		{
			int status = userDao.updateUser(userDetails);
			
			status = userDao.deleteUserAssignedRoles(username);
			//update user roles
			for (String role : userDetails.getRoles())
			{
				if (!role.equals(CaNanoRoleEnum.ROLE_ANONYMOUS))
					userDao.insertUserAuthority(username, role);
			}
			
		}
	}

}

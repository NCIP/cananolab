package gov.nih.nci.cananolab.security.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import gov.nih.nci.cananolab.security.CananoUserDetails;
import gov.nih.nci.cananolab.security.dao.UserDao;
import gov.nih.nci.cananolab.security.utils.SpringSecurityUtil;
import gov.nih.nci.cananolab.util.StringUtils;

//@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
@Component("userService")
public class UserServiceImpl implements UserService
{
	protected Logger logger = Logger.getLogger(UserServiceImpl.class);
	
	@Autowired
	private UserDao userDao;
	
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

}

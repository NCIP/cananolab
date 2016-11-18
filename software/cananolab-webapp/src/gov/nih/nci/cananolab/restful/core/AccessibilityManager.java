package gov.nih.nci.cananolab.restful.core;

import gov.nih.nci.cananolab.security.CananoUserDetails;
import gov.nih.nci.cananolab.security.service.UserService;
import gov.nih.nci.cananolab.security.utils.SpringSecurityUtil;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
@Component("accessibilityManager")
public class AccessibilityManager
{
	private Logger logger = Logger.getLogger(AccessibilityManager.class);
	
	@Autowired
	private UserService userService;

	public Map<String, String> getMatchedUsers(String dataOwner, String searchStr, HttpServletRequest request) throws Exception
	{
		try {
			List<CananoUserDetails> matchedUsers = userService.loadUsers(searchStr);
			Map<String, String> userMap = new HashMap<String, String>();
			// add only if not logged in user and not owner of the entity and not curator
			for (CananoUserDetails currUser: matchedUsers)
			{
				if (!currUser.getUsername().equals(SpringSecurityUtil.getLoggedInUserName()) &&
					!currUser.getUsername().equalsIgnoreCase(dataOwner) && !currUser.isCurator())
				{
					userMap.put(currUser.getUsername(), currUser.getLastName() + " " + currUser.getFirstName());
				}
			}
			return userMap;
		} catch (Exception e) {
			logger.error("Problem getting matched user login names", e);
			return null;
		}
	}

	public CananoUserDetails[] getUsers(String searchStr) throws Exception
	{
		try {
			List<CananoUserDetails> matchedUsers = userService.loadUsers(searchStr);

			return matchedUsers.toArray(new CananoUserDetails[matchedUsers.size()]);
		} 
		catch (Exception e) {
			logger.error("Problem getting user login names", e);
			return null;
		}
	}

	public String[] getMatchedGroupNames(String searchStr, HttpServletRequest request) throws Exception
	{
		try 
		{
			CananoUserDetails userDetails = SpringSecurityUtil.getPrincipal();
			String upperSearchStr = (!StringUtils.isEmpty(searchStr)) ? searchStr.toUpperCase() : searchStr;
			List<String> matchGroupNames = new ArrayList<String>();
			if (userDetails != null)
				for (String groupName: userDetails.getGroups())
				{
					if (groupName.toUpperCase().indexOf(upperSearchStr) >= 0)
						matchGroupNames.add(groupName);
				}
			return matchGroupNames.toArray(new String[matchGroupNames.size()]);
		} catch (Exception e) {
			logger.error("Problem getting matched group names", e);
			return null;
		}
	}

}



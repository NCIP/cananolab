package gov.nih.nci.cananolab.service;

import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.exception.FileException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.exception.SecurityException;
import gov.nih.nci.cananolab.service.security.UserBean;

import java.util.List;

public interface BaseService {
	public List<AccessibilityBean> findGroupAccessibilities(String protectedData)
			throws SecurityException, NoAccessException;

	public List<AccessibilityBean> findUserAccessibilities(String protectedData)
			throws SecurityException, NoAccessException;

	public AccessibilityBean findAccessibilityByGroupName(String groupName,
			String protectedData) throws SecurityException, NoAccessException;

	public AccessibilityBean findAccessibilityByUserLoginName(
			String userLoginName, String protectedData)
			throws SecurityException, NoAccessException;

	public List<UserBean> findUserBeans(String loginNameSearchStr)
			throws SecurityException;

	public List<String> findGroupNames(String groupNameSearchStr)
			throws SecurityException;

	public void removeAllAccesses(String protectedData)
			throws SecurityException, NoAccessException;

	public FileBean findFileById(String id) throws FileException,
			NoAccessException;
}

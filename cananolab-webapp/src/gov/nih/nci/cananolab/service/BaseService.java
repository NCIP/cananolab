package gov.nih.nci.cananolab.service;

import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.exception.NoAccessException;

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
}

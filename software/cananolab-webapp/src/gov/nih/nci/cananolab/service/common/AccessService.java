package gov.nih.nci.cananolab.service.common;

import gov.nih.nci.cananolab.dto.common.AccessibilityBean;

import java.util.List;

public interface AccessService {
	public List<AccessibilityBean> findGroupAccessibilities(String protectedData)
			throws SecurityException;

	public List<AccessibilityBean> findUserAccessibilities(String protectedData)
			throws SecurityException;

	public void saveAccessibility(AccessibilityBean access, String protectedData)
			throws SecurityException;

	public AccessibilityBean findAccessibilityByGroupName(String groupName,
			String protectedData) throws SecurityException;

	public AccessibilityBean findAccessibilityByUserLoginName(String userLoginName,
			String protectedData) throws SecurityException;
}

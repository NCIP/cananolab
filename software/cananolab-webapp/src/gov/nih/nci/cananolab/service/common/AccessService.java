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
}

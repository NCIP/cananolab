/**
 * The caNanoLab Software License, Version 1.5
 *
 * Copyright 2006 SAIC. This software was developed in conjunction with the National
 * Cancer Institute, and so to the extent government employees are co-authors, any
 * rights in such works shall be subject to Title 17 of the United States Code,
 * section 105.
 * 
 */
package gov.nih.nci.cananolab.ui.admin;

/**
 *
 * @author houyh
 */
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.SecurityException;
import gov.nih.nci.cananolab.ui.core.AbstractDispatchAction;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.PropertyReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class AdministrationAction extends AbstractDispatchAction {
	
	private static String fileRoot = PropertyReader.getProperty(
			Constants.FILEUPLOAD_PROPERTY, Constants.FILE_REPOSITORY_DIR);
	
	private static Logger logger = 
			Logger.getLogger(AdministrationAction.class);

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return mapping.findForward("setup");
	}

	public boolean loginRequired() {
		return true;
	}

	public boolean canUserExecute(UserBean user)
			throws SecurityException {
		return InitSecuritySetup.getInstance().userHasCreatePrivilege(user,
				Constants.CSM_PG_PARTICLE);
	}

}

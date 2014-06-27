/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.ui.core;

import gov.nih.nci.cananolab.restful.core.InitSetup;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * This class calls the Struts ForwardAction to forward to a page without login
 *
 * @author pansu
 */

public class WelcomeAction extends AbstractForwardAction {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//set public data counts in the context
		InitSetup.getInstance().setPublicCountInContext(
				request.getSession().getServletContext());
		//save the token
		saveToken(request);
		return super.execute(mapping, form, request, response);
	}

	public boolean loginRequired() {
		return false;
	}
}

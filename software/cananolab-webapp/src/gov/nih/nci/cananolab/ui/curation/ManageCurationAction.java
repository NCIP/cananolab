/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.ui.curation;

/**
 * This class calls the Struts ForwardAction to forward to a page
 *
 * @author pansu
 */

import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.ui.core.AbstractForwardAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ManageCurationAction extends AbstractForwardAction {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");
		if (user != null && !user.isCurator()) {
			throw new NoAccessException(
					"You need to be a curator to access the page");
		}
		return super.execute(mapping, form, request, response);
	}

	public boolean loginRequired() {
		return true;
	}
}

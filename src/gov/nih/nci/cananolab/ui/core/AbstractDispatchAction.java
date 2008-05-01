package gov.nih.nci.cananolab.ui.core;

import gov.nih.nci.cananolab.dto.common.LabFileBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.exception.InvalidSessionException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;
import gov.nih.nci.cananolab.util.PropertyReader;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;

public abstract class AbstractDispatchAction extends DispatchAction {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");

		if (!loginRequired()) {
			return super.execute(mapping, form, request, response);
		}

		String dispatch = request.getParameter("dispatch");
		if (Arrays.asList(CaNanoLabConstants.PUBLIC_DISPATCHES).contains(
				dispatch)) {
			return super.execute(mapping, form, request, response);
		}
		if (user != null) {
			// check whether user have access to the class
			boolean accessStatus = canUserExecute(user);
			if (accessStatus) {
				return super.execute(mapping, form, request, response);
			} else {
				request.getSession().removeAttribute("user");
				throw new NoAccessException();
			}
		} else {
			throw new InvalidSessionException();
		}
	}

	/**
	 * Download action to handle file downloading and viewing
	 * 
	 * @param
	 * @return
	 */
	public ActionForward download(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String fileId = request.getParameter("fileId");
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		FileService service = new FileService();
		LabFileBean fileBean = service.findFile(fileId, user);
		if (fileBean.getDomainFile().getUriExternal()) {
			response.sendRedirect(fileBean.getDomainFile().getUri());
		}
		String fileRoot = PropertyReader.getProperty(
				CaNanoLabConstants.FILEUPLOAD_PROPERTY, "fileRepositoryDir");
		File dFile = new File(fileRoot + File.separator
				+ fileBean.getDomainFile().getUri());
		if (dFile.exists()) {
			response.setContentType("application/octet-stream");
			response.setHeader("Content-disposition", "attachment;filename="
					+ fileBean.getDomainFile().getName());
			response.setHeader("cache-control", "Private");

			java.io.InputStream in = new FileInputStream(dFile);
			java.io.OutputStream out = response.getOutputStream();

			byte[] bytes = new byte[32768];

			int numRead = 0;
			while ((numRead = in.read(bytes)) > 0) {
				out.write(bytes, 0, numRead);
			}
			out.close();
		} else {
			ActionMessages msgs = new ActionMessages();
			ActionMessage msg = new ActionMessage("error.noFile");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			this.saveErrors(request, msgs);
			return mapping.findForward("fileMessage");
		}
		return null;
	}

	public abstract boolean loginRequired();

	/**
	 * Check whether the current user can execute the action
	 * 
	 * @param user
	 * @return
	 * @throws CaNanoLabSecurityException
	 */
	public abstract boolean canUserExecute(UserBean user)
			throws CaNanoLabSecurityException;
}
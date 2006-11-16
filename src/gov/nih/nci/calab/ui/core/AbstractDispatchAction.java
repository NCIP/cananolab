package gov.nih.nci.calab.ui.core;

import java.io.File;
import java.io.FileInputStream;

import gov.nih.nci.calab.dto.characterization.CharacterizationBean;
import gov.nih.nci.calab.dto.characterization.CharacterizationFileBean;
import gov.nih.nci.calab.exception.InvalidSessionException;
import gov.nih.nci.calab.exception.NoAccessException;
import gov.nih.nci.calab.service.util.CalabConstants;
import gov.nih.nci.calab.service.util.PropertyReader;
import gov.nih.nci.calab.ui.submit.NanoparticleSizeAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.DynaValidatorForm;

public abstract class AbstractDispatchAction extends DispatchAction {
	private static Logger logger = Logger.getLogger(AbstractDispatchAction.class);

	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;

		// response.setHeader("Cache-Control", "no-cache");

		if (isCancelled(request))
			return mapping.findForward("cancel");
		// TODO fill in the common operations */
		if (!loginRequired() || loginRequired() && isUserLoggedIn(request)) {
			if (loginRequired()) {
				String dispatch = request.getParameter("dispatch");
				// check whether user have access to the class
				boolean accessStatus = canUserExecute(request.getSession());
				//if dispatch is view or accessStatus is true don't throw exception
				if (!dispatch.equals("view") && !accessStatus) {
					throw new NoAccessException(
							"You don't have access to class: "
									+ this.getClass().getName());
				}
			}
			forward = super.execute(mapping, form, request, response);
		} else {
			throw new InvalidSessionException();
		}
		return forward;
	}

	/**
	 * 
	 * @param request
	 * @return whether the user is successfully logged in.
	 */
	private boolean isUserLoggedIn(HttpServletRequest request) {
		boolean isLoggedIn = false;
		if (request.getSession().getAttribute("user") != null) {
			isLoggedIn = true;
		}
		return isLoggedIn;
	}

	public abstract boolean loginRequired();

	/**
	 * Check whether the current user in the session can perform the action
	 * 
	 * @param session
	 * @return
	 * @throws Exception
	 */
	public boolean canUserExecute(HttpSession session) throws Exception {
		return InitSessionSetup.getInstance().canUserExecuteClass(session,
				this.getClass());
	}
	
	/**
	 * Update multiple children on the same form
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward updateManufacturers(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;

		HttpSession session = request.getSession();
/*
		SizeBean sizeChar=(SizeBean) theForm.get("achar");
		if (sizeChar.getInstrument() != null) {
			String type = sizeChar.getInstrument().getType();
			logger.info("***************Action: getting manufacturers for " + type);
			InitSessionSetup.getInstance().setManufacturerPerType(session, sizeChar.getInstrument().getType());
		}
*/		
		CharacterizationBean sizeChar=(CharacterizationBean) theForm.get("achar");
		if (sizeChar.getInstrument() != null) {
			String type = sizeChar.getInstrument().getType();
			session.setAttribute("selectedInstrumentType", type);
			//logger.info("***************Action: getting manufacturers for " + type);
			InitSessionSetup.getInstance().setManufacturerPerType(session, sizeChar.getInstrument().getType());
		}

		return mapping.getInputForward();
	}

	/**
	 * Download action to handle download characterization file
	 * @param 
	 * @return
	 */
	public ActionForward download (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String fileId=request.getParameter("fileId");

		String rootPath = PropertyReader.getProperty(CalabConstants.FILEUPLOAD_PROPERTY, "fileRepositoryDir");
		if (rootPath.charAt(rootPath.length()-1) == File.separatorChar)
			rootPath = rootPath.substring(0, rootPath.length()-1);

		CharacterizationFileBean fileBean = (CharacterizationFileBean) request.getSession().getAttribute("characterizationFile" + fileId);
		String filename = rootPath + fileBean.getPath();
		
		File dFile = new File(filename);
		if (dFile.exists()) {
			response.setContentType("application/octet-stream");
			response.setHeader("Content-disposition", "attachment;filename=" + fileBean.getName());
			response.setHeader("Cache-Control", "no-cache");
		
			java.io.InputStream in = new FileInputStream (dFile);
			java.io.OutputStream out = response.getOutputStream();

			byte[] bytes = new byte[32768];
	
			int numRead = 0;
			while ((numRead = in.read(bytes)) > 0) {
				out.write(bytes, 0, numRead);
			}
			out.close();
		} else {
			throw new Exception ("ERROR: file not found.");
		}
			
		
		return null;
	}
	
}
package gov.nih.nci.calab.ui.core;

import gov.nih.nci.calab.dto.characterization.CharacterizationBean;
import gov.nih.nci.calab.dto.characterization.CharacterizationFileBean;
import gov.nih.nci.calab.service.submit.SubmitNanoparticleService;
import gov.nih.nci.calab.service.util.CalabConstants;
import gov.nih.nci.calab.service.util.PropertyReader;

import java.io.File;
import java.io.FileInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * This action serves as the base action for all characterization related action
 * classes. It includes common operations such as download, updateManufacturers.
 * 
 * @author pansu
 */

/* CVS $Id: BaseCharacterizationAction.java,v 1.1 2006-11-16 16:54:53 pansu Exp $ */

public class BaseCharacterizationAction extends AbstractDispatchAction {
	private static Logger logger = Logger
			.getLogger(BaseCharacterizationAction.class);

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
	public ActionForward updateManufacturers(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;

		HttpSession session = request.getSession();
		/*
		 * SizeBean sizeChar=(SizeBean) theForm.get("achar"); if
		 * (sizeChar.getInstrument() != null) { String type =
		 * sizeChar.getInstrument().getType();
		 * logger.info("***************Action: getting manufacturers for " +
		 * type); InitSessionSetup.getInstance().setManufacturerPerType(session,
		 * sizeChar.getInstrument().getType()); }
		 */
		CharacterizationBean sizeChar = (CharacterizationBean) theForm
				.get("achar");
		if (sizeChar.getInstrument() != null) {
			String type = sizeChar.getInstrument().getType();
			session.setAttribute("selectedInstrumentType", type);
			// logger.info("***************Action: getting manufacturers for " +
			// type);
			InitSessionSetup.getInstance().setManufacturerPerType(session,
					sizeChar.getInstrument().getType());
		}

		return mapping.getInputForward();
	}

	/**
	 * Download action to handle download characterization file
	 * 
	 * @param
	 * @return
	 */
	public ActionForward download(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String fileId = request.getParameter("fileId");
		SubmitNanoparticleService service = new SubmitNanoparticleService();
		CharacterizationFileBean fileBean = service.getFile(fileId);
		String fileRoot = PropertyReader.getProperty(
				CalabConstants.FILEUPLOAD_PROPERTY, "fileRepositoryDir");
		File dFile = new File(fileRoot + File.separator + fileBean.getPath());
		if (dFile.exists()) {
			response.setContentType("application/octet-stream");
			response.setHeader("Content-disposition", "attachment;filename="
					+ fileBean.getName());
			response.setHeader("Cache-Control", "no-cache");

			java.io.InputStream in = new FileInputStream(dFile);
			java.io.OutputStream out = response.getOutputStream();

			byte[] bytes = new byte[32768];

			int numRead = 0;
			while ((numRead = in.read(bytes)) > 0) {
				out.write(bytes, 0, numRead);
			}
			out.close();
		} else {
			throw new Exception("ERROR: file not found.");
		}
		return null;
	}

	public boolean loginRequired() {
		return true;
	}
}
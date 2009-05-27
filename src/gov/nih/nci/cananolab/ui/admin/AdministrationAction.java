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
import gov.nih.nci.cananolab.util.ExportUtils;
import gov.nih.nci.cananolab.util.PropertyReader;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.upload.FormFile;

public class AdministrationAction extends AbstractDispatchAction {
	
	public static final String SITE_LOGO = "siteLogo";
	
	private static Logger logger = 
		Logger.getLogger(AdministrationAction.class);

	/**
	 * Action to show site preference page.
	 *
	 * @param
	 * @return
	 */
	public ActionForward sitePreference(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaActionForm theForm = (DynaActionForm) form;
		String siteName = PropertyReader.getProperty(
				Constants.FILEUPLOAD_PROPERTY, Constants.SITE_NAME);
		theForm.set(Constants.SITE_NAME, siteName);
		
		if (logger.isInfoEnabled()) {
			logger.info("siteName = " + siteName);
		}
		
		return mapping.findForward("sitePreference");
	}

	/**
	 * Action to update site preference settings.
	 *
	 * @param
	 * @return
	 */
	public ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaActionForm theForm = (DynaActionForm) form;
		String siteName = (String) theForm.getString(Constants.SITE_NAME);
		ActionMessages messages = new ActionMessages();
		InputStream in = null;
		OutputStream out = null;
		try {
			Properties props = new Properties();
			in = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(Constants.FILEUPLOAD_PROPERTY);
			props.load(in);
			
			out = new BufferedOutputStream(new FileOutputStream(this.getPropertyFileName()));
			props.setProperty(Constants.SITE_NAME, siteName);
			props.store(out, null);
		} catch (Exception e) {
			String errorMsg = "Property file cannot be updated: " + e.toString();
			this.saveErrorMsg(messages, e, errorMsg);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.flush();
					out.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		FormFile file = (FormFile) theForm.get(SITE_LOGO);
		File siteLogo = new File(this.getSiteLogoName());
		byte[] data = null;
		if (file != null) {
			data = file.getFileData();
		}
		if (data == null || data.length == 0) {
			siteLogo.delete();
		} else {
			if (data.length > Constants.MAX_LOGO_SIZE) {
				String errorMsg = "Size of site logo file cannot be larger than "
						+ Constants.MAX_LOGO_SIZE + " bytes.";
				this.saveErrorMsg(messages, null, errorMsg);
			} else {
		        try {
					out = new BufferedOutputStream(new FileOutputStream(siteLogo));
		            out.write(data);
		        } catch (Exception e) {
					String errorMsg = "Site logo file cannot be saved: " + e.toString();
					this.saveErrorMsg(messages, e, errorMsg);
		        } finally {
		            //Close the BufferedOutputStream
		            try {
		                if (out != null) {
		                    out.flush();
		                    out.close();
		                }
		            } catch (IOException e) {
		            }
		        }		
			}
		}
		
		// Set error message in request if it is not empty.
		if (messages.size() > 0) {
			saveMessages(request, messages);
		}
		
		return mapping.findForward("success");
	}

	/**
	 * Download action to handle file downloading and viewing
	 *
	 * @param
	 * @return
	 */
	public ActionForward siteLogo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		File siteLogo = new File(this.getSiteLogoName());
		if (siteLogo.exists() && siteLogo.length() > 0) {
			response.setContentType(ExportUtils.IMAGE_CONTENT_TYPE);
			response.setHeader(ExportUtils.CONTENT_DISPOSITION, ExportUtils.ATTACHMENT
					+ Constants.SITE_LOGO_FILENAME + "\"");
			response.setHeader(ExportUtils.CACHE_CONTROL, ExportUtils.PRIVATE);

			InputStream in = new BufferedInputStream(new FileInputStream(siteLogo));
			OutputStream out = response.getOutputStream();

			byte[] bytes = new byte[Constants.MAX_LOGO_SIZE];
			int numRead = 0;
			try {
				while ((numRead = in.read(bytes)) > 0) {
					out.write(bytes, 0, numRead);
				}
			} finally {
				try {
					out.close();
				} catch (Exception e) {
				}
			}
		}
		return null;
	}

	/**
	 * Get the file name of site log.
	 *
	 * @return file name of site logo.
	 */
	private String getSiteLogoName() {
		StringBuilder sb = new StringBuilder();
		String fileRoot = PropertyReader.getProperty(
				Constants.FILEUPLOAD_PROPERTY, Constants.FILE_REPOSITORY_DIR);
		sb.append(fileRoot).append(File.separator).append(Constants.SITE_LOGO_FILENAME);
		
		return sb.toString();
	}
	
	/**
	 * Get the file name of caNanolab property file.
	 *
	 * @return file name of caNanolab property file.
	 */
	private String getPropertyFileName() {
		StringBuilder sb = new StringBuilder();
		String path = Thread.currentThread().getContextClassLoader().getResource(
					Constants.FILEUPLOAD_PROPERTY).getPath();
		sb.append(path);
		sb.deleteCharAt(0);  // First char is a forward slash, remove it.
		
		return sb.toString();
	}
	
	/**
	 * Save error message and log exception.
	 *
	 * @param messages
	 * @param e
	 * @param errorMsg
	 */
	private void saveErrorMsg(ActionMessages messages, Throwable e, String errorMsg) {
		if (logger.isInfoEnabled()) {
			logger.info(errorMsg, e);
		}
		ActionMessage msg = new ActionMessage(errorMsg);
		messages.add(ActionMessages.GLOBAL_MESSAGE, msg);
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

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
 * Action class for Administration section.
 * 
 * @author houyh
 */
import gov.nih.nci.cananolab.dto.admin.SitePreferenceBean;
import gov.nih.nci.cananolab.dto.admin.VisitorCountBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.SecurityException;
import gov.nih.nci.cananolab.service.admin.AdminService;
import gov.nih.nci.cananolab.ui.core.AbstractDispatchAction;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.DateUtils;
import gov.nih.nci.cananolab.util.ExportUtils;
import gov.nih.nci.cananolab.util.PropertyUtils;
import gov.nih.nci.cananolab.util.StringUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.upload.FormFile;

public class AdministrationAction extends AbstractDispatchAction {
	private static Logger logger = Logger.getLogger(AdministrationAction.class);

	private AdminService adminService;

	/**
	 * Action to show site preference page.
	 */
	public ActionForward setupNew(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		SitePreferenceBean siteBean = null;
		siteBean = this.getSiteBean();
		if (siteBean != null) {
			DynaActionForm theForm = (DynaActionForm) form;
			theForm.set(Constants.SITE_NAME, siteBean.getSiteName());
		}
		return mapping.getInputForward();
	}

	/**
	 * Action to clear site preferences: remove site and site logo.
	 * 
	 * @param
	 * @return
	 */
	public ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		ActionMessages messages = new ActionMessages();
		SitePreferenceBean siteBean = null;
		siteBean = this.getNewSiteBean(user, null, null);
		if (this.saveSiteBean(user, siteBean) > 0) {
			// set success message in request if everything is fine now.
			ActionMessage msg = new ActionMessage(
					"admin.sitePreference.success");
			messages.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveMessages(request, messages);
		} else {
			ActionMessage msg = new ActionMessage("admin.sitePreference.error");
			messages.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveErrors(request, messages);
		}
		return mapping.getInputForward();
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
		SitePreferenceBean siteBean = null;
		ActionMessages messages = new ActionMessages();
		DynaActionForm theForm = (DynaActionForm) form;
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		String siteName = (String) theForm.getString(Constants.SITE_NAME);
		FormFile file = (FormFile) theForm.get(Constants.SITE_LOGO);

		// 1.check if uploaded file is empty first.
		if (file == null || file.getFileSize() == 0) {
			siteBean = this.getNewSiteBean(user, siteName, null);
		} else {
			// 2.do nothing if uploaded file is too large, set error msg.
			if (file.getFileSize() > Constants.MAX_LOGO_SIZE) {
				ActionMessage msg = new ActionMessage(
						"admin.sitePreference.error.logoTooLarge",
						Constants.MAX_LOGO_SIZE);
				messages.add(ActionMessages.GLOBAL_MESSAGE, msg);
			} else {
				// 3.save uploaded file first, then update database.
				StringBuilder sb = new StringBuilder();
				String logoFilename = this.getNewLogoFileName(file
						.getFileName());
				String fileRoot = PropertyUtils.getProperty(
						Constants.CANANOLAB_PROPERTY,
						Constants.FILE_REPOSITORY_DIR);
				sb.append(fileRoot).append(File.separator);
				sb.append(logoFilename);

				OutputStream out = null;
				try {
					out = new BufferedOutputStream(new FileOutputStream(sb
							.toString()));
					out.write(file.getFileData());
					siteBean = this
							.getNewSiteBean(user, siteName, logoFilename);
				} catch (Exception e) {
					ActionMessage msg = new ActionMessage(
							"admin.sitePreference.error.savingLogofile");
					messages.add(ActionMessages.GLOBAL_MESSAGE, msg);
				} finally {
					// Close the BufferedOutputStream
					if (out != null) {
						try {
							out.flush();
							out.close();
						} catch (IOException e) {
						}
					}
				}
			}
		}
		// 4.update database record.
		if (siteBean != null) {
			if (this.saveSiteBean(user, siteBean) <= 0) {
				ActionMessage msg = new ActionMessage(
						"admin.sitePreference.error");
				messages.add(ActionMessages.GLOBAL_MESSAGE, msg);
			}
		}
		// 5.set success message in request if everything is fine now.
		if (messages.isEmpty()) {
			ActionMessage msg = new ActionMessage(
					"admin.sitePreference.success");
			messages.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveMessages(request, messages);
		} else {
			saveErrors(request, messages);
		}
		return mapping.getInputForward();
	}

	/**
	 * Action for loading site preferences for cananoHeader.jsp.
	 * 
	 * @param
	 * @return
	 */
	public ActionForward getCaNanoHeader(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		SitePreferenceBean siteBean = this.getSiteBean();
		if (siteBean != null) {
			if (!StringUtils.isEmpty(siteBean.getSiteName())) {
				request.setAttribute(Constants.SITE_NAME, siteBean
						.getSiteName());
			}
			if (!StringUtils.isEmpty(siteBean.getSiteLogoFilename())) {
				request.setAttribute("hasSiteLogo", Boolean.TRUE);
			}
		}
		return mapping.findForward("cananoHeader");
	}

	/**
	 * Action for loading Visitor Counter for cananoSidemenu.jsp.
	 * 
	 * @param
	 * @return
	 */
	public ActionForward getCaNanoSidemenu(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 1.load counter from application scope, set it if not exists.
		HttpSession session = request.getSession();
		ServletContext context = session.getServletContext();
		Integer counter = (Integer) context.getAttribute("visitorCounter");
		if (counter == null) {
			VisitorCountBean counterBean = this.adminService.getVisitorCount();
			counter = counterBean.getVisitorCount();
			context.setAttribute("visitorCount", counter);
			context.setAttribute("countString", counter.toString());
			context.setAttribute("counterStartDate", DateUtils
					.convertDateToString(counterBean.getCounterStartDate(),
							Constants.DATE_FORMAT));
		}
		// 2.increase counter if requester's IP is not in session.
		String visitorIP = (String) session.getAttribute("visitorIP");
		if ((visitorIP == null || !visitorIP.equals(request.getRemoteAddr()))
				&& request.getAttribute("justLogout") == null) {
			session.setAttribute("visitorIP", request.getRemoteAddr());
			context.setAttribute("visitorCount", ++counter);
			context.setAttribute("countString", counter.toString());
			this.adminService.increaseVisitorCount();
		}
		request.setAttribute("showVisitorCount", Boolean.TRUE);

		return mapping.findForward("cananoSidemenu");
	}

	/**
	 * Action to handle site logo file download request.
	 * 
	 * @param
	 * @return
	 */
	public ActionForward getSiteLogo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		SitePreferenceBean siteBean = this.getSiteBean();
		if (siteBean != null
				&& !StringUtils.isEmpty(siteBean.getSiteLogoFilename())) {
			// 1.compose logo's full file name.
			StringBuilder sb = new StringBuilder();
			String fileRoot = PropertyUtils
					.getProperty(Constants.CANANOLAB_PROPERTY,
							Constants.FILE_REPOSITORY_DIR);
			sb.append(fileRoot).append(File.separator).append(
					siteBean.getSiteLogoFilename());
			// 2.load logo file from file system.
			File siteLogo = new File(sb.toString());
			if (siteLogo.exists() && siteLogo.length() > 0) {
				ExportUtils.prepareReponseForImage(response,
						Constants.SITE_LOGO_FILENAME);
				int numRead = 0;
				InputStream in = null;
				OutputStream out = null;
				byte[] bytes = new byte[Constants.MAX_LOGO_SIZE];
				try {
					in = new BufferedInputStream(new FileInputStream(siteLogo));
					out = response.getOutputStream();
					while ((numRead = in.read(bytes)) > 0) {
						out.write(bytes, 0, numRead);
					}
				} finally {
					if (in != null) { // Close the InputStream
						try {
							in.close();
						} catch (IOException e) {
						}
					}
					if (out != null) {// Close the OutputStream
						try {
							out.flush();
							out.close();
						} catch (IOException e) {
						}
					}
				}
			} else {
				if (logger.isInfoEnabled()) {
					logger.info("Missing site logo file: " + sb);
				}
			}
		}
		return null;
	}

	/**
	 * Retrieve site preference bean from database.
	 * 
	 * @return SitePreferenceBean.
	 */
	private SitePreferenceBean getSiteBean() {
		SitePreferenceBean siteBean = null;
		try {
			siteBean = this.adminService.getSitePreference();
			if (siteBean == null && logger.isDebugEnabled()) {
				logger.debug("Error retrieving site preference, got null.");
			}
		} catch (Exception e) {
			if (logger.isInfoEnabled()) {
				logger.info("Error retrieving site preference.", e);
			}
		}
		return siteBean;
	}

	/**
	 * Save site preference bean to database.
	 * 
	 * @param user
	 * @return SitePreferenceBean.
	 */
	private int saveSiteBean(UserBean user, SitePreferenceBean siteBean) {
		int rowUpdated = 0;
		try {
			rowUpdated = this.adminService.updateSitePreference(siteBean, user);
			if (rowUpdated == 0 && logger.isDebugEnabled()) {
				logger.debug("Error updating site preference, got 0.");
			}
		} catch (Exception e) {
			if (logger.isInfoEnabled()) {
				logger.info("Error updating site preference.", e);
			}
		}
		return rowUpdated;
	}

	/**
	 * Create a new site preference bean for save/update.
	 * 
	 * @param user
	 * @return SitePreferenceBean.
	 */
	private SitePreferenceBean getNewSiteBean(UserBean user, String siteName,
			String siteLogo) {
		SitePreferenceBean siteBean = new SitePreferenceBean();

		siteBean.setSiteLogoFilename(siteLogo);
		siteBean.setSiteName(siteName);
		siteBean.setUpdatedBy(user.getLoginName());
		siteBean.setUpdatedDate(Calendar.getInstance().getTime());

		return siteBean;
	}

	/**
	 * Get an unique file name for logo in format of "fileName_{timestamp}.ext".
	 * 
	 * @param fileName
	 * @return an unique file name of site logo.
	 */
	private String getNewLogoFileName(String fileName) {
		Date now = Calendar.getInstance().getTime();
		StringBuilder sb = new StringBuilder();
		int index = fileName.lastIndexOf('.');
		if (index == -1) {
			sb.append(fileName).append('_').append(now.getTime());
		} else {
			sb.append(fileName.substring(0, index));
			sb.append('_').append(now.getTime());
			sb.append(fileName.substring(index));
		}
		return sb.toString();
	}

	public Boolean canUserExecutePrivateDispatch(UserBean user)
			throws SecurityException {
		return InitSecuritySetup.getInstance().userHasAdminPrivilege(user);
	}

	/**
	 * Getter for Srping dependency injection.
	 * 
	 * @return
	 */
	public AdminService getAdminService() {
		return adminService;
	}

	/**
	 * Setter for Srping dependency injection.
	 * 
	 * @param adminService
	 */
	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}
}

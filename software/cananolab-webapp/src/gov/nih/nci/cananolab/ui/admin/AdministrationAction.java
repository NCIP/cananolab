/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.ui.admin;

/**
 * Action class for Administration section.
 *
 * @author houyh, pansu
 */
import gov.nih.nci.cananolab.dto.admin.SitePreferenceBean;
import gov.nih.nci.cananolab.dto.admin.VisitorCountBean;
import gov.nih.nci.cananolab.exception.SecurityException;
import gov.nih.nci.cananolab.service.admin.AdminService;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.ui.core.AbstractDispatchAction;
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

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.DynaValidatorForm;

public class AdministrationAction extends AbstractDispatchAction {
	private static Logger logger = Logger.getLogger(AdministrationAction.class);

	private AdminService adminService;

	/**
	 * Action to show site preference page.
	 */
	public ActionForward setupNew(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		/* populate the form with data from the session */
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		SitePreferenceBean siteBean = this.getNewSiteBean(user, null, null);
		theForm.set("sitePreference", siteBean);
		request.getSession().setAttribute("existingSiteBean", siteBean);
		saveToken(request);
		return mapping.findForward("createInput");
	}

	/**
	 * Action to show site preference page.
	 */
	public ActionForward setupUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		/* populate the form with data from the session */
		SitePreferenceBean sitePreference = (SitePreferenceBean) request
				.getSession().getAttribute("existingSiteBean");
		if (sitePreference != null) {
			theForm.set("sitePreference", sitePreference);
		}
		saveToken(request);
		return mapping.findForward("createInput");
	}

	public ActionForward input(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		SitePreferenceBean sitePreference = (SitePreferenceBean) theForm
				.get("sitePreference");
		// escape XML for site logo file name for security reasons
		if (StringUtils.isEmpty(sitePreference.getSiteLogoFilename())) {
			StringEscapeUtils.escapeXml(sitePreference.getSiteLogoFilename());
		}
		return mapping.findForward("createInput");
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
		if (!validateToken(request)) {
			return mapping.findForward("createInput");
		}
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		ActionMessages messages = new ActionMessages();
		SitePreferenceBean siteBean = null;
		siteBean = this.getNewSiteBean(user, null, null);
		if (this.saveSiteBean(user, siteBean) > 0) {
			// set success message in request if everything is fine now.
			ActionMessage msg = new ActionMessage(
					"admin.sitePreference.delete.success");
			messages.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveMessages(request, messages);
		} else {
			ActionMessage msg = new ActionMessage("admin.sitePreference.error");
			messages.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveErrors(request, messages);
		}
		request.getSession().removeAttribute("existingSiteBean");
		resetToken(request);
		return mapping.findForward("new");
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
		if (!validateToken(request)) {
			return mapping.findForward("createInput");
		}
		SitePreferenceBean siteBean = null;
		ActionMessages messages = new ActionMessages();
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		SitePreferenceBean sitePreference = (SitePreferenceBean) theForm
				.get("sitePreference");
		String siteName = sitePreference.getSiteName();
		FormFile file = sitePreference.getSiteLogoFile().getUploadedFile();
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
					out = new BufferedOutputStream(new FileOutputStream(
							sb.toString()));
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
		request.getSession().setAttribute("existingSiteBean", siteBean);
		resetToken(request);
		return mapping.findForward("update");
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
		/* retrieve from database if session attribute existingSiteBean is null */
		if (request.getSession().getAttribute("existingSiteBean") == null) {
			SitePreferenceBean siteBean = this.retrieveExistingSitePreference();
			request.getSession().setAttribute("existingSiteBean", siteBean);
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
		// retrieve from session
		SitePreferenceBean siteBean = (SitePreferenceBean) request.getSession()
				.getAttribute("existingSiteBean");
		if (siteBean != null
				&& !StringUtils.isEmpty(siteBean.getSiteLogoFilename())) {
			// 1.compose logo's full file name.
			StringBuilder sb = new StringBuilder();
			String fileRoot = PropertyUtils
					.getProperty(Constants.CANANOLAB_PROPERTY,
							Constants.FILE_REPOSITORY_DIR);
			sb.append(fileRoot).append(File.separator)
					.append(siteBean.getSiteLogoFilename());
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
	private SitePreferenceBean retrieveExistingSitePreference() {
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
		return user.isAdmin();
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

	public Boolean canUserExecutePrivateDispatch(UserBean user,
			String protectedData) throws SecurityException {
		if (user == null) {
			return false;
		}
		return true;
	}
}

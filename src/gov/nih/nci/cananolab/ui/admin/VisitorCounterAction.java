/*
 The caNanoLab Software License, Version 1.4

 Copyright 2006 SAIC. This software was developed in conjunction with the National
 Cancer Institute, and so to the extent government employees are co-authors, any
 rights in such works shall be subject to Title 17 of the United States Code,
 section 105.

 */
package gov.nih.nci.cananolab.ui.admin;

/**
 * This class implements FR# 26489, Vistor Counter.
 *
 * @author houyh
 */
import gov.nih.nci.cananolab.dto.admin.VisitorCountBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.SecurityException;
import gov.nih.nci.cananolab.service.admin.AdminService;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.DateUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.web.struts.ActionSupport;

public class VisitorCounterAction extends ActionSupport {
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		//1.load counter from application scope, set it if not exists.
		HttpSession session = request.getSession();
		ServletContext context = session.getServletContext();
		Integer counter = (Integer) context.getAttribute("visitorCounter");
		if (counter == null) {
			AdminService adminService = (AdminService) this
					.getWebApplicationContext().getBean("adminServiceDAO");
			VisitorCountBean counterBean = adminService.getVisitorCount();
			counter = counterBean.getVisitorCount();
			context.setAttribute("visitorCount", counter);
			context.setAttribute("counterStartDate", 
				DateUtils.convertDateToString(counterBean.getCounterStartDate(),
							Constants.DATE_FORMAT));
		}
		//2.increase counter if requester's IP is not in session.
		String visitorIP = (String) session.getAttribute("visitorIP");
		if (visitorIP == null || !visitorIP.equals(request.getRemoteAddr())) {
			session.setAttribute("visitorIP", request.getRemoteAddr());
			context.setAttribute("visitorCount", ++counter);
			
			AdminService adminService = (AdminService) this
				.getWebApplicationContext().getBean("adminServiceDAO");
			adminService.increaseVisitorCount();
		}
		
		return mapping.findForward("welcomePage");
	}

	public boolean loginRequired() {
		return false;
	}

	public boolean canUserExecute(UserBean user)
			throws SecurityException {
		return true;
	}
}

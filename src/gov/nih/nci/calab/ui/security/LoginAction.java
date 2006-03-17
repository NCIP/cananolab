package gov.nih.nci.calab.ui.security;

import org.apache.log4j.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
//import org.apache.struts.validator.DynaValidatorActionForm;
import org.apache.struts.validator.DynaValidatorForm;

import gov.nih.nci.calab.service.login.*; 
import gov.nih.nci.calab.ui.core.*;

public class LoginAction extends AbstractBaseAction {
	private static Logger logger = Logger.getLogger(LoginAction.class);

	public ActionForward executeTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		try 
		{
			// TODO fill in details for sample information */
			DynaValidatorForm theForm = (DynaValidatorForm) form;
			String loginId = (String) theForm.get("loginId");
			String password = (String) theForm.get("password");
			
			if (loginId == null || password == null)
			{
				//TODO provide user with the exact error.
				mapping.findForward("failure");
			}
			else
			{
				//TODO turn on password encryption. 
				//PasswordService passwordservice = new PasswordService();
				//password = passwordservice.encrypt(password);
				
				LoginService loginservice = new LoginService("caLAB");
			    Boolean authenticated = loginservice.login(loginId, password);
			    if (authenticated == true)
			    {		    	
			    	//request.setAttribute("loginId", loginId);
					//request.setAttribute("password", password);
			    	
			    	//Invalidate the current session and create a new one.
			    	HttpSession session = request.getSession(false);
			    	session.invalidate();
			    	session = request.getSession(true);
			    	session.setAttribute("loginId",loginId);
			    	session.setAttribute("password",password);
			    	
					forward = mapping.findForward("success");
			    }
			}
			
		} catch (Exception e) {
			logger.error("Caught exception when authenticating the user", e);
			forward = mapping.findForward("failure");			
		}
		return forward;
	}

	public boolean loginRequired() 
	{
		// temporarily set to false until login module is working
		return false;
		// return true;
	}
	
	}


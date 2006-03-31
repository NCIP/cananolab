package gov.nih.nci.calab.ui.security;

import org.apache.log4j.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import org.apache.struts.validator.DynaValidatorForm;

import gov.nih.nci.calab.dto.security.SecurityBean;
import gov.nih.nci.calab.service.login.*; 
import gov.nih.nci.calab.ui.core.*;

/** 
* The LoginAction authenticates a user into the caLAB system.
* 
* @author      doswellj 
*/

public class LoginAction extends AbstractBaseAction 
{
	private static Logger logger = Logger.getLogger(LoginAction.class);

	public ActionForward executeTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		ActionForward forward = null;
		ActionMessages msgs = new ActionMessages();
		HttpSession session = request.getSession();
		try 
		{
			DynaValidatorForm theForm = (DynaValidatorForm) form;
			String strLoginId = (String) theForm.get("loginId");
			String strPassword = (String) theForm.get("password");

			//Encrypt the password. 
			String strEncryptedPass = PasswordService.getInstance().encrypt(strPassword);
			
			//Call CSM to authenticate the user.
			LoginService loginservice = new LoginService("calab");
			Boolean blnAuthenticated = loginservice.login(strLoginId, strEncryptedPass);
			if (blnAuthenticated  == true)
			{		    	
			    //Invalidate the current session and create a new one.
			    session = request.getSession(false);
			    session.invalidate();
			    session = request.getSession(true);
			    
			    //Save authenticated user information into User DTO.
			    SecurityBean securityBean = new SecurityBean();
			    securityBean.setLoginId(strLoginId);
			    securityBean.setPassword(strEncryptedPass);
			    
			    session.setAttribute("user",securityBean);	
				forward = mapping.findForward("success");
			 }
			 
		} catch (Exception e) 
		{
			ActionMessage error = new ActionMessage("error.login");
	        msgs.add("error", error);
	        saveMessages(request, msgs);
			logger.error("Error Authenticating the user", e);
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


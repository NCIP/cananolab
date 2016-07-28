package gov.nih.nci.cananolab.security.utils;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class LoginFailureHandler implements AuthenticationFailureHandler
{
	protected Logger logger = Logger.getLogger(LoginFailureHandler.class);

	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, 
										AuthenticationException ae) throws IOException, ServletException
	{
		logger.error("Authentication failed for user: " + request.getParameter("username"), ae);
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		OutputStream out = response.getOutputStream();
		//out.write("Invalid login attempt. Please try again.".getBytes());
		out.write(ae.getMessage().getBytes());
	}

}

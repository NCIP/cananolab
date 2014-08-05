package gov.nih.nci.cananolab.restful.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import gov.nih.nci.cananolab.service.security.UserBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.Test;

public class SecurityUtilTest {

	@Test
	public void testIsUserLoggedIn() {
		
		boolean loggedin = SecurityUtil.isUserLoggedIn(null);
		assertFalse(loggedin);
				
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpSession session = mock(HttpSession.class);
		
		when(request.getSession()).thenReturn(null);
		loggedin = SecurityUtil.isUserLoggedIn(request);
		assertFalse(loggedin);
		
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(null);
		loggedin = SecurityUtil.isUserLoggedIn(request);
		assertFalse(loggedin);
		
		UserBean user = new UserBean();
		when(session.getAttribute("user")).thenReturn(user);
		loggedin = SecurityUtil.isUserLoggedIn(request);
		assertFalse(loggedin);
		
		user.setUserId("someid");
		when(session.getAttribute("user")).thenReturn(user);
		loggedin = SecurityUtil.isUserLoggedIn(request);
		assertTrue(loggedin);
		
	}

}

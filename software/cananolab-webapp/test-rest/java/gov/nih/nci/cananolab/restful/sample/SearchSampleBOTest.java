package gov.nih.nci.cananolab.restful.sample;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SearchSampleBOTest {
	
	@Mock
	private HttpServletRequest request;
	
	@Mock
	private HttpSession session;
	
	@Before
	public void before() throws ServletException {
	 final ServletConfig servletConfig = mock(ServletConfig.class);
	 final ServletContext servletContext = mock(ServletContext.class);

	 when(servletConfig.getServletContext()).thenReturn(servletContext);
	 when(request.getSession()).thenReturn(session);
	 //request.getSession().getServletContext();
	 when(session.getServletContext()).thenReturn(servletContext);

	 //this.servlet = new EmailListServlet();
	 //servlet.init(servletConfig);
	}

	

	@After
	public void tearDown() throws Exception {
	}

	//@Test
	public void testSearch() {
		fail("Not yet implemented");
	}

	//@Test
	public void testSetup() {
		SearchSampleBO searchbo = new SearchSampleBO();
		
		try {
			Map<String, List<String>> map = searchbo.setup(request);
			
			assertNotNull(map);
		} catch (Exception e) {
			
		}
	}

}

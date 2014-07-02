package gov.nih.nci.cananolab.restful.sample;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SearchSampleBOTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSearch() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetup() {
		SearchSampleBO searchbo = new SearchSampleBO();
		
		HttpSession session = mock(HttpSession.class);
		HttpServletRequest request = mock(HttpServletRequest.class);
		ServletContext context = mock(ServletContext.class);
		
		when(request.getSession()).thenReturn(session);
		when(session.getServletContext()).thenReturn(context);
		try {
			Map<String, List<String>> map = searchbo.setup(request);
			
			assertNotNull(map);
		} catch (Exception e) {
			
		}
	}

}

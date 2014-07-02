package gov.nih.nci.cananolab.restful;

import static org.junit.Assert.*;

import java.net.URI;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath:/applicationContext.xml"})
public class CoreServicesTest extends JerseyTest {
	
	String urlbase = "http://localhost:8080/caNanoLab/rest/";
	

	
	@Override
	protected Application configure() {
		// TODO Auto-generated method stub
		return new ResourceConfig(CoreServices.class);
	}

	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testInitSetup() {
		WebTarget wt = this.target(urlbase + "core/initSetup");
		//wt.request().get(String.class);
	}

}

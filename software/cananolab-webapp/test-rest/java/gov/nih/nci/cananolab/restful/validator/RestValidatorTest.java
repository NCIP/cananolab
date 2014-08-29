package gov.nih.nci.cananolab.restful.validator;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.cananolab.restful.view.edit.SimpleAddressBean;
import gov.nih.nci.cananolab.restful.view.edit.SimpleOrganizationBean;
import gov.nih.nci.cananolab.restful.view.edit.SimplePointOfContactBean;

import java.util.List;

import org.junit.Test;

public class RestValidatorTest {

	//@Test
	public void testValidate1LevelObject() {
		SimpleAddressBean address = new SimpleAddressBean();
		address.setZip(null);
		
		List<String> errors = RestValidator.validate(address);
		
		assertNotNull(errors);
		assertTrue(errors.size() == 0);
		
		address.setZip("");
		
		errors = RestValidator.validate(address);
		
		assertNotNull(errors);
		assertTrue(errors.size() == 0);
		
		
		address.setZip("fasfal#&*");
		address.setLine1("lsfhaease\n");
		address.setStateProvince("789");
		
		errors = RestValidator.validate(address);
		
		assertNotNull(errors);
		assertTrue(errors.size() >  0);
		
//		for (String error : errors)
//			System.out.println(errors.get(0));
	}

	//@Test
	public void testValidate2LevelObject() {
		SimplePointOfContactBean pocBean = new SimplePointOfContactBean();
		
		pocBean.setFirstName("lslfaj453");
		pocBean.setLastName("sfalsflafa");
		pocBean.setRole("read+write");
		
		SimpleAddressBean address = new SimpleAddressBean();
		address.setLine1("90877 Shady Grove Rd.");
		address.setCountry("lsfhael*&'");
		pocBean.setAddress(address);
		
		SimpleOrganizationBean orgBean = new SimpleOrganizationBean();
		orgBean.setName("&$\nisfa22");
		
		pocBean.setOrganization(orgBean);
		
		List<String> errors = RestValidator.validate(pocBean);
		assertTrue(errors.size() > 0);
	}
}
